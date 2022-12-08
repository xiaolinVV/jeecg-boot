package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.entity.StoreShouyinRecord;

import java.util.Map;

/**
 * @Description: 店铺收银记录
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
public interface IStoreShouyinRecordService extends IService<StoreShouyinRecord> {


    /**
     * 收银成功
     *
     * @param payShouyinLogId
     */
    public void success(String payShouyinLogId);


    /**
     * 统计信息
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object> statistics(Map<String,Object> paramMap);

}
