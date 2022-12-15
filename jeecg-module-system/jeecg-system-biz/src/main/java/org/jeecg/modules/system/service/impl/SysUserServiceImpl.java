package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.RoleIndexConfigEnum;
import org.jeecg.common.desensitization.annotation.SensitiveEncode;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;
import org.jeecg.modules.alliance.service.IAllianceAccountCapitalService;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.member.dto.MemberListDTO;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.service.IProviderAccountCapitalService;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.vo.ProviderWorkbenchVO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.vo.StoreWorkbenchVO;
import org.jeecg.modules.system.dto.SysUserDTO;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.service.ISysBackSettingService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysUserDepVo;
import org.jeecg.modules.system.vo.SysWorkbenchVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author: scott
 * @Date: 2018-12-20
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SysUserDepartMapper sysUserDepartMapper;
	@Autowired
	private SysDepartMapper sysDepartMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysDepartRoleUserMapper departRoleUserMapper;
	@Autowired
	private SysDepartRoleMapper sysDepartRoleMapper;
	@Resource
	private BaseCommonService baseCommonService;
	@Autowired
	private SysThirdAccountMapper sysThirdAccountMapper;
	@Autowired
	ThirdAppWechatEnterpriseServiceImpl wechatEnterpriseService;
	@Autowired
	ThirdAppDingtalkServiceImpl dingtalkService;
	@Autowired
	SysRoleIndexMapper sysRoleIndexMapper;

	@Autowired
	@Lazy
	private IStoreManageService iStoreManageService;
	@Autowired
	@Lazy
	private IProviderManageService iProviderManageService;
	@Autowired
	@Lazy
	private ISysBackSettingService iSysBackSettingService;
	@Autowired
	@Lazy
	private IAgencyManageService iAgencyManageService;
	@Autowired
	@Lazy
	private IStoreAccountCapitalService iStoreAccountCapitalService;
	@Autowired
	@Lazy
	private IProviderAccountCapitalService iProviderAccountCapitalService;
	@Autowired
	@Lazy
	private IAgencyAccountCapitalService iAgencyAccountCapitalService;
	@Autowired
	@Lazy
	private IMemberListService iMemberListService;
	@Autowired
	@Lazy
	private IAllianceManageService iAllianceManageService;
	@Autowired
	@Lazy
	private IAllianceAccountCapitalService iAllianceAccountCapitalService;

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
        SysUser user = userMapper.getUserByName(username);
        String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            return Result.error("旧密码输入错误!");
        }
        if (oConvertUtils.isEmpty(newpassword)) {
            return Result.error("新密码不允许为空!");
        }
        if (!newpassword.equals(confirmpassword)) {
            return Result.error("两次输入密码不一致!");
        }
        String password = PasswordUtil.encrypt(username, newpassword, user.getSalt());
        this.userMapper.update(new SysUser().setPassword(password), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        return Result.ok("密码重置成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> changePassword(SysUser sysUser) {
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setPassword(passwordEncode);
        this.userMapper.updateById(sysUser);
        return Result.ok("密码修改成功!");
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteUser(String userId) {
		//1.删除用户
		this.removeById(userId);
		return false;
	}

	@Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteBatchUsers(String userIds) {
		//1.删除用户
		this.removeByIds(Arrays.asList(userIds.split(",")));
		return false;
	}

	@Override
	public SysUser getUserByName(String username) {
		return userMapper.getUserByName(username);
	}
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUserWithRole(SysUser user, String roles) {
		this.save(user);
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}

	@Override
	@CacheEvict(value= {CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public void editUserWithRole(SysUser user, String roles) {
		this.updateById(user);
		//先删后加
		sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}


	@Override
	public List<String> getRole(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}

	/**
	 * 获取动态首页路由配置
	 * @param username
	 * @param version
	 * @return
	 */
	@Override
	public SysRoleIndex getDynamicIndexByUserRole(String username,String version) {
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		String componentUrl = RoleIndexConfigEnum.getIndexByRoles(roles);
		SysRoleIndex roleIndex = new SysRoleIndex(componentUrl);
		//只有 X-Version=v3 的时候，才读取sys_role_index表获取角色首页配置
		if (oConvertUtils.isNotEmpty(version) && roles!=null && roles.size()>0) {
			LambdaQueryWrapper<SysRoleIndex> routeIndexQuery = new LambdaQueryWrapper();
			//用户所有角色
			routeIndexQuery.in(SysRoleIndex::getRoleCode, roles);
			//角色首页状态0：未开启  1：开启
			routeIndexQuery.eq(SysRoleIndex::getStatus, CommonConstant.STATUS_1);
			//优先级正序排序
			routeIndexQuery.orderByAsc(SysRoleIndex::getPriority);
			List<SysRoleIndex> list = sysRoleIndexMapper.selectList(routeIndexQuery);
			if (null != list && list.size() > 0) {
				roleIndex = list.get(0);
			}
		}
		
		//如果componentUrl为空，则返回空
		if(oConvertUtils.isEmpty(roleIndex.getComponent())){
			return null;
		}
		return roleIndex;
	}

	/**
	 * 通过用户名获取用户角色集合
	 * @param username 用户名
     * @return 角色集合
	 */
	@Override
	public Set<String> getUserRolesSet(String username) {
		// 查询用户拥有的角色集合
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
		return new HashSet<>(roles);
	}

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	@Override
	public Set<String> getUserPermissionsSet(String username) {
		Set<String> permissionSet = new HashSet<>();
		List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
		for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
			if (oConvertUtils.isNotEmpty(po.getPerms())) {
				permissionSet.add(po.getPerms());
			}
		}
		log.info("-------通过数据库读取用户拥有的权限Perms------username： "+ username+",Perms size: "+ (permissionSet==null?0:permissionSet.size()) );
		return permissionSet;
	}

	/**
	 * 升级SpringBoot2.6.6,不允许循环依赖
	 * @author:qinfeng
	 * @update: 2022-04-07
	 * @param username
	 * @return
	 */
	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		info.setOneDepart(true);
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}

		//查询用户信息
		SysUser sysUser = userMapper.getUserByName(username);
		if(sysUser!=null) {
			info.setSysUserCode(sysUser.getUsername());
			info.setSysUserName(sysUser.getRealname());
			info.setSysOrgCode(sysUser.getOrgCode());
		}
		
		//多部门支持in查询
		List<SysDepart> list = sysDepartMapper.queryUserDeparts(sysUser.getId());
		List<String> sysMultiOrgCode = new ArrayList<String>();
		if(list==null || list.size()==0) {
			//当前用户无部门
			//sysMultiOrgCode.add("0");
		}else if(list.size()==1) {
			sysMultiOrgCode.add(list.get(0).getOrgCode());
		}else {
			info.setOneDepart(false);
			for (SysDepart dpt : list) {
				sysMultiOrgCode.add(dpt.getOrgCode());
			}
		}
		info.setSysMultiOrgCode(sysMultiOrgCode);
		
		return info;
	}

    /**
     * 根据部门Id查询
     * @param page
     * @param departId 部门id
     * @param username 用户账户名称
     * @return
     */
	@Override
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId,String username) {
		return userMapper.getUserByDepId(page, departId,username);
	}

	@Override
	public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username) {
		return userMapper.getUserByDepIds(page, departIds,username);
	}

	@Override
	public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
		List<SysUserDepVo> list = this.baseMapper.getDepNamesByUserIds(userIds);

		Map<String, String> res = new HashMap(5);
		list.forEach(item -> {
					if (res.get(item.getUserId()) == null) {
						res.put(item.getUserId(), item.getDepartName());
					} else {
						res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
					}
				}
		);
		return res;
	}

	//update-begin-author:taoyan date:2022-9-13 for: VUEN-2245【漏洞】发现新漏洞待处理20220906 ----sql注入  方法没有使用，注掉
