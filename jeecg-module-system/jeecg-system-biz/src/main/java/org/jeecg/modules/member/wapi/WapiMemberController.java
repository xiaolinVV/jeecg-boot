package org.jeecg.modules.member.wapi;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskService;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 会员接口
 *
 * 张靠勤  2021-4-7
 */

@RequestMapping("wapi/member")
@Controller
public class WapiMemberController {


    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberBankCardService iMemberBankCardService;

    @Autowired
    private IMarketingIntegralTaskService iMarketingIntegralTaskService;


    /**
     * 根据手机号获取会员信息
     *
     * @param phone
     * @return
     */
    @RequestMapping("getMemberByPhone")
    @ResponseBody
    public Result<?> getMemberByPhone(String phone){
        Map<String,Object> resultMap=Maps.newHashMap();
        if(StringUtils.isBlank(phone)){
            return Result.error("手机号不能为空");
        }
        MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                .eq(MemberList::getPhone,phone)
                .orderByDesc(MemberList::getCreateTime)
                .last("limit 1"));
        if(memberList==null){
            return Result.error("会员不存在");
        }
        resultMap.put("id",memberList.getId());
        return Result.ok(resultMap);
    }

    /**
     * 根据推广码获取会员手机号
     *
     * @param promotionCode
     * @return
     */
    @RequestMapping("getMemberByPromotionCode")
    @ResponseBody
    public Result<?> getMemberByPromotionCode(String promotionCode){
        Map<String,Object> resultMap=Maps.newHashMap();
        if(StringUtils.isBlank(promotionCode)){
            return Result.error("推广码不能为空");
        }
        MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPromotionCode,promotionCode));
        if(memberList==null){
            return Result.error("会员不存在");
        }
        resultMap.put("phone",memberList.getPhone());
        return Result.ok(resultMap);
    }



    /**
     * 会员手机和验证码登录
     *
     * 张靠勤  2021-4-7
     * @param phone
     * @return
     */
    @RequestMapping("loginByPhone")
    @ResponseBody
    public Result<Map<String,String>> loginByPhone(String phone){
        Result<Map<String,String>> result=new Result<>();
        Map<String,String> stringMap= Maps.newHashMap();

        //参数验证
        if(StringUtils.isBlank(phone)){
            result.error500("手机号码不能为空！！！");
            return result;
        }

        //判断用户是否存在
        String shareTimes = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "GET_THE_NUMBER");
        MemberList memberList=null;
        long count=iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPhone,phone));
        if(count==0){
            //会员不存在
            memberList=new MemberList();
            memberList.setDelFlag("0");
            memberList.setPhone(phone);
            memberList.setMemberType("0");
            memberList.setIsOpenStore("0");
            memberList.setStatus("1");
            memberList.setNickName(phone);
            memberList.setShareTimes(new BigDecimal(shareTimes));
            //保存用户信息
            iMemberListService.saveOrUpdate(memberList);
            //注册积分奖励
            iMarketingIntegralTaskService.registerSuccess(memberList.getId());
        }else{
            //会员存在
            memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                    .eq(MemberList::getPhone,phone)
                    .orderByDesc(MemberList::getCreateTime)
                    .last("limit 1"));
        }
        stringMap.put("memberId",memberList.getId());
        result.setResult(stringMap);
        return result;
    }


    /**
     * 支付密码验证接口
     *
     * 张靠勤  2021-4-8
     *
     * @param srcTransactionPassword
     * @param memberId
     * @return
     */
    @RequestMapping("srcTransactionPasswordValid")
    @ResponseBody
    public Result<?> srcTransactionPasswordValid(String srcTransactionPassword
            ,String memberId){
        MemberList memberList=iMemberListService.getById(memberId);
        if(StringUtils.isNotBlank(memberList.getTransactionPassword())&&StringUtils.isBlank(srcTransactionPassword)){
            return Result.error("原交易密码不能为空");
        }
        try {
            if(StringUtils.isNotBlank(memberList.getTransactionPassword())&&
                    (!PasswordUtil.decrypt(memberList.getTransactionPassword(),srcTransactionPassword,PasswordUtil.Salt).equals(srcTransactionPassword))){
                return Result.error("原交易密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("交易密码错误");
        }
        return Result.ok("支付密码验证成功");
    }


    /**
     * 设置用户交易密码
     *
     * 张靠勤    2021-4-8
     *
     * @param transactionPassword
     * @param memberId
     * @return
     */
    @RequestMapping("settingTransactionPassword")
    @ResponseBody
    public Result<?> settingTransactionPassword(String transactionPassword
            ,String memberId){
        //参数验证
        if(StringUtils.isBlank(transactionPassword)){
            return Result.error("交易密码参数不能为空");
        }
        MemberList memberList=iMemberListService.getById(memberId);
        memberList.setTransactionPassword(PasswordUtil.encrypt(transactionPassword,transactionPassword,PasswordUtil.Salt));
        iMemberListService.saveOrUpdate(memberList);
        return Result.ok("交易密码设置成功");
    }



    /**
     * 获取会员信息
     *
     * 张靠勤  2021-4-8
     *
     * @param
     * @return
     */
    @RequestMapping("getMemberInfo")
    @ResponseBody
    public Result<Map<String,Object>> getMemberInfo(String memberId){
        Result<Map<String,Object>> result =new Result<>();

        Map<String,Object> memberObjectMap  = Maps.newHashMap();

        //查询个人中心信息
        MemberList memberList=iMemberListService.getById(memberId);
        memberObjectMap.put("id",memberId);
        memberObjectMap.put("memberType",memberList.getMemberType());
        memberObjectMap.put("welfarePayments",memberList.getWelfarePayments());
        memberObjectMap.put("haveWithdrawal",memberList.getHaveWithdrawal());
        memberObjectMap.put("balance",memberList.getBalance());
        memberObjectMap.put("shareTimes",memberList.getShareTimes());
        memberObjectMap.put("isOpenStore",memberList.getIsOpenStore());
        memberObjectMap.put("phone",memberList.getPhone());
        memberObjectMap.put("accountFrozen",memberList.getAccountFrozen());
        memberObjectMap.put("unusableFrozen",memberList.getUnusableFrozen());
        memberObjectMap.put("welfarePaymentsFrozen",memberList.getWelfarePaymentsFrozen());
        memberObjectMap.put("welfarePaymentsUnusable",memberList.getWelfarePaymentsUnusable());
        memberObjectMap.put("nickName",memberList.getNickName());
        memberObjectMap.put("headPortrait",memberList.getHeadPortrait());
        memberObjectMap.put("mail",memberList.getMail());

        //是否设置交易密码
        if(StringUtils.isBlank(memberList.getTransactionPassword())){
            memberObjectMap.put("transactionPassword","0");
        }else{
            memberObjectMap.put("transactionPassword","1");
        }
        /**
         * 会员银行卡
         */
        MemberBankCard memberBankCard = iMemberBankCardService.getOne(new LambdaQueryWrapper<MemberBankCard>().
                eq(MemberBankCard::getMemberListId, memberId)
                .eq(MemberBankCard::getCarType, "0")
                .orderByDesc(MemberBankCard::getCreateTime)
                .last("limit 1"));
        if (oConvertUtils.isEmpty(memberBankCard)){
            memberObjectMap.put("memberBankCard","");
        }else {
            memberObjectMap.put("memberBankCard",memberBankCard.getBankName()+new StringBuilder(new StringBuilder(memberBankCard.getBankCard())
                    .reverse().toString().substring(0,4)).reverse().toString());
            memberObjectMap.put("memberBankCardInfo",memberBankCard);
        }
        result.setResult(memberObjectMap);
        result.success("获取会员信息成功");
        return result;
    }
    @RequestMapping("pageList")
    @ResponseBody
    public Result<?> pageList(MemberList memberList,
                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                              HttpServletRequest req){
        QueryWrapper<MemberList> queryWrapper = QueryGenerator.initQueryWrapper(memberList, req.getParameterMap());
        Page<MemberList> page = new Page<MemberList>(pageNo, pageSize);
        return Result.ok(iMemberListService.page(page,queryWrapper));
    }

    /**
     * 增加会员金额
     *
     * @param memberId
     * @param balance
     * @param orderNo
     * @param payType
     * @return
     */
    @RequestMapping("addBlance")
    @ResponseBody
    public Result<?> addBlance(String memberId,BigDecimal balance,String orderNo,String payType){
        iMemberListService.addBlance(memberId,balance,orderNo,payType);
        return Result.ok("增加会员余额成功");
    }

    /**
     * 会员邮箱设置
     *
     * @param memberId
     * @param mail
     * @return
     */
    @RequestMapping("memberMailSetting")
    @ResponseBody
    public Result<?> memberMailSetting(String memberId,String mail){
        //参数校验
        if(StringUtils.isBlank(memberId)){
            return Result.error("会员id不能为空");
        }
        if(StringUtils.isBlank(mail)){
            return Result.error("邮箱不能为空");
        }
        if(iMemberListService.update(new MemberList().setMail(mail),new LambdaQueryWrapper<MemberList>().eq(MemberList::getId,memberId))){
            return Result.ok("会员设置邮箱成功");
        }else{
            return Result.error("邮箱设置失败");
        }
    }
}
