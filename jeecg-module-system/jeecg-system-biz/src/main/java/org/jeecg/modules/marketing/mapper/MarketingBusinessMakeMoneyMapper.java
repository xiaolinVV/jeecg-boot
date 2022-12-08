package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoney;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyVO;

/**
 * @Description: 创业进账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
public interface MarketingBusinessMakeMoneyMapper extends BaseMapper<MarketingBusinessMakeMoney> {

    IPage<MarketingBusinessMakeMoneyVO> queryPageList(Page<MarketingBusinessMakeMoneyVO> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingBusinessMakeMoney> queryWrapper);
}
