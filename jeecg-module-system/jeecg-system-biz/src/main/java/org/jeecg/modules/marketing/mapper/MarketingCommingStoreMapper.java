package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingCommingStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCommingStore;
import org.jeecg.modules.marketing.vo.MarketingCommingStoreVO;

/**
 * @Description: 进店活动
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
public interface MarketingCommingStoreMapper extends BaseMapper<MarketingCommingStore> {

    IPage<MarketingCommingStoreVO> queryPageList(Page<MarketingCommingStoreVO> page,@Param("marketingCommingStoreDTO") MarketingCommingStoreDTO marketingCommingStoreDTO);
}
