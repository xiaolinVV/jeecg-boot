package org.jeecg.modules.message.api;


import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 消息类型的接口
 */
@RequestMapping("after/message")
@Controller
public class AfterMessageController {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DySmsHelper dySmsHelper;

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

}
