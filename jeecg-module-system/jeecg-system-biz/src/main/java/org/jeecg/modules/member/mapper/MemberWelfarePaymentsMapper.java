package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.entity.MemberWelfarePayments;
import org.jeecg.modules.member.vo.MemberWelfarePaymentsVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员福利金
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
public interface MemberWelfarePaymentsMapper extends BaseMapper<MemberWelfarePayments> {


    /**
     * 分页查询用户福利金列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> findMemberWelfarePaymentsByMemberId(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

    IPage<MemberWelfarePaymentsVO> findMemberWelfarePayments(Page<MemberWelfarePaymentsVO> page,@Param("memberWelfarePaymentsVO") MemberWelfarePaymentsVO memberWelfarePaymentsVO);

    IPage<MemberWelfarePaymentsVO> findMemberPayRecord(Page<MemberWelfarePaymentsVO> page,@Param("memberWelfarePaymentsVO") MemberWelfarePaymentsVO  memberWelfarePaymentsVO);

    IPage<Map<String,Object>> findMemberWelfarePaymentsPageByMemberId(Page<Map<String,Object>> page,@Param("paramObjectMap") Map<String, Object> paramObjectMap);

    IPage<Map<String,Object>> findfrozenAandUnusableById(Page<Map<String,Object>> page,@Param("paramObjectMap") Map<String, Object> paramObjectMap);

    List<MemberWelfarePayments> getAbnormalWelfarePam(@Param("map") HashMap<String, Object> map);

}
