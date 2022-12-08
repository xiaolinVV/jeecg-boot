package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.dto.MemberShoppingCartDto;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.mapper.MemberShoppingCartMapper;
import org.jeecg.modules.member.service.IMemberShoppingCartService;
import org.jeecg.modules.member.vo.MemberShoppingCartVo;
import org.jeecg.modules.order.service.IOrderListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 购物车商品
 * @Author: jeecg-boot
 * @Date:   2019-10-30
 * @Version: V1.0
 */
@Service
public class MemberShoppingCartServiceImpl extends ServiceImpl<MemberShoppingCartMapper, MemberShoppingCart> implements IMemberShoppingCartService {


    @Autowired
    @Lazy
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Autowired
    @Lazy
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;
    @Autowired
    @Lazy
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    @Lazy
    private IOrderListService iOrderListService;

    @Autowired
    @Lazy
    private IMarketingFreeGoodSpecificationService iMarketingFreeGoodSpecificationService;

    @Autowired
    @Lazy
    private IMarketingGroupRecordService iMarketingGroupRecordService;

    @Autowired
    @Lazy
    private IMarketingFreeBaseSettingService iMarketingFreeBaseSettingService;

    @Autowired
    @Lazy
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;

    @Autowired
    @Lazy
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IMarketingLeagueGoodSpecificationService iMarketingLeagueGoodSpecificationService;

    @Autowired
    private IMarketingLeagueSettingService iMarketingLeagueSettingService;


