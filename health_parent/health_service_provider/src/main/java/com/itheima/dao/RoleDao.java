package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> getByUserId(Integer userId);
}
