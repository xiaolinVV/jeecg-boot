package org.jeecg.modules.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.provider.dto.ProviderManageDTO;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.mapper.ProviderManageMapper;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.vo.ProviderManageVO;
import org.jeecg.modules.provider.vo.ProviderWorkbenchVO;
import org.jeecg.modules.provider.vo.TestVO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 供应商列表
 * @Author: jeecg-boot
 * @Date:   2020-01-02
 * @Version: V1.0
 */
@Service
public class ProviderManageServiceImpl extends ServiceImpl<ProviderManageMapper, ProviderManage> implements IProviderManageService {
    @Autowired
    private ISysUserService iSysUserService;
    @Override
    public IPage<ProviderManageDTO> findProviderManage(Page<ProviderManageDTO> page, ProviderManageVO providerManageVO) {
        return baseMapper.findProviderManage(page,providerManageVO);
    }

    @Override
    public List<TestVO> findProviderByRoleCode(String roleCode) {
        return baseMapper.findProviderByRoleCode(roleCode);
    }



    @Override
    public IPage<ProviderManageDTO> findProviderBalance(Page<ProviderManageDTO> page,ProviderManageVO providerManageVO) {
        return baseMapper.findProviderBalance(page, providerManageVO);
    }

    @Override
    public Result<ProviderManageVO> returnProvider() {
        Result<ProviderManageVO> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ProviderManageVO providerManageVO = baseMapper.returnProvider(userId);
        result.setSuccess(true);
        result.setResult(providerManageVO);
        return result;
    }

    @Override
    public Result<ProviderManageDTO> editProviderManage(ProviderManageVO providerManageVO) {
        Result<ProviderManageDTO> result = new Result<ProviderManageDTO>();
        ProviderManage providerManageEntity = this.getById(providerManageVO.getId());
        if (providerManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            SysUser sysUser = iSysUserService.getById(providerManageEntity.getSysUserId());
            ProviderManage providerManage = new ProviderManage();
            if (StringUtils.equals(sysUser.getAvatar(),providerManageVO.getAvatar())){
                BeanUtils.copyProperties(providerManageVO,providerManage);
            }else {
                sysUser.setAvatar(providerManageVO.getAvatar());
                BeanUtils.copyProperties(providerManageVO,providerManage);
                iSysUserService.updateById(sysUser);
            }

            boolean ok = this.updateById(providerManage);
            if (ok) {
                result.success("修改成功!");
            }else {
                result.error500("修改失败!");
            }
        }

        return result;
    }

    @Override
    public ProviderWorkbenchVO findproviderWorkbenchVO(String userId) {
        return baseMapper.findproviderWorkbenchVO(userId);
    }

    @Override
    public IPage<ProviderManageVO> queryPageList(Page<ProviderManage> page, ProviderManageDTO providerManageDTO) {
        return baseMapper.queryPageList(page,providerManageDTO);
    }
}
