package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingLiveLotteryRecord;

import java.util.Map;

/**
 * @Description: 直播管理-中奖记录
 * @Author: jeecg-boot
 * @Date:   2021-09-15
 * @Version: V1.0
 */
public interface MarketingLiveLotteryRecordMapper extends BaseMapper<MarketingLiveLotteryRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingLiveLotteryRecord> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    IPage<Map<String,Object>> getMarketingLiveLotteryRecordList(Page<Map<String,Object>> page,@Param("map")Map<String, Object> map);
}
