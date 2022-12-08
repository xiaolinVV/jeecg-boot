package org.jeecg.modules.weixin.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 微信登陆后验证的api
 */
@RequestMapping("back/weixin")
@Controller
@Slf4j
public class BackWeixinController {


    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private WeixinQRUtils weixinQRUtils;
    /**
     * 验证token
     * @return
     */
    @RequestMapping("checkToken")
    @ResponseBody
        public Result<String> checkToken(){
            Result<String> result=new Result<>();
            result.success("token验证成功");
            return result;
        }


    /**
     * 生成店铺会员端对应地址的二维码
     * @param page
     * @param request
     * @return
     */
    @RequestMapping("getQrCodeByPage")
        @ResponseBody
        public Result<String> getQrCodeByPage(String page, String param,HttpServletRequest request){
            Result<String> result=new Result<>();
            if(StringUtils.isBlank(page)){
                result.error500("page不能为空！！！");
                return result;
            }
            String sysUserId=request.getAttribute("sysUserId").toString();
                  QueryWrapper<SysSmallcode> sysSmallcodeQueryWrapper=new QueryWrapper<>();
            sysSmallcodeQueryWrapper.eq("sys_user_id",sysUserId);
            sysSmallcodeQueryWrapper.eq("page",page);
            sysSmallcodeQueryWrapper.eq("param",param);
            log.info("微信二维码生成参数param："+param);
            log.info("微信二维码生成参数page："+page);
            SysSmallcode sysSmallcode=iSysSmallcodeService.getOne(sysSmallcodeQueryWrapper);
            if(sysSmallcode==null) {
                    sysSmallcode = new SysSmallcode();
                    sysSmallcode.setDelFlag("0");
                    sysSmallcode.setSysUserId(sysUserId);
                    sysSmallcode.setCodeType("0");
                    sysSmallcode.setPage(page);
                    sysSmallcode.setParam(param);
                    iSysSmallcodeService.save(sysSmallcode);
                    sysSmallcode.setAddress(weixinQRUtils.getQrCodeByPage(sysSmallcode.getId(),page));
                    iSysSmallcodeService.saveOrUpdate(sysSmallcode);
            }
            result.setResult(sysSmallcode.getAddress());
            result.success("二维码地址生成成功");
            return result;
        }


    /**
     * 生成店铺店铺端对应地址的二维码
     * @param page
     * @param request
     * @return
     */
    @RequestMapping("getCommercialQrCodeByPage")
    @ResponseBody
    public Result<String> getCommercialQrCodeByPage(String page, String param,HttpServletRequest request){
        Result<String> result=new Result<>();
        if(StringUtils.isBlank(page)){
            result.error500("page不能为空！！！");
            return result;
        }
        String sysUserId=request.getAttribute("sysUserId").toString();
        QueryWrapper<SysSmallcode> sysSmallcodeQueryWrapper=new QueryWrapper<>();
        sysSmallcodeQueryWrapper.eq("sys_user_id",sysUserId);
        sysSmallcodeQueryWrapper.eq("page",page);
        sysSmallcodeQueryWrapper.eq("param",param);
        log.info("微信二维码生成参数param："+param);
        log.info("微信二维码生成参数page："+page);
        SysSmallcode sysSmallcode=iSysSmallcodeService.getOne(sysSmallcodeQueryWrapper);
        if(sysSmallcode==null) {
            sysSmallcode = new SysSmallcode();
            sysSmallcode.setDelFlag("0");
            sysSmallcode.setSysUserId(sysUserId);
            sysSmallcode.setCodeType("0");
            sysSmallcode.setSoftModel("1");
            sysSmallcode.setPage(page);
            sysSmallcode.setParam(param);
            iSysSmallcodeService.save(sysSmallcode);
            sysSmallcode.setAddress(weixinQRUtils.getCommercialQrCodeByPage(sysSmallcode.getId(),page));
            iSysSmallcodeService.saveOrUpdate(sysSmallcode);
        }
        result.setResult(sysSmallcode.getAddress());
        result.success("二维码地址生成成功");
        return result;
    }
 }
