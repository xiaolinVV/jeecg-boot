package org.jeecg.modules.captcha.api;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 验证码的控制类
 */


@Controller
@RequestMapping("captcha")
@Log
public class CaptchaController {

    @Autowired
    private DefaultKaptcha producer;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IMemberListService iMemberListService;


    /**
     * 图片验证码
     *
     * @return
     */
    @RequestMapping("getCaptcha")
    @ResponseBody
    public Result<Map<String, String>> getCaptcha(HttpServletRequest request) {
        Result<Map<String, String>> result = new Result<>();

        Map<String, String> map = Maps.newHashMap();
        String ip = IpUtils.getIpAddr(request);
        log.info("请求的IP地址：" + ip);
        if (redisUtil.get("captcha" + ip) == null) {
            //缓存验证码并设置过期时间
            redisUtil.set("captcha" + ip, ip, 2);
        } else {
            return result.error500("同一个ip请求过于频繁！！！请稍后再试！！！");
        }
        // 生成文字验证码
        String content = producer.createText();
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = producer.createImage(content);

        outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = "data:image/jpeg;base64,";
        String base64Img = str + Base64Utils.encodeToString(outputStream.toByteArray()).replace("\n", "").replace("\r", "");

        //设置验证码
        map.put("base64Img", base64Img);

        //生成一个随机标识符
        String captchaKey = UUID.randomUUID().toString();

        log.info("生成的验证码为：" + content);
        log.info("生成的唯一标识为：" + captchaKey);

        //设置唯一标识
        map.put("captchaKey", captchaKey);

        //缓存验证码并设置过期时间
        redisUtil.set(captchaKey, content, 600);


        //设置验证码的值
        map.put("content", content);

        result.setResult(map);
        result.success("验证码查询成功");
        return result;
    }


    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping("verificationCode")
    @ResponseBody
    public Result<Map<String, String>> verificationCode(String phone, String captchaKey, String captchaContent) {
        Result<Map<String, String>> result = new Result<>();

        Map<String, String> stringMap = Maps.newHashMap();

        //参数验证
        if (StringUtils.isBlank(phone)) {
            result.error500("手机号码不能为空");
            return result;
        }

        if (StringUtils.isBlank(captchaKey)) {
            result.error500("验证码的key不能为空");
            return result;
        }

        if (StringUtils.isBlank(captchaContent)) {
            result.error500("图片验证码的值不能为空");
            return result;
        }


        //获取验证码
        Object captchaObject = redisUtil.get(captchaKey);
        if (captchaObject == null || !captchaObject.toString().toLowerCase().equals(captchaContent.toLowerCase())) {
            result.error500("验证码不正确");
            return result;
        }


        //随机数
        String captcha = RandomUtil.randomNumbers(4);
        JSONObject templateParamJson = new JSONObject();
        templateParamJson.put("code", captcha);
        try {
            if (DySmsHelper.sendSms(phone, templateParamJson, DySmsEnum.IDENTITY_TEMPLATE_CODE)) {
                //验证码10分钟内有效
                redisUtil.set(phone, captcha, 600);
            } else {
                result.error500("验证码发送失败");
                return result;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }


        //获取推广人信息
        MemberList memberList = iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                .eq(MemberList::getPhone, phone)
                .orderByDesc(MemberList::getCreateTime)
                .last("limit 1"));
        if (memberList != null && memberList.getPromoterType().equals("1") && StringUtils.isNotBlank(memberList.getPromoter())) {
            MemberList memberListPromoter = iMemberListService.getById(memberList.getPromoter());
            if (memberListPromoter != null) {
                stringMap.put("promoterNickName", memberListPromoter.getNickName());
                stringMap.put("promoterHeadPortrait", memberListPromoter.getHeadPortrait());
            }
        }

        result.setResult(stringMap);
        result.success("验证码发送成功");
        return result;
    }


    /**
     * 发送短信验证码不带图片验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping("verificationCodeNoCaptcha")
    @ResponseBody
    public Result<?> verificationCodeNoCaptcha(String phone) {
        Result<String> result = new Result<>();

        //参数验证
        if (StringUtils.isBlank(phone)) {
            result.error500("手机号码不能为空");
            return result;
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(4);
        log.info("交易密码" + captcha);
        JSONObject templateParamJson = new JSONObject();
        templateParamJson.put("code", captcha);
        try {
            if(DySmsHelper.sendSms(phone, templateParamJson, DySmsEnum.IDENTITY_TEMPLATE_CODE)){
                //验证码10分钟内有效
                redisUtil.set(phone, captcha, 600);
            }else{
                result.error500("验证码发送失败");
                return result;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        result.setResult(captcha);
        result.success("验证码发送成功");
        return result;
    }


}
