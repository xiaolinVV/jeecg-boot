package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingIntegralRecord;
import org.jeecg.modules.marketing.vo.MarketingIntegralRecordVO;

import java.util.Map;

/**
 * @Description: 积分记录
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface MarketingIntegralRecordMapper extends BaseMapper<MarketingIntegralRecord> {

    IPage<MarketingIntegralRecordVO> queryPageList(Page<MarketingIntegralRecordVO> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingIntegralRecordVO> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    IPage<Map<String,Object>> getMarketingIntegralRecordPageMap(Page<Map<String,Object>> page,@Param("memberId") String memberId);
}
