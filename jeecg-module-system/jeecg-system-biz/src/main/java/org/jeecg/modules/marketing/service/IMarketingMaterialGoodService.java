package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.marketing.dto.MarketingMaterialGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialGood;
import org.jeecg.modules.marketing.vo.MarketingMaterialListVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 素材商品关系表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
public interface IMarketingMaterialGoodService extends IService<MarketingMaterialGood> {
    /**
     * 根据素材id查找商品
     * @param marketingMaterialListId
     * @return
     */
    List<MarketingMaterialGoodDTO> getMarketingMaterialGoodDTO( String marketingMaterialListId);

    /**
     * 添加素材商品
     * @param goodListVo
     * @return
     */
     List<Map<String,Object>> postGoodListList(GoodListVo goodListVo);
    /**
     * 保存修改素材商品
     * @param marketingMaterialListVO
     * @return
     */
    public void addMarketingMaterialGood(String marketingMaterialListId, MarketingMaterialListVO marketingMaterialListVO);

    /**
     * 获取素材商品信息
     * @param marketingMaterialListId
     * @return
     */
     List<Map<String,Object>> getMarketingMaterialGoodMap( String marketingMaterialListId);

}
