package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupManage;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
public interface IMarketingZoneGroupManageService extends IService<MarketingZoneGroupManage> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingZoneGroupManage> queryWrapper);


    /**
     * 专区团支付成功
     *
     * @param payZoneGroupLog
     */
    public void success(String payZoneGroupLog);

    /**
     * 获取参团进度列表
     *
     * @return
     */
    public List<Map<String,Object>> numberTuxedo(String marketingZoneGroupId);
}
