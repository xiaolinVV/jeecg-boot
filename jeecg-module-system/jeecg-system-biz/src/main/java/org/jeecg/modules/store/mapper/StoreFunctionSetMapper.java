package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.entity.StoreFunctionSet;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺管理-功能设置
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
public interface StoreFunctionSetMapper extends BaseMapper<StoreFunctionSet> {


    /**
     * 获取店铺列表
     *
     * @param type
     * @return
     */
    public List<Map<String,Object>> getStore(@Param("type") String  type);

}
