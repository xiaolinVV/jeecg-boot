package org.jeecg.modules.provider.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
public class rightControlUtil{
    @Resource
    private ISysUserRoleService sysUserRoleService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysBaseAPI sysBaseAPI;
    /*public Result<IPage<T>>findUserByRoleCode(T t,
                                              @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                              HttpServletRequest req){
        //创建返回数据
        Result<IPage<T>> result = new Result<>();
        //获取当前登录人信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //获取登录人id
        String userId = sysUser.getId();
        //获取角色集合
        List<String> roleByUserId = sysUserRoleService.getRoleByUserId(userId);
        for (String u : roleByUserId) {
            if ("jeecg".equals(u)||"admin".equals(u)){
                QueryWrapper<t> queryWrapper = QueryGenerator.initQueryWrapper(t, req.getParameterMap());
                Page<T> page = new Page<T>(pageNo, pageSize);
                IPage<T> pageList = t.page(page, queryWrapper);
                result.setSuccess(true);
                result.setResult(pageList);
            }
        }
        return result;
    }*/

}
