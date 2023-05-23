package org.jeecg.modules.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.marketing.dto.MarketingDisountGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.entity.MarketingDiscountCouponRecord;
import org.jeecg.modules.marketing.entity.MarketingStoreDistributionSetting;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardMemberList;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGoodService;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.order.dto.OrderStoreGoodRecordDTO;
import org.jeecg.modules.order.dto.OrderStoreListDTO;
import org.jeecg.modules.order.dto.OrderStoreListExportDTO;
import org.jeecg.modules.order.dto.OrderStoreSubListDTO;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import org.jeecg.modules.order.mapper.OrderStoreListMapper;
import org.jeecg.modules.order.service.*;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.order.vo.OrderStoreListVO;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.store.dto.StoreAddressDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 商品订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-30
 * @Version: V1.0
 */
@Service
@Slf4j
public class OrderStoreListServiceImpl extends ServiceImpl<OrderStoreListMapper, OrderStoreList> implements IOrderStoreListService {
    @Autowired(required = false)
    private OrderStoreListMapper orderStoreListMapper;

    @Autowired
    @Lazy
    private IMemberListService iMemberListService;

    @Autowired
    @Lazy
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    @Autowired
    @Lazy
    private IStoreManageService iStoreManageService;

    @Autowired
    @Lazy
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    @Lazy
    private IOrderStoreSubListService iOrderStoreSubListService;

    @Autowired
    @Lazy
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Autowired
    @Lazy
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    @Lazy
    private IOrderStoreGoodRecordService iOrderStoreGoodRecordService;

    @Autowired
    @Lazy
    private IOrderStoreGoodRecordService orderStoreGoodRecordService;
    @Autowired
    @Lazy
    private ISysUserService sysUserService;
    @Autowired
    @Lazy
    private IStoreAddressService storeAddressService;

    @Autowired
    @Lazy
    private IStoreTemplateService iStoreTemplateService;
    @Autowired
    @Lazy
    private ISysDictService iSysDictService;
    @Autowired
    @Lazy
    private IMarketingStoreDistributionSettingService iMarketingStoreDistributionSettingService;
    @Autowired
    @Lazy
    private IMemberRechargeRecordService iMemberRechargeRecordService;
    @Autowired
    @Lazy
    private IMemberDistributionRecordService iMemberDistributionRecordService;
    @Autowired
    @Lazy
    private IMemberAccountCapitalService iMemberAccountCapitalService;

    @Autowired
    @Lazy
    private IMemberGradeService iMemberGradeService;

    @Autowired
    @Lazy
    private IMemberGrowthRecordService iMemberGrowthRecordService;
    @Autowired
    @Lazy
    private IOrderStoreTemplateService iOrderStoreTemplateService;

    @Autowired
    @Lazy
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    @Lazy
    private IMarketingStoreGiftCardMemberListService iMarketingStoreGiftCardMemberListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;


    @Autowired
    private PayUtils payUtils;

    @Autowired
    private HftxPayUtils hftxPayUtils;

    @Autowired
    @Lazy
    private IStoreCashierRoutingService iStoreCashierRoutingService;

    @Autowired
    private IStoreOrderSettingService iStoreOrderSettingService;

    @Autowired
    private IMarketingStorePrefectureGoodService iMarketingStorePrefectureGoodService;

    @Autowired
    private IMarketingDiscountGoodService marketingDiscountGoodService;

    @Autowired
    private IMarketingDiscountCouponRecordService marketingDiscountCouponRecordService;

    @Autowired
    private IOrderStoreSubListService orderStoreSubListService;

    @Autowired
    private IOrderRefundListService orderRefundListService;

    @Autowired
    private IMemberWelfarePaymentsService memberWelfarePaymentsService;

    @Override
    public IPage<OrderStoreListDTO> getOrderStoreListDto(Page<OrderStoreList> page, OrderStoreListVO orderListVO, String sysUserId) {
        IPage<OrderStoreListDTO> page1 = orderStoreListMapper.getOrderStoreListDto(page, orderListVO);


        page1.getRecords().forEach(ol -> {
            //会员信息
            MemberList memberList = iMemberListService.getMemberListById(ol.getMemberListId());
            if (memberList != null) {
                ol.setMemberList(memberList);
            }
            if (StringUtils.isNotBlank(ol.getMarketingDiscountCouponId())) {
                //优惠券信息
                MarketingDiscountCoupon marketingDiscountCoupon = iMarketingDiscountCouponService.getById(ol.getMarketingDiscountCouponId());
                if (marketingDiscountCoupon != null) {
                    ol.setMarketingDiscountCoupon(marketingDiscountCoupon);
                }
            }
            List<OrderStoreSubListDTO> orderProviderLists;
            if ("2".equals(ol.getStatus()) || "3".equals(ol.getStatus()) || "5".equals(ol.getStatus())) {
                //发货后的商品信息
                orderProviderLists = iOrderStoreSubListService.selectorderStoreListId(ol.getId(), sysUserId, null, "0");
            } else {
                //未发货的商品信息
                //查询供应商订单信息 sysUserId：null为平台登录，不为null为供应商登录
                orderProviderLists = iOrderStoreSubListService.selectorderStoreListId(ol.getId(), sysUserId, "0", null);
            }

            orderProviderLists.forEach(opl -> {
                SysUser sysUser = sysUserService.getById(opl.getSysUserId());
                if (sysUser != null) {
                    opl.setSysUserName(sysUser.getRealname());
                }
                List<OrderStoreGoodRecord> orderProviderGoodRecords = orderStoreGoodRecordService.selectOrderStoreSubListId(opl.getId());
                //添加供应商订单商品记录
                opl.setOrderStoreGoodRecords(orderProviderGoodRecords);

                List<OrderStoreGoodRecordDTO> orderStoreGoodRecordDTOList = orderStoreGoodRecordService.selectOrderStoreSubListIdDTO(opl.getId());
                //添加供应商订单商品记录
                opl.setOrderStoreGoodRecordDTOList(orderStoreGoodRecordDTOList);


                //供应商发货信息
                Map<String, String> paramMap = Maps.newHashMap();
                paramMap.put("id", opl.getStoreAddressIdSender());
                if (opl.getStoreAddressIdSender() == null || "".equals(opl.getStoreAddressIdSender())) {
                    paramMap.put("sysUserId", opl.getSysUserId());
                    paramMap.put("isDeliver", "1");//发货默认
                    paramMap.put("isReturn", "");//退货
                }
                List<StoreAddressDTO> listStoreAddressDTO = storeAddressService.getlistStoreAddress(paramMap);
                if (listStoreAddressDTO.size() > 0) {
                    opl.setStoreAddressDTOFa(listStoreAddressDTO.get(0));
                }
                //供应商退信息
                Map<String, String> paramMaptui = Maps.newHashMap();
                paramMaptui.put("id", opl.getStoreAddressIdTui());
                if (opl.getStoreAddressIdTui() == null || "".equals(opl.getStoreAddressIdTui())) {
                    paramMaptui.put("sysUserId", opl.getSysUserId());
                    paramMaptui.put("isDeliver", "");//发货默认
                    paramMaptui.put("isReturn", "1");//退货
                }
                List<StoreAddressDTO> listStoreAddressDTOTui = storeAddressService.getlistStoreAddress(paramMaptui);
                if (listStoreAddressDTOTui.size() > 0) {
                    opl.setStoreAddressDTOTui(listStoreAddressDTOTui.get(0));
                }


            });
            //添加供应商订单列表
            if (orderProviderLists.size() > 0) {
                ol.setOrderStoreSubListDTOs(orderProviderLists);

                List<String> OrderStoreSubIdList = new ArrayList<>();
                //查询运费模板信息
                for (OrderStoreSubListDTO opl : orderProviderLists) {
                    OrderStoreSubIdList.add(opl.getId());
                }
                List<Map<String, Object>> providerTemplateMaps = new ArrayList<>();
                OrderStoreSubIdList = OrderStoreSubIdList.stream().distinct().collect(Collectors.toList());
                //查询运费模板信息
                providerTemplateMaps = iOrderStoreTemplateService.getOrderStoreTemplateMaps(OrderStoreSubIdList);
                if (providerTemplateMaps.size() == 0) {
                    //如果未生成运费模板信息,查询运费模板匹配信息
                    providerTemplateMaps = iStoreTemplateService.getStoreTemplateMaps(OrderStoreSubIdList);
                }
                //添加运费模板信息
                ol.setStoreTemplateMaps(providerTemplateMaps);
            }
        });

        return page1;
    }

