package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingTextbookListService extends IService<MarketingTextbookList> {

    IPage<MarketingTextbookListVO> queryPageList(Page<MarketingTextbookListVO> page, MarketingTextbookListDTO marketingTextbookListDTO);

    IPage<Map<String,Object>> findMarketingTextbookList(Page<Map<String,Object>> page, String id);

    Map<String,Object> findMarketingTextBookById(String id);

    IPage<Map<String,Object>> findMarketingTextBookByTitle(Page<Map<String,Object>> page,String title);
}
