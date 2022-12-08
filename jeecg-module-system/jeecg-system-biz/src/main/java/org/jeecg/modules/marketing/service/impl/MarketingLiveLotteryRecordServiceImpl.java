package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingLiveLotteryRecordMapper;
import org.jeecg.modules.marketing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 直播管理-中奖记录
 * @Author: jeecg-boot
 * @Date: 2021-09-15
 * @Version: V1.0
 */
@Service
@Log
public class MarketingLiveLotteryRecordServiceImpl extends ServiceImpl<MarketingLiveLotteryRecordMapper, MarketingLiveLotteryRecord> implements IMarketingLiveLotteryRecordService {
    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;
    @Autowired
    @Lazy
    private IMarketingLiveLotteryService iMarketingLiveLotteryService;
    @Autowired
    @Lazy
    private IMarketingLivePrizeService iMarketingLivePrizeService;
    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;
    @Autowired
    private IMarketingChannelService iMarketingChannelService;

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingLiveLotteryRecord> queryWrapper, Map<String, Object> requestMap) {
        IPage<Map<String, Object>> mapIPage = baseMapper.queryPageList(page, queryWrapper, requestMap);
        mapIPage.getRecords().forEach(mp -> {
            if (mp.get("prizeType").equals("1")) {
                MarketingDiscountCoupon marketingDiscountCoupon = iMarketingDiscountCouponService.getById(String.valueOf(mp.get("marketingDiscountCouponId")));
                if (marketingDiscountCoupon != null) {
                    mp.put("qqzixuangu", marketingDiscountCoupon.getQqzixuangu());
                    mp.put("DiscountName", marketingDiscountCoupon.getName());
                }
            }
        });
        return mapIPage;
    }

    @Override
    public IPage<Map<String, Object>> getMarketingLiveLotteryRecordList(Page<Map<String, Object>> page, Map<String, Object> map) {
        return baseMapper.getMarketingLiveLotteryRecordList(page, map);
    }

    @Override
    @Transactional
    public Map<String, Object> runLottery(String memberId, long duration, String marketingLiveLotteryId) {
        HashMap<String, Object> map = new HashMap<>();
        MarketingLiveLottery marketingLiveLottery = iMarketingLiveLotteryService.getById(marketingLiveLotteryId);
        log.info("会员: " + memberId + ";累计时长" + duration + "秒");
        iMarketingLiveLotteryService.updateById(marketingLiveLottery
                .setStatus("1"));
        if (duration >= marketingLiveLottery.getLotteryQualification().longValue()*60) {
            marketingLiveLottery
                    .setLotteryPrizeTotal(marketingLiveLottery.getLotteryPrizeTotal().subtract(new BigDecimal(1)));
            if (iMarketingLiveLotteryService.update(marketingLiveLottery, new LambdaUpdateWrapper<MarketingLiveLottery>()
                    .eq(MarketingLiveLottery::getId, marketingLiveLottery.getId())
                    .gt(MarketingLiveLottery::getLotteryPrizeTotal, 0))) {
                return runLotterySet(map, marketingLiveLottery, memberId, duration, "0");
            } else {
                return runLotterySet(map, marketingLiveLottery, memberId, duration, "1");
            }
        } else {
            log.info("会员: " + memberId + ";累计时长" + duration + "分钟抽奖条件:"+marketingLiveLottery.getLotteryQualification().longValue());
            //没有达到资格
            map.put("status", "2");
        }
        return map;
    }

    private Map<String, Object> runLotterySet(Map<String, Object> map, MarketingLiveLottery marketingLiveLottery, String memberId, long durationc, String isWinning) {
        MarketingLivePrize marketingLivePrize = null;
        log.info("开奖啦+++++++++" + DateUtils.now());
        log.info("会员: " + memberId + ";累计时长" + durationc + "秒");
        //判断是否中奖
        if (isWinning.equals("0")) {
            marketingLivePrize = iMarketingLivePrizeService.getById(marketingLiveLottery.getLotteryPrizeId());
        } else {
            if (StringUtils.isBlank(marketingLiveLottery.getLosingLotteryPrizeId())) {
                map.put("status", "1");
                map.put("lotteryName", marketingLiveLottery.getLotteryName());
                log.info("开奖啦 本轮没有奖品" + DateUtils.now());
            } else {
                marketingLivePrize = iMarketingLivePrizeService.getById(marketingLiveLottery.getLosingLotteryPrizeId());
            }

        }
        if (marketingLivePrize != null) {
            iMarketingLivePrizeService.updateById(marketingLivePrize
                    .setRepertory(marketingLivePrize.getRepertory().subtract(new BigDecimal(1))));
            MarketingLiveLotteryRecord marketingLiveLotteryRecord = new MarketingLiveLotteryRecord();
            marketingLiveLotteryRecord
                    .setMemberListId(memberId)
                    .setLotteryNo(OrderNoUtils.getOrderNo())
                    .setMarketingLivePrizeId(marketingLivePrize.getId())
                    .setPrizeName(marketingLivePrize.getPrizeName())
                    .setPrizeImage(marketingLivePrize.getPrizeImage())
                    .setPrizeType(marketingLivePrize.getPrizeType())
                    .setQuantity(marketingLiveLottery.getLotteryPrizeQuantity())
                    .setGetTime(new Date())
                    .setTotalTimes(new BigDecimal(durationc))
                    .setIsLottery(isWinning.equals("0") ? "1" : "0")
                    .setMarketingLiveLotteryId(marketingLiveLottery.getId())
                    .setMarketingLiveStreamingId(marketingLiveLottery.getMarketingLiveStreamingId());
            if (marketingLivePrize.getPrizeType().equals("1")) {
                //获取优惠券
                MarketingDiscount marketingDiscount = iMarketingDiscountService.getById(marketingLivePrize.getMarketingDiscountId());
                //生成优惠券
                MarketingDiscountCoupon marketingDiscountCoupon = new MarketingDiscountCoupon();
                marketingDiscountCoupon.setDelFlag("0");
                marketingDiscountCoupon.setPrice(marketingDiscount.getSubtract());
                marketingDiscountCoupon.setName(marketingDiscount.getName());
                marketingDiscountCoupon.setIsThreshold(marketingDiscount.getIsThreshold());
                marketingDiscountCoupon.setMemberListId(memberId);
                marketingDiscountCoupon.setSysUserId(marketingDiscount.getSysUserId());
                marketingDiscountCoupon.setQqzixuangu(OrderNoUtils.getOrderNo());
                marketingDiscountCoupon.setMarketingDiscountId(marketingDiscount.getId());
                marketingDiscountCoupon.setIsPlatform(marketingDiscount.getIsPlatform());
                marketingDiscountCoupon.setCompletely(marketingDiscount.getCompletely());
                marketingDiscountCoupon.setIsGive(marketingDiscount.getIsGive());
                marketingDiscountCoupon.setIsWarn(marketingDiscount.getIsWarn());
                marketingDiscountCoupon.setWarnDays(marketingDiscount.getWarnDays());
                marketingDiscountCoupon.setUserRestrict(marketingDiscount.getUserRestrict());
                marketingDiscountCoupon.setDiscountExplain(marketingDiscount.getDiscountExplain());
                marketingDiscountCoupon.setCoverPlan(marketingDiscount.getCoverPlan());
                marketingDiscountCoupon.setPosters(marketingDiscount.getPosters());
                marketingDiscountCoupon.setMainPicture(marketingDiscount.getMainPicture());
                marketingDiscountCoupon.setIsUniqueness(marketingDiscount.getIsUniqueness());
                marketingDiscountCoupon.setIsDistribution(marketingDiscount.getIsDistribution());
                marketingDiscountCoupon.setGetRestrict(marketingDiscount.getGetRestrict());
                //平台渠道判断
                MarketingChannel marketingChannel = iMarketingChannelService.getOne(new LambdaQueryWrapper<MarketingChannel>()
                        .eq(MarketingChannel::getDelFlag, "0")
                        .orderByAsc(MarketingChannel::getCreateTime)
                        .last("limit 1")

                );
                if (marketingChannel != null) {
                    marketingDiscountCoupon.setMarketingChannelId(marketingChannel.getId());
                    marketingDiscountCoupon.setTheChannel(marketingChannel.getName());
                }
                //标准用券方式
                if (marketingDiscount.getVouchersWay().equals("0")) {
                    //优惠券的时间生成
                    marketingDiscountCoupon.setStartTime(marketingDiscount.getStartTime());
                    marketingDiscountCoupon.setEndTime(marketingDiscount.getEndTime());
                }

                //领券当日起
                if (marketingDiscount.getVouchersWay().equals("1")) {
                    //优惠券的时间生成
                    Calendar calendar = Calendar.getInstance();
                    marketingDiscountCoupon.setStartTime(calendar.getTime());

                    if (marketingDiscount.getMonad().equals("天")) {
                        calendar.add(Calendar.DATE, marketingDiscount.getDisData().intValue());
                    }
                    if (marketingDiscount.getMonad().equals("周")) {
                        calendar.add(Calendar.WEEK_OF_MONTH, marketingDiscount.getDisData().intValue());
                    }
                    if (marketingDiscount.getMonad().equals("月")) {
                        calendar.add(Calendar.MONTH, marketingDiscount.getDisData().intValue());
                    }

                    marketingDiscountCoupon.setEndTime(calendar.getTime());
                }
                //领券次日起
                if (marketingDiscount.getVouchersWay().equals("2")) {
                    //优惠券的时间生成
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 1);
                    marketingDiscountCoupon.setStartTime(calendar.getTime());

                    if (marketingDiscount.getMonad().equals("天")) {
                        calendar.add(Calendar.DATE, marketingDiscount.getDisData().intValue());
                    }
                    if (marketingDiscount.getMonad().equals("周")) {
                        calendar.add(Calendar.WEEK_OF_MONTH, marketingDiscount.getDisData().intValue());
                    }
                    if (marketingDiscount.getMonad().equals("月")) {
                        calendar.add(Calendar.MONTH, marketingDiscount.getDisData().intValue());
                    }

                    marketingDiscountCoupon.setEndTime(calendar.getTime());
                }

                if (new Date().getTime() >= marketingDiscountCoupon.getStartTime().getTime() && new Date().getTime() <= marketingDiscountCoupon.getEndTime().getTime()) {
                    //设置生效
                    marketingDiscountCoupon.setStatus("1");
                } else {
                    marketingDiscountCoupon.setStatus("0");
                }

                iMarketingDiscountCouponService.save(marketingDiscountCoupon);
                marketingDiscount.setReleasedQuantity(marketingDiscount.getReleasedQuantity().add(new BigDecimal(1)));
                iMarketingDiscountService.saveOrUpdate(marketingDiscount);
                marketingLiveLotteryRecord.setDrawStatus("1");
                marketingLiveLotteryRecord.setDrawTime(new Date());
                marketingLiveLotteryRecord.setMarketingDiscountCouponId(marketingDiscountCoupon.getId());
                this.save(marketingLiveLotteryRecord);
                map.put("status", isWinning);
                map.put("lotteryName", marketingLiveLottery.getLotteryName());
                map.put("prizeName", marketingLiveLotteryRecord.getPrizeName());
                map.put("prizeType", marketingLiveLotteryRecord.getPrizeType());
                map.put("quantity", marketingLiveLotteryRecord.getQuantity());
                map.put("marketingDiscountCouponId", marketingLiveLotteryRecord.getMarketingDiscountCouponId());
                log.info("开奖啦++++" + "会员id:" + memberId + marketingLiveLottery.getLotteryName() + "是否中奖:0中奖,1未中奖" + isWinning + "获得优惠券" + marketingLiveLotteryRecord.getPrizeName() + DateUtils.now());
            } else {
                this.save(marketingLiveLotteryRecord);
                map.put("status", isWinning);
                map.put("marketingLiveLotteryRecordId", marketingLiveLotteryRecord.getId());
                map.put("lotteryName", marketingLiveLottery.getLotteryName());
                map.put("prizeName", marketingLiveLotteryRecord.getPrizeName());
                map.put("prizeType", marketingLiveLotteryRecord.getPrizeType());
                map.put("prizeImage", marketingLiveLotteryRecord.getPrizeImage());
                map.put("quantity", marketingLiveLotteryRecord.getQuantity());
                log.info("开奖啦++++" + "会员id:" + memberId + marketingLiveLottery.getLotteryName() + "是否中奖:0中奖,1未中奖" + isWinning + "获得礼品" + marketingLiveLotteryRecord.getPrizeName() + DateUtils.now());
            }
        }
        map.put("isViewLotteryNumber", iMarketingLiveLotteryService.count(new LambdaQueryWrapper<MarketingLiveLottery>()
                .eq(MarketingLiveLottery::getDelFlag, "0")
                .eq(MarketingLiveLottery::getMarketingLiveStreamingId, marketingLiveLottery.getMarketingLiveStreamingId())
                .eq(MarketingLiveLottery::getCancelNumber, "0")
                .eq(MarketingLiveLottery::getStatus, "0")
                .gt(MarketingLiveLottery::getLotteryNumber, marketingLiveLottery.getLotteryNumber())
        ) > 0 ? 1 : 0);
        log.info("返回参数" + map);
        return map;
    }

}
