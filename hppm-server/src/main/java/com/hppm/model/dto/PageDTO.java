package com.hppm.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class PageDTO<T> {
    private int page; // 当前第几页
    private int pageSize; // 每页的数据行数
    private List<T> rows; // 查询结果
    private long total;  // 总记录数
    private int totalPages; // 总页数
}
