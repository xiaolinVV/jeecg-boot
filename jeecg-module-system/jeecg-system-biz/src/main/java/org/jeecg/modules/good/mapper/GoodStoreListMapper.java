package org.jeecg.modules.good.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface GoodStoreListMapper extends BaseMapper<GoodStoreList> {


    /*
     * 后端列表
     * */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap,@Param(Constants.WRAPPER) QueryWrapper wrapper);



    /**
     * 商品选择组件
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectGood(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);


    List<GoodStoreList> getGoodStoreListById(@Param("id") String id);
    void updateDelFalg(@Param("id")String id,@Param("delFlag")String delFlag);
    IPage<GoodStoreListDto> getGoodListDto(Page page,@Param("goodListVo") GoodStoreListVo goodListVo,@Param("notauditStatus")String notauditStatus);
    IPage<GoodStoreListDto> getGoodListDtoDelFlag(Page page,@Param("goodListVo") GoodStoreListVo goodListVo);

    public IPage<Map<String,Object>> findGoodListByGoodType(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    public IPage<Map<String,Object>> searchGoodList(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    public Map<String,Object> findGoodListByGoodId(@Param("goodId") String goodId);


    /**
     * 根据店铺id查询商品
     *
     * @param page
     * @param sysUserId
     * @return
     */
    public IPage<Map<String,Object>> findGoodListBySysUserId(Page<Map<String,Object>> page, @Param("sysUserId")String sysUserId);
    public List<Map<String,Object>> findGoodListBySysUserIds(@Param("sysUserIds")List<String> sysUserIds);
    /**
     *每日上新返回goodTypeId 集合 根据个数倒叙
     * @param createTime
     * @param limit
     * @return
     */
    List<Map<String,Object>>  getEverydayGoodStoreTypeId(@Param("sysUserId")String sysUserId,@Param("createTime")String createTime,@Param("limit")Integer limit);
    /**
     * 每周特惠查询
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getEveryWeekPreferential(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    List<GoodStoreDiscountDTO > findStoreGoodList(@Param("goodListVo") GoodStoreListVo goodListVo);

    /**
     * 查询规格商品里有0库存的商品ID和目前总库存
     * @return
     */
    List<Map<String,Object>>  getGoodStoreListIdAndRepertory();

    Integer getGoodStoreListdelFlag(@Param("sysUserId")String sysUserId);

    /**
     * 商家端店铺商品列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getGoodStoreListMaps(Page<GoodStoreList> page, @Param("paramMap") Map<String,Object> paramMap);

    /**
     * 商家端普通商品查询列表
     * @param page
     * @param searchTermsVO
     * @return
     */
    public IPage<Map<String,Object>> searchGoodListStore(Page<Map<String,Object>> page, @Param("searchTermsVO") SearchTermsVO searchTermsVO);

}
