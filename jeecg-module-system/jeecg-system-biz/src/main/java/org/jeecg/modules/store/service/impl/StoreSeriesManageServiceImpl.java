package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.jeecg.modules.store.entity.StoreSeriesManage;
import org.jeecg.modules.store.mapper.StoreSeriesManageMapper;
import org.jeecg.modules.store.service.IStoreSeriesListService;
import org.jeecg.modules.store.service.IStoreSeriesManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺-系列店铺-系列管理
 * @Author: jeecg-boot
 * @Date:   2022-07-18
 * @Version: V1.0
 */
@Service
public class StoreSeriesManageServiceImpl extends ServiceImpl<StoreSeriesManageMapper, StoreSeriesManage> implements IStoreSeriesManageService {

    @Autowired
    private IStoreSeriesListService iStoreSeriesListService;

    @Override
    public void settingStoreSeriesManageByparentIdIndex(Map<String, Object> objectMap) {
        List<StoreSeriesManage>  storeSeriesManages=this.list(new LambdaQueryWrapper<StoreSeriesManage>()
                .eq(StoreSeriesManage::getParentId,"0")
                .orderByAsc(StoreSeriesManage::getSort));
        objectMap.put("storeSeriesManageList",storeSeriesManages);
        List<List<Map<String,Object>>> storeSeriesManagesStores= Lists.newArrayList();
        for (StoreSeriesManage storeSeriesManage:storeSeriesManages) {
            storeSeriesManagesStores.add(iStoreSeriesListService.getStoreSeriesListByStoreManageId(new Page<>(1,3),storeSeriesManage.getId()).getRecords());
        }
        objectMap.put("storeSeriesManagesStores",storeSeriesManagesStores);
    }

    @Override
    public IPage<Map<String, Object>> getStoreSeriesManageByParentId(Page<Map<String, Object>> page, String parentId) {
        return baseMapper.getStoreSeriesManageByParentId(page,parentId);
    }
}
