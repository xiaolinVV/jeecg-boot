package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.provider.dto.ProviderAccountCapitalDTO;
import org.jeecg.modules.provider.entity.ProviderAccountCapital;
import org.jeecg.modules.provider.entity.ProviderStatementAccount;
import org.jeecg.modules.provider.vo.ProviderAccountCapitalVO;
import org.jeecg.modules.provider.vo.ProviderWorkbenchVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-18
 * @Version: V1.0
 */
public interface ProviderAccountCapitalMapper extends BaseMapper<ProviderAccountCapital> {
    IPage<ProviderAccountCapitalDTO> getProviderAccountCapitalList(Page<ProviderAccountCapital> page, @Param("providerAccountCapitalVO") ProviderAccountCapitalVO providerAccountCapitalVO);
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     * @param month
     * @param day
     * @return
     */

    List<Map<String,Object>> findAccountCapitalSum(@Param("year")Integer year,@Param("month")Integer month,@Param("day")Integer day);

    List<ProviderAccountCapitalDTO> getEarningList(@Param("providerWorkbenchVO") ProviderWorkbenchVO providerWorkbenchVO);

    IPage<ProviderAccountCapitalVO> getProviderAccountCapitalListList(Page<ProviderAccountCapital> page,@Param("providerStatementAccount") ProviderStatementAccount providerStatementAccount);
}
