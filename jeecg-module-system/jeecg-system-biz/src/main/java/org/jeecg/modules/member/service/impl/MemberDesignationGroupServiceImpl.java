package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.member.entity.MemberDesignationGroup;
import org.jeecg.modules.member.mapper.MemberDesignationGroupMapper;
import org.jeecg.modules.member.service.IMemberDesignationGroupService;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 团队管理
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Service
public class MemberDesignationGroupServiceImpl extends ServiceImpl<MemberDesignationGroupMapper, MemberDesignationGroup> implements IMemberDesignationGroupService {

    @Override
    public IPage<MemberDesignationGroupVO> queryPageList(Page<MemberDesignationGroupVO> page, QueryWrapper<MemberDesignationGroupVO> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> findMemberDesignationGroupByName(String name) {
        return baseMapper.findMemberDesignationGroupByName(name);
    }

    @Override
    public List<Map<String,Object>> getToDayPartner(String id, String date) {
        return baseMapper.getToDayPartner(id,date);
    }

    @Override
    public List<Map<String, Object>> getNewPartner(String id) {
        return baseMapper.getNewPartner(id);
    }

    @Override
    public List<Map<String, Object>> getPerformanceSum(String id) {
        return baseMapper.getPerformanceSum(id);
    }

    @Override
    public List<Map<String, Object>> getToDayPerformance(String id) {
        return baseMapper.getToDayPerformance(id);
    }
}
