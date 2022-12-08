package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.order.utils.TotalPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 兑换券登录后api
 */
@RequestMapping("after/marketingCertificate")
@Controller
@Slf4j
public class AfterMarketingCertificateController {


    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;

    @Autowired
    private TotalPayUtils totalPayUtils;
    @Autowired
    private IMarketingCertificateGroupListService iMarketingCertificateGroupListService;
    @Autowired
    private IMarketingCertificateSeckillListService iMarketingCertificateSeckillListService;
    @Autowired
    private IMarketingCertificateGroupManageService iMarketingCertificateGroupManageService;

    @Autowired
    private IMarketingCertificateGroupRecordService iMarketingCertificateGroupRecordService;
    @Autowired
    private IMarketingCertificateSeckillRecordService iMarketingCertificateSeckillRecordService;

    /**
     * 兑换券支付
     *
     * @param id
     * @param quantity
     * @param request
     * @return
     */
    @RequestMapping("submitCertificate")
    @ResponseBody
    @Transactional
    public Result<?> submitCertificate(String id,
                                       Integer quantity,
                                       String type,
                                       @RequestHeader(defaultValue = "0") BigDecimal longitude,
                                       @RequestHeader(defaultValue = "0") BigDecimal latitude,
                                       @RequestHeader(defaultValue = "") String sysUserId,
                                       @RequestHeader(defaultValue = "") String softModel,
                                       @RequestAttribute(value = "memberId",required = false) String memberId,
                                       HttpServletRequest request) {
        log.info("销售店铺userId: " + sysUserId);
        Result<Map<String, Object>> result = new Result<>();
        log.info("支付类型: "+ softModel);
        if (StringUtils.isBlank(id)) {
            result.error500("id不能为空！！！");
            return result;
        }

        if (quantity == null) {
            result.error500("quantity不能为空！！！");
            return result;
        }
        if (quantity.intValue() < 1) {
            result.error500("购买商品数必须大于等于1！！！");
            return result;
        }
        if (StringUtils.isBlank(type)||type.equals("0")){
            BigDecimal totalPrice = new BigDecimal(0);
            //获取兑换券
            MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(id);

            if (marketingCertificate.getTotal().subtract(marketingCertificate.getReleasedQuantity()).doubleValue() < quantity) {
                return result.error500("库存不足!请重新购买");
            }

            //计算出购买总价
            totalPrice = marketingCertificate.getPrice().multiply(new BigDecimal(quantity));

            //收银台
            result.setResult(totalPayUtils.payCertificate(request,marketingCertificate.getId(),new BigDecimal(quantity),memberId,longitude,latitude,totalPrice,sysUserId,softModel,"0",""));
        }else {
            if (type.equals("1")){
                if (quantity.intValue() > 1) {
                    result.error500("拼好券只能购买一张！！！");
                    return result;
                }
                BigDecimal totalPrice = new BigDecimal(0);
                MarketingCertificateGroupList marketingCertificateGroupList = iMarketingCertificateGroupListService.getById(id);
                //获取兑换券
                MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(marketingCertificateGroupList.getMarketingCertificateId());

                if (marketingCertificate.getTotal().subtract(marketingCertificate.getReleasedQuantity()).doubleValue() < quantity) {
                    return result.error500("库存不足!请重新购买");
                }

                //计算出购买总价
                totalPrice = marketingCertificateGroupList.getActivityPrice().multiply(new BigDecimal(quantity));

                //收银台
                result.setResult(totalPayUtils.payCertificate(request,marketingCertificate.getId(),new BigDecimal(quantity),memberId,longitude,latitude,totalPrice,sysUserId,softModel,"1",id));
            }else if (type.equals("2")){
                BigDecimal totalPrice = new BigDecimal(0);
                //
                MarketingCertificateSeckillList marketingCertificateSeckillList = iMarketingCertificateSeckillListService.getById(id);
                //获取兑换券
                MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(marketingCertificateSeckillList.getMarketingCertificateId());

                if (marketingCertificate.getTotal().subtract(marketingCertificate.getReleasedQuantity()).doubleValue() < quantity) {
                    return result.error500("库存不足!请重新购买");
                }

                //计算出购买总价
                totalPrice = marketingCertificateSeckillList.getActivityPrice().multiply(new BigDecimal(quantity));

                //收银台
                result.setResult(totalPayUtils.payCertificate(request,marketingCertificate.getId(),new BigDecimal(quantity),memberId,longitude,latitude,totalPrice,sysUserId,softModel,"2",id));
            }else if (type.equals("3")){
                if (quantity.intValue() > 1) {
                    result.error500("拼好券只能购买一张！！！");
                    return result;
                }

                BigDecimal totalPrice = new BigDecimal(0);
                MarketingCertificateGroupManage marketingCertificateGroupManage = iMarketingCertificateGroupManageService.getById(id);
                if (iMarketingCertificateGroupRecordService.count(new LambdaQueryWrapper<MarketingCertificateGroupRecord>()
                        .eq(MarketingCertificateGroupRecord::getDelFlag,"0")
                        .eq(MarketingCertificateGroupRecord::getMemberListId,memberId)
                        .eq(MarketingCertificateGroupRecord::getMarketingCertificateGroupManageId,marketingCertificateGroupManage.getId())
                )>0){
                    return result.error500("自己跟自己不能拼");
                }
                MarketingCertificateGroupList marketingCertificateGroupList = iMarketingCertificateGroupListService.getById(marketingCertificateGroupManage.getMarketingCertificateGroupListId());
                //获取兑换券
                MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(marketingCertificateGroupList.getMarketingCertificateId());

                if (marketingCertificate.getTotal().subtract(marketingCertificate.getReleasedQuantity()).doubleValue() < quantity) {
                    return result.error500("库存不足!请重新购买");
                }

                //计算出购买总价
                totalPrice = marketingCertificateGroupList.getActivityPrice().multiply(new BigDecimal(quantity));

                //收银台
                result.setResult(totalPayUtils.payCertificate(request,marketingCertificate.getId(),new BigDecimal(quantity),memberId,longitude,latitude,totalPrice,sysUserId,softModel,"3",id));
            }
        }
        result.success("生成兑换券支付成功");
        return result;
    }

}
