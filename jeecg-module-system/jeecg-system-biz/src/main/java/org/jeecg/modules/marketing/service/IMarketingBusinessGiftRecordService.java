package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftRecord;

import java.util.Map;

/**
 * @Description: 创业礼包购买记录
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface IMarketingBusinessGiftRecordService extends IService<MarketingBusinessGiftRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingBusinessGiftRecord> queryWrapper, Map<String, Object> requestMap);

    Map<String,Object> getGiftRecordDetails(String id);


    /**
     * 根据会员id获取礼包总金额
     *
     * @param memberId
     * @return
     */
    public double getSumByMemberId(String memberId);
}
