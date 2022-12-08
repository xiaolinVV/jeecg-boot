package org.jeecg.modules.good.api;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodSpecificationService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/open/goodList")
public class OpenGoodListController {
    @Autowired
    private IGoodListService goodListService;
    @Autowired
    private IGoodSpecificationService goodSpecificationService;
    @Autowired
    private IProviderTemplateService providerTemplateService;

    @Autowired
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMarketingPrefectureTypeService iMarketingPrefectureTypeService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;


    /**
     * 专区商品提交
     *
     * @param goodListJson
     * @param goodSpecificationsJson
     * @param providerTemplateJson
     * @param prefectureType
     * @return
     */
    @RequestMapping("submitPrefectureGoods")
    @ResponseBody
    public Result<String> submitPrefectureGoods(String goodListJson,
                                                String goodSpecificationsJson,
                                                String providerTemplateJson,
                                                String prefectureType){
        Result<String> result=new Result<>();
        //会员价比例 28%
        String membershipRate = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "会员价比例"), "%");
        //供货价比例 12%
        String supplyPriceRatio = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "供货价比例"), "%");


        //组织商品数据
        GoodList goodList= JSON.parseObject(goodListJson,GoodList.class);

        goodList.setStatus("1");//状态：0：停用；1：启用
        goodList.setFrameStatus("0");//上下架；0：下架；1：上架
        goodList.setFrameExplain("接口上架");//上下架说明
        goodList.setAuditStatus("2");//审核状态：0:草稿；1：待审核；2：审核通过；3：审核不通过
        goodList.setGoodForm("0");//商品形态；0：常规商品；1：特价商品
        //解析规格
        List<GoodSpecification> goodSpecifications=JSON.parseArray(goodSpecificationsJson,GoodSpecification.class);


        GoodSpecification smallGoodSpecification=null;

        GoodSpecification bigGoodSpecification=null;


        //设置计算规格需要的价格
        for (GoodSpecification goodSpecification:goodSpecifications) {
            goodSpecification.setSupplyPrice(goodSpecification.getCostPrice().multiply(new BigDecimal(1).add(new BigDecimal(supplyPriceRatio).divide(new BigDecimal(100)))).setScale(2, RoundingMode.DOWN));//供货价
            goodSpecification.setVipPrice(goodSpecification.getPrice().subtract(goodSpecification.getSupplyPrice()).multiply(new BigDecimal(membershipRate).divide(new BigDecimal(100))).add(goodSpecification.getSupplyPrice()).setScale(2,RoundingMode.DOWN));//vip价格
            //获取最小价格的规格
            if(smallGoodSpecification==null){
                smallGoodSpecification=goodSpecification;
            }else{
                if(smallGoodSpecification.getPrice().subtract(goodSpecification.getPrice()).doubleValue()>0){
                    smallGoodSpecification=goodSpecification;
                }
            }
            //获取最大价格的规格
            if(bigGoodSpecification==null){
                bigGoodSpecification=goodSpecification;
            }else{
                if(bigGoodSpecification.getPrice().subtract(goodSpecification.getPrice()).doubleValue()<0){
                    bigGoodSpecification=goodSpecification;
                }
            }
        }
        //最低商品价格
        goodList.setSmallPrice(smallGoodSpecification.getPrice()+"");
        //最低vip价格
        goodList.setSmallVipPrice(smallGoodSpecification.getVipPrice()+"");
        //最低成本价
        goodList.setCostPrice(smallGoodSpecification.getCostPrice()+"");
        //最低供货价
        goodList.setSmallSupplyPrice(smallGoodSpecification.getSupplyPrice()+"");


        if(smallGoodSpecification.getPrice().subtract(bigGoodSpecification.getPrice()).doubleValue()<0){
            goodList.setPriceRange("1");
        }else{
            goodList.setPriceRange("0");
        }

        if(goodList.getPriceRange().equals("0")) {
            goodList.setPrice(smallGoodSpecification.getPrice()+"");
            goodList.setSupplyPrice(smallGoodSpecification.getSupplyPrice()+"");
            goodList.setVipPrice(smallGoodSpecification.getVipPrice()+"");
            goodList.setCostPrice(smallGoodSpecification.getCostPrice()+"");
        }else{
            goodList.setPrice(smallGoodSpecification.getPrice()+"--"+bigGoodSpecification.getPrice());
            goodList.setSupplyPrice(smallGoodSpecification.getSupplyPrice()+"--"+bigGoodSpecification.getSupplyPrice());
            goodList.setVipPrice(smallGoodSpecification.getVipPrice()+"--"+bigGoodSpecification.getVipPrice());
            goodList.setCostPrice(smallGoodSpecification.getCostPrice()+"--"+bigGoodSpecification.getCostPrice());
        }



        String marketPrice=bigGoodSpecification.getPrice().multiply(new BigDecimal(1.1)).setScale(2,RoundingMode.DOWN)+"";//商品市场价
        goodList.setMarketPrice(marketPrice);

        //查询商品是否存在
        QueryWrapper<GoodList> goodListQueryWrapper=new QueryWrapper<>();
        goodListQueryWrapper.eq("good_no",goodList.getGoodNo());
        if(goodListService.count(goodListQueryWrapper)>0){
            result.error500("商品已经存在，如需要请更新商品");
            return result;
        }

        //解析运费模板
        ProviderTemplate providerTemplate=JSON.parseObject(providerTemplateJson,ProviderTemplate.class);
        QueryWrapper<ProviderTemplate> providerTemplateQueryWrapper=new QueryWrapper<>();
        providerTemplateQueryWrapper.eq("freight_template_id",providerTemplate.getFreightTemplateId());
        if(providerTemplateService.count(providerTemplateQueryWrapper)==0) {
            //保存运费模板
            providerTemplateService.saveOrUpdate(providerTemplate);
        }else{
            providerTemplate=providerTemplateService.getOne(providerTemplateQueryWrapper);
        }

        //设置运费模板
        goodList.setProviderTemplateId(providerTemplate.getId());

        goodListService.list(new LambdaQueryWrapper<GoodList>().eq(GoodList::getGoodNo,goodList.getGoodNo())).forEach(g->{
            goodListService.removeById(g.getId());
            log.info("删除商品："+g.getId());
            iMarketingPrefectureGoodService.list(new LambdaQueryWrapper<MarketingPrefectureGood>().eq(MarketingPrefectureGood::getGoodListId,g.getId())).forEach(m->{
                iMarketingPrefectureGoodService.removeById(m.getId());
                log.info("删专区商品："+m.getId());
            });
        });
        //保存商品
        goodListService.saveOrUpdate(goodList);
        //保存规格
        for (GoodSpecification goodSpecification:goodSpecifications) {
            //设置商品id
            goodSpecification.setGoodListId(goodList.getId());
            goodSpecificationService.saveOrUpdate(goodSpecification);
        }
        //保存专区商品
        MarketingPrefectureType marketingPrefectureType=iMarketingPrefectureTypeService.getById(prefectureType);
        MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(marketingPrefectureType.getMarketingPrefectureId());
        MarketingPrefectureGood marketingPrefectureGood=new MarketingPrefectureGood();
        marketingPrefectureGood.setMarketingPrefectureId(marketingPrefecture.getId());
        marketingPrefectureGood.setMarketingPrefectureTypeId(marketingPrefectureType.getId());
        marketingPrefectureGood.setGoodListId(goodList.getId());
        marketingPrefectureGood.setStatus("0");
        marketingPrefectureGood.setSrcStatus("1");
        marketingPrefectureGood.setPrefecturePrice(goodList.getPrice());
        marketingPrefectureGood.setIsWelfare("1");
        marketingPrefectureGood.setWelfareProportion("100");
        marketingPrefectureGood.setIsGiveWelfare("0");
        marketingPrefectureGood.setGiveWelfareProportion("0");
        marketingPrefectureGood.setBuyProportionDay("-1");
        marketingPrefectureGood.setBuyProportionLetter("-1");
        marketingPrefectureGood.setSmallPrefecturePrice(new BigDecimal(goodList.getSmallPrice()));
        marketingPrefectureGood.setIsVipLower("0");
        iMarketingPrefectureGoodService.saveOrUpdate(marketingPrefectureGood);

        for (GoodSpecification goodSpecification:goodSpecifications) {
            MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification=new MarketingPrefectureGoodSpecification();
            marketingPrefectureGoodSpecification.setMarketingPrefectureGoodId(marketingPrefectureGood.getId());
            marketingPrefectureGoodSpecification.setGoodSpecificationId(goodSpecification.getId());
            marketingPrefectureGoodSpecification.setPrefecturePrice(goodSpecification.getPrice());
            marketingPrefectureGoodSpecification.setPrefecturePriceProportion(new BigDecimal(100));
            marketingPrefectureGoodSpecification.setIsWelfare("1");
            marketingPrefectureGoodSpecification.setWelfareProportion(new BigDecimal(100));
            marketingPrefectureGoodSpecification.setIsGiveWelfare("0");
            marketingPrefectureGoodSpecification.setGiveWelfareProportion(new BigDecimal(0));
            marketingPrefectureGoodSpecification.setBuyProportionDay(new BigDecimal(-1));
            marketingPrefectureGoodSpecification.setBuyProportionLetter(new BigDecimal(-1));
            marketingPrefectureGoodSpecification.setIsVipLower("0");
            iMarketingPrefectureGoodSpecificationService.saveOrUpdate(marketingPrefectureGoodSpecification);
        }

        result.setResult("商品添加成功");
        return result;
    }
}
