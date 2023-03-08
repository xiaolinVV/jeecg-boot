package org.jeecg.modules.good.api;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.good.vo.GoodStoreListVo;
import org.jeecg.modules.good.vo.SearchTermsVO;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;
import org.jeecg.modules.marketing.service.IMarketingDistributionSettingService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodSpecificationService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.member.entity.MemberGoodsCollection;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.service.IMemberBrowsingHistoryService;
import org.jeecg.modules.member.service.IMemberGoodsCollectionService;
import org.jeecg.modules.member.service.IMemberShoppingCartService;
import org.jeecg.modules.order.entity.OrderEvaluate;
import org.jeecg.modules.order.entity.OrderEvaluateStore;
import org.jeecg.modules.order.service.IOrderEvaluateService;
import org.jeecg.modules.order.service.IOrderEvaluateStoreService;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品列表控制器
 */
@Controller
@RequestMapping("back/goodList")
public class BackGoodListController {
    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IMemberGoodsCollectionService iMemberGoodsCollectionService;

    @Autowired
    private IOrderEvaluateService iOrderEvaluateService;

    @Autowired
    private IOrderEvaluateStoreService iOrderEvaluateStoreService;

    @Autowired
    private IMemberShoppingCartService iMemberShoppingCartService;


    @Autowired
    private IMemberBrowsingHistoryService iMemberBrowsingHistoryService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;
    @Autowired
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;
    @Autowired
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;
    @Autowired
    private IOrderListService iOrderListService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;
    @Autowired
    private SignUtil signUtil;


