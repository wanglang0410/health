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
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

    @Value("${out_put_path}")
    private String outPutPath;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public Setmeal findById(Integer id) {
        return this.setMealDao.findById(id);
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add(setmeal);
        this.setMealAndCheckGroup(setmeal.getId(), checkgroupIds);
        if (!StringUtils.isEmpty(setmeal.getImg())) {
            this.savePicRedis(setmeal.getImg());
        }
        this.generateMobileStaticHtml();
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
            //删除关联
            setMealDao.deleteAssociation(id);
            //删除远程图片
            if (!StringUtils.isEmpty(setmeal.getImg())) {
                QiNiuUtils.deleteFileFromQiniu(setmeal.getImg());
            }
            //删除数据
            setMealDao.deleteById(id);
        }
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    @Override
    public Setmeal getById(Integer id) {
        return setMealDao.getById(id);
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

    //生成静态页面
    public void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl", "m_setmeal.html", dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("setmeal", this.getById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_" + setmeal.getId() + ".html",
                    dataMap);
        }
    }

    public void generateHtml(String templateName, String htmlPageName, Map<String, Object> dataMap) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outPutPath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
