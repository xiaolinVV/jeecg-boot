package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreRechargeRecordDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.vo.StoreRechargeRecordVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 店铺余额记录
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
public interface StoreRechargeRecordMapper extends BaseMapper<StoreRechargeRecord> {

    IPage<StoreRechargeRecordDTO> getStoreRechargeRecord(Page<StoreRechargeRecord> page, @Param("storeRechargeRecordVO") StoreRechargeRecordVO storeRechargeRecordVO);

    IPage<Map<String,Object>> findStoreRechargeRecordInfo(Page<StoreAccountCapital> page,@Param("map") HashMap<String, Object> map);

    IPage<StoreRechargeRecordVO> queryPageList(Page<StoreRechargeRecord> page,@Param("storeRechargeRecordDTO") StoreRechargeRecordDTO storeRechargeRecordDTO);

    IPage<StoreRechargeRecordVO> findRechargeRecord(Page<StoreRechargeRecord> page,@Param("storeRechargeRecordDTO") StoreRechargeRecordDTO storeRechargeRecordDTO);
}
