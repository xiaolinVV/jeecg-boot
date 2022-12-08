package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreStatementAccountDTO;
import org.jeecg.modules.store.entity.StoreStatementAccount;
import org.jeecg.modules.store.vo.StoreStatementAccountVO;

/**
 * @Description: 店铺对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
public interface StoreStatementAccountMapper extends BaseMapper<StoreStatementAccount> {

    IPage<StoreStatementAccountVO> queryPageList(Page<StoreStatementAccount> page,@Param("storeStatementAccountDTO") StoreStatementAccountDTO storeStatementAccountDTO);
}
