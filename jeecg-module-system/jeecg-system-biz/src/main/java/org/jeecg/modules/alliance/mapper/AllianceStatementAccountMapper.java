package org.jeecg.modules.alliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.alliance.dto.AllianceStatementAccountDTO;
import org.jeecg.modules.alliance.entity.AllianceStatementAccount;
import org.jeecg.modules.alliance.vo.AllianceStatementAccountVO;

/**
 * @Description: 加盟商对账单
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
public interface AllianceStatementAccountMapper extends BaseMapper<AllianceStatementAccount> {

    IPage<AllianceStatementAccountVO> queryPageList(Page<AllianceStatementAccount> page,@Param("allianceStatementAccountDTO") AllianceStatementAccountDTO allianceStatementAccountDTO);
}
