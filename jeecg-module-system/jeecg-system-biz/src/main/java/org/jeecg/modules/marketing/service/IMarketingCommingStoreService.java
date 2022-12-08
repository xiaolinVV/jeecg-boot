package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingCommingStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCommingStore;
import org.jeecg.modules.marketing.vo.MarketingCommingStoreVO;

/**
 * @Description: 进店活动
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
public interface IMarketingCommingStoreService extends IService<MarketingCommingStore> {

    IPage<MarketingCommingStoreVO> queryPageList(Page<MarketingCommingStoreVO> page, MarketingCommingStoreDTO marketingCommingStoreDTO);
}
