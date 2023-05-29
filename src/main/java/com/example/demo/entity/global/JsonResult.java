package com.example.demo.entity.global;


import cn.hutool.core.util.IdUtil;

/**
 * 通用的Json返回
 *
 * @author song
 */
public class JsonResult implements IResult {
    public int code;
    public String message;
    public Object data;
    public String requestId;

    private JsonResult(JsonResultCode resultCode, String message, Object data, String requestId) {
        this.code = resultCode.getCode();
        this.data = data;
        this.message = message;
        if (requestId == null || requestId == "")
            requestId = IdUtil.simpleUUID();
        this.requestId = requestId;
    }

    private JsonResult(int resultCode, String message, Object data, String requestId) {
        this.code = resultCode;
        this.data = data;
        this.message = message;
        if (requestId == null || requestId == "")
            requestId = IdUtil.simpleUUID();
        this.requestId = requestId;
    }

    public JsonResult() {
        this.code = JsonResultCode.fail.getCode();
        this.data = data;
        this.message = message;
        this.requestId = IdUtil.simpleUUID();
    }

    public static IResult success(String message) {
        return new JsonResult(JsonResultCode.success, message, null, "");
    }

    public static IResult success(String message, Object data) {
        return new JsonResult(JsonResultCode.success, message, data, "");
    }

    public static IResult success(int resultCode, String message, Object data) {
        return new JsonResult(resultCode, message, data, "");
    }

    public static IResult error(String message) {
        return new JsonResult(JsonResultCode.fail, message, null, "");
    }

    public static IResult error(JsonResultCode resultCode, String message, Object data) {
        return new JsonResult(resultCode, message, data, "");
    }

    public static IResult error(int resultCode, String message, Object data) {
        return new JsonResult(resultCode, message, data, "");
    }

    public static IResult error(String message, Object data) {
        return JsonResult.error(JsonResultCode.fail, message, data);
    }
}

