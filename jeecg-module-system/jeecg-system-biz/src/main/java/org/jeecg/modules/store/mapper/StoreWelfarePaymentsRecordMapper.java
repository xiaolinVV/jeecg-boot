package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreWelfarePaymentsRecordDTO;
import org.jeecg.modules.store.entity.StoreWelfarePaymentsRecord;
import org.jeecg.modules.store.vo.StoreWelfarePaymentsRecordVO;

/**
 * @Description: 福利金收款记录
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
public interface StoreWelfarePaymentsRecordMapper extends BaseMapper<StoreWelfarePaymentsRecord> {

    IPage<StoreWelfarePaymentsRecordVO> queryPageList(Page<StoreWelfarePaymentsRecord> page,@Param("storeWelfarePaymentsRecordDTO") StoreWelfarePaymentsRecordDTO storeWelfarePaymentsRecordDTO);
}