    /**
     *  通过类型id查询商品列表
     * @param searchTermsVO pattern   0:综合；1：销量；2：最新;3:价格降序；4：价格升序
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("searchGoodList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> searchGoodList(SearchTermsVO searchTermsVO,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest  request){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //参数判断
        if(searchTermsVO.getIsPlatform() == null ){
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return  result;
        }

        /*if(StringUtils.isBlank(searchTermsVO.getSearch())){
            result.error500("goodTypeId  商品类型id不能为空！！！   ");

            return  result;
        }*/
        String sysUserId=request.getAttribute("sysUserId").toString();
        searchTermsVO.setSysUserId(sysUserId);
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        if(searchTermsVO.getPattern() ==null){
            searchTermsVO.setPattern(0);
        }
        //查询店铺店铺商品列表
        if(searchTermsVO.getIsPlatform().intValue()==0){
            result.setResult(iGoodStoreListService.searchGoodListStore(page,searchTermsVO));
        }else
            //查询平台商品列表
            if(searchTermsVO.getIsPlatform().intValue()==1){
                result.setResult(iGoodListService.searchGoodListStore(page,searchTermsVO));
            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }
        result.success("查询商品列表成功");
        return result;
    }

    /**
     * 根据商品id获取商品详情需要的信息
     *
     * @param goodId
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("findGoodListByGoodId")
    @ResponseBody
    public Result<Map<String,Object>> findGoodListByGoodId(String goodId, Integer isPlatform,@RequestParam(value = "marketingPrefectureId",defaultValue = "",required = false) String marketingPrefectureId, HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> resultMap=Maps.newHashMap();
        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return  result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId  商品id不能为空！！！   ");
            return  result;
        }

        //组织查询参数
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("goodId",goodId);
        paramObjectMap.put("isPlatform",isPlatform);

        Object sysUserId= request.getAttribute("sysUserId").toString();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUserId);
        storeManageQueryWrapper.eq("status", "1");
        if (iStoreManageService.count(storeManageQueryWrapper) <= 0) {
            result.error500("查询不到店铺用户信息！！！");
            return result;
        }
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);


        //添加浏览记录
        if(StringUtils.isNotBlank(storeManage.getMemberListId())){
            iMemberBrowsingHistoryService.addGoodToBrowsingHistory(isPlatform,goodId,storeManage.getMemberListId(),marketingPrefectureId,"");
        }

        //查询店铺商品详情
        if(isPlatform.intValue()==0){
            //判断商品是否被收藏
            if(StringUtils.isNotBlank(storeManage.getMemberListId())){
                QueryWrapper<MemberGoodsCollection> queryWrapper= new QueryWrapper<>();
                queryWrapper.eq("good_store_list_id",goodId);
                queryWrapper.eq("member_list_id",storeManage.getMemberListId());
                long count= iMemberGoodsCollectionService.count(queryWrapper);
                if(count>0){
                    resultMap.put("isCollect",1);
                }else {
                    resultMap.put("isCollect",0);
                }
            }else {
                resultMap.put("isCollect",0);
            }
            //获取商品信息
            Map<String,Object> goodStoreListMap= iGoodStoreListService.findGoodListByGoodId(goodId);
            //获取最便宜的规格组合信息
            QueryWrapper<GoodStoreSpecification> goodStoreSpecificationQueryWrapper=new QueryWrapper<>();
            goodStoreSpecificationQueryWrapper.eq("good_store_list_id",goodId);
            goodStoreSpecificationQueryWrapper.orderByAsc("price");
            GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.list(goodStoreSpecificationQueryWrapper).get(0);;
            goodStoreListMap.put("smallSpecification",goodStoreSpecification.getSpecification());
            resultMap.put("goodinfo",goodStoreListMap);

            //获取商品最新的一条评论信息
            Page<Map<String,Object>> evaluatepage = new Page<Map<String,Object>>(1, 1);
            Map<String,Object> paramevaluateMap= Maps.newHashMap();
            paramevaluateMap.put("goodId",goodId);
            paramevaluateMap.put("pattern",0);

            resultMap.put("evaluateInfo",iOrderEvaluateStoreService.findOrderEvaluateByGoodId(evaluatepage,paramevaluateMap).getRecords());
            //评价总数
            resultMap.put("evaluateCount",iOrderEvaluateStoreService.count(new LambdaQueryWrapper<OrderEvaluateStore>().eq(OrderEvaluateStore::getGoodStoreListId,goodId).eq(OrderEvaluateStore::getStatus, "1")));
            //获取同类型9条推荐商品列表信息
            Page<Map<String,Object>> page = new Page<Map<String,Object>>(1, 9);
            Map<String,Object> paramGoodsMap= Maps.newHashMap();
            paramObjectMap.put("goodTypeId",goodStoreListMap.get("goodTypeId"));
            paramObjectMap.put("pattern",3);
            resultMap.put("recommendGoods",iGoodStoreListService.findGoodListByGoodType(page,paramGoodsMap).getRecords());
        }else
            //查询平台商品详情
            if(isPlatform.intValue()==1){
                //判断商品是否被收藏
                if(StringUtils.isNotBlank(storeManage.getMemberListId())){
                    QueryWrapper<MemberGoodsCollection> queryWrapper= new QueryWrapper<>();
                    queryWrapper.eq("good_list_id",goodId);
                    queryWrapper.eq("member_list_id",storeManage.getMemberListId());
                    long count= iMemberGoodsCollectionService.count(queryWrapper);
                    if(count>0){
                        resultMap.put("isCollect",1);
                    }else {
                        resultMap.put("isCollect",0);
                    }
                }else {
                    resultMap.put("isCollect",0);
                }
                //获取商品信息
                Map<String,Object> goodlistMap=iGoodListService.findGoodListByGoodId(goodId);

                //专区商品信息
                if(StringUtils.isNotBlank(marketingPrefectureId)){
                    //不显示vip价格
                    goodlistMap.put("isViewVipPrice","0");
                    goodlistMap.put("isPrefectureGood","1");
                    MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(marketingPrefectureId);
                    if(marketingPrefecture==null){
                        result.error500("专区id找不到相关的专区信息");
                        return  result;
                    }
                    goodlistMap.put("prefectureLabel",marketingPrefecture.getPrefectureLabel());
                    //获取专区商品
                    QueryWrapper<MarketingPrefectureGood> marketingPrefectureGoodQueryWrapper=new QueryWrapper<>();
                    marketingPrefectureGoodQueryWrapper.eq("marketing_prefecture_id",marketingPrefecture.getId());
                    marketingPrefectureGoodQueryWrapper.eq("good_list_id",goodId);
                    MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(marketingPrefectureGoodQueryWrapper);
                    if(marketingPrefectureGood==null){
                        result.error500("此专区商品不存在！！！");
                        return  result;
                    }
                    goodlistMap.put("smallPrefecturePrice",marketingPrefectureGood.getSmallPrefecturePrice());
                    //是否支持福利金抵扣
                    goodlistMap.put("isWelfare",marketingPrefectureGood.getIsWelfare());
                    if(marketingPrefectureGood.getIsWelfare().equals("1")) {
                        goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice());
                    }else if(marketingPrefectureGood.getIsWelfare().equals("2")){
                        if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1){
                            goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))));

                        }else{
                            goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))));

                        }
                    }else if(marketingPrefectureGood.getIsWelfare().equals("3")){
                        if(StringUtils.indexOf(goodlistMap.get("supplyPrice").toString(),"-")>-1){
                            if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(goodlistMap.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }else{
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(goodlistMap.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }
                        }else{

                            if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(goodlistMap.get("supplyPrice").toString())).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                            }else{
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(goodlistMap.get("supplyPrice").toString())).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                            }



                        }
                    }else{
                        goodlistMap.put("welfareProportionPrice", 0);
                    }

                    //是否会员免福利金
                    goodlistMap.put("isVipLower",marketingPrefecture.getIsVipLower());

                    //是否显示市场价格
                    goodlistMap.put("isViewMarketPrice",marketingPrefecture.getIsViewMarketPrice());

                    //赠送福利金
                    goodlistMap.put("isGiveWelfare",marketingPrefectureGood.getIsGiveWelfare());
                    if(marketingPrefectureGood.getIsGiveWelfare().equals("1")){
                        if(StringUtils.indexOf(marketingPrefectureGood.getGiveWelfareProportion(),"-")>-1){
                            goodlistMap.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.substringAfterLast(marketingPrefectureGood.getGiveWelfareProportion(), "-")).divide(new BigDecimal(100))));
                        }else{
                            goodlistMap.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getGiveWelfareProportion()).divide(new BigDecimal(100))));
                        }
                    }else{
                        goodlistMap.put("giveWelfareProportion", 0);
                    }

                    goodlistMap.put("marketingPrefectureId",marketingPrefectureId);

                    goodlistMap.put("isDiscount",marketingPrefecture.getIsDiscount());

                }else{
                    //显示vip价格
                    goodlistMap.put("isViewVipPrice","1");
                    goodlistMap.put("isPrefectureGood","0");
                }






                //获取最便宜的规格组合信息

                QueryWrapper<GoodSpecification> goodSpecificationQueryWrapper=new QueryWrapper<>();
                goodSpecificationQueryWrapper.eq("good_list_id",goodId);
                goodSpecificationQueryWrapper.orderByAsc("price");
                GoodSpecification goodSpecification=iGoodSpecificationService.list(goodSpecificationQueryWrapper).get(0);
                goodlistMap.put("smallSpecification",goodSpecification.getSpecification());
                resultMap.put("goodinfo",goodlistMap);
                //获取商品最新的一条评论信息

                Page<Map<String,Object>> evaluatepage = new Page<Map<String,Object>>(1, 1);
                Map<String,Object> paramevaluateMap= Maps.newHashMap();
                paramevaluateMap.put("goodId",goodId);
                paramevaluateMap.put("pattern",0);

                resultMap.put("evaluateInfo",iOrderEvaluateService.findOrderEvaluateByGoodId(evaluatepage,paramevaluateMap).getRecords());
                //评价总数
                resultMap.put("evaluateCount",iOrderEvaluateService.count(new  LambdaQueryWrapper<OrderEvaluate>().eq(OrderEvaluate::getGoodListId,goodId).eq(OrderEvaluate::getStatus, "1")));

                //获取同类型9条推荐商品列表信息
                Page<Map<String,Object>> page = new Page<Map<String,Object>>(1, 9);
                Map<String,Object> paramGoodsMap= Maps.newHashMap();
                paramObjectMap.put("goodTypeId",goodlistMap.get("goodTypeId"));
                paramObjectMap.put("pattern",3);
                resultMap.put("recommendGoods",iGoodListService.findGoodListByGoodType(page,paramGoodsMap).getRecords());
            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }

        if(StringUtils.isNotBlank(storeManage.getMemberListId())){
            QueryWrapper<MemberShoppingCart> memberShoppingCartQueryWrapper=new QueryWrapper<>();
            memberShoppingCartQueryWrapper.eq("member_list_id",StringUtils.isNotBlank(storeManage.getMemberListId()));
            resultMap.put("carGoods",iMemberShoppingCartService.count(memberShoppingCartQueryWrapper));
        }else{
            resultMap.put("carGoods","0");
        }

        result.setResult(resultMap);
        result.success("查询商品详情成功");
        return result;
    }

    /**
     * 获取规格商品的价格
     *
     * @param goodId
     * @param isPlatform
     * @param specification
     * @param request
     * @return
     */
    @RequestMapping("getGoodBySpecification")
    @ResponseBody
    public Result<Map<String,Object>> getGoodBySpecification(String goodId, Integer isPlatform,String specification,@RequestParam(value = "marketingPrefectureId",defaultValue = "",required = false) String marketingPrefectureId, HttpServletRequest request){
        Result<Map<String,Object>>result=new Result<>();
        Map<String,Object>paramMap=Maps.newHashMap();
        Object sysUserId=request.getAttribute("sysUserId");

        //参数校验
        //判断数据不为空
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId商品id不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(specification)){
            result.error500("规格组合数据不能为空！！！");
            return result;
        }

        //查询商品价格
        //查询店铺商品
        if(isPlatform.intValue()==0){
            //获取规格商品数据信息进行判断
            //获取规格商品数据信息进行判断
            QueryWrapper<GoodStoreSpecification> goodStoreSpecificationQueryWrapper=new QueryWrapper<>();
            goodStoreSpecificationQueryWrapper.eq("good_store_list_id",goodId);
            goodStoreSpecificationQueryWrapper.eq("specification",specification);
            GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getOne(goodStoreSpecificationQueryWrapper);
            if(goodStoreSpecification==null){
                result.error500("找不到此规格的商品！！！");
                return result;
            }
            if(goodStoreSpecification.getSpecificationPicture()==null){
                GoodStoreList goodStoreList=iGoodStoreListService.getById(goodId);
                paramMap.put("specificationPicture", JSON.parseObject(goodStoreList.getMainPicture()).get("0"));
            }else{
                paramMap.put("specificationPicture",goodStoreSpecification.getSpecificationPicture());
            }
            paramMap.put("repertory",goodStoreSpecification.getRepertory());
            paramMap.put("vipPrice",goodStoreSpecification.getVipPrice());
            paramMap.put("price",goodStoreSpecification.getPrice());


        }else
            //查询平台商品
            if(isPlatform.intValue()==1){
                QueryWrapper<GoodSpecification> goodSpecificationQueryWrapper=new QueryWrapper<>();
                goodSpecificationQueryWrapper.eq("good_list_id",goodId);
                goodSpecificationQueryWrapper.eq("specification",specification);
                GoodSpecification goodSpecification=iGoodSpecificationService.getOne(goodSpecificationQueryWrapper);
                if(goodSpecification==null){
                    result.error500("找不到此规格的商品！！！");
                    return result;
                }
                GoodList goodList=iGoodListService.getById(goodId);
                if(goodSpecification.getSpecificationPicture()==null){
                    if(goodList.getMainPicture()!=null) {
                        paramMap.put("specificationPicture", JSON.parseObject(goodList.getMainPicture()).get(0));
                    }else{
                        paramMap.put("specificationPicture", "");
                    }
                }else{
                    paramMap.put("specificationPicture",goodSpecification.getSpecificationPicture());
                }
                paramMap.put("repertory",goodSpecification.getRepertory());

                if(StringUtils.isBlank(marketingPrefectureId)) {
                            paramMap.put("vipPrice", goodSpecification.getVipPrice());
                            paramMap.put("price", goodSpecification.getPrice());


                }else{
                    //获取专区商品
                    QueryWrapper<MarketingPrefectureGood> marketingPrefectureGoodQueryWrapper=new QueryWrapper<>();
                    marketingPrefectureGoodQueryWrapper.eq("marketing_prefecture_id",marketingPrefectureId);
                    marketingPrefectureGoodQueryWrapper.eq("good_list_id",goodId);
                    MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(marketingPrefectureGoodQueryWrapper);
                    if(marketingPrefectureGood==null){
                        result.error500("此专区商品不存在！！！");
                        return  result;
                    }
                    //获取专区规格

                    QueryWrapper<MarketingPrefectureGoodSpecification> marketingPrefectureGoodSpecificationQueryWrapper=new QueryWrapper<>();
                    marketingPrefectureGoodSpecificationQueryWrapper.eq("marketing_prefecture_good_id",marketingPrefectureGood.getId());
                    marketingPrefectureGoodSpecificationQueryWrapper.eq("good_specification_id",goodSpecification.getId());
                    MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification=iMarketingPrefectureGoodSpecificationService.getOne(marketingPrefectureGoodSpecificationQueryWrapper);
                    if(marketingPrefectureGoodSpecification==null){
                        result.error500("此专区商品规格不存在！！！");
                        return  result;
                    }
                    paramMap.put("price", marketingPrefectureGoodSpecification.getPrefecturePrice());
                }
            }else{
                result.error500("isPlatform参数不正确请联系平台管理员！！！");
                return result;
            }

        result.setResult(paramMap);
        result.success("查询规格对应价格成功");
        return result;
    }

    /**
     * 显示 已购买商品 轮播购买人
     * @return
     */
    @RequestMapping("getOrderMemberHubble")
    @ResponseBody
    public Result<List<Map<String,Object>>>  getOrderMemberHubble(){
        Result<List<Map<String,Object>>> result = new Result<>();
        List<Map<String,Object>> mapList= iOrderListService.getOrderMemberHubble();
        result.setResult(mapList);
        result.success("查询成功");
        return  result;

    }

    /**
     * 归属奖励金额数据
     * @param goodId
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("getAmountAttributionBonus")
    @ResponseBody
    public Result<Map<String,Object>> getAmountAttributionBonus(String goodId, Integer isPlatform, HttpServletRequest request){
        Result<Map<String,Object>>result=new Result<>();
        Map<String,Object>paramMap=Maps.newHashMap();
        Object sysUserId=request.getAttribute("sysUserId");

        //参数校验
        //判断数据不为空
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId商品id不能为空！！！");
            return result;
        }

        //平台预留利润比例
        String profitProportion  = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item",
                "item_value", "item_text", "commission_rate"), "%");

        QueryWrapper<MarketingDistributionSetting> marketingDistributionSettingQueryWrapper = new QueryWrapper<>();
        marketingDistributionSettingQueryWrapper.eq("status", "1");
        marketingDistributionSettingQueryWrapper.orderByDesc("create_time");

        if (iMarketingDistributionSettingService.count(marketingDistributionSettingQueryWrapper) == 0) {
            result.error500("未找到奖励比例!");
            return result;
        }
        MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.list(marketingDistributionSettingQueryWrapper).get(0);

        //查询店铺商品详情
        if(isPlatform.intValue()==0){
            return  result.success("请求成功!");
        }else
            //查询平台商品详情
            if(isPlatform.intValue()==1){
                GoodList  goodList=   iGoodListService.getById(goodId);
                if(goodList == null){
                    result.error500("商品不存在！");
                    return result;
                }

                GoodSpecification goodSpecification=iGoodSpecificationService.getSmallGoodSpecification(goodId);

                //总利润(普通会员)
                BigDecimal sumProfit =goodSpecification.getPrice().subtract(goodSpecification.getCostPrice());
                //平台净利润(普通会员)
                BigDecimal retainedProfits = goodSpecification.getSupplyPrice().subtract(goodSpecification.getCostPrice()).add(new BigDecimal(profitProportion).multiply(sumProfit).divide(new BigDecimal(100)));

                //归属店铺奖励(普通会员)
                BigDecimal  storeAward = retainedProfits.multiply(marketingDistributionSetting.getAffiliationStoreAward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                //销售渠道奖励(普通会员)
                BigDecimal  channelAward  = retainedProfits.multiply(marketingDistributionSetting.getDistributionChannelAward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                paramMap.put("storeAward",storeAward);
                paramMap.put("channelAward",channelAward);

                //总利润(VIP会员)
                BigDecimal sumVIPProfit = goodSpecification.getVipPrice().subtract(goodSpecification.getCostPrice());
                //平台净利润(VIP会员)
                BigDecimal retainedVIPProfits = goodSpecification.getSupplyPrice().subtract(goodSpecification.getCostPrice()).add(new BigDecimal(profitProportion).multiply(sumVIPProfit).divide(new BigDecimal(100)));

                //归属店铺奖励(VIP会员)
                BigDecimal  storeVIPAward = retainedVIPProfits.multiply(marketingDistributionSetting.getAffiliationStoreAward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                //销售渠道奖励(VIP会员)
                BigDecimal  channelVIPAward  = retainedVIPProfits.multiply(marketingDistributionSetting.getDistributionChannelAward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                paramMap.put("storeVIPAward",storeVIPAward);
                paramMap.put("channelVIPAward",channelVIPAward);
                //数值排序
                BigDecimal[] awardPaixu = new BigDecimal[4];
                awardPaixu[0] = storeAward;
                awardPaixu[1] = channelAward;
                awardPaixu[2] = storeVIPAward;
                awardPaixu[3] = channelVIPAward;
                //数值排序(冒泡排序)
                awardPaixu = signUtil.maopaoSort(awardPaixu);
                //奖励最小值
                paramMap.put("minAward",awardPaixu[0]);
                //奖励最大值
                if(storeAward.add(channelAward).compareTo(storeVIPAward.add(channelVIPAward)) == 1){
                    //奖励最大值
                    paramMap.put("maxAward",storeAward.add(channelAward));
                }else{
                    //奖励最大值
                    paramMap.put("maxAward",storeVIPAward.add(channelVIPAward));
                }
                  result.setResult(paramMap);
                  result.success("请求成功!");
            }

        return result;


    }


    /**
     * 店铺商品管理
     * @param request
     * @return
     */
    @RequestMapping("getGoodStoreCount")
    @ResponseBody
    public Result<Map<String,Object>> getGoodStoreCount(HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> paramMap=Maps.newHashMap();
        Object sysUserId=request.getAttribute("sysUserId");
        //店铺商品总数
        long goodSumCount =  iGoodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().ne(GoodStoreList::getAuditStatus,"0")
                .eq(GoodStoreList::getSysUserId, sysUserId));
        //店铺 在售 商品数
        long goodSumSaleCount = iGoodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().eq(GoodStoreList::getAuditStatus,"2")
                .eq(GoodStoreList::getSysUserId, sysUserId)
                .eq(GoodStoreList::getFrameStatus,"1")
                .eq(GoodStoreList::getStatus,"1"));
        //店铺 下架 商品数
        long goodSoldOutCount = iGoodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().ne(GoodStoreList::getAuditStatus,"0")
                .eq(GoodStoreList::getSysUserId, sysUserId)
                .eq(GoodStoreList::getAuditStatus,"2")
                .eq(GoodStoreList::getFrameStatus,"0")
                .eq(GoodStoreList::getStatus,"1"));
        //店铺 在审核 商品数
        long goodAuditCount = iGoodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().ne(GoodStoreList::getAuditStatus,"0")
                .eq(GoodStoreList::getSysUserId, sysUserId)
                .eq(GoodStoreList::getFrameStatus,"1")
                .eq(GoodStoreList::getStatus,"1")
                .and(wrapper -> wrapper.eq(GoodStoreList::getAuditStatus,"1").or().eq(GoodStoreList::getAuditStatus,"3")));
        //回收站商品
        Integer goodRecycleBinCount = iGoodStoreListService.getGoodStoreListdelFlag(sysUserId.toString());
        //在草稿箱商品
        long goodDraftsCount = iGoodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().eq(GoodStoreList::getAuditStatus,"0")
                .eq(GoodStoreList::getSysUserId, sysUserId));
        paramMap.put("goodSumCount",goodSumCount);//店铺商品总数
        paramMap.put("goodSumSaleCount",goodSumSaleCount); //店铺 在售 商品数
        paramMap.put("goodSoldOutCount",goodSoldOutCount); //店铺 下架 商品数
        paramMap.put("goodAuditCount",goodAuditCount); //店铺 在审核 商品数
        paramMap.put("goodRecycleBinCount",goodRecycleBinCount);  //回收站商品
        paramMap.put("goodDraftsCount",goodDraftsCount); //在草稿箱商品

        result.setResult(paramMap);
        result.success("请求成功!");
        return result;
    }


    /**
     * 商品端店铺商品列表查询
     * @param goodStatus 0.所有店铺商品 1.店铺 在售 商品  2.店铺 下架 商品数 3.店铺 在审核 商品数 4.回收站商品 5.草稿箱商品
     * @param search 商品名称查询
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("getGoodStoreListMapList")
    @ResponseBody
    public Result<Map<String,Object>> getGoodStoreListMapList(@RequestParam(name="goodStatus", defaultValue="0")String goodStatus,
                                                         String search,
                                                         String goodStoreTypeId,
                                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                         HttpServletRequest request) {
        Result<Map<String,Object>> result=new Result<>();
        Page<GoodStoreList> page = new Page<GoodStoreList>(pageNo, pageSize);
        Map<String,Object> paramMap=Maps.newHashMap();
        String sysUserId=request.getAttribute("sysUserId").toString();
        paramMap.put("sysUserId",sysUserId);
        //查询商品状态区分
        paramMap.put("goodStatus",goodStatus);
        //搜索框条件
        if(StringUtils.isNotBlank(search)){
            paramMap.put("search",search);
        }
        //分类查询
        if(StringUtils.isNotBlank(goodStoreTypeId)){
            paramMap.put("goodStoreTypeId",goodStoreTypeId);
        }

        IPage<Map<String,Object>> mapIPage =  iGoodStoreListService.getGoodStoreListMaps(page,paramMap);
        Map<String,Object> objectMap=Maps.newHashMap();
        objectMap.put("goodMaps",mapIPage);
        result.setResult(objectMap);
        result.success("查询商品成功!");
        return result;
    }

    /**
     * 查询修改价格商品信息
     * @param goodId
     * @param request
     * @return
     */
    @RequestMapping("getGoodPriceAndRepertory")
    @ResponseBody
   public Result<Map<String,Object>> getGoodPriceAndRepertory(@RequestParam(name="goodId")String goodId,HttpServletRequest request){
       Result<Map<String,Object>> result=new Result<>();
        QueryWrapper queryWrapperGoodStoreList =   new QueryWrapper();
       Map<String,Object> paramMap=Maps.newHashMap();
       queryWrapperGoodStoreList.select("id,main_picture as mainPicture,good_name AS goodName,repertory,market_price AS marketPrice,price,vip_price AS vipPrice,cost_price AS costPrice,small_price as smallPrice,small_vip_price AS smallVipPrice,small_cost_price AS smallCostPrice");
       String sysUserId=request.getAttribute("sysUserId").toString();
       queryWrapperGoodStoreList.eq("sys_user_id",sysUserId);
       queryWrapperGoodStoreList.eq("id",goodId);
       Map<String,Object> goodPrice = iGoodStoreListService.getMap(queryWrapperGoodStoreList);
       if(goodPrice.size()>0){
           QueryWrapper<GoodStoreSpecification> goodStoreSpecificationQueryWrapper = new QueryWrapper<>();
           goodStoreSpecificationQueryWrapper.select("id,price,vip_price as vipPrice,cost_price as costPrice,repertory,specification");
           goodStoreSpecificationQueryWrapper.eq("good_store_list_id",goodPrice.get("id"));
           //添加规格数据
          List<Map<String,Object>> goodStoreSpecificationList =  iGoodStoreSpecificationService.listMaps(goodStoreSpecificationQueryWrapper);

           goodPrice.put("goodStoreSpecificationList",goodStoreSpecificationList);
       }

       paramMap.put("goods",goodPrice);
       result.setResult(paramMap);
       result.success("查询商品价格库存信息成功!");
   return result;
   }

    /**
     * 修改商家端店铺商品价格跟库存
     * @param goodStoreListVo
     * @param request
     * @return
     */
    @RequestMapping("updateGoodPriceAndRepertory")
    @ResponseBody
     public Result<Map<String,Object>> updateGoodPriceAndRepertory (GoodStoreListVo goodStoreListVo, HttpServletRequest request){
         Result<Map<String,Object>> result=new Result<>();
         if( goodStoreListVo ==null ){
             return result.error500("修改商品不能为空!");
         }
         GoodStoreList goodStoreList =   iGoodStoreListService.getById(goodStoreListVo.getId()) ;
         if(goodStoreList == null){
             return result.error500("未查到修改商品!");
         }
         //修改店铺商品数据
         goodStoreList.setMarketPrice(goodStoreListVo.getMarketPrice()); //市场价
         iGoodStoreListService.updateById(goodStoreList);
         goodStoreListVo.getGoodStoreSpecificationList().forEach(gss->{
             GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(gss.getId());
             if(goodStoreSpecification != null){
                 //修改商品规格数据
                 goodStoreSpecification.setPrice(gss.getPrice());
                 goodStoreSpecification.setVipPrice(gss.getVipPrice());
                 goodStoreSpecification.setCostPrice(gss.getCostPrice());
                 goodStoreSpecification.setRepertory(gss.getRepertory());
                 iGoodStoreSpecificationService.updateById(goodStoreSpecification);
             }

         });
         result.success("修改成功!");
      return result;
     }
    /**
     *   通过id删除
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public Result<?> delete(@RequestParam(name="id",required=true) String id,HttpServletRequest request) {
        try {
            String sysUserId=request.getAttribute("sysUserId").toString();
          if(iGoodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().eq(GoodStoreList::getId,id)
                  .eq(GoodStoreList::getSysUserId,sysUserId))>0){
              iGoodStoreListService.removeById(id)  ;
          }else{
              return Result.error("商品不存在!");
          }


        } catch (Exception e) {

            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     *  批量修改上下架
     * @param ids
     * @return
     */
    @RequestMapping("updateFrameStatus")
    @ResponseBody
    public  Result<GoodStoreList> updateFrameStatus(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="frameStatus",required=true)String frameStatus,String frameExplain,HttpServletRequest request){
        Result<GoodStoreList> result = new Result<GoodStoreList>();
        if(ids==null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        }else {
            GoodStoreList goodStoreList;
            try {
                List<String> listid= Arrays.asList(ids.split(","));
                for (String id: listid){
                    goodStoreList = iGoodStoreListService.getGoodStoreListById(id);
                    if(goodStoreList==null){
                        result.error500("未找到对应实体");
                    }else{
                        String sysUserId=request.getAttribute("sysUserId").toString();
                        if( sysUserId.equals(goodStoreList.getSysUserId())){
                            goodStoreList.setFrameExplain(frameExplain);
                            goodStoreList.setFrameStatus(frameStatus);
                            iGoodStoreListService.updateById(goodStoreList);
                        }else{
                            return  result.error500("没有权限修改商品!");
                        }

                    }
                }
                result.success("修改成功!");
            }catch (Exception e){
                result.error500("修改失败！");
            }
        }
        return result;
    }


}
