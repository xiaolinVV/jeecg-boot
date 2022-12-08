package org.jeecg.modules.good.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.good.entity.GoodType;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
public interface GoodTypeMapper extends BaseMapper<GoodType> {

    /**
     * 编辑节点状态
     * @param id
     * @param status
     */
    void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

    /**
     * 修改状态用的查询
     * @param parentId 父级Id
     * @param level 分类等级
     * @return
     */
 public List<GoodType> getGoodTypeOrParentIdListtwo(@Param("parentId")String parentId, @Param("level")String level);

  /**
   * 根据第三级的名称获取分类
   * @param nodeName
   * @param level
   * @return
   */
  List<Map<String,Object>> getgoodTypeListName(@Param("nodeName")String nodeName, @Param("level")String level);

  List<GoodType> selectGoodTypeByParentId(@Param("parentId")String parentId);
  List<Map<String,String>> selectGoodTypeByParentIdAndName(@Param("name")String name, @Param("parentId")String parentId);

    public   List<Map<String,Object>> findTopGoodType();

    /**
     * 根据父类id查询子类商品类型
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> findGoodTypeByParentId(@Param("parentId") String parentId);

    /**
     *热门推荐分类 按销量倒叙排名
     * @return
     */
    List<Map<String,Object>> getHotRecommended(@Param("limit")Integer limit);

    /**
     * 通过热门推荐分类 按销量倒叙排名 获取Id；
     * 在通过评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    List<Map<String,Object>> getHotRecommendedAVG(@Param("goodTypeIds")List<String> goodTypeIds,@Param("limit")Integer limit);

    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
    List<Map<String,Object>> getGoodTypeNumberDesc(@Param("limit")Integer limit);

    /**
     * 根据用户评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    List<Map<String,Object>>  getGoodTypeAvgDesc(@Param("goodTypeIds")List<String> goodTypeIds,@Param("limit")Integer limit);

}
