package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberGrowthRecordDTO;
import org.jeecg.modules.member.entity.MemberGrowthRecord;
import org.jeecg.modules.member.vo.MemberGrowthRecordVO;

/**
 * @Description: 成长值记录
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
public interface MemberGrowthRecordMapper extends BaseMapper<MemberGrowthRecord> {

    IPage<MemberGrowthRecordVO> queryPageList(Page<MemberGrowthRecord> page,@Param("memberGrowthRecordDTO") MemberGrowthRecordDTO memberGrowthRecordDTO);
}
