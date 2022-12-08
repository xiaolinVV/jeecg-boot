package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;
import org.jeecg.modules.member.mapper.MemberBusinessDesignationMapper;
import org.jeecg.modules.member.service.IMemberBusinessDesignationService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 创业团队成员
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Service
public class MemberBusinessDesignationServiceImpl extends ServiceImpl<MemberBusinessDesignationMapper, MemberBusinessDesignation> implements IMemberBusinessDesignationService {

    @Override
    public IPage<Map<String, Object>> findMemberBusinessDesignationBySuperiorMemberId(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.findMemberBusinessDesignationBySuperiorMemberId(page,memberId);
    }

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,requestMap);
    }
}
