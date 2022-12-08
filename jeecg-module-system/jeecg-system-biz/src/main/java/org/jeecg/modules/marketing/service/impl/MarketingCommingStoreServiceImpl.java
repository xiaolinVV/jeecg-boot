package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingCommingStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCommingStore;
import org.jeecg.modules.marketing.mapper.MarketingCommingStoreMapper;
import org.jeecg.modules.marketing.service.IMarketingCommingStoreService;
import org.jeecg.modules.marketing.vo.MarketingCommingStoreVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 进店活动
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Service
public class MarketingCommingStoreServiceImpl extends ServiceImpl<MarketingCommingStoreMapper, MarketingCommingStore> implements IMarketingCommingStoreService {

    @Override
    public IPage<MarketingCommingStoreVO> queryPageList(Page<MarketingCommingStoreVO> page, MarketingCommingStoreDTO marketingCommingStoreDTO) {
        return baseMapper.queryPageList(page,marketingCommingStoreDTO);
    }
}
