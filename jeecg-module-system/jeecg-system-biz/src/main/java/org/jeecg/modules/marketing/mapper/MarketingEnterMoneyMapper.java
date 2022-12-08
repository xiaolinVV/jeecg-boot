package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingEnterMoneyDTO;
import org.jeecg.modules.marketing.entity.MarketingEnterMoney;
import org.jeecg.modules.marketing.vo.MarketingEnterMoneyVO;

/**
 * @Description: 称号出账资金
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
public interface MarketingEnterMoneyMapper extends BaseMapper<MarketingEnterMoney> {

    IPage<MarketingEnterMoneyVO> queryPageList(Page<MarketingEnterMoney> page,@Param("marketingEnterMoneyDTO") MarketingEnterMoneyDTO marketingEnterMoneyDTO);
}
