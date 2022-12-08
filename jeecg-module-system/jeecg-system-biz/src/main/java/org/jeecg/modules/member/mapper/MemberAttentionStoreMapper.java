package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberAttentionStoreDto;
import org.jeecg.modules.member.entity.MemberAttentionStore;
import org.jeecg.modules.member.vo.MemberAttentionStoreVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 关注店铺
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
public interface MemberAttentionStoreMapper extends BaseMapper<MemberAttentionStore> {
    /**
     * 分页列表查询
     * @param page
     * @param memberAttentionStoreVo
     * @return
     */
    IPage<MemberAttentionStoreDto> getMemberAttentionStoreVo(Page<MemberAttentionStoreDto> page, @Param("memberAttentionStoreVo")MemberAttentionStoreVo memberAttentionStoreVo);



    List<MemberAttentionStoreDto> getMemberAttentionStoreVoList(@Param("memberAttentionStoreVo")QueryWrapper<MemberAttentionStoreVo> memberAttentionStoreVo);

    /**
     * 根据不同方式获取店铺列表
     *
     *
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> findMemberAttentionStoreByMember(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    List<Map<String,Object>> getRecommendStore();

}
