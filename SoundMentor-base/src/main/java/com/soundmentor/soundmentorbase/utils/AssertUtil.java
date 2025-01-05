package com.soundmentor.soundmentorbase.utils;

import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类
 * @Author: Make
 * @DATE: 2025/01/05
 **/
public class AssertUtil {


    public static void isTrue(boolean expression, String errorMsg) {
        if (!expression) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }

    public static void isTrue(boolean expression, String code, String errorMsg) {
        if (!expression) {
            throw new BizException(code, errorMsg);
        }
    }

    public static void isTrue(boolean expression, BizException bizException) {
        if (!expression) {
            throw bizException;
        }
    }


    public static <T> T notNull(T object, String errorMsg) {
        if (object == null) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        } else {
            return object;
        }
    }

    public static <T> T notNull(T object, String code, String errorMsg) {
        if (object == null) {
            throw new BizException(code, errorMsg);
        } else {
            return object;
        }
    }

    public static void hasLength(String text, String errorMsg) {
        if (!StringUtils.isNotEmpty(text)) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }

    public static void hasLength(String text, String code, String errorMsg) {
        if (!StringUtils.isNotEmpty(text)) {
            throw new BizException(code, errorMsg);
        }
    }


    public static void notEmpty(Object[] array, String errorMsg) {
        if (array == null || array.length == 0) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }

    public static void notEmpty(Object[] array, String code, String errorMsg) {
        if (array == null || array.length == 0) {
            throw new BizException(code, errorMsg);
        }
    }

    public static void notEmpty(Collection collection, String errorMsg) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }

    public static void notEmpty(Collection collection, String code, String errorMsg) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(code, errorMsg);
        }
    }


    public static void notEmpty(Map map, String errorMsg) {
        if (map == null || map.isEmpty()) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }

    public static void notEmpty(Map map, String code, String errorMsg) {
        if (map == null || map.isEmpty()) {
            throw new BizException(code, errorMsg);
        }
    }


    public static void ifNotNull(Object object, String errorMsg) {
        if (object != null) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }


    public static void ifNotNull(Object object, String code, String errorMsg) {
        if (object != null) {
            throw new BizException(code, errorMsg);
        }
    }


    public static void ifNull(Object object, String errorMsg) {
        if (object == null) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }

    public static void ifNull(Object object, String code, String errorMsg) {
        if (object == null) {
            throw new BizException(code, errorMsg);
        }
    }


    public static void ifTrue(boolean expression, String errorMsg) {
        if (expression) {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), errorMsg);
        }
    }


    public static void ifTrue(boolean expression, String code, String errorMsg) {
        if (expression) {
            throw new BizException(code, errorMsg);
        }
    }
}

