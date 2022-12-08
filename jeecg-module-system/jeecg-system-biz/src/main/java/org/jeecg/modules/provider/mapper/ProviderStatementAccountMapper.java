package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.provider.dto.ProviderStatementAccountDTO;
import org.jeecg.modules.provider.entity.ProviderStatementAccount;
import org.jeecg.modules.provider.vo.ProviderStatementAccountVO;

/**
 * @Description: 供应商对账单
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
public interface ProviderStatementAccountMapper extends BaseMapper<ProviderStatementAccount> {

    IPage<ProviderStatementAccountVO> queryPageList(Page<ProviderStatementAccount> page,@Param("providerStatementAccountDTO") ProviderStatementAccountDTO providerStatementAccountDTO);
}
