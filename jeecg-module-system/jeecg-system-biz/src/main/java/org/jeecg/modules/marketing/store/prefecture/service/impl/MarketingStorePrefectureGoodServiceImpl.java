package org.jeecg.modules.marketing.store.prefecture.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGive;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGood;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureRecord;
import org.jeecg.modules.marketing.store.prefecture.mapper.MarketingStorePrefectureGoodMapper;
import org.jeecg.modules.marketing.store.prefecture.service.*;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.service.IPayOrderCarLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 店铺专区商品
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Service
@Log
public class MarketingStorePrefectureGoodServiceImpl extends ServiceImpl<MarketingStorePrefectureGoodMapper, MarketingStorePrefectureGood> implements IMarketingStorePrefectureGoodService {

    @Autowired
    private IMarketingStorePrefectureListService iMarketingStorePrefectureListService;


    @Autowired
    private IMarketingStorePrefectureRecordService iMarketingStorePrefectureRecordService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;


    @Autowired
    private IMarketingStorePrefectureDividendService iMarketingStorePrefectureDividendService;


    @Autowired
    private IMarketingStorePrefectureGiveService iMarketingStorePrefectureGiveService;

    @Autowired
    private IPayOrderCarLogService iPayOrderCarLogService;


    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper wrapper, String goodName) {
        return baseMapper.queryPageList(page,wrapper,goodName);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingStorePrefectureGoodList(Page<Map<String, Object>> page,String marketingStorePrefectureGoodListId) {
        IPage<Map<String, Object>> iPage=baseMapper.getMarketingStorePrefectureGoodList(page,marketingStorePrefectureGoodListId);
        iPage.getRecords().forEach(gsl->{
           String specifications= gsl.get("specifications").toString();
           Double smallPrice=0d;
            for(Object sp :JSON.parseArray(specifications)){
                JSONObject jsonObject=(JSONObject) sp;
                Double activePrice=jsonObject.getDouble("activePrice");
                if(smallPrice.doubleValue()==0){
                    smallPrice=activePrice;
                }
                if(smallPrice.doubleValue()>activePrice.doubleValue()){
                    smallPrice=activePrice;
                }
            }
            gsl.put("smallPrice",smallPrice);
            MarketingStorePrefectureGood marketingStorePrefectureGood=this.getById(gsl.get("marketingStorePrefectureGoodId").toString());
            MarketingStorePrefectureList marketingStorePrefectureList=iMarketingStorePrefectureListService.getById(marketingStorePrefectureGood.getMarketingStorePrefectureListId());
            gsl.put("prefectureLabel",marketingStorePrefectureList.getPrefectureName());
        });
        return iPage;
    }

    @Override
    public void settingGoodInfo(Map<String, Object> objectMap, String id) {
        MarketingStorePrefectureGood marketingStorePrefectureGood=this.getById(id);
        MarketingStorePrefectureList marketingStorePrefectureList=iMarketingStorePrefectureListService.getById(marketingStorePrefectureGood.getMarketingStorePrefectureListId());
        String specifications= marketingStorePrefectureGood.getSpecifications().toString();
        Double smallPrice=0d;
        for(Object sp :JSON.parseArray(specifications)){
            JSONObject jsonObject=(JSONObject) sp;
            Double activePrice=jsonObject.getDouble("activePrice");
            if(smallPrice.doubleValue()==0){
                smallPrice=activePrice;
            }
            if(smallPrice.doubleValue()>activePrice.doubleValue()){
                smallPrice=activePrice;
            }
        }
        objectMap.put("smallPrice",smallPrice);
        objectMap.put("isViewShopCar",0);
        objectMap.put("isViewVipPrice","0");
        objectMap.put("isPrefectureGood","9");
        objectMap.put("prefectureType",marketingStorePrefectureList.getPrefectureType());
        objectMap.put("astrict",marketingStorePrefectureList.getAstrict());
        objectMap.put("give",marketingStorePrefectureList.getGive());
        objectMap.put("label",marketingStorePrefectureList.getPrefectureName());
        objectMap.put("marketingStorePrefectureListId",marketingStorePrefectureList.getId());
        objectMap.put("marketingStorePrefectureGoodId",id);
    }

    @Override
    public void settingGoodSpecification(Map<String, Object> objectMap, String id,String specification) {
        MarketingStorePrefectureGood marketingStorePrefectureGood=this.getById(id);
        MarketingStorePrefectureList marketingStorePrefectureList=iMarketingStorePrefectureListService.getById(marketingStorePrefectureGood.getMarketingStorePrefectureListId());
        String specifications= marketingStorePrefectureGood.getSpecifications().toString();
        for(Object sp :JSON.parseArray(specifications)){
            JSONObject jsonObject=(JSONObject) sp;
            if(jsonObject.getString("specification").equals(specification)){
                BigDecimal price=jsonObject.getBigDecimal("activePrice");
                objectMap.put("price", price);
                break;
            }
        }
        objectMap.put("label",marketingStorePrefectureList.getPrefectureName());
    }

    @Override
    public void settingMemberShoppingCar(MemberShoppingCart memberShoppingCart, String id, String specification) {
        MarketingStorePrefectureGood marketingStorePrefectureGood=this.getById(id);
        MarketingStorePrefectureList marketingStorePrefectureList=iMarketingStorePrefectureListService.getById(marketingStorePrefectureGood.getMarketingStorePrefectureListId());
        String specifications= marketingStorePrefectureGood.getSpecifications().toString();
        for(Object sp :JSON.parseArray(specifications)){
            JSONObject jsonObject=(JSONObject) sp;
            if(jsonObject.getString("specification").equals(specification)){
                BigDecimal price=jsonObject.getBigDecimal("activePrice");
                memberShoppingCart.setAddPrice(price);
                break;
            }
        }
        memberShoppingCart.setMarketingStorePrefectureGoodId(id);
        memberShoppingCart.setPrefectureLabel(marketingStorePrefectureList.getPrefectureName());
    }

    @Override
    public void settingGetMemberShoppingCarInfo(Map<String, Object> objectMap, String id, String specification) {
        MarketingStorePrefectureGood marketingStorePrefectureGood=this.getById(id);
        MarketingStorePrefectureList marketingStorePrefectureList=iMarketingStorePrefectureListService.getById(marketingStorePrefectureGood.getMarketingStorePrefectureListId());
        String specifications= marketingStorePrefectureGood.getSpecifications().toString();
        for(Object sp :JSON.parseArray(specifications)){
            JSONObject jsonObject=(JSONObject) sp;
            if(jsonObject.getString("specification").equals(specification)){
                BigDecimal price=jsonObject.getBigDecimal("activePrice");
                objectMap.put("price",price);
                break;
            }
        }
        objectMap.put("label",marketingStorePrefectureList.getPrefectureName());
    }

    @Override
    @Transactional
    public void success(OrderStoreList orderStoreList,String payOrderCarLogId) {
        log.info("店铺专区订单交易成功:"+orderStoreList.getOrderNo());
        PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getById(payOrderCarLogId);
        MarketingStorePrefectureGood marketingStorePrefectureGood=this.getById(orderStoreList.getActiveId());
        MarketingStorePrefectureList marketingStorePrefectureList=iMarketingStorePrefectureListService.getById(marketingStorePrefectureGood.getMarketingStorePrefectureListId());
        GoodStoreList goodStoreList=iGoodStoreListService.getById(marketingStorePrefectureGood.getGoodStoreListId());
        /*生成交易记录*/
        MarketingStorePrefectureRecord marketingStorePrefectureRecord=new MarketingStorePrefectureRecord();
        marketingStorePrefectureRecord.setMemberListId(orderStoreList.getMemberListId());
        marketingStorePrefectureRecord.setMainPicture(goodStoreList.getMainPicture());
        marketingStorePrefectureRecord.setGoodName(goodStoreList.getGoodName());
        marketingStorePrefectureRecord.setGoodNo(goodStoreList.getGoodNo());
        marketingStorePrefectureRecord.setPrice(orderStoreList.getActualPayment());
        marketingStorePrefectureRecord.setGive(marketingStorePrefectureList.getGive());
        marketingStorePrefectureRecord.setMarketingStorePrefectureGoodId(marketingStorePrefectureGood.getId());
        marketingStorePrefectureRecord.setMarketingStorePrefectureListId(marketingStorePrefectureList.getId());
        marketingStorePrefectureRecord.setOrderStoreListId(orderStoreList.getId());
        marketingStorePrefectureRecord.setTMemberId(payOrderCarLog.getTMemberId());
        iMarketingStorePrefectureRecordService.save(marketingStorePrefectureRecord);
        //生成奖励
        iMarketingStorePrefectureDividendService.paymentIncentives(payOrderCarLogId,marketingStorePrefectureList ,marketingStorePrefectureRecord.getId());
        /*生成限制记录*/
        if(marketingStorePrefectureList.getPrefectureType().equals("2")){
            JSONObject give=JSON.parseObject(marketingStorePrefectureList.getGive());
            if(StringUtils.isNotBlank(give.getString("marketingStorePrefectureListId"))){
                MarketingStorePrefectureList astrictStorePrefectureList=iMarketingStorePrefectureListService.getById(give.getString("marketingStorePrefectureListId"));
                if(astrictStorePrefectureList!=null){
                    MarketingStorePrefectureGive marketingStorePrefectureGive=iMarketingStorePrefectureGiveService.getOne(new LambdaQueryWrapper<MarketingStorePrefectureGive>().eq(MarketingStorePrefectureGive::getMarketingStorePrefectureListId,astrictStorePrefectureList.getId()).eq(MarketingStorePrefectureGive::getMemberListId,orderStoreList.getMemberListId()));
                    if(marketingStorePrefectureGive==null){
                        JSONObject astrictObject=JSON.parseObject(astrictStorePrefectureList.getAstrict());
                        marketingStorePrefectureGive=new MarketingStorePrefectureGive();
                        marketingStorePrefectureGive.setMarketingStorePrefectureListId(astrictStorePrefectureList.getId());
                        marketingStorePrefectureGive.setMemberListId(orderStoreList.getMemberListId());
                        marketingStorePrefectureGive.setTotalTimes(give.getBigDecimal("totalTimes"));
                        marketingStorePrefectureGive.setTime(astrictObject.getBigDecimal("time"));
                        marketingStorePrefectureGive.setModel(astrictObject.getString("model"));
                    }else{
                        marketingStorePrefectureGive.setTotalTimes(marketingStorePrefectureGive.getTotalTimes().add(give.getBigDecimal("totalTimes")));
                    }
                    iMarketingStorePrefectureGiveService.saveOrUpdate(marketingStorePrefectureGive);
                }
            }
        }
        /*限制商品订单*/
        if(marketingStorePrefectureList.getPrefectureType().equals("1")){
            MarketingStorePrefectureGive marketingStorePrefectureGive=iMarketingStorePrefectureGiveService.getOne(new LambdaQueryWrapper<MarketingStorePrefectureGive>().eq(MarketingStorePrefectureGive::getMarketingStorePrefectureListId,marketingStorePrefectureList.getId()).eq(MarketingStorePrefectureGive::getMemberListId,orderStoreList.getMemberListId()));
            if(marketingStorePrefectureGive!=null){
                marketingStorePrefectureGive.setTradetime(new Date());
                marketingStorePrefectureGive.setTotalTimes(marketingStorePrefectureGive.getTotalTimes().subtract(new BigDecimal(1)));
                iMarketingStorePrefectureGiveService.saveOrUpdate(marketingStorePrefectureGive);
            }
        }
    }
}
