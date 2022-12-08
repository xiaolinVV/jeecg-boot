package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingRushType;

import java.util.Map;

/**
 * @Description: 抢购活动-分类管理
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface MarketingRushTypeMapper extends BaseMapper<MarketingRushType> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingRushType> queryWrapper);
}
