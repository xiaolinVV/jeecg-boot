package org.jeecg.modules.good.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.good.entity.GoodStoreType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-17
 * @Version: V1.0
 */
public interface GoodStoreTypeMapper extends BaseMapper<GoodStoreType> {

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
	public List<GoodStoreType> getGoodStoreTypeOrParentIdListtwo(@Param("parentId")String parentId, @Param("level")String level);

	/**
	 * 修改状态用的查询
	 * @param parentId 父级Id
	 * @param level 分类等级
	 * @return
	 */
	public List<GoodStoreType> getGoodStoreTypeOrParentIdList(@Param("parentId")String parentId, @Param("level")String level);

	/**
	 * 修改状态用的查询
	 * @param parentId 父级Id
	 * @param level 分类等级
	 * @return
	 */
	public List<GoodStoreType> getGoodTypeOrParentIdListtwo(@Param("parentId")String parentId, @Param("level")String level, @Param("sysUserId")String sysUserId);
	/**
	 * 根据第三级的名称获取分类
	 * @param nodeName
	 * @param level
	 * @return
	 */
	List<Map<String,Object>> getgoodTypeListName(@Param("nodeName")String nodeName, @Param("level")String level,@Param("sysUserId")String sysUserId);
	List<GoodStoreType> selectGoodTypeByParentId(@Param("parentId")String parentId);
	List<Map<String,String>> selectGoodTypeByParentIdAndName(@Param("name")String name, @Param("parentId")String parentId,@Param("sysUserId")String sysUserId);

	/**
	 * 根据店铺id查询商品类型
	 * @param sysUserId
	 * @return
	 */
	public List<Map<String,Object>> findGoodTypeBySysUserId(@Param("sysUserId") String sysUserId);

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
	List<Map<String,Object>> getHotRecommended(@Param("sysUserId")String sysUserId,@Param("limit")Integer limit);

	/**
	 * 通过热门推荐分类 按销量倒叙排名 获取Id；
	 * 在通过评分排行
	 * @param goodStoreTypeIds
	 * @param limit
	 * @return
	 */
	List<Map<String,Object>> getHotRecommendedAVG(@Param("sysUserId")String sysUserId,@Param("goodStoreTypeIds")List<String> goodStoreTypeIds, @Param("limit")Integer limit);

    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
    List<Map<String,Object>> getGoodStoreTypeNumberDesc(@Param("sysUserId")String sysUserId,@Param("limit")Integer limit);

    /**
     * 根据用户评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    List<Map<String,Object>>  getGoodStoreTypeAvgDesc(@Param("goodTypeIds")List<String> goodTypeIds,@Param("limit")Integer limit);

}
