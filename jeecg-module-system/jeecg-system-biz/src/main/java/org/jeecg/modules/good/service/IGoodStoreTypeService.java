package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.good.dto.GoodStoreTypeDto;
import org.jeecg.modules.good.entity.GoodStoreType;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-17
 * @Version: V1.0
 */
public interface IGoodStoreTypeService extends IService<GoodStoreType> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addGoodStoreType(GoodStoreType goodStoreType);
	
	/**修改节点*/
	void updateGoodStoreType(GoodStoreType goodStoreType) throws JeecgBootException;
	
	/**删除节点*/
	void deleteGoodStoreType(String id) throws JeecgBootException;

	public List<GoodStoreType> getGoodStoreTypeOrParentIdListtwo(String parentId, String level,String sysUserId);
	public List<GoodStoreType> getGoodTypeOrParentIdListtwo(String parentId,String level,String sysUserId);
	/**查询节点*/
	List<GoodStoreTypeDto> getgoodTypeListName(String nodeName, String level,String sysUserId);
	/**
	 * 构建分类树
	 * @param id
	 * @return
	 */
	Map<String,Object> encapsulationlassificationTree(String id, String userName);
	/**
	 * 根据第三节点名称构建1，2，3节点视图
	 * @param goodTypeId3
	 * @return
	 */
	public GoodStoreTypeDto getGoodTypeByGoodTyeId3(String goodTypeId3);
	/**
	 * 根据节点名称和父id获取节点
	 * @param name
	 * @param parentId
	 * @return
	 */
	List<Map<String,String>>getGoodTypeListNameAndParentId(String name, String parentId,String sysUserId);


	/**
	 * 根据店铺id查询商品类型
	 * @param sysUserId
	 * @return
	 */
	public List<Map<String,Object>> findGoodTypeBySysUserId(String sysUserId);

	/**
	 * 根据父类id查询子类商品类型
	 * @param parentId
	 * @return
	 */
	public List<Map<String,Object>> findGoodTypeByParentId(String parentId);

	/**
	 *热门推荐分类 按销量倒叙排名
	 * @return
	 */

	public List<Map<String,Object>> getHotRecommended(String sysUserId,Integer limit);
	/**
	 * 通过热门推荐分类 按销量倒叙排名 获取Id；
	 * 在通过评分排行
	 * @param goodStoreTypeIds
	 * @param limit
	 * @return
	 */
	List<Map<String,Object>> getHotRecommendedAVG(String sysUserId,List<String> goodStoreTypeIds,Integer limit);

	/**
	 * 人气推荐分类 按销量倒叙排名
	 * @param limit
	 * @return
	 */
	List<Map<String,Object>> getPopularityRecommended(String sysUserId,Integer limit);

    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
    List<Map<String,Object>> getGoodStoreTypeNumberDesc(String sysUserId,Integer limit);

    /**
     * 根据用户评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    List<Map<String,Object>>  getGoodStoreTypeAvgDesc(List<String> goodTypeIds,Integer limit);
    /**
     * 人气排行根据购买分类内商品数量后根据评分排行
     * @param limit
     * @return
     */
    List<Map<String,Object>> getGoodStoreTypeNumberAndAvgDesc(String sysUserId,Integer limit);
}
