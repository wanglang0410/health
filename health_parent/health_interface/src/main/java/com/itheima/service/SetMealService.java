package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface SetMealService {
     public Setmeal findById(Integer id);

    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    public PageResult findPage(QueryPageBean queryPageBean);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(Setmeal setmeal, Integer[] checkgroupIds);

    public void deleteById(Integer id);
}
