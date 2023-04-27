package org.jeecg.modules.good.api;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.good.entity.*;
import org.jeecg.modules.good.service.*;
import org.jeecg.modules.good.util.GoodUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGoodService;
import org.jeecg.modules.member.entity.MemberGoodsCollection;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.order.entity.OrderEvaluate;
import org.jeecg.modules.order.entity.OrderEvaluateStore;
import org.jeecg.modules.order.service.IOrderEvaluateService;
import org.jeecg.modules.order.service.IOrderEvaluateStoreService;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.searchHistory.service.SearchHistoryService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 商品列表控制器
 */
@Controller
@RequestMapping("front/goodList")
@Log
public class FrontGoodListController {

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
    private IMemberListService iMemberListService;

    @Autowired
    private IGoodTypeService iGoodTypeService;

    @Autowired
    private IMemberBrowsingHistoryService iMemberBrowsingHistoryService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
     private ISysDictService sysDictService;
    @Autowired
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;

    @Autowired
    private IMarketingFreeGoodListService iMarketingFreeGoodListService;

    @Autowired
    private IMarketingFreeGoodSpecificationService iMarketingFreeGoodSpecificationService;

    @Autowired
    private IOrderListService iOrderListService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberGradeService iMemberGradeService;

    @Autowired
    private IMarketingFreeSessionService iMarketingFreeSessionService;

    @Autowired
    private IMarketingGroupGoodListService iMarketingGroupGoodListService;

    @Autowired
    private IMarketingGroupGoodSpecificationService iMarketingGroupGoodSpecificationService;

    @Autowired
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;

    @Autowired
    private IMarketingGroupManageService iMarketingGroupManageService;
    @Autowired
    private IMarketingGroupRecordService iMarketingGroupRecordService;

    @Autowired
    private IMarketingFreeBaseSettingService iMarketingFreeBaseSettingService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMarketingZoneGroupGoodService iMarketingZoneGroupGoodService;

    @Autowired
    private IMarketingZoneGroupService iMarketingZoneGroupService;

    @Autowired
    private IMarketingZoneGroupBaseSettingService iMarketingZoneGroupBaseSettingService;

    @Autowired
    private IMarketingRushGoodService iMarketingRushGoodService;

    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;

    @Autowired
    private IMarketingIntegralSettingService iMarketingIntegralSettingService;

    @Autowired
    private IMarketingLeagueGoodListService iMarketingLeagueGoodListService;

    @Autowired
    private IMarketingLeagueSettingService iMarketingLeagueSettingService;

    @Autowired
    private IMarketingLeagueGoodSpecificationService iMarketingLeagueGoodSpecificationService;


    @Autowired
    private GoodUtils goodUtils;


    @Autowired
    private IMarketingStorePrefectureGoodService iMarketingStorePrefectureGoodService;

    @Autowired
    SearchHistoryService searchHistoryService;



