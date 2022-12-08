package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.store.dto.StoreSeriesListDTO;
import org.jeecg.modules.store.entity.StoreSeriesList;
import org.jeecg.modules.store.mapper.StoreSeriesListMapper;
import org.jeecg.modules.store.service.IStoreSeriesListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 系列店铺
 * @Author: jeecg-boot
 * @Date:   2022-07-18
 * @Version: V1.0
 */
@Service
public class StoreSeriesListServiceImpl extends ServiceImpl<StoreSeriesListMapper, StoreSeriesList> implements IStoreSeriesListService {

    @Override
    public IPage<StoreSeriesListDTO> queryPageList(Page<StoreSeriesListDTO> page, QueryWrapper wrapper) {
        return baseMapper.queryPageList(page,wrapper);
    }

    @Override
    public IPage<Map<String, Object>> getStoreSeriesListByStoreManageId(Page<Map<String, Object>> page, String storeSeriesManageId) {
        return baseMapper.getStoreSeriesListByStoreManageId(page,storeSeriesManageId);
    }
}
