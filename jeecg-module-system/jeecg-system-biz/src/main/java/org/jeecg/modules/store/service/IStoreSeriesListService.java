package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.dto.StoreSeriesListDTO;
import org.jeecg.modules.store.entity.StoreSeriesList;

import java.util.Map;

/**
 * @Description: 系列店铺
 * @Author: jeecg-boot
 * @Date:   2022-07-18
 * @Version: V1.0
 */
public interface IStoreSeriesListService extends IService<StoreSeriesList> {

    /**
     * 后端列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<StoreSeriesListDTO> queryPageList(Page<StoreSeriesListDTO> page,QueryWrapper wrapper);

    /**
     * 根据系列id获取店铺信息
     * @param page
     * @param storeSeriesManageId
     * @return
     */
    public IPage<Map<String,Object>> getStoreSeriesListByStoreManageId(Page<Map<String,Object>> page,String storeSeriesManageId);
}
