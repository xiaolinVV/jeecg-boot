package org.jeecg.modules.member.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.member.entity.MemberDesignationMemberList;
import org.jeecg.modules.member.vo.MemberDesignationMemberListVO;

/**
 * @Description: 会员称号关系
 * @Author: jeecg-boot
 * @Date:   2021-03-01
 * @Version: V1.0
 */
public interface MemberDesignationMemberListMapper extends BaseMapper<MemberDesignationMemberList> {

    List<Map<String,Object>> findMemberDesignationList(@Param("memberId") String memberId);

    IPage<MemberDesignationMemberListVO> queryPageList(Page<MemberDesignationMemberListVO> page,@Param(Constants.WRAPPER)QueryWrapper<MemberDesignationMemberListVO> queryWrapper);

    List<MemberDesignationMemberListVO> findMemberDesignationMemberlist(@Param("memberDesignationMemberListVO") MemberDesignationMemberListVO memberDesignationMemberListVO);

}
