package org.jeecg.modules.shopBoot.store.storeType.service;

import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.shopBoot.store.storeType.entity.StoreType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 店铺分类
 * @Author: jeecg-boot
 * @Date:   2022-12-07
 * @Version: V1.0
 */
public interface IStoreTypeService extends IService<StoreType> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**
	 * 新增节点
	 *
	 * @param storeType
	 */
	void addStoreType(StoreType storeType);
	
	/**
   * 修改节点
   *
   * @param storeType
   * @throws JeecgBootException
   */
	void updateStoreType(StoreType storeType) throws JeecgBootException;
	
	/**
	 * 删除节点
	 *
	 * @param id
   * @throws JeecgBootException
	 */
	void deleteStoreType(String id) throws JeecgBootException;

	  /**
	   * 查询所有数据，无分页
	   *
	   * @param queryWrapper
	   * @return List<StoreType>
	   */
    List<StoreType> queryTreeListNoPage(QueryWrapper<StoreType> queryWrapper);

	/**
	 * 【vue3专用】根据父级编码加载分类字典的数据
	 *
	 * @param parentCode
	 * @return
	 */
	List<SelectTreeModel> queryListByCode(String parentCode);

	/**
	 * 【vue3专用】根据pid查询子节点集合
	 *
	 * @param pid
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(String pid);

}
