package org.jeecg.modules.store.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
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
public interface IStoreRechargeRecordService extends IService<StoreRechargeRecord> {
    IPage<StoreRechargeRecordDTO> getStoreRechargeRecord(Page<StoreRechargeRecord> page, StoreRechargeRecordVO storeRechargeRecordVO);

    Result<StoreRechargeRecordDTO> cashOut(JSONObject jsonObject);

    IPage<Map<String,Object>> findStoreRechargeRecordInfo(Page<StoreAccountCapital> page, HashMap<String, Object> map);

    IPage<StoreRechargeRecordVO> queryPageList(Page<StoreRechargeRecord> page, StoreRechargeRecordDTO storeRechargeRecordDTO);

    IPage<StoreRechargeRecordVO> findRechargeRecord(Page<StoreRechargeRecord> page, StoreRechargeRecordDTO storeRechargeRecordDTO);
}
