package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreTemplateDTO;
import org.jeecg.modules.store.entity.StoreTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 运费模板
 * @Author: jeecg-boot
 * @Date:   2019-10-21
 * @Version: V1.0
 */
public interface StoreTemplateMapper extends BaseMapper<StoreTemplate> {

    boolean updateIsTemplateById(String id);

    List<StoreTemplateDTO> getListStoreTemplate(@Param("uId") String uId);

    /**
     * 查询店铺运费模板信息
     * @param orderStoreSubListIdList
     * @return
     */
    List<Map<String,Object>> getStoreTemplateMaps(@Param("orderStoreSubListIdList") List<String> orderStoreSubListIdList);
}
