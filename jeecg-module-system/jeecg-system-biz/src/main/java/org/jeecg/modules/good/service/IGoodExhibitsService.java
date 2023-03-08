package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.dto.GoodExhibitsDTO;
import org.jeecg.modules.good.entity.GoodExhibits;

/**
 * @Description: 展品列表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
public interface IGoodExhibitsService extends IService<GoodExhibits> {


    /**
     * 后台列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<GoodExhibitsDTO> queryPageList(Page<GoodExhibitsDTO> page,QueryWrapper wrapper);
}
