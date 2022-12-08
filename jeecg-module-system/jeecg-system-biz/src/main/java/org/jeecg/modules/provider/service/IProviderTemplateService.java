package org.jeecg.modules.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.provider.dto.ProviderTemplateDTO;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.provider.vo.ProviderTemplateVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商运费模板
 * @Author: jeecg-boot
 * @Date:   2019-10-26
 * @Version: V1.0
 */
public interface IProviderTemplateService extends IService<ProviderTemplate> {

    /**
     * 运费模板集合
     * @param
     * @return
     */
    List<ProviderTemplateDTO> getlistProviderTemplate(String uId);

    /**
     * 判断运费模板唯一
     * @param providerTemplate
     * @return
     */
    boolean updateIsTemplate(ProviderTemplate providerTemplate);

    /**
     * 根据userId查询对应的角色
     * @param userId
     * @return
     */
    ProviderTemplate findRoleByUserId(String userId);


    IPage<ProviderTemplateDTO> getProviderTemplateList(Page<ProviderTemplateDTO> page, ProviderTemplateVO providerTemplateVO);


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
    public BigDecimal calculateFreight(List<Map<String,Object>> goodsMap,String sysAreaId);


    /**
     * 判断商品是是否可以发货
     *
     * @param goodMap
     * @param sysAreaId
     * @return
     */
    public boolean opinionCalculate(Map<String,Object> goodMap,String sysAreaId);

    /**
     * 获取订单运费模板信息
     *
     * @param orderProviderIdList
     * @return
     */
    List<Map<String,Object>> getProviderTemplateMaps(@Param("orderProviderIdList") List<String> orderProviderIdList);

    /**
     * 提交订单添加运费模板信息
     * @param goodsMap
     * @param sysAreaId
     * @param orderProviderList
     * @return
     */

    public String addOrderTemplate(List<Map<String, Object>> goodsMap, String sysAreaId, OrderProviderList orderProviderList);
}
