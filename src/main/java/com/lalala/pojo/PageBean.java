package com.lalala.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页模板,用来对quartz的实体类结果进行分类
 */
public class PageBean<T> {

    private List<T> pageData = new ArrayList<>();
    private Integer pageNo = 0;
    private Integer pageSize = Integer.valueOf(10);
    private Long totalCount = 0L;

    public PageBean(List<T> pageData, Long totalCount) {
        this.pageData = pageData;
        this.totalCount = totalCount;
    }

    public PageBean() {

    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getPageData() {
        return this.pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}