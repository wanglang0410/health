package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    public void add(Setmeal setmeal);

    public void setMealAndCheckGroup(Map map);

    public Page<Setmeal> selectByCondition(String queryString);

    public Setmeal findById(Integer id);

    public List<Integer> findCheckGroupIdsBySetMealId(Integer id);

   public void edit(Setmeal setmeal);

   public void deleteAssociation(Integer id);

   public void deleteById(Integer id);

    List<Setmeal> findAll();

    Setmeal getById(Integer id);
}
