package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingCertificateDTO;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingCertificateMapper;
import org.jeecg.modules.marketing.mapper.MarketingCertificateStoreMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.vo.MarketingCertificateVO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 兑换券
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Service
public class MarketingCertificateServiceImpl extends ServiceImpl<MarketingCertificateMapper, MarketingCertificate> implements IMarketingCertificateService {
    @Autowired(required = false)
    private MarketingCertificateStoreMapper marketingCertificateStoreMapper;
    @Autowired(required = false)
    private IStoreManageService iStoreManageService;
    @Autowired
    private IMarketingCertificateGoodService iMarketingCertificateGoodService;
    @Autowired
    private IMarketingCertificateStoreService iMarketingCertificateStoreService;

    @Autowired
    private IMarketingChannelService iMarketingChannelService;


    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;

    @Override
    public Result<MarketingCertificate> saveMarketingCertificate(MarketingCertificate marketingCertificate, String goodListIds, String sysUserIds) {
        Result<MarketingCertificate> result = new Result<>();
        //新增兑换券
        boolean b = this.save(marketingCertificate);
        //获取兑换券id
        String id = marketingCertificate.getId();
        JSONArray discountIds = JSON.parseArray(goodListIds);
        if (oConvertUtils.isNotEmpty(discountIds)){
        //插入兑换券商品
            for (int i = 0; i < discountIds.size(); i++) {
                iMarketingCertificateGoodService.save(new MarketingCertificateGood()
                        .setDelFlag("0")
                        .setMarketingCertificateId(id)
                        .setGoodListId(discountIds.getJSONObject(i).getString("id"))
                        .setIsPlatform("1")
                        .setCanMonth(discountIds.getJSONObject(i).getString("canMonth"))
                        .setGoodSpecificationId(discountIds.getJSONObject(i).getString("goodSpecificationId"))
                        .setQuantity(discountIds.getJSONObject(i).getBigDecimal("quantity"))
                );
            }
        }
        if (StringUtils.isNotBlank(sysUserIds)) {
            //插入兑换券核销门店
            List<String> userIds = Arrays.asList(StringUtils.split(sysUserIds, ","));
            userIds.forEach(u->{
                StoreManage storeManageServiceById = iStoreManageService.getById(u);
                MarketingCertificateStore marketingCertificateStore = new MarketingCertificateStore();
                marketingCertificateStore.setMarketingCertificateId(id);
                marketingCertificateStore.setSysUserId(storeManageServiceById.getSysUserId());
                marketingCertificateStore.setDelFlag("0");
                marketingCertificateStoreMapper.insert(marketingCertificateStore);
            });
        }
        if (b){
            result.setMessage("新增成功!");
        }else {
            result.error500("新增失败!");
        }
        return result;
    }

    @Override
    public IPage<MarketingCertificateDTO> findMarketingCertificate(Page<MarketingCertificateDTO> page,MarketingCertificateVO marketingCertificateVO) {

        return baseMapper.findMarketingCertificate(page,marketingCertificateVO);
    }

    @Override
    public List<MarketingCertificateVO> findCertificateVO(MarketingCertificateVO marketingCertificateVO) {
        List<MarketingCertificateVO> certificateVO = baseMapper.findCertificateVO(marketingCertificateVO);
        certificateVO.forEach(c->{
            if (StringUtils.isNotBlank(c.getMainPictures())){
                String s = (String) JSON.parseObject(c.getMainPicture()).get("0");
                c.setMainPictures(s);
            }
            if (c.getRewardStore().equals("0")){
                c.setStoreQuantity("全平台");
            }
        });
        return certificateVO;
    }

    @Override
    public IPage<MarketingCertificateVO> findCertificateVO(Page<MarketingCertificateDTO> page, MarketingCertificateVO marketingCertificateVO) {
        IPage<MarketingCertificateVO> marketingCertificateVOIPage = baseMapper.findCertificateVO(page,marketingCertificateVO);
        marketingCertificateVOIPage.getRecords().forEach(c->{
            if (StringUtils.isNotBlank(c.getMainPictures())){
                String s = (String) JSON.parseObject(c.getMainPicture()).get("0");
                c.setMainPictures(s);
            }
            if (c.getRewardStore().equals("0")){
                c.setStoreQuantity("全平台");
            }
        });
        return marketingCertificateVOIPage;
    }

