package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.dto.MemberBrowsingHistoryDto;
import org.jeecg.modules.member.entity.MemberBrowsingHistory;
import org.jeecg.modules.member.vo.MemberBrowsingHistoryVo;

import java.util.Map;

/**
 * @Description: 浏览记录
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
public interface IMemberBrowsingHistoryService extends IService<MemberBrowsingHistory> {


    IPage<MemberBrowsingHistoryDto> getMemberGoodsCollectionVo(Page<MemberBrowsingHistoryDto> page, MemberBrowsingHistoryVo memberBrowsingHistoryVo );

    /**
     * 新增浏览记录
     * @param isPlatform
     * @param goodId
     * @param memberId
     * @return
     */
    public Boolean addGoodToBrowsingHistory(Integer isPlatform, String goodId, String memberId,String marketingPrefectureId,String marketingFreeGoodListId);

    /**
     * 获取用户的日期排序
     *
     * @param page
     * @param memberId
     * @return
     */
    IPage<Map<String,Object>> findBrowsingHistoryBySort(Page<Map<String,Object>> page,String memberId);

}
