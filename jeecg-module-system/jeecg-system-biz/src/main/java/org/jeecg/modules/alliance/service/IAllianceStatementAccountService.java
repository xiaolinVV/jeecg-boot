package org.jeecg.modules.alliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.alliance.dto.AllianceStatementAccountDTO;
import org.jeecg.modules.alliance.entity.AllianceStatementAccount;
import org.jeecg.modules.alliance.vo.AllianceStatementAccountVO;

/**
 * @Description: 加盟商对账单
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
public interface IAllianceStatementAccountService extends IService<AllianceStatementAccount> {

    IPage<AllianceStatementAccountVO> queryPageList(Page<AllianceStatementAccount> page, AllianceStatementAccountDTO allianceStatementAccountDTO);
    /**
     * 监听器调用
     * 生成每日的对账单数据
     */

     void addAllianceStatementAccount();
}
