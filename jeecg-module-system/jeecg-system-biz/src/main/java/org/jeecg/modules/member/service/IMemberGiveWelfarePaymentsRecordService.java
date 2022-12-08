package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.entity.MemberGiveWelfarePaymentsRecord;

/**
 * @Description: 福利金可获赠数量记录
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IMemberGiveWelfarePaymentsRecordService extends IService<MemberGiveWelfarePaymentsRecord> {

    boolean setMemberGiveWelfarePaymentsRecord(MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord);
}
