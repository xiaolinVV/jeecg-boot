package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 平台专区商品
 */
@Controller
@RequestMapping("front/marketingPrefectureGood")
public class FrontMarketingPrefectureGoodController {

    @Autowired
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IMarketingPrefectureTypeService iMarketingPrefectureTypeService;

    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 根据专区类型查询专区商品
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findByMarketingPrefectureType")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findByMarketingPrefectureType(@RequestParam(name="id", defaultValue="") String id,
                                                                           @RequestParam(required = false,defaultValue = "0") Integer pattern,
                                                                           @RequestParam(name="marketingPrefectureId", defaultValue="") String marketingPrefectureId,
                                                                           @RequestParam(name="marketingPrefectureRecommendId", defaultValue="") String marketingPrefectureRecommendId,
                                                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        IPage<Map<String,Object>> marketingPrefectureGoodPage=null;
        if(StringUtils.isNotBlank (id)) {
            //组织查询参数
            Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
            Map<String, Object> paramObjectMap = Maps.newHashMap();

            paramObjectMap.put("pattern", pattern);
            //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
            String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
            if(goodsSortType.equals("1")&&pattern==0){
                paramObjectMap.put("pattern",5);
            }
            paramObjectMap.put("id",id);
            MarketingPrefectureType marketingPrefectureType = iMarketingPrefectureTypeService.getById(id);
            if (marketingPrefectureType.getLevel().intValue()==1){
                marketingPrefectureGoodPage = iMarketingPrefectureGoodService.findByMarketingPrefectureTypeOne(page,paramObjectMap);
            }else {
                marketingPrefectureGoodPage = iMarketingPrefectureGoodService.findByMarketingPrefectureType(page, paramObjectMap);
            }

        }else{
            //参数验证
            if(StringUtils.isBlank(marketingPrefectureId)){
                result.error500("平台专区id为空！！！");
                return  result;
            }
            //无非类的专区商品数据查询
            //组织查询参数
            Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
            Map<String,Object> paramObjectMap= Maps.newHashMap();
            paramObjectMap.put("id",marketingPrefectureId);
            paramObjectMap.put("search",null);
            paramObjectMap.put("pattern",pattern);
            //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
            String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
            if(goodsSortType.equals("1")&&pattern==0){
                paramObjectMap.put("pattern",5);
            }
           marketingPrefectureGoodPage=iMarketingPrefectureGoodService.findByMarketingPrefectureIdAndSearch(page,paramObjectMap);
        }
            for (Map<String,Object> mp:marketingPrefectureGoodPage.getRecords()) {

                if(mp.get("marketingPrefectureId")!=null){
                    //不显示vip价格
                    mp.put("isViewVipPrice","0");
                    mp.put("isPrefectureGood","1");
                    MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(mp.get("marketingPrefectureId").toString());
                    if(marketingPrefecture==null){
                       continue;
                    }
                    mp.put("prefectureLabel",marketingPrefecture.getPrefectureLabel());
                    //获取专区商品
                    MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(new LambdaQueryWrapper<MarketingPrefectureGood>()
                            .eq(MarketingPrefectureGood::getMarketingPrefectureId,marketingPrefecture.getId())
                            .eq(MarketingPrefectureGood::getGoodListId,mp.get("id").toString())
                            .orderByDesc(MarketingPrefectureGood::getCreateTime)
                            .last("limit 1"));
                    if(marketingPrefectureGood==null){
                       continue;
                    }
                    mp.put("smallPrice",marketingPrefectureGood.getSmallPrefecturePrice());
                    //是否支持福利金抵扣
                    mp.put("isWelfare",marketingPrefectureGood.getIsWelfare());

                    if(marketingPrefectureGood.getIsWelfare().equals("1")) {
                        mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice());
                    }else if(marketingPrefectureGood.getIsWelfare().equals("2")){
                        if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1){
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))));

                        }else{
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))));

                        }
                    }else if(marketingPrefectureGood.getIsWelfare().equals("3")){
                        if(StringUtils.indexOf(mp.get("supplyPrice").toString(),"-")>-1){
                            if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                                mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(mp.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }else{
                                mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(mp.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }
                        }else{
                            if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                                mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(mp.get("supplyPrice").toString())).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }else{
                                mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(mp.get("supplyPrice").toString())).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                            }
                        }
                    }else{
                        mp.put("welfareProportionPrice", 0);
                    }

                    //是否会员免福利金
                    mp.put("isVipLower",marketingPrefecture.getIsVipLower());

                    //是否显示市场价格
                    mp.put("isViewMarketPrice",marketingPrefecture.getIsViewMarketPrice());

                    //赠送福利金
                    mp.put("isGiveWelfare",marketingPrefectureGood.getIsGiveWelfare());
                    if(marketingPrefectureGood.getIsGiveWelfare().equals("1")){
                        if(StringUtils.indexOf(marketingPrefectureGood.getGiveWelfareProportion(),"-")>-1){
                            mp.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getGiveWelfareProportion(), "-"))).divide(new BigDecimal(100))));
                        }else{
                            mp.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getGiveWelfareProportion()).divide(new BigDecimal(100))));
                        }
                    }else{
                        mp.put("giveWelfareProportion", 0);
                    }
                    mp.put("isDiscount",marketingPrefecture.getIsDiscount());

                }else{
                    //显示vip价格
                    mp.put("isViewVipPrice","1");
                    mp.put("isPrefectureGood","0");
                }
            }
            result.setResult(marketingPrefectureGoodPage);
        result.success("查询专区类型下面的商品！！！");
        return result;
    }


    /**
     * 查询专商品列表，带模糊查询
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findByMarketingPrefectureIdAndSearch")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findByMarketingPrefectureIdAndSearch(String id,String search,
                                                                                  @RequestParam(required = false,defaultValue = "0") Integer pattern,
                                                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //参数验证
//        if(StringUtils.isBlank(id)){
//            result.error500("平台专区商品类型id为空！！！");
//            return  result;
//        }

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("id",id);
        paramObjectMap.put("search",search);
        paramObjectMap.put("pattern",pattern);
        //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
        String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
        if(goodsSortType.equals("1")&&pattern==0){
            paramObjectMap.put("pattern",5);
        }
        IPage<Map<String,Object>> marketingPrefectureGoodPage=iMarketingPrefectureGoodService.findByMarketingPrefectureIdAndSearch(page,paramObjectMap);

        for (Map<String,Object> mp:marketingPrefectureGoodPage.getRecords()) {

            if(mp.get("marketingPrefectureId")!=null){
                //不显示vip价格
                mp.put("isViewVipPrice","0");
                mp.put("isPrefectureGood","1");
                MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(mp.get("marketingPrefectureId").toString());
                if(marketingPrefecture==null){
                    continue;
                }
                mp.put("prefectureLabel",marketingPrefecture.getPrefectureLabel());
                //获取专区商品
                QueryWrapper<MarketingPrefectureGood> marketingPrefectureGoodQueryWrapper=new QueryWrapper<>();
                marketingPrefectureGoodQueryWrapper.eq("marketing_prefecture_id",marketingPrefecture.getId());
                marketingPrefectureGoodQueryWrapper.eq("good_list_id",mp.get("id").toString());
                MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(marketingPrefectureGoodQueryWrapper);
                if(marketingPrefectureGood==null){
                    continue;
                }
                mp.put("smallPrefecturePrice",marketingPrefectureGood.getSmallPrefecturePrice());
                //是否支持福利金抵扣
                mp.put("isWelfare",marketingPrefectureGood.getIsWelfare());

                if(marketingPrefectureGood.getIsWelfare().equals("1")) {
                    mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice());
                }else if(marketingPrefectureGood.getIsWelfare().equals("2")){
                    if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1){
                        mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))));

                    }else{
                        mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))));

                    }
                }else if(marketingPrefectureGood.getIsWelfare().equals("3")){
                    if(StringUtils.indexOf(mp.get("supplyPrice").toString(),"-")>-1){
                        if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(mp.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                        }else{
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(mp.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                        }
                    }else{
                        if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(mp.get("supplyPrice").toString())).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                        }else{
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(mp.get("supplyPrice").toString())).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                        }
                    }
                }else{
                    mp.put("welfareProportionPrice", 0);
                }

                //是否会员免福利金
                mp.put("isVipLower",marketingPrefecture.getIsVipLower());

                //是否显示市场价格
                mp.put("isViewMarketPrice",marketingPrefecture.getIsViewMarketPrice());

                //赠送福利金
                mp.put("isGiveWelfare",marketingPrefectureGood.getIsGiveWelfare());
                if(marketingPrefectureGood.getIsGiveWelfare().equals("1")){
                    if(StringUtils.indexOf(marketingPrefectureGood.getGiveWelfareProportion(),"-")>-1){
                        mp.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getGiveWelfareProportion(), "-"))).divide(new BigDecimal(100))));
                    }else{
                        mp.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getGiveWelfareProportion()).divide(new BigDecimal(100))));
                    }
                }else{
                    mp.put("giveWelfareProportion", 0);
                }

                mp.put("isDiscount",marketingPrefecture.getIsDiscount());

            }else{
                //显示vip价格
                mp.put("isViewVipPrice","1");
                mp.put("isPrefectureGood","0");
            }
        }

        result.setResult(marketingPrefectureGoodPage);
        result.success("查询专区类型下面的商品！！！");
        return result;
    }
    /**
     * 查询专商品列表，带模糊查询
     * @param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findByMarketingPrefectureId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findByMarketingPrefectureId(String search,
                                                                         @RequestParam(required = false,defaultValue = "0") Integer pattern,
                                                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();


        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("search",search);
        paramObjectMap.put("pattern",pattern);
        //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
        String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
        if(goodsSortType.equals("1")&&pattern==0){
            paramObjectMap.put("pattern",5);
        }
        IPage<Map<String,Object>> marketingPrefectureGoodPage=iMarketingPrefectureGoodService.findByMarketingPrefectureId(page,paramObjectMap);

        for (Map<String,Object> mp:marketingPrefectureGoodPage.getRecords()) {

            if(mp.get("marketingPrefectureId")!=null){
                //不显示vip价格
                mp.put("isViewVipPrice","0");
                mp.put("isPrefectureGood","1");
                MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(mp.get("marketingPrefectureId").toString());
                if(marketingPrefecture==null){
                    continue;
                }
                mp.put("prefectureLabel",marketingPrefecture.getPrefectureLabel());
                //获取专区商品
                QueryWrapper<MarketingPrefectureGood> marketingPrefectureGoodQueryWrapper=new QueryWrapper<>();
                marketingPrefectureGoodQueryWrapper.eq("marketing_prefecture_id",marketingPrefecture.getId());
                marketingPrefectureGoodQueryWrapper.eq("good_list_id",mp.get("id").toString());
                MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(marketingPrefectureGoodQueryWrapper);
                if(marketingPrefectureGood==null){
                    continue;
                }
                mp.put("smallPrefecturePrice",marketingPrefectureGood.getSmallPrefecturePrice());
                //是否支持福利金抵扣
                mp.put("isWelfare",marketingPrefectureGood.getIsWelfare());

                if(marketingPrefectureGood.getIsWelfare().equals("1")) {
                    mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice());
                }else if(marketingPrefectureGood.getIsWelfare().equals("2")){
                    if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1){
                        mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))));

                    }else{
                        mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))));

                    }
                }else if(marketingPrefectureGood.getIsWelfare().equals("3")){
                    if(StringUtils.indexOf(mp.get("supplyPrice").toString(),"-")>-1){
                        if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(mp.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                        }else{
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(mp.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                        }
                    }else{
                        if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(mp.get("supplyPrice").toString())).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                        }else{
                            mp.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(mp.get("supplyPrice").toString())).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                        }
                    }
                }else{
                    mp.put("welfareProportionPrice", 0);
                }

                //是否会员免福利金
                mp.put("isVipLower",marketingPrefecture.getIsVipLower());

                //是否显示市场价格
                mp.put("isViewMarketPrice",marketingPrefecture.getIsViewMarketPrice());

                //赠送福利金
                mp.put("isGiveWelfare",marketingPrefectureGood.getIsGiveWelfare());
                if(marketingPrefectureGood.getIsGiveWelfare().equals("1")){
                    if(StringUtils.indexOf(marketingPrefectureGood.getGiveWelfareProportion(),"-")>-1){
                        mp.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getGiveWelfareProportion(), "-"))).divide(new BigDecimal(100))));
                    }else{
                        mp.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getGiveWelfareProportion()).divide(new BigDecimal(100))));
                    }
                }else{
                    mp.put("giveWelfareProportion", 0);
                }

                mp.put("isDiscount",marketingPrefecture.getIsDiscount());

            }else{
                //显示vip价格
                mp.put("isViewVipPrice","1");
                mp.put("isPrefectureGood","0");
            }
        }

        result.setResult(marketingPrefectureGoodPage);
        result.success("查询专区类型下面的商品！！！");
        return result;
    }
}
