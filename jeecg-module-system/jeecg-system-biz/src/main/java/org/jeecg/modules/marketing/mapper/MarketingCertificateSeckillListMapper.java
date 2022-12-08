package org.jeecg.modules.marketing.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillList;
import org.jeecg.modules.marketing.vo.MarketingCertificateSeckillListVO;

/**
 * @Description: 限时抢券列表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface MarketingCertificateSeckillListMapper extends BaseMapper<MarketingCertificateSeckillList> {

    List<Map<String,Object>> pageListBySout(@Param("marketingCertificateSeckillActivityListId") String marketingCertificateSeckillActivityListId,@Param("sout") Integer sout);

    IPage<MarketingCertificateSeckillListVO> queryPageList(Page<MarketingCertificateSeckillListVO> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingCertificateSeckillListVO> queryWrapper);

    IPage<Map<String,Object>> pageListByMarketingCertificateSeckillActivityListId(Page<Map<String,Object>> page,@Param("marketingCertificateSeckillActivityListId") String marketingCertificateSeckillActivityListId);
}
