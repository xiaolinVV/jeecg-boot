package org.jeecg.modules.marketing.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingIntegralTaskRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.vo.MarketingIntegralTaskRecordVO;

/**
 * @Description: 积分任务记录
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface MarketingIntegralTaskRecordMapper extends BaseMapper<MarketingIntegralTaskRecord> {

    IPage<MarketingIntegralTaskRecordVO> queryPageList(Page<MarketingIntegralTaskRecordVO> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingIntegralTaskRecordVO> queryWrapper);
}
