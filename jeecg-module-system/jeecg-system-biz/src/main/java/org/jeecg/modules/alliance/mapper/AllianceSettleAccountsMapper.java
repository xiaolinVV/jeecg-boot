package org.jeecg.modules.alliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.alliance.dto.AllianceSettleAccountsDTO;
import org.jeecg.modules.alliance.entity.AllianceSettleAccounts;
import org.jeecg.modules.alliance.vo.AllianceSettleAccountsVO;

/**
 * @Description: 加盟商提现
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
public interface AllianceSettleAccountsMapper extends BaseMapper<AllianceSettleAccounts> {

    IPage<AllianceSettleAccountsVO> queryPageList(Page<AllianceSettleAccounts> page,@Param("allianceSettleAccountsDTO") AllianceSettleAccountsDTO allianceSettleAccountsDTO);
}
