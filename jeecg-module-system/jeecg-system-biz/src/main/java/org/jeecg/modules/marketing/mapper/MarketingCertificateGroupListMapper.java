package org.jeecg.modules.marketing.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupList;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupListVO;

/**
 * @Description: 拼好券
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface MarketingCertificateGroupListMapper extends BaseMapper<MarketingCertificateGroupList> {

    IPage<MarketingCertificateGroupListVO> queryPageList(Page<MarketingCertificateGroupListVO> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingCertificateGroupListVO> queryWrapper);

    List<Map<String,Object>> pageListBySout(@Param("sout") Integer sout);

    IPage<Map<String,Object>> pageListByMarketingCertificateGroupList(Page<Map<String,Object>> page);
}