    //发货后信息
    @Override
    public IPage<OrderStoreListDTO> getOrderListDtoWaitForReceiving(Page<OrderStoreList> page, OrderStoreListVO orderListVO, String sysUserId) {
        IPage<OrderStoreListDTO> page1 = orderStoreListMapper.getOrderStoreListDto(page, orderListVO);
        page1.getRecords().forEach(ol -> {
            //会员信息
            MemberList memberList = iMemberListService.getMemberListById(ol.getMemberListId());
            if (memberList != null) {
                ol.setMemberList(memberList);
            }
            //优惠券信息
            MarketingDiscountCoupon marketingDiscountCoupon = iMarketingDiscountCouponService.getById(ol.getMarketingDiscountCouponId());
            if (marketingDiscountCoupon != null) {
                ol.setMarketingDiscountCoupon(marketingDiscountCoupon);
            }
            //查询供应商订单信息 sysUserId：null为平台登录，不为null为供应商登录
            List<OrderStoreSubListDTO> orderProviderLists = iOrderStoreSubListService.selectorderStoreListId(ol.getId(), sysUserId, null, "0");
            orderProviderLists.forEach(opl -> {
                SysUser sysUser = sysUserService.getById(opl.getSysUserId());
                if (sysUser != null) {
                    opl.setSysUserName(sysUser.getRealname());
                }
                List<OrderStoreGoodRecord> orderProviderGoodRecords = orderStoreGoodRecordService.selectOrderStoreSubListId(opl.getId());
                //添加供应商订单商品记录
                opl.setOrderStoreGoodRecords(orderProviderGoodRecords);
                List<OrderStoreGoodRecordDTO> orderStoreGoodRecordDTOList = orderStoreGoodRecordService.selectOrderStoreSubListIdDTO(opl.getId());
                //添加供应商订单商品记录
                opl.setOrderStoreGoodRecordDTOList(orderStoreGoodRecordDTOList);

                //供应商发货信息
                Map<String, String> paramMap = Maps.newHashMap();
                paramMap.put("id", opl.getStoreAddressIdSender());
                if (opl.getStoreAddressIdSender() == null || "".equals(opl.getStoreAddressIdSender())) {
                    paramMap.put("sysUserId", opl.getSysUserId());
                    paramMap.put("isDeliver", "1");//发货默认
                    paramMap.put("isReturn", "");//退货
                }
                List<StoreAddressDTO> listStoreAddressDTO = storeAddressService.getlistStoreAddress(paramMap);
                if (listStoreAddressDTO.size() > 0) {
                    opl.setStoreAddressDTOFa(listStoreAddressDTO.get(0));
                }
                //供应商退信息
                Map<String, String> paramMaptui = Maps.newHashMap();
                paramMaptui.put("id", opl.getStoreAddressIdTui());
                if (opl.getStoreAddressIdTui() == null || "".equals(opl.getStoreAddressIdTui())) {
                    paramMaptui.put("sysUserId", opl.getSysUserId());
                    paramMaptui.put("isDeliver", "");//发货默认
                    paramMaptui.put("isReturn", "1");//退货
                }
                List<StoreAddressDTO> listStoreAddressDTOTui = storeAddressService.getlistStoreAddress(paramMaptui);
                if (listStoreAddressDTOTui.size() > 0) {
                    opl.setStoreAddressDTOTui(listStoreAddressDTOTui.get(0));
                }
            });
            //添加供应商订单列表
            if (orderProviderLists.size() > 0) {
                ol.setOrderStoreSubListDTOs(orderProviderLists);
                List<String> OrderStoreSubIdList = new ArrayList<>();
                //查询运费模板信息
                for (OrderStoreSubListDTO opl : orderProviderLists) {
                    OrderStoreSubIdList.add(opl.getId());
                }
                OrderStoreSubIdList = OrderStoreSubIdList.stream().distinct().collect(Collectors.toList());
                List<Map<String, Object>> providerTemplateMaps = new ArrayList<>();
                //查询运费模板信息
                providerTemplateMaps = iOrderStoreTemplateService.getOrderStoreTemplateMaps(OrderStoreSubIdList);
                if (providerTemplateMaps.size() == 0) {
                    //如果未生成运费模板信息,查询运费模板匹配信息
                    providerTemplateMaps = iStoreTemplateService.getStoreTemplateMaps(OrderStoreSubIdList);
                }
                //添加运费模板信息
                ol.setStoreTemplateMaps(providerTemplateMaps);
            }
        });

        return page1;
    }


