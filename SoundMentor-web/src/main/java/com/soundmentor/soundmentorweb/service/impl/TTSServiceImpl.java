package com.soundmentor.soundmentorweb.service.impl;

import com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.dashscope.utils.Constants;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorweb.config.properties.DashScopeProperties;
import com.soundmentor.soundmentorweb.service.FileService;
import com.soundmentor.soundmentorweb.service.TTSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class TTSServiceImpl implements TTSService {

    private static final String apiUrl = "https://dashscope.aliyuncs.com/api/v1";
    private static final String MODEL = "qwen-tts-latest";
    private static final String LANGUAGE_TYPE = "Auto";

    @Autowired
    private FileService fileService;

    @Autowired
    private DashScopeProperties dashScopeProperties;



    @Override
    public String textToSpeech(AudioParameters.Voice voice, String text, float speed) {
        // 语速在0.5到2.0之间，避免过快或过慢（断言）
        assert speed >= 0.5 && speed <= 2.0 : "语速必须在0.5到2.0之间";

        try {
            // 设置API URL
            Constants.baseHttpApiUrl = apiUrl;

            // 构建请求参数
            MultiModalConversationParam param = MultiModalConversationParam.builder()
                    .apiKey(dashScopeProperties.getApiKey())
                    .model(MODEL)
                    .text(text)
                    .voice(voice)
                    .languageType(LANGUAGE_TYPE)
                    .parameter("instructions", "语速" + (speed > 1 ? "较快" : speed < 1 ? "较慢" : "适中"))
                    .parameter("optimize_instructions", true)
                    .build();

            // 调用API
            MultiModalConversation conv = new MultiModalConversation();
            MultiModalConversationResult result = conv.call(param);

            // 检查API调用是否成功
            if (result == null || result.getOutput() == null || result.getOutput().getAudio() == null) {
                log.error("TTS API调用失败，返回结果为空");
                throw new RuntimeException("TTS API调用失败，返回结果为空");
            }

            // 检查返回的音频URL是否有效
            String audioUrl = result.getOutput().getAudio().getUrl();
            if (audioUrl == null || audioUrl.isEmpty()) {
                log.error("TTS API调用失败，未返回音频URL");
                throw new RuntimeException("TTS API调用失败，未返回音频URL");
            }

            log.info("TTS API返回的音频URL: {}", audioUrl);

            // 下载音频文件（wav格式）
            byte[] wavBytes = downloadAudio(audioUrl);
            if (wavBytes == null || wavBytes.length == 0) {
                log.error("TTS API返回的音频文件下载失败");
                throw new RuntimeException("TTS API返回的音频文件下载失败");
            }

            log.info("WAV音频文件下载成功，大小: {} bytes", wavBytes.length);

            // 计算MD5
            String md5 = calculateMD5(wavBytes);

            // 上传到MinIO
            try (InputStream inputStream = new ByteArrayInputStream(wavBytes)) {
                String minioUrl = fileService.uploadFileToMinio(inputStream, FileTypeEnum.WAV, md5);
                log.info("音频文件上传到MinIO成功，URL: {}", minioUrl);
                return minioUrl;
            }

        } catch (ApiException | NoApiKeyException | UploadFileException e) {
            log.error("调用TTS API失败: {}", e.getMessage(), e);
            throw new RuntimeException("调用TTS API失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("文本转语音失败: {}", e.getMessage(), e);
            throw new RuntimeException("文本转语音失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载音频文件
     */
    private byte[] downloadAudio(String audioUrl) throws IOException {
        try (InputStream in = new URL(audioUrl).openStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        }
    }

    /**
     * 计算MD5
     */
    private String calculateMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}