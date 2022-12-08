package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.store.entity.StoreYards;
import org.jeecg.modules.store.mapper.StoreYardsMapper;
import org.jeecg.modules.store.service.IStoreYardsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 店铺码
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
@Service
public class StoreYardsServiceImpl extends ServiceImpl<StoreYardsMapper, StoreYards> implements IStoreYardsService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper wrapper) {
        return baseMapper.queryPageList(page,wrapper);
    }
}
