package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.modules.good.entity.GoodSetting;
import org.jeecg.modules.good.mapper.GoodSettingMapper;
import org.jeecg.modules.good.service.IGoodSettingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 平台商品-设置
 * @Author: jeecg-boot
 * @Date:   2022-11-19
 * @Version: V1.0
 */
@Service
public class GoodSettingServiceImpl extends ServiceImpl<GoodSettingMapper, GoodSetting> implements IGoodSettingService {

    @Override
    public GoodSetting getGoodSetting() {
        return this.getOne(new LambdaQueryWrapper<>());
    }

    @Override
    public void setView(Map<String, Object> resultMap) {
        GoodSetting goodSetting=this.getGoodSetting();
        if(goodSetting!=null){
            Map<String, Object> goodSettingMap =Maps.newHashMap();
            goodSettingMap.put("goodListModel",goodSetting.getGoodListModel());
            resultMap.put("goodSettingMap", goodSettingMap);
        }
    }
}
