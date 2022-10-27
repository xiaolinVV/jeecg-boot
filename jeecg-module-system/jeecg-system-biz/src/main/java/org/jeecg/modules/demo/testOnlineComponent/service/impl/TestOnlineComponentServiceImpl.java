package org.jeecg.modules.demo.testOnlineComponent.service.impl;

import org.jeecg.modules.demo.testOnlineComponent.entity.TestOnlineComponent;
import org.jeecg.modules.demo.testOnlineComponent.mapper.TestOnlineComponentMapper;
import org.jeecg.modules.demo.testOnlineComponent.service.ITestOnlineComponentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



/**
 * @Description: 测试 Online 表单控件
 * @Author: jeecg-boot
 * @Date:   2022-10-27
 * @Version: V1.0
 */
@Service("testOnlineComponentService")
public class TestOnlineComponentServiceImpl extends ServiceImpl<TestOnlineComponentMapper, TestOnlineComponent> implements ITestOnlineComponentService  {

}