    @Override
    @Transactional
    public OrderStoreList submitOrderStoreGoods(Map<String, Object> storeGood,
                                                String memberId, String orderJson,
                                                MemberShippingAddress memberShippingAddress,
                                                String longitude, String latitude) {

        //总运费
        BigDecimal freight = new BigDecimal(0);
        //价格
        BigDecimal totalPrice = new BigDecimal(0);
        //店铺成本价
        BigDecimal costPrice = new BigDecimal(0);
        //件数
        BigDecimal allNumberUnits = new BigDecimal(0);
        //获取用户信息
        MemberList memberList = iMemberListService.getById(memberId);
        //解析订单json
        JSONObject orderJsonObject = JSON.parseObject(orderJson);
        JSONArray storeGoods = orderJsonObject.getJSONArray("storeGoods");
        JSONObject jsonGoods = null;
        for (Object s : storeGoods) {
            JSONObject ss = (JSONObject) s;
            if (storeGood.get("id").toString().equals(ss.getString("id"))) {
                jsonGoods = ss;
                break;
            }
        }

        if (jsonGoods == null) {
            throw new JeecgBootException("传入的店铺id有误~");
        }


        //建立店铺订单
        OrderStoreList orderStoreList = new OrderStoreList();
        orderStoreList.setDelFlag("0");
        //普通订单
        orderStoreList.setOrderType("0");
        //订单号
        orderStoreList.setOrderNo(OrderNoUtils.getOrderNo());
        //会员设置
        orderStoreList.setMemberListId(memberId);
        //收货地址设置
        orderStoreList.setConsignee(memberShippingAddress.getLinkman());
        orderStoreList.setContactNumber(memberShippingAddress.getPhone());
        orderStoreList.setShippingAddress(memberShippingAddress.getAreaExplan() + memberShippingAddress.getAreaAddress());
        orderStoreList.setHouseNumber(memberShippingAddress.getHouseNumber());
        //设置留言
        orderStoreList.setMessage(jsonGoods.getString("message"));
        orderStoreList.setCoupon(new BigDecimal(0));
        //设置无修改地址
        orderStoreList.setIsUpdateAddr("0");
        //设置订单状态
        orderStoreList.setStatus("0");
        //推广人设置
        String promoterType = memberList.getPromoterType();
        String promoter = memberList.getPromoter();
        if (StringUtils.isNotBlank(promoter)) {
            if (promoterType.equals("2")) {
                orderStoreList.setPromoter("平台")
                        .setPromoterType("2");
            } else {
                orderStoreList
                        .setPromoter(promoter)
                        .setPromoterType(promoterType);
            }
        }

        //归属店铺
        orderStoreList.setSysUserId(storeGood.get("id").toString());


        //配送方式
        orderStoreList.setDistribution(jsonGoods.getString("distribution"));

        //是否部分发货
        orderStoreList.setIsSender("0");

        //设置未评价
        orderStoreList.setIsEvaluate("0");

        //保存订单信息
        this.saveOrUpdate(orderStoreList);

        //建立sub子订单信息

        //商品总价(成本价)
        BigDecimal goodsTotal = new BigDecimal(0);
        //建立供应商订单
        OrderStoreSubList orderStoreSubList = new OrderStoreSubList();
        orderStoreSubList.setDelFlag("0");
        orderStoreSubList.setMemberListId(memberList.getId());
        //设置订单id
        orderStoreSubList.setOrderStoreListId(orderStoreList.getId());
        //设置店铺id
        orderStoreSubList.setSysUserId(storeGood.get("id").toString());
        //设置订单号
        orderStoreSubList.setOrderNo(OrderNoUtils.getOrderNo());
        orderStoreSubList.setDistribution(orderStoreList.getDistribution());
        orderStoreSubList.setParentId("0");
        orderStoreSubList.setStatus("0");
        orderStoreSubList.setShipFee(orderStoreList.getShipFee());
        //保存订单信息
        iOrderStoreSubListService.saveOrUpdate(orderStoreSubList);
        //建立店铺商品快照

        List<Map<String, Object>> myStoreGoods = (List<Map<String, Object>>) storeGood.get("myStoreGoods");
        //有地址计算运费
        if (memberShippingAddress != null) {
            freight = iStoreTemplateService.calculateFreight(myStoreGoods, memberShippingAddress.getSysAreaId());
            //添加店铺运费模板信息
            iStoreTemplateService.addOrderStoreTemplate(myStoreGoods, memberShippingAddress.getSysAreaId(), orderStoreSubList);

        }
        orderStoreList.setShipFee(freight);
        orderStoreSubList.setShipFee(freight);
        //商品礼品卡金额
        BigDecimal goodGiftCardtotal = new BigDecimal(0);
        String marketingStoreGiftCardMemberListId = null;
        String marketingStorePrefectureGoodId = null;

        String storeId = storeGood.get("id").toString();
        //折扣券优惠相关返回参数
        Map<String, Object> settleMap = MapUtil.newHashMap();
        BigDecimal marketingTotalPrice = new BigDecimal(0); //可优惠金额
        BigDecimal noMarketingTotalPrice = new BigDecimal(0); //不可优惠金额
        List<String> marketingGoodIds = CollUtil.newArrayList(); //优惠商品
        List<String> noMarketingGoodIds = CollUtil.newArrayList();  //无优惠商品
        BigDecimal coupon = BigDecimal.ZERO;

        MarketingDiscountCoupon marketingDiscountCoupon = null;
        List<String> marketingDiscountCouponGoodIds = CollUtil.newArrayList(); //优惠券对应的商品ID列表
        //礼品卡ID不为空，计算折扣券优惠金额
        String discountId = jsonGoods.getString("discountId");
        //优惠券ID 不为空，则计算优惠金额
        if (StrUtil.isNotBlank(discountId)) {
            marketingDiscountCoupon = iMarketingDiscountCouponService.getById(discountId);
            if (marketingDiscountCoupon != null
                    && StrUtil.equals(marketingDiscountCoupon.getStatus(), "1") && StrUtil.equals(storeId, marketingDiscountCoupon.getSysUserId())) {
                List<MarketingDisountGoodDTO> storeGoodList = marketingDiscountGoodService.findStoreGood(marketingDiscountCoupon.getMarketingDiscountId());
                marketingDiscountCouponGoodIds = storeGoodList.stream().map(MarketingDisountGoodDTO::getId).collect(Collectors.toList());
            }
        }

        // 参与优惠券优惠的店铺商品列表，暂存，用于后面更新商品实付金额 --- by zhangshaolin
        List<OrderStoreGoodRecord> marketingOrderStoreGoodRecords = CollUtil.newArrayList();
        List<OrderStoreGoodRecord> goodGiftCardGoodRecords = CollUtil.newArrayList();
        for (Map<String, Object> m : myStoreGoods) {
            GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(m.get("goodSpecificationId").toString());
            GoodStoreList goodStoreList = iGoodStoreListService.getById(m.get("goodId").toString());
            goodsTotal = goodsTotal.add(goodStoreSpecification.getCostPrice());
            //添加商品记录
            OrderStoreGoodRecord orderStoreGoodRecord = new OrderStoreGoodRecord();
            orderStoreGoodRecord.setDelFlag("0");
            orderStoreGoodRecord.setOrderStoreSubListId(orderStoreSubList.getId());
            orderStoreGoodRecord.setMainPicture(goodStoreList.getMainPicture());
            orderStoreGoodRecord.setGoodStoreListId(goodStoreList.getId());
            orderStoreGoodRecord.setGoodStoreSpecificationId(goodStoreSpecification.getId());
            orderStoreGoodRecord.setGoodName(goodStoreList.getGoodName());
            orderStoreGoodRecord.setSpecification(goodStoreSpecification.getSpecification());
            orderStoreGoodRecord.setPrice(goodStoreSpecification.getPrice());
            orderStoreGoodRecord.setVipPrice(goodStoreSpecification.getVipPrice());
            orderStoreGoodRecord.setCostPrice(goodStoreSpecification.getCostPrice());
            orderStoreGoodRecord.setUnitPrice((BigDecimal) m.get("price"));
            orderStoreGoodRecord.setAmount((BigDecimal) m.get("quantity"));
            orderStoreGoodRecord.setTotal(orderStoreGoodRecord.getUnitPrice().multiply(orderStoreGoodRecord.getAmount()));
            orderStoreGoodRecord.setCustomaryDues(orderStoreGoodRecord.getUnitPrice().multiply(orderStoreGoodRecord.getAmount()));
            orderStoreGoodRecord.setActualPayment(orderStoreGoodRecord.getUnitPrice().multiply(orderStoreGoodRecord.getAmount()));
            orderStoreGoodRecord.setCoupon(BigDecimal.ZERO);
            orderStoreGoodRecord.setGiftCardCoupon(BigDecimal.ZERO);
            orderStoreGoodRecord.setTotalCoupon(BigDecimal.ZERO);
            //商品总重量
            orderStoreGoodRecord.setWeight(goodStoreSpecification.getWeight().multiply(new BigDecimal(m.get("quantity").toString())).setScale(3, RoundingMode.DOWN));
            iOrderStoreGoodRecordService.save(orderStoreGoodRecord);
            goodStoreSpecification.setRepertory(goodStoreSpecification.getRepertory().subtract(orderStoreGoodRecord.getAmount()));
            iGoodStoreListService.saveOrUpdate(goodStoreList);
            iGoodStoreSpecificationService.saveOrUpdate(goodStoreSpecification);

            //计算总价格
            totalPrice = totalPrice.add(orderStoreGoodRecord.getTotal());

            //判断礼品卡商品和金额
            if (m.get("marketingStoreGiftCardMemberListId") != null) {
                goodGiftCardtotal = goodGiftCardtotal.add(((BigDecimal) m.get("price")).multiply((BigDecimal) m.get("quantity")));
                if (StringUtils.isBlank(marketingStoreGiftCardMemberListId)) {
                    marketingStoreGiftCardMemberListId = m.get("marketingStoreGiftCardMemberListId").toString();
                }
                goodGiftCardGoodRecords.add(orderStoreGoodRecord);
            }
            if (StrUtil.isBlank(marketingStoreGiftCardMemberListId)) {
                //判断哪些商品有优惠、哪些商品没优惠、并且计算优惠金额
                if (marketingDiscountCoupon != null
                        && StrUtil.equals(marketingDiscountCoupon.getStatus(), "1") && StrUtil.equals(storeId, marketingDiscountCoupon.getSysUserId())) {
                    if (marketingDiscountCouponGoodIds.contains(Convert.toStr(m.get("goodId")))) {
                        marketingGoodIds.add(Convert.toStr(m.get("goodId")));
                        //可优惠总金额
                        marketingTotalPrice = marketingTotalPrice.add(((BigDecimal) m.get("price")).multiply((BigDecimal) m.get("quantity")));
                        marketingOrderStoreGoodRecords.add(orderStoreGoodRecord);
                        //设置优惠券记录id
                        orderStoreGoodRecord.setMarketingDiscountCouponId(marketingDiscountCoupon.getId());
                        iOrderStoreGoodRecordService.updateById(orderStoreGoodRecord);
                    } else {
                        noMarketingGoodIds.add(Convert.toStr(m.get("goodId")));
                        noMarketingTotalPrice = noMarketingTotalPrice.add(((BigDecimal) m.get("price")).multiply((BigDecimal) m.get("quantity")));
                    }
                }
            }
            /*店铺专区*/
            if (m.get("marketingStorePrefectureGoodId") != null) {
                marketingStorePrefectureGoodId = m.get("marketingStorePrefectureGoodId").toString();
            }
            //店铺成本价
            costPrice = costPrice.add(goodStoreSpecification.getCostPrice().multiply((BigDecimal) m.get("quantity")));
            //商品总件数
            allNumberUnits = allNumberUnits.add((BigDecimal) m.get("quantity"));
        }
        //商品总价(成本价)
        orderStoreSubList.setGoodsTotal(goodsTotal);
        //应付款
        orderStoreSubList.setCustomaryDues(orderStoreSubList.getGoodsTotal().add(orderStoreSubList.getShipFee()));
        //实付款
        orderStoreSubList.setActualPayment(orderStoreSubList.getGoodsTotal().add(orderStoreSubList.getShipFee()));
        //保存订单信息
        iOrderStoreSubListService.saveOrUpdate(orderStoreSubList);


        //子订单数(拆分供应商后写入)
        orderStoreList.setChildOrder(new BigDecimal(1));
        //设置商品总价
        orderStoreList.setGoodsTotal(totalPrice);
        //运费
        orderStoreList.setShipFee(freight);
        //商品总件数
        orderStoreList.setAllNumberUnits(allNumberUnits);
        //成本价
        costPrice = costPrice.add(freight);
        orderStoreList.setCostPrice(costPrice);

        //使用了礼品卡就不能再使用优惠券  fix by zhangshaolin
        if (StringUtils.isBlank(marketingStoreGiftCardMemberListId)) {
            //通过优惠券判断优惠金额
            //设置优惠金额
            if (marketingDiscountCoupon == null || !marketingDiscountCoupon.getStatus().equals("1")) {
                log.info("优惠券不可用");
            } else {
                String isNomal = marketingDiscountCoupon.getIsNomal();
                //计算优惠券折扣多少钱
                if (StrUtil.equals(isNomal, "2") && StrUtil.equals(storeId, marketingDiscountCoupon.getSysUserId())) {
                    BigDecimal discountLimitAmount = marketingDiscountCoupon.getDiscountLimitAmount();
                    BigDecimal discountUseAmount = marketingDiscountCoupon.getDiscountUseAmount();
                    BigDecimal discountPercent = marketingDiscountCoupon.getDiscountPercent();
                    //可使用折扣余额
                    BigDecimal discountBalance = NumberUtil.sub(discountLimitAmount, discountUseAmount);
                    settleMap.put("marketingTotalPrice", marketingTotalPrice);
                    settleMap.put("noMarketingTotalPrice", noMarketingTotalPrice);
                    settleMap.put("marketingGoodIds", CollUtil.join(marketingGoodIds, ","));
                    settleMap.put("noMarketingGoodIds", CollUtil.join(noMarketingGoodIds, ","));
                    //判断订单金额不在上限金额的范围内
                    if (marketingTotalPrice.compareTo(BigDecimal.ZERO) > 0) {
                        if (discountBalance.compareTo(BigDecimal.ZERO) > 0) {
                            //记录折扣券使用记录
                            MarketingDiscountCouponRecord marketingDiscountCouponRecord = new MarketingDiscountCouponRecord();
                            marketingDiscountCouponRecord.setDelFlag("0");
                            marketingDiscountCouponRecord.setDelReason("");
                            marketingDiscountCouponRecord.setMarketingDiscountCouponId(marketingDiscountCoupon.getId());
                            marketingDiscountCouponRecord.setIsPlatform("0");
                            marketingDiscountCouponRecord.setIsNomad("2");
                            marketingDiscountCouponRecord.setAmount(totalPrice);
                            marketingDiscountCouponRecord.setDiscountGoodAmount(marketingTotalPrice);
                            marketingDiscountCouponRecord.setDiscountLimitAmount(discountLimitAmount);
                            marketingDiscountCouponRecord.setDiscountPercent(discountPercent);
                            marketingDiscountCouponRecord.setMemberListId(memberId);
                            marketingDiscountCouponRecord.setStoreSysUserId(storeId);
                            marketingDiscountCouponRecord.setOrderStoreListId(orderStoreList.getId());
                            marketingDiscountCouponRecord.setUserTime(new Date());

                            // 超过折扣金额，一次性使用，券状态更改为已使用
                            if (marketingTotalPrice.subtract(discountBalance).doubleValue() >= 0) {
                                coupon = NumberUtil.mul(NumberUtil.sub(new BigDecimal("1"), NumberUtil.div(discountPercent.toString(), "10")), discountBalance);
                                //控制优惠金额的优惠幅度
                                if (marketingTotalPrice.subtract(coupon).doubleValue() < 0) {
                                    coupon = marketingTotalPrice;
                                }
//                                if (totalPrice.subtract(coupon).doubleValue()< 0) {
//                                    coupon = totalPrice;
//                                }
                                settleMap.put("coupon", coupon);
                                orderStoreList.setCoupon(coupon);
                                //优惠券id
                                orderStoreList.setMarketingDiscountCouponId(marketingDiscountCoupon.getId());

                                // 更新折扣券已使用额度，状态更新为已使用
                                marketingDiscountCoupon.setDiscountUseAmount(NumberUtil.add(discountUseAmount, discountBalance));
                                marketingDiscountCoupon.setStatus("2");
                                iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon);
                                settleMap.put("discountUseAmount", discountBalance);
                                settleMap.put("discountBalance", BigDecimal.ZERO);
                                // 记录折扣券使用记录明细
                                marketingDiscountCouponRecord.setDiscountUseAmount(discountBalance);
                                marketingDiscountCouponRecord.setCoupon(coupon);
                                marketingDiscountCouponRecord.setDiscountBalance(NumberUtil.sub(discountLimitAmount, marketingDiscountCoupon.getDiscountUseAmount()));
                                marketingDiscountCouponRecord.setDiscountSettleJson(JSON.toJSONString(settleMap));
                                marketingDiscountCouponRecordService.save(marketingDiscountCouponRecord);
                            } else {
                                coupon = NumberUtil.mul(marketingTotalPrice, NumberUtil.sub(new BigDecimal("1"), NumberUtil.div(discountPercent.toString(), "10")));
                                //控制优惠金额的优惠幅度
                                if (marketingTotalPrice.subtract(coupon).doubleValue() < 0) {
                                    coupon = marketingTotalPrice;
                                }
                                settleMap.put("coupon", coupon);
                                orderStoreList.setCoupon(coupon);
                                //优惠券id
                                orderStoreList.setMarketingDiscountCouponId(marketingDiscountCoupon.getId());

                                //更新折扣券已使用额度
                                marketingDiscountCoupon.setDiscountUseAmount(NumberUtil.add(discountUseAmount, marketingTotalPrice));
                                if (NumberUtil.sub(discountBalance, marketingTotalPrice).compareTo(BigDecimal.ZERO) <= 0) {
                                    marketingDiscountCoupon.setStatus("2");
                                }
                                iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon);
                                settleMap.put("discountUseAmount", marketingTotalPrice);
                                settleMap.put("discountBalance", NumberUtil.sub(discountBalance, marketingTotalPrice));
                                // 记录折扣券使用记录明细
                                marketingDiscountCouponRecord.setDiscountUseAmount(marketingTotalPrice);
                                marketingDiscountCouponRecord.setCoupon(coupon);
                                marketingDiscountCouponRecord.setDiscountBalance(NumberUtil.sub(discountLimitAmount, marketingDiscountCoupon.getDiscountUseAmount()));
                                marketingDiscountCouponRecord.setDiscountSettleJson(JSON.toJSONString(settleMap));
                                marketingDiscountCouponRecordService.save(marketingDiscountCouponRecord);
                            }
                        }
                    }
                    orderStoreList.setDiscountSettleJson(JSON.toJSONString(settleMap));
                }
                //判断优惠券类型
                if (StrUtil.equals(isNomal, "0") || StrUtil.equals(isNomal, "1")) {
                    //满减券,判断消费满多少钱优惠多少钱
                    BigDecimal completely = marketingDiscountCoupon.getCompletely();
                    if (marketingTotalPrice.subtract(completely).doubleValue() >= 0) {
                        orderStoreList.setCoupon(marketingDiscountCoupon.getPrice());
                    }
                    //标识优惠券已使用
                    marketingDiscountCoupon.setStatus("2");
                    marketingDiscountCoupon.setUserTime(DateUtil.date());
                    //优惠券id
                    orderStoreList.setMarketingDiscountCouponId(marketingDiscountCoupon.getId());
                    iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon);
                }
            }
        } else {
            orderStoreList.setCoupon(new BigDecimal(0));
        }

        //控制优惠金额的优惠幅度
        if (totalPrice.subtract(orderStoreList.getCoupon()).doubleValue() < 0) {
            orderStoreList.setCoupon(totalPrice);
        }

        // 更新订单商品表扣除优惠券后的实付金额 @zhangshaolin
        if (CollUtil.isNotEmpty(marketingOrderStoreGoodRecords)) {
            //订单优惠后实付款，不含运费
            BigDecimal actualPayment = marketingTotalPrice.subtract(ObjectUtil.defaultIfNull(orderStoreList.getCoupon(), new BigDecimal("0")));
            BigDecimal tempSum = new BigDecimal(0);
            for (int i = 0; i < marketingOrderStoreGoodRecords.size(); i++) {
                OrderStoreGoodRecord orderStoreGoodRecord = marketingOrderStoreGoodRecords.get(i);
                BigDecimal total = orderStoreGoodRecord.getTotal();
                if (i == marketingOrderStoreGoodRecords.size() - 1) {
                    BigDecimal orderGoodActualPayment = NumberUtil.sub(actualPayment, tempSum);
                    orderStoreGoodRecord.setCustomaryDues(orderGoodActualPayment);
                    orderStoreGoodRecord.setActualPayment(orderGoodActualPayment);
                    orderStoreGoodRecord.setCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderStoreGoodRecord.getActualPayment()));
                    orderStoreGoodRecord.setTotalCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderStoreGoodRecord.getActualPayment()));
                } else {
                    BigDecimal orderGoodActualPayment = NumberUtil.mul(NumberUtil.div(total, marketingTotalPrice, 2), actualPayment);
                    orderStoreGoodRecord.setCustomaryDues(orderGoodActualPayment);
                    orderStoreGoodRecord.setActualPayment(orderGoodActualPayment);
                    orderStoreGoodRecord.setCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderStoreGoodRecord.getActualPayment()));
                    orderStoreGoodRecord.setTotalCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderStoreGoodRecord.getActualPayment()));
                    tempSum = NumberUtil.add(tempSum, orderGoodActualPayment);
                }
            }
            orderStoreGoodRecordService.updateBatchById(marketingOrderStoreGoodRecords);
        }

        //无优惠的总价
