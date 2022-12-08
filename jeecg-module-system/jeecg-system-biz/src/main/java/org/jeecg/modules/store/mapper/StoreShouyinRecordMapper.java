package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.entity.StoreShouyinRecord;

import java.util.Map;

/**
 * @Description: 店铺收银记录
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
public interface StoreShouyinRecordMapper extends BaseMapper<StoreShouyinRecord> {


    /**
     * 统计信息
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object> statistics(@Param("paramMap") Map<String,Object> paramMap);

}
