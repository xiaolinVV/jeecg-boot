package org.jeecg.modules.store.service.impl;

import org.jeecg.modules.store.entity.StoreFunctionSet;
import org.jeecg.modules.store.mapper.StoreFunctionSetMapper;
import org.jeecg.modules.store.service.IStoreFunctionSetService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺管理-功能设置
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Service
public class StoreFunctionSetServiceImpl extends ServiceImpl<StoreFunctionSetMapper, StoreFunctionSet> implements IStoreFunctionSetService {

    @Override
    public List<Map<String, Object>> getStore(String type) {
        return baseMapper.getStore(type);
    }
}
