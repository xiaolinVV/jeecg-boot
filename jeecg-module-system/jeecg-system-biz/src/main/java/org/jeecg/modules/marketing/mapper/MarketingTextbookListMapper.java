package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingTextbookListDTO;
import org.jeecg.modules.marketing.entity.MarketingTextbookList;
import org.jeecg.modules.marketing.vo.MarketingTextbookListVO;

import java.util.Map;

/**
 * @Description: 教程素材
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
public interface MarketingTextbookListMapper extends BaseMapper<MarketingTextbookList> {

    IPage<MarketingTextbookListVO> queryPageList(Page<MarketingTextbookListVO> page,@Param("marketingTextbookListDTO") MarketingTextbookListDTO marketingTextbookListDTO);

    IPage<Map<String,Object>> findMarketingTextbookList(Page<Map<String,Object>> page,@Param("id") String id);

    Map<String,Object> findMarketingTextBookById(@Param("id") String id);

    IPage<Map<String,Object>> findMarketingTextBookByTitle(Page<Map<String,Object>> page,@Param("title") String title);}
