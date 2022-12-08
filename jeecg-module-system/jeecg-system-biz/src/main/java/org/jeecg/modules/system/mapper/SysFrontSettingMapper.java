package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysFrontSetting;

import java.util.Map;

/**
 * @Description: 小程序前端设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface SysFrontSettingMapper extends BaseMapper<SysFrontSetting> {

    Map<String,Object> findSysFrontSettingMap();

}
