package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettings) {
        for (OrderSetting orderSetting : orderSettings) {
            long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if (count > 0) {
                //已经存在，执行更新操作
                orderSettingDao.editNumberByOrderDate(orderSetting);
            } else {
                //不存在，执行添加操作
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("dateBegin", date + "-01");
        map.put("dateEnd", date + "-31");
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("date", orderSetting.getOrderDate().getDate());
                orderMap.put("number", orderSetting.getNumber());
                orderMap.put("reservations", orderSetting.getReservations());
                result.add(orderMap);
            }
        }
        return result;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //已经存在，执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //不存在，执行添加操作
            orderSettingDao.add(orderSetting);
        }
    }
}
