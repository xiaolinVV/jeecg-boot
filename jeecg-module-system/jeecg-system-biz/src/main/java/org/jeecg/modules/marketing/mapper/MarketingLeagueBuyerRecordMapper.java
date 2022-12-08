package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingLeagueBuyerRecord;

import java.util.Map;

/**
 * @Description: 加盟专区-购买记录
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
public interface MarketingLeagueBuyerRecordMapper extends BaseMapper<MarketingLeagueBuyerRecord> {


    /**
     * 查询购买记录
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

}
