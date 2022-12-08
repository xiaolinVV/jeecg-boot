package org.jeecg.modules.alliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.alliance.dto.AllianceSettleAccountsDTO;
import org.jeecg.modules.alliance.entity.AllianceBankCard;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.entity.AllianceSettleAccounts;
import org.jeecg.modules.alliance.vo.AllianceSettleAccountsVO;

import java.math.BigDecimal;

/**
 * @Description: 加盟商提现
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
public interface IAllianceSettleAccountsService extends IService<AllianceSettleAccounts> {

    IPage<AllianceSettleAccountsVO> queryPageList(Page<AllianceSettleAccounts> page, AllianceSettleAccountsDTO allianceSettleAccountsDTO);

    void cashOut(AllianceBankCard allianceBankCard, AllianceManage allianceManage, BigDecimal amount, String remark);

    void audit(AllianceSettleAccounts allianceSettleAccounts);

    void remit(AllianceSettleAccounts allianceSettleAccounts);
}
