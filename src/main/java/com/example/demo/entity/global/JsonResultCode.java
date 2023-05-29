package com.example.demo.entity.global;


/**
 * Json返回结果类别 枚举(参考HTTP状态码)
 * 110-199请求类问题、200成功、210-299其他问题、400-499请求错误、510-599内部错误问题
 *
 * @author song
 */
public enum JsonResultCode {
    //未知初始错误
    fail(0),
    //参数错误
    format_invalid(101),
    //当前请求未通过验证
    unauthorized(401),
    //拒绝执行(没有权限操作)
    forbidden(403),
    //对象未找到
    not_found(404),
    //请求服务错误
    service_error(500),

    success(200),
    warning(210);


    private final int code;

    JsonResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}