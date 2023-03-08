package org.jeecg.modules.marketing.store.giftbag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagRecord;

import java.util.Map;

/**
 * @Description: 礼包团-购买记录
 * @Author: jeecg-boot
 * @Date:   2022-11-05
 * @Version: V1.0
 */
public interface MarketingStoreGiftbagRecordMapper extends BaseMapper<MarketingStoreGiftbagRecord> {


    /**
     * 后端列表查询
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);
}
