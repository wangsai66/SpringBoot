package com.base;

/**
 * @ClassName ResultCode
 * @Description
 * @Author liwd
 * @Date 2019/07/10
 */
public enum ResultCode {

    SUCCESS("0000", "操作成功"),
    FAILED("9999", "操作失败");

    private String code;
    private String msg;

    private ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
