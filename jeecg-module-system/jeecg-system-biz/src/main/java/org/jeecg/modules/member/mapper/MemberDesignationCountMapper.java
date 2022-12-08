package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberDesignationCountDTO;
import org.jeecg.modules.member.entity.MemberDesignationCount;
import org.jeecg.modules.member.vo.MemberDesignationCountVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员称号统计
 * @Author: jeecg-boot
 * @Date:   2020-07-11
 * @Version: V1.0
 */
public interface MemberDesignationCountMapper extends BaseMapper<MemberDesignationCount> {

    List<MemberDesignationCountVO> findListByMemberId(@Param("id") String id,@Param("memberDesignationGroupId")String memberDesignationGroupId);

    List<MemberDesignationCountVO> getListByMap(@Param("map") HashMap<String, Object> map);

    IPage<MemberDesignationCountVO> findMemberDesignationCountPageListByMemberId(Page<MemberDesignationCountVO> page,@Param("memberDesignationCountDTO") MemberDesignationCountDTO memberDesignationCountDTO);

    List<Map<String,Object>> findMemberdesignationCountListById(@Param("id") String id, @Param("memberDesignationGroupId") String memberDesignationGroupId);
}
