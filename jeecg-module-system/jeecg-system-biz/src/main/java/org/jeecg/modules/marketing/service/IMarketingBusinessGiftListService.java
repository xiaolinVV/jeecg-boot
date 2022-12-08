package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftList;

import java.util.Map;

/**
 * @Description: 创业礼包列表
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface IMarketingBusinessGiftListService extends IService<MarketingBusinessGiftList> {

    IPage<Map<String,Object>> findMarketingBusinessGiftList(Page<Map<String,Object>> page);

    IPage<Map<String,Object>> getIsMarketingBusinessGiftList(Page<Map<String,Object>> page, String memberId);


    /**
     * 创业礼包支付成功回调
     *
     * @param payBusinessGiftLogId
     */
    public void success(String payBusinessGiftLogId);
}
