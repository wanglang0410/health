package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User getByUsername(String username) {
        User user = userDao.getByUsername(username);
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles = roleDao.getByUserId(userId);
        user.setRoles(roles);
        for (Role role : roles) {
            Set<Permission> permissions = permissionDao.getByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        return user;
    }
}
