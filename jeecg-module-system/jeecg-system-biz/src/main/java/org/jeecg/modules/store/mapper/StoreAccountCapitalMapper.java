package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreAccountCapitalDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreStatementAccount;
import org.jeecg.modules.store.vo.StoreAccountCapitalVO;
import org.jeecg.modules.system.vo.SysWorkbenchVO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
public interface StoreAccountCapitalMapper extends BaseMapper<StoreAccountCapital> {
   IPage<StoreAccountCapitalDTO>  getStoreAccountCapitalList(Page<StoreAccountCapital> page, @Param("storeAccountCapitalVO") StoreAccountCapitalVO storeAccountCapitalVO);


    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year  年
     * @param month 月
     * @param day 日
     * @return
     */
    List<Map<String,Object>> findAccountCapitalSum(@Param("year")Integer year,@Param("month")Integer month,@Param("day")Integer day);

    List<StoreAccountCapitalDTO> storeAccountCapitalList(@Param("storeAccountCapitalVO") StoreAccountCapitalVO storeAccountCapitalVO);
    StoreAccountCapitalVO testForech(@Param("storeAccountCapitalVO")StoreAccountCapitalVO storeAccountCapitalVO);

    List<StoreAccountCapitalVO> forechX(@Param("sysWorkbenchVO") SysWorkbenchVO sysWorkbenchVO);


    /**
     * 查询店铺 日期收入金额总和
     * @param paramMap
     * @return
     */
    BigDecimal getStoreIncomeSummation(@Param("paramMap") Map<String,String> paramMap);

    IPage<Map<String,Object>> findStoreAccountCapitalInfo(Page<StoreAccountCapital> page,@Param("map") HashMap<String, Object> map);

    IPage<StoreAccountCapitalVO> getStoreAccountCapitalListList(Page<StoreAccountCapital> page,@Param("storeStatementAccount") StoreStatementAccount storeStatementAccount);
}
