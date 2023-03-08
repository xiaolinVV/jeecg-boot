package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;
import org.jeecg.modules.store.dto.StoreManageDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.vo.StoreManageVO;
import org.jeecg.modules.store.vo.StoreWorkbenchVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
public interface StoreManageMapper extends BaseMapper<StoreManage> {


    /**
     * 获取所有的店铺列表
     *
     * @return
     */
    public List<Map<String,Object>> getAllStoreList(@Param("userId") String userId);

    /**
     * 获取店铺详情
     *
     * @param storeManageId
     * @return
     */
    public Map<String,Object> getStoreManageById(@Param("storeManageId") String storeManageId);


    /**
     * 获取推荐店铺列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getStoreManageByRecommend(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 根据不同方式获取店铺列表
     *
     *
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> findStoreManageList(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    List<StoreManageDTO> findStoreManage(@Param("storeManageDTO") StoreManageDTO storeManageDTO);

    StoreManageVO findStore(@Param("userId") String userId);

    IPage<StoreManageDTO> queryStoreManagePage(Page<StoreManageDTO> page,@Param("storeManageVO") StoreManageVO storeManageVO);

    List<StoreManageDTO> findPageList(@Param("storeManageVO") StoreManageVO storeManageVO);

    IPage<StoreManageDTO> findStoreBalance(Page<StoreManageDTO> page,@Param("storeManageVO") StoreManageVO storeManageVO);

    StoreWorkbenchVO findStoreWorkbenchVO(@Param("userId") String userId);

    Map<String,Object> myStore(@Param("sysUserId") String sysUserId);

    Map<String,Object> findStoreInfo(@Param("sysUserId") String sysUserId);

    Map<String,Object> returnAuthentication(@Param("sysUserId") String sysUserId);

    Map<String,Object> returnSecurity(@Param("sysUserId") String sysUserId);

    IPage<Map<String,Object>> findCertificateStore(Page<Map<String,Object>> page,@Param("objectObjectHashMap") HashMap<Object,Object> objectObjectHashMap);

    Map<String,Object> findUseInfo(@Param("sysUserId")String sysUserId);

    IPage<StoreManageVO> findAllianceStoreList(Page<StoreManageVO> page,@Param("storeManageDTO") StoreManageDTO storeManageDTO);

    List<Map<String,Object>> getStoreByBossPhone(@Param("bossPhone") String bossPhone);

    List<Map<String,Object>> findStoreInfoByStoreName(@Param("storeName") String storeName);

    IPage<Map<String,Object>> findCityLifeStoreManageList(Page<Map<String,Object>> page,@Param("paramObjectMap") Map<String, Object> paramObjectMap);

    IPage<Map<String,Object>> findCityLifeStoreManageListNew(Page<Map<String,Object>> page,@Param("paramObjectMap") Map<String, Object> paramObjectMap);

    /**
     * 根据标签id获取店铺列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getLabelStoreManage(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

    List<MemberDesignationGroupVO> getPartnerSum(@Param("id") String id);


    /**
     * 获取可用店铺列表
     *
     * @param sysUserId
     * @return
     */
    List<Map<String,Object>> getStoreList(@Param("sysUserId") String sysUserId);

    /**
     * 获取特权店铺列表信息
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> getPrivilege(Page<Map<String,Object>> page,@Param("memberId") String memberId);

    /**
     * 获取特权店铺详情
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object> getPrivilegeInfo(@Param("paramMap") Map<String,Object> paramMap);
}