//        totalPrice=totalPrice.add(freight);

        //礼品卡实际抵扣金额
        BigDecimal actuaGoodGiftCardtotal = new BigDecimal("0");
        //判断礼品卡的优惠
        if (StringUtils.isNotBlank(marketingStoreGiftCardMemberListId)) {
            MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList = iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
            if (marketingStoreGiftCardMemberList.getDenomination().subtract(goodGiftCardtotal).doubleValue() < 0) {
                actuaGoodGiftCardtotal = marketingStoreGiftCardMemberList.getDenomination();
            }
            orderStoreList.setGiftCardTotal(actuaGoodGiftCardtotal);
            orderStoreList.setActiveId(marketingStoreGiftCardMemberListId);
            orderStoreList.setOrderType("7");
            //        更新订单商品表扣除礼品卡后的实付金额
            if (actuaGoodGiftCardtotal.compareTo(BigDecimal.ZERO) != 0 && CollUtil.isNotEmpty(goodGiftCardGoodRecords)) {
                BigDecimal tempSum = new BigDecimal(0);
                for (int i = 0; i < goodGiftCardGoodRecords.size(); i++) {
                    OrderStoreGoodRecord orderStoreGoodRecord = goodGiftCardGoodRecords.get(i);
                    if (i == goodGiftCardGoodRecords.size() - 1) {
                        BigDecimal orderGoodActualGiftCardPayment = NumberUtil.sub(actuaGoodGiftCardtotal, tempSum);
                        orderStoreGoodRecord.setCustomaryDues(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderGoodActualGiftCardPayment));
                        orderStoreGoodRecord.setActualPayment(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderGoodActualGiftCardPayment));
                        orderStoreGoodRecord.setGiftCardCoupon(orderGoodActualGiftCardPayment);
                        orderStoreGoodRecord.setTotalCoupon(orderGoodActualGiftCardPayment);
                    } else {
                        //商品实际礼品卡抵扣金额
                        BigDecimal orderGoodActualGiftCardPayment = NumberUtil.mul(NumberUtil.div(orderStoreGoodRecord.getTotal(), goodGiftCardtotal, 2), actuaGoodGiftCardtotal);
                        orderStoreGoodRecord.setCustomaryDues(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderGoodActualGiftCardPayment));
                        orderStoreGoodRecord.setActualPayment(NumberUtil.sub(orderStoreGoodRecord.getTotal(), orderGoodActualGiftCardPayment));
                        orderStoreGoodRecord.setGiftCardCoupon(orderGoodActualGiftCardPayment);
                        orderStoreGoodRecord.setTotalCoupon(orderGoodActualGiftCardPayment);
                        tempSum = NumberUtil.add(tempSum, orderGoodActualGiftCardPayment);
                    }
                }
                orderStoreGoodRecordService.updateBatchById(goodGiftCardGoodRecords);
            }
        }
        /*店铺专区*/
        if (StringUtils.isNotBlank(marketingStorePrefectureGoodId)) {
            log.info("店铺专区购买：" + marketingStorePrefectureGoodId);
            orderStoreList.setActiveId(marketingStorePrefectureGoodId);
            orderStoreList.setOrderType("8");
        }
        //设置礼品卡优惠金额
        orderStoreList.setGiftCardTotal(actuaGoodGiftCardtotal);

        //应付款
        orderStoreList.setCustomaryDues(totalPrice.subtract(ObjectUtil.defaultIfNull(orderStoreList.getCoupon(), new BigDecimal("0"))).subtract(actuaGoodGiftCardtotal));
        //实付款
        orderStoreList.setActualPayment(totalPrice.subtract(ObjectUtil.defaultIfNull(orderStoreList.getCoupon(), new BigDecimal("0"))).subtract(actuaGoodGiftCardtotal));

        //实付款小于等于，就设置为0
        if (orderStoreList.getActualPayment().doubleValue() <= 0) {
            orderStoreList.setActualPayment(new BigDecimal(0));
        }

        //给应付款和支付款加上运费
        orderStoreList.setCustomaryDues(orderStoreList.getCustomaryDues().add(freight));
        //实付款
        orderStoreList.setActualPayment(orderStoreList.getActualPayment().add(freight));

        //实际分销佣金
        BigDecimal actualBrokerage = new BigDecimal(0);

        //商品利润
        orderStoreList.setProfit(orderStoreList.getActualPayment().subtract(costPrice));
        //分销佣金
        orderStoreList.setDistributionCommission(new BigDecimal(0));
        if (orderStoreList.getProfit().doubleValue() > 0) {
            LambdaQueryWrapper<MarketingStoreDistributionSetting> marketingStoreDistributionSettingLambdaQueryWrapper = new LambdaQueryWrapper<MarketingStoreDistributionSetting>()
                    .eq(MarketingStoreDistributionSetting::getDelFlag, "0")
                    .eq(MarketingStoreDistributionSetting::getStatus, "1")
                    .eq(MarketingStoreDistributionSetting::getSysUserId, orderStoreList.getSysUserId());
            if (iMarketingStoreDistributionSettingService.count(marketingStoreDistributionSettingLambdaQueryWrapper) > 0) {
                //获取分销比例
                MarketingStoreDistributionSetting marketingStoreDistributionSetting = iMarketingStoreDistributionSettingService.list(marketingStoreDistributionSettingLambdaQueryWrapper).get(0);

                orderStoreList.setDistributionCommission(orderStoreList.getActualPayment().subtract(costPrice));

                if (oConvertUtils.isNotEmpty(marketingStoreDistributionSetting) && StringUtils.isNotBlank(memberList.getPromoterType()) && StringUtils.isNotBlank(memberList.getPromoter())) {
                    //判断推广人是否为用户
                    if (memberList.getPromoterType().equals("1")) {
                        MemberList firstMemberList = iMemberListService.getById(memberList.getPromoter());
                        //判断用户是否为vip
                        if (firstMemberList.getMemberType().equals("0")) {
                            BigDecimal commonFirst = marketingStoreDistributionSetting.getCommonFirst();
                            //普通用户一级分销
                            if (commonFirst.doubleValue() > 0) {
                                MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord()
                                        .setDelFlag("0")
                                        .setMemberListId(firstMemberList.getId())
                                        .setPayType("3")
                                        .setGoAndCome("0")
                                        .setAmount(orderStoreList.getDistributionCommission().multiply(commonFirst.divide(new BigDecimal(100))))
                                        .setTradeStatus("0")
                                        .setOrderNo(OrderNoUtils.getOrderNo())
                                        .setOperator("系统")
                                        .setRemark("订单分销奖励 (一) [" + orderStoreList.getOrderNo() + "]")
                                        .setPayment("0")
                                        .setTradeNo(orderStoreList.getOrderNo())
                                        .setTradeType("0")
                                        .setMemberLevel("1")
                                        .setTMemberListId(orderStoreList.getMemberListId());
                                iMemberRechargeRecordService.save(memberRechargeRecord);
                                actualBrokerage = actualBrokerage.add(memberRechargeRecord.getAmount());

                                myStoreGoods.forEach(sg -> {

                                    GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(sg.get("goodSpecificationId").toString());

                                    GoodStoreList goodStoreList = iGoodStoreListService.getById(sg.get("goodId").toString());

                                    iMemberDistributionRecordService.save(new MemberDistributionRecord()
                                            .setDelFlag("0")
                                            .setMemberRechargeRecordId(memberRechargeRecord.getId())
                                            .setGoodPicture(goodStoreList.getMainPicture())
                                            .setGoodName(goodStoreList.getGoodName())
                                            .setGoodSpecification(goodStoreSpecification.getSpecification())
                                            .setCommission(((BigDecimal) sg.get("price"))
                                                    .multiply(((BigDecimal) sg.get("quantity")))
                                                    .divide(orderStoreList.getGoodsTotal(), 2, BigDecimal.ROUND_UP)
                                                    .multiply(memberRechargeRecord.getAmount())));
                                });
                                log.info("形成待支付订单一级分销：" + memberRechargeRecord.getAmount() + "--普通会员冻结金额：" + firstMemberList.getAccountFrozen() + "--会员：" + firstMemberList.getNickName());

                            }
                        }
                        //判断用户是VIP
                        if (firstMemberList.getMemberType().equals("1")) {

                            BigDecimal vipFirst = marketingStoreDistributionSetting.getVipFirst();

                            if (vipFirst.doubleValue() > 0) {
                                MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord()
                                        .setDelFlag("0")
                                        .setMemberListId(firstMemberList.getId())
                                        .setPayType("3")
                                        .setGoAndCome("0")
                                        .setAmount(orderStoreList.getDistributionCommission().multiply(vipFirst.divide(new BigDecimal(100))))
                                        .setTradeStatus("0")
                                        .setOrderNo(OrderNoUtils.getOrderNo())
                                        .setOperator("系统")
                                        .setRemark("订单分销奖励 (一) [" + orderStoreList.getOrderNo() + "]")
                                        .setPayment("0")
                                        .setTradeNo(orderStoreList.getOrderNo())
                                        .setTradeType("0")
                                        .setMemberLevel("1")
                                        .setTMemberListId(orderStoreList.getMemberListId());
                                iMemberRechargeRecordService.save(memberRechargeRecord);
                                actualBrokerage = actualBrokerage.add(memberRechargeRecord.getAmount());

                                myStoreGoods.forEach(sg -> {

                                    GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(sg.get("goodSpecificationId").toString());

                                    GoodStoreList goodStoreList = iGoodStoreListService.getById(sg.get("goodId").toString());

                                    iMemberDistributionRecordService.save(new MemberDistributionRecord()
                                            .setDelFlag("0")
                                            .setMemberRechargeRecordId(memberRechargeRecord.getId())
                                            .setGoodPicture(goodStoreList.getMainPicture())
                                            .setGoodName(goodStoreList.getGoodName())
                                            .setGoodSpecification(goodStoreSpecification.getSpecification())
                                            .setCommission(((BigDecimal) sg.get("price"))
                                                    .multiply(((BigDecimal) sg.get("quantity")))
                                                    .divide(orderStoreList.getGoodsTotal(), 2, BigDecimal.ROUND_UP)
                                                    .multiply(memberRechargeRecord.getAmount())));
                                });
                                log.info("形成待支付订单一级分销：" + memberRechargeRecord.getAmount() + "--vip会员冻结金额：" + firstMemberList.getAccountFrozen() + "--vip会员：" + firstMemberList.getNickName());

                            }
                        }
                        //判断二级分销有没有上级
                        if (StringUtils.isNotBlank(firstMemberList.getPromoterType()) && StringUtils.isNotBlank(firstMemberList.getPromoterType())) {
                            if (firstMemberList.getPromoterType().equals("1")) {

                                MemberList secondMemberList = iMemberListService.getById(firstMemberList.getPromoter());
                                //推广人为普通会员
                                if (secondMemberList.getMemberType().equals("0")) {
                                    BigDecimal commonSecond = marketingStoreDistributionSetting.getCommonSecond();
                                    if (commonSecond.doubleValue() > 0) {
                                        MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord()
                                                .setDelFlag("0")
                                                .setMemberListId(secondMemberList.getId())
                                                .setPayType("3")
                                                .setGoAndCome("0")
                                                .setAmount(orderStoreList.getDistributionCommission().multiply(commonSecond.divide(new BigDecimal(100))))
                                                .setTradeStatus("0")
                                                .setOrderNo(OrderNoUtils.getOrderNo())
                                                .setOperator("系统")
                                                .setRemark("订单分销奖励 (二) [" + orderStoreList.getOrderNo() + "]")
                                                .setPayment("0")
                                                .setTradeNo(orderStoreList.getOrderNo())
                                                .setTradeType("0")
                                                .setMemberLevel("2")
                                                .setTMemberListId(orderStoreList.getMemberListId());
                                        iMemberRechargeRecordService.save(memberRechargeRecord);
                                        actualBrokerage = actualBrokerage.add(memberRechargeRecord.getAmount());

                                        myStoreGoods.forEach(sg -> {

                                            GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(sg.get("goodSpecificationId").toString());

                                            GoodStoreList goodStoreList = iGoodStoreListService.getById(sg.get("goodId").toString());

                                            iMemberDistributionRecordService.save(new MemberDistributionRecord()
                                                    .setDelFlag("0")
                                                    .setMemberRechargeRecordId(memberRechargeRecord.getId())
                                                    .setGoodPicture(goodStoreList.getMainPicture())
                                                    .setGoodName(goodStoreList.getGoodName())
                                                    .setGoodSpecification(goodStoreSpecification.getSpecification())
                                                    .setCommission(((BigDecimal) sg.get("price"))
                                                            .multiply(((BigDecimal) sg.get("quantity")))
                                                            .divide(orderStoreList.getGoodsTotal(), 2, BigDecimal.ROUND_UP)
                                                            .multiply(memberRechargeRecord.getAmount())));
                                        });
                                        log.info("形成待支付订单二级分销：" + memberRechargeRecord.getAmount() + "--二级分销普通会员冻结金额：" + secondMemberList.getAccountFrozen() + "--二级分销会员：" + secondMemberList.getNickName());

                                    }
                                }
                                if (secondMemberList.getMemberType().equals("1")) {
                                    BigDecimal vipSecond = marketingStoreDistributionSetting.getVipSecond();
                                    if (vipSecond.doubleValue() > 0) {
                                        MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord()
                                                .setDelFlag("0")
                                                .setMemberListId(secondMemberList.getId())
                                                .setPayType("3")
                                                .setGoAndCome("0")
                                                .setAmount(orderStoreList.getDistributionCommission().multiply(vipSecond.divide(new BigDecimal(100))))
                                                .setTradeStatus("0")
                                                .setOrderNo(OrderNoUtils.getOrderNo())
                                                .setOperator("系统")
                                                .setRemark("订单分销奖励 (二) [" + orderStoreList.getOrderNo() + "]")
                                                .setPayment("0")
                                                .setTradeNo(orderStoreList.getOrderNo())
                                                .setTradeType("0")
                                                .setMemberLevel("2")
                                                .setTMemberListId(orderStoreList.getMemberListId());
                                        iMemberRechargeRecordService.save(memberRechargeRecord);
                                        actualBrokerage = actualBrokerage.add(memberRechargeRecord.getAmount());

                                        myStoreGoods.forEach(sg -> {

                                            GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(sg.get("goodSpecificationId").toString());

                                            GoodStoreList goodStoreList = iGoodStoreListService.getById(sg.get("goodId").toString());

                                            iMemberDistributionRecordService.save(new MemberDistributionRecord()
                                                    .setDelFlag("0")
                                                    .setMemberRechargeRecordId(memberRechargeRecord.getId())
                                                    .setGoodPicture(goodStoreList.getMainPicture())
                                                    .setGoodName(goodStoreList.getGoodName())
                                                    .setGoodSpecification(goodStoreSpecification.getSpecification())
                                                    .setCommission(((BigDecimal) sg.get("price"))
                                                            .multiply(((BigDecimal) sg.get("quantity")))
                                                            .divide(orderStoreList.getGoodsTotal(), 2, BigDecimal.ROUND_UP)
                                                            .multiply(memberRechargeRecord.getAmount())));
                                        });
                                        log.info("形成待支付订单二级分销：" + memberRechargeRecord.getAmount() + "--二级分销vip会员冻结金额：" + secondMemberList.getAccountFrozen() + "--二级分销会员：" + secondMemberList.getNickName());

                                    }
                                }

                            }
                        }
                    }
                }
            }

        }
        log.info("实际分销金额：" + actualBrokerage);
        //净利润
        orderStoreList.setRetainedProfits(orderStoreList.getProfit().subtract(actualBrokerage));
        //保存订单信息
        this.saveOrUpdate(orderStoreList);

        //判断礼品卡的优惠
        if (StringUtils.isNotBlank(marketingStoreGiftCardMemberListId)) {
            iMarketingStoreGiftCardMemberListService.subtractBlance(marketingStoreGiftCardMemberListId, goodGiftCardtotal, orderStoreList.getOrderNo(), "1");
        }
        return orderStoreList;
    }

    @Override
    @Transactional
    public Boolean paySuccessOrder(String id, PayOrderCarLog payOrderCarLog) {
        //修改订单成功状态信息
        OrderStoreList orderStoreList = this.getById(id);
        if (orderStoreList.getStatus().equals("1")) {
            return true;
        }
        orderStoreList.setStatus("1");

        orderStoreList.setModePayment(payOrderCarLog.getPayModel());//支付方式的设定
        log.info("支付成功后日志内容：" + JSON.toJSONString(payOrderCarLog));
        BigDecimal integralValue = iMarketingWelfarePaymentsSettingService.getIntegralValue();
        if (payOrderCarLog.getPayPrice().doubleValue() != 0) {
            orderStoreList.setPayPrice(orderStoreList.getActualPayment().divide(payOrderCarLog.getAllTotalPrice(), 2, RoundingMode.HALF_UP).multiply(payOrderCarLog.getPayPrice()));
        }
        if (payOrderCarLog.getBalance().doubleValue() != 0) {
            orderStoreList.setBalance(orderStoreList.getActualPayment().divide(payOrderCarLog.getAllTotalPrice(), 2, RoundingMode.HALF_UP).multiply(payOrderCarLog.getBalance()));
        }
        if (payOrderCarLog.getWelfarePayments().doubleValue() != 0) {
            orderStoreList.setPayWelfarePayments(orderStoreList.getActualPayment().divide(payOrderCarLog.getAllTotalPrice(), 2, RoundingMode.HALF_UP).multiply(payOrderCarLog.getWelfarePayments()));
            orderStoreList.setPayWelfarePaymentsPrice(orderStoreList.getPayWelfarePayments().multiply(integralValue));
        }
        // TODO: 2023/4/20 计算商品实际支付金额占比 @zhangshaolin
        List<Map<String, Object>> storeGoodRecordList = iOrderStoreGoodRecordService.getOrderStoreGoodRecordByOrderId(orderStoreList.getId());
        if (CollUtil.isNotEmpty(storeGoodRecordList)) {
            for (int i = 0; i < storeGoodRecordList.size(); i++) {
                Map<String, Object> storeOrderGoodRecordMap = storeGoodRecordList.get(i);

            }
        }


        orderStoreList.setPayTime(new Date());
        //设置交易流水号
        orderStoreList.setSerialNumber(payOrderCarLog.getId());
        orderStoreList.setHftxSerialNumber(payOrderCarLog.getSerialNumber());

        //赠送积分

        iStoreOrderSettingService.success(orderStoreList);

        this.saveOrUpdate(orderStoreList);

        //余额分账
        iStoreCashierRoutingService.independentAccountOrderBalance(orderStoreList);

        /*店铺专区成功*/
        if (orderStoreList.getOrderType().equals("8")) {
            iMarketingStorePrefectureGoodService.success(orderStoreList, payOrderCarLog.getId());
        }


        //修改子订单状态
        OrderStoreSubList orderStoreSubList = new OrderStoreSubList();
        orderStoreSubList.setStatus("1");
        UpdateWrapper<OrderStoreSubList> orderProviderListUpdateWrapper = new UpdateWrapper<>();
        orderProviderListUpdateWrapper.eq("order_store_list_id", orderStoreList.getId());
        iOrderStoreSubListService.update(orderStoreSubList, orderProviderListUpdateWrapper);
        //分销记录条件构造器
        LambdaQueryWrapper<MemberRechargeRecord> memberRechargeRecordLambdaQueryWrapper = new LambdaQueryWrapper<MemberRechargeRecord>()
                .eq(MemberRechargeRecord::getTradeNo, orderStoreList.getOrderNo())
                .eq(MemberRechargeRecord::getTradeType, "0")
                .eq(MemberRechargeRecord::getTradeStatus, "0")
                .eq(MemberRechargeRecord::getPayType, "3");
        //查出分销记录
        if (iMemberRechargeRecordService.count(memberRechargeRecordLambdaQueryWrapper) > 0) {
            List<MemberRechargeRecord> memberRechargeRecords = iMemberRechargeRecordService.list(memberRechargeRecordLambdaQueryWrapper);
            memberRechargeRecords.forEach(mrrs -> {
                //修改分销记录状态
                iMemberRechargeRecordService.saveOrUpdate(mrrs.setTradeStatus("2"));
                MemberList memberList = iMemberListService.getById(mrrs.getMemberListId());
                //分配分销佣金
                iMemberListService.saveOrUpdate(memberList
                        .setAccountFrozen(memberList.getAccountFrozen().add(mrrs.getAmount()))
                        .setTotalCommission(memberList.getTotalCommission().add(mrrs.getAmount())));
            });
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void abrogateOrder(String id, String CloseExplain, String closeType) {
        OrderStoreList orderStoreList = this.getById(id);

        // fix 在后台操作，取消并退款，福利金没有回收。by zhangshaolin
        if (CompareUtil.compare(orderStoreList.getGiveWelfarePayments(), BigDecimal.ZERO) > 0) {
            MemberList memberList = iMemberListService.getById(orderStoreList.getMemberListId());
            if (CompareUtil.compare(memberList.getWelfarePayments(), orderStoreList.getGiveWelfarePayments()) < 0) {
                throw new JeecgBootException("会员福利金小于订单赠送福利金，无法取消订单");
            }
            //扣除店铺订单赠送的积分 by zhangshaolin
            memberWelfarePaymentsService.subtractWelfarePayments(memberList.getId(), orderStoreList.getGiveWelfarePayments(), "17", orderStoreList.getId(), "订单取消");
        }

        //买家关闭
        orderStoreList.setCloseType(closeType);
        orderStoreList.setCloseExplain(CloseExplain);
        orderStoreList.setCloseTime(new Date());
        //交易关闭
        orderStoreList.setStatus("4");
        this.saveOrUpdate(orderStoreList);
        //礼品卡金额退回
        if (orderStoreList.getOrderType().equals("7") && StringUtils.isNotBlank(orderStoreList.getActiveId()) && orderStoreList.getGiftCardTotal().doubleValue() > 0) {
            iMarketingStoreGiftCardMemberListService.addBlance(orderStoreList.getActiveId(), orderStoreList.getGiftCardTotal(), orderStoreList.getOrderNo(), "2");
        }

        /*退回优惠券*/
        iMarketingDiscountCouponService.sendBackOrderStoreMarketingDiscountCoupon(this.getById(id));

        QueryWrapper<OrderStoreSubList> orderStoreSubListQueryWrapper = new QueryWrapper<>();
        orderStoreSubListQueryWrapper.eq("order_store_list_id", id);
        List<OrderStoreSubList> orderStoreSubLists = iOrderStoreSubListService.list(orderStoreSubListQueryWrapper);
        orderStoreSubLists.forEach(oss -> {
            oss.setStatus("4");
            iOrderStoreSubListService.saveOrUpdate(oss);
            //退回库存
            QueryWrapper<OrderStoreGoodRecord> orderStoreGoodRecordQueryWrapper = new QueryWrapper<>();
            orderStoreGoodRecordQueryWrapper.eq("order_store_sub_list_id", oss.getId());
            List<OrderStoreGoodRecord> orderStoreGoodRecords = iOrderStoreGoodRecordService.list(orderStoreGoodRecordQueryWrapper);
            orderStoreGoodRecords.forEach(osgr -> {
                String goodStoreSpecificationId = osgr.getGoodStoreSpecificationId();
                GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(goodStoreSpecificationId);
                if (goodStoreSpecification != null) {
                    goodStoreSpecification.setRepertory(goodStoreSpecification.getRepertory().add(osgr.getAmount()));
                    iGoodStoreSpecificationService.saveOrUpdate(goodStoreSpecification);
                }
            });
        });

        //查询出分销用户
        List<MemberRechargeRecord> memberRechargeRecords = iMemberRechargeRecordService.list(new LambdaQueryWrapper<MemberRechargeRecord>()
                .eq(MemberRechargeRecord::getTradeNo, orderStoreList.getOrderNo())
                .eq(MemberRechargeRecord::getTradeType, "0")
                .eq(MemberRechargeRecord::getTradeStatus, "0")
                .eq(MemberRechargeRecord::getPayType, "3"));
        if (oConvertUtils.isNotEmpty(memberRechargeRecords)) {
            //遍历出分销用户
            memberRechargeRecords.forEach(mrrs -> {
                //交易关闭
                iMemberRechargeRecordService.saveOrUpdate(mrrs
                        .setTradeStatus("7"));
            });
        }


        //店铺查询条件
        LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId, orderStoreList.getSysUserId());

        long count = iStoreManageService.count(storeManageLambdaQueryWrapper);
    }


    @Override
    @Transactional
    public Result<?> refundAndAbrogateOrder(String id, String closeExplain, String closeType) {
        OrderStoreList orderStoreList = this.getById(id);
        if (orderStoreList.getStatus().equals("0") || orderStoreList.getStatus().equals("4")) {
            return Result.error("订单状态不正确");
        }

        // fix 在后台操作，取消并退款，福利金没有回收。by zhangshaolin
        if (CompareUtil.compare(orderStoreList.getGiveWelfarePayments(), BigDecimal.ZERO) > 0) {
            MemberList memberList = iMemberListService.getById(orderStoreList.getMemberListId());
            if (CompareUtil.compare(memberList.getWelfarePayments(), orderStoreList.getGiveWelfarePayments()) < 0) {
                return Result.error("会员福利金小于订单赠送福利金，无法退款");
            }
        }

        if (orderStoreList.getPayPrice().doubleValue() > 0.0D) {
            Map balanceMap;
            try {
                balanceMap = this.hftxPayUtils.getSettleAccountBalance();
                log.info(JSON.toJSONString("账户余额信息：" + balanceMap));
                if (!balanceMap.get("status").equals("succeeded")) {
                    return Result.error("汇付账户的余额查询出错");
                }

                if (Double.parseDouble(balanceMap.get("avl_balance").toString()) < orderStoreList.getPayPrice().doubleValue()) {
                    Object var10000 = balanceMap.get("avl_balance");
                    return Result.error("汇付账户的余额：" + var10000 + "；需退金额：" + orderStoreList.getPayPrice());
                }
            } catch (BaseAdaPayException var6) {
                var6.printStackTrace();
            }

            balanceMap = this.payUtils.refund(orderStoreList.getPayPrice(), orderStoreList.getSerialNumber(), orderStoreList.getHftxSerialNumber());
            if (balanceMap.get("status").equals("failed")) {
                return Result.error("现金退款失败");
            }

            orderStoreList.setRefundJson(JSON.toJSONString(balanceMap));
        }
        this.saveOrUpdate(orderStoreList);
        //退回余额
        iMemberListService.addBlance(orderStoreList.getMemberListId(), orderStoreList.getBalance(), orderStoreList.getOrderNo(), "2");
        //退回交易积分
        iMemberWelfarePaymentsService.addWelfarePayments(orderStoreList.getMemberListId(), orderStoreList.getPayWelfarePayments(), "20", orderStoreList.getOrderNo(), "");
        //取消订单
        this.abrogateOrder(id, closeExplain, closeType);

        return Result.ok("退款成功");
    }


    @Override
    public void affirmOrder(String id) {

        this.updateById(new OrderStoreList().setId(id).setStatus("3").setDeliveryTime(new Date()));
        iOrderStoreSubListService.update(new OrderStoreSubList().setStatus("3"), new LambdaQueryWrapper<OrderStoreSubList>().eq(OrderStoreSubList::getOrderStoreListId, id));
    }

    /**
     * 待支付订单超时数据
     *
     * @param hour
     * @return
     */
    @Override
    public List<OrderStoreList> getCancelOrderStoreList(String hour) {
        return baseMapper.getCancelOrderStoreList(hour);
    }

    /**
     * 定时器取消订单
     */
    @Override
    public void cancelOrderStoreListJob() {
        //过期时间(小时)
        String hour = iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "common_order_payment_timeout");
        if (StringUtils.isNotBlank(hour)) {
            List<OrderStoreList> orderStoreListList = baseMapper.getCancelOrderStoreList(hour);
            orderStoreListList.forEach(ol -> {
                log.info("定时器取消 店铺 待支付订单超时未支付:订单编号:" + ol.getOrderNo());
                abrogateOrder(ol.getId(), "0", "0");
            });

        }

    }

    /**
     * 返回待支付订单计时器(秒)
     *
     * @param id
     * @param hour
     * @return
     */
    @Override
    public String getOrderStoreListTimer(String id, String hour) {
        return baseMapper.getOrderStoreListTimer(id, hour);
    }

    /**
     * 确认订单超时数据
     *
     * @param hour
     * @return
     */
    @Override
    public List<OrderStoreList> getStoreConfirmReceiptOrderList(String hour) {
        return baseMapper.getStoreConfirmReceiptOrderList(hour);
    }

    /**
     * 定时器取消订单
     */
    @Override
    public void storeConfirmReceiptOrderList() {
        //过期时间(小时)
        String hour = iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "order_confirmation_receiving_timeout");
        if (StringUtils.isNotBlank(hour)) {
            List<OrderStoreList> orderStoreListList = baseMapper.getStoreConfirmReceiptOrderList(hour);
            orderStoreListList.forEach(ol -> {
                log.info("定时器取消 店铺 确认收货超时:订单编号:" + ol.getOrderNo());
                affirmOrder(ol.getId());
            });

        }

    }

    @Override
    public void confirmReceiptOrderJob() {
        //确认收货时间超过7天后完成订单
        //获取售后时间(单位:小时)
        String hour = iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "order_after_service_timeout");
        if (StringUtils.isNotBlank(hour)) {
            //查询出超过售后时间的订单(50条上限) 取店铺订单
            List<OrderStoreList> orderCompletion = baseMapper.getOrderCompletion(hour);
            //遍历店铺订单
            orderCompletion.forEach(oc -> {
                log.info(" 店铺 确认收货后7天完成订单:订单编号:" + oc.getOrderNo());
                this.accomplishStoreOrder(oc.getId());
            });
        }
    }


    /**
     * 返回确认收货订单计时器(秒)
     *
     * @param id
     * @param hour
     * @return
     */
    @Override
    public String getStoreConfirmReceiptTimer(String id, String hour) {
        return baseMapper.getStoreConfirmReceiptTimer(id, hour);
    }

    ;


    @Override
    @Transactional
    public void accomplishStoreOrder(String id) {
        OrderStoreList orderStoreList = this.getById(id);
        //写入订单完成时间,状态为交易完成
        this.saveOrUpdate(orderStoreList
                .setCompletionTime(new Date())
                .setStatus("5"));
        MemberList member = iMemberListService.getById(orderStoreList.getMemberListId());
        LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                .eq(MemberGrade::getDelFlag, "0")
                .eq(MemberGrade::getStatus, "1");
        String storeOrderTransaction = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_order_transaction");
        //订单实付款大于0赠送成长值
        if (orderStoreList.getActualPayment().doubleValue() > 0 && iMemberGradeService.count(memberGradeLambdaQueryWrapper) > 0 && storeOrderTransaction.equals("1")) {
            if (StringUtils.isNotBlank(member.getMemberGradeId())) {
                member.setGrowthValue(member.getGrowthValue().add(orderStoreList.getActualPayment()));
                MemberGrade memberGrade = iMemberGradeService.getById(member.getMemberGradeId());

                if (member.getGrowthValue().doubleValue() > memberGrade.getGrowthValueBig().doubleValue()) {

                    memberGradeLambdaQueryWrapper.le(MemberGrade::getGrowthValueSmall, member.getGrowthValue())
                            .ge(MemberGrade::getGrowthValueBig, member.getGrowthValue())
                            .orderByAsc(MemberGrade::getSort);

                    if (iMemberGradeService.count(memberGradeLambdaQueryWrapper) > 0) {
                        MemberGrade grade = iMemberGradeService.list(memberGradeLambdaQueryWrapper).get(0);
                        if (member.getGrowthValue().doubleValue() > grade.getGrowthValueSmall().doubleValue()) {
                            member.setMemberGradeId(grade.getId());
                        }

                    }
                }

            } else {
                member.setGrowthValue(member.getGrowthValue().add(orderStoreList.getActualPayment()));
                memberGradeLambdaQueryWrapper
                        .le(MemberGrade::getGrowthValueSmall, member.getGrowthValue())
                        .ge(MemberGrade::getGrowthValueBig, member.getGrowthValue())
                        .orderByAsc(MemberGrade::getSort)
                ;

                if (iMemberGradeService.count(memberGradeLambdaQueryWrapper) > 0) {
                    if (member.getMemberType().equals("0")) {
                        member.setMemberType("1");
                        member.setVipTime(new Date());
                    }
                    member.setMemberGradeId(iMemberGradeService.list(memberGradeLambdaQueryWrapper).get(0).getId());
                } else {
                    MemberGrade grade = iMemberGradeService.list(new LambdaQueryWrapper<MemberGrade>()
                            .eq(MemberGrade::getDelFlag, "0")
                            .eq(MemberGrade::getStatus, "1")
                            .orderByDesc(MemberGrade::getSort)).get(0);
                    if (grade.getGrowthValueBig().doubleValue() <= member.getGrowthValue().doubleValue()) {
                        if (member.getMemberType().equals("0")) {
                            member.setMemberType("1");
                            member.setVipTime(new Date());
                        }
                        member.setMemberGradeId(grade.getId());
                    }
                }
            }
            iMemberListService.saveOrUpdate(member);
            iMemberGrowthRecordService.save(new MemberGrowthRecord()
                    .setMemberListId(member.getId())
                    .setTradeNo(orderStoreList.getOrderNo())
                    .setTradeType("1")
                    .setRemark("订单交易[" + orderStoreList.getOrderNo() + "]")
                    .setGrowthValue(orderStoreList.getActualPayment())
                    .setOrderNo(OrderNoUtils.getOrderNo())
            );
        }
        //判断分销
        if (orderStoreList.getDistributionCommission().doubleValue() > 0) {
            //获取分销集合
            List<MemberRechargeRecord> memberRechargeRecords = iMemberRechargeRecordService.list(new LambdaQueryWrapper<MemberRechargeRecord>()
                    .eq(MemberRechargeRecord::getTradeNo, orderStoreList.getOrderNo())
                    .eq(MemberRechargeRecord::getPayType, "3")
                    .eq(MemberRechargeRecord::getGoAndCome, "0")
                    .eq(MemberRechargeRecord::getTradeStatus, "2")
                    .eq(MemberRechargeRecord::getTradeType, "0"));
            //不为空时遍历
            if (oConvertUtils.isNotEmpty(memberRechargeRecords)) {
                memberRechargeRecords.forEach(mrrs -> {
                    //获取分销会员
                    MemberList memberList = iMemberListService.getById(mrrs.getMemberListId());
                    //将分销冻结金额转为金额
                    iMemberListService.saveOrUpdate(memberList
                            .setAccountFrozen(memberList.getAccountFrozen().subtract(mrrs.getAmount()))
                            .setBalance(memberList.getBalance().add(mrrs.getAmount())));
                    //交易完成
                    iMemberRechargeRecordService.saveOrUpdate(mrrs.setTradeStatus("5"));
                    //生成资金流水记录
                    iMemberAccountCapitalService.save(new MemberAccountCapital()
                            .setDelFlag("0")
                            .setMemberListId(mrrs.getMemberListId())
                            .setPayType("3")
                            .setGoAndCome("0")
                            .setAmount(mrrs.getAmount())
                            .setOrderNo(mrrs.getOrderNo())
                            .setBalance(memberList.getBalance()));
                    log.info(" 店铺 确认收货后7天完成订单:分销会员:" + mrrs.getOrderNo());
                });
            }

        }
    }

    /**
     * 订单导出列表
     *
     * @param orderStoreListVO
     * @return
     */
    @Override
    public List<OrderStoreListExportDTO> getOrderStoreListDtoExport(Map<String, Object> orderStoreListVO) {
        return baseMapper.getOrderStoreListDtoExport(orderStoreListVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> ordereDlivery(String listMap) {
        Result<String> result = new Result<String>();
        JSONObject jsonObject = JSONObject.parseObject(new String(listMap));
        List<Map<String, Object>> listObjectSec = jsonObject.getJSONArray("listMap").toJavaObject(List.class);
        OrderStoreSubList orderStoreSubList = new OrderStoreSubList();
        OrderStoreGoodRecord orderStoreGoodRecord;
        //OrderStoreSubList orderStoreSubList;
        List<Map<String, Object>> orderProviderGoodRecordInfoList;
        String addorderProviderId;
        if (listObjectSec.size() > 0) {
            for (Map<String, Object> map : listObjectSec) {
                System.out.println("map++++" + map);
                //包裹数据
                List<Map<String, Object>> listParcelMapSS = (List<Map<String, Object>>) (List) map.get("listParcel");
                for (Map<String, Object> listParcelMap : listParcelMapSS) {
                    System.out.println("listParcelMap++++" + listParcelMap);
                    //包裹内的商品修改供应商订单id
                    orderProviderGoodRecordInfoList = (List<Map<String, Object>>) (List) listParcelMap.get("orderProviderGoodRecordInfo");
                    System.out.println("orderProviderGoodRecordInfoList++++" + orderProviderGoodRecordInfoList);
                    if (orderProviderGoodRecordInfoList.size() > 0) {
                        //查询供应商订单信息
                        orderStoreSubList = orderStoreSubListService.getById(listParcelMap.get("id").toString());
                        //添加包裹,并返回新增ID
                        addorderProviderId = addorderStoreSubList(orderStoreSubList, listParcelMap.get("logisticsCompany").toString(), listParcelMap.get("trackingNumber").toString());

                        for (Map<String, Object> orderProviderGoodRecordId : orderProviderGoodRecordInfoList) {
                            //订单商品信息
                            orderStoreGoodRecord = orderStoreGoodRecordService.getById(orderProviderGoodRecordId.get("id").toString());
                            if (orderStoreGoodRecord == null) {
                                throw new JeecgBootException("订单商品信息不存在");
                            }
                            QueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new QueryWrapper<>();
                            orderRefundListLambdaQueryWrapper.select("IFNULL(sum('refund_amount'),0) as ongoingRefundCount");
                            orderRefundListLambdaQueryWrapper.eq("order_good_record_id", orderStoreGoodRecord.getId());
                            orderRefundListLambdaQueryWrapper.in("status", "0", "1", "2", "3", "4", "5");
                            Map<String, Object> refundMap = orderRefundListService.getMap(orderRefundListLambdaQueryWrapper);
                            if (Convert.toLong(refundMap.get("ongoingRefundCount")) > 0) {
                                throw new JeecgBootException("售后待处理订单，处理售后才可进行发货");
                            }
                            //修改商品的OrderProviderListId为包裹的已发货包裹
                            orderStoreGoodRecord.setOrderStoreSubListId(addorderProviderId);
                            orderStoreGoodRecord.setStatus("1");
                            orderStoreGoodRecordService.updateById(orderStoreGoodRecord);
                        }
                    }
                }

            }
            //调用方法
            //是否全部发货,修改orderList的状态内容
            ShipmentOrderModification(orderStoreSubList);
            result.success("添加成功!");
        }
        return result;
    }

    /**
     * 添加已发货的供应商包裹信息
     *
     * @param orderStoreSubList
     * @param logisticsCompany  物流公司
     * @param trackingNumber    快递单号
     */
    public String addorderStoreSubList(OrderStoreSubList orderStoreSubList, String logisticsCompany, String trackingNumber) {
        try {
            OrderStoreSubList opl = new OrderStoreSubList();
            // opl.setDelFlag(orderStoreSubList.getDelFlag());
            //opl = orderStoreSubList;
            opl.setDelFlag(orderStoreSubList.getDelFlag());
            opl.setMemberListId(orderStoreSubList.getMemberListId());
            opl.setOrderStoreListId(orderStoreSubList.getOrderStoreListId());
            opl.setSysUserId(orderStoreSubList.getSysUserId());
            opl.setOrderNo(orderStoreSubList.getOrderNo());
            opl.setDistribution(orderStoreSubList.getDistribution());
            opl.setStoreTemplateId(orderStoreSubList.getStoreTemplateId());
            opl.setTemplateName(orderStoreSubList.getTemplateName());
            opl.setAccountingRules(orderStoreSubList.getAccountingRules());
            opl.setChargeMode(orderStoreSubList.getChargeMode());
            opl.setAmount(orderStoreSubList.getAmount());
            opl.setShipFee(orderStoreSubList.getShipFee());
            opl.setStoreAddressIdSender(orderStoreSubList.getStoreAddressIdSender());
            opl.setStoreAddressIdTui(orderStoreSubList.getStoreAddressIdTui());
            opl.setLogisticsTracking(orderStoreSubList.getLogisticsTracking());
            opl.setGoodsTotal(orderStoreSubList.getGoodsTotal());
            //opl.setGoodsTotalCost(orderStoreSubList.getGoodsTotalCost());
            opl.setCustomaryDues(orderStoreSubList.getCustomaryDues());
            opl.setActualPayment(orderStoreSubList.getActualPayment());
            //修改数据
            opl.setParentId(orderStoreSubList.getId());
            opl.setLogisticsCompany(logisticsCompany);
            opl.setTrackingNumber(trackingNumber);
            opl.setStatus("2");
            orderStoreSubListService.save(opl);
            String id = opl.getId();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * //是否全部发货,修改orderList的状态内容
     *
     * @param orderStoreSubList
     * @return
     */
    public void ShipmentOrderModification(OrderStoreSubList orderStoreSubList) {
        if (orderStoreSubList != null) {
            List<OrderStoreGoodRecord> listOrderStoreGoodRecord;
            Boolean bl = false;
            OrderStoreList orderStoreList = getById(orderStoreSubList.getOrderStoreListId());
            QueryWrapper<OrderStoreSubList> queryWrapper = new QueryWrapper<OrderStoreSubList>();
            QueryWrapper<OrderStoreGoodRecord> queryWrapperOPGR = new QueryWrapper<OrderStoreGoodRecord>();
            if (orderStoreList != null) {
                queryWrapper.eq("order_store_list_id", orderStoreList.getId());
                //queryWrapper.ne("parent_id","0");
                //判断是否有发货
                List<OrderStoreSubList> listOrderStoreSubList = orderStoreSubListService.list(queryWrapper);
                if (listOrderStoreSubList.size() > 0) {
                    for (OrderStoreSubList oplfh : listOrderStoreSubList) {

                        if (!"0".equals(oplfh.getParentId())) {
                            //部分发货
                            orderStoreList.setIsSender("1");
                        } else {
                            //是否全部发货
                            queryWrapperOPGR = new QueryWrapper<OrderStoreGoodRecord>();
                            queryWrapperOPGR.eq("order_store_sub_list_id", oplfh.getId());
                            queryWrapperOPGR.in("status", "0", "2", "4", "5");
                            listOrderStoreGoodRecord = orderStoreGoodRecordService.list(queryWrapperOPGR);
                            if (listOrderStoreGoodRecord.size() > 0) {
                                //说明还没为发货商品
                                bl = true;
                            }
                        }
                    }
                    //bl = false 为已全部发货，改变状态为待收货
                    if (!bl) {
                        orderStoreList.setStatus("2");
                    }
                    if (orderStoreList.getShipmentsTime() == null || "".equals(orderStoreList.getShipmentsTime())) {
                        //第一次发货时间
                        orderStoreList.setShipmentsTime(new Date());
                    }
                    //子订单数量
                    orderStoreList.setChildOrder(new BigDecimal(listOrderStoreSubList.size()));
                    //修改订单信息
                    updateById(orderStoreList);
                }
            }
        }
    }

    public static void main(String[] args) {
//        JSONObject orderJson = new JSONObject();
//        JSONArray storeGoods = new JSONArray();
//        JSONObject storeGood = new JSONObject();
//        storeGood.put("id","168f004bbe6893e07ac2aa4b2562c02d");
//        storeGood.put("discountId","89205518a4e9feb3aa43e2f118e6727c");
//        storeGoods.add(storeGood);
//        orderJson.put("storeGoods",storeGoods);
//
//        String jsonString = orderJson.toJSONString();
//        System.out.println(jsonString);

//        BigDecimal mul = NumberUtil.mul(NumberUtil.div(new BigDecimal("50"), new BigDecimal("110")), new BigDecimal("50"));
//        BigDecimal mul1 = NumberUtil.mul(NumberUtil.div(new BigDecimal("50"), new BigDecimal("110")), new BigDecimal("60"));
//        System.out.println(mul);
//        System.out.println(mul1);

        BigDecimal add = NumberUtil.add(new BigDecimal("908.35"), new BigDecimal("1068.11"));
        BigDecimal sub = NumberUtil.sub(new BigDecimal("2862"), add);
        System.out.println(sub);
    }

}
