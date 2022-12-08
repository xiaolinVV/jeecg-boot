package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysBackSetting;

/**
 * @Description: 后台设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface ISysBackSettingService extends IService<SysBackSetting> {

    Result<SysBackSetting> add(SysBackSetting sysBackSetting);

    Result<SysBackSetting> edit(SysBackSetting sysBackSetting);

    Result<SysBackSetting> setSysBackSetting(SysBackSetting sysBackSetting);

    Result<SysBackSetting> findSysBackSetting();

}
