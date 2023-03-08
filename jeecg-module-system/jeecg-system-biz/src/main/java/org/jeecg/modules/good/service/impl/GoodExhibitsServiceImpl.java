package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.good.dto.GoodExhibitsDTO;
import org.jeecg.modules.good.entity.GoodExhibits;
import org.jeecg.modules.good.mapper.GoodExhibitsMapper;
import org.jeecg.modules.good.service.IGoodExhibitsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 展品列表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Service
public class GoodExhibitsServiceImpl extends ServiceImpl<GoodExhibitsMapper, GoodExhibits> implements IGoodExhibitsService {

    @Override
    public IPage<GoodExhibitsDTO> queryPageList(Page<GoodExhibitsDTO> page, QueryWrapper wrapper) {
        return baseMapper.queryPageList(page,wrapper);
    }
}
