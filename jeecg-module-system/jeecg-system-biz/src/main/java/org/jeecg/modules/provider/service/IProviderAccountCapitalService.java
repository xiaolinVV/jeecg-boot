package org.jeecg.modules.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.provider.dto.ProviderAccountCapitalDTO;
import org.jeecg.modules.provider.entity.ProviderAccountCapital;
import org.jeecg.modules.provider.entity.ProviderStatementAccount;
import org.jeecg.modules.provider.vo.ProviderAccountCapitalVO;
import org.jeecg.modules.provider.vo.ProviderWorkbenchVO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-18
 * @Version: V1.0
 */
public interface IProviderAccountCapitalService extends IService<ProviderAccountCapital> {
    IPage<ProviderAccountCapitalDTO> getProviderAccountCapitalList(Page<ProviderAccountCapital> page, ProviderAccountCapitalVO providerAccountCapitalVO);
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     * @param month
     * @param day
     * @return
     */

    List<Map<String,Object>> findAccountCapitalSum(Integer year,Integer month,Integer day);


    ProviderWorkbenchVO getEarningList(ProviderWorkbenchVO providerWorkbenchVO) throws ParseException;

    IPage<ProviderAccountCapitalVO> getProviderAccountCapitalListList(Page<ProviderAccountCapital> page, ProviderStatementAccount providerStatementAccount);
}
