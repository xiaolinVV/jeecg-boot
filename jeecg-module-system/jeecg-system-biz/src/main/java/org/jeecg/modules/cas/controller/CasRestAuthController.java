package org.jeecg.modules.cas.controller;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.UnsupportedEncodingException;

/**
 * @author 张少林
 * @date 2024年05月08日 09:32
 */
@RestController
@RequestMapping(value = "/sys/casRestAuth")
@Slf4j
public class CasRestAuthController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 1. cas 服务端会通过post请求，并且把用户信息以"用户名:密码"进行Base64编码放在authorization请求头中
     * 2. 返回200状态码并且格式为{"@class":"org.apereo.cas.authentication.principal.SimplePrincipal","id":"xuda","attributes":{}} 是成功的
     * 2. 返回状态码403用户不可用；404账号不存在；423账户被锁定；428过期；其他登录失败
     *
     * @param httpHeaders
     * @return
     */
    @PostMapping("/login")
    public Object login(@RequestHeader HttpHeaders httpHeaders) {
        log.info("Rest api login.");
        log.debug("request headers: {}", httpHeaders);
        SysUser user = null;
        try {
            UserTemp userTemp = obtainUserFormHeader(httpHeaders);
            //尝试查找用户库是否存在
            user = sysUserService.getUserByName(userTemp.username);
            if (user != null) {
                // step.2 校验用户是否存在且有效
                Result result = sysUserService.checkUserIsEffective(user);
                if(!result.isSuccess()) {
                    //禁用 403
                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                }
                // step.3 校验用户名或密码是否正确
                String userpassword = PasswordUtil.encrypt(userTemp.username, userTemp.password, user.getSalt());
                String syspassword = user.getPassword();
                if (!syspassword.equals(userpassword)) {
                    //密码不匹配
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            } else {
                //不存在 404
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
            new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        log.info("[{}] login is ok", user.getUsername());

        //成功返回json
        Dict result = Dict.create()
                .set("@class", "org.apereo.cas.authentication.principal.SimplePrincipal")
                .set("id", user.getUsername())
                .set("attributes", new JSONObject());
        return result;
    }

    /**
     * 根据请求头获取用户名及密码
     *
     * @param httpHeaders
     * @return
     * @throws UnsupportedEncodingException
     */
    private UserTemp obtainUserFormHeader(HttpHeaders httpHeaders) throws UnsupportedEncodingException {
        /**
         *
         * This allows the CAS server to reach to a remote REST endpoint via a POST for verification of credentials.
         * Credentials are passed via an Authorization header whose value is Basic XYZ where XYZ is a Base64 encoded version of the credentials.
         */
        //当请求过来时，会通过把用户信息放在请求头authorization中，并且通过Basic认证方式加密
        String authorization = httpHeaders.getFirst("authorization");//将得到 Basic Base64(用户名:密码)
        String baseCredentials = authorization.split(" ")[1];
        String usernamePassword = Base64Coder.decodeString(baseCredentials);//用户名:密码
        log.debug("login user: {}", usernamePassword);
        String credentials[] = usernamePassword.split(":");
        return new UserTemp(credentials[0], credentials[1]);
    }

    /**
     * 解析请求过来的用户
     */
    private class UserTemp {
        private String username;
        private String password;

        public UserTemp(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
