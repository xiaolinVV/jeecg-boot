package org.jeecg.modules.extFlow.flowMyBusinessConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.extFlow.flowMyBusinessConfig.entity.FlowMyBusinessConfig;
import org.jeecg.modules.extFlow.flowMyBusinessConfig.mapper.FlowMyBusinessConfigMapper;
import org.jeecg.modules.extFlow.flowMyBusinessConfig.service.IFlowMyBusinessConfigService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



/**
 * @Description:  流程配置表
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
@Service("flowMyBusinessConfigService")
public class FlowMyBusinessConfigServiceImpl extends ServiceImpl<FlowMyBusinessConfigMapper, FlowMyBusinessConfig> implements IFlowMyBusinessConfigService {

    /**
     * 根据表名称查询数据
     *
     * @param tableName 表名称
     * @return
     */
    @Override
    public FlowMyBusinessConfig getFlowMyBusinessConfigByTableName(String tableName) {
        LambdaQueryWrapper<FlowMyBusinessConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowMyBusinessConfig::getTableName,tableName);
        FlowMyBusinessConfig flowMyBusinessConfig = getOne(lambdaQueryWrapper, false);
        if (flowMyBusinessConfig == null) {
            throw new JeecgBootException("请先配置流程业务关联！");
        }
        return flowMyBusinessConfig;
    }
}
