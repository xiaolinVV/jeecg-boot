package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.good.dto.GoodTypeDto;
import org.jeecg.modules.good.entity.GoodType;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
public interface IGoodTypeService extends IService<GoodType> {

    /**根节点父ID的值*/
    public static final String ROOT_PID_VALUE = "0";

    /**树节点有子节点状态值*/
    public static final String HASCHILD = "1";

    /**树节点无子节点状态值*/
    public static final String NOCHILD = "0";

    /**新增节点*/
    void addGoodType(GoodType goodType);

    /**修改节点*/
    void updateGoodType(GoodType goodType) throws JeecgBootException;

    /**删除节点*/
    void deleteGoodType(String id) throws JeecgBootException;
    public List<GoodType> getGoodTypeOrParentIdListtwo(String parentId,String level);
    /**查询节点*/
  List<GoodTypeDto> getgoodTypeListName(String nodeName, String level);

    /**
     * 构建分类树
     * @param id
     * @return
     */
    Map<String,Object> encapsulationlassificationTree(String id,String userName);

    /**
     * 根据第三节点名称构建1，2，3节点视图
     * @param goodTypeId3
     * @return
     */
    public GoodTypeDto getGoodTypeByGoodTyeId3(String goodTypeId3);
  /**
   * 根据节点名称和父id获取节点
   * @param name
   * @param parentId
   * @return
   */
 public List<Map<String,String>>getGoodTypeListNameAndParentId(String name, String parentId);

    /**
     * 获取第一级分类id和名称
     * @return
     */
  public   List<Map<String,Object>> findTopGoodType();

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

    public List<Map<String,Object>> getHotRecommended(Integer limit);
    /**
     * 通过热门推荐分类 按销量倒叙排名 获取Id；
     * 在通过评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    List<Map<String,Object>> getHotRecommendedAVG(List<String> goodTypeIds,Integer limit);

    /**
     * 人气推荐分类 按销量倒叙排名
     * @param limit
     * @return
     */
    List<Map<String,Object>> getPopularityRecommended(Integer limit);
    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
    List<Map<String,Object>> getGoodTypeNumberDesc(Integer limit);

    /**
     * 根据用户评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    List<Map<String,Object>>  getGoodTypeAvgDesc(List<String> goodTypeIds,Integer limit);
    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
    List<Map<String,Object>> getGoodTypeNumberAndAvgDesc(Integer limit);
}
