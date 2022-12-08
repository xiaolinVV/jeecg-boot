package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.entity.MemberGiveWelfarePayments;

import java.util.Map;

/**
 * @Description: 会员福利金可获赠数量
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface MemberGiveWelfarePaymentsMapper extends BaseMapper<MemberGiveWelfarePayments> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("requestMap") Map<String, Object> requestMap);
}
