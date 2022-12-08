package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;

import java.util.List;
import java.util.Map;

/**
 * @Description: 平台专区分类
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
public interface MarketingPrefectureTypeMapper extends BaseMapper<MarketingPrefectureType> {


    /**
     * 根据平台专区id查询专区类型  但是必须这个类型下面有商品
     *
     * @param id
     * @return
     */
    public List<Map<String,Object>> findByMarketingPrefectureId(@Param("id") String id);

    List<Map<String,Object>> findUnderlingListMap(@Param("id") String id);

    List<Map<String,Object>> findMarketingPrefectureTypeTwoById(@Param("id") String id);

    List<MarketingPrefectureType> getUnderlingList(@Param("id") String id);
}
