package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.dto.MemberGradeDTO;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.mapper.MemberGradeMapper;
import org.jeecg.modules.member.service.IMemberGradeService;
import org.jeecg.modules.member.vo.MemberGradeVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员等级
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Service
public class MemberGradeServiceImpl extends ServiceImpl<MemberGradeMapper, MemberGrade> implements IMemberGradeService {

    @Override
    public IPage<MemberGradeVO> queryPageList(Page<MemberGrade> page, MemberGradeDTO memberGradeDTO) {
        return baseMapper.queryPageList(page,memberGradeDTO);
    }

    @Override
    public List<Map<String, Object>> findMemberGradeListMap() {
        return baseMapper.findMemberGradeListMap();
    }

    @Override
    public List<Map<String, String>> getReferMemberGradeList() {
        return baseMapper.getReferMemberGradeList();
    }
}
