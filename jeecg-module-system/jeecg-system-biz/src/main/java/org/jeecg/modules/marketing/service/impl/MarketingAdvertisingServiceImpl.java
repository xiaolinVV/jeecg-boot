package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.marketing.dto.MarketingAdvertisingDTO;
import org.jeecg.modules.marketing.entity.MarketingAdvertising;
import org.jeecg.modules.marketing.mapper.MarketingAdvertisingMapper;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingService;
import org.jeecg.modules.marketing.vo.MarketingAdvertisingVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 广告管理
 * @Author: jeecg-boot
 * @Date:   2019-10-10
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingAdvertisingServiceImpl extends ServiceImpl<MarketingAdvertisingMapper, MarketingAdvertising> implements IMarketingAdvertisingService {

    @Override
    public List<MarketingAdvertising> getMarketingAdvertisingByAdLocation(String adLocation){
        List<MarketingAdvertising> listma=baseMapper.getMarketingAdvertisingByAdLocation(adLocation);
    return listma;
    };
    @Override
    public List<MarketingAdvertising>  allAvailable(){
        List<MarketingAdvertising> listma=baseMapper.allAvailable();
        return listma;
    };
    //多添加的广告list接口(null默认添加为空)
    @Override
    public List<MarketingAdvertising> getMarketingAdvertisingList(String createBy,String title,
                                                                  String adLocation,String goToType,
                                                                  String adType,String goodListId){
        List<MarketingAdvertising> listma=baseMapper.getMarketingAdvertisingList( createBy,title, adLocation,goToType, adType,goodListId);
        return listma;
    }
    /**
     * 查询广告推广集合
     * @param page
     * @param marketingAdvertisingVO
     * @return
     */
    @Override
    public IPage<MarketingAdvertisingDTO> getMarketingAdvertisingDTO(Page<MarketingAdvertising> page, MarketingAdvertisingVO marketingAdvertisingVO){
        return baseMapper.getMarketingAdvertisingDTO(page,marketingAdvertisingVO);
    }

    @Override
    public List<Map<String, Object>> findMarketingAdvertisingByAdLocation(Map<String,Object> map) {
        return baseMapper.findMarketingAdvertisingByAdLocation(map);
    }

    /**
     * 过期时间停用推荐广告
     */
    @Override
    public void   getMarketingAdvertisingOvertime() {
        log.info("推荐广告时间过期被停用推荐广告定时器启动!!!");
        List<MarketingAdvertising> marketingAdvertisingList = baseMapper.selectList(new LambdaQueryWrapper<MarketingAdvertising>()
                .eq(MarketingAdvertising::getStatus, "1")
                .lt(MarketingAdvertising::getEndTime, new Date()));
        marketingAdvertisingList.forEach(mp -> {
            log.info("推荐广告时间过期被停用广告:" + mp.getTitle() + "推荐分类id:" + mp.getId());
            //停用
            mp.setStatus("0");
            baseMapper.updateById(mp);

        });
    }
}
