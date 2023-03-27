package org.jeecg.modules.weixin.api;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.encryption.AesCbcUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.util.weixin.WeixinUtils;
import org.jeecg.config.jwt.def.JwtConstants;
import org.jeecg.config.jwt.model.TokenModel;
import org.jeecg.config.jwt.service.TokenManager;
import org.jeecg.config.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.service.IMarketingDistributionSettingService;
import org.jeecg.modules.marketing.service.IMarketingIntegralTaskService;
import org.jeecg.modules.marketing.service.IMarketingLeagueMemberService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.utils.MemberUtils;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信登陆接口
 */
@RequestMapping("front/weixin")
@Controller
@Slf4j
public class FrontWeixinController {

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private WeixinQRUtils weixinQRUtils;

    @Autowired
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;

    @Autowired
    private IMarketingIntegralTaskService iMarketingIntegralTaskService;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private MemberUtils memberUtils;

    @Autowired
    private IMarketingLeagueMemberService iMarketingLeagueMemberService;

    @Autowired
    private IMemberListService memberListService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 获取请求token
     * @param phone 刷新用户token
     * @return
     */
    @RequestMapping("refreshToken")
    @ResponseBody
    public Result<?> refreshToken(String phone,HttpServletRequest request) {
        String softModel = request.getHeader("softModel");
        LambdaQueryWrapper<MemberList> listLambdaQueryWrapper = new LambdaQueryWrapper<>();
        listLambdaQueryWrapper.eq(MemberList::getPhone,phone).eq(MemberList::getStatus,"1");
        MemberList memberList = memberListService.getOne(listLambdaQueryWrapper, false);
        if (memberList == null) {
            throw new JeecgBootException("手机号不存在,请先注册~");
        }
        String token = (String) redisTemplate.boundValueOps(softModel+"member="+memberList.getId()).get();
        if (StrUtil.isNotBlank(token)) {
            redisTemplate.boundValueOps(softModel+"member="+memberList.getId()).expire(JwtConstants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        }else {
            token = tokenManager.createToken(memberList.getId(),softModel);
        }
        return Result.ok(token);
    }


    /**
     * 获取公众号openid
     * @param gzCode
     * @return
     */
    @RequestMapping("getGzOpenId")
    @ResponseBody
    public Result<?> getGzOpenId(String gzCode) {
        if(StringUtils.isNotBlank(gzCode)){
            String wechatDeveloperID= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","wechatDeveloperID");
            String wechatDeveloperSecretKey= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","wechatDeveloperSecretKey");
            return Result.ok(WeixinUtils.getAccessToken(wechatDeveloperID,wechatDeveloperSecretKey,gzCode));
        }else{
            return Result.error("code不存在");
        }
    }


    /**
     * 验证微信公众号信息
     *
     * @param request
     * @return
     */
    @RequestMapping("yanZhengWeixin")
    @ResponseBody
    public Object yanZhengWeixin(HttpServletRequest request){
        log.info("signature：",request.getParameter("signature"));
        log.info("timestamp：",request.getParameter("timestamp"));
        log.info("nonce：",request.getParameter("nonce"));
        log.info("echostr：",request.getParameter("echostr"));
        return request.getParameter("echostr");
    }


    /**
     * 通过code进行登录
     *
     * @param code
     * @return
     */
    @RequestMapping("loginByCode")
    @ResponseBody
    public Result<Map<String, Object>> loginByCode(String encryptedData,
                                                   String iv,
                                                   String code,
                                                   @RequestHeader(defaultValue = "") String softModel,
                                                   @RequestHeader(defaultValue = "") String sysUserId,
                                                   @RequestHeader(defaultValue = "") String tMemberId
                                                   ) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();
        //softModel : 0:小程序；1：android；2：ios；3：H5
        log.info("softModel: "+softModel);
        log.info("encryptedData: "+encryptedData);
        log.info("iv: "+iv);
        log.info("code: "+code);
            //参数判断
            if (StringUtils.isBlank(code)) {
                result.error500("code不能为空！！！");
                return result;
            }

            if (StringUtils.isBlank(encryptedData)) {
                result.error500("encryptedData不能为空！！！");
                return result;
            }

            if (StringUtils.isBlank(iv)) {
                result.error500("iv不能为空！！！");
                return result;
            }
            //获取常量信息
            String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID");
            String appSecret = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppSecret");
            String shareTimes = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "GET_THE_NUMBER");
            //用户唯一标识：0：phone（手机号）；1：openid；2：unionid
            String userUniqueIdentity=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "user_unique_identity ");

            log.info("微信小程序信息：appid="+appid+"；appSecret="+appSecret);

        //获取openid
            Map<String, Object> stringMap = WeixinUtils.getOpenId(appid, appSecret, code);
            String openid = (String) stringMap.get("openid");
            String sessionKey = (String) stringMap.get("session_key");
            String unionid=null;
            if(stringMap.get("unionid")!=null){
                unionid=(String)stringMap.get("unionid");
            }
            if (StringUtils.isBlank(openid)) {
                log.error("微信登录失败，获取不到 openid：{}",JSON.toJSON(stringMap));
                result.error500(JSON.toJSON(stringMap).toString());
                return result;
            }

        Map<String, Object> memMap = null;
        //根据openid获取用户信息
        try {
            String jsonresult = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            log.info("登录解密返回信息："+jsonresult);
            memMap = JSON.parseObject(jsonresult);
        } catch (Exception e) {
            e.printStackTrace();
            result.error500("解密出错");
            return result;
        }
        if(memMap==null){
            return result.error500("请再次登录");
        }
            MemberList memberList = iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                    .eq(userUniqueIdentity.equals("0"),MemberList::getPhone,memMap.get("phoneNumber").toString())
                    .eq(userUniqueIdentity.equals("3"),MemberList::getPhone,memMap.get("phoneNumber").toString())
                    .eq(userUniqueIdentity.equals("1"),MemberList::getOpenid,openid)
                    .eq(userUniqueIdentity.equals("2"),MemberList::getUnionId,unionid)
                    .eq(MemberList::getStatus,"1")
                    .orderByDesc(MemberList::getCreateTime)
                    .last("limit 1"));
            //用户存在
            if (memberList == null) {


                //限制注册时间
                if(memberUtils.registrationDate()){
                    return result.error500("当前注册人数较多，请稍后再试...");
                }

                //限制注册数量
                if(memberUtils.registerOnline()){
                    return result.error500("当前注册人数较多，请稍后再试...");
                }

                String promoCode  = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "promo_code");
                if(promoCode.equals("1")) {
                    if (StringUtils.isBlank(tMemberId)) {
                        return result.error500("您还没有推荐人，请先找个推荐人！！！");
                    }
                }
                memberList = new MemberList();
                memberList.setUnionId(unionid);
                memberList.setOpenid(openid);
                memberList.setDelFlag("0");
                if (memMap.get("phoneNumber") != null) {
                    memberList.setPhone(memMap.get("phoneNumber").toString());
                }
                memberList.setMemberType("0");
                memberList.setWelfarePayments(new BigDecimal(0));
                memberList.setBalance(new BigDecimal(0));
                memberList.setShareTimes(new BigDecimal(shareTimes));
                memberList.setIsOpenStore("0");
                memberList.setStatus("1");
                memberList.setSessionKey(sessionKey);
                log.info("sessionKey:  "+sessionKey);
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
                if(StringUtils.isBlank(memberList.getOpenid())){
                    memberList.setOpenid(openid);
                }else if(!memberList.getOpenid().equals(openid)){
                    memberList.setOpenid(openid);
                }
                if(StringUtils.isBlank(memberList.getUnionId())){
                    memberList.setUnionId(unionid);
                }else if(!memberList.getUnionId().equals(unionid)){
                    memberList.setUnionId(unionid);
                }
                memberList.setSessionKey(sessionKey);
                log.info("sessionKey:  "+sessionKey);
                iMemberListService.saveOrUpdate(memberList);
            }

            objectMap.put("X-AUTH-TOKEN", tokenManager.createToken(memberList.getId(),softModel));
            //添加分享二维码信息
            iMemberListService.addShareQr(memberList,sysUserId);

            //获取推广人信息
            MemberList memberList1=iMemberListService.getById(memberList.getId());
            if(memberList1.getPromoterType().equals("1")&&StringUtils.isNotBlank(memberList1.getPromoter())){
                MemberList memberListPromoter=iMemberListService.getById(memberList1.getPromoter());
                objectMap.put("promoterNickName",memberListPromoter.getNickName());
                objectMap.put("promoterHeadPortrait",memberListPromoter.getHeadPortrait());
            }

            //返回token信息
            result.setResult(objectMap);
            result.success("用户登录成功");

        return result;
    }

    /**
     * 通过code进行增加用户信息（接口已废除：客户端已经删除使用）
     *
     * @param code
     * @return
     */
    @RequestMapping("submitMemberInfo")
    @ResponseBody
    @Deprecated
    public Result<Map<String, Object>> submitMemberInfo(String encryptedData,
                                                        String iv,
                                                        String code,
                                                        @RequestHeader(defaultValue = "") String sysUserId,
                                                        @RequestHeader(defaultValue = "") String softModel
                                                        ) {
        Result<Map<String, Object>> result = new Result<>();
        //参数判断
        if (StringUtils.isBlank(code)) {
            result.error500("code不能为空！！！");
            return result;
        }

        if (StringUtils.isBlank(encryptedData)) {
            result.error500("encryptedData不能为空！！！");
            return result;
        }

        if (StringUtils.isBlank(iv)) {
            result.error500("iv不能为空！！！");
            return result;
        }

        //获取常量信息
        String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID");
        String appSecret = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppSecret");
        String shareTimes = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "GET_THE_NUMBER");
        //获取openid
        Map<String, Object> stringMap = WeixinUtils.getOpenId(appid, appSecret, code);
        String openid = (String) stringMap.get("openid");
        String sessionKey = (String) stringMap.get("session_key");
        if (StringUtils.isBlank(openid)) {
            result.error500("通过code查询不到openid");
            return result;
        }

        Map<String, Object> memMap = null;
        //根据openid获取用户信息
        try {
            String jsonresult = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            log.info(jsonresult);
            memMap = JSON.parseObject(jsonresult);
        } catch (Exception e) {
            e.printStackTrace();
            result.error500("解密出错");
            return result;
        }

        QueryWrapper<MemberList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        queryWrapper.eq("status", "1");
        queryWrapper.eq("del_flag", "0");
        MemberList memberList = iMemberListService.getOne(queryWrapper);
        //用户存在
        if (memberList != null) {
            memberList.setNickName(memMap.get("nickName").toString());
            memberList.setHeadPortrait(memMap.get("avatarUrl").toString());
            memberList.setSessionKey(sessionKey);
            String sex = memMap.get("gender").toString();
            if (sex.equals("0")) {
                memberList.setSex("0");
            } else if (sex.equals("1")) {
                memberList.setSex("1");
            } else if (sex.equals("2")) {
                memberList.setSex("2");
            }
            memberList.setAreaAddr(memMap.get("country").toString() + "-" + memMap.get("province").toString() + "-" + memMap.get("city").toString());
        } else {
            //用户不存在
            memberList = new MemberList();
            memberList.setOpenid(openid);
            memberList.setSessionKey(sessionKey);
            memberList.setDelFlag("0");
            memberList.setNickName(memMap.get("nickName").toString());
            memberList.setHeadPortrait(memMap.get("avatarUrl").toString());
            memberList.setMemberType("0");
            memberList.setWelfarePayments(new BigDecimal(0));
            memberList.setBalance(new BigDecimal(0));
            memberList.setShareTimes(new BigDecimal(shareTimes));
            memberList.setIsOpenStore("0");
            memberList.setStatus("1");
            String sex = memMap.get("gender").toString();
            if (sex.equals("0")) {
                memberList.setSex("0");
            } else if (sex.equals("1")) {
                memberList.setSex("1");
            } else if (sex.equals("2")) {
                memberList.setSex("2");
            }
            memberList.setAreaAddr(memMap.get("country").toString() + "-" + memMap.get("province").toString() + "-" + memMap.get("city").toString());
        }

        iMemberListService.saveOrUpdate(memberList);

        result.success("用户信息添加成功");
        return result;
    }

    /**
     * 通过会员id获取token
     *
     * @param id
     * @return
     */
    @RequestMapping("getMemberToken")
    @ResponseBody
    public Result<String> getMemberToken(String id,
                                         @RequestHeader(defaultValue = "", name = "X-AUTO-TOKEN") String token,
                                         @RequestHeader("softModel") String softModel,
                                         @RequestHeader(defaultValue = "") String sysUserId,
                                         HttpServletRequest request) {
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(id)) {
            result.error500("id不能为空！！！");
            return result;
        }
        MemberList memberList = iMemberListService.getById(id);
        if (memberList == null) {
            result.error500("找不到会员！！！");
            return result;
        }
        log.info("进来时候的token：" + token + ";memberId：" + id + ";" + memberList.getNickName());
        //用户登录判断
        Object memberId = request.getAttribute("memberId");
        if (memberId==null) {
            result.setResult(tokenManager.createToken(id,softModel));
        } else {
            TokenModel model = tokenManager.getToken(token, memberId.toString());
            if (tokenManager.checkToken(model,softModel)) {
                log.info("返回token：" + token);
                result.setResult(token);
            } else {
                result.setResult(tokenManager.createToken(id,softModel));
            }
        }

        //增加分享二维码
        if (StringUtils.isBlank(memberList.getSysSmallcodeId())) {
           //添加分享二维码信息
            iMemberListService.addShareQr(memberList,sysUserId);
        } else {
            SysSmallcode sysSmallcode = iSysSmallcodeService.getById(memberList.getSysSmallcodeId());
            if (sysSmallcode != null && StringUtils.isBlank(sysSmallcode.getTMemberId())) {
                sysSmallcode.setTMemberId(memberList.getId());
                iSysSmallcodeService.saveOrUpdate(sysSmallcode);
            }
        }
        result.success("token获取成功");
        return result;
    }


    /**
     * 方法已废弃
     *
     * @param jsonObject
     * @param softModel
     * @param sysUserId
     * @param tMemberId
     * @return
     */
    @RequestMapping("loginByOpenid")
    @ResponseBody
    @Deprecated
    public Result<Map<String,Object>> loginByOpenid(@RequestBody JSONObject jsonObject,
                                                    @RequestHeader(defaultValue = "") String softModel,
                                                    @RequestHeader(defaultValue = "") String sysUserId,
                                                    @RequestHeader(defaultValue = "") String tMemberId){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        JSONObject authResult = jsonObject.getJSONObject("authResult");
        Map<String, Object> userInfo = WeixinUtils.getUserInfo(authResult.getString("access_token"),authResult.getString("openid"));
        String unionid = (String) userInfo.get("unionid");
        String openid = (String) userInfo.get("openid");
        MemberList memberList = iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                .eq(MemberList::getDelFlag, "0")
                .eq(MemberList::getUnionId, unionid)
                .eq(MemberList::getStatus, "1"));
        if (oConvertUtils.isNotEmpty(memberList)){
            if (StringUtils.isBlank(memberList.getAppOpenid())){
                memberList.setAppOpenid(openid);
            }
        }else {
            memberList = new MemberList()
                    .setDelFlag("0")
                    .setHeadPortrait((String) userInfo.get("headimgurl"))
                    .setNickName((String)userInfo.get("nickname"))
                    .setSex(String.valueOf(userInfo.get("sex")))
                    .setAreaAddr(userInfo.get("province")+"-"+userInfo.get("city"))
                    .setMemberType("0")
                    .setIsOpenStore("0")
                    .setStatus("1")
                    .setAppOpenid(openid)
                    .setUnionId(unionid)
                    .setSysUserId(sysUserId);

            //查出分销设置
            MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.list(new LambdaQueryWrapper<MarketingDistributionSetting>()
                    .eq(MarketingDistributionSetting::getDelFlag, "0")
                    .eq(MarketingDistributionSetting::getStatus, "1")).get(0);

            if (StringUtils.isNotBlank(tMemberId)){
                log.info("会员信息："+userInfo+"；推广人id："+tMemberId+"；推广店铺id："+sysUserId);
                //推广人id不为空
                if (marketingDistributionSetting.getDistributionBuild().equals("0")) {
                    iMemberListService.setPromoter(memberList,tMemberId);

                }
                //2021年3月3日17:15:46 原称号废弃,改为多对多称号团队
                /*LambdaQueryWrapper<MemberDesignation> memberDesignationLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignation>()
                        .eq(MemberDesignation::getDelFlag, "0")
                        .eq(MemberDesignation::getStatus, "1");
                if (iMemberDesignationService.count(memberDesignationLambdaQueryWrapper)>0){
                    if (StringUtils.isBlank(memberList.getMemberDesignationId())){
                        MemberDesignation memberDesignation = iMemberDesignationService
                                .list(memberDesignationLambdaQueryWrapper
                                        .eq(MemberDesignation::getSort,"0")).get(0);
                        iMemberListService.saveOrUpdate(memberList
                                .setMemberDesignationId(memberDesignation.getId())
                        );
                        if (iMemberDesignationCountService.count(new LambdaQueryWrapper<MemberDesignationCount>()
                                .eq(MemberDesignationCount::getDelFlag,"0")
                                .eq(MemberDesignationCount::getMemberDesignationId,memberList.getMemberDesignationId())
                                .eq(MemberDesignationCount::getMemberListId,memberList.getId())
                        )<=0){
                            iMemberDesignationCountService.save(new MemberDesignationCount()
                                    .setDelFlag("0")
                                    .setMemberDesignationId(memberDesignation.getId())
                                    .setMemberListId(memberList.getId())
                                    .setTotalMembers(new BigDecimal(1))
                            );
                        }
                    }
                    if (StringUtils.isBlank(memberList.getOldTManageId())){
                        iMemberListService.memberListSetTManageId(memberList,tMemberId);
                    }
                }*/

                /*if (StringUtils.isBlank(memberList.getOldTManageId())){
                    iMemberListService.memberListSetTManageId(memberList,tMemberId);
                }*/
            }else {
                if (StringUtils.isNotBlank(sysUserId)) {
                    //推广人类型为店铺
                    memberList.setPromoterType("0");
                    memberList.setPromoter(sysUserId);
                }
            }
        }
        iMemberListService.saveOrUpdate(memberList);
        map.put("X-AUTH-TOKEN", tokenManager.createToken(memberList.getId(),softModel));
        //增加分享二维码
        if (memberList.getSysSmallcodeId() == null) {
            SysSmallcode sysSmallcode = new SysSmallcode();
            sysSmallcode.setSysUserId(memberList.getSysUserId());

            //判断渠道id归属渠道id
            if (StringUtils.isNotBlank(sysUserId)) {
                String systemSharingModel = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "system_sharing_model");
                if (systemSharingModel.equals("1")) {
                    sysSmallcode.setSysUserId(sysUserId);
                }
            }

            sysSmallcode.setTMemberId(memberList.getId());
            sysSmallcode.setCodeType("1");
            iSysSmallcodeService.save(sysSmallcode);
            memberList.setSysSmallcodeId(sysSmallcode.getId());
            sysSmallcode.setAddress(weixinQRUtils.getQrCode(sysSmallcode.getId()));
            iSysSmallcodeService.saveOrUpdate(sysSmallcode);
        }
        iMemberListService.saveOrUpdate(memberList);
        result.setResult(map);
        result.success("用户登录成功!");
        return result;
    }


    /**
     * 方法已废弃
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping("appSubmitMemberInfo")
    @ResponseBody
    @Deprecated
    public Result<Map<String,Object>> appSubmitMemberInfo(@RequestBody JSONObject jsonObject){
        Result<Map<String, Object>> result = new Result<>();
        if (jsonObject.getString("errMsg").contains("ok")){
            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            List<MemberList> memberLists = iMemberListService.list(new LambdaQueryWrapper<MemberList>()
                    .eq(MemberList::getDelFlag, "0")
                    .eq(MemberList::getUnionId, userInfo.getString("unionId"))
            );
            if (memberLists.size()>0){
                result.success("用户信息添加成功");
            }else {
                iMemberListService.save(new MemberList()
                        .setDelFlag("0")
                        .setHeadPortrait(userInfo.getString("avatarUrl"))
                        .setNickName("")
                );
            }
        }else {
            return result.error500("获取信息失败!");
        }
        return result;
    }

    public static void main(String[] args) {
        String appid = "wx3726e5aa7e0fe80b";
        String appSecret = "49aafafbf07436bf7669d9d7831651f2";
        String code = "053BQZZv3IJ3103aLQ0w3UEHgz0BQZZG";
        Map<String, Object> stringMap = WeixinUtils.getOpenId(appid, appSecret, code);
        System.out.println(stringMap);
    }
}
