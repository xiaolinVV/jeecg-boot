package org.jeecg.modules.agency.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.dto.AgencyAccountCapitalDTO;
import org.jeecg.modules.agency.entity.AgencyStatementAccount;
import org.jeecg.modules.agency.vo.AgencyAccountCapitalVO;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 代理资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
public interface AgencyAccountCapitalMapper extends BaseMapper<AgencyAccountCapital> {
    IPage<AgencyAccountCapitalDTO> getAgencyAccountCapitalList(Page<AgencyAccountCapital> page, @Param("agencyAccountCapitalVO") AgencyAccountCapitalVO agencyAccountCapitalVO);
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     * @param month
     * @param day
     * @return
     */
    List<Map<String,Object>> findAccountCapitalSum(@Param("year")Integer year,@Param("month")Integer month,@Param("day")Integer day);

    List<AgencyAccountCapitalDTO> getEarningList(@Param("agencyWorkbenchVO") AgencyWorkbenchVO agencyWorkbenchVO);

    IPage<AgencyAccountCapitalVO> getAgencyAccountCapitalListList(Page<AgencyAccountCapital> page,@Param("agencyStatementAccount") AgencyStatementAccount agencyStatementAccount);
}
