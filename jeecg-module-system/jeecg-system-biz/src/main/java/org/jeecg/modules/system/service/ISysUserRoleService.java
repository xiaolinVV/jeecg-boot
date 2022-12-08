package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserRole;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 通过用户id查询出用户角色编码
     * @param userId
     * @return
     */
    List<String> getRoleByUserId(@Param("userId")String userId);
}
