package com.base.base.param;


import java.io.Serializable;

/**
 * @ClassName BaseRequestParam
 * @Description
 * @Author liwd
 * @Date 2019/07/10
 */
public class BaseRequestParam<T> implements Serializable {

    private static final long serialVersionUID = -8984597410657442040L;
    private String k;
    private int pageNum;
    private int pageSize;
    private String version;
    private T data;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
