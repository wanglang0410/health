package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    public PageResult findPage(QueryPageBean queryPageBean);

    public void add(CheckGroup checkGroup, Integer[] checkItemIds);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    public void delete(Integer id);
}
