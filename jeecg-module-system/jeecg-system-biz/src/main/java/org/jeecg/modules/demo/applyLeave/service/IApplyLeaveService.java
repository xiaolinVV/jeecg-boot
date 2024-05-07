package org.jeecg.modules.demo.applyLeave.service;

import org.jeecg.modules.demo.applyLeave.entity.ApplyLeave;
import com.github.yulichang.base.MPJBaseService;

/**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2024-05-07
 * @Version: V1.0
 */
public interface IApplyLeaveService extends MPJBaseService<ApplyLeave> {

    /**
     * 关联流程
     * @param dataId 业务id
     */
    void relationAct(String dataId);
}
