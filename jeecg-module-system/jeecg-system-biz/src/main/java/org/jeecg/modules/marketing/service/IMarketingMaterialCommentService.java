package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingMaterialCommentDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialComment;
import org.jeecg.modules.marketing.vo.MarketingMaterialCommentVO;

import java.util.Map;

/**
 * @Description: 素材评论表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
public interface IMarketingMaterialCommentService extends IService<MarketingMaterialComment> {


    /**
     * 查询素材评论列表分页
     * @param page
     * @param marketingMaterialCommentVO
     * @return
     */
    IPage<MarketingMaterialCommentDTO> getMarketingMaterialCommentDTO(Page page, MarketingMaterialCommentVO marketingMaterialCommentVO);

    /**
     * 评论数据一二级评论
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getMarketingMaterialCommentMaps(Page<Map<String,Object>> page, Map<String,Object> paramMap);
    /**
     * 评论数据二级评论
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getMarketingMaterialCommentTwoMaps(Page<Map<String,Object>> page,Map<String,Object> paramMap);

}
