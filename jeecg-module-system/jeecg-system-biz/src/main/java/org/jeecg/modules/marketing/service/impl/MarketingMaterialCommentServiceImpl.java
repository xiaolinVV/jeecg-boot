package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingMaterialCommentDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialComment;
import org.jeecg.modules.marketing.mapper.MarketingMaterialCommentMapper;
import org.jeecg.modules.marketing.service.IMarketingMaterialCommentService;
import org.jeecg.modules.marketing.vo.MarketingMaterialCommentVO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 素材评论表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Service
public class MarketingMaterialCommentServiceImpl extends ServiceImpl<MarketingMaterialCommentMapper, MarketingMaterialComment> implements IMarketingMaterialCommentService {

    /**
     * 查询素材评论列表分页
     * @param page
     * @param marketingMaterialCommentVO
     * @return
     */
   public IPage<MarketingMaterialCommentDTO> getMarketingMaterialCommentDTO(Page page, MarketingMaterialCommentVO marketingMaterialCommentVO){
       return baseMapper.getMarketingMaterialCommentDTO(page,marketingMaterialCommentVO);
   };

    /**
     * 评论数据一级评论
     * @param page
     * @param paramMap
     * @return
     */
   public IPage<Map<String,Object>> getMarketingMaterialCommentMaps(Page<Map<String,Object>> page,Map<String,Object> paramMap){
       return baseMapper.getMarketingMaterialCommentMaps(page,paramMap);
   };
    /**
     * 评论数据二级评论
     * @param page
     * @param paramMap
     * @return
     */
   public IPage<Map<String,Object>> getMarketingMaterialCommentTwoMaps(Page<Map<String,Object>> page,Map<String,Object> paramMap){
       return baseMapper.getMarketingMaterialCommentTwoMaps(page,paramMap);
   };

}
