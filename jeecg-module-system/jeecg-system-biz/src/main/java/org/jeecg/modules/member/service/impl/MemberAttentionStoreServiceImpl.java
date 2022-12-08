package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.dto.MemberAttentionStoreDto;
import org.jeecg.modules.member.entity.MemberAttentionStore;
import org.jeecg.modules.member.mapper.MemberAttentionStoreMapper;
import org.jeecg.modules.member.service.IMemberAttentionStoreService;
import org.jeecg.modules.member.vo.MemberAttentionStoreVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 关注店铺
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
@Service
public class MemberAttentionStoreServiceImpl extends ServiceImpl<MemberAttentionStoreMapper, MemberAttentionStore> implements IMemberAttentionStoreService {

    /**
     * 分页列表查询
     * @param page
     * @param memberAttentionStoreVo
     * @return
     */
    @Override
    public IPage<MemberAttentionStoreDto> getMemberAttentionStoreVo(Page<MemberAttentionStoreDto> page, MemberAttentionStoreVo memberAttentionStoreVo) {
        return baseMapper.getMemberAttentionStoreVo(page, memberAttentionStoreVo);
    }

    @Override
    public List<MemberAttentionStoreDto> getMemberAttentionStoreVoList(QueryWrapper<MemberAttentionStoreVo> queryWrapper) {
        List<MemberAttentionStoreDto> memberAttentionStoreVoList = baseMapper.getMemberAttentionStoreVoList(queryWrapper);
        return memberAttentionStoreVoList;
    }

    @Override
    public IPage<Map<String, Object>> findMemberAttentionStoreByMember(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findMemberAttentionStoreByMember(page,paramMap);
    }

    @Override
    public List<Map<String, Object>> getRecommendStore() {
        return baseMapper.getRecommendStore();
    }

}
