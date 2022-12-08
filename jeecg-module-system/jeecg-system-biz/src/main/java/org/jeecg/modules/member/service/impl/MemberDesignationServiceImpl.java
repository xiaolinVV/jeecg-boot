package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.dto.MemberDesignationDTO;
import org.jeecg.modules.member.entity.MemberDesignation;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.mapper.MemberDesignationMapper;
import org.jeecg.modules.member.service.IMemberDesignationCountService;
import org.jeecg.modules.member.service.IMemberDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.vo.MemberDesignationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 称号管理
 * @Author: jeecg-boot
 * @Date:   2020-06-16
 * @Version: V1.0
 */
@Service
public class MemberDesignationServiceImpl extends ServiceImpl<MemberDesignationMapper, MemberDesignation> implements IMemberDesignationService {
    @Autowired
    @Lazy
    private IMemberListService iMemberListService;

    @Autowired
    @Lazy
    private IMemberDesignationCountService iMemberDesignationCountService;
    @Override
    public IPage<MemberDesignationVO> queryPageList(Page<MemberDesignation> page, MemberDesignationDTO memberDesignationDTO) {
        return baseMapper.queryPageList(page,memberDesignationDTO);
    }

    @Override
    public List<Map<String, Object>> memberDesignationNameList(String id) {
        return baseMapper.memberDesignationNameList(id);
    }

    @Override
    public List<Map<String, Object>> getMemberDesignationListBySort(String sort,String memberDesignationGroupId) {
        return baseMapper.getMemberDesignationListBySort(sort,memberDesignationGroupId);
    }


    @Override
    public MemberDesignationVO getMemberDesignationById(String memberDesignationId) {
        return baseMapper.getMemberDesignationById(memberDesignationId);
    }

}
