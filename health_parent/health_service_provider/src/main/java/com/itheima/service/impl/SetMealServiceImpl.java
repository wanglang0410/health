package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public Setmeal findById(Integer id) {
        return this.setMealDao.findById(id);
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add(setmeal);
        this.setMealAndCheckGroup(setmeal.getId(), checkgroupIds);
        this.savePicRedis(setmeal.getImg());
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> list = setMealDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(list.getTotal(), list.getResult());
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return setMealDao.findCheckGroupIdsBySetMealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.edit(setmeal);
        //删除关联
        setMealDao.deleteAssociation(setmeal.getId());
        //新增管理
        setMealAndCheckGroup(setmeal.getId(), checkgroupIds);
    }

    @Override
    public void deleteById(Integer id) {
        Setmeal setmeal = setMealDao.findById(id);
        if (setmeal != null) {
            //删除数据
            setMealDao.deleteById(id);
            //删除关联
            setMealDao.deleteAssociation(id);
            //删除远程图片
            QiNiuUtils.deleteFileFromQiniu(setmeal.getImg());
        }
    }

    public void setMealAndCheckGroup(Integer setMealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", setMealId);
                map.put("checkgroup_id", checkgroupId);
                setMealDao.setMealAndCheckGroup(map);
            }
        }
    }

    //将图片名称保存到Redis
    private void savePicRedis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }
}
