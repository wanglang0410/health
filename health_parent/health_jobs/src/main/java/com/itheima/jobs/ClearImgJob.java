package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        Set<String> fileNames = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (fileNames != null && fileNames.size() > 0) {
            for (String filename : fileNames) {
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
                QiNiuUtils.deleteFileFromQiniu(filename);
            }
        }
    }
}
