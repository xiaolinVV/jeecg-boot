package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMemberAccountCapitalService extends IService<MemberAccountCapital> {

    /**
     * 分页查询用户会员资金列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> findMemberAccountCapitalByMemberId(Page<Map<String,Object>> page, Map<String,Object> paramMap);

    /**
     * 资金流水列表
     * @param page
     * @param memberAccountCapitalVO
     * @return
     */
    IPage<MemberAccountCapitalDTO> getMemberAccountCapitalList(Page<MemberAccountCapital> page, MemberAccountCapitalVO memberAccountCapitalVO);

    /**
     * 用户端小程序分页查询可用余额明细
     * @param page
     * @param map
     * @return
     */
    IPage<Map<String,Object>> findAccountCapitalByMemberId(Page<Map<String,Object>> page, HashMap<String, Object> map);

    IPage<MemberAccountCapitalVO> getEnterMoney(Page<AgencyAccountCapital> page, MarketingEnterMoney marketingEnterMoney);
}
