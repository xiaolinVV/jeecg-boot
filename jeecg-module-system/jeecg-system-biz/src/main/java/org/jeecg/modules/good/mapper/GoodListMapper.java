package org.jeecg.modules.good.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.good.dto.GoodDiscountDTO;
import org.jeecg.modules.good.dto.GoodListDto;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.good.vo.SearchTermsVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date:   2019-10-18
 * @Version: V1.0
 */
public interface GoodListMapper extends BaseMapper<GoodList> {
IPage<GoodList> getGoodListdelFlagOrAuditStatus(Page page,@Param("delFlag") String delFlag,@Param("auditStatus") String auditStatus,QueryWrapper<GoodList> queryWrapper);
GoodList getGoodListById(@Param("id") String id);
void updateDelFalg(@Param("goodList")GoodList goodList,@Param("delFlag")String delFlag);
    /**
     * 上架，启用的商品集合
     * @return
     */
    public  List<GoodList>  getGoodListOK(QueryWrapper<GoodList> queryWrapper);

    IPage<GoodListDto> getGoodListDto(Page page,@Param("goodListVo")GoodListVo goodListVo,@Param("notauditStatus")String notauditStatus);

    IPage<GoodListDto> getGoodListDtoDelFlag(Page page,@Param("goodListVo")GoodListVo goodListVo);


    public IPage<Map<String,Object>> findGoodListByGoodType(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);


    public IPage<Map<String,Object>> searchGoodList(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);



    /**
     * 张靠勤    2020-3-29
     *
     * 选折商品列表
     * 1、免单活动新增商品选择商品接口
     * 2、中奖拼团新增商品选择商品接口
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> chooseGoodList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

    public Map<String,Object> findGoodListByGoodId(@Param("goodId") String goodId);

    /**
     * 通过类型id和级别查询商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findGoodListByGoodTypeAndlevel(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    /**
     *每日上新返回goodTypeId 集合 根据个数倒叙
     * @param createTime
     * @param limit
     * @return
     */
   List<Map<String,Object>>  getEverydayGoodTypeId(@Param("createTime")String createTime,@Param("limit")Integer limit);


    /**
     * 每周特惠查询
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> getEveryWeekPreferential(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    IPage<GoodDiscountDTO> findGoodList(Page<GoodListVo> page,@Param("goodListVo") GoodListVo goodListVo);

    /**
     * 查询出专区商品数据
     * @param goodListVo
     * @return
     */
    IPage<Map<String,Object>>   getMarketingPrefectureGood(Page<GoodList> page,@Param("goodListVo") GoodListVo goodListVo);

    /**
     * 选中专区商品数据
     * @param goodListVo
     * @return
     */
    List<Map<String,Object>>   getMarketingPrefectureGood(@Param("goodListVo") GoodListVo goodListVo);

    /**
     * 选中素材商品数据
     * @param goodListVo
     * @return
     */
    List<Map<String,Object>>  getMarketingMaterialGood(@Param("goodListVo") GoodListVo goodListVo);

    /**
     * 查询规格商品里有0库存的商品ID和目前总库存
     * @return
     */
    List<Map<String,Object>>  getGoodListIdAndRepertory();

    /**
     * 商家端普通商品列表查询
     * @param page
     * @param searchTermsVO
     * @return
     */
    public IPage<Map<String,Object>> searchGoodListStore(Page<Map<String,Object>> page, @Param("searchTermsVO") SearchTermsVO searchTermsVO);


}
