package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingRecordedMoneyDTO;
import org.jeecg.modules.marketing.entity.MarketingRecordedMoney;
import org.jeecg.modules.marketing.vo.MarketingRecordedMoneyVO;

/**
 * @Description: 称号入账资金
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
public interface MarketingRecordedMoneyMapper extends BaseMapper<MarketingRecordedMoney> {

    IPage<MarketingRecordedMoneyVO> queryPageList(Page<MarketingRecordedMoney> page,@Param("marketingRecordedMoneyDTO") MarketingRecordedMoneyDTO marketingRecordedMoneyDTO);
}
