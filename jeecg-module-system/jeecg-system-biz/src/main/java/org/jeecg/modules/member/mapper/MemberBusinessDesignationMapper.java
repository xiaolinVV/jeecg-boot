package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;

import java.util.Map;

/**
 * @Description: 创业团队成员
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface MemberBusinessDesignationMapper extends BaseMapper<MemberBusinessDesignation> {

    IPage<Map<String,Object>> findMemberBusinessDesignationBySuperiorMemberId(Page<Map<String,Object>> page,@Param("memberId") String memberId);

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("requestMap") Map<String, Object> requestMap);
}
