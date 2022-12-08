package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import org.jeecg.modules.store.dto.StoreTemplateDTO;
import org.jeecg.modules.store.entity.StoreTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 运费模板
 * @Author: jeecg-boot
 * @Date:   2019-10-21
 * @Version: V1.0
 */
public interface IStoreTemplateService extends IService<StoreTemplate> {

    boolean updateIsTemplateById (StoreTemplate storeTemplate);

    List<StoreTemplateDTO> getlistTemplate(@Param("uId") String uId);

    /**
     *
     * @param goodsMap     keys:
     *                     goodId:商品id
     *                     goodSpecificationId：规格id
     *                     quantity：商品数量
     *                     price：商品价格
     * @param sysAreaId   区域id
     * @return
     */
    public BigDecimal calculateFreight(List<Map<String,Object>> goodsMap, String sysAreaId);


    /**
     * 判断商品是是否可以发货
     *
     * @param goodMap
     * @param sysAreaId
     * @return
     */
    public boolean opinionCalculate(Map<String,Object> goodMap,String sysAreaId);

    /**
     * 提交店铺订单添加运费模板信息
     * @param goodsMap
     * @param sysAreaId
     * @param orderStoreSubList
     * @return
     */

    public String addOrderStoreTemplate(List<Map<String, Object>> goodsMap, String sysAreaId, OrderStoreSubList orderStoreSubList);

    /**
     * 查询店铺运费模板信息
     * @param orderStoreSubListIdList
     * @return
     */
    List<Map<String,Object>> getStoreTemplateMaps(@Param("orderStoreSubListIdList") List<String> orderStoreSubListIdList);
}
