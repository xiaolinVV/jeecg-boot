package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingTextbookListDTO;
import org.jeecg.modules.marketing.entity.MarketingTextbookList;
import org.jeecg.modules.marketing.mapper.MarketingTextbookListMapper;
import org.jeecg.modules.marketing.service.IMarketingTextbookListService;
import org.jeecg.modules.marketing.vo.MarketingTextbookListVO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 教程素材
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Service
public class MarketingTextbookListServiceImpl extends ServiceImpl<MarketingTextbookListMapper, MarketingTextbookList> implements IMarketingTextbookListService {

    @Override
    public IPage<MarketingTextbookListVO> queryPageList(Page<MarketingTextbookListVO> page, MarketingTextbookListDTO marketingTextbookListDTO) {
        return baseMapper.queryPageList(page,marketingTextbookListDTO);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingTextbookList(Page<Map<String,Object>> page, String id) {
        return baseMapper.findMarketingTextbookList(page,id);
    }

    @Override
    public Map<String, Object> findMarketingTextBookById(String id) {
        return baseMapper.findMarketingTextBookById(id);
    }

    @Override
    public IPage<Map<String,Object>> findMarketingTextBookByTitle(Page<Map<String,Object>> page,String title) {
        return baseMapper.findMarketingTextBookByTitle(page,title);
    }
}
