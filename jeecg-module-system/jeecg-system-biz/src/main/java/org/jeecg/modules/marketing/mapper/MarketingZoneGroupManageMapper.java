package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupManage;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
public interface MarketingZoneGroupManageMapper extends BaseMapper<MarketingZoneGroupManage> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingZoneGroupManage> queryWrapper);


    /**
     * 获取参团进度列表
     *
     * @return
     */
    public List<Map<String,Object>> numberTuxedo(@Param("marketingZoneGroupId") String marketingZoneGroupId);
}
