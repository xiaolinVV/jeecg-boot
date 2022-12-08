package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMemberGoodsCollectionService extends IService<MemberGoodsCollection> {
    IPage<MemberGoodsCollectionDto> getMemberGoodsCollectionVo(Page<MemberGoodsCollectionDto> page, MemberGoodsCollectionVo memberGoodsCollectionVo );


    /**
     * 商品收藏服务
     *
     * @param goodId
     * @param isPlatform
     * @param memberId
     * @return
     */
    public Boolean addMemberGoodsCollectionByGoodId(String goodId, Integer isPlatform,String memberId,String marketingPrefectureId);

    /**
     * 分页查询用户收藏列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> findMemberGoodsCollections(Page<Map<String,Object>> page, Map<String,Object> paramMap);
}
