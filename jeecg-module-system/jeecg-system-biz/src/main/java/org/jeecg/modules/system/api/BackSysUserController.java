package org.jeecg.modules.system.api;


import org.jeecg.common.api.vo.Result;
import org.jeecg.config.jwt.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 店铺登录后api
 */

@RequestMapping("back/sysUser")
@Controller
public class BackSysUserController {


    @Autowired
    private TokenManager tokenManager;


    /**\
     * 退出
     * @param request
     * @return
     */
    @RequestMapping("logout")
    @ResponseBody
    public Result<String> logout(HttpServletRequest request,@RequestHeader("softModel") String softModel){
        Result<String> result=new Result<>();
        String sysUserId=request.getAttribute("sysUserId").toString();

        tokenManager.deleteToken(sysUserId,softModel);

        result.success("退出成功");
        return result;
    }


}
