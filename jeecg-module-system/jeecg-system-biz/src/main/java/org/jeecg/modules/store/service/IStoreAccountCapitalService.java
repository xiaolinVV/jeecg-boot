package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.dto.StoreAccountCapitalDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreStatementAccount;
import org.jeecg.modules.store.vo.StoreAccountCapitalVO;
import org.jeecg.modules.store.vo.StoreWorkbenchVO;
import org.jeecg.modules.system.vo.SysWorkbenchVO;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
public interface IStoreAccountCapitalService extends IService<StoreAccountCapital> {
    /**
     * 资金流水列表
     * @param page
     * @param storeAccountCapitalVO
     * @return
     */
    IPage<StoreAccountCapitalDTO> getStoreAccountCapitalList(Page<StoreAccountCapital> page, StoreAccountCapitalVO storeAccountCapitalVO);
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     * @param month
     * @param day
     * @return
     */
    List<Map<String,Object>> findAccountCapitalSum(Integer year,Integer month,Integer day);

    List<StoreAccountCapitalDTO> storeAccountCapitalList(StoreAccountCapitalVO storeAccountCapitalVO);

    StoreWorkbenchVO getEarningList(SysWorkbenchVO sysWorkbenchVO) throws ParseException;
    /**
     * 查询店铺 日期收入金额总和
     * @param paramMap
     * @return
     */
     BigDecimal getStoreIncomeSummation(Map<String,String> paramMap);

    IPage<Map<String,Object>> findStoreAccountCapitalInfo(Page<StoreAccountCapital> page, HashMap<String, Object> map);

    IPage<StoreAccountCapitalVO> getStoreAccountCapitalListList(Page<StoreAccountCapital> page, StoreStatementAccount storeStatementAccount);
}
