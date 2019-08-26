package com.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @ClassName CommonResult
 * @Description 通用返回
 * @Author liwd
 * @Date 2019/07/10
 */
public class CommonResult<T> implements Serializable {

    @ApiModelProperty("返回码")
    private String code;
    @ApiModelProperty("返回消息")
    private String msg;
    @ApiModelProperty("返回数据")
    private T data;

    public CommonResult() {
    }

    protected CommonResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> ok(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  msg 提示信息
     */
    public static <T> CommonResult<T> ok(T data, String msg) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 失败返回结果
     * @param msg 提示信息
     */
    public static <T> CommonResult<T> error(String msg) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), msg, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> error() {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMsg(), null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //@JsonSerialize(using = DataSerializer.class)
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
