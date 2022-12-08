package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface MemberBrowsingHistoryMapper extends BaseMapper<MemberBrowsingHistory> {
    IPage<MemberBrowsingHistoryDto> getMemberGoodsCollectionVo(Page<MemberBrowsingHistoryDto> page,@Param("memberBrowsingHistoryVo") MemberBrowsingHistoryVo memberBrowsingHistoryVo );


    /**
     * 获取用户的日期排序
     *
     * @param page
     * @param memberId
     * @return
     */
    IPage<Map<String,Object>> findBrowsingHistoryBySort(Page<Map<String,Object>> page,@Param("memberId") String memberId);
}
