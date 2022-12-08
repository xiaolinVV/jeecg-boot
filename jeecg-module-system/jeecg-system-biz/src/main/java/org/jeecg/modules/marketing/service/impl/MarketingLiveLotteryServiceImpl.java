package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.jeecg.modules.marketing.entity.MarketingLiveLottery;
import org.jeecg.modules.marketing.entity.MarketingLiveLotteryRecord;
import org.jeecg.modules.marketing.entity.MarketingLivePrize;
import org.jeecg.modules.marketing.mapper.MarketingLiveLotteryMapper;
import org.jeecg.modules.marketing.service.IMarketingLiveLotteryRecordService;
import org.jeecg.modules.marketing.service.IMarketingLiveLotteryService;
import org.jeecg.modules.marketing.service.IMarketingLivePrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 直播管理-直播抽奖
 * @Author: jeecg-boot
 * @Date:   2021-09-15
 * @Version: V1.0
 */
@Service
@Log
public class MarketingLiveLotteryServiceImpl extends ServiceImpl<MarketingLiveLotteryMapper, MarketingLiveLottery> implements IMarketingLiveLotteryService {
    @Autowired
    @Lazy
    private IMarketingLiveLotteryRecordService iMarketingLiveLotteryRecordService;
    @Autowired
    @Lazy
    private IMarketingLivePrizeService iMarketingLivePrizeService;
    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingLiveLottery> queryWrapper, Map<String, Object> requestMap) {
        IPage<Map<String, Object>> mapIPage = baseMapper.queryPageList(page, queryWrapper, requestMap);
        mapIPage.getRecords().forEach(mp->{
            mp.put("participateMember",mp.get("status").equals("1")?iMarketingLiveLotteryRecordService.count(new LambdaQueryWrapper<MarketingLiveLotteryRecord>()
                    .eq(MarketingLiveLotteryRecord::getDelFlag,"0")
                    .eq(MarketingLiveLotteryRecord::getMarketingLiveLotteryId,mp.get("id"))):0);
        });
        return mapIPage;
    }

    @Override
    public IPage<Map<String, Object>> getMarketingLiveLotteryListByStreamingId(Page<Map<String, Object>> page, String marketingLiveStreamingId) {
        IPage<Map<String, Object>> mapperMarketingLiveLotteryListByStreamingId = baseMapper.getMarketingLiveLotteryListByStreamingId(page, marketingLiveStreamingId);
        mapperMarketingLiveLotteryListByStreamingId.getRecords().forEach(mll->{
            if (mll.get("lotteryType").equals("0")){
                MarketingLivePrize marketingLivePrize = iMarketingLivePrizeService.getById(String.valueOf(mll.get("prizeId")));
                if (marketingLivePrize!=null){
                    mll.put("prizeImage",marketingLivePrize.getPrizeImage());
                    mll.put("prizeName",marketingLivePrize.getPrizeName());
                }
            }else {
                MarketingLivePrize marketingLivePrize = iMarketingLivePrizeService.getById(String.valueOf(mll.get("prizeId")));
                if (marketingLivePrize!=null){
                    mll.put("prizeImage",marketingLivePrize.getPrizeImage());
                    mll.put("prizeName",marketingLivePrize.getPrizeName());
                }
            }
        });
        return mapperMarketingLiveLotteryListByStreamingId;
    }

    @Override
    public Long getTimeInfo(String id) {
        return baseMapper.getTimeInfo(id);
    }
}
