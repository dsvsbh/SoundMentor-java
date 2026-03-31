package com.soundmentor.soundmentorbase.utils;

import com.soundmentor.soundmentorbase.exception.BizException;
import org.junit.jupiter.api.Test;

/**
 * DashScopeUtil 单元测试
 *
 * @author Claude
 * @since 2026-03-31
 */
class DashScopeUtilTest {

    private static final String API_KEY = "sk-835ae53dacb948d1a49a4e8edf098f40"; // TODO: 在此处填入你的 API Key

    @Test
    void testGeneratePPTExplanation() {
        String pptContent = "第一页：人工智能概述\n人工智能（AI）是计算机科学的一个重要分支，致力于创建能够执行通常需要人类智能的任务的系统。";

        try {
            String result = DashScopeUtil.generatePPTExplanation(API_KEY, pptContent);
            System.out.println("========================================");
            System.out.println("生成结果：");
            System.out.println(result);
            System.out.println("========================================");
            System.out.println("生成内容长度：" + result.length() + " 字符");
        } catch (BizException e) {
            System.err.println("调用失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testGeneratePPTExplanationMultiPage() {
        String pptContent = "第一页：机器学习基础\n" +
            "机器学习是人工智能的核心技术之一，通过算法让计算机从数据中学习规律。\n\n" +
            "第二页：深度学习\n" +
            "深度学习是机器学习的子集，使用多层神经网络模拟人脑的学习过程。";

        try {
            String result = DashScopeUtil.generatePPTExplanation(API_KEY, pptContent);
            System.out.println("========================================");
            System.out.println("多页 PPT 生成结果：");
            System.out.println(result);
            System.out.println("========================================");
            System.out.println("生成内容长度：" + result.length() + " 字符");
        } catch (BizException e) {
            System.err.println("调用失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testGeneratePPTExplanationEmptyContent() {
        try {
            DashScopeUtil.generatePPTExplanation(API_KEY, "");
        } catch (BizException e) {
            System.out.println("测试空内容 - 异常信息：" + e.getMessage());
        }
    }

    @Test
    void testTestConnection() {
        boolean result = DashScopeUtil.testConnection(API_KEY);
        System.out.println("API 连接测试结果：" + (result ? "成功" : "失败"));
    }
}
