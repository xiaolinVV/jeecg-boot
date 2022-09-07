package org.jeecg.modules.demo.test_demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import liquibase.pro.packaged.E;
import org.jeecg.modules.demo.test_demo.entity.TestDemo;
import org.jeecg.modules.demo.test_demo.mapper.TestDemoMapper;
import org.jeecg.modules.demo.test_demo.service.ITestDemoService;
import org.jeecg.modules.flowable.apithird.business.entity.FlowMyBusiness;
import org.jeecg.modules.flowable.apithird.service.FlowCallBackServiceI;
import org.jeecg.modules.flowable.apithird.service.FlowCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 测试用户表
 * @Author: jeecg-boot
 * @Date:   2021-11-30
 * @Version: V1.0
 */
@Service("testDemoService")
public class TestDemoServiceImpl extends ServiceImpl<TestDemoMapper, TestDemo> implements ITestDemoService, FlowCallBackServiceI {
    @Autowired
    FlowCommonService flowCommonService;
    @Override
    public void afterFlowHandle(FlowMyBusiness business) {
        //流程操作后做些什么
        business.getTaskNameId();//接下来审批的节点
        business.getValues();//前端传进来的参数
        business.getBpmStatus();//流程状态 ActStatus.java
        //....其他

    }



    @Override
    public Object getBusinessDataById(String dataId) {
        return this.getById(dataId);
    }

    @Override
    public Map<String, Object> flowValuesOfTask(String taskNameId, Map<String, Object> values) {
        return null;
    }

    @Override
    public List<String> flowCandidateUsernamesOfTask(String taskNameId, Map<String, Object> values) {
        // 案例，写死了jeecg，实际业务中通过当前节点来判断下一个节点的候选人并写回到反参中，如果为null，流程模块会根据默认设置处理
        return Lists.newArrayList("jeecg");
    }

    @Override
    public IPage<TestDemo> myPage(Page<TestDemo> page, QueryWrapper<TestDemo> queryWrapper) {
        return this.baseMapper.myPage(page, queryWrapper);
    }
    @Override
    public void relationAct(String dataId) {
        flowCommonService.initActBusiness("测试流程：dataId"+dataId,dataId,"testDemoService","test-demo",null,null,null);
    }
    @Override
    public boolean save(TestDemo testDemo) {
        /**新增数据，初始化流程关联信息**/
        testDemo.setId(IdUtil.fastSimpleUUID());
        this.relationAct(testDemo.getId());
        return super.save(testDemo);
    }
    @Override
    public boolean removeById(Serializable id) {
        /**删除数据，移除流程关联信息**/
        flowCommonService.delActBusiness(id.toString());
        return super.removeById(id);
    }

    @Override
    public <E extends IPage<TestDemo>> E page(E page, Wrapper<TestDemo> queryWrapper) {
        IPage<TestDemo> pageList = super.page(page, queryWrapper);
        List<TestDemo> records = pageList.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<String> ids = records.stream().map(TestDemo::getId).collect(Collectors.toList());
            Map<String, FlowMyBusiness> flowMyBusinessMap = flowCommonService.getMapByDataIds(ids);
            for (TestDemo record : records) {
                FlowMyBusiness flowMyBusiness = flowMyBusinessMap.get(record.getId());
                if (ObjectUtil.isNotNull(flowMyBusiness)) {
                    BeanUtil.copyProperties(flowMyBusiness,record);
                }
            }
        }
        return page;
    }
}
