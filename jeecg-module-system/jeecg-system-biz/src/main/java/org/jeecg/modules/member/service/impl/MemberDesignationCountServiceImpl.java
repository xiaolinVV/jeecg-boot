package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.dto.MemberDesignationCountDTO;
import org.jeecg.modules.member.entity.MemberDesignationCount;
import org.jeecg.modules.member.mapper.MemberDesignationCountMapper;
import org.jeecg.modules.member.service.IMemberDesignationCountService;
import org.jeecg.modules.member.vo.MemberDesignationCountVO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员称号统计
 * @Author: jeecg-boot
 * @Date:   2020-07-11
 * @Version: V1.0
 */
@Service
public class MemberDesignationCountServiceImpl extends ServiceImpl<MemberDesignationCountMapper, MemberDesignationCount> implements IMemberDesignationCountService {

    @Override
    public List<MemberDesignationCountVO> findListByMemberId(String id,String memberDesignationGroupId) {
        return baseMapper.findListByMemberId(id,memberDesignationGroupId);
    }

    @Override
    public List<MemberDesignationCountVO> getListByMap(HashMap<String, Object> map) {
        return baseMapper.getListByMap(map);
    }

    @Override
    public IPage<MemberDesignationCountVO> findMemberDesignationCountPageListByMemberId(Page<MemberDesignationCountVO> page, MemberDesignationCountDTO memberDesignationCountDTO) {
        return baseMapper.findMemberDesignationCountPageListByMemberId(page,memberDesignationCountDTO);
    }

    @Override
    public List<Map<String,Object>> findMemberdesignationCountListById(String id, String memberDesignationGroupId) {
        return baseMapper.findMemberdesignationCountListById(id,memberDesignationGroupId);
    }
}
