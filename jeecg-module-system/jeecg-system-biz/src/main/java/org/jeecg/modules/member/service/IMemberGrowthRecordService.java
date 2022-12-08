package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.dto.MemberGrowthRecordDTO;
import org.jeecg.modules.member.entity.MemberGrowthRecord;
import org.jeecg.modules.member.vo.MemberGrowthRecordVO;

/**
 * @Description: 成长值记录
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
public interface IMemberGrowthRecordService extends IService<MemberGrowthRecord> {

    IPage<MemberGrowthRecordVO> queryPageList(Page<MemberGrowthRecord> page, MemberGrowthRecordDTO memberGrowthRecordDTO);

}
