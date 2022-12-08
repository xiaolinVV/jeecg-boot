package org.jeecg.modules.captcha.wapi;


import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.exceptions.ClientException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DySmsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


/**
 * 验证码接口
 */
@RequestMapping("wapi/captcha")
@Controller
@Log
public class WapiCaptchaController {


    @Autowired
    private DefaultKaptcha producer;

    @Autowired
    private DySmsHelper dySmsHelper;

    /**
     * 图片验证码
     *
     * 张靠勤  2021-4-7
     *
     * @return
     */
    @RequestMapping("getCaptcha")
    @ResponseBody
    public Result<Map<String,String>> getCaptcha(){
        Result<Map<String,String>> result=new Result<>();

        Map<String,String> map= Maps.newHashMap();

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
        map.put("base64Img",base64Img);

        //生成一个随机标识符
        String captchaKey = UUID.randomUUID().toString();

        log.info("生成的验证码为："+content);
        log.info("生成的唯一标识为："+captchaKey);

        //设置唯一标识
        map.put("captchaKey",captchaKey);

        //设置验证码的值
        map.put("content",content);

        result.setResult(map);
        result.success("验证码查询成功");
        return result;
    }


    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @RequestMapping("verificationCode")
    @ResponseBody
    public Result<Map<String,String>> verificationCode(String phone){
        Result<Map<String,String>> result=new Result<>();

        Map<String,String> stringMap=Maps.newHashMap();

        //参数验证
        if(StringUtils.isBlank(phone)){
            result.error500("手机号码不能为空");
            return result;
        }
        //随机数
        String captcha = RandomUtil.randomNumbers(4);
        stringMap.put("captcha",captcha);
        // TODO: 2022/12/8 发送短信工具需要重构，这里暂时注释 @zhangshaolin
//        try {
//            if(dySmsHelper.sendSms(phone, captcha, dySmsHelper.IDENTITY_TEMPLATE_CODE)){
//            }else{
//                result.error500("验证码发送失败");
//                return result;
//            }
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
        result.setResult(stringMap);
        result.success("验证码发送成功");
        return result;
    }
}
