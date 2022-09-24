package org.jeecg.modules.extFlow.flowMyBusinessConfig.service;

import org.jeecg.modules.extFlow.flowMyBusinessConfig.entity.FlowMyBusinessConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 流程配置表
 * @Author: jeecg-boot
 * @Date: 2022-09-14
 * @Version: V1.0
 */
public interface IFlowMyBusinessConfigService extends IService<FlowMyBusinessConfig> {

    /**
     * 根据表名称查询数据
     *
     * @param tableName 表名称
     * @return
     */
    FlowMyBusinessConfig getFlowMyBusinessConfigByTableName(String tableName);
}
