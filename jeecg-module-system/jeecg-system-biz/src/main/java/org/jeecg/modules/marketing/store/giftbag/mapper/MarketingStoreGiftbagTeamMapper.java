package org.jeecg.modules.marketing.store.giftbag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagTeam;

import java.util.Map;

/**
 * @Description: 礼包团列表
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
public interface MarketingStoreGiftbagTeamMapper extends BaseMapper<MarketingStoreGiftbagTeam> {

    /**
     * 根据团队会员获取团队
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object>  getMarketingStoreGiftbagTeamByMemberId(@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 获取团队列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingStoreGiftbagTeamList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

}
