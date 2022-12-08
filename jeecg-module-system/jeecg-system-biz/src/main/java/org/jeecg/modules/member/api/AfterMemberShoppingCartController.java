package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.service.IMemberShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("after/memberShoppingCart")
@Log
public class AfterMemberShoppingCartController {
    @Autowired
    private IMemberShoppingCartService iMemberShoppingCartService;

    @Autowired
    private IGoodListService iGoodListService;




    /**
     * 添加商品到购物车
     * @param goodId
     * @param specification
     * @param isPlatform
     * @param quantity
     * @param marketingFreeGoodListId
     * @param marketingPrefectureId
     * @param memberId
     * @return
     */
    @RequestMapping("addGoodToShoppingCart")
    @ResponseBody
    public Result<String> addGoodToShoppingCart(String goodId,
                                                String specification,
                                                Integer isPlatform,
                                                Integer quantity,
                                                @RequestParam(value = "marketingPrefectureId",defaultValue = "",required = false) String marketingPrefectureId,
                                                @RequestParam(value = "marketingFreeGoodListId",defaultValue = "",required = false) String marketingFreeGoodListId,
                                                @RequestParam(value = "marketingStoreGiftCardMemberListId",defaultValue = "",required = false) String marketingStoreGiftCardMemberListId,
                                                @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<String> result=new Result<>();

        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId商品id不能为空！！！");
            return result;
        }

        if(quantity==null){
            result.error500("quantity不能为空！！！");
            return result;
        }
        if(quantity.intValue()<1){
            result.error500("购买商品数必须大于等于1！！！");
            return result;
        }
        if(specification==null){
            result.error500("specification不能为空！！！");
            return result;
        }
        String backResult=iMemberShoppingCartService.addGoodToShoppingCart(isPlatform,goodId,specification,memberId,quantity,"1",marketingPrefectureId,marketingFreeGoodListId,"",marketingStoreGiftCardMemberListId,"",null);


        if(backResult.indexOf("SUCCESS")==-1){
            result.error500(backResult);
            return result;
        }
        result.setResult(StringUtils.substringAfter(backResult,"="));
        result.success("添加购物车成功");
        return result;
    }


    /**
     * 查询购物车商品数(废除)
     * @return
     */
    @RequestMapping("findCarGoods")
    @ResponseBody
    @Deprecated
    public Result<String> findCarGoods(@RequestAttribute(value = "memberId",required = false) String memberId,
                                       @RequestParam(name = "sysUserId",defaultValue = "",required = false) String sysUserId,
                                       @RequestParam(value = "marketingStoreGiftCardMemberListId",defaultValue = "",required = false) String marketingStoreGiftCardMemberListId){
        Result<String> result=new Result<>();
        String isView="1";
        if(StringUtils.isNotBlank(marketingStoreGiftCardMemberListId)){
            isView="0";
        }
        result.setResult(iMemberShoppingCartService.count(new LambdaQueryWrapper<MemberShoppingCart>()
                .eq(MemberShoppingCart::getMemberListId,memberId)
                .eq(MemberShoppingCart::getIsView,isView)
                .eq(StringUtils.isNotBlank(sysUserId),MemberShoppingCart::getSysUserId,sysUserId)
                .eq(StringUtils.isNotBlank(marketingStoreGiftCardMemberListId),MemberShoppingCart::getMarketingStoreGiftCardMemberListId,marketingStoreGiftCardMemberListId))+"");

        result.success("查询购物车商品数成功");
        return result;
    }


    /**
     * 获取购物车列表商品
     * @param memberId
     * @return
     */
    @RequestMapping("getCarGoodByMemberId")
    @ResponseBody
    public Result<Map<String,Object>> getCarGoodByMemberId(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                           @RequestParam(name = "sysUserId",defaultValue = "",required = false) String sysUserId,
                                                           @RequestParam(value = "marketingStoreGiftCardMemberListId",defaultValue = "",required = false) String marketingStoreGiftCardMemberListId){
        Result<Map<String,Object>> result=new Result<>();

        String isView="1";
        if(StringUtils.isNotBlank(marketingStoreGiftCardMemberListId)){
            isView="0";
        }
       //查询购物车商品
        List<MemberShoppingCart> memberShoppingCarts=iMemberShoppingCartService.list(new LambdaQueryWrapper<MemberShoppingCart>()
                .eq(MemberShoppingCart::getMemberListId,memberId)
                .eq(MemberShoppingCart::getIsView,isView)
                .eq(StringUtils.isNotBlank(sysUserId),MemberShoppingCart::getSysUserId,sysUserId)
                .eq(StringUtils.isNotBlank(marketingStoreGiftCardMemberListId),MemberShoppingCart::getMarketingStoreGiftCardMemberListId,marketingStoreGiftCardMemberListId)
                .orderByDesc(MemberShoppingCart::getCreateTime));
        //购物车商品数据查询
        result.setResult(iGoodListService.getCarGoodByMemberId(memberShoppingCarts,memberId));
        result.success("查询购物车成功");
        return result;

    }

    /**
     * 根据id删除购物车信息
     * @param ids
     * @return
     */
    @RequestMapping("delCarGood")
    @ResponseBody
    public Result<String> delCarGood(String ids){
        Result<String> result=new Result<>();

        if(StringUtils.isBlank(ids)){
            return result.error500("删除购物车id不能为空");
        }

        //删除会员下面的购物车信息
        iMemberShoppingCartService.removeByIds(Arrays.asList(StringUtils.split(ids,",")));

        result.success("删除购物车商品成功");
        return  result;
    }


    /**
     * 修改购物车购买商品数量
     * @return
     */
    @RequestMapping("updateCarGood")
    @ResponseBody
    public Result<?> updateCarGood(String id,Integer quantity){

        //参数判断
        if(StringUtils.isBlank(id)){
            return Result.error("id不能为空！！！");
        }

        if(quantity==null){
            return Result.error("quantity不能为空！！！");
        }

        //修改购物车商品数
        MemberShoppingCart memberShoppingCart=iMemberShoppingCartService.getById(id);
        if(memberShoppingCart==null){
            return Result.error("购物车id没有找到！！！");
        }
        if(quantity.intValue()>0) {
            log.info("编辑购物车id："+memberShoppingCart.getId()+"；数量："+quantity);
            memberShoppingCart.setQuantity(new BigDecimal(quantity));
            iMemberShoppingCartService.saveOrUpdate(memberShoppingCart);
        }else{
            log.info("删除购物车id："+memberShoppingCart.getId()+"；数量："+quantity);
            //数量小于零就进行删除
            iMemberShoppingCartService.removeById(memberShoppingCart.getId());
        }
        return  Result.ok(memberShoppingCart);
    }
}
