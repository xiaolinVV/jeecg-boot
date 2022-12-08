package org.jeecg.modules.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
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
public interface IProviderManageService extends IService<ProviderManage> {

    IPage<ProviderManageDTO> findProviderManage(Page<ProviderManageDTO> page, ProviderManageVO providerManageVO);

    List<TestVO>findProviderByRoleCode(String roleCode);



    IPage<ProviderManageDTO> findProviderBalance(Page<ProviderManageDTO> page,ProviderManageVO providerManageVO);

    Result<ProviderManageVO> returnProvider();

    Result<ProviderManageDTO> editProviderManage(ProviderManageVO providerManageVO);

    ProviderWorkbenchVO findproviderWorkbenchVO(String userId);

    IPage<ProviderManageVO> queryPageList(Page<ProviderManage> page, ProviderManageDTO providerManageDTO);
}
