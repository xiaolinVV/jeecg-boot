package org.jeecg.modules.alliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.alliance.dto.AllianceBankCardDTO;
import org.jeecg.modules.alliance.entity.AllianceBankCard;
import org.jeecg.modules.alliance.vo.AllianceBankCardVO;

import java.util.List;

/**
 * @Description: 加盟商银行卡
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
public interface AllianceBankCardMapper extends BaseMapper<AllianceBankCard> {

    List<AllianceBankCardDTO> findBankCardById(@Param("sysUserId") String sysUserId);

    IPage<AllianceBankCardVO> queryPageList(Page<AllianceBankCardVO> page,@Param("allianceBankCardDTO") AllianceBankCardDTO allianceBankCardDTO);
}
