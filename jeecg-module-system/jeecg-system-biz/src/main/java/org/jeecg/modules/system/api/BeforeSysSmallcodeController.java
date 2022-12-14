package org.jeecg.modules.system.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 二维码api
 */
@RequestMapping("before/sysSmallcode")
@Controller
public class BeforeSysSmallcodeController {


    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysUserService iSysUserService;


    /**
     * 根据二维码id获取信息
     * @param id
     * @return
     */
    @RequestMapping("findSysSmallcodeById")
    @ResponseBody
    public Result<Map<String,Object>>  findSysSmallcodeById(String id){
        Result<Map<String,Object>> result=new Result<>();
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }

        Map<String,Object> objectMap= Maps.newHashMap();
        SysSmallcode sysSmallcode=iSysSmallcodeService.getById(id);

        objectMap.put("sysUserId",sysSmallcode.getSysUserId());
        objectMap.put("codeType",sysSmallcode.getCodeType());
        if(sysSmallcode.getParam()==null){
            String param="";

            if(sysSmallcode.getCodeType().equals("0")){
                QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                storeManageQueryWrapper.eq("sys_user_id", sysSmallcode.getSysUserId());
                storeManageQueryWrapper.in("pay_status", "1", "2");
                if (iStoreManageService.count(storeManageQueryWrapper) > 0) {
                    StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                    param="{\"TmemberInfo\":{\"TmemberName\":\""+storeManage.getStoreName()+"\",\"TmemberHeadPortrait\":\""+storeManage.getLogoAddr()+"\"}}";
                }
            }

            if(sysSmallcode.getCodeType().equals("1")){
                MemberList memberList=iMemberListService.getById(sysSmallcode.getTMemberId());
                param="{\"TmemberInfo\":{\"TmemberName\":\""+memberList.getNickName()+"\",\"TmemberHeadPortrait\":\""+memberList.getHeadPortrait()+"\"}}";
            }


            if(sysSmallcode.getCodeType().equals("3")){
                SysUser sysUser=iSysUserService.getById(sysSmallcode.getSysUserId());
                param="{\"TmemberInfo\":{\"TmemberName\":\""+sysUser.getRealname()+"\",\"TmemberHeadPortrait\":\""+sysUser.getAvatar()+"\"}}";
              }

            objectMap.put("param", param);
        }else {
            objectMap.put("param", sysSmallcode.getParam());
        }
        result.setResult(objectMap);
        result.success("获取二维码信息成功");
        return result;
    }
}
