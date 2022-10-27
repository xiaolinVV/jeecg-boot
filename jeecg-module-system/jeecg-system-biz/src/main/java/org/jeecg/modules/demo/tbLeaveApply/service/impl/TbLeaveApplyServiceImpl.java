package org.jeecg.modules.demo.tbLeaveApply.service.impl;

import org.jeecg.modules.demo.tbLeaveApply.entity.TbLeaveApply;
import org.jeecg.modules.demo.tbLeaveApply.mapper.TbLeaveApplyMapper;
import org.jeecg.modules.demo.tbLeaveApply.service.ITbLeaveApplyService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


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

/**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2022-10-26
 * @Version: V1.0
 */
@Service("tbLeaveApplyService")
public class TbLeaveApplyServiceImpl extends ServiceImpl<TbLeaveApplyMapper, TbLeaveApply> implements ITbLeaveApplyService , FlowCallBackServiceI  {

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
        String tableName = SqlHelper.table(TbLeaveApply.class).getTableName();
        FlowMyBusinessConfig flowMyBusinessConfig = flowMyBusinessConfigService.getFlowMyBusinessConfigByTableName(tableName);
        flowCommonService.initActBusiness(flowMyBusinessConfig.getTitleExpression(), dataId, "tbLeaveApplyService",flowMyBusinessConfig.getProcessDefinitionKey(),null,flowMyBusinessConfig.getJimuReportId(),flowMyBusinessConfig.getPcFormUrl());
    }

    @Override
    public <E extends IPage<TbLeaveApply>> E page(E page, Wrapper<TbLeaveApply> queryWrapper) {
        IPage<TbLeaveApply> pageList = super.page(page, queryWrapper);
        List<TbLeaveApply> records = pageList.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<String> ids = records.stream().map(TbLeaveApply::getId).collect(Collectors.toList());
            Map<String, FlowMyBusiness> flowMyBusinessMap = flowCommonService.getMapByDataIds(ids);
            for (TbLeaveApply tbLeaveApply : records) {
                FlowMyBusiness flowMyBusiness = flowMyBusinessMap.get(tbLeaveApply.getId());
                if (ObjectUtil.isNotNull(flowMyBusiness)) {
                    BeanUtil.copyProperties(flowMyBusiness,tbLeaveApply,"id","createBy","createTime","updateBy","updateTime");
                }
            }
        }
        return page;
    }

    @Override
    public boolean save(TbLeaveApply tbLeaveApply) {
        /**新增数据，初始化流程关联信息**/
        tbLeaveApply.setId(IdUtil.fastSimpleUUID());
        boolean saved = super.save(tbLeaveApply);
        this.relationAct(tbLeaveApply.getId());

        /**
         * 默认情况是是新增完申请单数据后，前端再做一次提交的动作，不过有些场景需要默认新增申请单直接他提交流程，这里也支持，只需要解开注释就行！@by zhangshaolin
         * Map<String, Object> map = BeanUtil.beanToMap(tbLeaveApply);
         * map.put("dataId",tbLeaveApply.getId());
         * iFlowDefinitionService.startProcessInstanceByDataId(tbLeaveApply.getId(), map);
         */
        return saved;
    }

    @Override
    public boolean removeById(Serializable id) {
        /**删除数据，移除流程关联信息**/
        flowCommonService.delActBusiness(id.toString());
        return super.removeById(id);
    }

    @Override
    public void afterFlowHandle(FlowMyBusiness business) {
        //**流程处理完成后的回调,将其中关键信息存入业务表，即可单表业务操作,否则需要关联flow_my_business表获取流程信息<br/>**/
        TbLeaveApply tbLeaveApply = getById(business.getDataId());
        tbLeaveApply.setBpmStatus(business.getBpmStatus());
        updateById(tbLeaveApply);
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
