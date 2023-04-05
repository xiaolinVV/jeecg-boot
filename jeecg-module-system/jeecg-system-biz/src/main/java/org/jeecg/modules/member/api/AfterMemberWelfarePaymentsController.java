package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.MemberGiveWelfarePayments;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberGiveWelfarePaymentsService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.member.utils.QrCodeUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("after/memberWelfarePayments")
@Controller
public class AfterMemberWelfarePaymentsController {

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMemberGiveWelfarePaymentsService iMemberGiveWelfarePaymentsService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private QrCodeUtils qrCodeUtils;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;


    /**
     *
     * @param memberId
     * @param memberWelfarePayments
     * @return
     */
    @RequestMapping("toBalance")
    @ResponseBody
    @Transactional
    public Result<?> toBalance(@RequestAttribute("memberId") String memberId,BigDecimal memberWelfarePayments){
        if(memberWelfarePayments==null||memberWelfarePayments.doubleValue()==0){
            return Result.error("积分数量不能为0或者为空");
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getWelfarePayments().doubleValue()<memberWelfarePayments.doubleValue()){
            return Result.error("兑换积分超出会员的最大积分");
        }
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        if(iMemberWelfarePaymentsService.subtractWelfarePayments(memberId,memberWelfarePayments,"51",memberId,"")){
            if(iMemberListService.addBlance(memberId,memberWelfarePayments.multiply(integralValue).setScale(2,RoundingMode.DOWN),memberId,"48")){
                return Result.ok("兑换成功");
            }else{
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return Result.error("未知错误");
    }

    /**
     * 查询用户福利金列表
     *
     * @param pattern -1:全部；0：支出；1：收入
     * @param request
     * @return
     */
    @RequestMapping("findMemberWelfarePaymentsByMemberId")
    @ResponseBody
    public Result<IPage<Map<String, Object>>> findMemberWelfarePaymentsByMemberId(Integer pattern,
                                                                                  HttpServletRequest request,
                                                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        String memberId = request.getAttribute("memberId").toString();
        Result<IPage<Map<String, Object>>> result = new Result<>();

        //组织查询参数
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        Map<String, Object> paramObjectMap = Maps.newHashMap();
        paramObjectMap.put("memberId", memberId);
        paramObjectMap.put("pattern", pattern);

        result.setResult(iMemberWelfarePaymentsService.findMemberWelfarePaymentsByMemberId(page, paramObjectMap));

        result.success("查询平台福利金列表");
        return result;
    }

    /**
     * 可用福利金明细
     *
     * @param pattern
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMemberWelfarePaymentsPageByMemberId")
    @ResponseBody
    public Result<IPage<Map<String, Object>>> findMemberWelfarePaymentsPageByMemberId(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                      @RequestParam(defaultValue = "-1",required = false)Integer pattern,
                                                                                      HttpServletRequest request
                                                                                      ) {
        String memberId = request.getAttribute("memberId").toString();
        Result<IPage<Map<String, Object>>> result = new Result<>();
        //组织查询参数
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        Map<String, Object> paramObjectMap = Maps.newHashMap();
        paramObjectMap.put("memberId", memberId);
        paramObjectMap.put("pattern", pattern);
        IPage<Map<String, Object>> memberWelfarePaymentsPageByMemberId = iMemberWelfarePaymentsService.findMemberWelfarePaymentsPageByMemberId(page, paramObjectMap);
        result.setResult(memberWelfarePaymentsPageByMemberId);
        result.success("返回会员可用福利金明细");
        return result;
    }

    /**
     * 待结算福利金明细
     * @param pattern
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findfrozenAandUnusableById")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findfrozenAandUnusableById(String pattern,
                                                                        HttpServletRequest request,
                                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        String memberId = request.getAttribute("memberId").toString();
        Result<IPage<Map<String, Object>>> result = new Result<>();
        //组织查询参数
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        Map<String, Object> paramObjectMap = Maps.newHashMap();
        paramObjectMap.put("memberId", memberId);
        paramObjectMap.put("pattern", pattern);
        IPage<Map<String, Object>> findfrozenAandUnusableById = iMemberWelfarePaymentsService.findfrozenAandUnusableById(page, paramObjectMap);
        result.success("返回明细");
        result.setResult(findfrozenAandUnusableById);
        return result;
    }

    /**
     * 赠送福利金
     * @param id    会员id可以为空，没有id,就为通过手机号赠送福利金,有id,就为扫码赠送福利金
     * @param phone 会员手机号
     * @param amont 赠送福利金
     * @param giveExplain 赠送理由
     * @param request
     * @return
     */
    @RequestMapping("PresentedMemberWelfarePayments")
    @ResponseBody
    @Transactional
    public Result<String> PresentedMemberWelfarePayments(String id,
                                                         String phone,
                                                         String amont,
                                                         String giveExplain,
                                                         HttpServletRequest request) {
        Result<String> result = new Result<>();
        String memberId = request.getAttribute("memberId").toString();
        if (StringUtils.isBlank(phone)){
            return result.error500("请输入会员手机号");
        }
        if (StringUtils.isBlank(amont)){
            return result.error500("请输入赠送福利金!");
        }
        if (new BigDecimal(amont).doubleValue()<0.01){
            return result.error500("赠送福利金不能小于0.01");
        }
        //查出赠送者
        MemberList benefactor = iMemberListService.getById(memberId);

        //判断赠送人福利金
        if (benefactor.getWelfarePayments().doubleValue() < new BigDecimal(amont).doubleValue()) {
            return result.error500("福利金不足,无法赠送!");
        }
        //没有id,就为通过手机号赠送福利金,有id,就为扫码赠送福利金
        if (StringUtils.isBlank(id)) {
            LambdaQueryWrapper<MemberList> memberListLambdaQueryWrapper = new LambdaQueryWrapper<MemberList>()
                    .eq(MemberList::getPhone, phone)
                    .eq(MemberList::getStatus, "1")
                    .orderByDesc(MemberList::getCreateTime)
                    .last("limit 1");
            if (iMemberListService.count(memberListLambdaQueryWrapper) > 0) {
                MemberList memberList = iMemberListService.getOne(memberListLambdaQueryWrapper);
                if (memberList.getId().equals(benefactor.getId())){
                    return result.error500("福利金不能赠送自己!");
                }

                String welfareGivenControl= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","welfare_given_control");
                if(StringUtils.isNotBlank(welfareGivenControl)&&welfareGivenControl.equals("1")){
                    MemberGiveWelfarePayments memberGiveWelfarePayments=iMemberGiveWelfarePaymentsService.getOne(new LambdaQueryWrapper<MemberGiveWelfarePayments>()
                            .eq(MemberGiveWelfarePayments::getMemberListId,memberList.getId())
                            .orderByDesc(MemberGiveWelfarePayments::getCreateTime)
                            .last("limit 1"));
                    if(memberGiveWelfarePayments==null){
                        return result.error500("获赠者的获赠金额不足!");
                    }else{
                        if(memberGiveWelfarePayments.getGiveWelfarePayments().doubleValue()<new BigDecimal(amont).doubleValue()){
                            return result.error500("获赠者的获赠金额不足!");
                        }else{
                            if(!iMemberGiveWelfarePaymentsService.subtract(memberList.getId(),new BigDecimal(amont),"3",memberList.getId())){
                                //手动强制回滚事务，这里一定要第一时间处理
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            }
                        }
                    }
                }
                iMemberWelfarePaymentsService.PresentedMemberWelfarePayments(memberList, amont, giveExplain, benefactor);
            } else {
                return result.error500("该会员状态异常,请联系管理员!");
            }
        } else {
            MemberList memberList = iMemberListService.getById(id);
            if (memberList.getId().equals(benefactor.getId())){
                return result.error500("福利金不能赠送自己!");
            }
            String welfareGivenControl= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","welfare_given_control");
            if(StringUtils.isNotBlank(welfareGivenControl)&&welfareGivenControl.equals("1")){
                MemberGiveWelfarePayments memberGiveWelfarePayments=iMemberGiveWelfarePaymentsService.getOne(new LambdaQueryWrapper<MemberGiveWelfarePayments>()
                        .eq(MemberGiveWelfarePayments::getMemberListId,memberList.getId())
                        .orderByDesc(MemberGiveWelfarePayments::getCreateTime)
                        .last("limit 1"));
                if(memberGiveWelfarePayments==null){
                    return result.error500("获赠者的获赠金额不足!");
                }else{
                    if(memberGiveWelfarePayments.getGiveWelfarePayments().doubleValue()<new BigDecimal(amont).doubleValue()){
                        return result.error500("获赠者的获赠金额不足!");
                    }else{
                        if(!iMemberGiveWelfarePaymentsService.subtract(memberList.getId(),new BigDecimal(amont),"3",memberList.getId())){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }
                }
            }
            iMemberWelfarePaymentsService.PresentedMemberWelfarePayments(memberList, amont, giveExplain, benefactor);
        }
        result.success("赠送成功");
        return result;
    }

    /**
     * 返回会员福利金付款码
     * @param request
     * @return
     */
    @RequestMapping("getMemberQrWelfareCode")
    @ResponseBody
    public Result<String> getMemberQrWelfareCode(HttpServletRequest request){
        Result<String> result = new Result<>();
        String memberId = request.getAttribute("memberId").toString();
        String uuId = UUID.randomUUID().toString().replace("-", "");
        redisUtil.set(uuId,memberId,600);
        result.setResult(qrCodeUtils.getMemberQrWelfareCode(uuId));
        result.success("返回会员福利金付款码");
        return result;
    }

    /**
     * 根据手机号查询会员
     * @param phone
     * @param request
     * @return
     */
    @RequestMapping("likeMemberByPhone")
    @ResponseBody
    public Result<List<Map<String,Object>>> likeMemberByPhone(String phone,
                                                              HttpServletRequest request){
        Result<List<Map<String, Object>>> result = new Result<>();
        List<Map<String, Object>> maps = iMemberListService.likeMemberByPhone(phone);
        maps.forEach(m->{
            MemberGiveWelfarePayments memberGiveWelfarePayments=iMemberGiveWelfarePaymentsService.getOne(new LambdaQueryWrapper<MemberGiveWelfarePayments>()
                    .eq(MemberGiveWelfarePayments::getMemberListId,m.get("id"))
                    .orderByDesc(MemberGiveWelfarePayments::getCreateTime)
                    .last("limit 1"));
            if(memberGiveWelfarePayments==null){
                m.put("giveWelfarePayments",0);
            }else{
                m.put("giveWelfarePayments",memberGiveWelfarePayments.getGiveWelfarePayments());
            }
        });
        result.setResult(maps);
        result.success("通过手机获取会员信息");
        return result;
    }
}
