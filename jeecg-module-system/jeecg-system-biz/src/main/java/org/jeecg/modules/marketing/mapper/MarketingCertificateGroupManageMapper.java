package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupManage;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupManageVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼好券管理
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface MarketingCertificateGroupManageMapper extends BaseMapper<MarketingCertificateGroupManage> {

    List<Map<String,Object>> dataList();

    IPage<Map<String,Object>> myMarketingCertificateGroupManage(Page<Map<String, Object>> page,@Param("memberId") String memberId, @Param("status") String status);

    IPage<MarketingCertificateGroupManageVO> queryPageList(Page<MarketingCertificateGroupManageVO> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingCertificateGroupManage> queryWrapper);

    Map<String,Object> getInfo(@Param("id") String id);
}
