package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysFrontSetting;

import java.util.Map;

/**
 * @Description: 小程序前端设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface ISysFrontSettingService extends IService<SysFrontSetting> {

    Result<SysFrontSetting> add(SysFrontSetting sysFrontSetting);

    Result<SysFrontSetting> edit(SysFrontSetting sysFrontSetting);

    Result<SysFrontSetting> setSysFrontSetting(SysFrontSetting sysFrontSetting);

    Result<SysFrontSetting> findSysFrontSetting();

    Map<String,Object> findSysFrontSettingMap();
}
