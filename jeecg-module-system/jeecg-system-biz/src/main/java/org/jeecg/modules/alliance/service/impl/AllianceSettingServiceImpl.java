package org.jeecg.modules.alliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.jeecg.modules.alliance.entity.AllianceSetting;
import org.jeecg.modules.alliance.mapper.AllianceSettingMapper;
import org.jeecg.modules.alliance.service.IAllianceSettingService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 加盟商设置
 * @Author: jeecg-boot
 * @Date:   2022-03-22
 * @Version: V1.0
 */
@Service
public class AllianceSettingServiceImpl extends ServiceImpl<AllianceSettingMapper, AllianceSetting> implements IAllianceSettingService {

    @Override
    public AllianceSetting getAllianceSetting() {
        return this.getOne(new LambdaQueryWrapper<AllianceSetting>().eq(AllianceSetting::getStatus,"1"));
    }

    @Override
    public Map<String, Object> settingAllianceSettingView(Map<String, Object> resultMap) {
        Map<String,Object> allianceSettingMap= Maps.newHashMap();
        if(this.getAllianceSetting()==null){
            allianceSettingMap.put("allianceSettingMapIsview",0);
        }else{
            allianceSettingMap.put("allianceSettingMapIsview",1);
        }
        resultMap.put("allianceSettingMap",allianceSettingMap);
        return resultMap;
    }
}
