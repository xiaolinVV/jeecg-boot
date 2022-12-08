package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.store.dto.StoreWelfarePaymentsRecordDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreWelfarePaymentsRecord;
import org.jeecg.modules.store.vo.StoreWelfarePaymentsRecordVO;

import java.math.BigDecimal;

/**
 * @Description: 福利金收款记录
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
public interface IStoreWelfarePaymentsRecordService extends IService<StoreWelfarePaymentsRecord> {

    Boolean storeCollectWelfare(MemberList memberList, StoreManage storeManage, BigDecimal money, String gatheringExplain);

    IPage<StoreWelfarePaymentsRecordVO> queryPageList(Page<StoreWelfarePaymentsRecord> page, StoreWelfarePaymentsRecordDTO storeWelfarePaymentsRecordDTO);
}
