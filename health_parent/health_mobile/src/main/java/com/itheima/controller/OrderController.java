package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        try {
            String codeInRedis = jedisPool.getResource().get(map.get("telephone") + RedisMessageConstant.SENDTYPE_ORDER);
            String validateCode = (String) map.get("validateCode");
            //校验手机验证码
//            if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
//                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
//            }
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            return orderService.order(map);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
