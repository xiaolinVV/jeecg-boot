package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.marketing.entity.MarketingEnterMoney;
import org.jeecg.modules.member.dto.MemberAccountCapitalDTO;
import org.jeecg.modules.member.entity.MemberAccountCapital;
import org.jeecg.modules.member.vo.MemberAccountCapitalVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 会员资金明细
 * @Author: jeecg-boot
 * @Date:   2019-12-17
 * @Version: V1.0
 */
public interface MemberAccountCapitalMapper extends BaseMapper<MemberAccountCapital> {

    IPage<MemberAccountCapitalDTO> getMemberAccountCapitalList(Page<MemberAccountCapital> page, @Param("memberAccountCapitalVO") MemberAccountCapitalVO memberAccountCapitalVO);

    /**
     * 分页查询用户会员资金列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> findMemberAccountCapitalByMemberId(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    IPage<Map<String,Object>> findAccountCapitalByMemberId(Page<Map<String,Object>> page,@Param("map") HashMap<String, Object> map);

    IPage<MemberAccountCapitalVO> getEnterMoney(Page<AgencyAccountCapital> page,@Param("marketingEnterMoney") MarketingEnterMoney marketingEnterMoney);

}