    @Override
    public IPage<MemberShoppingCartDto> getMemberShoppingCartVo(Page<MemberShoppingCartDto> page, MemberShoppingCartVo memberShoppingCartVo) {
        IPage<MemberShoppingCartDto> page1 = baseMapper.getMemberShoppingCartVo(page,memberShoppingCartVo);
        try {
            if(page1.getSize()>0){
                List<MemberShoppingCartDto> list=  page1.getRecords();
                if(list.size()>0){
                    String[]  strs;
                    DecimalFormat df = new DecimalFormat("0.00");
                    for(MemberShoppingCartDto memberShoppingCartDto:list){
                        if(memberShoppingCartDto.getPrice()==null  ||"".equals(memberShoppingCartDto.getPrice())){
                        }else{
                            strs =memberShoppingCartDto.getPrice().split("-");
                            memberShoppingCartDto.setPrice(strs[0]);
                            if(memberShoppingCartDto.getAddPrice()!=null){
                                Double addPrice = Double.valueOf(memberShoppingCartDto.getAddPrice());
                                Double price=Double.valueOf(memberShoppingCartDto.getPrice());
                                Double ss=addPrice-price;
                                if(ss>0){
                                    memberShoppingCartDto.setDepreciate(df.format(ss) );
                                }else {
                                    memberShoppingCartDto.setDepreciate("--");
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return page1;
    }

    @Override
    @Transactional
    public String addGoodToShoppingCart(Integer isPlatform,String goodId,String specification,String memberId,Integer quantity,String isView,
                                        String marketingPrefectureId,String marketingFreeGoodListId,String marketingGroupRecordId,String marketingStoreGiftCardMemberListId,String marketingRushGroupId,String marketingLeagueGoodListId) {

        MemberShoppingCart memberShoppingCart=null;
        //添加购物车
        if(isPlatform.intValue()==0){
            //获取规格商品数据信息进行判断
            GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getOne(new LambdaQueryWrapper<GoodStoreSpecification>()
                    .eq(GoodStoreSpecification::getGoodStoreListId,goodId)
                    .eq(GoodStoreSpecification::getSpecification,specification));
            if(goodStoreSpecification==null||goodStoreSpecification.getRepertory().longValue()<=0){
                return "商品规格数据不正确！！！";
            }

            //店铺商品
            //查询是否有商品
            memberShoppingCart=this.getOne(new LambdaQueryWrapper<MemberShoppingCart>()
                    .eq(MemberShoppingCart::getMemberListId,memberId)
                    .eq(MemberShoppingCart::getGoodStoreListId,goodId)
                    .eq(MemberShoppingCart::getIsView,"1")
                    .eq(MemberShoppingCart::getGoodStoreSpecificationId,goodStoreSpecification.getId())
                    .eq(StringUtils.isNotBlank(marketingStoreGiftCardMemberListId),MemberShoppingCart::getMarketingStoreGiftCardMemberListId,marketingStoreGiftCardMemberListId)
                    .eq(MemberShoppingCart::getGoodType,"0"));
            //根据情况添加数据
            if(memberShoppingCart!=null){
                //修改创建时间
                memberShoppingCart.setCreateTime(new Date());
                memberShoppingCart.setQuantity(memberShoppingCart.getQuantity().add(new BigDecimal(quantity)));
            }else{
                GoodStoreList goodStoreList=iGoodStoreListService.getById(goodId);
                memberShoppingCart=new MemberShoppingCart();
                memberShoppingCart.setDelFlag("0");
                memberShoppingCart.setMemberListId(memberId);
                memberShoppingCart.setGoodType("0");
                memberShoppingCart.setGoodStoreListId(goodId);
                memberShoppingCart.setAddTime(new Date());
                memberShoppingCart.setAddPrice(goodStoreSpecification.getPrice());
                memberShoppingCart.setGoodStoreSpecificationId(goodStoreSpecification.getId());
                memberShoppingCart.setQuantity(new BigDecimal(quantity));
                memberShoppingCart.setSysUserId(goodStoreList.getSysUserId());
            }

            //礼品卡商品
            if(StringUtils.isNotBlank(marketingStoreGiftCardMemberListId)){
                memberShoppingCart.setMarketingStoreGiftCardMemberListId(marketingStoreGiftCardMemberListId);
                isView="0";
            }

            //库存不足
            if(goodStoreSpecification.getRepertory().subtract(memberShoppingCart.getQuantity()).doubleValue()<0){
                return "商品库存不足！！！";
            }

        }else
        if(isPlatform.intValue()==1){
            //平台商品
            //获取规格商品数据信息进行判断
            GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                    .eq(GoodSpecification::getGoodListId,goodId)
                    .eq(GoodSpecification::getSpecification,specification));

            if(goodSpecification==null||goodSpecification.getRepertory().longValue()<=0){
               return "商品规格数据不正确！！！";
            }

            //查询是否有商品
            memberShoppingCart=this.getOne(new LambdaQueryWrapper<MemberShoppingCart>()
                    .eq(MemberShoppingCart::getMemberListId,memberId)
                    .eq(MemberShoppingCart::getGoodListId,goodId)
                    .eq(MemberShoppingCart::getIsView,"1")
                    .eq(MemberShoppingCart::getGoodSpecificationId,goodSpecification.getId())
                    .eq(StringUtils.isNotBlank(marketingPrefectureId),MemberShoppingCart::getMarketingPrefectureId,marketingPrefectureId)
                    .eq(StringUtils.isNotBlank(marketingFreeGoodListId),MemberShoppingCart::getMarketingFreeGoodListId,marketingFreeGoodListId)
                    .eq(StringUtils.isNotBlank(marketingLeagueGoodListId),MemberShoppingCart::getMarketingLeagueGoodListId,marketingLeagueGoodListId)
                    .eq(MemberShoppingCart::getGoodType,"1"));

            //根据情况添加数据
            if(memberShoppingCart!=null){
                memberShoppingCart.setCreateTime(new Date());
                memberShoppingCart.setQuantity(memberShoppingCart.getQuantity().add(new BigDecimal(quantity)));
            }else{
                memberShoppingCart=new MemberShoppingCart();
                memberShoppingCart.setDelFlag("0");
                memberShoppingCart.setMemberListId(memberId);
                memberShoppingCart.setGoodType("1");
                memberShoppingCart.setGoodListId(goodId);
                memberShoppingCart.setAddTime(new Date());
                memberShoppingCart.setGoodSpecificationId(goodSpecification.getId());
                memberShoppingCart.setQuantity(new BigDecimal(quantity));
                memberShoppingCart.setMarketingRushGroupId(marketingRushGroupId);
            }
            //库存不足
            if(goodSpecification.getRepertory().subtract(memberShoppingCart.getQuantity()).doubleValue()<0){
                return "商品库存不足！！！";
            }

            //普通商品
            if(StringUtils.isBlank(marketingPrefectureId)&&StringUtils.isBlank(marketingFreeGoodListId)&&StringUtils.isBlank(marketingLeagueGoodListId)) {
                memberShoppingCart.setAddPrice(goodSpecification.getPrice());
                memberShoppingCart.setMarketingPrefectureId(null);
            }
            //专区商品
            if(StringUtils.isNotBlank(marketingPrefectureId)){
                //获取专区商品
                MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(new LambdaQueryWrapper<MarketingPrefectureGood>()
                        .eq(MarketingPrefectureGood::getMarketingPrefectureId,marketingPrefectureId)
                        .eq(MarketingPrefectureGood::getGoodListId,goodId));
                if(marketingPrefectureGood==null){
                    return "专区商品不存在！！！";
                }

                //获取专区规格
                MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification=iMarketingPrefectureGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingPrefectureGoodSpecification>()
                        .eq(MarketingPrefectureGoodSpecification::getMarketingPrefectureGoodId,marketingPrefectureGood.getId())
                        .eq(MarketingPrefectureGoodSpecification::getGoodSpecificationId,goodSpecification.getId()));
                if(marketingPrefectureGoodSpecification==null){
                    return "专区商品规格数据库错误！！！";
                }

                memberShoppingCart.setAddPrice(marketingPrefectureGoodSpecification.getPrefecturePrice());
                //保存专区数据
                MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(marketingPrefectureId);
                if(marketingPrefecture!=null){
                    memberShoppingCart.setMarketingPrefectureId(marketingPrefectureId);
                    //专区标签
                    memberShoppingCart.setPrefectureLabel(marketingPrefecture.getPrefectureLabel());

                    //判断专区的购买限制
                    if(marketingPrefecture.getBuyLimit().equals("1")){
                        //需要做专区购买限制
                        Map<String,Object> paramMap= Maps.newHashMap();
                        paramMap.put("prefectureId",marketingPrefectureId);
                        paramMap.put("memberListId",memberId);
                        if(marketingPrefecture.getLimitCount().subtract(new BigDecimal(iOrderListService.getPrefectureGoodCount(paramMap)).add(memberShoppingCart.getQuantity())).intValue()<0){
                            return "抱歉，"+marketingPrefecture.getPrefectureName()+" 专区每人限购"+marketingPrefecture.getLimitCount()+"件，您的购买件数已达到最大限购件数，无法再购买。";
                        }
                    }

                    //控制商品自定义限购
                    if(marketingPrefecture.getBuyLimit().equals("0")){
                        //判断专区商品是否有限制
                        if(!marketingPrefectureGood.getBuyProportionLetter().equals("-1")) {
                            //需要做专区购买限制
                            Map<String, Object> paramMap = Maps.newHashMap();
                            paramMap.put("prefectureId", marketingPrefectureId);
                            paramMap.put("memberListId", memberId);
                            paramMap.put("goodListId", marketingPrefectureGood.getGoodListId());
                            if (new BigDecimal(marketingPrefectureGood.getBuyProportionLetter()).subtract(new BigDecimal(iOrderListService.getPrefectureGoodCount(paramMap)).add(memberShoppingCart.getQuantity())).intValue() < 0) {
                                return "抱歉，" + marketingPrefecture.getPrefectureName() + " 专区本商品每人限购" + marketingPrefectureGood.getBuyProportionLetter() + "件，您的购买件数已达到最大限购件数，无法再购买。";
                            }
                        }
                    }

                }
            }
            //免单活动商品
            if(StringUtils.isNotBlank(marketingFreeGoodListId)){
                //获取免单商品规格信息
                MarketingFreeGoodSpecification marketingFreeGoodSpecification=iMarketingFreeGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingFreeGoodSpecification>()
                        .eq(MarketingFreeGoodSpecification::getMarketingFreeGoodListId,marketingFreeGoodListId)
                        .eq(MarketingFreeGoodSpecification::getGoodSpecificationId,goodSpecification.getId()));
                if(marketingFreeGoodSpecification==null){
                    return "免单商品规格不存在！！！";
                }
                memberShoppingCart.setAddPrice(marketingFreeGoodSpecification.getFreePrice());
                memberShoppingCart.setMarketingFreeGoodListId(marketingFreeGoodListId);
                MarketingFreeBaseSetting marketingFreeBaseSetting=iMarketingFreeBaseSettingService.getOne(new LambdaQueryWrapper<MarketingFreeBaseSetting>());
                memberShoppingCart.setPrefectureLabel(marketingFreeBaseSetting.getLabel());
            }
            //加盟专区
            if(StringUtils.isNotBlank(marketingLeagueGoodListId)){
                MarketingLeagueGoodSpecification marketingLeagueGoodSpecification=iMarketingLeagueGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingLeagueGoodSpecification>()
                        .eq(MarketingLeagueGoodSpecification::getMarketingLeagueGoodListId,marketingLeagueGoodListId)
                        .eq(MarketingLeagueGoodSpecification::getGoodSpecificationId,goodSpecification.getId())
                        .last("limit 1"));
                if(marketingLeagueGoodSpecification==null){
                    return  "此专区商品不存在！！！";
                }
                memberShoppingCart.setAddPrice(marketingLeagueGoodSpecification.getLeaguePrice());
                memberShoppingCart.setMarketingLeagueGoodListId(marketingLeagueGoodListId);
                MarketingLeagueSetting  marketingLeagueSetting=iMarketingLeagueSettingService.getMarketingLeagueSetting();
                memberShoppingCart.setPrefectureLabel(marketingLeagueSetting.getLabel());
            }


            //中奖拼团商品
            if(StringUtils.isNotBlank(marketingGroupRecordId)){
                //获取拼团记录
                MarketingGroupRecord marketingGroupRecord=iMarketingGroupRecordService.getById(marketingGroupRecordId);
                //判断是否可以购买
                if(marketingGroupRecord.getRewardStatus().equals("0")){
                    return "中奖拼团记录没中奖";
                }
                if(marketingGroupRecord.getBuyEndTime().getTime()<new Date().getTime()){
                    return "拼团记录已超时";
                }
                if(marketingGroupRecord.getStatus().equals("1")){
                    return "拼团记录已使用";
                }
                memberShoppingCart.setAddPrice(marketingGroupRecord.getActivityPrice());
                memberShoppingCart.setMarketingGroupRecordId(marketingGroupRecordId);
                MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>());
                memberShoppingCart.setPrefectureLabel(marketingGroupBaseSetting.getLabel());
            }

        }else{
           return "商品isPlatform参数有问题";
        }
        memberShoppingCart.setIsView(isView);
        this.saveOrUpdate(memberShoppingCart);
        return "SUCCESS="+memberShoppingCart.getId();
    }

    @Override
    public String addGoodToShoppingCartCertificate(Integer isPlatform, String goodSpecificationId, String memberId, Integer quantity, String isView, String marketingCertificateRecordId) {
        MemberShoppingCart memberShoppingCart=null;
        //添加购物车
        if(isPlatform.intValue()==0){
            //获取规格商品数据信息进行判断

            GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getById(goodSpecificationId);

            if(goodStoreSpecification==null||goodStoreSpecification.getRepertory().longValue()<=0){
                return "商品规格数据不正确！！！";
            }
            //店铺商品
            //查询是否有商品
            QueryWrapper<MemberShoppingCart> memberShoppingCartQueryWrapper=new QueryWrapper<>();
            memberShoppingCartQueryWrapper.eq("member_list_id",memberId);
            memberShoppingCartQueryWrapper.eq("good_store_list_id",goodStoreSpecification.getGoodStoreListId());
            memberShoppingCartQueryWrapper.eq("is_view","1");
            memberShoppingCartQueryWrapper.eq("good_store_specification_id",goodStoreSpecification.getId());
            memberShoppingCartQueryWrapper.eq("good_type","0");
            memberShoppingCart=this.getOne(memberShoppingCartQueryWrapper);
            //根据情况添加数据
            if(memberShoppingCart!=null){
                //修改创建时间
                memberShoppingCart.setCreateTime(new Date());
                memberShoppingCart.setQuantity(memberShoppingCart.getQuantity().add(new BigDecimal(quantity)));
            }else{
                memberShoppingCart=new MemberShoppingCart();
                memberShoppingCart.setDelFlag("0");
                memberShoppingCart.setMemberListId(memberId);
                memberShoppingCart.setGoodType("0");
                memberShoppingCart.setGoodStoreListId(goodStoreSpecification.getGoodStoreListId());
                memberShoppingCart.setAddTime(new Date());
                memberShoppingCart.setAddPrice(goodStoreSpecification.getPrice());
                memberShoppingCart.setGoodStoreSpecificationId(goodStoreSpecification.getId());
                memberShoppingCart.setQuantity(new BigDecimal(quantity));
            }
            //库存不足
            if(goodStoreSpecification.getRepertory().subtract(memberShoppingCart.getQuantity()).doubleValue()<0){
                return "商品高库存不足！！！";
            }
        }else
        if(isPlatform.intValue()==1){
            //平台商品
            //获取规格商品数据信息进行判断
            GoodSpecification goodSpecification=iGoodSpecificationService.getById(goodSpecificationId);

            if(goodSpecification==null||goodSpecification.getRepertory().longValue()<=0){
                return "商品规格数据不正确！！！";
            }
            //查询是否有商品
            QueryWrapper<MemberShoppingCart> memberShoppingCartQueryWrapper=new QueryWrapper<>();
            memberShoppingCartQueryWrapper.eq("member_list_id",memberId);
            memberShoppingCartQueryWrapper.eq("good_list_id",goodSpecification.getGoodListId());
            memberShoppingCartQueryWrapper.eq("is_view","1");
            memberShoppingCartQueryWrapper.eq("good_specification_id",goodSpecification.getId());
            memberShoppingCartQueryWrapper.eq("good_type","1");
            memberShoppingCart=this.getOne(memberShoppingCartQueryWrapper);
            //根据情况添加数据
            if(memberShoppingCart!=null){
                memberShoppingCart.setCreateTime(new Date());
                memberShoppingCart.setQuantity(memberShoppingCart.getQuantity().add(new BigDecimal(quantity)));
            }else{
                memberShoppingCart=new MemberShoppingCart();
                memberShoppingCart.setDelFlag("0");
                memberShoppingCart.setMemberListId(memberId);
                memberShoppingCart.setGoodType("1");
                memberShoppingCart.setGoodListId(goodSpecification.getGoodListId());
                memberShoppingCart.setAddTime(new Date());
                memberShoppingCart.setGoodSpecificationId(goodSpecification.getId());
                memberShoppingCart.setQuantity(new BigDecimal(quantity));
            }
            if(StringUtils.isBlank(marketingCertificateRecordId)) {
                memberShoppingCart.setAddPrice(goodSpecification.getPrice());
                memberShoppingCart.setMarketingPrefectureId(null);
            }else{
               memberShoppingCart.setAddPrice(new BigDecimal(0));
               memberShoppingCart.setMarketingCertificateRecordId(marketingCertificateRecordId);
            }
            //库存不足
            if(goodSpecification.getRepertory().subtract(memberShoppingCart.getQuantity()).doubleValue()<0){
                return "商品库存不足！！！";
            }
        }else{
            return "商品isPlatform参数有问题";
        }
        memberShoppingCart.setIsView(isView);
        this.saveOrUpdate(memberShoppingCart);
        return "SUCCESS="+memberShoppingCart.getId();
    }
}
