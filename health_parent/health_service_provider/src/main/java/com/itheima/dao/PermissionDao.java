package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> getByRoleId(Integer roleId);
}
