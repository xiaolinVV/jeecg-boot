package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.store.entity.StoreSeriesManage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 店铺-系列店铺-系列管理
 * @Author: jeecg-boot
 * @Date:   2022-07-18
 * @Version: V1.0
 */
public interface IStoreSeriesManageService extends IService<StoreSeriesManage> {


    public void settingStoreSeriesManageByparentIdIndex(Map<String,Object> objectMap);

    /**
     * 根据父类id获取数据
     *
     * @param page
     * @param parentId
     * @return
     */
    public IPage<Map<String,Object>> getStoreSeriesManageByParentId(Page<Map<String,Object>> page,String parentId);

}
