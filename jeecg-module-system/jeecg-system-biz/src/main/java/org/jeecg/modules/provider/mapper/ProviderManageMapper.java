package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.provider.dto.ProviderManageDTO;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.vo.ProviderManageVO;
import org.jeecg.modules.provider.vo.ProviderWorkbenchVO;
import org.jeecg.modules.provider.vo.TestVO;

import java.util.List;

/**
 * @Description: 供应商列表
 * @Author: jeecg-boot
 * @Date:   2020-01-02
 * @Version: V1.0
 */
public interface ProviderManageMapper extends BaseMapper<ProviderManage> {

    IPage<ProviderManageDTO> findProviderManage(Page<ProviderManageDTO> page, @Param("providerManageVO") ProviderManageVO providerManageVO);

    List<TestVO> findProviderByRoleCode(@Param("roleCode") String roleCode);

    IPage<ProviderManageDTO> findProviderBalance(Page<ProviderManageDTO> page,@Param("providerManageVO") ProviderManageVO providerManageVO);

    ProviderManageVO returnProvider(@Param("userId") String userId);

    ProviderWorkbenchVO findproviderWorkbenchVO(@Param("userId") String userId);

    IPage<ProviderManageVO> queryPageList(Page<ProviderManage> page,@Param("providerManageDTO") ProviderManageDTO providerManageDTO);
}
