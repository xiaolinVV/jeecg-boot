package org.jeecg.modules.marketing.store.prefecture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGive;

import java.util.Map;

/**
 * @Description: 店铺专区限制记录
 * @Author: jeecg-boot
 * @Date:   2022-12-16
 * @Version: V1.0
 */
public interface MarketingStorePrefectureGiveMapper extends BaseMapper<MarketingStorePrefectureGive> {


    /**
     *
     * 获取限购信息列表
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> getMarketingStorePrefectureGiveList(Page<Map<String,Object>> page,@Param("memberId") String memberId);
}
