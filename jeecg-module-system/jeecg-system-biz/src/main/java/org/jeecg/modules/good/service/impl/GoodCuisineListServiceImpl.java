package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.good.entity.GoodCuisineList;
import org.jeecg.modules.good.mapper.GoodCuisineListMapper;
import org.jeecg.modules.good.service.IGoodCuisineListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 菜品列表
 * @Author: jeecg-boot
 * @Date:   2022-06-09
 * @Version: V1.0
 */
@Service
public class GoodCuisineListServiceImpl extends ServiceImpl<GoodCuisineListMapper, GoodCuisineList> implements IGoodCuisineListService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, GoodCuisineList goodCuisineList) {
        return baseMapper.queryPageList(page,goodCuisineList);
    }
}
