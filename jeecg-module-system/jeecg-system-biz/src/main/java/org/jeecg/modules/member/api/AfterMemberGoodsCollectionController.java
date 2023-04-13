package org.jeecg.modules.member.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.member.entity.MemberGoodsCollection;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.service.IMemberGoodsCollectionService;
import org.jeecg.modules.member.service.IMemberShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品收藏api控制器
 */
@RequestMapping("after/memberGoodsCollection")
@Controller
public class AfterMemberGoodsCollectionController {

    @Autowired
    private IMemberGoodsCollectionService iMemberGoodsCollectionService;

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private   IMemberShoppingCartService iMemberShoppingCartService;
    /**
     * 分页查询用户收藏列表
     *
     * @param pattern  1：默认；2：降价；3：活动；4：有券
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMemberGoodsCollections")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMemberGoodsCollections( Integer pattern,HttpServletRequest request, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        String memberId= request.getAttribute("memberId").toString();
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("memberId",memberId);
        paramObjectMap.put("pattern",pattern);
        IPage<Map<String,Object>> mapIPage=iMemberGoodsCollectionService.findMemberGoodsCollections(page,paramObjectMap);
//        mapIPage.getRecords().stream().forEach(m->{
//            //获取优惠券
//            //组织查询参数
//            Page<Map<String,Object>> dispage = new Page<Map<String,Object>>(1, 3);
//            Map<String,Object> paramMap= Maps.newHashMap();
//            paramMap.put("goodId",m.get("goodId"));
//            paramMap.put("isPlatform",m.get("isPlatform"));
//            m.put("discounts",iMarketingDiscountService.findMarketingDiscountByGoodId(dispage,paramMap).getRecords());
//
//            //添加专区标签
//
//
//        });

        result.setResult(mapIPage);
        result.success("获取商品收藏列表");
        return result;
    }
    /**
     * 商品收藏
     * @param goodId
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("addMemberGoodsCollectionByGoodId")
    @ResponseBody
    public Result<String> addMemberGoodsCollectionByGoodId(String goodId,
                                                           Integer isPlatform,
                                                           String marketingPrefectureId,
                                                           HttpServletRequest request){
        Result<String>result = new Result<>();
        String memberId= request.getAttribute("memberId").toString();

        //商品收藏
        if(!iMemberGoodsCollectionService.addMemberGoodsCollectionByGoodId(goodId,isPlatform,memberId,marketingPrefectureId)){
                result.error500("isPlatform参数不正确请联系平台管理员！！！");
                return result;
            }

        result.success("商品收藏成功");
        return result;
    }


    /**
     * 商品批量收藏
     * @param goodIds
     * @param isPlatforms
     * @param request
     * @return
     */
    @RequestMapping("addMemberGoodsCollectionByGoodIds")
    @ResponseBody
    public Result<String> addMemberGoodsCollectionByGoodIds(String ids,
                                                            String goodIds,
                                                            String isPlatforms,
                                                            HttpServletRequest request){
        Result<String>result = new Result<>();
        String memberId= request.getAttribute("memberId").toString();

        List<String> idsList=Arrays.asList(StringUtils.split(goodIds,","));
        if(idsList.size()<=0){
            result.error500("收藏的商品数据不能为空");
            return result;
        }
        List<String> isPlatformsList=Arrays.asList(StringUtils.split(isPlatforms,","));
        for(int i=0;i<idsList.size();i++){
            MemberShoppingCart memberShoppingCart = iMemberShoppingCartService.getById(idsList.get(i));
           String marketingPrefectureId = "";
            if(memberShoppingCart!= null && StringUtils.isNotBlank(memberShoppingCart.getMarketingPrefectureId())){
                marketingPrefectureId =memberShoppingCart.getMarketingPrefectureId();
            }

            //商品收藏
            if(!iMemberGoodsCollectionService.addMemberGoodsCollectionByGoodId(idsList.get(i),Integer.parseInt(isPlatformsList.get(i)),memberId,marketingPrefectureId)){
                result.error500("isPlatform参数不正确请联系平台管理员！！！");
                return result;
            }
        }


        result.success("商品批量收藏成功");
        return result;
    }


    /**
     * 根据商品id取消收藏
     * @param goodId
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("delMemberGoodsCollectionByGoodId")
    @ResponseBody
    public Result<String> delMemberGoodsCollectionByGoodId(String goodId, Integer isPlatform, HttpServletRequest request){
        Result<String>result = new Result<>();
        String memberId= request.getAttribute("memberId").toString();



        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId商品id不能为空！！！");
            return result;
        }

        MemberGoodsCollection memberGoodsCollection=null;

        if(isPlatform.intValue()==0){
            QueryWrapper<MemberGoodsCollection> memberGoodsCollectionQueryWrapper=new QueryWrapper<>();
            memberGoodsCollectionQueryWrapper.eq("member_list_id",memberId);
            memberGoodsCollectionQueryWrapper.eq("good_type","0");
            memberGoodsCollectionQueryWrapper.eq("good_store_list_id",goodId);
            memberGoodsCollection=iMemberGoodsCollectionService.getOne(memberGoodsCollectionQueryWrapper);
        }else

            if(isPlatform.intValue()==1){
                QueryWrapper<MemberGoodsCollection> memberGoodsCollectionQueryWrapper=new QueryWrapper<>();
                memberGoodsCollectionQueryWrapper.eq("member_list_id",memberId);
                memberGoodsCollectionQueryWrapper.eq("good_type","1");
                memberGoodsCollectionQueryWrapper.eq("good_list_id",goodId);
                memberGoodsCollection=iMemberGoodsCollectionService.getOne(memberGoodsCollectionQueryWrapper);
            }else{
                result.error500("isPlatform参数不正确请联系平台管理员！！！");
                return result;
            }

            //删除收藏
            if(memberGoodsCollection!=null){
                iMemberGoodsCollectionService.removeById(memberGoodsCollection.getId());
            }

        result.success("商品删除收藏成功");
        return result;
    }


    /**
     * 根据ids删除收藏
     * @param ids
     * @return
     */
    @RequestMapping("delMemberGoodsCollectionByIds")
    @ResponseBody
    public Result<String> delMemberGoodsCollectionByIds(String ids){
        Result<String>result = new Result<>();

        //删除收藏
        iMemberGoodsCollectionService.removeByIds(Arrays.asList(StringUtils.split(ids,",")));

        result.success("批量删除收藏成功");
        return result;
    }
}
