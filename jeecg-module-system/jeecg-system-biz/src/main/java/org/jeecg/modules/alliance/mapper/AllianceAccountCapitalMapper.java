package org.jeecg.modules.alliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.alliance.dto.AllianceAccountCapitalDTO;
import org.jeecg.modules.alliance.entity.AllianceAccountCapital;
import org.jeecg.modules.alliance.entity.AllianceStatementAccount;
import org.jeecg.modules.alliance.vo.AllianceAccountCapitalVO;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟商资金管理
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
public interface AllianceAccountCapitalMapper extends BaseMapper<AllianceAccountCapital> {

    IPage<AllianceAccountCapitalVO> queryPageList(Page<AllianceAccountCapital> page,@Param("allianceAccountCapitalDTO") AllianceAccountCapitalDTO allianceAccountCapitalDTO);

    List<AllianceAccountCapitalDTO> getEarningList(@Param("workbench") AllianceWorkbenchVO workbench);
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     * @param month
     * @param day
     * @return
     */
    List<Map<String,Object>> findAccountCapitalSum(@Param("year")Integer year,@Param("month")Integer month,@Param("day")Integer day);

    IPage<AllianceAccountCapitalVO> findAccountCapitalById(Page<AgencyAccountCapital> page,@Param("allianceStatementAccount") AllianceStatementAccount allianceStatementAccount);
}
