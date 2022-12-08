package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.dto.MemberGrowthRecordDTO;
import org.jeecg.modules.member.entity.MemberGrowthRecord;
import org.jeecg.modules.member.mapper.MemberGrowthRecordMapper;
import org.jeecg.modules.member.service.IMemberGrowthRecordService;
import org.jeecg.modules.member.vo.MemberGrowthRecordVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 成长值记录
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Service
public class MemberGrowthRecordServiceImpl extends ServiceImpl<MemberGrowthRecordMapper, MemberGrowthRecord> implements IMemberGrowthRecordService {

    @Override
    public IPage<MemberGrowthRecordVO> queryPageList(Page<MemberGrowthRecord> page, MemberGrowthRecordDTO memberGrowthRecordDTO) {
        return baseMapper.queryPageList(page,memberGrowthRecordDTO);
    }
}
