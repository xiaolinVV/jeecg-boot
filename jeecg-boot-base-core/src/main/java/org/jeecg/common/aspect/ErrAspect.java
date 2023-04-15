package org.jeecg.common.aspect;

import club.javafamily.nf.request.FeiShuCardNotifyRequest;
import club.javafamily.nf.service.FeiShuNotifyHandler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.vo.LoginMemberList;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.jwt.def.JwtConstants;
import org.jeecg.config.sign.util.BodyReaderHttpServletRequestWrapper;
import org.jeecg.config.sign.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.SortedMap;

/**
 * @author 张少林
 * @description aop异常处理器，捕获 controller异常并通知
 * @date 2023年04月02日 9:46 上午
 */
@Aspect
@Component
@Slf4j
public class ErrAspect {

    @Autowired
    private FeiShuNotifyHandler feiShuNotifyHandler;

    @Lazy
    @Resource
    private CommonAPI commonApi;

    @Value("${spring.profiles.active}")
    private String env;

    // 定义切点Pointcut
    @Pointcut("execution(public * org.jeecg.modules..*.*Controller.*(..))")
    public void excudeService() {
    }

    @AfterThrowing(value = "excudeService()", throwing = "ex")
    public void getErr(JoinPoint joinPoint, Exception ex) throws IOException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        String requestMethodName = className + "." + methodName + "()";

        //获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        HttpServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        //获取全部参数(包括URL和body上的)
        SortedMap<String, String> allParams = HttpUtils.getAllParams(requestWrapper);
        String reqestParams = JSON.toJSONString(allParams);
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());

        //获取登录用户信息
        Dict requestUserDict = null;
        if(requestPath.startsWith("/front/") || requestPath.startsWith("/after/") || requestPath.startsWith("/back/")){
            String memberId = Convert.toStr(request.getAttribute(JwtConstants.CURRENT_USER_NAME));
            if (StrUtil.isNotBlank(memberId)) {
                LoginMemberList loginMemberList = commonApi.getMemberListById(memberId);
                if (loginMemberList != null) {
                    requestUserDict = new Dict().set("memberId",loginMemberList.getId()).set("phone",loginMemberList.getPhone()).set("nickName",loginMemberList.getNickName());
                }
            }
        }else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if(sysUser!=null){
                requestUserDict = new Dict().set("sysUserId",sysUser.getId()).set("username",sysUser.getUsername()).set("realName",sysUser.getRealname());
            }
        }
        // 发送飞书告警
        String title = StrUtil.equals(env, "prod") ? "全惠付正式环境异常告警" : "全惠付v2开发环境异常告警";
        String buttonUrl = StrUtil.equals(env, "prod") ? "https://gk.quanhuifu.com" : "https://test.api.quanhuifu.com";
        String content = "告警时间: " + DateUtil.formatDateTime(DateUtil.date())
                + "\n接口地址：" + requestPath
                + "\n接口方法：" + requestMethodName
                + "\n请求参数：" + reqestParams;
        if (requestUserDict != null) {
            String requestUserName = StrUtil.isNotBlank(requestUserDict.getStr("nickName")) ? requestUserDict.getStr("nickName") : requestUserDict.getStr("realName");
            content = content  + "\n调用人名称：" + (StrUtil.isNotBlank(requestUserName) ? requestUserName : "无");
            content = content + "\n调用人信息：" + JSON.toJSONString(requestUserDict);
        }
        content = content  + "\n异常信息：" + ExceptionUtil.stacktraceToString(ex);
        final FeiShuCardNotifyRequest feiShuCardNotifyRequest
                = FeiShuCardNotifyRequest.of(title, content,
                "立即前往系统查看 :玫瑰:️ ✅ \uD83D\uDDA5️", buttonUrl);
        feiShuNotifyHandler.notify(feiShuCardNotifyRequest);
    }
}
