package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.member.dto.MemberGradeDTO;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.vo.MemberGradeVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员等级
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
public interface IMemberGradeService extends IService<MemberGrade> {

    IPage<MemberGradeVO> queryPageList(Page<MemberGrade> page, MemberGradeDTO memberGradeDTO);

    List<Map<String,Object>> findMemberGradeListMap();

    List<Map<String,String>> getReferMemberGradeList();

    /*会员等级设置商品详情信息*/
    public void settingGoodInfo(Map<String,Object> resultMap, GoodSpecification goodSpecification,String memberId);


    /*会员等级设置商品列表信息*/
    public void settingGoodListInfo(Map<String,Object> resultMap,String goodId,String memberId);


    /*会员等级设置商品规格*/
    public void settingGoodSpecificationInfo(Map<String,Object> resultMap, GoodSpecification goodSpecification,String memberId);

    /*购物车信息设置*/
    public void settingMemberShopCardinfo(MemberShoppingCart memberShoppingCart, GoodSpecification goodSpecification, String memberId);


    /*购物车信息设置*/
    public void settingGetMemberShopCardinfo(Map<String,Object> resultMap, GoodSpecification goodSpecification, String memberId);
}