    @Override
    public IPage<Map<String, Object>> findMarketingCertificateList(Page<Map<String, Object>> page,String name) {
        return baseMapper.findMarketingCertificateList(page,name);
    }

    @Override
    public Map<String, Object> findMarketingCertificateInfo(String id) {
        return baseMapper.findMarketingCertificateInfo(id);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingCertificateBySysUserId(Page<Map<String, Object>> page, String sysUserId) {
        return baseMapper.findMarketingCertificateBySysUserId(page,sysUserId);
    }

    @Override
    public Result<MarketingCertificate> edit(MarketingCertificateVO marketingCertificateVO) {
        Result<MarketingCertificate> result = new Result<MarketingCertificate>();
        MarketingCertificate marketingCertificateEntity = this.getById(marketingCertificateVO.getId());

        if (oConvertUtils.isEmpty(marketingCertificateEntity)) {
            result.error500("未找到对应实体");
        } else {
            JSONArray goodListIds = JSON.parseArray(marketingCertificateVO.getGoodListIds());
            String sysUserIds = marketingCertificateVO.getSysUserIds();
            MarketingCertificate marketingCertificate = new MarketingCertificate();
            BeanUtils.copyProperties(marketingCertificateVO, marketingCertificate);
            if("1".equals(marketingCertificateVO.getVouchersWay())){
                marketingCertificate.setDisData(marketingCertificateVO.getToday());
            }
            if("2".equals(marketingCertificateVO.getVouchersWay())){
                marketingCertificate.setDisData(marketingCertificateVO.getTomorow());
            }
            marketingCertificate.setMainPicture(marketingCertificateVO.getMainPictures());
            marketingCertificate.setCoverPlan(marketingCertificateVO.getCoverPlans());
            if (oConvertUtils.isNotEmpty(goodListIds)) {
                //查询出对应的商品
                QueryWrapper<MarketingCertificateGood> marketingCertificateGoodQueryWrapper = new QueryWrapper<>();
                marketingCertificateGoodQueryWrapper.eq("marketing_certificate_id", marketingCertificateEntity.getId());
                //删除对应的商品
                iMarketingCertificateGoodService.remove(marketingCertificateGoodQueryWrapper);
                //添加新的商品
                for (int i = 0; i < goodListIds.size(); i++) {
                    iMarketingCertificateGoodService.save(new MarketingCertificateGood()
                            .setDelFlag("0")
                            .setMarketingCertificateId(marketingCertificateEntity.getId())
                            .setGoodListId(goodListIds.getJSONObject(i).getString("id"))
                            .setIsPlatform("1")
                            .setCanMonth(goodListIds.getJSONObject(i).getString("canMonth"))
                            .setGoodSpecificationId(goodListIds.getJSONObject(i).getString("goodSpecificationId"))
                            .setQuantity(goodListIds.getJSONObject(i).getBigDecimal("quantity"))
                    );
                }
            }else {
                //查询出对应的商品
                QueryWrapper<MarketingCertificateGood> marketingCertificateGoodQueryWrapper = new QueryWrapper<>();
                marketingCertificateGoodQueryWrapper.eq("marketing_certificate_id", marketingCertificateEntity.getId());
                //删除对应的商品
                iMarketingCertificateGoodService.remove(marketingCertificateGoodQueryWrapper);
            }
            if (StringUtils.isNotBlank(sysUserIds)) {
                //查询出核销的门店
                QueryWrapper<MarketingCertificateStore> marketingCertificateStoreQueryWrapper = new QueryWrapper<>();
                marketingCertificateStoreQueryWrapper.eq("marketing_certificate_id", marketingCertificateEntity.getId());
                //删除核销的门店
                iMarketingCertificateStoreService.remove(marketingCertificateStoreQueryWrapper);
                List<String> strings = Arrays.asList(StringUtils.split(sysUserIds, ","));
                for (String s : strings) {
                    StoreManage storeManageServiceById = iStoreManageService.getById(s);
                    String sysUserId = storeManageServiceById.getSysUserId();
                    MarketingCertificateStore marketingCertificateStore = new MarketingCertificateStore();
                    marketingCertificateStore.setSysUserId(sysUserId);
                    marketingCertificateStore.setMarketingCertificateId(marketingCertificateEntity.getId());
                    marketingCertificateStore.setDelFlag("0");
                    iMarketingCertificateStoreService.saveOrUpdate(marketingCertificateStore);
                }
            }else {
                //查询出核销的门店
                QueryWrapper<MarketingCertificateStore> marketingCertificateStoreQueryWrapper = new QueryWrapper<>();
                marketingCertificateStoreQueryWrapper.eq("marketing_certificate_id", marketingCertificateEntity.getId());
                //删除核销的门店
                iMarketingCertificateStoreService.remove(marketingCertificateStoreQueryWrapper);
            }
            //编辑兑换券
            boolean ok = this.updateById(marketingCertificate);
            if (ok) {
                result.success("修改成功!");
            } else {
                result.error500("修改失败!");
            }
        }

        return result;
    }

    @Override
    public List<MarketingCertificateVO> findCertificate(MarketingCertificateVO marketingCertificateVO) {
        return baseMapper.findCertificate(marketingCertificateVO);
    }

    @Override
    public List<MarketingCertificateVO> findGiveMarketingCertificateVO(MarketingCertificateVO marketingCertificateVO) {
        return baseMapper.findGiveMarketingCertificateVO(marketingCertificateVO);
    }

    @Override
    public List<Map<String, Object>> pageListBySout(int sout) {
        return baseMapper.pageListBySout(sout);
    }

    @Override
    @Transactional
    public  List<MarketingCertificateRecord> generate(String marketingCertificateId,
                                                      String sysUserId,
                                                      BigDecimal quantity,
                                                      String memberId,
                                                      Boolean isContinuous) {
        MarketingCertificate marketingCertificate=this.getById(marketingCertificateId);

        int certificateCount = quantity.intValue();

        Calendar myCalendar = Calendar.getInstance();

        List<MarketingCertificateRecord> marketingCertificateRecords= Lists.newArrayList();


        if(marketingCertificate==null){
            return marketingCertificateRecords;
        }



        //判断券剩余的数量够不够,不够弹出
        while (certificateCount > 0) {
            if (marketingCertificate.getTotal().subtract(marketingCertificate.getReleasedQuantity()).longValue() <= 0) {
                break;
            }
            MarketingCertificateRecord marketingCertificateRecord = new MarketingCertificateRecord();
            marketingCertificateRecord.setMemberListId(memberId);
            marketingCertificateRecord.setMarketingCertificateId(marketingCertificate.getId());
            marketingCertificateRecord.setName(marketingCertificate.getName());
            marketingCertificateRecord.setIsPlatform("1");
            marketingCertificateRecord.setQqzixuangu(OrderNoUtils.getOrderNo());
            marketingCertificateRecord.setIsGive(marketingCertificate.getIsGive());
            marketingCertificateRecord.setIsWarn(marketingCertificate.getIsWarn());
            marketingCertificateRecord.setWarnDays(marketingCertificate.getWarnDays());
            marketingCertificateRecord.setUserRestrict(marketingCertificate.getUserRestrict());
            marketingCertificateRecord.setDiscountExplain(marketingCertificate.getDiscountExplain());
            marketingCertificateRecord.setTheReward(marketingCertificate.getTheReward());
            marketingCertificateRecord.setMainPicture(marketingCertificate.getMainPicture());
            marketingCertificateRecord.setRewardStore(marketingCertificate.getRewardStore());
            marketingCertificateRecord.setIsNomal(marketingCertificate.getIsNomal());
            marketingCertificateRecord.setMarketPrice(marketingCertificate.getMarketPrice());
            marketingCertificateRecord.setPrice(marketingCertificate.getPrice());
            marketingCertificateRecord.setCostPrice(marketingCertificate.getCostPrice());
            marketingCertificateRecord.setCertificateType(marketingCertificate.getCertificateType());
            marketingCertificateRecord.setSellRewardStore(marketingCertificate.getSellRewardStore());
            marketingCertificateRecord.setRewardDayOne(marketingCertificate.getRewardDayOne());
            marketingCertificateRecord.setCoverPlan(marketingCertificate.getCoverPlan());
            marketingCertificateRecord.setPosters(marketingCertificate.getPosters());
            //sysUserId为空时,是在平台购买的,不为空是在店铺购买
            if (StringUtils.isBlank(sysUserId)){
                marketingCertificateRecord.setIsBuyPlatform("1");
            }else {

                if (marketingCertificate.getSellRewardStore().equals("1")){
                    marketingCertificateRecord.setSysUserId(sysUserId);
                    marketingCertificateRecord.setVerificationPeople("1");
                    marketingCertificateRecord.setIsBuyPlatform("0");
                }else {
                    marketingCertificateRecord.setIsBuyPlatform("1");
                }
            }
            marketingCertificateRecord.setSellRewardStore(marketingCertificate.getSellRewardStore());
            marketingCertificateRecord.setRewardDayOne(marketingCertificate.getRewardDayOne());
            marketingCertificateRecord.setCoverPlan(marketingCertificate.getCoverPlan());
            marketingCertificateRecord.setPosters(marketingCertificate.getPosters());
//选择线上线下核销
            marketingCertificateRecord.setAboveUse(marketingCertificate.getAboveUse());
            marketingCertificateRecord.setBelowUse(marketingCertificate.getBelowUse());
            //平台渠道判断
            QueryWrapper<MarketingChannel> marketingChannelQueryWrapper = new QueryWrapper<>();
            marketingChannelQueryWrapper.eq("english_name", "NORMAL_TO_GET");
            MarketingChannel marketingChannel = iMarketingChannelService.getOne(marketingChannelQueryWrapper);
            if (marketingChannel != null) {
                marketingCertificateRecord.setMarketingChannelId(marketingChannel.getId());
                marketingCertificateRecord.setTheChannel(marketingChannel.getName());
            }
            //标准用券方式
            if (marketingCertificate.getVouchersWay().equals("0")) {
                //优惠券的时间生成
                marketingCertificateRecord.setStartTime(marketingCertificate.getStartTime());
                marketingCertificateRecord.setEndTime(marketingCertificate.getEndTime());
            }

            //领券当日起
            if (marketingCertificate.getVouchersWay().equals("1")) {
                //优惠券的时间生成
                if(!isContinuous){
                    myCalendar = Calendar.getInstance();
                }
                Calendar instance = Calendar.getInstance();
                marketingCertificateRecord.setStartTime(myCalendar.getTime());

                if (marketingCertificate.getMonad().equals("天")) {
                    instance.add(Calendar.DATE, marketingCertificate.getDisData().intValue());
                }
                if (marketingCertificate.getMonad().equals("周")) {
                    instance.add(Calendar.WEEK_OF_MONTH, marketingCertificate.getDisData().intValue());
                }
                if (marketingCertificate.getMonad().equals("月")) {
                    instance.add(Calendar.MONTH, marketingCertificate.getDisData().intValue());
                }

                marketingCertificateRecord.setEndTime(instance.getTime());
            }
            //领券次日起
            if (marketingCertificate.getVouchersWay().equals("2")) {
                //优惠券的时间生成
                if(!isContinuous){
                    myCalendar = Calendar.getInstance();
                }
                myCalendar.add(Calendar.DATE, 1);
                Calendar instance = Calendar.getInstance();
                marketingCertificateRecord.setStartTime(myCalendar.getTime());

                if (marketingCertificate.getMonad().equals("天")) {
                    instance.add(Calendar.DATE, marketingCertificate.getDisData().intValue());
                }
                if (marketingCertificate.getMonad().equals("周")) {
                    instance.add(Calendar.WEEK_OF_MONTH, marketingCertificate.getDisData().intValue());
                }
                if (marketingCertificate.getMonad().equals("月")) {
                    instance.add(Calendar.MONTH, marketingCertificate.getDisData().intValue());
                }

                marketingCertificateRecord.setEndTime(instance.getTime());
            }
            if (new Date().getTime() >= marketingCertificateRecord.getStartTime().getTime() && new Date().getTime() <= marketingCertificateRecord.getEndTime().getTime()) {
                //设置生效
                marketingCertificateRecord.setStatus("1");
            } else {
                marketingCertificateRecord.setStatus("0");
            }
            iMarketingCertificateRecordService.save(marketingCertificateRecord);
            marketingCertificate.setReleasedQuantity(marketingCertificate.getReleasedQuantity().add(new BigDecimal(1)));
            this.saveOrUpdate(marketingCertificate);
            marketingCertificateRecords.add(marketingCertificateRecord);
            certificateCount--;
        }
        return marketingCertificateRecords;
    }

    @Override
    public List<MarketingCertificateVO> findCertificateData(MarketingCertificateVO marketingCertificateVO) {
        return baseMapper.findCertificateData(marketingCertificateVO);
    }

    @Override
    public List<MarketingCertificateVO> findCertificateDataByType(MarketingCertificateVO marketingCertificateVO) {
        return baseMapper.findCertificateDataByType(marketingCertificateVO);
    }
}
