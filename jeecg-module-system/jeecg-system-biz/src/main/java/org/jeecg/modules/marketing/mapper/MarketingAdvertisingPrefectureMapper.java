package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingAdvertisingPrefectureDTO;
import org.jeecg.modules.marketing.entity.MarketingAdvertisingPrefecture;
import org.jeecg.modules.marketing.vo.MarketingAdvertisingPrefectureVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 专区广告
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
public interface MarketingAdvertisingPrefectureMapper extends BaseMapper<MarketingAdvertisingPrefecture> {


    /**
     * 根据平台专区id查询广告列表
     *
     * @param id
     * @return
     */
    public List<Map<String,Object>> findByMarketingPrefectureId(@Param("id") String id);


    /**
     * 查询专区广告推广集合
     * @param page
     * @param marketingAdvertisingPrefectureVO
     * @return
     */
    IPage<MarketingAdvertisingPrefectureDTO> getMarketingAdvertisingPrefectureDTO(Page<MarketingAdvertisingPrefecture> page, @Param("marketingAdvertisingPrefectureVO") MarketingAdvertisingPrefectureVO marketingAdvertisingPrefectureVO);

}
