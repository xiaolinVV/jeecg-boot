package org.jeecg.modules.agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.dto.AgencyBankCardDTO;
import org.jeecg.modules.agency.entity.AgencyBankCard;
import org.jeecg.modules.agency.vo.AgencyBankCardVO;

import java.util.List;

/**
 * @Description: 代理银行卡
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
public interface AgencyBankCardMapper extends BaseMapper<AgencyBankCard> {

    List<AgencyBankCardDTO> findBankCardById(@Param("sysUserId") String sysUserId);

    IPage<AgencyBankCardVO> queryPageList(Page<AgencyBankCardVO> page,@Param("agencyBankCardDTO") AgencyBankCardDTO agencyBankCardDTO);
}
