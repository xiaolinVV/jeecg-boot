package org.jeecg.modules.system.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.weixin.WeixinUtils;
import org.jeecg.config.jwt.model.TokenModel;
import org.jeecg.config.jwt.service.TokenManager;
import org.jeecg.config.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.order.utils.WeixinHftxPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.dto.StoreParamDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.dto.SysUserDTO;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 店铺登录api
 */

@RequestMapping("before/sysUser")
@Controller
@Slf4j
public class BeforeSysUserController {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private WeixinQRUtils weixinQRUtils;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    @Autowired
    private WeixinHftxPayUtils weixinHftxPayUtils;


    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public Result<Map<String,Object>> login(@RequestHeader("softModel") String softModel,String userName,String password){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();

        if(StringUtils.isBlank(userName)){
            result.error500("用户名不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(password)){
            result.error500("密码不能为空！！！");
            return result;
        }


        //1. 校验用户是否有效
        SysUser sysUser = iSysUserService.getUserByName(userName);
        result = iSysUserService.checkUserIsEffective(sysUser);
        if(!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(userName, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        //生成token
        objectMap.put("X-AUTH-TOKEN",tokenManager.createToken(sysUser.getId(),softModel));

        //获取用户信息
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUser.getId());
        storeManageQueryWrapper.in("pay_status", "1","2");
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
        if (storeManage.getSubStoreName() == null) {
            objectMap.put("storeName", storeManage.getStoreName());
        } else {
            objectMap.put("storeName", storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
        }
        objectMap.put("logoAddr",storeManage.getLogoAddr());
        objectMap.put("bossPhone",storeManage.getBossPhone());
        objectMap.put("status",storeManage.getStatus());
        objectMap.put("attestationStatus",storeManage.getAttestationStatus());
        objectMap.put("sysUserId",sysUser.getId());
        //返回token信息
        result.setResult(objectMap);
        result.success("商户登录成功");
        return result;

    }


    /**
     * 注册 开店支付调用
     *
     * @param storeParamDTO
     * @param bindingResult
     * @return
     */
    @RequestMapping("registeredOpenTheStore")
    @ResponseBody
    public Result<Map<String,Object>> registeredOpenTheStore(@Valid StoreParamDTO storeParamDTO,
                                                             BindingResult bindingResult){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //参数验证
        if (bindingResult.getErrorCount() > 0){
            result.error500(bindingResult.getFieldError().toString());
            return result;

        }
        //验证码验证
        String smscode = storeParamDTO.getVerificationCode();
        Object code1 = redisUtil.get(storeParamDTO.getBossPhone());
        if(code1==null){
            result.error500("手机号找不到验证码");
            return result;
        }
        if(!code1.toString().equals(smscode)){
            result.error500("验证码输入不正确，请重新输入");
            return result;
        }
        if(StringUtils.isBlank(storeParamDTO.getCode())){
            result.error500("小程序参数code,不能为空!");
            return result;
        }


        SysUser sysUser=iSysUserService.getUserByName(storeParamDTO.getBossPhone());
        if(sysUser!=null){
            result.error500("本手机号已经开通了店铺");
            return result;
        }

        QueryWrapper<StoreManage> storeManageQueryWrapper2=new QueryWrapper<>();
        storeManageQueryWrapper2.eq("boss_phone",storeParamDTO.getBossPhone());
        storeManageQueryWrapper2.eq("pay_status","1");
        if(iStoreManageService.count(storeManageQueryWrapper2)>0){
            result.error500("本手机号已经开通了店铺");
            return result;
        }

        BigDecimal totalPrice=new BigDecimal(0);

        //新建待支付店铺信息
        StoreManage storeManage=new StoreManage();
        //拷贝数据
        BeanUtils.copyProperties(storeParamDTO,storeManage);
        //设置开店基本信息
        //开通费用
        String openType=storeManage.getOpenType();
        if(openType.equals("0")){
            //包年
            totalPrice=new BigDecimal(iSysDictService
                    .queryTableDictTextByKey("sys_dict_item","item_value","item_text","pack_years"));

        }else if (openType.equals("1")){
            //终生
            totalPrice=new BigDecimal(iSysDictService
                    .queryTableDictTextByKey("sys_dict_item","item_value","item_text","lifelong"));

        }
        //获取常量信息
        String appid1 = iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","AppID_store");
        String appSecret= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","AppSecret_store");
        //获取openid
        Map<String,Object> stringMap = WeixinUtils.getOpenId(appid1,appSecret,storeParamDTO.getCode());
        String openid= (String) stringMap.get("openid");


        storeManage.setDelFlag("0");

        //开通费用
        storeManage.setMoney(totalPrice);
        //未开通
        storeManage.setStatus("0");
        //认证状态   未认证
        storeManage.setAttestationStatus("-1");
        //支付状态未支付
        storeManage.setPayStatus("0");
        //配送方式快递
        storeManage.setDistributionType("0");
        //配送状态启用
        storeManage.setDistributionStatus("0");

        //storeManage.setMemberListId(memberId);
        storeManage.setWelfarePayments(new BigDecimal(0));
        storeManage.setBalance(new BigDecimal(0));
        storeManage.setAccountFrozen(new BigDecimal(0));
        storeManage.setUnusableFrozen(new BigDecimal(0));
        storeManage.setServiceRange(new BigDecimal(iSysDictService
                .queryTableDictTextByKey("sys_dict_item","item_value","item_text","default_service_distance")));

        storeManage.setGrade(new BigDecimal(5));
        storeManage.setComprehensiveEvaluation(new BigDecimal(5));
        storeManage.setBackStatus("0");
        storeManage.setBackTimes(new BigDecimal(0));

        //推广标识

        if(storeParamDTO.getTType().equals("0")){
            storeManage.setPromoterType(storeParamDTO.getTType());
            storeManage.setPromoter(storeParamDTO.getTSysUserId());
            StoreManage serviceOne = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getDelFlag, "0")
                    .eq(StoreManage::getSysUserId, storeParamDTO.getTSysUserId()));
            if (StringUtils.isNotBlank(serviceOne.getAllianceUserId())){
                storeManage.setAllianceUserId(serviceOne.getAllianceUserId());
            }
        }

        if(storeParamDTO.getTType().equals("3")){
            storeManage.setPromoterType(storeParamDTO.getTType());
            storeManage.setPromoter(storeParamDTO.getTSysUserId());
            storeManage.setAllianceUserId(storeParamDTO.getTSysUserId());
        }

        //保存店铺信息
        iStoreManageService.save(storeManage);

        //设置回调地址

        String openStoreNotifyUrl=notifyUrlUtils.getNotify("registered_shops_open_store");
        //支付信息调用
        Map<String,String> resultMap=weixinHftxPayUtils.payWeixinStore(totalPrice,storeManage.getId(),openStoreNotifyUrl,openid,null);
        String jsonStr=resultMap.get("jsonStr");
        storeManage.setPayParam(resultMap.get("params"));
        //保存支付日志
        iStoreManageService.saveOrUpdate(storeManage);
        objectMap.put("notifyUrl",openStoreNotifyUrl+"?id="+storeManage.getId());
        objectMap.put("jsonStr",jsonStr);
        objectMap.put("storeManageId",storeManage.getId());
        result.setResult(objectMap);
        result.success("开店调起支付");
        return result;
    }



    /**
     * 通过店铺id获取token
     * @param id
     * @return
     */
    @RequestMapping("getSysUserIdToken")
    @ResponseBody
    public  Result<Map<String,Object>> getSysUserIdToken(String id,@RequestHeader("softModel") String softModel, @RequestHeader(defaultValue = "",name = "X-AUTO-TOKEN") String token, HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        SysUser sysUser=iSysUserService.getById(id);
        log.info("进来时候的token："+token+";sysUserId："+id+";"+sysUser.getUsername());
        if(sysUser==null){
            result.error500("找不到会员！！！");
            return result;
        }

        //用户登录判断
        Object sysUserId=request.getAttribute("sysUserId");
        if(sysUserId==null){
            objectMap.put("X-AUTH-TOKEN",tokenManager.createToken(id,softModel));
        }else{
            TokenModel model = tokenManager.getToken(token,sysUserId.toString());
            if (tokenManager.checkToken(model,softModel)) {
                log.info("返回token："+token);
                objectMap.put("X-AUTH-TOKEN",token);
            } else {
                objectMap.put("X-AUTH-TOKEN",tokenManager.createToken(id,softModel));
            }
        }

        //获取用户信息
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUser.getId());
        storeManageQueryWrapper.in("pay_status", "1","2");
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
        if (storeManage.getSubStoreName() == null) {
            objectMap.put("storeName", storeManage.getStoreName());
        } else {
            objectMap.put("storeName", storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
        }
        objectMap.put("logoAddr",storeManage.getLogoAddr());
        objectMap.put("bossPhone",storeManage.getBossPhone());
        objectMap.put("status",storeManage.getStatus());
        objectMap.put("attestationStatus",storeManage.getAttestationStatus());
        objectMap.put("sysUserId",sysUser.getId());

        if(StringUtils.isBlank(storeManage.getSysSmallcodeId())){
            SysSmallcode sysSmallcode = new SysSmallcode();
            sysSmallcode.setDelFlag("0");
            sysSmallcode.setSysUserId(storeManage.getSysUserId());
            sysSmallcode.setCodeType("0");
            iSysSmallcodeService.save(sysSmallcode);
            storeManage.setSysSmallcodeId(sysSmallcode.getId());
            sysSmallcode.setAddress(weixinQRUtils.getQrCode(sysSmallcode.getId()));
            iSysSmallcodeService.saveOrUpdate(sysSmallcode);
            iStoreManageService.saveOrUpdate(storeManage);
        }
        SysSmallcode sysSmallcode=iSysSmallcodeService.getById(storeManage.getSysSmallcodeId());
        objectMap.put("qRaddress",sysSmallcode.getAddress());
        result.setResult(objectMap);
        result.success("token获取成功");
        return result;
    }

    /**
     * 忘记密码
     * @param request
     * @return
     */
    @RequestMapping("forgetPassword")
    @ResponseBody
    public Result<String> forgetPassword(SysUserDTO sysUserDTO,
                                         HttpServletRequest request){
        Result<String> result = new Result<>();

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDelFlag, "0")
                .eq(SysUser::getUsername, sysUserDTO.getUserName());
        if (iSysUserService.count(sysUserLambdaQueryWrapper)>0){
            SysUser user = iSysUserService.list(sysUserLambdaQueryWrapper).get(0);
            if (StringUtils.isBlank(sysUserDTO.getNewPassword())) {
                result.error500("新密码不存在!");
                return result;
            }

            if (!sysUserDTO.getNewPassword().equals(sysUserDTO.getAffirmPassword())) {
                result.error500("两次输入密码不一致!");
                return result;
            }
            Object code = redisUtil.get(sysUserDTO.getPhone());
            if (!code.equals(sysUserDTO.getSbCode())){
                return result.error500("验证码错误!");
            }
            String newpassword = PasswordUtil.encrypt(sysUserDTO.getUserName(), sysUserDTO.getNewPassword(), user.getSalt());
            boolean b = iSysUserService.saveOrUpdate(user.setPassword(newpassword));
            if (b){
                result.success("密码修改成功");
            }else {
                return result.error500("密码修改失败!");
            }
        }else {
            return result.error500("账号异常,查无此人");
        }
        return result;
    }
}
