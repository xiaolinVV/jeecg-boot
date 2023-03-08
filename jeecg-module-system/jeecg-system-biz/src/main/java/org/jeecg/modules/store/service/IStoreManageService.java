package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;
import org.jeecg.modules.store.dto.StoreManageDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.vo.StoreManageVO;
import org.jeecg.modules.store.vo.StoreWorkbenchVO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
public interface IStoreManageService extends IService<StoreManage> {


    public StoreManage getStoreManageBySysUserId(String sysUserId);


    /**
     * 获取所有的店铺列表
     *
     * @return
     */
    public List<Map<String,Object>> getAllStoreList(String userId);


    /**
     * 设置店铺活动
     *
     * @param resultMap
     * @return
     */
    public Map<String,Object> setActivity(Map<String,Object> resultMap,String storeManageId);


    /**
     * 根据不同方式获取店铺列表
     *
     *
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> findStoreManageList(Page<Map<String,Object>> page,Map<String,Object> paramMap);

    List<StoreManageDTO> findStoreManage(StoreManageDTO storeManageDTO);

    StoreManageVO findStore(String userId);

    IPage<StoreManageDTO> queryStoreManagePage(Page<StoreManageDTO> page,@Param("storeManageVO") StoreManageVO storeManageVO);

    /**
     * 开店成功的时候回调
     * @param id
     */
    public void paySuccess(String id);
    /**
     * 商家端开店成功的时候回调
     * @param id
     */
    public void paySuccessSJD(String id);


    List<StoreManageDTO> findPageList(StoreManageVO storeManageVO);

    IPage<StoreManageDTO> findStoreBalance(Page<StoreManageDTO> page, StoreManageVO storeManageVO);

    /**
     * 开店记录回调
     * @param id
     */
    public void backSucess(String id);

    StoreWorkbenchVO findStoreWorkbenchVO(String userId);

    Map<String,Object> myStore(HttpServletRequest request);

    Map<String, Object> findStoreInfo(HttpServletRequest request);

    Map<String,Object> returnSmallcode(HttpServletRequest request);

    Map<String,Object> returnAuthentication(HttpServletRequest request);

    Map<String,Object> returnSecurity(HttpServletRequest request);

    Boolean updateStorePassword(StoreManageVO storeManageVO, HttpServletRequest request);

    String cashOut(String sysUserId, BigDecimal amount);

    /**
     * 充值支付回调
     * @param xmlMsg
     */
    public Object pay( String xmlMsg);

    IPage<Map<String,Object>> findCertificateStore(Page<Map<String,Object>> page, HashMap<Object,Object> objectObjectHashMap);

    Map<String,Object> findUseInfo(String sysUserId);

    Map<String,Object> findStorePromoter(String id);

    Map<String,Object> findInfoByidAndType(String promoter, String promoterType);

    String updateStorePromoter(StoreManageDTO storeManageDTO);

    IPage<StoreManageVO> findAllianceStoreList(Page<StoreManageVO> page, StoreManageDTO storeManageDTO);

    List<Map<String,Object>> getStoreByBossPhone(String bossPhone);

    List<Map<String,Object>> findStoreInfoByStoreName(String storeName);

    IPage<Map<String,Object>> findCityLifeStoreManageList(Page<Map<String,Object>> page, Map<String, Object> paramObjectMap);

    IPage<Map<String,Object>> findCityLifeStoreManageListNew(Page<Map<String,Object>> page, Map<String, Object> paramObjectMap);


    /**
     * 获取推荐店铺列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getStoreManageByRecommend(Page<Map<String,Object>> page, Map<String,Object> paramMap);


    /**
     * 获取店铺详情
     *
     * @param storeManageId
     * @return
     */
    public Map<String,Object> getStoreManageById(String storeManageId);


    /**
     * 根据标签id获取店铺列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getLabelStoreManage(Page<Map<String,Object>> page, Map<String,Object> paramMap);

    List<MemberDesignationGroupVO> getPartnerSum(String id);


    /**
     * 获取可用店铺列表
     *
     * @param sysUserId
     * @return
     */
    List<Map<String,Object>> getStoreList(@Param("sysUserId") String sysUserId);

    /**
     * 店铺余额扣除
     * @param storeManageId 店铺id
     * @param subtractBalance 金额
     * @param orderNo 单号
     * @param payType 交易类型
     * @return
     */
    boolean subtractStoreBlance(String storeManageId, BigDecimal subtractBalance, String orderNo, String payType);

    boolean addStoreBlance(String storeManageId, BigDecimal addBalance, String orderNo, String payType);

    /**
     * 获取特权店铺列表信息
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> getPrivilege(Page<Map<String,Object>> page,String memberId);

    /**
     * 获取特权店铺详情
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object> getPrivilegeInfo( Map<String,Object> paramMap);
}
