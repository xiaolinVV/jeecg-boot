package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingAdvertisingDTO;
import org.jeecg.modules.marketing.entity.MarketingAdvertising;
import org.jeecg.modules.marketing.vo.MarketingAdvertisingVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告管理
 * @Author: jeecg-boot
 * @Date:   2019-10-10
 * @Version: V1.0
 */
public interface MarketingAdvertisingMapper extends BaseMapper<MarketingAdvertising> {
//根据广告位置查询
    public List<MarketingAdvertising> getMarketingAdvertisingByAdLocation(@Param("adLocation")String adLocation);
  //查询广告集合（状态为：1）
    public List<MarketingAdvertising>  allAvailable();
   //多条件的广告list接口(null默认添加为空)
   public List<MarketingAdvertising> getMarketingAdvertisingList(@Param("createBy")String createBy,@Param("title")String title,
                                                                 @Param("adLocation")String adLocation,@Param("goToType")String goToType,
                                                              @Param("adType")String adType,@Param("goodListId")String goodListId);

    /**
     * 查询广告推广集合
     * @param page
     * @param marketingAdvertisingVO
     * @return
     */
    IPage<MarketingAdvertisingDTO> getMarketingAdvertisingDTO(Page<MarketingAdvertising> page, @Param("marketingAdvertisingVO")MarketingAdvertisingVO marketingAdvertisingVO);


    /**
     * 根据类型查询广告信息
     *
     * @param
     * @return
     */
    List<Map<String,Object>> findMarketingAdvertisingByAdLocation(@Param("paramObjectMap") Map<String,Object> paramObjectMap);

}
