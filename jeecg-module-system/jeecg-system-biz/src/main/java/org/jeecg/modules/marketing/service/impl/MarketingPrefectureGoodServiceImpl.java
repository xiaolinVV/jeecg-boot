package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.vo.SearchTermsVO;
import org.jeecg.modules.marketing.dto.MarketingPrefectureGoodDTO;
import org.jeecg.modules.marketing.dto.MarketingPrefectureGoodSpecificationDTO;
import org.jeecg.modules.marketing.entity.MarketingAdvertisingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;
import org.jeecg.modules.marketing.mapper.MarketingPrefectureGoodMapper;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodSpecificationService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.vo.MarketingPrefectureGoodSpecificationVO;
import org.jeecg.modules.marketing.vo.MarketingPrefectureGoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingPrefectureGoodServiceImpl extends ServiceImpl<MarketingPrefectureGoodMapper, MarketingPrefectureGood> implements IMarketingPrefectureGoodService {
    @Autowired
    private IMarketingPrefectureGoodSpecificationService  marketingPrefectureGoodSpecificationService;
    @Autowired
    private IMarketingAdvertisingPrefectureService iMarketingAdvertisingPrefectureService;
    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;
    @Autowired
    @Lazy
    private IGoodListService goodListService;
    @Override
    public  IPage<MarketingPrefectureGoodDTO> getMarketingPrefectureGood(Page page,MarketingPrefectureGoodVO marketingPrefectureGoodVO){
        IPage<MarketingPrefectureGoodDTO> pageList = baseMapper.getMarketingPrefectureGood(page,marketingPrefectureGoodVO);
        pageList.getRecords().forEach(marketingPrefectureGoodDTO -> {
            QueryWrapper<MarketingPrefectureGoodSpecification> queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag","0");
            queryWrapper.eq("marketing_prefecture_good_id",marketingPrefectureGoodDTO.getId());
            List<MarketingPrefectureGoodSpecificationDTO> marketingPrefectureGoodSpecificationListDTO =   marketingPrefectureGoodSpecificationService.getMarketingPrefectureGoodSpecification(marketingPrefectureGoodDTO.getId());
            marketingPrefectureGoodDTO.setGoodListSpecificationList(marketingPrefectureGoodSpecificationListDTO);

        });
        return pageList;
  }

    @Override
    public IPage<Map<String, Object>> findByMarketingPrefectureType(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findByMarketingPrefectureType(page,paramObjectMap);
    }


    /**
     * 查询专区商品返回商品id 商品名称
     * @param marketingPrefectureId
     * @return
     */
    @Override
    public List<Map<String, Object>> getMarketingPrefectureGoodName(String marketingPrefectureId){
     return   baseMapper.getMarketingPrefectureGoodName(marketingPrefectureId);
    }

    @Override
    public IPage<Map<String, Object>> findByMarketingPrefectureIdAndSearch(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findByMarketingPrefectureIdAndSearch(page,paramMap);
    }


    /**
     * 删除商品 关联删除  广告
     */
    @Override
    public void linkToDelete(String ids,String marketingPrefectureId,String goodListId) {
        Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            List<MarketingPrefectureGoodSpecification> marketingPrefectureGoodSpecificationList;

            List<String> listid = Arrays.asList(ids.split(","));
            QueryWrapper<MarketingPrefectureGoodSpecification> queryWrapperMarketingPrefectureGoodSpecification = new QueryWrapper();

                //规格
                queryWrapperMarketingPrefectureGoodSpecification = new QueryWrapper();
                queryWrapperMarketingPrefectureGoodSpecification.in("marketing_prefecture_good_id",listid);
                marketingPrefectureGoodSpecificationList =  marketingPrefectureGoodSpecificationService.list(queryWrapperMarketingPrefectureGoodSpecification);
                List<String> marketingPrefectureGoodidSpecificationIds = new ArrayList<>();
                for(MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification:marketingPrefectureGoodSpecificationList){
                    marketingPrefectureGoodidSpecificationIds.add(marketingPrefectureGoodSpecification.getId()) ;
                }
                //删除规格
            marketingPrefectureGoodSpecificationService.removeByIds(marketingPrefectureGoodidSpecificationIds);

            //专区广告
            List<MarketingAdvertisingPrefecture>  marketingAdvertisingPrefectureList;
            List<String> marketingAdvertisingPrefectureIds = new ArrayList<>();
            QueryWrapper<MarketingAdvertisingPrefecture> queryWrapperMarketingAdvertisingPrefecture = new QueryWrapper();
            queryWrapperMarketingAdvertisingPrefecture.eq("marketing_prefecture_id", marketingPrefectureId);
            queryWrapperMarketingAdvertisingPrefecture.eq("good_list_id", goodListId);
            marketingAdvertisingPrefectureList = iMarketingAdvertisingPrefectureService.list(queryWrapperMarketingAdvertisingPrefecture);
            for(MarketingAdvertisingPrefecture marketingAdvertisingPrefecture:marketingAdvertisingPrefectureList){
                marketingAdvertisingPrefectureIds.add(marketingAdvertisingPrefecture.getId()) ;
            }
            //删除专区广告
            if(marketingAdvertisingPrefectureIds.size()>0){
                iMarketingAdvertisingPrefectureService.removeByIds(marketingAdvertisingPrefectureIds);
            }

        }
    }



    /**
     * 修改专区商品启用判断是否可启用
     */
    @Override
    public Map<String,Object> linkToUpdate(String marketingPrefectureGoodId) {

        Map<String,Object> map = Maps.newHashMap();
         MarketingPrefectureGood marketingPrefectureGood = baseMapper.selectById(marketingPrefectureGoodId);
       if(marketingPrefectureGood ==null){
           map.put("data","1");// 0 :修改成功 1:修改失败
           map.put("msg","专区商品未找到！");

           return map;
       }
       QueryWrapper<MarketingPrefecture> queryWrapperMarketingPrefecture = new QueryWrapper();
        queryWrapperMarketingPrefecture.eq("id",marketingPrefectureGood.getMarketingPrefectureId());
        queryWrapperMarketingPrefecture.eq("status","1");
        MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getOne(queryWrapperMarketingPrefecture);
        if (marketingPrefecture == null) {
            map.put("data","1");// 0 :修改成功 1:修改失败
            map.put("msg","专区被停用,先去启用专区！");
            return map;
        }
        marketingPrefectureGood.setStatus("1");
        baseMapper.updateById(marketingPrefectureGood);
        map.put("data", "0");// 0 :修改成功 1:修改失败
        map.put("msg", "可以启用!");

        return map;
    }
    /**
     * 修改专区商品
     * @param marketingPrefectureGoodVO
     * @param marketingPrefectureGood
     */
    @Override
    @Transactional
    public void updateMarketingPrefectureGoodAndSpecification(MarketingPrefectureGoodVO marketingPrefectureGoodVO,MarketingPrefectureGood marketingPrefectureGood){
        marketingPrefectureGood.setMarketingPrefectureTypeId(marketingPrefectureGoodVO.getMarketingPrefectureTypeId());//平台专区类型id
        marketingPrefectureGood.setStatus(marketingPrefectureGoodVO.getStatus());//状态；0：停用；1：启用
        marketingPrefectureGood.setSrcStatus(marketingPrefectureGoodVO.getSrcStatus());//原商品是否可用；0：不可用；1：可用
        marketingPrefectureGood.setSort(marketingPrefectureGoodVO.getSort());
        BigDecimal[] ArrPrefecturePrice =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()]  ;//专区价
        BigDecimal[] ArrWelfareProportion =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()] ;//福利金限额抵扣比例
        BigDecimal[] ArrBuyProportionDay =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()] ;//可购买件数；-1：不限制；其他代表件数
        BigDecimal[] ArrBuyProportionLetter =new BigDecimal[marketingPrefectureGoodVO.getGoodListSpecificationList().size()] ;  //可购买件数；-1：不限制；其他代表件数
        String isWelfare="";//福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣
        String isGiveWelfare=""; //赠送福利金；0：不支持；1：支持
        if (marketingPrefectureGoodVO.getMarketingPrefectureTypeId().contains(",")){
            List<String> strings = Arrays.asList(org.apache.commons.lang3.StringUtils.split(marketingPrefectureGoodVO.getMarketingPrefectureTypeId(), ","));
            marketingPrefectureGood.setMarketingPrefectureTypeId(strings.get(1));
        }else {
            marketingPrefectureGood.setMarketingPrefectureTypeId(marketingPrefectureGoodVO.getMarketingPrefectureTypeId());//平台专区类型id
        }
        for(int a=0;marketingPrefectureGoodVO.getGoodListSpecificationList().size()>a;a++ ){
            isWelfare = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getIsWelfare();
            isGiveWelfare = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getIsGiveWelfare();
            ArrPrefecturePrice[a] = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getPrefecturePrice();
            ArrWelfareProportion[a] = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getWelfareProportion();//福利金限额抵扣比例
            ArrBuyProportionDay[a] =  marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getBuyProportionDay();
            ArrBuyProportionLetter[a] = marketingPrefectureGoodVO.getGoodListSpecificationList().get(a).getBuyProportionLetter();
        }
        marketingPrefectureGood.setIsWelfare(isWelfare);
        marketingPrefectureGood.setIsGiveWelfare(isGiveWelfare);
        //marketingPrefectureGood.setIsWelfare(marketingPrefectureGoodVO.)

        ArrPrefecturePrice= maopaoSort(ArrPrefecturePrice);//专区价
        if(ArrPrefecturePrice[0].compareTo(ArrPrefecturePrice[ArrPrefecturePrice.length-1])==0){//两数相等
            marketingPrefectureGood.setPrefecturePrice(ArrPrefecturePrice[0].toString());//商品销售价格
            marketingPrefectureGood.setSmallPrefecturePrice(ArrPrefecturePrice[0]);
        }else{
            marketingPrefectureGood.setPrefecturePrice(ArrPrefecturePrice[0]+"--"+ArrPrefecturePrice[ArrPrefecturePrice.length-1]);
            marketingPrefectureGood.setSmallPrefecturePrice(ArrPrefecturePrice[0]);
        }
        ArrWelfareProportion =maopaoSort(ArrWelfareProportion);//福利金限额抵扣比例
        if(ArrWelfareProportion[0].compareTo(ArrWelfareProportion[ArrWelfareProportion.length-1])==0){//两数相等
            marketingPrefectureGood.setWelfareProportion(ArrWelfareProportion[0].toString());//商品销售价格

        }else{
            marketingPrefectureGood.setWelfareProportion(ArrWelfareProportion[0]+"--"+ArrWelfareProportion[ArrWelfareProportion.length-1]);
        }
        ArrBuyProportionDay =maopaoSort(ArrBuyProportionDay);//可购买件数；-1：不限制；其他代表件数
        if(ArrBuyProportionDay[0].compareTo(ArrBuyProportionDay[ArrBuyProportionDay.length-1])==0){//两数相等
            marketingPrefectureGood.setBuyProportionDay(ArrBuyProportionDay[0].toString());//商品销售价格

        }else{
            marketingPrefectureGood.setBuyProportionDay(ArrBuyProportionDay[0]+"--"+ArrBuyProportionDay[ArrBuyProportionDay.length-1]);
        }
        ArrBuyProportionLetter =maopaoSort(ArrBuyProportionLetter);  //可购买件数；-1：不限制；其他代表件数
        if(ArrBuyProportionLetter[0].compareTo(ArrBuyProportionLetter[ArrBuyProportionLetter.length-1])==0){//两数相等
            marketingPrefectureGood.setBuyProportionLetter(ArrBuyProportionLetter[0].toString());//商品销售价格

        }else{
            marketingPrefectureGood.setBuyProportionLetter(ArrBuyProportionLetter[0]+"--"+ArrBuyProportionLetter[ArrBuyProportionLetter.length-1]);
        }
        baseMapper.updateById(marketingPrefectureGood);
        MarketingPrefectureGoodSpecification  marketingPrefectureGoodSpecification;
        //修改专区规格数据
        for(MarketingPrefectureGoodSpecificationVO mps:marketingPrefectureGoodVO.getGoodListSpecificationList()){
            marketingPrefectureGoodSpecification= marketingPrefectureGoodSpecificationService.getById(mps.getId());
            if(marketingPrefectureGoodSpecification!=null){
                marketingPrefectureGoodSpecification.setPrefecturePrice(mps.getPrefecturePrice());//专区价
                marketingPrefectureGoodSpecification.setPrefecturePriceProportion(mps.getPrefecturePriceProportion());//专区折扣
                marketingPrefectureGoodSpecification.setIsWelfare(mps.getIsWelfare());//福利金抵扣；0：不支持福利金抵扣；1：福利金全额抵扣；2：福利金限额抵扣
                marketingPrefectureGoodSpecification.setWelfareProportion(mps.getWelfareProportion());//福利金限额抵扣比例
                marketingPrefectureGoodSpecification.setIsGiveWelfare(mps.getIsGiveWelfare());//赠送福利金；0：不支持；1：支持
                marketingPrefectureGoodSpecification.setGiveWelfareProportion(mps.getGiveWelfareProportion());//赠送福利金比例
                marketingPrefectureGoodSpecification.setBuyProportionDay(mps.getBuyProportionDay());//购买天数;-1：不限制；其他代表天数
                marketingPrefectureGoodSpecification.setBuyProportionLetter(mps.getBuyProportionLetter());//可购买件数；-1：不限制；其他代表件数
                marketingPrefectureGoodSpecificationService.updateById(marketingPrefectureGoodSpecification);
            }

        }
    }
    /**
     * 冒泡排序
     * @param a
     */
    public  BigDecimal[] maopaoSort(BigDecimal[] a){
        //外层循环，是需要进行比较的轮数，一共进行5次即可
        for(int i=0;i<a.length-1;i++){
            //内存循环，是每一轮中进行的两两比较
            for(int j=0;j<a.length-1;j++)
            {
                if(a[j].compareTo(a[j+1])  > 0)
                {
                    BigDecimal temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }
            //System.out.println("第"+(i+1)+"轮排序后的数组为: "+Arrays.toString(a));

        }
        return a;
    }


    /**
     * 商家端查询对应专区商品列表
     *
     * @param page
     * @param searchTermsVO
     * @return
     */
   public IPage<Map<String,Object>>  findByMarketingPrefectureIdAndSearchMerchant(Page<Map<String,Object>> page, SearchTermsVO searchTermsVO){
       return baseMapper.findByMarketingPrefectureIdAndSearchMerchant( page,  searchTermsVO);
       }

    @Override
    public IPage<Map<String, Object>> findByMarketingPrefectureId(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findByMarketingPrefectureId(page,paramObjectMap);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingPrefectureGoodList(Page<Map<String,Object>> page,MarketingPrefectureGoodDTO marketingPrefectureGoodDTO) {
        return baseMapper.findMarketingPrefectureGoodList(page,marketingPrefectureGoodDTO);
    }

    @Override
    public IPage<Map<String, Object>> findByMarketingPrefectureTypeOne(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findByMarketingPrefectureTypeOne(page,paramObjectMap);
    }

}
