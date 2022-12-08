package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.member.entity.MemberBrowsingHistory;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberBrowsingHistoryService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags="浏览记录")
@RestController
@Controller
@RequestMapping("after/memberBrowsingHistory")
public class AfterMemberBrowsingHistoryController {
    @Autowired
    private IMemberBrowsingHistoryService memberBrowsingHistoryService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private  IMarketingPrefectureGoodService iMarketingPrefectureGoodService;
    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;
    /**
     * 根据ids删除收藏
     * @param ids
     * @return
     */
    @RequestMapping("delMemberBrowsingHistoryByIds")
    @ResponseBody
    public Result<String> delMemberBrowsingHistoryByIds(String ids){
        Result<String>result = new Result<>();

        //删除收藏
        memberBrowsingHistoryService.removeByIds(Arrays.asList(StringUtils.split(ids,",")));

        result.success("批量删除浏览记录成功");
        return result;
    }

    /**
     *
     * 查询历史记录信息
     *
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findBrowsingHistorys")
    @ResponseBody
   public Result<IPage<Map<String,Object>>> findBrowsingHistorys(@RequestAttribute(name = "memberId" ,required = false) String memberId,
                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
       Result<IPage<Map<String,Object>>> result=new Result<>();

       //组织查询参数
       Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);

       IPage<Map<String,Object>> browsingHistoryMapIPage=memberBrowsingHistoryService.findBrowsingHistoryBySort(page,memberId);
        List<Map<String,Object>>  browsingHistoryMap= Lists.newArrayList();
        for (Map<String,Object> bhm:browsingHistoryMapIPage.getRecords()) {
            QueryWrapper<MemberBrowsingHistory> memberBrowsingHistoryQueryWrapper=new QueryWrapper<>();
            memberBrowsingHistoryQueryWrapper.eq("year",bhm.get("YEAR"));
            memberBrowsingHistoryQueryWrapper.eq("month",bhm.get("MONTH"));
            memberBrowsingHistoryQueryWrapper.eq("day",bhm.get("DAY"));
            List<MemberBrowsingHistory> memberBrowsingHistories=memberBrowsingHistoryService.list(memberBrowsingHistoryQueryWrapper);
            List<Map<String,Object>>  goodsMap= Lists.newArrayList();
            bhm.put("goodsMap",goodsMap);
            for (MemberBrowsingHistory m:memberBrowsingHistories) {
                Map<String,Object> objectMap= Maps.newHashMap();
                objectMap.put("memberBrowsingHistoryId",m.getId());
                objectMap.put("isPlatform",m.getGoodType());
                MemberList memberList=iMemberListService.getById(memberId);
                if(m.getGoodType().equals("0")){
                    //店铺商品
                    GoodStoreList goodStoreList=iGoodStoreListService.getById(m.getGoodStoreListId());
                    if(goodStoreList==null){
                        continue;
                    }
                    objectMap.put("mainPicture",goodStoreList.getMainPicture());
                    objectMap.put("goodName",goodStoreList.getGoodName());
                    if (memberList.getMemberType().equals("1")) {
                        objectMap.put("price", goodStoreList.getSmallVipPrice());
                    } else {
                        objectMap.put("price", goodStoreList.getSmallPrice());
                    }
                    objectMap.put("goodId",goodStoreList.getId());

                    objectMap.put("repertory",goodStoreList.getRepertory());

                    objectMap.put("frameStatus",goodStoreList.getFrameStatus());

                    objectMap.put("status",goodStoreList.getStatus());
                }else{
                    //平台商品

                    GoodList goodList=iGoodListService.getById(m.getGoodListId());
                    if(goodList==null){
                        continue;
                    }
                    objectMap.put("mainPicture",goodList.getMainPicture());
                    objectMap.put("goodName",goodList.getGoodName());
                    //判断是否属专区商品
                   if(StringUtils.isNotBlank(m.getMarketingPrefectureId())){


                          List<MarketingPrefectureGood>  marketingPrefectureGoodList = iMarketingPrefectureGoodService.list(new LambdaQueryWrapper<MarketingPrefectureGood>()
                                   .eq(MarketingPrefectureGood::getGoodListId,m.getGoodListId())
                                   .eq(MarketingPrefectureGood::getMarketingPrefectureId,m.getMarketingPrefectureId()));
                           if(marketingPrefectureGoodList.size()>0){
                               //专区商品价
                               objectMap.put("price", marketingPrefectureGoodList.get(0).getSmallPrefecturePrice());
                           }else{
                               //平台商品价格
                               if (memberList.getMemberType().equals("1")) {
                                   objectMap.put("price", goodList.getSmallVipPrice());
                               } else {
                                   objectMap.put("price", goodList.getSmallPrice());
                               }
                           }
                       MarketingPrefecture marketingPrefecture =   iMarketingPrefectureService.getById(m.getMarketingPrefectureId());
                       if(marketingPrefecture!=null){
                           //专区标签
                           objectMap.put("label", marketingPrefecture.getPrefectureLabel());
                           objectMap.put("marketingPrefectureId", marketingPrefecture.getId());

                       }
                        }else{
                       //平台商品价格
                       if (memberList.getMemberType().equals("1")) {
                           objectMap.put("price", goodList.getSmallVipPrice());
                           //专区标签
                           objectMap.put("label", "");
                       } else {
                           objectMap.put("price", goodList.getSmallPrice());
                           //专区标签
                           objectMap.put("label", "");
                       }
                   }

                    objectMap.put("goodId",goodList.getId());

                    objectMap.put("repertory",goodList.getRepertory());

                    objectMap.put("frameStatus",goodList.getFrameStatus());

                    objectMap.put("status",goodList.getStatus());
                }

                objectMap.put("depreciate",new BigDecimal(m.getBrowsingPrice()).subtract(new BigDecimal(objectMap.get("price").toString())));

                //获取优惠券
                //组织查询参数
                Page<Map<String,Object>> dispage = new Page<Map<String,Object>>(1, 3);
                Map<String,Object> paramMap= Maps.newHashMap();
                paramMap.put("goodId",objectMap.get("goodId"));
                paramMap.put("isPlatform",objectMap.get("isPlatform"));
                objectMap.put("discounts",iMarketingDiscountService.findMarketingDiscountByGoodId(dispage,paramMap).getRecords());
                goodsMap.add(objectMap);
            }
            browsingHistoryMap.add(bhm);
        }
        browsingHistoryMapIPage.setRecords(browsingHistoryMap);
       result.setResult(browsingHistoryMapIPage);
       result.success("查询浏览商品列表成功");
       return result;
   }

}
