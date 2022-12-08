package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberGoodsCollectionDto;
import org.jeecg.modules.member.entity.MemberGoodsCollection;
import org.jeecg.modules.member.vo.MemberGoodsCollectionVo;

import java.util.Map;

/**
 * @Description: 商品收藏
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
public interface MemberGoodsCollectionMapper extends BaseMapper<MemberGoodsCollection> {
    IPage<MemberGoodsCollectionDto> getMemberGoodsCollectionVo(Page<MemberGoodsCollectionDto> page,@Param("memberGoodsCollectionVo") MemberGoodsCollectionVo memberGoodsCollectionVo );


    /**
     * 分页查询用户收藏列表
     * @param page
     * @param paramMap
     * @return
     */
   IPage<Map<String,Object>> findMemberGoodsCollections(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);
}
