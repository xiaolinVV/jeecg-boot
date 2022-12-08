package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.entity.MemberDesignationMemberList;
import org.jeecg.modules.member.vo.MemberDesignationMemberListVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员称号关系
 * @Author: jeecg-boot
 * @Date:   2021-03-01
 * @Version: V1.0
 */
public interface IMemberDesignationMemberListService extends IService<MemberDesignationMemberList> {

    List<Map<String,Object>> findMemberDesignationList(String memberId);

    IPage<MemberDesignationMemberListVO> queryPageList(Page<MemberDesignationMemberListVO> page, QueryWrapper<MemberDesignationMemberListVO> queryWrapper);

    List<MemberDesignationMemberListVO> findMemberDesignationMemberlist(MemberDesignationMemberListVO memberDesignationMemberListVO);

}
