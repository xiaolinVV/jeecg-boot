package org.jeecg.modules.agency.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.agency.dto.AgencyBalanceDTO;
import org.jeecg.modules.agency.dto.AgencyManageDTO;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.mapper.AgencyManageMapper;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.vo.AgencyBalanceVO;
import org.jeecg.modules.agency.vo.AgencyManageVO;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;
import org.jeecg.modules.system.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
@Service
public class AgencyManageServiceImpl extends ServiceImpl<AgencyManageMapper, AgencyManage> implements IAgencyManageService {
    @Autowired
    private ISysAreaService iSysAreaService;
    /**
     * 查询代理列表
     * @param page
     * @param agencyManageVO
     * @return
     */
    @Override
    public IPage<AgencyManageDTO> getAgencyManageList(Page<AgencyManage> page, AgencyManageVO agencyManageVO){
        return  baseMapper.getAgencyManageList(page,agencyManageVO);
    };
   public List<Map<String,Object >> getAgencyManageMap(){
       return baseMapper.getAgencyManageMap();
   }

    @Override
    public IPage<AgencyBalanceDTO> findAgencyBalance(Page<AgencyBalanceDTO> page, AgencyBalanceVO agencyBalanceVO) {
        return baseMapper.findAgencyBalance(page,agencyBalanceVO);
    }

    @Override
    public AgencyManageVO getAgencyManageVO() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        return baseMapper.getAgencyManageVO(userId);
    }

    @Override
    public AgencyWorkbenchVO findAgencyWorkbenchVO(String userId) {
        return baseMapper.findAgencyWorkbenchVO(userId);
    }

    @Override
    public AgencyManageVO returnAgencyInfo() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        return baseMapper.returnAgencyInfo(userId);
    }

    @Override
    public List<Map<String, Object>> findAgencyManageByPhone(String phone) {
        return baseMapper.findAgencyManageByPhone(phone);
    }

}
