package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 * 优惠券接口控制层
 */
@RequestMapping("back/marketingDiscount")
@Controller
public class BackMarketingDiscountController {

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;
    /**
     * 根据商品id查询优惠券列表
     * @param goodId
     * @param isPlatform
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingDiscountByGoodId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingDiscountByGoodId(String goodId, Integer isPlatform, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest request){
        Result<IPage<Map<String,Object>>> result=new Result<>();
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
        //用户登录判断
        Object sysUserId=request.getAttribute("sysUserId");
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("goodId",goodId);
        paramMap.put("isPlatform",isPlatform);

        IPage<Map<String,Object>> marketingDiscountMapIPage=iMarketingDiscountService.findMarketingDiscountByGoodId(page,paramMap);
        List<Map<String,Object>> marketingDiscount=marketingDiscountMapIPage.getRecords();
        for (Map<String,Object> md:marketingDiscount) {
            if(sysUserId!=null) {
                QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
                marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", md.get("id"));
                //marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
                marketingDiscountCouponQueryWrapper.eq("sys_user_id", sysUserId);
                marketingDiscountCouponQueryWrapper.in("status", "0", "1");
                if (iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper) > 0) {
                    md.put("ifGet", "1");
                    MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.list(marketingDiscountCouponQueryWrapper).get(0);
                    md.put("marketingDiscountCouponId",marketingDiscountCoupon.getId());
                    md.put("marketingDiscountCouponStatus",marketingDiscountCoupon.getStatus());
                } else {
                    md.put("ifGet", "0");
                }
            }else{
                md.put("ifGet", "0");
            }
        }
        result.setResult(marketingDiscountMapIPage);
        result.success("查询优惠券成功");
        return result;
    }

}
