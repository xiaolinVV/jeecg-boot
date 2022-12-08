package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.member.entity.MemberDesignationMemberList;
import org.jeecg.modules.member.mapper.MemberDesignationMemberListMapper;
import org.jeecg.modules.member.service.IMemberDesignationMemberListService;
import org.jeecg.modules.member.vo.MemberDesignationMemberListVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员称号关系
 * @Author: jeecg-boot
 * @Date:   2021-03-01
 * @Version: V1.0
 */
@Service
public class MemberDesignationMemberListServiceImpl extends ServiceImpl<MemberDesignationMemberListMapper, MemberDesignationMemberList> implements IMemberDesignationMemberListService {

    @Override
    public List<Map<String, Object>> findMemberDesignationList(String memberId) {
        return baseMapper.findMemberDesignationList(memberId);
    }

    @Override
    public IPage<MemberDesignationMemberListVO> queryPageList(Page<MemberDesignationMemberListVO> page, QueryWrapper<MemberDesignationMemberListVO> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    public List<MemberDesignationMemberListVO> findMemberDesignationMemberlist(MemberDesignationMemberListVO memberDesignationMemberListVO) {
        return baseMapper.findMemberDesignationMemberlist(memberDesignationMemberListVO);
    }


}
