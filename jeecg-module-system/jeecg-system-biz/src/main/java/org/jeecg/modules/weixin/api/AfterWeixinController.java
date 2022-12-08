package org.jeecg.modules.weixin.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.encryption.AesCbcUtil;
import org.jeecg.modules.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.utils.QrCodeUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 微信登陆后验证的api
 */
@RequestMapping("after/weixin")
@Controller
@Slf4j
public class AfterWeixinController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private WeixinQRUtils weixinQRUtils;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private QrCodeUtils qrCodeUtils;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

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
     * 生成会员对应地址的二维码
     * @param page
     * @param memberId
     * @return
     */
    @RequestMapping("getQrCodeByPage")
        @ResponseBody
        public Result<String> getQrCodeByPage(String page, String param,
                                              @RequestHeader(defaultValue = "") String sysUserId,
                                              @RequestAttribute(value = "memberId",required = false) String memberId){
            Result<String> result=new Result<>();
            if(StringUtils.isBlank(page)){
                result.error500("page不能为空！！！");
                return result;
            }

            String shareControl= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","share_control");
            String address="";
            if(shareControl.equals("1")){
                QueryWrapper<SysSmallcode> sysSmallcodeQueryWrapper=new QueryWrapper<>();
                sysSmallcodeQueryWrapper.eq("t_member_id",memberId);
                sysSmallcodeQueryWrapper.eq("page",page);
                sysSmallcodeQueryWrapper.eq("param",param);
                log.info("微信二维码生成参数param："+param);
                log.info("微信二维码生成参数page："+page);
                SysSmallcode sysSmallcode=iSysSmallcodeService.getOne(sysSmallcodeQueryWrapper);
                if(sysSmallcode==null) {
                    MemberList memberList = iMemberListService.getById(memberId);
                    sysSmallcode = new SysSmallcode();
                    sysSmallcode.setSysUserId(memberList.getSysUserId());

                    //判断渠道id归属渠道id
                    if (StringUtils.isNotBlank(sysUserId)) {
                        String systemSharingModel = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "system_sharing_model");
                        if (systemSharingModel.equals("1")) {
                            sysSmallcode.setSysUserId(sysUserId);
                        }
                    }

                    sysSmallcode.setTMemberId(memberList.getId());
                    sysSmallcode.setCodeType("1");
                    sysSmallcode.setPage(page);
                    sysSmallcode.setParam(param);
                    iSysSmallcodeService.save(sysSmallcode);
                    memberList.setSysSmallcodeId(sysSmallcode.getId());
                    sysSmallcode.setAddress(weixinQRUtils.getQrCodeByPage(sysSmallcode.getId(), page));
                    iSysSmallcodeService.saveOrUpdate(sysSmallcode);
                }
                address=sysSmallcode.getAddress();
            }else{
                String shareUrl= notifyUrlUtils.getBseUrl("share_url");
                address=qrCodeUtils.getMemberQrCode(shareUrl+param);
                log.info("param="+param);
            }
            log.info("二维码生成地址信息："+address);
            result.setResult(address);
            result.success("二维码地址生成成功");
            return result;
        }


    /**
     * 进行增加用户信息
     *
     * @return
     */
    @RequestMapping("submitMemberInfo")
    @ResponseBody
    public Result<Map<String, Object>> submitMemberInfo(String encryptedData,
                                                        String iv,
                                                        @RequestAttribute(value = "memberId",required = false) String memberId,
                                                        @RequestHeader(defaultValue = "") String sysUserId,
                                                        @RequestHeader(defaultValue = "") String softModel) {
        Result<Map<String, Object>> result = new Result<>();
        //参数判断
        if (StringUtils.isBlank(encryptedData)) {
            result.error500("encryptedData不能为空！！！");
            return result;
        }

        if (StringUtils.isBlank(iv)) {
            result.error500("iv不能为空！！！");
            return result;
        }
        MemberList memberList = iMemberListService.getById(memberId);

        if(memberId==null){
            return result.error500("会员不存在");
        }

        Map<String, Object> memMap = null;
        //根据openid获取用户信息
        try {
            String jsonresult = AesCbcUtil.decrypt(encryptedData, memberList.getSessionKey(), iv, "UTF-8");
            log.info(jsonresult);
            memMap = JSON.parseObject(jsonresult);
        } catch (Exception e) {
            e.printStackTrace();
            result.error500("解密出错");
            return result;
        }

        if(memMap!=null){
            //用户存在
            memberList.setNickName(memMap.get("nickName").toString());
            memberList.setHeadPortrait(memMap.get("avatarUrl").toString());
            String sex = memMap.get("gender").toString();
            if (sex.equals("0")) {
                memberList.setSex("0");
            } else if (sex.equals("1")) {
                memberList.setSex("1");
            } else if (sex.equals("2")) {
                memberList.setSex("2");
            }
            memberList.setAreaAddr(memMap.get("country").toString() + "-" + memMap.get("province").toString() + "-" + memMap.get("city").toString());
        }
        iMemberListService.saveOrUpdate(memberList);

        result.success("用户信息添加成功");
        return result;
    }
 }
