package com.soundmentor.soundmentorbase.utils;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.soundmentor.soundmentorbase.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 阿里云通义千问大模型工具类
 *
 * @author Claude
 * @since 2026-03-31
 */
@Slf4j
public class DashScopeUtil {

    private static final String MODEL_NAME = "qwen-turbo";

    /**
     * 生成PPT讲解内容
     *
     * @param apiKey     API密钥
     * @param pptContent PPT页面内容
     * @return 生成的讲解文本
     */
    public static String generatePPTExplanation(String apiKey, String pptContent) {
        if (StringUtils.isBlank(pptContent)) {
            throw new BizException("PPT内容不能为空");
        }

        Generation generation = new Generation();
        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(buildPrompt(pptContent))
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(MODEL_NAME)
                .messages(Arrays.asList(userMessage))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .temperature(0.7f)
                .maxTokens(1000)
                .build();

        try {
            GenerationResult result = generation.call(param);
            if (result == null || result.getOutput() == null ||
                    result.getOutput().getChoices() == null ||
                    result.getOutput().getChoices().isEmpty()) {
                throw new BizException("大模型返回结果为空");
            }

            String explanation = result.getOutput().getChoices().get(0).getMessage().getContent();
            log.info("大模型生成讲解成功，内容长度: {}", explanation.length());
            return explanation;
        } catch (NoApiKeyException e) {
            log.error("API密钥未配置", e);
            throw new BizException("API密钥未配置");
        } catch (ApiException e) {
            log.error("API调用异常, 错误信息: {}", e.getMessage(), e);
            throw new BizException("API调用失败: " + e.getMessage());
        } catch (InputRequiredException e) {
            log.error("缺少必要参数", e);
            throw new BizException("缺少必要参数: " + e.getMessage());
        } catch (Exception e) {
            log.error("生成讲解失败", e);
            throw new BizException("生成讲解失败: " + e.getMessage());
        }
    }

    /**
     * 构建提示词
     *
     * @param pptContent PPT内容
     * @return 提示词
     */
    private static String buildPrompt(String pptContent) {
        return "请根据以下PPT页面内容，生成一段生动、易懂的讲解文本，适合教学使用。" +
                "讲解应该简洁明了，重点突出，时长控制在1分钟左右（约500字）。\n\n" +
                "PPT内容：\n" + pptContent + "\n\n" +
                "请直接返回讲解文本，不需要额外的说明或格式。";
    }

    /**
     * 测试API连接
     *
     * @param apiKey API密钥
     * @return 是否连接成功
     */
    public static boolean testConnection(String apiKey) {
        try {
            String result = generatePPTExplanation(apiKey, "测试内容");
            return StringUtils.isNotBlank(result);
        } catch (Exception e) {
            log.error("测试API连接失败", e);
            return false;
        }
    }
}
