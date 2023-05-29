package com.itheima.service;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult findPage(QueryPageBean queryPageBean);

    public CheckItem findById(Integer id);

    public void delete(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();
}
