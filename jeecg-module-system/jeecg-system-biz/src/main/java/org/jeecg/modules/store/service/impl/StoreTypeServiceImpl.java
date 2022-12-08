package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.store.dto.StoreTypeDTO;
import org.jeecg.modules.store.entity.StoreType;
import org.jeecg.modules.store.mapper.StoreTypeMapper;
import org.jeecg.modules.store.service.IStoreTypeService;
import org.jeecg.modules.store.vo.StoreTypeVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺分类
 * @Author: jeecg-boot
 * @Date:   2020-08-18
 * @Version: V1.0
 */
@Service
public class StoreTypeServiceImpl extends ServiceImpl<StoreTypeMapper, StoreType> implements IStoreTypeService {

    @Override
    public IPage<StoreTypeVO> queryPageList(Page<StoreTypeVO> page, StoreTypeDTO storeTypeDTO) {
        return baseMapper.queryPageList(page,storeTypeDTO);
    }

    @Override
    public List<StoreTypeVO> getUnderlingList(String id) {
        return baseMapper.getUnderlingList(id);
    }

    @Override
    public List<Map<String, Object>> getStoreTypeMap(String pId) {
        return baseMapper.getStoreTypeMap(pId);
    }

    @Override
    public List<Map<String, Object>> getStoreTypeListMapById(Map<String,Object> map) {
        return baseMapper.getStoreTypeListMapById(map);
    }

    @Override
    public List<Map<String, Object>> getstoreTypeListById(String id) {
        return baseMapper.getstoreTypeListById(id);
    }

    @Override
    public List<Map<String, Object>> getStoreTypeMapListOne() {
        return baseMapper.getStoreTypeMapListOne();
    }

    @Override
    public List<Map<String, String>> getStoreTypeMapListTwo(String id) {
        return baseMapper.getStoreTypeMapListTwo(id);
    }

}
