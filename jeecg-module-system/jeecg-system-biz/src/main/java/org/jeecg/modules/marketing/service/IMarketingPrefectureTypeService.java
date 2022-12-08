package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;

import java.util.List;
import java.util.Map;

/**
 * @Description: 平台专区分类
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
public interface IMarketingPrefectureTypeService extends IService<MarketingPrefectureType> {


    /**
     * 根据平台专区id查询专区类型  但是必须这个类型下面有商品
     *
     * @param id
     * @return
     */
    public List<Map<String,Object>> findByMarketingPrefectureId(String id);
    /**
     * 删除分类 关联删除 商品
     */
    public void linkToDelete(String ids);

    /**
     * 修改停用分类 关联修改 商品停用
     */

    public void linkToUpdateStatus(String ids);

    /**
     * 修改专区分类启用判断是否可启用
     */
    public Map<String,Object> linkToUpdate(String marketingPrefectureGoodId);

    List<Map<String,Object>> findUnderlingListMap(String id);

    List<Map<String,Object>> findMarketingPrefectureTypeTwoById(String id);

    List<MarketingPrefectureType> getUnderlingList(String id);


    List<Map<String, Object>> getMarketingPrefectureTypeAll(String marketingPrefectureId);
}
