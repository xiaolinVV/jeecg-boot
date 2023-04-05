package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.dto.GoodDiscountDTO;
import org.jeecg.modules.good.dto.GoodListDto;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.good.vo.SearchTermsVO;
import org.jeecg.modules.member.entity.MemberShoppingCart;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date:   2019-10-18
 * @Version: V1.0
 */
public interface IGoodListService extends IService<GoodList> {



    /*
     * 后端列表
     * */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, Map<String,Object> paramMap, QueryWrapper wrapper);




    public GoodList getGoodListById(String id);
    public void updateDelFalg(GoodList goodList,String delFlag);
    public GoodListDto selectById(String id);
    boolean saveOrUpdate(GoodListVo goodListVo);

    /**
     * 平台商品分页查询
     * @param page
     * @param goodListVo
     * @param notauditStatus
     * @return
     */
    IPage<GoodListDto> getGoodListDto(Page<GoodList> page, GoodListVo goodListVo,String notauditStatus);

    IPage<GoodListDto> getGoodListDtoDelFlag(Page<GoodList> page, GoodListVo goodListVo);
    /**
     * 通过类型id查询商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findGoodListByGoodType(Page<Map<String,Object>> page, Map<String,Object> paramMap);

    /**
     * 搜索商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> searchGoodList(Page<Map<String,Object>> page, Map<String,Object> paramMap);


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
    public IPage<Map<String,Object>> chooseGoodList(Page<Map<String,Object>> page, Map<String,Object> paramMap);


    /**
     * 通过类型id和级别查询商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findGoodListByGoodTypeAndlevel(Page<Map<String,Object>> page, Map<String,Object> paramMap);

    /**
     * 根据商品id获取商品详情
     * @param goodId
     * @return
     */
    public Map<String,Object> findGoodListByGoodId(String goodId);


    /**
     * 获取购物车需要的数据
     * @param memberShoppingCarts
     * @return
     */
    public Map<String,Object> getCarGoodByMemberId(List<MemberShoppingCart> memberShoppingCarts,String membetId);

    /**
     *每日上新返回goodTypeId 集合 根据个数倒叙
     * @param createTime
     * @param limit
     * @return
     */
    List<Map<String,Object>>  getEverydayGoodTypeId(@Param("createTime")String createTime, @Param("limit")Integer limit);


    IPage<GoodDiscountDTO> findGoodList(Page<GoodListVo>page,GoodListVo goodListVo);

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
    List<Map<String,Object>>   getMarketingPrefectureGoodPitchOn(GoodListVo goodListVo);

    /**
     * 定时器下架0库存的商品
     * @return
     */
    public void updateGoodListISRepertoryZero();


    /**
     * 判断是否要重新审核
     * @param goodListVo
     * @return
     */
    public GoodListVo isReExamination(GoodListVo goodListVo,String sysUserId);

    /**
     * 查询商品编号是否存在
     * @param goodId
     * @param isPlatform
     * @param goodNo
     * @return
     */
     long getGoodNoCount(String goodId,Integer isPlatform,String goodNo);

    /**
     * 选中素材商品数据
     * @param goodListVo
     * @return
     */
    List<Map<String,Object>>  getMarketingMaterialGood( GoodListVo goodListVo);

    /**
     * 修改商品价格判断专区商品是否不符合要求
     *
     * @param goodId

     * @return
     */

    public void updateMarketingPrefectureGood(String goodId);


    /**
     * 商家端普通商品列表查询
     * @param page
     * @param searchTermsVO
     * @return
     */
    public IPage<Map<String,Object>> searchGoodListStore(Page<Map<String,Object>> page,  SearchTermsVO searchTermsVO);

    /**
     * 批量修改上下架
     * @param ids 商品id,逗号分隔
     * @param frameStatus 上下架；0：下架；1：上架
     * @param frameExplain 上下架说明
     * @return
     */
    Result<GoodList> updateFrameStatus(String ids, String frameStatus, String frameExplain);

    Result<?> deleteAndDelExplain(String id, String delExplain);

}
