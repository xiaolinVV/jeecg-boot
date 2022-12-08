package org.jeecg.modules.pay.utils;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Log
public class PayLogUtils {


    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;


    public Map<String,Object> toCashierDesk(BigDecimal allTotalPrice,String softModel,String payLogId,String payModelSysDict,String memberId){

        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("allTotalPrice", allTotalPrice);

        MemberList memberList=iMemberListService.getById(memberId);

        BigDecimal integralValue = iMarketingWelfarePaymentsSettingService.getIntegralValue();
        resultMap.put("integralValue", integralValue);


        resultMap.put("welfarePayments", memberList.getWelfarePayments());
        resultMap.put("balance",memberList.getBalance());

        resultMap.put("memberListId",memberId);
        
        String payModel = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", payModelSysDict);

        resultMap.put("payModel", payModel);
        //小程序
        if (softModel.equals("0")) {
            List<String> payModels = Lists.newArrayList();
            Arrays.asList(StringUtils.split(payModel, ","))
                    .stream().filter(model -> !model.equals("1"))
                    .forEach(model -> {
                        payModels.add(model);
                    });
            payModel = StringUtils.join(payModels, ",");

            log.info("收银台支付方式payModel：" + payModel);

            resultMap.put("payModel", payModel);
        }
        //app是否禁用小程序
        if(softModel.equals("1")||softModel.equals("2")) {
            String closeWxPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "close_wx_pay");
            if (closeWxPay.equals("1")) {
                List<String> payModels = Lists.newArrayList();
                Arrays.asList(StringUtils.split(payModel, ","))
                        .stream().filter(model -> !model.equals("0"))
                        .forEach(model -> {
                            payModels.add(model);
                        });
                payModel = StringUtils.join(payModels, ",");

                log.info("收银台支付方式payModel：" + payModel);

                resultMap.put("payModel", payModel);
            }
        }
        resultMap.put("payLogId",payLogId);
        return resultMap;
    }

}
