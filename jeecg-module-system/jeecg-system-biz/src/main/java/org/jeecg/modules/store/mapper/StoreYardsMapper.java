package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.entity.StoreYards;

import java.util.Map;

/**
 * @Description: 店铺码
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
public interface StoreYardsMapper extends BaseMapper<StoreYards> {

    /**
     * 后台列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper wrapper);
}
