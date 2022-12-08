package org.jeecg.modules.alliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.alliance.dto.AllianceBankCardDTO;
import org.jeecg.modules.alliance.entity.AllianceBankCard;
import org.jeecg.modules.alliance.mapper.AllianceBankCardMapper;
import org.jeecg.modules.alliance.service.IAllianceBankCardService;
import org.jeecg.modules.alliance.vo.AllianceBankCardVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 加盟商银行卡
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
@Service
public class AllianceBankCardServiceImpl extends ServiceImpl<AllianceBankCardMapper, AllianceBankCard> implements IAllianceBankCardService {

    @Override
    public List<AllianceBankCardDTO> findBankCardById(String sysUserId) {
        return baseMapper.findBankCardById(sysUserId);
    }

    @Override
    public IPage<AllianceBankCardVO> queryPageList(Page<AllianceBankCardVO> page, AllianceBankCardDTO allianceBankCardDTO) {
        return baseMapper.queryPageList(page,allianceBankCardDTO);
    }
}