/*	@Override
	public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper) {
		LambdaQueryWrapper<SysUser> lambdaQueryWrapper = queryWrapper.lambda();

		lambdaQueryWrapper.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_0);
        lambdaQueryWrapper.inSql(SysUser::getId, "SELECT user_id FROM sys_user_depart WHERE dep_id = '" + departId + "'");

        return userMapper.selectPage(page, lambdaQueryWrapper);
	}*/
	//update-end-author:taoyan date:2022-9-13 for: VUEN-2245【漏洞】发现新漏洞待处理20220906 ----sql注入 方法没有使用，注掉

	@Override
	public IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page) {
		List<SysUserSysDepartModel> list = baseMapper.getUserByOrgCode(page, orgCode, userParams);
		Integer total = baseMapper.getUserByOrgCodeTotal(orgCode, userParams);

		IPage<SysUserSysDepartModel> result = new Page<>(page.getCurrent(), page.getSize(), total);
		result.setRecords(list);

		return result;
	}

    /**
     * 根据角色Id查询
     * @param page
     * @param roleId 角色id
     * @param username 用户账户名称
     * @return
     */
	@Override
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
		return userMapper.getUserByRoleId(page,roleId,username);
	}


	@Override
	@CacheEvict(value= {CacheConstant.SYS_USERS_CACHE}, key="#username")
	public void updateUserDepart(String username,String orgCode) {
		baseMapper.updateUserDepart(username, orgCode);
	}


	@Override
	public SysUser getUserByPhone(String phone) {
		return userMapper.getUserByPhone(phone);
	}


	@Override
	public SysUser getUserByEmail(String email) {
		return userMapper.getUserByEmail(email);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUserWithDepart(SysUser user, String selectedParts) {
//		this.save(user);  //保存角色的时候已经添加过一次了
		if(oConvertUtils.isNotEmpty(selectedParts)) {
			String[] arr = selectedParts.split(",");
			for (String deaprtId : arr) {
				SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
				sysUserDepartMapper.insert(userDeaprt);
			}
		}
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public void editUserWithDepart(SysUser user, String departs) {
        //更新角色的时候已经更新了一次了，可以再跟新一次
		this.updateById(user);
		String[] arr = {};
		if(oConvertUtils.isNotEmpty(departs)){
			arr = departs.split(",");
		}
		//查询已关联部门
		List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(userDepartList != null && userDepartList.size()>0){
			for(SysUserDepart depart : userDepartList ){
				//修改已关联部门删除部门用户角色关系
				if(!Arrays.asList(arr).contains(depart.getDepId())){
					List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(
							new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId,depart.getDepId()));
					List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
					if(roleIds != null && roleIds.size()>0){
						departRoleUserMapper.delete(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, user.getId())
								.in(SysDepartRoleUser::getDroleId,roleIds));
					}
				}
			}
		}
		//先删后加
		sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(departs)) {
			for (String departId : arr) {
				SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
				sysUserDepartMapper.insert(userDepart);
			}
		}
	}


	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	@Override
	public Result<?> checkUserIsEffective(SysUser sysUser) {
		Result<?> result = new Result<Object>();
		//情况1：根据用户信息查询，该用户不存在
		if (sysUser == null) {
			result.error500("该用户不存在，请注册");
			baseCommonService.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}
		//情况2：根据用户信息查询，该用户已注销
		//update-begin---author:王帅   Date:20200601  for：if条件永远为falsebug------------
		if (CommonConstant.DEL_FLAG_1.equals(sysUser.getDelFlag())) {
		//update-end---author:王帅   Date:20200601  for：if条件永远为falsebug------------
			baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已注销");
			return result;
		}
		//情况3：根据用户信息查询，该用户已冻结
		if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
			baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已冻结");
			return result;
		}
		return result;
	}

	@Override
	public List<SysUser> queryLogicDeleted() {
		return this.queryLogicDeleted(null);
	}

	@Override
	public List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper) {
		if (wrapper == null) {
			wrapper = new LambdaQueryWrapper<>();
		}
		wrapper.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_1);
		return userMapper.selectLogicDeleted(wrapper);
	}

	@Override
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity) {
		return userMapper.revertLogicDeleted(userIds, updateEntity) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeLogicDeleted(List<String> userIds) {
		// 1. 删除用户
		int line = userMapper.deleteLogicDeleted(userIds);
		// 2. 删除用户部门关系
		line += sysUserDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getUserId, userIds));
		//3. 删除用户角色关系
		line += sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
		//4.同步删除第三方App的用户
		try {
			dingtalkService.removeThirdAppUser(userIds);
			wechatEnterpriseService.removeThirdAppUser(userIds);
		} catch (Exception e) {
			log.error("同步删除第三方App的用户失败：", e);
		}
		//5. 删除第三方用户表（因为第4步需要用到第三方用户表，所以在他之后删）
		line += sysThirdAccountMapper.delete(new LambdaQueryWrapper<SysThirdAccount>().in(SysThirdAccount::getSysUserId, userIds));

		return line != 0;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNullPhoneEmail() {
        userMapper.updateNullByEmptyString("email");
        userMapper.updateNullByEmptyString("phone");
        return true;
    }

	@Override
	public void saveThirdUser(SysUser sysUser) {
		//保存用户
		String userid = UUIDGenerator.generate();
		sysUser.setId(userid);
		baseMapper.insert(sysUser);
		//获取第三方角色
		SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, "third_role"));
		//保存用户角色
		SysUserRole userRole = new SysUserRole();
		userRole.setRoleId(sysRole.getId());
		userRole.setUserId(userid);
		sysUserRoleMapper.insert(userRole);
	}

	@Override
	public List<SysUser> queryByDepIds(List<String> departIds, String username) {
		return userMapper.queryByDepIds(departIds,username);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUser(SysUser user, String selectedRoles, String selectedDeparts) {
		//step.1 保存用户
		this.save(user);
		//step.2 保存角色
		if(oConvertUtils.isNotEmpty(selectedRoles)) {
			String[] arr = selectedRoles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
		//step.3 保存所属部门
		if(oConvertUtils.isNotEmpty(selectedDeparts)) {
			String[] arr = selectedDeparts.split(",");
			for (String deaprtId : arr) {
				SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
				sysUserDepartMapper.insert(userDeaprt);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public void editUser(SysUser user, String roles, String departs) {
		//step.1 修改用户基础信息
		this.updateById(user);
		//step.2 修改角色
		//处理用户角色 先删后加
		sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}

		//step.3 修改部门
		String[] arr = {};
		if(oConvertUtils.isNotEmpty(departs)){
			arr = departs.split(",");
		}
		//查询已关联部门
		List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(userDepartList != null && userDepartList.size()>0){
			for(SysUserDepart depart : userDepartList ){
				//修改已关联部门删除部门用户角色关系
				if(!Arrays.asList(arr).contains(depart.getDepId())){
					List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(
							new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId,depart.getDepId()));
					List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
					if(roleIds != null && roleIds.size()>0){
						departRoleUserMapper.delete(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, user.getId())
								.in(SysDepartRoleUser::getDroleId,roleIds));
					}
				}
			}
		}
		//先删后加
		sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(departs)) {
			for (String departId : arr) {
				SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
				sysUserDepartMapper.insert(userDepart);
			}
		}
		//step.4 修改手机号和邮箱
		// 更新手机号、邮箱空字符串为 null
		userMapper.updateNullByEmptyString("email");
		userMapper.updateNullByEmptyString("phone");

	}

	@Override
	public List<String> userIdToUsername(Collection<String> userIdList) {
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(SysUser::getId, userIdList);
		List<SysUser> userList = super.list(queryWrapper);
		return userList.stream().map(SysUser::getUsername).collect(Collectors.toList());
	}

	@Override
	@Cacheable(cacheNames=CacheConstant.SYS_USERS_CACHE, key="#username")
	@SensitiveEncode
	public LoginUser getEncodeUserInfo(String username){
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.getUserByName(username);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}

	/**
	 * 根据sysUserId 查询 返回角色roleCode
	 */
	@Override
	public String getUserRoleCode(String sysUserId){
		List<String> list = userMapper.getUserRoleCode(sysUserId);
		if(list.size()>0){
			return	list.get(0);
		}
		return null;
	}

	/**
	 * 根据角色查询用户列表
	 * @param roleName
	 * @return
	 */
	@Override
	public List<SysUserDTO> getUserByRoleName(String roleName){
		return userMapper.getUserByRoleName(roleName);
	}

	/**
	 * 判断用户角色跟审核状态
	 * @param sysUserId
	 * @return
	 */
	@Override
	public Map<String, Object> getUserRoleCodeAndGoodAudit(String sysUserId) {
		String role = getUserRoleCode(sysUserId);
		Map<String, Object> map = Maps.newHashMap();
		if ("Supplier".equals(role)) {
			//供应商管理员信息
			QueryWrapper<ProviderManage> queryWrapperProviderManage = new QueryWrapper();
			queryWrapperProviderManage.eq("sys_user_id", sysUserId);
			queryWrapperProviderManage.eq("status", "1");
			ProviderManage providerManage = iProviderManageService.getOne(queryWrapperProviderManage);
			if (oConvertUtils.isNotEmpty(providerManage)) {
				map.put("roleCode", "2");
				map.put("goodAudit", providerManage.getGoodAudit());
			}

		} else if ("Merchant".equals(role)) {
			//店铺管理员信息
			QueryWrapper<StoreManage> queryWrapperProviderManage = new QueryWrapper();
			queryWrapperProviderManage.eq("sys_user_id", sysUserId);
			queryWrapperProviderManage.eq("status", "1");
			StoreManage storeManage = iStoreManageService.getOne(queryWrapperProviderManage);
			if (oConvertUtils.isNotEmpty(storeManage)) {
				map.put("roleCode", "3");
				map.put("goodAudit", storeManage.getGoodAudit());
			}
		} else {
			//平台管理员
			map.put("roleCode", "1");
			map.put("goodAudit", "1");
		}

		return map;
	}

	@Override
	public Map<String,Object> findAll(SysWorkbenchVO sysWorkbenchVO) throws ParseException {
		//获取当前登录人信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String userId = sysUser.getId();
		//查出当前登录人角色集合
		List<String> roleByUserId = sysUserRoleMapper.getRoleByUserId(userId);
		//返回给前端的数据格式
		HashMap<String, Object> map = new HashMap<>();
		//查出后台设置logo
		QueryWrapper<SysBackSetting> sysBackSettingQueryWrapper = new QueryWrapper<>();
		sysBackSettingQueryWrapper.eq("del_flag",0);
		SysBackSetting sysBackSetting = iSysBackSettingService.list(sysBackSettingQueryWrapper).get(0);
		//解析json(将多图格式转为单图)
		String s = (String) JSON.parseObject(sysBackSetting.getLoginLogo()).get("0");
		ArrayList<Map<String, Object>> memberList = new ArrayList<>();
		ArrayList<Map<String, Object>> memberSexList = new ArrayList<>();
		if (roleByUserId.contains("Merchant")){//店铺
			StoreWorkbenchVO storeWorkbenchVO = iStoreManageService.findStoreWorkbenchVO(userId);
			//判断有没有logo,没有就取后台设置logo
			if (StringUtils.isBlank(storeWorkbenchVO.getLogoAddr())){
				storeWorkbenchVO.setLogoAddr(s);
			}
			sysWorkbenchVO.setSysUserId(userId);
			sysWorkbenchVO.setId(storeWorkbenchVO.getId());
			StoreWorkbenchVO earningList = iStoreAccountCapitalService.getEarningList(sysWorkbenchVO);
			MemberListDTO storeSexSum = iMemberListService.getStoreSexSum(sysWorkbenchVO);
			storeWorkbenchVO.setMemberList(storeSexSum.getMemberList());
			storeWorkbenchVO.setMemberSexList(storeSexSum.getMemberSexList());
			storeWorkbenchVO.setEarningsList(earningList.getEarningsList());
			storeWorkbenchVO.setEarningsPatch(earningList.getEarningsPatch());
			storeWorkbenchVO.setMemberPatch(storeSexSum.getMemberPatch());
			map.put("Merchant",storeWorkbenchVO);
			return map;
		}else if (roleByUserId.contains("Supplier")){//供应商
			ProviderWorkbenchVO providerWorkbenchVO = iProviderManageService.findproviderWorkbenchVO(userId);
			if (StringUtils.isBlank(providerWorkbenchVO.getAvatar())){
				providerWorkbenchVO.setAvatar(s);
			}
			providerWorkbenchVO.setEarningTime_begin(sysWorkbenchVO.getEarningTime_begin());
			providerWorkbenchVO.setEarningTime_end(sysWorkbenchVO.getEarningTime_end());
			ProviderWorkbenchVO earningList = iProviderAccountCapitalService.getEarningList(providerWorkbenchVO);
			providerWorkbenchVO.setEarningsList(earningList.getEarningsList());
			providerWorkbenchVO.setEarningsPatch(earningList.getEarningsPatch());
			map.put("Supplier",providerWorkbenchVO);
			return map;
		}else if (roleByUserId.contains("Provincial_agents")||
				roleByUserId.contains("Municipal_agent")||
				roleByUserId.contains("County_agent")){//代理
			AgencyWorkbenchVO agencyWorkbenchVO = iAgencyManageService.findAgencyWorkbenchVO(userId);
			if (StringUtils.isBlank(agencyWorkbenchVO.getAvatar())){
				agencyWorkbenchVO.setAvatar(s);
			}
			agencyWorkbenchVO.setEarningTime_begin(sysWorkbenchVO.getEarningTime_begin());
			agencyWorkbenchVO.setEarningTime_end(sysWorkbenchVO.getEarningTime_end());
			agencyWorkbenchVO.setMemberTime_begin(sysWorkbenchVO.getMemberTime_begin());
			agencyWorkbenchVO.setMemberTime_end(sysWorkbenchVO.getMemberTime_end());
			AgencyWorkbenchVO earningList = iAgencyAccountCapitalService.getEarningList(agencyWorkbenchVO);
			AgencyWorkbenchVO agencySexSum = iMemberListService.getAgencySexSum(agencyWorkbenchVO);
			agencyWorkbenchVO.setMemberSexList(agencySexSum.getMemberSexList());
			agencyWorkbenchVO.setMemberList(agencySexSum.getMemberList());
			agencyWorkbenchVO.setMemberPatch(agencySexSum.getMemberPatch());
			agencyWorkbenchVO.setEarningsList(earningList.getEarningsList());
			agencyWorkbenchVO.setEarningsPatch(earningList.getEarningsPatch());
			map.put("agency",agencyWorkbenchVO);
			return map;
		} else if (roleByUserId.contains("Franchisee")){
			AllianceWorkbenchVO workbench = iAllianceManageService.findWorkbench(userId);
			if (StringUtils.isBlank(workbench.getAvatar())){
				workbench.setAvatar(s);
			}
			workbench.setEarningTime_begin(sysWorkbenchVO.getEarningTime_begin());
			workbench.setEarningTime_end(sysWorkbenchVO.getEarningTime_end());
			workbench.setMemberTime_begin(sysWorkbenchVO.getMemberTime_begin());
			workbench.setMemberTime_end(sysWorkbenchVO.getMemberTime_end());
			AllianceWorkbenchVO earningList = iAllianceAccountCapitalService.getEarningList(workbench);
			AllianceWorkbenchVO franchiseeSexSum = iMemberListService.getFranchiseeSexSum(workbench);
			workbench.setMemberSexList(franchiseeSexSum.getMemberSexList());
			workbench.setMemberList(franchiseeSexSum.getMemberList());
			workbench.setMemberPatch(franchiseeSexSum.getMemberPatch());
			workbench.setEarningsList(earningList.getEarningsList());
			workbench.setEarningsPatch(earningList.getEarningsPatch());
			map.put("Franchisee",workbench);
			return map;
		}else {
			SysWorkbenchVO all = baseMapper.findAll(userId).get(0);
			if (StringUtils.isBlank(all.getAvatar())){
				all.setAvatar(s);
			}
			//定义传入后台时间list
			ArrayList<String> arrayList = new ArrayList<>();
			Date earningTime_begin = sysWorkbenchVO.getEarningTime_begin();
			Date earningTime_end = sysWorkbenchVO.getEarningTime_end();
			if (oConvertUtils.isEmpty(earningTime_begin)){
				for (int i = 1; i < 31; i++) {
					//推算当前时间前一天日期
					Calendar calendar=Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_YEAR,-i);
					String s1 = calendar.get(Calendar.YEAR) + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
					arrayList.add(s1);
					if (i==1){
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						sysWorkbenchVO.setEarningTime_end(simpleDateFormat.parse(s1));
					}
					if (i==30){
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						sysWorkbenchVO.setEarningTime_begin(simpleDateFormat.parse(s1));
					}
				}

			}else {
				long day = (earningTime_end.getTime()-earningTime_begin.getTime())/(24*60*60*1000);
				for (int i = 0; i < day; i++) {
					//推算当前时间前一天日期
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(earningTime_end);
					calendar.add(Calendar.DAY_OF_YEAR,-i);
					String s1 = calendar.get(Calendar.YEAR) + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
					arrayList.add(s1);
				}
			}
			sysWorkbenchVO.setEarningTimeList(arrayList);

			List<SysWorkbenchVO> earningList = baseMapper.getEarningList(sysWorkbenchVO);

			//算出时间内的累计收益
			BigDecimal earningsPatch = earningList.stream()
					.map(SysWorkbenchVO::getTotalPrice)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			//将查出的集合转为map形式
			Map<String, BigDecimal> collect = earningList.stream().collect(Collectors.
					toMap(SysWorkbenchVO::getMyDate, SysWorkbenchVO::getTotalPrice));

			ArrayList<Map<String, Object>> maps = new ArrayList<>();

			arrayList.forEach(arr->{
				HashMap<String, Object> hashMap = new HashMap<>();
				hashMap.put("x",arr);
				hashMap.put("y",collect.getOrDefault(arr,new BigDecimal(0)));
				maps.add(hashMap);
			});
			sysWorkbenchVO.setId(userId);
			Map<String, Long> sysSexSum = iMemberListService.getSysSexSum(sysWorkbenchVO);
			Long asordinarySum = sysSexSum.get("asordinarySum");
			Long vipSum = sysSexSum.get("vipSum");
			sysSexSum.forEach((k,v)->{
				HashMap<String, Object> mapx = new HashMap<>();
				if (k.equals("asordinarySum")){
					mapx.put("item","普通会员");
					mapx.put("count",String.valueOf(v));
					memberList.add(mapx);
				}
				if (k.equals("vipSum")){
					mapx.put("item","vip");
					mapx.put("count",String.valueOf(v));
					memberList.add(mapx);
				}
				if (k.equals("memberMan")){
					mapx.put("item","男");
					mapx.put("count",String.valueOf(v));
					memberSexList.add(mapx);
				}
				if (k.equals("memberWoMan")){
					mapx.put("item","女");
					mapx.put("count",String.valueOf(v));
					memberSexList.add(mapx);
				}
				if (k.equals("memberUnknown")){
					mapx.put("item","未知");
					mapx.put("count",String.valueOf(v));
					memberSexList.add(mapx);
				}
			});
			all.setEarningsPatch(earningsPatch);
			all.setMemberPatch(asordinarySum+vipSum);
			all.setMemberList(memberList);
			all.setMemberSexList(memberSexList);
			all.setEarningsList(maps);
			map.put("admin",all);
			return map;
		}
	}

	@Override
	public SysWorkbenchVO totalSum() {
		return baseMapper.totalSum();
	}
}
