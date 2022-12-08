package org.jeecg.modules.marketing.api;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("after/marketingWelfarePayments")
@Controller
@Slf4j
public class AfterMarketingWelfarePaymentsController {
    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    private IMarketingWelfarePaymentsService iMarketingWelfarePaymentsService;
    @Autowired
    private IMemberListService iMemberListService;

    public Result<String> presentedWelfarePayments(String phone,
                                                   String amont,
                                                   String weExplain,
                                                   HttpServletRequest request){
        return null;
    }
}
