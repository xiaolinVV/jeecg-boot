package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.provider.dto.ProviderTemplateDTO;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.provider.vo.ProviderTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商运费模板
 * @Author: jeecg-boot
 * @Date: 2019-10-26
 * @Version: V1.0
 */
public interface ProviderTemplateMapper extends BaseMapper<ProviderTemplate> {
    /**
     * 运费模板集合
     *
     * @param
     * @return
     */
    List<ProviderTemplateDTO> getlistProviderTemplate(@Param("uId") String uId);

    boolean updateIsTemplateByid(String id);

    IPage<ProviderTemplateDTO> getProviderTemplateList(Page<ProviderTemplateDTO> page, @Param("providerTemplateVO") ProviderTemplateVO providerTemplateVO);

    List<Map<String, Object>> getProviderTemplateMaps(@Param("orderProviderIdList") List<String> orderProviderIdList);

    List<Map<String, Object>> getProviderTemplateMap(@Param("orderProviderId")String orderProviderId);

}
