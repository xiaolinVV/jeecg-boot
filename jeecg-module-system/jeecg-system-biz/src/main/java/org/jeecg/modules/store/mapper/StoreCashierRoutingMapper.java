package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreCashierRoutingDTO;
import org.jeecg.modules.store.entity.StoreCashierRouting;

/**
 * @Description: 店铺收银分账设置
 * @Author: jeecg-boot
 * @Date:   2022-06-06
 * @Version: V1.0
 */
public interface StoreCashierRoutingMapper extends BaseMapper<StoreCashierRouting> {


    /**
     *
     * 后台列表查询
     *
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<StoreCashierRoutingDTO> queryPageList(Page<StoreCashierRoutingDTO> page, @Param(Constants.WRAPPER) QueryWrapper wrapper);
}
