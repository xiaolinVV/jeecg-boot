package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface StoreTypeMapper extends BaseMapper<StoreType> {

    IPage<StoreTypeVO> queryPageList(Page<StoreTypeVO> page,@Param("storeTypeDTO") StoreTypeDTO storeTypeDTO);

    List<StoreTypeVO> getUnderlingList(@Param("id") String id);

    List<Map<String,Object>> getStoreTypeMap(@Param("pId") String pId);

    List<Map<String,Object>> getStoreTypeListMapById(@Param("map") Map<String,Object> map);

    List<Map<String,Object>> getstoreTypeListById(@Param("id") String id);

    List<Map<String,Object>> getStoreTypeMapListOne();

    List<Map<String,String>> getStoreTypeMapListTwo(@Param("id") String id);
}