    /**
     *  通过类型id查询商品列表
     * @param isPlatform
     * @param pattern   0:综合；1：销量；2：最新;3:价格降序；4：价格升序
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("searchGoodList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> searchGoodList(String search, Integer isPlatform,
                                                            @RequestParam(required = false,defaultValue = "0") Integer pattern,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            @RequestAttribute(name = "memberId",required = false) String memberId){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return  result;
        }

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("search",search);
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("memberId",StringUtils.defaultString(memberId,""));

        //查询店铺店铺商品列表
        if(isPlatform ==0){

            result.setResult(iGoodStoreListService.searchGoodList(page,paramObjectMap));

        }else
            //查询平台商品列表
            if(isPlatform ==1){
                // 个人搜索记录、热搜存储
                if (StrUtil.isNotBlank(memberId) && StrUtil.isNotBlank(search)) {
                    searchHistoryService.addSearchHistoryByUserId(memberId, search);
                    searchHistoryService.incrementScore(search);
                }
                //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
                String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
                if(goodsSortType.equals("1")&&pattern==0){
                    paramObjectMap.put("pattern",5);
                }

                //活动商品显示控制，加入活动后仅在活动中显示(多选)；0：专区；1：免单；2：中奖拼团
                String activeGoodsDisplayControl = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "active_goods_display_control");
                paramObjectMap.put("isViewPrefecture","1");
                paramObjectMap.put("isViewfree","1");
                paramObjectMap.put("isViewgroup","1");
                if(StringUtils.contains(activeGoodsDisplayControl,"0")){
                    paramObjectMap.put("isViewPrefecture","0");
                }
                if(StringUtils.contains(activeGoodsDisplayControl,"1")){
                    paramObjectMap.put("isViewfree","0");
                }
                if(StringUtils.contains(activeGoodsDisplayControl,"2")){
                    paramObjectMap.put("isViewgroup","0");
                }
                result.setResult(iGoodListService.searchGoodList(page,paramObjectMap));
            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }
        result.success("查询商品列表成功");
        return result;
    }


    /**
     * 根据任何类型查询商品信息
     * @param goodTypeId
     * @param isPlatform
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findGoodListEveryGoodType")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findGoodListEveryGoodType(String goodTypeId, Integer isPlatform, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                       @RequestAttribute(name = "memberId",required = false) String memberId){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return  result;
        }

        if(StringUtils.isBlank(goodTypeId)){
            result.error500("goodTypeId  商品类型id不能为空！！！   ");

            return  result;
        }

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);

        //查询店铺店铺商品列表
        if(isPlatform.intValue()==0){
            result.setResult(iGoodStoreListService.findGoodListBySysUserId(page,goodTypeId));

        }else
            //查询平台商品列表
            if(isPlatform.intValue()==1||isPlatform.intValue()==2){

                Map<String,Object> paramObjectMap= Maps.newHashMap();


                //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
                String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
                paramObjectMap.put("pattern",goodsSortType);

                paramObjectMap.put("goodTypeId",goodTypeId);
                paramObjectMap.put("isPlatform",isPlatform);
                GoodType goodType=iGoodTypeService.getById(goodTypeId);
                if(goodType==null){
                    result.error500("类型id找不到相应的类型数据！！！");
                    return  result;
                }
                paramObjectMap.put("level",goodType.getLevel());
                //活动商品显示控制，加入活动后仅在活动中显示(多选)；0：专区；1：免单；2：中奖拼团
                String activeGoodsDisplayControl = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "active_goods_display_control");
                paramObjectMap.put("isViewPrefecture","1");
                paramObjectMap.put("isViewfree","1");
                paramObjectMap.put("isViewgroup","1");
                if(StringUtils.contains(activeGoodsDisplayControl,"0")){
                    paramObjectMap.put("isViewPrefecture","0");
                }
                if(StringUtils.contains(activeGoodsDisplayControl,"1")){
                    paramObjectMap.put("isViewfree","0");
                }
                if(StringUtils.contains(activeGoodsDisplayControl,"2")){
                    paramObjectMap.put("isViewgroup","0");
                }
                IPage<Map<String, Object>> iPage=iGoodListService.findGoodListByGoodTypeAndlevel(page,paramObjectMap);
                iPage.getRecords().forEach(g->{
                    iMemberGradeService.settingGoodListInfo(g,g.get("id").toString(),memberId);
                });
                result.setResult(iPage);
            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }


        result.success("查询商品列表成功");
        return result;
    }

    /**
     *  通过类型id查询商品列表（目前新的商品数据都采用这个接口）
     * @param goodTypeId
     * @param isPlatform
     * @param pattern   0:综合；1：销量；2：最新;3:价格降序；4：价格升序
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findGoodListByGoodType")
    @ResponseBody
    public Result<?> findGoodListByGoodType(String goodTypeId,
                                            Integer isPlatform,
                                            String searchInfo,
                                            @RequestParam(required = false,defaultValue = "") String goodBrandId,
                                            @RequestParam(required = false,defaultValue = "") String goodMachineBrandId,
                                            @RequestParam(required = false,defaultValue = "") String goodMachineModeId,
                                            @RequestParam(required = false,defaultValue = "0") Integer pattern,
                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                            @RequestAttribute(name = "memberId",required = false) String memberId){
        //参数判断
        if(isPlatform==null){
            return Result.error("isPlatform  是否平台类型参数不能为空！！！   ");
        }

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("goodTypeId",goodTypeId);
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("goodBrandId",goodBrandId);
        paramObjectMap.put("goodMachineBrandId",goodMachineBrandId);
        paramObjectMap.put("goodMachineModeId",goodMachineModeId);
        if(StringUtils.isBlank(goodTypeId)){
            paramObjectMap.put("searchInfo",searchInfo);
        }else {
            paramObjectMap.put("searchInfo","");
        }

        paramObjectMap.put("memberId",StringUtils.defaultString(memberId,""));

        //查询店铺店铺商品列表
        if(isPlatform.intValue()==0){
           return Result.ok(iGoodStoreListService.findGoodListByGoodType(page,paramObjectMap));

        }else
            //查询平台商品列表
            if(isPlatform.intValue()==1){
                //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
                String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
                if(goodsSortType.equals("1")&&pattern==0){
                    paramObjectMap.put("pattern",5);
                }

                //活动商品显示控制，加入活动后仅在活动中显示(多选)；0：专区；1：免单；2：中奖拼团
                String activeGoodsDisplayControl = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "active_goods_display_control");
                paramObjectMap.put("isViewPrefecture","1");
                paramObjectMap.put("isViewfree","1");
                paramObjectMap.put("isViewgroup","1");
                if(StringUtils.contains(activeGoodsDisplayControl,"0")){
                    paramObjectMap.put("isViewPrefecture","0");
                }
                if(StringUtils.contains(activeGoodsDisplayControl,"1")){
                    paramObjectMap.put("isViewfree","0");
                }
                if(StringUtils.contains(activeGoodsDisplayControl,"2")){
                    paramObjectMap.put("isViewgroup","0");
                }
                IPage<Map<String, Object>> iPage= iGoodListService.findGoodListByGoodType(page,paramObjectMap);
                iPage.getRecords().forEach(g->{
                    iMemberGradeService.settingGoodListInfo(g,g.get("id").toString(),memberId);
                });
                return Result.ok(iPage);
            }else{
                return Result.error("isPlatform  参数不正确请联系平台管理员！！！  ");
            }
    }


    /**
     * 根据商品id获取商品详情需要的信息
     *
     * @param goodId
     * @param isPlatform
     * isTransition  兼容新旧更新的时候使用，后期考虑去掉
     * @return
     */
    @RequestMapping("findGoodListByGoodId")
    @ResponseBody
    public Result<Map<String,Object>> findGoodListByGoodId(String goodId,
                                                           Integer isPlatform,
                                                           @RequestParam(name = "isTransition",defaultValue = "0",required = false)String isTransition,
                                                           @RequestParam(name = "marketingFreeGoodListId",defaultValue = "",required = false) String marketingFreeGoodListId,
                                                           @RequestParam(name = "marketingGroupGoodListId",defaultValue = "",required = false) String marketingGroupGoodListId,
                                                           @RequestParam(name = "marketingPrefectureId",defaultValue = "",required = false) String marketingPrefectureId,
                                                           @RequestParam(name = "marketingGroupRecordId",defaultValue = "" ,required = false) String marketingGroupRecordId,
                                                           @RequestParam(name = "marketingZoneGroupGoodId",defaultValue = "" ,required = false) String marketingZoneGroupGoodId,
                                                           @RequestParam(name = "marketingRushGoodId",defaultValue = "" ,required = false) String marketingRushGoodId,
                                                           @RequestParam(name = "marketingLeagueGoodListId",defaultValue = "" ,required = false) String marketingLeagueGoodListId,
                                                           @RequestParam(name = "marketingStorePrefectureGoodId",defaultValue = "" ,required = false) String marketingStorePrefectureGoodId,
                                                           @RequestAttribute(name = "memberId",required = false) String memberId){

        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> resultMap=Maps.newHashMap();
        //商品销售   0 禁售； 1 开售；
        String goodsSale = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sale");
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
        paramObjectMap.put("memberId",StringUtils.defaultString(memberId,""));

        //查询店铺商品详情
        if(isPlatform.intValue()==0){
            //判断商品是否被收藏
            if(StringUtils.isNotBlank(memberId)){
               QueryWrapper<MemberGoodsCollection> queryWrapper= new QueryWrapper<>();
               queryWrapper.eq("good_store_list_id",goodId);
               queryWrapper.eq("member_list_id",memberId);
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


            /*兼容数据转换*/
            if(isTransition.equals("1")){
                if(goodStoreListMap.get("mainPicture")!=null){
                    goodStoreListMap.put("mainPicture",goodUtils.imgTransition(goodStoreListMap.get("mainPicture").toString()));
                }
                if(goodStoreListMap.get("detailsGoods")!=null){
                    goodStoreListMap.put("detailsGoods",goodUtils.imgTransition(goodStoreListMap.get("detailsGoods").toString()));
                }

               if(goodStoreListMap.get("specifications")==null){

                    if(goodStoreListMap.get("specification")==null){
                        goodStoreListMap.put("specifications",goodUtils.oldSpecificationToNew("",true));
                    }else {
                        goodStoreListMap.put("specifications",goodUtils.oldSpecificationToNew(goodStoreListMap.get("specification").toString(),true));
                    }

               }
            }

            GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getSmallGoodSpecification(goodId);
            goodStoreListMap.put("smallSpecification",goodStoreSpecification.getSpecification());
            goodStoreListMap.put("goodsSale",goodsSale);
            goodStoreListMap.put("isViewShopCar",1);

            /*店铺专区*/
            if(StringUtils.isNotBlank(marketingStorePrefectureGoodId)){
                iMarketingStorePrefectureGoodService.settingGoodInfo(goodStoreListMap,marketingStorePrefectureGoodId);
            }


            /**
             * 普通商品
             */
            if(StringUtils.isBlank(marketingStorePrefectureGoodId)){
                //显示vip价格
                goodStoreListMap.put("isViewVipPrice","1");
                goodStoreListMap.put("isPrefectureGood","0");
            }

            resultMap.put("goodinfo",goodStoreListMap);
            //获取商品最新的一条评论信息
            Page<Map<String,Object>> evaluatepage = new Page<Map<String,Object>>(1, 1);
            Map<String,Object> paramevaluateMap= Maps.newHashMap();
            paramevaluateMap.put("goodId",goodId);
            paramevaluateMap.put("pattern",0);

            resultMap.put("evaluateInfo",iOrderEvaluateStoreService.findOrderEvaluateByGoodId(evaluatepage,paramevaluateMap).getRecords());
            //评价总数
            resultMap.put("evaluateCount",iOrderEvaluateStoreService.count(new  LambdaQueryWrapper<OrderEvaluateStore>().eq(OrderEvaluateStore::getGoodStoreListId,goodId).eq(OrderEvaluateStore::getStatus, "1")));
        }else
            //查询平台商品详情
            if(isPlatform.intValue()==1){
                //判断商品是否被收藏
                if(StringUtils.isNotBlank(memberId)){
                    QueryWrapper<MemberGoodsCollection> queryWrapper= new QueryWrapper<>();
                    queryWrapper.eq("good_list_id",goodId);
                    queryWrapper.eq("member_list_id",memberId);
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
                if (goodlistMap == null) {
                    throw new JeecgBootException("商品id：" + goodId + " 对应的商品不存在");
                }

                /*兼容数据转换*/
                if(isTransition.equals("1")){
                    if(goodlistMap.get("mainPicture")!=null){
                        goodlistMap.put("mainPicture",goodUtils.imgTransition(goodlistMap.get("mainPicture").toString()));
                    }
                    if(goodlistMap.get("detailsGoods")!=null){
                        goodlistMap.put("detailsGoods",goodUtils.imgTransition(goodlistMap.get("detailsGoods").toString()));
                    }
                    if(goodlistMap.get("specifications")==null){

                        if(goodlistMap.get("specification")==null){
                            goodlistMap.put("specifications",goodUtils.oldSpecificationToNew("",true));
                        }else {
                            goodlistMap.put("specifications",goodUtils.oldSpecificationToNew(goodlistMap.get("specification").toString(),true));
                        }

                    }
                }
                goodlistMap.put("isViewShopCar",1);


                //商品销售   0 禁售； 1 开售；
                goodlistMap.put("goodsSale",goodsSale);

                //中奖拼团商品
                if(StringUtils.isNotBlank(marketingGroupGoodListId)){
                    //不显示vip价格
                    goodlistMap.put("isViewVipPrice","0");
                    goodlistMap.put("isPrefectureGood","3");
                    MarketingGroupGoodList marketingGroupGoodList=iMarketingGroupGoodListService.getById(marketingGroupGoodListId);
                    if(marketingGroupGoodList==null){
                        return result.error500("中奖拼团商品不存在");
                    }
                    goodlistMap.put("marketingGroupGoodListId",marketingGroupGoodList.getId());
                    MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>());
                    MarketingGroupGoodSpecification marketingGroupGoodSpecification=iMarketingGroupGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingGroupGoodSpecification>().
                            eq(MarketingGroupGoodSpecification::getMarketingGroupGoodListId,marketingGroupGoodListId).
                            orderByAsc(MarketingGroupGoodSpecification::getGroupPrice).
                            last("limit 1"));
                    GoodSpecification goodSpecification=iGoodSpecificationService.getById(marketingGroupGoodSpecification.getGoodSpecificationId());
                    //如果拼团找不到规格商品拼团商品停用
                    if(goodSpecification==null){
                        marketingGroupGoodList.setStatus("0");
                        iMarketingGroupGoodListService.saveOrUpdate(marketingGroupGoodList);
                        return result.error500("拼团商品已停用！！！");
                    }
                    goodlistMap.put("smallPrice",goodSpecification.getPrice());
                    goodlistMap.put("groupPrice",marketingGroupGoodSpecification.getGroupPrice());
                    goodlistMap.put("smallSpecification",goodSpecification.getSpecification());
                    goodlistMap.put("label",marketingGroupBaseSetting.getLabel());
                    goodlistMap.put("tuxedoWelfarePayments",marketingGroupGoodList.getTuxedoWelfarePayments());
                    goodlistMap.put("countDown",(marketingGroupGoodList.getEndTime().getTime()-new Date().getTime())/1000);
                    goodlistMap.put("rulesBriefly",marketingGroupBaseSetting.getRulesBriefly());
                    if(StringUtils.isNotBlank(marketingGroupRecordId)){
                        MarketingGroupRecord marketingGroupRecord=iMarketingGroupRecordService.getById(marketingGroupRecordId);
                        goodlistMap.put("countDown",(marketingGroupRecord.getBuyEndTime().getTime()-new Date().getTime())/1000);
                        goodlistMap.put("percentage", 100);
                    }else{
                        MarketingGroupManage marketingGroupManageOne=iMarketingGroupManageService.getOne(new LambdaQueryWrapper<MarketingGroupManage>()
                                .eq(MarketingGroupManage::getMarketingGroupGoodListId,marketingGroupGoodListId)
                                .eq(MarketingGroupManage::getStatus,"0")
                                .ge(MarketingGroupManage::getEndTime, DateUtils.formatDateTime())
                                .le(MarketingGroupManage::getStartTime, DateUtils.formatDateTime()));
                        if(marketingGroupManageOne==null){
                            goodlistMap.put("percentage",0);
                        }else {
                            long marketingGroupRecorCount = iMarketingGroupRecordService.count(new LambdaQueryWrapper<MarketingGroupRecord>().eq(MarketingGroupRecord::getMarketingGroupManageId, marketingGroupManageOne.getId()));
                            if (marketingGroupRecorCount != 0) {
                                goodlistMap.put("percentage", new BigDecimal(marketingGroupRecorCount).divide(marketingGroupManageOne.getNumberTuxedo(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
                            } else {
                                goodlistMap.put("percentage", 0);
                            }
                        }
                    }
                }

                //免单商品
                if(StringUtils.isNotBlank(marketingFreeGoodListId)){
                    //不显示vip价格
                    goodlistMap.put("isViewVipPrice","0");
                    goodlistMap.put("isPrefectureGood","2");
                    MarketingFreeGoodList marketingFreeGoodList=iMarketingFreeGoodListService.getById(marketingFreeGoodListId);
                    if(marketingFreeGoodList==null){
                        result.error500("免单活动商品id找不到相关的商品信息");
                        return  result;
                    }
                    //判断场次信息
                    Map<String,Object> marketingFreeSessionMap= iMarketingFreeSessionService.selectCurrentSession();
                    if(marketingFreeSessionMap==null){
                        //商品销售   0 禁售； 1 开售；
                        goodlistMap.put("goodsSale","0");
                    }
                    goodlistMap.put("marketingFreeGoodListId",marketingFreeGoodList.getId());
                    //获取最低的活动价格和最低的规格信息
                   Map<String,Object> marketingFreeGoodSpecificationMap=  iMarketingFreeGoodSpecificationService.selectMarketingFreeGoodSpecificationBySmallprice(marketingFreeGoodListId);
                    if(marketingFreeGoodSpecificationMap==null){
                        marketingFreeGoodList.setStatus("0");
                        iMarketingFreeGoodListService.saveOrUpdate(marketingFreeGoodList);
                        return result.error500("免单商品已停用！！！");
                    }
                    goodlistMap.put("smallPrice",marketingFreeGoodSpecificationMap.get("freePrice"));
                    goodlistMap.put("smallSpecification",marketingFreeGoodSpecificationMap.get("specification"));
                    MarketingFreeBaseSetting marketingFreeBaseSetting=iMarketingFreeBaseSettingService.getOne(new LambdaQueryWrapper<MarketingFreeBaseSetting>());
                    goodlistMap.put("label",marketingFreeBaseSetting.getLabel());
                    goodlistMap.put("rulesBriefly",marketingFreeBaseSetting.getRulesBriefly());
                }

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
                    goodlistMap.put("label",marketingPrefecture.getPrefectureLabel());
                    //获取专区商品
                    MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(new LambdaQueryWrapper<MarketingPrefectureGood>()
                            .eq(MarketingPrefectureGood::getMarketingPrefectureId,marketingPrefecture.getId())
                            .eq(MarketingPrefectureGood::getGoodListId,goodId));
                    if(marketingPrefectureGood==null){
                        result.error500("此专区商品不存在！！！");
                        return  result;
                    }

                    BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
                    //获取专区最低价格的规格数据
                    Map<String,Object> marketingPrefectureGoodSpecificationMap= iMarketingPrefectureGoodSpecificationService.selectMarketingPrefectureGoodSpecificationBySmallprice(marketingPrefectureGood.getId());
                    if(marketingPrefectureGoodSpecificationMap==null){
                        marketingPrefectureGood.setStatus("0");
                        iMarketingPrefectureGoodService.saveOrUpdate(marketingPrefectureGood);
                        return result.error500("专区商品已停用！！！");
                    }

                    marketingPrefectureGood.setSmallPrefecturePrice(new BigDecimal(marketingPrefectureGoodSpecificationMap.get("prefecturePrice").toString()));
                    goodlistMap.put("smallPrice",marketingPrefectureGoodSpecificationMap.get("prefecturePrice"));
                    goodlistMap.put("smallSpecification",marketingPrefectureGoodSpecificationMap.get("specification"));
                    goodlistMap.put("smallPrefecturePrice",marketingPrefectureGood.getSmallPrefecturePrice());
                    //是否支持福利金抵扣
                    goodlistMap.put("isWelfare",marketingPrefectureGood.getIsWelfare());
                    if(marketingPrefectureGood.getIsWelfare().equals("1")) {
                        goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().divide(integralValue,2,RoundingMode.DOWN));
                    }else if(marketingPrefectureGood.getIsWelfare().equals("2")){
                        if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1){
                            goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).divide(integralValue,2,RoundingMode.DOWN));

                        }else{
                            goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).divide(integralValue,2,RoundingMode.DOWN));

                        }
                    }else if(marketingPrefectureGood.getIsWelfare().equals("3")){
                        if(StringUtils.indexOf(goodlistMap.get("supplyPrice").toString(),"-")>-1){
                            if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(goodlistMap.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN).divide(integralValue,2,RoundingMode.DOWN));
                            }else{
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(StringUtils.trim(StringUtils.substringBefore(goodlistMap.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).divide(integralValue,2,RoundingMode.DOWN));
                            }
                        }else{

                            if(StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(),"-")>-1) {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(goodlistMap.get("supplyPrice").toString())).multiply(new BigDecimal(StringUtils.trim(StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).divide(integralValue,2,RoundingMode.DOWN));
                            }else{
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(goodlistMap.get("supplyPrice").toString())).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).divide(integralValue,2,RoundingMode.DOWN));
                            }

                        }
                    }else{
                        goodlistMap.put("welfareProportionPrice", 0);
                    }


                    //免费积分抵扣
                    if(marketingPrefecture.getIsIntegral().equals("1")) {

                        MarketingIntegralSetting marketingIntegralSetting = iMarketingIntegralSettingService.getMarketingIntegralSetting();
                        if (marketingIntegralSetting!=null) {
                            //获取专区最高价格的规格
                            MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification = iMarketingPrefectureGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingPrefectureGoodSpecification>()
                                    .eq(MarketingPrefectureGoodSpecification::getMarketingPrefectureGoodId, marketingPrefectureGood.getId())
                                    .orderByDesc(MarketingPrefectureGoodSpecification::getPrefecturePrice)
                                    .last("limit 1"));
                            goodlistMap.put("maxIntegral", marketingPrefectureGoodSpecification.getPrefecturePrice().multiply(marketingPrefectureGoodSpecification.getProportionIntegral()).divide(new BigDecimal(100)).divide(marketingIntegralSetting.getPrice(), RoundingMode.DOWN));
                            goodlistMap.put("maxIntegralPrice", marketingPrefectureGoodSpecification.getPrefecturePrice().multiply(marketingPrefectureGoodSpecification.getProportionIntegral()).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN));
                        }else{
                            goodlistMap.put("maxIntegral", 0);
                            goodlistMap.put("maxIntegralPrice",0);
                        }
                    }
                    goodlistMap.put("isIntegral",marketingPrefecture.getIsIntegral());


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

                    //根据限制条件限制商品详情是否显示购物车

                    if(marketingPrefecture.getBuyLimit().equals("1")||marketingPrefecture.getIsIntegral().equals("1")||marketingPrefecture.getRemovePay().equals("1")){
                        goodlistMap.put("isViewShopCar",0);
                    }

                    goodlistMap.put("buyLimit",marketingPrefecture.getBuyLimit());
                    goodlistMap.put("limitCount",marketingPrefecture.getLimitCount());


                }

                //专区团商品
                if(StringUtils.isNotBlank(marketingZoneGroupGoodId)){
                    //不显示vip价格
                    goodlistMap.put("isViewVipPrice","0");
                    goodlistMap.put("isPrefectureGood","4");
                    MarketingZoneGroupGood marketingZoneGroupGood=iMarketingZoneGroupGoodService.getById(marketingZoneGroupGoodId);
                    if(marketingZoneGroupGood==null){
                        result.error500("专区团商品id找不到相关的商品信息");
                        return  result;
                    }
                    goodlistMap.put("marketingZoneGroupGoodId",marketingZoneGroupGoodId);
                    //获取最便宜的规格组合信息
                    GoodSpecification goodSpecification = iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                            .eq(GoodSpecification::getGoodListId, goodId)
                            .gt(GoodSpecification::getRepertory,0)
                            .orderByAsc(GoodSpecification::getPrice).last("limit 1"));
                    //找不到规格数据的商品进行下架处理（到时候要做预警处理）
                    if(goodSpecification==null){
                        GoodList goodList =iGoodListService.getById(goodId);
                        goodList.setFrameStatus("0");//下架
                        iGoodListService.saveOrUpdate(goodList);
                        return result.error500("商品已下架");
                    }
                    MarketingZoneGroup marketingZoneGroup=iMarketingZoneGroupService.getById(marketingZoneGroupGood.getMarketingZoneGroupId());
                    goodlistMap.put("smallSpecification", goodSpecification.getSpecification());
                    goodlistMap.put("smallPrice",marketingZoneGroup.getPrice());
                    goodlistMap.put("smallVipPrice",goodSpecification.getVipPrice());
                    MarketingZoneGroupBaseSetting marketingZoneGroupBaseSetting=iMarketingZoneGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupBaseSetting>()
                            .eq(MarketingZoneGroupBaseSetting::getStatus,"1"));
                    goodlistMap.put("label",marketingZoneGroup.getZoneName());
                    goodlistMap.put("marketingZoneGroupId",marketingZoneGroup.getId());
                    goodlistMap.put("virtualGroupMembers",marketingZoneGroup.getVirtualGroupMembers());
                    goodlistMap.put("rulesBriefly",marketingZoneGroupBaseSetting.getRulesBriefly());
                }



                //抢购商品
                if(StringUtils.isNotBlank(marketingRushGoodId)){
                    //不显示vip价格
                    goodlistMap.put("isViewVipPrice","0");
                    goodlistMap.put("isPrefectureGood","5");

                    MarketingRushGood marketingRushGood=iMarketingRushGoodService.getById(marketingRushGoodId);
                    if(marketingRushGood==null){
                        result.error500("抢购团商品id找不到相关的商品信息");
                        return  result;
                    }
                    goodlistMap.put("marketingRushGoodId",marketingRushGoodId);
                    //获取最便宜的规格组合信息
                    GoodSpecification goodSpecification = iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                            .eq(GoodSpecification::getGoodListId, goodId)
                            .gt(GoodSpecification::getRepertory,0)
                            .orderByAsc(GoodSpecification::getPrice).last("limit 1"));
                    //找不到规格数据的商品进行下架处理（到时候要做预警处理）
                    if(goodSpecification==null){
                        GoodList goodList =iGoodListService.getById(goodId);
                        goodList.setFrameStatus("0");//下架
                        iGoodListService.saveOrUpdate(goodList);
                        return result.error500("商品已下架");
                    }
                    MarketingRushBaseSetting  marketingRushBaseSetting=iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                            .eq(MarketingRushBaseSetting::getStatus,"1"));
                    if(marketingRushBaseSetting==null){
                        return result.error500("抢购设置不存在");
                    }
                    goodlistMap.put("smallSpecification", goodSpecification.getSpecification());
                    goodlistMap.put("smallPrice",marketingRushGood.getPrice());
                    goodlistMap.put("smallVipPrice",goodSpecification.getVipPrice());
                    goodlistMap.put("label",marketingRushBaseSetting.getLabel());
                }

                //加盟专区商品
                if(StringUtils.isNotBlank(marketingLeagueGoodListId)){
                    //不显示vip价格
                    goodlistMap.put("isViewVipPrice","0");
                    goodlistMap.put("isPrefectureGood","6");

                    MarketingLeagueGoodList marketingLeagueGoodList=iMarketingLeagueGoodListService.getById(marketingLeagueGoodListId);
                    if(marketingLeagueGoodList==null){
                        result.error500("找不到相关的加盟专区商品信息");
                        return  result;
                    }
                    goodlistMap.put("marketingLeagueGoodListId",marketingLeagueGoodListId);
                    //获取最便宜的规格组合信息
                    GoodSpecification goodSpecification = iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                            .eq(GoodSpecification::getGoodListId, goodId)
                            .gt(GoodSpecification::getRepertory,0)
                            .orderByAsc(GoodSpecification::getPrice).last("limit 1"));
                    //找不到规格数据的商品进行下架处理（到时候要做预警处理）
                    if(goodSpecification==null){
                        GoodList goodList =iGoodListService.getById(goodId);
                        goodList.setFrameStatus("0");//下架
                        iGoodListService.saveOrUpdate(goodList);
                        return result.error500("商品已下架");
                    }
                    MarketingLeagueGoodSpecification marketingLeagueGoodSpecification=iMarketingLeagueGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingLeagueGoodSpecification>()
                            .eq(MarketingLeagueGoodSpecification::getMarketingLeagueGoodListId,marketingLeagueGoodList.getId())
                            .eq(MarketingLeagueGoodSpecification::getGoodSpecificationId,goodSpecification.getId())
                            .last("limit 1"));
                    if(marketingLeagueGoodSpecification==null){
                        result.error500("此专区商品不存在！！！");
                        return  result;
                    }
                    MarketingLeagueSetting  marketingLeagueSetting=iMarketingLeagueSettingService.getMarketingLeagueSetting();
                    if(marketingLeagueSetting==null){
                        return result.error500("抢购设置不存在");
                    }
                    goodlistMap.put("smallSpecification", goodSpecification.getSpecification());
                    goodlistMap.put("smallPrice",marketingLeagueGoodSpecification.getLeaguePrice());
                    goodlistMap.put("smallVipPrice",goodSpecification.getVipPrice());
                    goodlistMap.put("label",marketingLeagueSetting.getLabel());
                    goodlistMap.put("isViewShopCar",0);
                }


                /**
                 * 普通商品
                 */
                if(StringUtils.isBlank(marketingFreeGoodListId)&&StringUtils.isBlank(marketingPrefectureId)&&StringUtils.isBlank(marketingGroupGoodListId)&&StringUtils.isBlank(marketingZoneGroupGoodId)&&StringUtils.isBlank(marketingRushGoodId)&&StringUtils.isBlank(marketingLeagueGoodListId)){
                    //显示vip价格
                    goodlistMap.put("isViewVipPrice","1");
                    goodlistMap.put("isPrefectureGood","0");
                }


                //添加浏览记录
                if(StringUtils.isNotBlank(memberId)){
                    log.info("商品id为："+goodId+"；会员id为："+memberId+"添加浏览记录成功！！！");
                    iMemberBrowsingHistoryService.addGoodToBrowsingHistory(isPlatform,goodId,memberId,marketingPrefectureId,marketingFreeGoodListId);
                }

                //普通商品交易的时候获取
                if(goodlistMap.get("isPrefectureGood").toString().equals("0")) {
                    //获取最便宜的规格组合信息
                    GoodSpecification goodSpecification = iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                            .eq(GoodSpecification::getGoodListId, goodId)
                            .gt(GoodSpecification::getRepertory,0)
                            .orderByAsc(GoodSpecification::getPrice).last("limit 1"));
                    //找不到规格数据的商品进行下架处理（到时候要做预警处理）
                    if(goodSpecification==null){
                        GoodList goodList =iGoodListService.getById(goodId);
                        goodList.setFrameStatus("0");//下架
                        iGoodListService.saveOrUpdate(goodList);
                        return result.error500("商品已下架");
                    }
                    goodlistMap.put("smallSpecification", goodSpecification.getSpecification());
                    goodlistMap.put("smallPrice",goodSpecification.getPrice());
                    goodlistMap.put("smallVipPrice",goodSpecification.getVipPrice());

                    /*根据会员等级显示*/
                    iMemberGradeService.settingGoodInfo(goodlistMap,goodSpecification,memberId);
                }
                resultMap.put("goodinfo",goodlistMap);
                //获取商品最新的一条评论信息

                Page<Map<String,Object>> evaluatepage = new Page<Map<String,Object>>(1, 1);
                Map<String,Object> paramevaluateMap= Maps.newHashMap();
                paramevaluateMap.put("goodId",goodId);
                paramevaluateMap.put("pattern",0);

                resultMap.put("evaluateInfo",iOrderEvaluateService.findOrderEvaluateByGoodId(evaluatepage,paramevaluateMap).getRecords());
                //评价总数
                resultMap.put("evaluateCount",iOrderEvaluateService.count(new  LambdaQueryWrapper<OrderEvaluate>().eq(OrderEvaluate::getGoodListId,goodId).eq(OrderEvaluate::getStatus, "1")));

            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }

        if(StringUtils.isNotBlank(memberId)){
            resultMap.put("carGoods",iMemberShoppingCartService.count(new LambdaQueryWrapper<MemberShoppingCart>()
                    .eq(MemberShoppingCart::getMemberListId,memberId)
                    .eq(MemberShoppingCart::getIsView,"1")));
        }else{
            resultMap.put("carGoods","0");
        }
        if(StringUtils.isNotBlank(memberId)){
            MemberList memberList = iMemberListService.getById(memberId);

            if (StringUtils.isBlank(marketingPrefectureId)){
                resultMap.put("isQualification","1");
                resultMap.put("qualificationExplain","");
            }else {

                MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(marketingPrefectureId);
                if (marketingPrefecture.getBuyerLimit().contains("1")){

                    if (StringUtils.isNotBlank(marketingPrefecture.getBuyVipMemberGradeId())){

                        if (StringUtils.isNotBlank(memberList.getMemberGradeId())&&marketingPrefecture.getBuyVipMemberGradeId().contains(memberList.getMemberGradeId())){
                            resultMap.put("isQualification","1");
                            resultMap.put("qualificationExplain","");
                        }else {
                            List<String> strings = Arrays.asList(StringUtils.split(marketingPrefecture.getBuyVipMemberGradeId(), ","));
                            ArrayList<String> arrayList = new ArrayList<>();
                            strings.forEach(ss->{
                                MemberGrade memberGrade = iMemberGradeService.getById(ss);
                                arrayList.add(memberGrade.getGradeName());
                            });
                            resultMap.put("isQualification","0");
                            if (StringUtils.isNotBlank(memberList.getMemberGradeId())){
                                MemberGrade memberGrade = iMemberGradeService.getById(memberList.getMemberGradeId());
                                resultMap.put("qualificationExplain","本专区商品仅vip会员"+arrayList+"可购买，您当前身份为VIP会员["+memberGrade.getGradeName()+"]，暂时不支持购买");
                            }else {
                                if (memberList.getMemberType().equals("0")){
                                    resultMap.put("qualificationExplain","本专区商品仅vip会员"+arrayList+"可购买，您当前身份为普通会员，暂时不支持购买");
                                }else {
                                    resultMap.put("qualificationExplain","本专区商品仅vip会员"+arrayList+"可购买，您当前身份为VIP会员，暂时不支持购买");
                                }

                            }

                        }
                    }else {
                        if (marketingPrefecture.getBuyerLimit().contains(memberList.getMemberType())){
                            resultMap.put("isQualification","1");
                            resultMap.put("qualificationExplain","");
                        }else {
                            resultMap.put("isQualification","0");
                            resultMap.put("qualificationExplain","本专区商品仅vip会员可购买，您当前身份为"+iSysDictService.queryDictTextByKey("member_type",memberList.getMemberType())+"，暂时不支持购买");
                        }
                    }

                }
                if (marketingPrefecture.getBuyerLimit().equals("0")){
                    if (marketingPrefecture.getBuyerLimit().contains(memberList.getMemberType())){
                        resultMap.put("isQualification","1");
                        resultMap.put("qualificationExplain","");
                    }else {
                        resultMap.put("isQualification","0");
                        resultMap.put("qualificationExplain","本专区商品仅普通会员可购买，您当前身份为"+iSysDictService.queryDictTextByKey("member_type",memberList.getMemberType())+"，暂时不支持购买");
                    }
                }
                if (marketingPrefecture.getBuyerLimit().contains("0")&&marketingPrefecture.getBuyerLimit().contains("1")){
                    resultMap.put("isQualification","1");
                    resultMap.put("qualificationExplain","");
                }
            }
        }else {
            resultMap.put("isQualification","1");
            resultMap.put("qualificationExplain","");
        }

        //加入客服信息
        String onlineServiceType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "online_service_type");
        if(StringUtils.isNotBlank(onlineServiceType)){
            String onlineServiceUrl = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "online_service_url");
            resultMap.put("onlineServiceUrl",onlineServiceUrl);
            resultMap.put("onlineServiceType",onlineServiceType);
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
     * @param marketingFreeGoodListId
     * @param memberId
     * @return
     */
    @RequestMapping("getGoodBySpecification")
    @ResponseBody
    public Result<Map<String,Object>> getGoodBySpecification(String goodId,
                                                             Integer isPlatform,
                                                             String specification,
                                                             @RequestParam(value = "marketingFreeGoodListId",defaultValue = "",required = false) String marketingFreeGoodListId,
                                                             @RequestParam(value = "marketingPrefectureId",defaultValue = "",required = false) String marketingPrefectureId,
                                                             @RequestParam(value = "marketingZoneGroupGoodId",defaultValue = "",required = false) String marketingZoneGroupGoodId,
                                                             @RequestParam(name = "marketingRushGoodId",defaultValue = "" ,required = false) String marketingRushGoodId,
                                                             @RequestParam(name = "marketingLeagueGoodListId",defaultValue = "" ,required = false) String marketingLeagueGoodListId,
                                                             @RequestParam(name = "marketingStorePrefectureGoodId",defaultValue = "" ,required = false) String marketingStorePrefectureGoodId,
                                                             @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<Map<String,Object>>result=new Result<>();
        Map<String,Object>paramMap=Maps.newHashMap();

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
            log.info("显示店铺商品规格价格，商品id："+goodId+"；商品规格数据："+specification);
            GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getOne(goodStoreSpecificationQueryWrapper);
            if(goodStoreSpecification==null){
                result.error500("找不到此规格的商品！！！");
                return result;
            }
            GoodStoreList goodStoreList=iGoodStoreListService.getById(goodId);
            if(StringUtils.isBlank(goodStoreSpecification.getSpecificationPicture())){
                if(goodStoreList.getMainPicture()!=null){
                    goodStoreList.setMainPicture(goodUtils.imgTransition(goodStoreList.getMainPicture()));
                }
                if(JSON.parseArray(goodStoreList.getMainPicture()).size()>0) {
                    paramMap.put("specificationPicture", JSON.parseArray(goodStoreList.getMainPicture()).get(0));
                }else{
                    paramMap.put("specificationPicture", "");
                }
            }else{
                paramMap.put("specificationPicture",goodStoreSpecification.getSpecificationPicture());
            }
            paramMap.put("repertory",goodStoreSpecification.getRepertory());
            if(StringUtils.isNotBlank(memberId)){
                MemberList memberList=iMemberListService.getById(memberId);
                if(memberList.getMemberType().equals("1")){
                    paramMap.put("price",goodStoreSpecification.getVipPrice().toString());
                }else{
                    paramMap.put("price",goodStoreSpecification.getPrice().toString());
                }
            }else{
                paramMap.put("price",goodStoreSpecification.getPrice());
            }
            paramMap.put("goodId",goodStoreList.getId());
            paramMap.put("specificationId",goodStoreSpecification.getId());

            /*店铺专区*/
            if(StringUtils.isNotBlank(marketingStorePrefectureGoodId)){
                iMarketingStorePrefectureGoodService.settingGoodSpecification(paramMap,marketingStorePrefectureGoodId,specification);
            }
        }else
        //查询平台商品
            if(isPlatform.intValue()==1){
                log.info("显示规格价格，商品id："+goodId+"；商品规格数据："+specification);
                GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                        .eq(GoodSpecification::getGoodListId,goodId)
                        .eq(GoodSpecification::getSpecification,specification)
                        .last("limit 1"));
                if(goodSpecification==null){
                    result.error500("找不到此规格的商品！！！");
                    return result;
                }
                GoodList goodList=iGoodListService.getById(goodId);
                if(StringUtils.isBlank(goodSpecification.getSpecificationPicture())){
                    if(goodList.getMainPicture()!=null){
                        goodList.setMainPicture(goodUtils.imgTransition(goodList.getMainPicture()));
                    }
                    if(JSON.parseArray(goodList.getMainPicture()).size()>0) {
                        paramMap.put("specificationPicture", JSON.parseArray(goodList.getMainPicture()).get(0));
                    }else{
                        paramMap.put("specificationPicture", "");
                    }
                }else{
                    paramMap.put("specificationPicture",goodSpecification.getSpecificationPicture());
                }
                paramMap.put("repertory",goodSpecification.getRepertory());
                paramMap.put("goodId",goodList.getId());
                paramMap.put("specificationId",goodSpecification.getId());

                //普通商品
                if(StringUtils.isBlank(marketingPrefectureId)&&StringUtils.isBlank(marketingFreeGoodListId)&&StringUtils.isBlank(marketingLeagueGoodListId)) {
                    log.info("商品是普通商品，规格id:"+goodSpecification.getId());
                    if (StringUtils.isNotBlank(memberId)) {
                        MemberList memberList = iMemberListService.getById(memberId);
                        if (memberList.getMemberType().equals("1")) {
                            paramMap.put("price", goodSpecification.getVipPrice());
                            iMemberGradeService.settingGoodSpecificationInfo(paramMap,goodSpecification,memberId);
                        } else {
                            paramMap.put("price", goodSpecification.getPrice().toString());
                        }
                    } else {
                        paramMap.put("price", goodSpecification.getPrice().toString());
                    }
                }


                //专区团
                if(StringUtils.isNotBlank(marketingZoneGroupGoodId)){
                    MarketingZoneGroupGood marketingZoneGroupGood=iMarketingZoneGroupGoodService.getById(marketingZoneGroupGoodId);
                    MarketingZoneGroup marketingZoneGroup=iMarketingZoneGroupService.getById(marketingZoneGroupGood.getMarketingZoneGroupId());
                    paramMap.put("price", marketingZoneGroup.getPrice().toString());
                    paramMap.put("label",marketingZoneGroup.getZoneName());
                }

                //专区团
                if(StringUtils.isNotBlank(marketingRushGoodId)){
                    MarketingRushGood marketingRushGood=iMarketingRushGoodService.getById(marketingRushGoodId);
                    MarketingRushBaseSetting  marketingRushBaseSetting=iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                            .eq(MarketingRushBaseSetting::getStatus,"1"));
                    paramMap.put("price", marketingRushGood.getPrice().toString());
                    paramMap.put("label",marketingRushBaseSetting.getLabel());
                }

                //加盟专区
                if(StringUtils.isNotBlank(marketingLeagueGoodListId)){
                    MarketingLeagueGoodList marketingLeagueGoodList=iMarketingLeagueGoodListService.getById(marketingLeagueGoodListId);
                    MarketingLeagueSetting  marketingLeagueSetting=iMarketingLeagueSettingService.getMarketingLeagueSetting();
                    MarketingLeagueGoodSpecification marketingLeagueGoodSpecification=iMarketingLeagueGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingLeagueGoodSpecification>()
                            .eq(MarketingLeagueGoodSpecification::getMarketingLeagueGoodListId,marketingLeagueGoodList.getId())
                            .eq(MarketingLeagueGoodSpecification::getGoodSpecificationId,goodSpecification.getId())
                            .last("limit 1"));
                    if(marketingLeagueGoodSpecification==null){
                        result.error500("此专区商品不存在！！！");
                        return  result;
                    }
                    paramMap.put("price", marketingLeagueGoodSpecification.getLeaguePrice().toString());
                    paramMap.put("label",marketingLeagueSetting.getLabel());
                }

                //专区商品
                if (StringUtils.isNotBlank(marketingPrefectureId)){
                    log.info("商品是专区商品，商品id"+goodId+"；专区id:"+marketingPrefectureId);
                    //获取专区商品
                    MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(new LambdaQueryWrapper<MarketingPrefectureGood>()
                            .eq(MarketingPrefectureGood::getMarketingPrefectureId,marketingPrefectureId).eq(MarketingPrefectureGood::getGoodListId,goodId)
                            .last("limit 1"));
                    if(marketingPrefectureGood==null){
                        result.error500("此专区商品不存在！！！");
                        return  result;
                    }
                    //获取专区规格
                    MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification=iMarketingPrefectureGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingPrefectureGoodSpecification>()
                            .eq(MarketingPrefectureGoodSpecification::getMarketingPrefectureGoodId,marketingPrefectureGood.getId())
                            .eq(MarketingPrefectureGoodSpecification::getGoodSpecificationId,goodSpecification.getId()));
                    if(marketingPrefectureGoodSpecification==null){
                        result.error500("此专区商品规格不存在！！！");
                        return  result;
                    }
                    paramMap.put("price", marketingPrefectureGoodSpecification.getPrefecturePrice().toString());
                }
                //免单商品
                if(StringUtils.isNotBlank(marketingFreeGoodListId)){
                    log.info("商品是免单商品，免单商品id："+marketingFreeGoodListId+"；规格id："+goodSpecification.getId());
                    //获取免单商品规格信息
                    MarketingFreeGoodSpecification marketingFreeGoodSpecification=iMarketingFreeGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingFreeGoodSpecification>()
                            .eq(MarketingFreeGoodSpecification::getMarketingFreeGoodListId,marketingFreeGoodListId)
                            .eq(MarketingFreeGoodSpecification::getGoodSpecificationId,goodSpecification.getId()));
                    if(marketingFreeGoodSpecification==null){
                        result.error500("免单商品规格不存在！！！");
                        return result;
                    }
                    paramMap.put("price", marketingFreeGoodSpecification.getFreePrice().toString());

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
     * 每周特惠
     * @param price
     * @return
     */
    @RequestMapping("getEveryWeekPreferential")
    @ResponseBody
    @Deprecated
    public Result<Map<String,Object>> getEveryWeekPreferential(String price,
                                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<Map<String,Object>>result=new Result<>();
        Map<String,Object> objectMap = Maps.newHashMap();
        List<DictModel> ls = sysDictService.queryDictItemsByCode("special_zone");
        if(StringUtils.isBlank(price)){
            result.error500("价格区间不能为空！");
            return result;
        }
        Map<String,Object> map = Maps.newHashMap();
      //  String  data= getLastTimeInterval();
        String[]  pricelist;
        pricelist = price.split("-");
       // String[]  datalist = data.split(",");
      //  map.put("dateBegin",datalist[0]);
       // map.put("dateEnd",datalist[1]);
        map.put("smallPriceMin",pricelist[0]);
        map.put("smallPriceMax",pricelist[1]);
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        IPage<Map<String,Object>> listIPage = iGoodStoreListService.getEveryWeekPreferential(page,map);
        //IPage<Map<String,Object>> listIPage1 = iGoodListService.getEveryWeekPreferential(page,map);
        objectMap.put("listIPage",listIPage);
        objectMap.put("listMenu",ls);
        result.setResult(objectMap);
        result.success("每周特惠查询成功");
      return result;
    }
    /**
     * 每日上新的6个商品轮播
     * @param isPlatform
     * @return
     */
    @RequestMapping("getNewProduct")
    @ResponseBody
    @Deprecated
    public  Result<Map<String,Object>> getNewProduct(String isPlatform){
        Result<Map<String,Object>>result=new Result<>();
        List<Map<String,Object>> objectList = Lists.newArrayList();
        Map<String,Object> objectMap = Maps.newHashMap();
        //Map<String,Object> map = Maps.newHashMap();
     if(isPlatform != null && "0".equals(isPlatform)){
         QueryWrapper<GoodStoreList> queryWrapperGoodList = new QueryWrapper<GoodStoreList>();
         queryWrapperGoodList.orderByDesc("create_time");
         queryWrapperGoodList.eq("status","1");
         queryWrapperGoodList.eq("frame_status","1");
         queryWrapperGoodList.last("limit 5");
         List<GoodStoreList> goodStoreLists = iGoodStoreListService.list(queryWrapperGoodList);
         goodStoreLists.forEach(goodStoreList -> {
             Map<String,Object> map = Maps.newHashMap();
             map.put("id",goodStoreList.getId());
             map.put("goodName",goodStoreList.getGoodName());
             map.put("mainPicture",goodStoreList.getMainPicture());
             map.put("isPlatform","0");
             objectList.add(map);
         });
     }else{
         QueryWrapper<GoodList> queryWrapperGoodList = new QueryWrapper<GoodList>();
         queryWrapperGoodList.orderByDesc("create_time");
         queryWrapperGoodList.eq("status","1");
         queryWrapperGoodList.eq("frame_status","1");
         queryWrapperGoodList.eq("good_form","0");
         queryWrapperGoodList.last("limit 5");
         List<GoodList> goodLists = iGoodListService.list(queryWrapperGoodList);
         goodLists.forEach(goodList -> {
             Map<String,Object> map = Maps.newHashMap();
             map.put("id",goodList.getId());
             map.put("goodName",goodList.getGoodName());
             map.put("mainPicture",goodList.getMainPicture());
             map.put("isPlatform","1");
             objectList.add(map);
         });
     }

        objectMap.put("objectList",objectList);
        result.setResult(objectMap);
        result.success("每周特惠查询成功");
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

}
