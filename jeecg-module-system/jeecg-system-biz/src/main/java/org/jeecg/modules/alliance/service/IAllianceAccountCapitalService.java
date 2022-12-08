package org.jeecg.modules.alliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.alliance.dto.AllianceAccountCapitalDTO;
import org.jeecg.modules.alliance.entity.AllianceAccountCapital;
import org.jeecg.modules.alliance.entity.AllianceStatementAccount;
import org.jeecg.modules.alliance.vo.AllianceAccountCapitalVO;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟商资金管理
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
public interface IAllianceAccountCapitalService extends IService<AllianceAccountCapital> {

    IPage<AllianceAccountCapitalVO> queryPageList(Page<AllianceAccountCapital> page, AllianceAccountCapitalDTO allianceAccountCapitalDTO);

    AllianceWorkbenchVO getEarningList(AllianceWorkbenchVO workbench) throws ParseException;
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     * @param month
     * @param day
     * @return
     */
    List<Map<String,Object>> findAccountCapitalSum(Integer year, Integer month, Integer day);


    IPage<AllianceAccountCapitalVO> findAccountCapitalById(Page<AgencyAccountCapital> page, AllianceStatementAccount allianceStatementAccount);
}
