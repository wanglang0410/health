package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);

    public Page<CheckGroup> selectByCondition(String queryString);

    public CheckGroup findById(Integer id);

    public void deleteById(Integer id);

    public void edit(CheckGroup checkGroup);

    public void setCheckGroupAndCheckItem(Map map);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void deleteAssociation(Integer id);

    public List<CheckGroup> findAll();
}
