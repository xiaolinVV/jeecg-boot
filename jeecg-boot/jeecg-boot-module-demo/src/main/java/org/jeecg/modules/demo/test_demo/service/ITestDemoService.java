package org.jeecg.modules.demo.test_demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.demo.test_demo.entity.TestDemo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 测试用户表
 * @Author: jeecg-boot
 * @Date:   2021-11-30
 * @Version: V1.0
 */
public interface ITestDemoService extends IService<TestDemo> {

    IPage<TestDemo> myPage(Page<TestDemo> page, QueryWrapper<TestDemo> queryWrapper);

    void relationAct(String dataId);
}
