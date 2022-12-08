package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingThirdIntegralRecordService extends IService<MarketingThirdIntegralRecord> {

    /**
     * 查询第三积分记录列表
     *
     * @param page
     * @param marketingThirdIntegralRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, MarketingThirdIntegralRecordDTO marketingThirdIntegralRecordDTO);


}
