package org.jeecg.modules.member.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.config.jwt.service.TokenManager;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskService;
import org.jeecg.modules.marketing.service.IMarketingLeagueMemberService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.utils.MemberUtils;
import org.jeecg.modules.member.utils.PromotionCodeUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 会员api接口
 */
@RequestMapping("front/member")
@Controller
public class FrontMemberController {


    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private IMarketingIntegralTaskService iMarketingIntegralTaskService;

    @Autowired
    private PromotionCodeUtils promotionCodeUtils;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private MemberUtils memberUtils;

    @Autowired
    private IMarketingLeagueMemberService iMarketingLeagueMemberService;


    /**
     * 查询会员手机号是否存在
     *
     * @param phone
     * @return
     */
    @RequestMapping("getMemberByPhone")
    @ResponseBody
    public Result<String> getMemberByPhone(String phone){
        Result<String> result=new Result<>();

        //参数验证
        if(StringUtils.isBlank(phone)){
            result.error500("手机号码不能为空");
            return result;
        }
        long count=iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPhone,phone));

        if(count==0){
            result.error500("会员手机号不存在");
            return result;
        }

        result.success("会员手机号已存在");
        return result;
    }


    /**
     * 会员手机和验证码登录
     * @param phone
     * @param captcha
     * @param sysUserId
     * @param tMemberId
     * @return
     */
    @RequestMapping("loginByPhone")
    @ResponseBody
    @Transactional
    public Result<Map<String,String>> loginByPhone(String phone,
                                                   String captcha,
                                                   @RequestHeader(defaultValue = "",required = false) String sysUserId,
                                                   @RequestHeader(name = "softModel",defaultValue = "4",required = false) String softModel,
                                                   @RequestHeader(defaultValue = "") String tMemberId,
                                                   @RequestParam(name ="promotionCode",defaultValue = "",required = false) String promotionCode){
        Result<Map<String,String>> result=new Result<>();
        Map<String,String> stringMap= Maps.newHashMap();

        //参数验证
        if(StringUtils.isBlank(phone)){
            result.error500("手机号码不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(captcha)){
            result.error500("验证码不能为空！！！");
            return result;
        }

        //核对验证码
        Object captchaContent=redisUtil.get(phone);
        if(captchaContent==null||!captchaContent.toString().equals(captcha)){
            result.error500("验证码不正确！！！");
            return result;
        }

        //判断用户是否存在
        String shareTimes = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "GET_THE_NUMBER");
        MemberList memberList=null;
        long count=iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPhone,phone));
        if(count==0){


            //限制注册时间
            if(memberUtils.registrationDate()){
                result.error500("当前注册人数较多，请稍后再试...");
                return result;
            }

            //限制注册数量
            if(memberUtils.registerOnline()){
                result.error500("当前注册人数较多，请稍后再试...");
                return result;
            }

            if(StringUtils.isNotBlank(promotionCode)){
                promotionCode=StringUtils.trim(promotionCode);
                MemberList memberListPro=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                        .eq(MemberList::getPromotionCode,promotionCode)
                        .orderByAsc(MemberList::getCreateTime)
                        .last("limit 1"));
                if(memberListPro!=null){
                    tMemberId=memberListPro.getId();
                }
            }

            String promoCode  = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "promo_code");
            if(promoCode.equals("1")){
                if(StringUtils.isBlank(tMemberId)){
                    result.error500("您还没有推荐人，请先找个推荐人！！！");
                    return result;
                }
            }

            //会员不存在
            memberList=new MemberList();
            memberList.setDelFlag("0");
            memberList.setPhone(phone);
            memberList.setMemberType("0");
            memberList.setIsOpenStore("0");
            memberList.setStatus("1");
            memberList.setNickName(phone);
            memberList.setAccountNumber(phone);
            memberList.setPassword(PasswordUtil.encrypt(phone,phone,PasswordUtil.Salt));
            //添加会员分享关系
            memberList.setShareTimes(new BigDecimal(shareTimes));
            //保存用户信息
            iMemberListService.saveOrUpdate(memberList);
            //设置登录注册的时候分销关系和团队关系
            iMemberListService.setLoginRegister(memberList,sysUserId,tMemberId);
            //注册积分奖励
            iMarketingIntegralTaskService.registerSuccess(memberList.getId());
            //分销升级
            iMemberDistributionLevelService.upgrade(memberList.getId());
            //加盟专区关系
            iMarketingLeagueMemberService.ordinary(memberList.getId(),tMemberId);
        }else{
            //会员存在
            memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                    .eq(MemberList::getPhone,phone)
                    .orderByDesc(MemberList::getCreateTime)
                    .last("limit 1"));
        }

        //生成token信息
        stringMap.put("X-AUTH-TOKEN", tokenManager.createToken(memberList.getId(),softModel));

        //添加分享二维码信息
        iMemberListService.addShareQr(memberList,sysUserId);

        result.setResult(stringMap);
        return result;
    }



    /**
     * 会员账号和密码登录
     *
     * @param accountNumber
     * @param password
     * @return
     */
    @RequestMapping("loginByAccount")
    @ResponseBody
    public Result<?> loginByAccount(String accountNumber,String password,
                                                   @RequestHeader("softModel") String softModel){
        //参数校验
        if(StringUtils.isBlank(accountNumber)){
            return Result.error("账号不能为空");
        }
        if(StringUtils.isBlank(password)){
            return Result.error("密码不能为空");
        }
        Map<String,String> stringMap= Maps.newHashMap();
        accountNumber=StringUtils.trim(accountNumber);
        password=StringUtils.trim(password);
        MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getAccountNumber,accountNumber).last("limit 1"));
        if(memberList==null){
            memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPhone,accountNumber).last("limit 1"));
            if(memberList==null) {
                return Result.error("会员不存在");
            }
        }
        String passwd=null;
        if(StringUtils.isNotBlank(memberList.getPassword())) {
            passwd = PasswordUtil.decrypt(memberList.getPassword(), password, PasswordUtil.Salt);
        }
        if(StringUtils.isBlank(passwd) ||
                (!PasswordUtil.decrypt(memberList.getPassword(),password,PasswordUtil.Salt).equals(password))){
            return Result.error("密码错误");
        }
        //生成token信息
        stringMap.put("X-AUTH-TOKEN", tokenManager.createToken(memberList.getId(),softModel));

        return Result.ok(stringMap);
    }


    /**
     * 忘记密码
     *
     * @param accountNumber
     * @param password
     * @param phone
     * @param captcha
     * @return
     */
    @RequestMapping("updatePassword")
    @ResponseBody
    public Result<?> updatePassword(String accountNumber,String password,
                                    String phone,String captcha){
        //参数校验
        if(StringUtils.isBlank(accountNumber)){
            return Result.error("账号不能为空");
        }
        if(StringUtils.isBlank(password)){
            return Result.error("密码不能为空");
        }
        if(StringUtils.isBlank(phone)){
            return Result.error("手机号不能为空");
        }
        if(StringUtils.isBlank(captcha)){
            return Result.error("验证码不能为空");
        }
        accountNumber=StringUtils.trim(accountNumber);
        password=StringUtils.trim(password);
        //核对验证码
        Object captchaContent=redisUtil.get(phone);
        if(captchaContent==null||!captchaContent.toString().equals(captcha)){
            return Result.error("验证码不正确");
        }
        MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getAccountNumber,accountNumber).last("limit 1"));
        if(memberList==null){
            memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPhone,accountNumber).last("limit 1"));
            if(memberList==null) {
                return Result.error("会员不存在");
            }
        }
        if(!memberList.getPhone().equals(phone)){
            return Result.error("手机号和会员绑定的手机号不同");
        }

        memberList.setPassword(PasswordUtil.encrypt(password,password,PasswordUtil.Salt));
        //保存用户信息
        iMemberListService.saveOrUpdate(memberList);
        return Result.ok("密码修改成功");
    }




    /**
     * 会员注册
     *
     * @param accountNumber
     * @param password
     * @param phone
     * @param captcha
     * @param promotionCode
     * @return
     */
    @RequestMapping("register")
    @ResponseBody
    public Result<?> register(String accountNumber,String password,String phone,String captcha,String promotionCode){
        //参数校验
        if(StringUtils.isBlank(accountNumber)){
            return Result.error("账号不能为空");
        }
        if(StringUtils.isBlank(password)){
            return Result.error("密码不能为空");
        }
        if(StringUtils.isBlank(phone)){
            return Result.error("手机号不能为空");
        }
        if(StringUtils.isBlank(captcha)){
            return Result.error("验证码不能为空");
        }


        //限制注册时间
        if(memberUtils.registrationDate()){
            return Result.error("当前注册人数较多，请稍后再试...");
        }

        //限制注册数量
        if(memberUtils.registerOnline()){
            return Result.error("当前注册人数较多，请稍后再试..");
        }

        accountNumber=StringUtils.trim(accountNumber);
        password=StringUtils.trim(password);
        captcha=StringUtils.trim(captcha);
        promotionCode=StringUtils.trim(promotionCode);
        String promoCode  = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "promo_code");
        if(promoCode.equals("1")){
            if(StringUtils.isBlank(promotionCode)){
                return Result.error("您还没有推荐人，请先找个推荐人！！！");
            }
        }
        //账号必须唯一
        long count=iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getAccountNumber,accountNumber));
        if(count!=0){
            return Result.error("账号已存在，请重新设定");
        }
        long phoneCount=iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPhone,phone));
        if(phoneCount!=0){
            return Result.error("手机号已存在，请重新设定手机号");
        }
        //核对验证码
        Object captchaContent=redisUtil.get(phone);
        if(captchaContent==null||!captchaContent.toString().equals(captcha)){
            return Result.error("验证码不正确");
        }
        String shareTimes = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "GET_THE_NUMBER");

        String tMemberId="";
        if(StringUtils.isNotBlank(promotionCode)){
            MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                    .eq(MemberList::getPromotionCode,promotionCode)
                    .orderByDesc(MemberList::getCreateTime)
                    .last("limit 1"));
            if(memberList!=null){
                tMemberId=memberList.getId();
            }
        }
        if(promoCode.equals("1")){
            if(StringUtils.isBlank(tMemberId)){
                return Result.error("您的推荐人没找到，请先找个推荐人！！！");
            }
        }
        //会员不存在
        MemberList memberList=new MemberList();
        memberList.setPhone(phone);
        memberList.setMemberType("0");
        memberList.setIsOpenStore("0");
        memberList.setStatus("1");
        memberList.setNickName(phone);
        memberList.setShareTimes(new BigDecimal(shareTimes));
        memberList.setAccountNumber(accountNumber);
        memberList.setPassword(PasswordUtil.encrypt(password,password,PasswordUtil.Salt));
        memberList.setPromotionCode(promotionCodeUtils.getCode());
        //设置登录注册的时候分销关系和团队关系
        iMemberListService.setLoginRegister(memberList,"",tMemberId);
        //保存用户信息
        iMemberListService.saveOrUpdate(memberList);
        //注册积分奖励
        iMarketingIntegralTaskService.registerSuccess(memberList.getId());
        //分销升级
        iMemberDistributionLevelService.upgrade(memberList.getId());
        //加盟专区关系
        iMarketingLeagueMemberService.ordinary(memberList.getId(),tMemberId);
        return Result.ok("注册成功");
    }

}
