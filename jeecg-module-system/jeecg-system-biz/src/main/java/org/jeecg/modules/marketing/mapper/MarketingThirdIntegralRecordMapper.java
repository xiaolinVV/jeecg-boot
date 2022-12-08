package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingThirdIntegralRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingThirdIntegralRecord;

import java.util.Map;

/**
 * @Description: 第三积分记录
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
public interface MarketingThirdIntegralRecordMapper extends BaseMapper<MarketingThirdIntegralRecord> {


    /**
     * 查询第三积分记录列表
     *
     * @param page
     * @param marketingThirdIntegralRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param("marketingThirdIntegralRecordDTO") MarketingThirdIntegralRecordDTO marketingThirdIntegralRecordDTO);


}
