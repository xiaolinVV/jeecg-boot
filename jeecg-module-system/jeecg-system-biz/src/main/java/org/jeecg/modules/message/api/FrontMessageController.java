package org.jeecg.modules.message.api;


import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.dto.SysUserDTO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 消息类型的接口
 */
@RequestMapping("front/message")
@Controller
public class FrontMessageController {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DySmsHelper dySmsHelper;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @RequestMapping("verificationCode")
    @ResponseBody
    public Result<String> verificationCode(String phone){
        Result<String> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(phone)){
            result.error500("手机号码不能为空");
            return result;
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(6);

        // TODO: 2022/12/8 发送短信工具需要重构，这里暂时注释 @zhangshaolin
//        try {
//            if(dySmsHelper.sendSms(phone, captcha, dySmsHelper.IDENTITY_TEMPLATE_CODE)){
//                //验证码10分钟内有效
//                redisUtil.set(phone, captcha, 600);
//            }else{
//                result.error500("验证码发送失败");
//                return result;
//            }
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }

        result.success("验证码发送成功");
        return result;
    }

    @RequestMapping("forGetThePassWord")
    @ResponseBody
    public Result<String> forGetThePassWord(@RequestBody SysUserDTO sysUserDTO){
        Result<String> result = new Result<>();
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDelFlag, "0")
                .eq(SysUser::getUsername, sysUserDTO.getUserName());
        if (iSysUserService.count(sysUserLambdaQueryWrapper)<=0){
            return result.error500("登录账号未找到,请重新填写");
        }
        SysUser sysUser = iSysUserService.list(sysUserLambdaQueryWrapper).get(0);
        Object code = redisUtil.get(sysUserDTO.getPhone());

        if (!sysUserDTO.getSbCode().equals(code)) {
            return result.error500("手机验证码错误");
        }

        if (StringUtils.isNotBlank(sysUser.getPhone())){
            if (!sysUser.getPhone().equals(sysUserDTO.getPhone())){
                return result.error500("绑定手机号码与账号的绑定号码有误,请核对");
            }
        }else {
            sysUser.setPhone(sysUserDTO.getPhone());
        }
        if (!sysUserDTO.getNewPassword().equals(sysUserDTO.getAffirmPassword())){
            return result.error500("两次输入密码有误");
        }
        String newpassword = PasswordUtil.encrypt(sysUser.getUsername(), sysUserDTO.getNewPassword(), sysUser.getSalt());
        boolean b = iSysUserService.saveOrUpdate(sysUser.setPassword(newpassword));
        if (b){
            result.setResult("修改成功");
        }else {
            return result.error500("修改失败");
        }
        return result;
    }
}
