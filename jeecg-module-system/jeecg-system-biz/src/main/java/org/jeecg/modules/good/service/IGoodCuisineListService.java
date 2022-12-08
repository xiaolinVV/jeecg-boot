package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.entity.GoodCuisineList;

import java.util.Map;

/**
 * @Description: 菜品列表
 * @Author: jeecg-boot
 * @Date:   2022-06-09
 * @Version: V1.0
 */
public interface IGoodCuisineListService extends IService<GoodCuisineList> {


    /**
     * 后台分页查询
     *
     * @param page
     * @param goodCuisineList
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,GoodCuisineList goodCuisineList);

}
