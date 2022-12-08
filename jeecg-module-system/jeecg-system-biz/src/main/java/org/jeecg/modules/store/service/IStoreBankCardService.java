package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.dto.StoreBankCardDTO;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.vo.StoreBankCardVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺银行卡
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
public interface IStoreBankCardService extends IService<StoreBankCard> {

    List<StoreBankCardDTO> findBankCardById(String id);

    Result<Map<String,Object>> findStoreBankCard();

    IPage<StoreBankCardVO> queryPageList(Page<StoreBankCardVO> page, StoreBankCardDTO storeBankCardDTO);

}
