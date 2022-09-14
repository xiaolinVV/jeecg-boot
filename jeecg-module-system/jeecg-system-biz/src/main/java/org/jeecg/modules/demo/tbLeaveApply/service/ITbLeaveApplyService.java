package org.jeecg.modules.demo.tbLeaveApply.service;

import org.jeecg.modules.demo.tbLeaveApply.entity.TbLeaveApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
public interface ITbLeaveApplyService extends IService<TbLeaveApply> {
    /**
     * 关联流程
     * @param dataId 业务id
     */
    void relationAct(String dataId);
}
