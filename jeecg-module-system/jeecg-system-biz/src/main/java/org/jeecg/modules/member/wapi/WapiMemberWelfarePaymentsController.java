package org.jeecg.modules.member.wapi;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.member.vo.MemberWelfarePaymentsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 积分操作接口
 */
@Controller
@RequestMapping("wapi/memberWelfarePayments")
public class WapiMemberWelfarePaymentsController {

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMemberListService iMemberListService;

    @RequestMapping("pageList")
    @ResponseBody
    public Result<?> pageList(MemberWelfarePaymentsVO memberWelfarePaymentsVO,
                              @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        Result<IPage<MemberWelfarePaymentsVO>> result = new Result<>();
        Page<MemberWelfarePaymentsVO> page = new Page<>(pageNo, pageSize);
        IPage<MemberWelfarePaymentsVO> memberWelfarePayments = iMemberWelfarePaymentsService.findMemberWelfarePayments(page, memberWelfarePaymentsVO);
        result.setSuccess(true);
        result.setResult(memberWelfarePayments);
        return result;
    }
    /**
     * 转入积分
     *
     * @param membeId
     * @param welfarePayments
     * @return
     */
    @RequestMapping("addWelfarePayments")
    @ResponseBody
    public Result<?> addWelfarePayments(String membeId, BigDecimal  welfarePayments,String tradeType,String remark){
        iMemberWelfarePaymentsService.addWelfarePayments(membeId,welfarePayments,tradeType,membeId,remark);
        return Result.ok("转入成功");
    }



    /**
     * 转出积分
     *
     * @param membeId
     * @param welfarePayments
     * @return
     */
    @RequestMapping("subtractWelfarePayments")
    @ResponseBody
    public Result<?> subtractWelfarePayments(String membeId, BigDecimal  welfarePayments,String tradeType,String remark){
        iMemberWelfarePaymentsService.subtractWelfarePayments(membeId,welfarePayments,tradeType,membeId,remark);
        return Result.ok("转出成功");
    }


    /**
     * 转出积分
     *
     * @param phone
     * @param welfarePayments
     * @return
     */
    @RequestMapping("subtractWelfarePaymentsByPhone")
    @ResponseBody
    public Result<?> subtractWelfarePaymentsByPhone(String phone, BigDecimal  welfarePayments,String tradeType,String remark){
        if(StringUtils.isBlank(phone)){
            return Result.error("手机号不能为空");
        }
        MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPhone,phone).orderByDesc(MemberList::getCreateTime).last("limit 1"));
        iMemberWelfarePaymentsService.subtractWelfarePayments(memberList.getId(),welfarePayments,tradeType,memberList.getId(),remark);
        return Result.ok("转出成功");
    }
}
