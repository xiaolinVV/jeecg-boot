package org.jeecg.modules.agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.dto.AgencyBalanceDTO;
import org.jeecg.modules.agency.dto.AgencyManageDTO;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.vo.AgencyBalanceVO;
import org.jeecg.modules.agency.vo.AgencyManageVO;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
public interface AgencyManageMapper extends BaseMapper<AgencyManage> {

    /**
     * 查询代理列表
     * @param page
     * @param agencyManageVO
     * @return
     */
    IPage<AgencyManageDTO> getAgencyManageList(Page<AgencyManage> page, @Param("agencyManageVO") AgencyManageVO agencyManageVO);

    /**
     * 代理下拉选择
     * @return
     */
    List<Map<String,Object >> getAgencyManageMap();

    IPage<AgencyBalanceDTO> findAgencyBalance(Page<AgencyBalanceDTO> page, @Param("agencyBalanceVO") AgencyBalanceVO agencyBalanceVO);

    AgencyManageVO getAgencyManageVO(@Param("userId") String userId);

    AgencyWorkbenchVO findAgencyWorkbenchVO(@Param("userId") String userId);

    AgencyManageVO returnAgencyInfo(@Param("userId") String userId);

    List<Map<String,Object>> findAgencyManageByPhone(@Param("phone") String phone);
}
