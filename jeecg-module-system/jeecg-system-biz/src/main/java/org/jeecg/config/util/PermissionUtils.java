package org.jeecg.config.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PermissionUtils {


    /**
     * 数据权限控制(通过userid匹配)
     *
     * @param queryWrapper
     */
    public static void accredit(QueryWrapper<?> queryWrapper) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        for (String s : roleByUserId) {
            if ("Supplier".equals(s) || "Merchant".equals(s)) {
                queryWrapper.eq("sys_user_id", userId);
            }
        }
    }


    /**
     * 数据权限控制(通过storeManageId匹配)
     * 店铺模块使用
     * @param queryWrapper
     */
    public static void storePower(QueryWrapper<?> queryWrapper) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",userId);
        IStoreManageService iStoreManageService = SpringContextUtils.getBean(IStoreManageService.class);
        StoreManage storeManage = iStoreManageService.getOne(storeManageQueryWrapper);
        if(oConvertUtils.isNotEmpty(storeManage)){
            queryWrapper.eq("store_manage_id",storeManage.getId());
        }
    }





    /**
     * 数据权限 判断是否是平台， 不是返回供应商Id
     * @param
     */
    public static String ifPlatform(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        for (String s : roleByUserId) {
            if ("Supplier".equals(s)||"Merchant".equals(s)){
                return userId;
            }
        }
        return null;
    }


    /**
     * 省市县代理数据权限控制
     *
     * @param queryWrapper
     */
    public static void accreditArea(QueryWrapper<?> queryWrapper) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        for (String s : roleByUserId) {//Provincial_agents Municipal_agent County_agent
            if ("Provincial_agents".equals(s) || "Municipal_agent".equals(s) ||  "County_agent".equals(s)) {
                queryWrapper.eq("sys_user_id", userId);
            }
        }

    }
    /**
     * 数据权限 判断是否是省市县代理， 不是返回代理UserId
     * @param
     */
    public static String ifPlatformArea(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        for (String s : roleByUserId) {
            if ("Provincial_agents".equals(s) || "Municipal_agent".equals(s) ||  "County_agent".equals(s)) {
                return userId;
            }
        }
        return null;
    }
    public static String ifAllianceRole(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        if (roleByUserId.contains("Franchisee")||roleByUserId.contains("Partner")){
            return userId;
        }else {
            return null;
        }
    }



    public static String ifStore(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        if (roleByUserId.contains("Merchant")){
            return userId;
        }else {
            return null;
        }
    }

}
