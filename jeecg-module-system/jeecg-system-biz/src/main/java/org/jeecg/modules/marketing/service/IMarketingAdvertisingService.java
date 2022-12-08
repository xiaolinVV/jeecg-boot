package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingAdvertisingService extends IService<MarketingAdvertising> {
    public List<MarketingAdvertising> getMarketingAdvertisingByAdLocation(String adLocation);
    //所有状态可用集合
    public List<MarketingAdvertising>  allAvailable();
    //多添加的广告list接口(null默认添加为空)
    public List<MarketingAdvertising> getMarketingAdvertisingList(String createBy,String title,
                                                                  String adLocation,String goToType,
                                                                  String adType,String goodListId);

    /**
     * 查询广告推广集合
     * @param page
     * @param marketingAdvertisingVO
     * @return
     */
    IPage<MarketingAdvertisingDTO> getMarketingAdvertisingDTO(Page<MarketingAdvertising> page, MarketingAdvertisingVO marketingAdvertisingVO);

    /**
     * 根据类型查询广告信息
     *
     * @param paramMap  0:首页；1：类型页
     * @return
     */
    List<Map<String,Object>> findMarketingAdvertisingByAdLocation(Map<String,Object> map);

    /**
     * 过期时间停用推荐广告(定时器)
     */

    public void   getMarketingAdvertisingOvertime();
}
