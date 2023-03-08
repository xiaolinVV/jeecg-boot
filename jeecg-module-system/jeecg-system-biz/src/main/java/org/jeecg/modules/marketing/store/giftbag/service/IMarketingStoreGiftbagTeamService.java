package org.jeecg.modules.marketing.store.giftbag.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeam;

import java.util.Map;

/**
 * @Description: 礼包团列表
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
public interface IMarketingStoreGiftbagTeamService extends IService<MarketingStoreGiftbagTeam> {

    /**
     * 创建团队
     *
     * @param payLogId
     */
    public MarketingStoreGiftbagTeam createGiftbagTeam(String payLogId);


    /**
     * 根据团队会员获取团队
     *
     * @param memberId
     * @return
     */
    public Map<String,Object> getMarketingStoreGiftbagTeamByMemberId(String memberId,String storeManageId);


    /**
     * 获取团队列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingStoreGiftbagTeamList(Page<Map<String,Object>> page, Map<String,Object> paramMap);



}
