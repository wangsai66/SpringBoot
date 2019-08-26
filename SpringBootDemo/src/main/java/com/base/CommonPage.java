package com.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @ClassName CommonPage
 * @Description 分页数据返回
 * @Author liwd
 * @Date 2019/07/10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonPage<T> extends CommonResult<List<T>>{

    @ApiModelProperty("当前页")
    private Integer pageNum;
    @ApiModelProperty("分页条数")
    private Integer pageSize;
    @ApiModelProperty("总页数")
    private Integer totalPage;
    @ApiModelProperty("总条数")
    private Long total;

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(PageInfo<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setData(pageInfo.getList());
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        return result;
    }

    /**
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(Page<T> page) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(page.getTotalPages());
        result.setPageNum(page.getNumber());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotalElements());
        result.setData(page.getContent());
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        return result;
    }

    /**
     * 失败返回结果
     * @param msg 提示信息
     */
    public static <T> CommonPage<T> restError(String msg) {
        CommonPage<T> result = new CommonPage<T>();
        result.setCode(ResultCode.FAILED.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonPage<T> restError() {
        CommonPage<T> result = new CommonPage<T>();
        result.setCode(ResultCode.FAILED.getCode());
        result.setMsg(ResultCode.FAILED.getMsg());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
