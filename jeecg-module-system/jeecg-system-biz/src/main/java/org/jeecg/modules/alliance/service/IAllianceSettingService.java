package org.jeecg.modules.alliance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.alliance.entity.AllianceSetting;

import java.util.Map;

/**
 * @Description: 加盟商设置
 * @Author: jeecg-boot
 * @Date:   2022-03-22
 * @Version: V1.0
 */
public interface IAllianceSettingService extends IService<AllianceSetting> {

    /**
     * 获取启用的加盟商设置对象
     *
     * @return
     */
    public AllianceSetting getAllianceSetting();

    /**
     * 显示和隐藏加盟商管理
     *
     * @param resultMap
     */
    public Map<String, Object> settingAllianceSettingView(Map<String,Object> resultMap);

}
