package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreSeriesListDTO;
import org.jeecg.modules.store.entity.StoreSeriesList;

import java.util.Map;

/**
 * @Description: 系列店铺
 * @Author: jeecg-boot
 * @Date:   2022-07-18
 * @Version: V1.0
 */
public interface StoreSeriesListMapper extends BaseMapper<StoreSeriesList> {

    /**
     * 后端列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<StoreSeriesListDTO> queryPageList(Page<StoreSeriesListDTO> page,@Param(Constants.WRAPPER) QueryWrapper wrapper);


    /**
     * 根据系列id获取店铺信息
     * @param page
     * @param storeSeriesManageId
     * @return
     */
    public IPage<Map<String,Object>> getStoreSeriesListByStoreManageId(Page<Map<String,Object>> page,@Param("storeSeriesManageId") String storeSeriesManageId);
}
