package org.jeecg.modules.agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.dto.AgencyStatementAccountDTO;
import org.jeecg.modules.agency.entity.AgencyStatementAccount;
import org.jeecg.modules.agency.vo.AgencyStatementAccountVO;

/**
 * @Description: 代理对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
public interface AgencyStatementAccountMapper extends BaseMapper<AgencyStatementAccount> {

    IPage<AgencyStatementAccountVO> queryPageList(Page<AgencyStatementAccount> page,@Param("agencyStatementAccountDTO") AgencyStatementAccountDTO agencyStatementAccountDTO);
}
