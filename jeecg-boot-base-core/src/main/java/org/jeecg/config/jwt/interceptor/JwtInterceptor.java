package org.jeecg.config.jwt.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginMemberList;
import org.jeecg.common.util.oConvertUtils;

import org.jeecg.config.jwt.def.JwtConstants;
import org.jeecg.config.jwt.model.TokenModel;
import org.jeecg.config.jwt.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/**
 * jwt拦截器
 */
@Log
public class JwtInterceptor implements HandlerInterceptor {

    @Lazy
    @Resource
    private TokenManager tokenManager;

    @Lazy
    @Resource
    private CommonAPI commonApi;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求地址
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        Result<String> result=new Result<>();
        //从header中得到token
        String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
        String softModel = request.getHeader("softModel");
        if(requestPath.indexOf("/front/")>=0){
            if(StringUtils.isBlank(authHeader)){
                return true;
            }
            Object memberId=tokenManager.getMemberIdByToken(authHeader);
            if(oConvertUtils.isEmpty(memberId)){
                return true;
            }
            TokenModel model = tokenManager.getToken(authHeader,memberId.toString());
            if (tokenManager.checkToken(model,softModel)) {
                //如果token验证成功，将token对应的用户id存在request中，便于之后注入
                request.setAttribute(JwtConstants.CURRENT_USER_NAME, model.getMemberId());
                return true;
            } else {
               return true;
            }
        }
        if(requestPath.indexOf("/after/")>=0){
            if (authHeader == null) {
                // 如果验证token失败，则返回401错误
                result.setMessage("token解析失败");
                result.setCode(666);
                result.setSuccess(false);
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(result));
                out.close();
                return false;
            }

            Object memberId=tokenManager.getMemberIdByToken(authHeader);
            if(oConvertUtils.isEmpty(memberId)){
                result.setMessage("token解析失败");
                result.setCode(666);
                result.setSuccess(false);
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(result));
                out.close();
                return false;
            }
            TokenModel model = tokenManager.getToken(authHeader,memberId.toString());
            if (tokenManager.checkToken(model,softModel)) {
                //如果token验证成功，将token对应的用户id存在request中，便于之后注入


                //判断账号是否冻结
                LoginMemberList loginMemberList = commonApi.getMemberListById(model.getMemberId());
                if(loginMemberList.getStatus().equals("0")){
                    result.setMessage("账号已冻结，请联系管理员");
                    result.setCode(500);
                    result.setSuccess(false);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.write(JSON.toJSONString(result));
                    out.close();
                    return false;
                }
                request.setAttribute(JwtConstants.CURRENT_USER_NAME, model.getMemberId());
                return true;
            } else {
                result.setMessage("token解析失败");
                result.setCode(666);
                result.setSuccess(false);
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(result));
                out.close();
                return false;
            }
        }

        if(requestPath.indexOf("/back/")>=0){
            if (authHeader == null) {
                // 如果验证token失败，则返回401错误
                result.setMessage("token解析失败");
                result.setCode(666);
                result.setSuccess(false);
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(result));
                out.close();
                return false;
            }

            Object sysUserId=tokenManager.getMemberIdByToken(authHeader);
            if(oConvertUtils.isEmpty(sysUserId)){
                result.setMessage("token解析失败");
                result.setCode(666);
                result.setSuccess(false);
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(result));
                out.close();
                return false;
            }
            TokenModel model = tokenManager.getToken(authHeader,sysUserId.toString());
            if (tokenManager.checkToken(model,softModel)) {
                //如果token验证成功，将token对应的用户id存在request中，便于之后注入
                request.setAttribute("sysUserId", model.getMemberId());
                return true;
            } else {
                result.setMessage("token解析失败");
                result.setCode(666);
                result.setSuccess(false);
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(result));
                out.close();
                return false;
            }
        }

        return true;
    }

}
