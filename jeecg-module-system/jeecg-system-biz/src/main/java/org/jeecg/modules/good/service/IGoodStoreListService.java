package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.dto.GoodStoreDiscountDTO;
import org.jeecg.modules.good.dto.GoodStoreListDto;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.vo.GoodStoreListVo;
import org.jeecg.modules.good.vo.SearchTermsVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺商品列表
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
public interface IGoodStoreListService extends IService<GoodStoreList> {



    /*
     * 后端列表
     * */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,Map<String,Object> paramMap,QueryWrapper wrapper);




    /**
     * 商品选择组件
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectGood(Page<Map<String,Object>> page,Map<String,Object> paramMap);

    public GoodStoreList getGoodStoreListById(String id);
    public void updateDelFalg(GoodStoreList goodStoreList,String delFlag);

    public GoodStoreListDto selectById(String id);
    boolean saveOrUpdate(GoodStoreListVo goodListVo);
    IPage<GoodStoreListDto> getGoodListDto(Page<GoodStoreList> page, GoodStoreListVo goodListVo,String notauditStatus);
    IPage<GoodStoreListDto> getGoodListDtoDelFlag(Page<GoodStoreList> page, GoodStoreListVo goodListVo);


    /**
     * 通过类型id查询商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findGoodListByGoodType(Page<Map<String,Object>> page,Map<String,Object> paramMap);

    /**
     * 根据商品id获取商品详情
     * @param goodId
     * @return
     */
    public Map<String,Object> findGoodListByGoodId(String goodId);

    /**
     * 根据店铺id查询商品
     *
     * @param page
     * @param sysUserId
     * @return
     */
    public IPage<Map<String,Object>> findGoodListBySysUserId(Page<Map<String,Object>> page,String sysUserId);

    /**
     * 搜索商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> searchGoodList(Page<Map<String,Object>> page, Map<String,Object> paramMap);


    /**
     *每日上新返回goodTypeId 集合 根据个数倒叙
     * @param createTime
     * @param limit
     * @return
     */
    List<Map<String,Object>>  getEverydayGoodStoreTypeId( String sysUserId,String createTime,Integer limit);
    /**
     * 每周特惠查询
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getEveryWeekPreferential(Page<Map<String,Object>> page, Map<String,Object> paramMap);

    /**
     * 优惠券选择店铺商品
     * @param
     * @param
     * @param
     * @return
     */
    List<GoodStoreDiscountDTO > findStoreGoodList(GoodStoreListVo goodListVo);

    /**
     * 查询规格商品里有0库存的商品ID和目前总库存
     * @return
     */
    List<Map<String,Object>>  getGoodStoreListIdAndRepertory();

    /**
     * 店铺商品回收站商品个数
     * @param sysUserId
     * @return
     */
    Integer getGoodStoreListdelFlag(String sysUserId);

    /**
     * 商家端店铺商品列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getGoodStoreListMaps(Page<GoodStoreList> page,Map<String,Object> paramMap);

    /**
     * 商家端普通商品查询列表
     * @param page
     * @param searchTermsVO
     * @return
     */
     IPage<Map<String,Object>> searchGoodListStore(Page<Map<String,Object>> page, SearchTermsVO searchTermsVO);


}
