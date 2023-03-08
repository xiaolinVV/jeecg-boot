package org.jeecg.modules.marketing.store.prefecture.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGive;

import java.util.Map;

/**
 * @Description: 店铺专区限制记录
 * @Author: jeecg-boot
 * @Date:   2022-12-16
 * @Version: V1.0
 */
public interface IMarketingStorePrefectureGiveService extends IService<MarketingStorePrefectureGive> {


    /**
     * 是否允许购买
     *
     * @param memberId
     * @param marketingStorePrefectureGoodId
     * @return
     */
    public boolean ifBuy(String memberId,String marketingStorePrefectureGoodId);


    /**
     *
     * 获取限购信息列表
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> getMarketingStorePrefectureGiveList(Page<Map<String,Object>> page,String memberId);

}
