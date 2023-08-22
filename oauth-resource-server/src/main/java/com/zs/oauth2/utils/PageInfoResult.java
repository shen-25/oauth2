package com.zs.oauth2.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoResult {
//
//    /**
//     * 当前页数
//     */
//    private Integer page;
//
//    /**
//     * 当前页的记录数
//      */
//    private Integer pageSize;
//
    /**
     * 总页数
     */
    private Integer pages;
//
//    /**
//     * 总记录数
//     */
//    private Long total;
//
//    /**
//     * 是否是最后一页
//     */
//    private Boolean isLastPage;
//
//    /**
//     * 每页显示的内容
//     */
//    private List<?> list;

    private int current;			// 当前页数
    private int size;			// 总页数
    private long total;		  // 总记录数
    private List<?> records;		// 每行显示的内容

}