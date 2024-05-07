package org.jeecg.modules.demo.applyLeave.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.jeecg.modules.flowable.apithird.business.entity.FlowMyBusiness;
import org.jeecg.modules.flowable.apithird.service.FlowCallBackServiceI;
import org.jeecg.modules.flowable.apithird.service.FlowCommonService;
import org.jeecg.modules.extFlow.flowMyBusinessConfig.service.IFlowMyBusinessConfigService;
import org.jeecg.modules.extFlow.flowMyBusinessConfig.entity.FlowMyBusinessConfig;
import org.jeecg.modules.flowable.service.IFlowDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collection;
import org.jeecg.modules.flowable.apithird.entity.ActStatus;

import org.jeecg.modules.demo.applyLeave.entity.ApplyLeave;
import org.jeecg.modules.demo.applyLeave.mapper.ApplyLeaveMapper;
import org.jeecg.modules.demo.applyLeave.service.IApplyLeaveService;
import org.springframework.stereotype.Service;

import com.github.yulichang.base.MPJBaseServiceImpl;

/**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2024-05-07
 * @Version: V1.0
 */
@Service("applyLeaveService")
public class ApplyLeaveServiceImpl extends MPJBaseServiceImpl<ApplyLeaveMapper, ApplyLeave> implements IApplyLeaveService , FlowCallBackServiceI   {

    @Autowired
    FlowCommonService flowCommonService;

    @Autowired
    IFlowMyBusinessConfigService flowMyBusinessConfigService;

    @Autowired
    private IFlowDefinitionService iFlowDefinitionService;

    /**
     * 关联流程
     * @param dataId 业务id
     */
    @Override
    public void relationAct(String dataId) {
        String tableName = SqlHelper.table(ApplyLeave.class).getTableName();
        FlowMyBusinessConfig flowMyBusinessConfig = flowMyBusinessConfigService.getFlowMyBusinessConfigByTableName(tableName);
        flowCommonService.initActBusiness(flowMyBusinessConfig.getTitleExpression(), dataId, "applyLeaveService",flowMyBusinessConfig.getProcessDefinitionKey(),null,flowMyBusinessConfig.getJimuReportId(),flowMyBusinessConfig.getPcFormUrl());
    }

    @Override
    public <E extends IPage<ApplyLeave>> E page(E page, Wrapper<ApplyLeave> queryWrapper) {
        IPage<ApplyLeave> pageList = super.page(page, queryWrapper);
        List<ApplyLeave> records = pageList.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<String> ids = records.stream().map(ApplyLeave::getId).collect(Collectors.toList());
            Map<String, FlowMyBusiness> flowMyBusinessMap = flowCommonService.getMapByDataIds(ids);
            for (ApplyLeave applyLeave : records) {
                FlowMyBusiness flowMyBusiness = flowMyBusinessMap.get(applyLeave.getId());
                if (ObjectUtil.isNotNull(flowMyBusiness)) {
                    BeanUtil.copyProperties(flowMyBusiness,applyLeave,"id","createBy","createTime","updateBy","updateTime");
                }
            }
        }
        return page;
    }

    @Override
    public boolean save(ApplyLeave applyLeave) {
        /**新增数据，初始化流程关联信息**/
        applyLeave.setId(IdUtil.fastSimpleUUID());
        applyLeave.setBpmStatus(ActStatus.waitStart.getValue());
        boolean saved = super.save(applyLeave);
        this.relationAct(applyLeave.getId());

        /**
         * 默认情况是是新增完申请单数据后，前端再做一次提交的动作，不过有些场景需要默认新增申请单直接他提交流程，这里也支持，只需要解开注释就行！@by zhangshaolin
         * Map<String, Object> map = BeanUtil.beanToMap(applyLeave);
         * map.put("dataId",applyLeave.getId());
         * iFlowDefinitionService.startProcessInstanceByDataId(applyLeave.getId(), map);
         */
        return saved;
    }

    @Override
    public boolean removeById(Serializable id) {
        /**删除数据，移除流程关联信息**/
        boolean remove = super.removeById(id);
        if (remove) {
            flowCommonService.delActBusiness(id.toString());
        }
        return remove;
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        boolean remove = super.removeByIds(list);
        if (remove) {
            list.forEach(id -> {
                /**删除数据，移除流程关联信息**/
                flowCommonService.delActBusiness(id.toString());
            });
        }
        return remove;
    }

    @Override
    public void afterFlowHandle(FlowMyBusiness business) {
        //**流程处理完成后的回调,将其中关键信息存入业务表，即可单表业务操作,否则需要关联flow_my_business表获取流程信息<br/>**/
        ApplyLeave applyLeave = getById(business.getDataId());
        applyLeave.setBpmStatus(business.getBpmStatus());
        updateById(applyLeave);
    }

    @Override
    public Object getBusinessDataById(String dataId) {
        return getById(dataId);
    }

    @Override
    public Map<String, Object> flowValuesOfTask(String taskNameId, Map<String, Object> values) {
        return null;
    }

    @Override
    public List<String> flowCandidateUsernamesOfTask(String taskNameId, Map<String, Object> values) {
        return null;
    }
}
