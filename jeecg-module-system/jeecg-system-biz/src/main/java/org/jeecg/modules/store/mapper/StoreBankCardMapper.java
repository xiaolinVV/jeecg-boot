package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreBankCardDTO;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.vo.StoreBankCardVO;

import java.util.List;

/**
 * @Description: 店铺银行卡
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
public interface StoreBankCardMapper extends BaseMapper<StoreBankCard> {

    List<StoreBankCardDTO> findBankCardById(@Param("id") String id);

    IPage<StoreBankCardVO> queryPageList(Page<StoreBankCardVO> page,@Param("storeBankCardDTO") StoreBankCardDTO storeBankCardDTO);
}
