package org.jeecg.modules.agency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.agency.dto.AgencyAccountCapitalDTO;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.agency.entity.AgencyStatementAccount;
import org.jeecg.modules.agency.vo.AgencyAccountCapitalVO;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 代理资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
public interface IAgencyAccountCapitalService extends IService<AgencyAccountCapital> {
   IPage<AgencyAccountCapitalDTO> getAgencyAccountCapitalList(Page<AgencyAccountCapital> page, AgencyAccountCapitalVO agencyAccountCapitalVO);
   /**
    * 根据日期查询，返回支出收入 金额跟交易笔数
    * @param year
    * @param month
    * @param day
    * @return
    */
   List<Map<String,Object>> findAccountCapitalSum(Integer year,Integer month,Integer day);

    AgencyWorkbenchVO getEarningList(AgencyWorkbenchVO agencyWorkbenchVO) throws ParseException;

    IPage<AgencyAccountCapitalVO> getAgencyAccountCapitalListList(Page<AgencyAccountCapital> page, AgencyStatementAccount agencyStatementAccount);
}
