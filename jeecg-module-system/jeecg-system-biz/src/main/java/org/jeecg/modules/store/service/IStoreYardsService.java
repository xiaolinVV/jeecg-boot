package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.entity.StoreYards;

import java.util.Map;

/**
 * @Description: 店铺码
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
public interface IStoreYardsService extends IService<StoreYards> {

    /**
     * 后台列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,QueryWrapper wrapper);

}
