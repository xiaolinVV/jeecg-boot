package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.dto.StoreTypeDTO;
import org.jeecg.modules.store.entity.StoreType;
import org.jeecg.modules.store.vo.StoreTypeVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺分类
 * @Author: jeecg-boot
 * @Date:   2020-08-18
 * @Version: V1.0
 */
public interface IStoreTypeService extends IService<StoreType> {

    IPage<StoreTypeVO> queryPageList(Page<StoreTypeVO> page, StoreTypeDTO storeTypeDTO);

    List<StoreTypeVO> getUnderlingList(String id);

    List<Map<String,Object>> getStoreTypeMap(String pId);

    List<Map<String,Object>> getStoreTypeListMapById(Map<String,Object> map);

    List<Map<String,Object>> getstoreTypeListById(String id);

    List<Map<String,Object>> getStoreTypeMapListOne();

    List<Map<String,String>> getStoreTypeMapListTwo(String id);
}
