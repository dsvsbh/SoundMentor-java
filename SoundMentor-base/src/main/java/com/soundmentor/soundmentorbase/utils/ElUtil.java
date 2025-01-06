package com.soundmentor.soundmentorbase.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * El表达式解析工具
 * @Author: Make
 * @DATE: 2025/01/06
 **/
public class ElUtil {
    private static final char LEFT_ANGLE_BRACKET = '<';
    private static final char RIGHT_ANGLE_BRACKET = '>';
    private static final Pattern EL_REG_EX = Pattern.compile(".*\\{#.*\\}$"), CONTAIN_EL_REG_EX = Pattern.compile(".*\\{#.*\\}.*$");

    /**
     * 构建kylin 发送模板的bizParams
     *
     * @param elObjs      el obj
     * @param templateStr 模板str
     * @return {@link Map}<{@link String}, {@link String}>
     */
    public static Map<String, String> buildKylinBizParam(Map<String, Object> elObjs,
                                                         String templateStr) {
        List<String> templatePlaceholder = getELStringPlaceholder(templateStr, '%', '%');
        Map<String, String> map = new HashMap<>(templatePlaceholder.size());
        if (CollUtil.isNotEmpty(templatePlaceholder)) {
            for (String s : templatePlaceholder) {
                map.put(s, parseExpression("#" + s, elObjs));
            }
        }
        return map;
    }

    /**
     * 是 el 字符串
     *
     * @param expressionString 表达式字符串
     * @return {@link Boolean}
     */
    public static Boolean isElString(String expressionString) {
        return EL_REG_EX.matcher(expressionString).matches();
    }

    /**
     * 包含 el 字符串
     *
     * @param expressionString 表达式字符串
     * @return {@link Boolean}
     */
    public static Boolean containElString(String expressionString) {
        return CONTAIN_EL_REG_EX.matcher(expressionString).matches();
    }

    /**
     * 替换字符串中的el表达式为实际值
     * ex：
     * 用户 {#webUser.userName} 修改了账号的状态为 {#param.accountEnable} -> 用户 张三 修改了账号的状态为 1
     *
     * @param expressionString 表达式字符串
     * @param data             arg参数
     * @return {@link String}
     */
    public static String replaceElString(String expressionString, Map<String, Object> data) {
        StringBuilder stringBuilder = new StringBuilder(expressionString.length() + 50);

        char[] chars = expressionString.toCharArray();

        boolean flag = false;
        StringBuilder choreBuild = new StringBuilder();
        for (char c : chars) {
            if (flag) {
                if (c == CharUtil.DELIM_END) {
                    flag = false;
                    stringBuilder.append(parseExpression(choreBuild.toString(), data));
                    choreBuild.delete(0, choreBuild.length());
                } else {
                    choreBuild.append(c);
                }
            } else {
                if (c == CharUtil.DELIM_START) {
                    flag = true;
                } else {
                    stringBuilder.append(c);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取el string的占位符包裹的字符串。排除HTML标签包含的特殊字符
     * 例子：
     * 传入：
     * 用户 {#webUser.userName} 修改了账号的状态为 {#param.accountEnable}, "{","}"
     * 返回:
     * ["#webUser.realName","param.accountEnable"]
     *
     * @param expressionString 表达式字符串
     * @param placeholderStart 占位符开始
     * @param placeholderEnd   占位符结束
     * @return {@link List<String>}
     */
    public static List<String> getELStringPlaceholder(String expressionString,
                                                      char placeholderStart,
                                                      char placeholderEnd) {
        List<String> result = new LinkedList<>();

        char[] chars = expressionString.toCharArray();
        boolean flag = false;
        boolean isHtml = false;
        StringBuilder choreBuild = new StringBuilder();
        for (char c : chars) {
            isHtml = isHtmlTag(c, isHtml);
            if (isHtml) {
                if (c == RIGHT_ANGLE_BRACKET) {
                    isHtml = false;
                }
                continue;
            }
            if (flag) {
                if (c == placeholderEnd) {
                    flag = false;
                    result.add(choreBuild.toString());
                    choreBuild.delete(0, choreBuild.length());
                } else {
                    choreBuild.append(c);
                }
            } else {
                if (c == placeholderStart) {
                    flag = true;
                }
            }
        }
        return result;
    }

    /**
     * 判断是否是html标签中的字符
     *
     * @param c      c字符
     * @param isHtml 是否html
     * @return boolean
     */
    private static boolean isHtmlTag(char c, boolean isHtml) {
        //判断是否是html < 或者是<后的字符
        if (c == LEFT_ANGLE_BRACKET) {
            return true;
        } else if (c == RIGHT_ANGLE_BRACKET) {
            return true;
        } else {
            return isHtml;
        }
    }

    /**
     * el表达式解析
     *
     * @param expressionString el表达式
     * @param args             用于el表达式解析的参数对象
     * @param argsNames        用于el表达式解析的参数对象名称
     * @return 加密后字符串
     */
    public static String parseExpression(String expressionString, Object[] args, String[] argsNames) {
        return parseExpression(expressionString, args, argsNames, String.class);
    }

    /**
     * el表达式解析
     *
     * @param expressionString el表达式
     * @param data             用于el表达式解析的参数对象 用于el表达式解析的参数对象名称
     * @return 加密后字符串
     */
    public static String parseExpression(String expressionString, Map<String, Object> data) {
        return parseExpression(expressionString, data, String.class);
    }

    /**
     * el表达式解析
     *
     * @param expressionString el表达式
     * @param args             用于el表达式解析的参数对象
     * @param argsNames        用于el表达式解析的参数对象名称
     * @param clazz            返回的值对象
     * @return 加密后字符串
     */
    private static <T> T parseExpression(String expressionString,
                                         Object[] args,
                                         String[] argsNames,
                                         Class<T> clazz) {
        //SPEL解析
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < argsNames.length; i++) {
            context.setVariable(argsNames[i], args[i]);
        }
        return parser.parseExpression(expressionString).getValue(context, clazz);
    }

    /**
     * el表达式解析
     *
     * @param expressionString el表达式
     * @param data             用于el表达式解析的参数对象 用于el表达式解析的参数对象名称
     * @param clazz            返回的值对象
     * @return 加密后字符串
     */
    private static <T> T parseExpression(String expressionString,
                                         Map<String, Object> data,
                                         Class<T> clazz) {
        //SPEL解析
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(data);
        return parser.parseExpression(expressionString).getValue(context, clazz);
    }
}