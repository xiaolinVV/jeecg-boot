package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftRecord;

import java.util.Map;

/**
 * @Description: 创业礼包购买记录
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface MarketingBusinessGiftRecordMapper extends BaseMapper<MarketingBusinessGiftRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingBusinessGiftRecord> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    Map<String,Object> getGiftRecordDetails(@Param("id") String id);


    /**
     * 根据会员id获取礼包总金额
     *
     * @param memberId
     * @return
     */
    public double getSumByMemberId(@Param("memberId") String memberId);
}
