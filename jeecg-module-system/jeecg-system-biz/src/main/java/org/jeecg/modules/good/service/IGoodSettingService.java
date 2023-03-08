package org.jeecg.modules.good.service;

import org.jeecg.modules.good.entity.GoodSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 平台商品-设置
 * @Author: jeecg-boot
 * @Date:   2022-11-19
 * @Version: V1.0
 */
public interface IGoodSettingService extends IService<GoodSetting> {

    /**
     *
     * 获取配置信息
     *
     * @return
     */
    public GoodSetting getGoodSetting();

    /**
     * 设置终端需要的基本信息
     *
     * @param resultMap
     */
    public void setView(Map<String,Object> resultMap);

}
