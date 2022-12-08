package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.store.entity.StoreTablesRecord;
import org.jeecg.modules.store.mapper.StoreTablesRecordMapper;
import org.jeecg.modules.store.service.IStoreTablesRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 桌台列表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Service
public class StoreTablesRecordServiceImpl extends ServiceImpl<StoreTablesRecordMapper, StoreTablesRecord> implements IStoreTablesRecordService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.queryPageList(page,paramMap);
    }
}
