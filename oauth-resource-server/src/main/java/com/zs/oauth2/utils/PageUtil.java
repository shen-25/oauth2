package com.zs.oauth2.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页工具类
 * @author word
 */
public class PageUtil {

    public static PageInfoResult setPageInfoResult(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PageInfoResult pageInfoResult = new PageInfoResult();
        pageInfoResult.setCurrent(page);
        pageInfoResult.setRecords(list);
        pageInfoResult.setTotal(pageList.getTotal());
        pageInfoResult.setSize(pageList.getSize());
        pageInfoResult.setPages(pageList.getPages());
        return pageInfoResult;
    }
}