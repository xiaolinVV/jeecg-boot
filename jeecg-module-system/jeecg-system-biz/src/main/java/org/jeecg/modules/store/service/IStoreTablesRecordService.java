package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.entity.StoreTablesRecord;

import java.util.Map;

/**
 * @Description: 桌台列表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
public interface IStoreTablesRecordService extends IService<StoreTablesRecord> {


    /**
     * 后台列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,Map<String,Object> paramMap);

}
