package org.jeecg.modules.demo.tbLeaveApply.service;

import org.jeecg.modules.demo.tbLeaveApply.entity.TbLeaveApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 请假申请
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
public interface ITbLeaveApplyService extends IService<TbLeaveApply> {
    void relationAct(String dataId);
}
