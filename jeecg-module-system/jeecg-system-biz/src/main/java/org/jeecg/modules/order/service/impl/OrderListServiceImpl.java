package org.jeecg.modules.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.FileUtil;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.service.IAgencyRechargeRecordService;
import org.jeecg.modules.alliance.entity.AllianceAccountCapital;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.entity.AllianceRechargeRecord;
import org.jeecg.modules.alliance.service.IAllianceAccountCapitalService;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.modules.alliance.service.IAllianceRechargeRecordService;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.mapper.GoodListMapper;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.order.dto.*;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.mapper.OrderListMapper;
import org.jeecg.modules.order.mapper.OrderProviderGoodRecordMapper;
import org.jeecg.modules.order.mapper.OrderProviderListMapper;
import org.jeecg.modules.order.mapper.OrderProviderTemplateMapper;
import org.jeecg.modules.order.service.*;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.order.vo.OrderListVO;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.service.IPayOrderCarLogService;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.provider.dto.ProviderAddressDTO;
import org.jeecg.modules.provider.entity.ProviderAccountCapital;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.entity.ProviderRechargeRecord;
import org.jeecg.modules.provider.mapper.ProviderTemplateMapper;
import org.jeecg.modules.provider.service.*;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysAreaService;
import org.jeecg.modules.system.service.ISysBlanceService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.taobao.service.IAli1688Service;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Service
@Slf4j
public class OrderListServiceImpl extends ServiceImpl<OrderListMapper, OrderList> implements IOrderListService {
    @Autowired(required = false)
    private OrderListMapper orderListMapper;
    @Autowired
    @Lazy
    private IMemberListService memberListService;
    @Autowired
    @Lazy
    private IOrderProviderListService orderProviderListService;
    @Autowired
    @Lazy
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    @Lazy
    private IMarketingDiscountCouponService marketingDiscountCouponService;

    @Autowired
    @Lazy
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    @Autowired
    @Lazy
    private IOrderProviderListService iOrderProviderListService;

    @Autowired
    @Lazy
    private IOrderProviderGoodRecordService iOrderProviderGoodRecordService;

    @Autowired
    @Lazy
    private IMemberListService iMemberListService;

    @Autowired
    @Lazy
    private IStoreManageService iStoreManageService;

    @Autowired
    @Lazy
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    @Lazy
    private IGoodListService iGoodListService;

    @Autowired
    @Lazy
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    @Lazy
    private IProviderAddressService providerAddressService;

    @Autowired
    @Lazy
    private IProviderTemplateService iProviderTemplateService;

    @Autowired
    @Lazy
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;

    @Autowired
    @Lazy
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    @Lazy
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    @Lazy
    private ISysAreaService iSysAreaService;

    @Autowired
    @Lazy
    private IAgencyManageService iAgencyManageService;

    @Autowired
    @Lazy
    private IAgencyRechargeRecordService iAgencyRechargeRecordService;

    @Autowired
    @Lazy
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;
    @Autowired
    @Lazy
    private IOrderStoreListService iOrderStoreListService;
    @Autowired
    @Lazy
    private IProviderManageService iProviderManageService;
    @Autowired
    @Lazy
    private IProviderRechargeRecordService iProviderRechargeRecordService;
    @Autowired
    @Lazy
    private IMemberDistributionRecordService iMemberDistributionRecordService;

    @Autowired
    @Lazy
    private IMarketingFreeOrderService iMarketingFreeOrderService;

    @Autowired
    @Lazy
    private IMarketingGroupRecordService iMarketingGroupRecordService;

    @Autowired
    @Lazy
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;


    @Resource
    private OrderProviderTemplateMapper orderProviderTemplateMapper;
    @Resource
    private ProviderTemplateMapper providerTemplateMapper;

    @Autowired
    @Lazy
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    @Lazy
    private IProviderAccountCapitalService iProviderAccountCapitalService;

    @Autowired
    @Lazy
    private IStoreAccountCapitalService iStoreAccountCapitalService;

    @Autowired
    @Lazy
    private IAgencyAccountCapitalService iAgencyAccountCapitalService;
    @Autowired
    @Lazy
    private IMarketingWelfarePaymentsService iMarketingWelfarePaymentsService;

    @Autowired
    @Lazy
    private IAllianceManageService iAllianceManageService;

    @Autowired
    @Lazy
    private IAllianceRechargeRecordService iAllianceRechargeRecordService;
    @Autowired
    @Lazy
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;
    @Autowired
    @Lazy
    private IOrderProviderTemplateService iOrderProviderTemplateService;
    @Autowired
    @Lazy
    private IMemberGradeService iMemberGradeService;

    @Autowired
    @Lazy
    private IMemberGrowthRecordService iMemberGrowthRecordService;

    @Autowired
    @Lazy
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;

    @Autowired
    @Lazy
    private IMarketingDiscountGoodService iMarketingDiscountGoodService;

    @Autowired
    @Lazy
    private ISysBlanceService iSysBlanceService;

    @Autowired
    @Lazy
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    @Lazy
    private IMarketingDiscountService iMarketingDiscountService;

    @Resource
    private OrderProviderListMapper orderProviderListMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private OrderProviderGoodRecordMapper orderProviderGoodRecordMapper;

    @Resource
    private GoodListMapper goodListMapper;

    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;

    @Autowired
    private PayUtils payUtils;

    @Autowired
    @Lazy
    private IPayOrderCarLogService iPayOrderCarLogService;

    @Autowired
    private IMarketingLeagueBuyerRecordService iMarketingLeagueBuyerRecordService;

    @Autowired
    private HftxPayUtils hftxPayUtils;

    @Autowired
    private IAli1688Service ali1688Service;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;


    @Override
    public IPage<OrderListDTO> getOrderListDto(Page<OrderList> page, OrderListVO orderListVO, String sysUserId) {
        IPage<OrderListDTO> page1 = orderListMapper.getOrderListDto(page, orderListVO);

        page1.getRecords().forEach(ol -> {
            if(StringUtils.isNotBlank(ol.getSerialNumber())) {
                PayOrderCarLog payOrderCarLog = iPayOrderCarLogService.getById(ol.getSerialNumber());
                ol.setIntegral(payOrderCarLog.getIntegral());
                ol.setIntegralPrice(payOrderCarLog.getIntegralPrice());
            }

            //会员信息
            MemberList memberList = memberListService.getMemberListById(ol.getMemberListId());
            if (memberList != null) {
                ol.setMemberList(memberList);
            }
            //优惠券信息
            MarketingDiscountCoupon marketingDiscountCoupon = marketingDiscountCouponService.getById(ol.getMarketingDiscountCouponId());
            if (marketingDiscountCoupon != null) {
                ol.setMarketingDiscountCoupon(marketingDiscountCoupon);
            }
            List<OrderProviderListDTO> orderProviderLists;
            /*if ("2".equals(ol.getStatus()) || "3".equals(ol.getStatus()) || "5".equals(ol.getStatus())) {
                //发货后商品信息


            } else {
                //未发货商品信息
                //查询供应商订单信息 sysUserId：null为平台登录，不为null为供应商登录
                orderProviderLists = orderProviderListService.selectorderListId(ol.getId(), sysUserId, "0", null);
            }*/
            orderProviderLists = orderProviderListService.selectorderListId(ol.getId(), sysUserId, null, null);
            orderProviderLists.forEach(opl -> {
                SysUser sysUser = sysUserService.getById(opl.getSysUserId());
                if (sysUser != null) {
                    opl.setSysUserName(sysUser.getRealname());
                }

                List<OrderProviderGoodRecord> orderProviderGoodRecords = orderProviderGoodRecordService.selectOrderProviderListId(opl.getId());
                if (orderProviderGoodRecords.size()>0){
                    //添加供应商订单商品记录
                    opl.setOrderProviderGoodRecords(orderProviderGoodRecords);
                }

                List<OrderProviderGoodRecordDTO> orderProviderGoodRecordDTOList = orderProviderGoodRecordService.selectOrderProviderListIdDTO(opl.getId());
                if (orderProviderGoodRecordDTOList.size()>0){
                    //添加供应商订单商品记录
                    opl.setOrderProviderGoodRecordDTOList(orderProviderGoodRecordDTOList);
                }



                //供应商发货信息
                Map<String, String> paramMap = Maps.newHashMap();
                paramMap.put("id", opl.getProviderAddressIdSender());
                if (opl.getProviderAddressIdSender() == null || "".equals(opl.getProviderAddressIdSender())) {
                    paramMap.put("sysUserId", opl.getSysUserId());
                    paramMap.put("isDeliver", "1");//发货默认
                    paramMap.put("isReturn", "");//退货
                }
                List<ProviderAddressDTO> listProviderAddressDTO = providerAddressService.getlistProviderAddress(paramMap);
                if (listProviderAddressDTO.size() > 0) {
                    opl.setProviderAddressDTOFa(listProviderAddressDTO.get(0));
                }
                //供应商退信息
                Map<String, String> paramMaptui = Maps.newHashMap();
                paramMaptui.put("id", opl.getProviderAddressIdTui());
                if (opl.getProviderAddressIdTui() == null || "".equals(opl.getProviderAddressIdTui())) {
                    paramMaptui.put("sysUserId", opl.getSysUserId());
                    paramMaptui.put("isDeliver", "");//发货默认
                    paramMaptui.put("isReturn", "1");//退货
                }
                List<ProviderAddressDTO> listProviderAddressDTOTui = providerAddressService.getlistProviderAddress(paramMaptui);
                if (listProviderAddressDTO.size() > 0) {
                    opl.setProviderAddressDTOTui(listProviderAddressDTOTui.get(0));
                }
            });


            //添加供应商订单列表
            if (orderProviderLists.size() > 0) {

                ol.setOrderProviderListDTOs(orderProviderLists.stream()
                        .filter(item->item.getOrderProviderGoodRecords()!=null&&item.getOrderProviderGoodRecords().size()>0).collect(Collectors.toList()));


                List<String> orderProviderIdList = new ArrayList<>();
                //查询运费模板信息
                for (OrderProviderListDTO opl : orderProviderLists) {
                    orderProviderIdList.add(opl.getId());
                }
                List<Map<String, Object>> providerTemplateMaps = new ArrayList<>();
                orderProviderIdList = orderProviderIdList.stream().distinct().collect(Collectors.toList());
                //查询运费模板信息
                providerTemplateMaps = iOrderProviderTemplateService.getOrderProviderTemplateMaps(orderProviderIdList);
                if (providerTemplateMaps.size() == 0) {
                    //如果未生成运费模板信息,查询运费模板匹配信息
                    providerTemplateMaps = iProviderTemplateService.getProviderTemplateMaps(orderProviderIdList);
                }
                //添加运费模板信息
                ol.setProviderTemplateMaps(providerTemplateMaps);
            }
        });

        return page1;
    }

    /**
     * 供应商列表用的
     *
     * @param page
     * @param orderListVO
     * @param sysUserId
     * @return
     */

    //发货后的商品显示
    @Override
    public IPage<OrderListDTO> getOrderListDtoToSendTheGoods(Page<OrderList> page, OrderListVO orderListVO, String sysUserId) {
        IPage<OrderListDTO> page1 = orderListMapper.getOrderListDtoNew(page, orderListVO);
        page1.getRecords().forEach(ol -> {
            //会员信息
            MemberList memberList = memberListService.getMemberListById(ol.getMemberListId());
            if (memberList != null) {
                ol.setMemberList(memberList);
            }
            //优惠券信息
            MarketingDiscountCoupon marketingDiscountCoupon = marketingDiscountCouponService.getById(ol.getMarketingDiscountCouponId());
            if (marketingDiscountCoupon != null) {
                ol.setMarketingDiscountCoupon(marketingDiscountCoupon);
            }
            //查询供应商订单信息 sysUserId：null为平台登录，不为null为供应商登录
            List<OrderProviderListDTO> orderProviderLists = orderProviderListService.selectorderListId(ol.getId(), sysUserId, null, "0");
            orderProviderLists.forEach(opl -> {
                SysUser sysUser = sysUserService.getById(opl.getSysUserId());
                if (sysUser != null) {
                    opl.setSysUserName(sysUser.getRealname());
                }

                List<OrderProviderGoodRecord> orderProviderGoodRecords = orderProviderGoodRecordService.selectOrderProviderListId(opl.getId());
                //添加供应商订单商品记录
                opl.setOrderProviderGoodRecords(orderProviderGoodRecords);
                List<OrderProviderGoodRecordDTO> orderProviderGoodRecordDTOList = orderProviderGoodRecordService.selectOrderProviderListIdDTO(opl.getId());
                //添加供应商订单商品记录
                opl.setOrderProviderGoodRecordDTOList(orderProviderGoodRecordDTOList);

                //供应商发货信息
                Map<String, String> paramMap = Maps.newHashMap();
                paramMap.put("id", opl.getProviderAddressIdSender());
                if (opl.getProviderAddressIdSender() == null || "".equals(opl.getProviderAddressIdSender())) {
                    paramMap.put("sysUserId", opl.getSysUserId());
                    paramMap.put("isDeliver", "1");//发货默认
                    paramMap.put("isReturn", "");//退货
                }
                List<ProviderAddressDTO> listProviderAddressDTO = providerAddressService.getlistProviderAddress(paramMap);
                if (listProviderAddressDTO.size() > 0) {
                    opl.setProviderAddressDTOFa(listProviderAddressDTO.get(0));
                }
                //供应商退信息
                Map<String, String> paramMaptui = Maps.newHashMap();
                paramMaptui.put("id", opl.getProviderAddressIdTui());
                if (opl.getProviderAddressIdTui() == null || "".equals(opl.getProviderAddressIdTui())) {
                    paramMaptui.put("sysUserId", opl.getSysUserId());
                    paramMaptui.put("isDeliver", "");//发货默认
                    paramMaptui.put("isReturn", "1");//退货
                }
                List<ProviderAddressDTO> listProviderAddressDTOTui = providerAddressService.getlistProviderAddress(paramMaptui);
                if (listProviderAddressDTO.size() > 0) {
                    opl.setProviderAddressDTOTui(listProviderAddressDTOTui.get(0));
                }

            });
            //添加供应商订单列表
            if (orderProviderLists.size() > 0) {
                ol.setOrderProviderListDTOs(orderProviderLists);
                List<String> orderProviderIdList = new ArrayList<>();
                //查询运费模板信息
                for (OrderProviderListDTO opl : orderProviderLists) {
                    orderProviderIdList.add(opl.getId());
                }
                orderProviderIdList = orderProviderIdList.stream().distinct().collect(Collectors.toList());
                List<Map<String, Object>> providerTemplateMaps = new ArrayList<>();
                //查询运费模板信息
                providerTemplateMaps = iOrderProviderTemplateService.getOrderProviderTemplateMaps(orderProviderIdList);
                if (providerTemplateMaps.size() == 0) {
                    //如果未生成运费模板信息,查询运费模板匹配信息
                    providerTemplateMaps = iProviderTemplateService.getProviderTemplateMaps(orderProviderIdList);
                }
                //添加运费模板信息
                ol.setProviderTemplateMaps(providerTemplateMaps);
            }
        });


        return page1;
    }

    @Override
    @Transactional
    public OrderList submitOrderGoods(List<Map<String, Object>> goods,
                                      String memberId, String orderJson, MemberShippingAddress memberShippingAddress,
                                      String sysUserId, int model, String longitude, String latitude) {
        //总运费
        BigDecimal freight = new BigDecimal(0);
        //价格
        BigDecimal totalPrice = new BigDecimal(0);
        //成本价
        BigDecimal costPrice = new BigDecimal(0);
        //平台成本价
        BigDecimal platformProfit = new BigDecimal(0);
        //件数
        BigDecimal allNumberUnits = new BigDecimal(0);
        //福利金
        BigDecimal welfarePayments = new BigDecimal(0);
        //赠送福利金
        BigDecimal giveWelfarePayments = new BigDecimal(0);

        //会员直降总额
        BigDecimal vipLowerTotal = new BigDecimal(0);

        //会员等级优惠总金额
        BigDecimal memberDiscountPriceTotal = new BigDecimal(0);

        //免单专区商品id
        String marketingFreeGoodListId=null;

        String marketingGroupRecordId=null;

        //优惠金额总金额   coupon
        BigDecimal couponTotal = new BigDecimal(0);

        //获取用户信息
        MemberList memberList = iMemberListService.getById(memberId);
        //解析订单json
        JSONObject orderJsonObject = JSON.parseObject(orderJson);

        JSONObject jsonGoods = null;
        if (model == 0) {
            jsonGoods = orderJsonObject.getJSONObject("goods");
        } else {
            jsonGoods = orderJsonObject.getJSONObject("marketingFreeGoods");
        }

        //建立平台订单
        OrderList orderList = new OrderList();
        orderList.setDelFlag("0");
        if (model == 0) {
            //普通订单
            orderList.setOrderType("0");
        } else {
            //免单专区订单
            orderList.setOrderType("3");
        }
        //订单号
        orderList.setOrderNo(OrderNoUtils.getOrderNo());
        //会员设置
        orderList.setMemberListId(memberId);
        //收货地址设置
        orderList.setConsignee(memberShippingAddress.getLinkman());
        orderList.setContactNumber(memberShippingAddress.getPhone());
        orderList.setShippingAddress(memberShippingAddress.getAreaExplan() + memberShippingAddress.getAreaAddress());
        orderList.setHouseNumber(memberShippingAddress.getHouseNumber());
        orderList.setSysAreaId(memberShippingAddress.getSysAreaId());
        orderList.setSysAreaIds(memberShippingAddress.getAreaExplanIds());
        //设置留言
        orderList.setMessage(jsonGoods.getString("message"));
        orderList.setCoupon(new BigDecimal(0));
        orderList.setWelfarePayments(new BigDecimal(0));

        //优惠券的统计参数
        BigDecimal costPriceMarketingDiscount = new BigDecimal(0);//优惠券商品成本价总和
        BigDecimal totalPriceMarketingDiscount = new BigDecimal(0);//优惠券商品金额总和
        BigDecimal couponPriceMarketingDiscount = new BigDecimal(0);//优惠券优惠金额
        List<String> marketingDiscountGoods = Lists.newArrayList();
        MarketingDiscount marketingDiscount = null;//优惠券信息

        if (model == 0) {
            //通过优惠券判断优惠金额
            String discountId = jsonGoods.getString("discountId");
            if (!oConvertUtils.isEmpty(discountId)) {
                //设置优惠金额
                MarketingDiscountCoupon marketingDiscountCoupon = iMarketingDiscountCouponService.getById(discountId);

                if (marketingDiscountCoupon == null || !marketingDiscountCoupon.getStatus().equals("1")) {
                    log.info("优惠券不可用");
                }
                orderList.setCoupon(marketingDiscountCoupon.getPrice());
                //添加优惠金额
                couponTotal = couponTotal.add(marketingDiscountCoupon.getPrice());
                orderList.setDiscountOuponPrice(marketingDiscountCoupon.getPrice());
                //标识优惠金额
                couponPriceMarketingDiscount = marketingDiscountCoupon.getPrice();
                //标识优惠券已使用                                                                                                                                                                            1
                marketingDiscountCoupon.setStatus("2");
                marketingDiscountCoupon.setUserTime(new Date());
                //优惠券id
                orderList.setMarketingDiscountCouponId(marketingDiscountCoupon.getId());
                iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon);
                //获取优惠券商品
                marketingDiscount = iMarketingDiscountService.getById(marketingDiscountCoupon.getMarketingDiscountId());
                if (marketingDiscount.getIsDistribution().equals("0")) {
                    QueryWrapper<MarketingDiscountGood> marketingDiscountGoodQueryWrapper = new QueryWrapper<>();
                    marketingDiscountGoodQueryWrapper.eq("marketing_discount_id", marketingDiscountCoupon.getMarketingDiscountId());
                    List<MarketingDiscountGood> marketingDiscountGoodsList = iMarketingDiscountGoodService.list(marketingDiscountGoodQueryWrapper);
                    for (MarketingDiscountGood marketingDiscountGood : marketingDiscountGoodsList) {
                        marketingDiscountGoods.add(marketingDiscountGood.getGoodId());
                    }
                }
            } else {
                orderList.setCoupon(new BigDecimal(0));
            }
        }
        //设置无修改地址
        orderList.setIsUpdateAddr("0");
        //设置订单状态
        orderList.setStatus("0");
        //推广人设置
        String promoterType = memberList.getPromoterType();
        String promoter = memberList.getPromoter();
        if (StringUtils.isNotBlank(promoter)) {
            if (promoterType.equals("2")) {
                orderList.setPromoter("平台")
                        .setPromoterType("2");
            } else {
                orderList
                        .setPromoter(promoter)
                        .setPromoterType(promoterType);
            }
        }

        //归属店铺
        String msysUserId = memberList.getSysUserId();
        if (StringUtils.isNotBlank(msysUserId)) {
            QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
            storeManageQueryWrapper.eq("sys_user_id", msysUserId);
            storeManageQueryWrapper.in("pay_status", "1", "2");
            if (iStoreManageService.count(storeManageQueryWrapper) > 0) {
                StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                orderList.setAffiliationStore(storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
            } else {
                orderList.setAffiliationStore("用户未绑定归属");
            }
        }

        //归属渠道
        if (StringUtils.isNotBlank(sysUserId)) {
            orderList.setDistributionChannel(sysUserId);
        }

        //配送方式
        orderList.setDistribution(jsonGoods.getString("distribution"));

        //是否部分发货
        orderList.setIsSender("0");

        //设置未评价
        orderList.setIsEvaluate("0");
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        //供应商信息列表
        List<Map<String, Object>> goodsMap = Lists.newArrayList();
        String memberGradeContent = "";
        //拆分供应商
        for (Map<String, Object> g : goods) {
            //计算总价格
            totalPrice = totalPrice.add(((BigDecimal) g.get("price")).multiply((BigDecimal) g.get("quantity")));

            //获取成本价
            GoodSpecification goodSpecification = iGoodSpecificationService.getById(g.get("goodSpecificationId").toString());
            costPrice = costPrice.add(goodSpecification.getSupplyPrice().multiply((BigDecimal) g.get("quantity")));

            //优惠券总价计算
            if (marketingDiscountGoods.contains(g.get("goodId"))) {
                log.info("订单发现优惠券里面的商品，商品id：" + g.get("goodId"));
                totalPriceMarketingDiscount = totalPriceMarketingDiscount.add(((BigDecimal) g.get("price")).multiply((BigDecimal) g.get("quantity")));
                costPriceMarketingDiscount = costPriceMarketingDiscount.add(goodSpecification.getSupplyPrice().multiply((BigDecimal) g.get("quantity")));
            }


            //平台成本价
            platformProfit = platformProfit.add(goodSpecification.getCostPrice().multiply((BigDecimal) g.get("quantity")));
            //商品总件数
            allNumberUnits = allNumberUnits.add((BigDecimal) g.get("quantity"));
            //会员等级折扣金额
            if (g.containsKey("memberDiscountPrice") && oConvertUtils.isNotEmpty(g.get("memberDiscountPrice"))) {
                memberDiscountPriceTotal = memberDiscountPriceTotal.add(new BigDecimal(g.get("memberDiscountPrice").toString()).multiply((BigDecimal) g.get("quantity")));

            }
            if (g.containsKey("memberGradeContent") && oConvertUtils.isNotEmpty(g.get("memberGradeContent"))) {
                memberGradeContent = g.get("memberGradeContent").toString();
            }

            //普通商品送福利金计算
            if (model == 0) {

                //判断兑换券
                if (g.get("marketingCertificateRecordId") != null) {
                    MarketingCertificateRecord marketingCertificateRecord = iMarketingCertificateRecordService.getById(g.get("marketingCertificateRecordId").toString());
                    //使得兑换券已使用
                    if (marketingCertificateRecord.getStatus().equals("1")) {
                        marketingCertificateRecord.setStatus("2");
                        marketingCertificateRecord.setUserTime(new Date());
                        iMarketingCertificateRecordService.saveOrUpdate(marketingCertificateRecord);
                        orderList.setOrderType("6");
                        orderList.setActiveId(marketingCertificateRecord.getId());
                    }
                }

                //加盟专区判断
                if(g.get("marketingLeagueGoodListId")!=null){
                    orderList.setOrderType("7");
                    orderList.setActiveId(g.get("marketingLeagueGoodListId").toString());
                }

                //判断专区商品
                if (g.get("marketingPrefectureId") != null) {
                    //获取专区商品
                    QueryWrapper<MarketingPrefectureGood> marketingPrefectureGoodQueryWrapper = new QueryWrapper<>();
                    marketingPrefectureGoodQueryWrapper.eq("marketing_prefecture_id", g.get("marketingPrefectureId").toString());
                    marketingPrefectureGoodQueryWrapper.eq("good_list_id", g.get("goodId").toString());
                    MarketingPrefectureGood marketingPrefectureGood = iMarketingPrefectureGoodService.getOne(marketingPrefectureGoodQueryWrapper);

                    //获取专区规格
                    QueryWrapper<MarketingPrefectureGoodSpecification> marketingPrefectureGoodSpecificationQueryWrapper = new QueryWrapper<>();
                    marketingPrefectureGoodSpecificationQueryWrapper.eq("marketing_prefecture_good_id", marketingPrefectureGood.getId());
                    marketingPrefectureGoodSpecificationQueryWrapper.eq("good_specification_id", g.get("goodSpecificationId").toString());
                    MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification = iMarketingPrefectureGoodSpecificationService.getOne(marketingPrefectureGoodSpecificationQueryWrapper);
                    //可抵用福利金的计划
                    BigDecimal welfareProportion = marketingPrefectureGoodSpecification.getWelfareProportion();
                    if (marketingPrefectureGoodSpecification.getIsWelfare().equals("1")) {
                        welfareProportion = new BigDecimal(100);
                    }
                    BigDecimal welfareProportionPrice = null;

                    if (marketingPrefectureGoodSpecification.getIsWelfare().equals("3")) {
                        welfareProportionPrice = marketingPrefectureGoodSpecification.getPrefecturePrice().subtract(goodSpecification.getSupplyPrice()).multiply(welfareProportion.divide(new BigDecimal(100))).multiply((BigDecimal) g.get("quantity")).divide(integralValue,2,RoundingMode.DOWN);
                    } else {
                        welfareProportionPrice = marketingPrefectureGoodSpecification.getPrefecturePrice().multiply(welfareProportion.divide(new BigDecimal(100))).multiply((BigDecimal) g.get("quantity")).divide(integralValue,2,RoundingMode.DOWN);
                    }

                    //判断专区会员直降
                    MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(g.get("marketingPrefectureId").toString());
                    //支持会员免福利金
                    if (marketingPrefecture.getIsVipLower().equals("1") && memberList.getMemberType().equals("1")) {
                        vipLowerTotal = vipLowerTotal.add(welfareProportionPrice).multiply(integralValue);
                        welfareProportionPrice = new BigDecimal(0);
                    }

                    log.info("商品福利金抵扣：" + welfareProportionPrice);

                    welfarePayments = welfarePayments.add(welfareProportionPrice);

                    //赠送福利金的计算
                    BigDecimal giveWelfareProportion = marketingPrefectureGoodSpecification.getGiveWelfareProportion();
                    giveWelfarePayments = giveWelfarePayments.add(marketingPrefectureGoodSpecification.getPrefecturePrice().multiply(giveWelfareProportion.divide(new BigDecimal(100))).multiply((BigDecimal) g.get("quantity"))).divide(integralValue,2,RoundingMode.DOWN);
                    //专区订单
                    orderList.setOrderType("5");
                    orderList.setActiveId(marketingPrefecture.getId());
                    orderList.setMarketingPrefectureGoodId(marketingPrefectureGood.getId());
                    orderList.setMarketingRushGroupId(String.valueOf(g.get("marketingRushGroupId")));

                }
                //中奖拼团商品
                if(g.get("marketingGroupRecordId")!=null){
                    marketingGroupRecordId=g.get("marketingGroupRecordId").toString();
                }



            }
            if(model==1){
                //给免单专区商品赋予专区商品id
                if(marketingFreeGoodListId==null&&g.get("marketingFreeGoodListId")!=null){
                    marketingFreeGoodListId=g.get("marketingFreeGoodListId").toString();
                    orderList.setActiveId(marketingFreeGoodListId);
                }
            }
            Map<String, Object> provideMap = null;
            List<Map<String, Object>> provideGoodsList = null;

            //选取已有供应商
            for (Map<String, Object> gm : goodsMap) {
                if (gm.get("id").toString().equals(g.get("sysUserId").toString())) {
                    provideMap = gm;
                    break;
                }
            }

            //加入供应商结构信息
            if (provideMap == null) {
                provideMap = Maps.newHashMap();
                provideGoodsList = Lists.newArrayList();
                provideMap.put("provideGoodsList", provideGoodsList);
                provideMap.put("id", g.get("sysUserId"));
                goodsMap.add(provideMap);
            } else {
                provideGoodsList = (List<Map<String, Object>>) provideMap.get("provideGoodsList");
            }
            //商品加入供应商中归类
            provideGoodsList.add(g);

        }


        log.info("福利金优惠之前的优惠金额：" + couponTotal);


        //平台福利金的计算
        if (model == 0) {
            //抵扣福利金的总数与会员福利金的情况进行限制
            if (iMemberListService.getById(memberId).getWelfarePayments().subtract(welfarePayments).doubleValue() < 0) {
                welfarePayments = iMemberListService.getById(memberId).getWelfarePayments();
            }
            orderList.setWelfarePayments(welfarePayments);
            orderList.setWelfarePaymentsPrice(welfarePayments.multiply(integralValue).setScale(2,RoundingMode.HALF_UP));
            //添加优惠金额(福利金抵扣)
            couponTotal = couponTotal.add(welfarePayments.multiply(integralValue)).setScale(2,RoundingMode.HALF_UP);
        }


        log.info("订单总优惠金额：" + couponTotal);

        //会员直降金额
        orderList.setVipLowerTotal(vipLowerTotal);

        //赠送福利金的数据设置
        orderList.setGiveWelfarePayments(giveWelfarePayments);


        //有地址计算运费
        if (memberShippingAddress != null) {
            freight = iProviderTemplateService.calculateFreight(goods, memberShippingAddress.getSysAreaId());
            orderList.setShipFee(freight);
        }

        //拆分前保存订单
        //保存订单信息
        this.saveOrUpdate(orderList);
        //免单专区加入专区订单信息
        if(marketingFreeGoodListId!=null){
            iMarketingFreeOrderService.submitOrder(orderList.getId());
        }
        //中奖拼团记录加入订单id
        if(marketingGroupRecordId!=null){
           orderList.setOrderType("4");
           orderList.setActiveId(marketingGroupRecordId);
        }

        //供应商订单设置
        for (Map<String, Object> gm : goodsMap) {
            //商品总价(成本价)
            BigDecimal goodsTotal = new BigDecimal(0);
            //商品总成本（供货价）
            BigDecimal goodsTotalCost = new BigDecimal(0);
            //建立供应商订单
            OrderProviderList orderProviderList = new OrderProviderList();
            orderProviderList.setDelFlag("0");
            orderProviderList.setMemberListId(memberId);
            //设置订单id
            orderProviderList.setOrderListId(orderList.getId());
            //设置供应商id
            orderProviderList.setSysUserId(gm.get("id").toString());
            //设置订单号
            orderProviderList.setOrderNo(OrderNoUtils.getOrderNo());
            orderProviderList.setDistribution(orderList.getDistribution());
            orderProviderList.setParentId("0");
            orderProviderList.setStatus("0");

            //保存订单信息
            iOrderProviderListService.saveOrUpdate(orderProviderList);
            //建立供应商商品快照

            List<Map<String, Object>> provideGoodsList = (List<Map<String, Object>>) gm.get("provideGoodsList");

            //有地址计算运费
            if (memberShippingAddress != null) {
                orderProviderList.setShipFee(iProviderTemplateService.calculateFreight(provideGoodsList, memberShippingAddress.getSysAreaId()));

                //添加订单运费模板信息
                iProviderTemplateService.addOrderTemplate(provideGoodsList, memberShippingAddress.getSysAreaId(), orderProviderList);
            }
            //会员等级供应商优惠总金额
            BigDecimal memberProvideDiscountPriceTotal = new BigDecimal(0);
            for (Map<String, Object> p : provideGoodsList) {
                GoodSpecification goodSpecification = iGoodSpecificationService.getById(p.get("goodSpecificationId").toString());
                GoodList goodList = iGoodListService.getById(p.get("goodId").toString());

                goodsTotal = goodsTotal.add(goodSpecification.getCostPrice().multiply((BigDecimal) p.get("quantity")));
                goodsTotalCost = goodsTotalCost.add(goodSpecification.getSupplyPrice().multiply((BigDecimal) p.get("quantity")));

                //添加商品记录
                OrderProviderGoodRecord orderProviderGoodRecord = new OrderProviderGoodRecord();
                orderProviderGoodRecord.setDelFlag("0");
                orderProviderGoodRecord.setOrderProviderListId(orderProviderList.getId());
                orderProviderGoodRecord.setMainPicture(goodList.getMainPicture());
                orderProviderGoodRecord.setGoodListId(goodList.getId());
                orderProviderGoodRecord.setGoodSpecificationId(goodSpecification.getId());
                orderProviderGoodRecord.setGoodName(goodList.getGoodName());
                orderProviderGoodRecord.setSpecification(goodSpecification.getSpecification());
                orderProviderGoodRecord.setPrice(goodSpecification.getPrice());
                orderProviderGoodRecord.setVipPrice(goodSpecification.getVipPrice());
                orderProviderGoodRecord.setSupplyPrice(goodSpecification.getSupplyPrice());
                orderProviderGoodRecord.setCostPrice(goodSpecification.getCostPrice());
                orderProviderGoodRecord.setUnitPrice((BigDecimal) p.get("price"));
                orderProviderGoodRecord.setAmount((BigDecimal) p.get("quantity"));
                orderProviderGoodRecord.setGoodNo(goodList.getGoodNo());
                orderProviderGoodRecord.setSkuNo(goodSpecification.getSkuNo());
                orderProviderGoodRecord.setTotal(orderProviderGoodRecord.getUnitPrice().multiply(orderProviderGoodRecord.getAmount()));
                //商品总重量
                orderProviderGoodRecord.setWeight(goodSpecification.getWeight().multiply(new BigDecimal(p.get("quantity").toString())).setScale(3, RoundingMode.DOWN));
                if (p.containsKey("label") && oConvertUtils.isNotEmpty(p.get("label"))) {
                    //添加专区标签
                    orderProviderGoodRecord.setPrefectureLabel(p.get("label").toString());
                }
                if (p.containsKey("marketingPrefectureId") && oConvertUtils.isNotEmpty(p.get("marketingPrefectureId"))) {
                    //添加专区标签
                    orderProviderGoodRecord.setMarketingPrefectureId((String) p.get("marketingPrefectureId"));
                }
                //会员等级折扣金额
                if (p.containsKey("memberDiscountPrice") && oConvertUtils.isNotEmpty(p.get("memberDiscountPrice"))) {
                    orderProviderGoodRecord.setMemberDiscountPrice((BigDecimal) p.get("memberDiscountPrice"));
                    orderProviderGoodRecord.setTotal((orderProviderGoodRecord.getUnitPrice().subtract(orderProviderGoodRecord.getMemberDiscountPrice())).multiply(orderProviderGoodRecord.getAmount()));
                    //会员等级优惠金额总和(供应商)
                    memberProvideDiscountPriceTotal = memberProvideDiscountPriceTotal.add(new BigDecimal(p.get("memberDiscountPrice").toString()).multiply((BigDecimal) p.get("quantity"))).setScale(2, RoundingMode.DOWN);
                    //添加会员等级数据
                    //会员等级
                    if (p.containsKey("memberGrade") && oConvertUtils.isNotEmpty(p.get("memberGrade"))) {
                        orderProviderGoodRecord.setMemberGrade(p.get("memberGrade").toString());
                        if (p.containsKey("memberGradeContent") && oConvertUtils.isNotEmpty(p.get("memberGradeContent"))) {
                            orderProviderGoodRecord.setMemberGradeContent(p.get("memberGradeContent").toString());
                        }
                    }
                }
                iOrderProviderGoodRecordService.save(orderProviderGoodRecord);
                //增加销量
                //扣除库存
                goodSpecification.setRepertory(goodSpecification.getRepertory().subtract(orderProviderGoodRecord.getAmount()));
                iGoodListService.saveOrUpdate(goodList);
                iGoodSpecificationService.saveOrUpdate(goodSpecification);

            }

            //商品总价(成本价)
            orderProviderList.setGoodsTotal(goodsTotal);
            //商品总成本（供货价）
            orderProviderList.setGoodsTotalCost(goodsTotalCost);
            //应付款
            orderProviderList.setCustomaryDues(orderProviderList.getGoodsTotal().add(orderProviderList.getShipFee()));
            //实付款
            orderProviderList.setActualPayment(orderProviderList.getGoodsTotal().add(orderProviderList.getShipFee()));

            //添加会员等级数据
            if (memberList.getMemberType().equals("1")) {
                //会员等级优惠总和
                orderProviderList.setMemberDiscountPriceTotal(memberProvideDiscountPriceTotal);
                //会员等级计算价格
                if (org.apache.commons.lang.StringUtils.isNotBlank(memberList.getMemberGradeId())) {
                    MemberGrade memberGrade = iMemberGradeService.getById(memberList.getMemberGradeId());
                    if (memberGrade != null) {
                        //会员等级
                        orderProviderList.setMemberGrade(memberGrade.getGradeName());
                    }
                }
            }

            //预先分配供应商的订单资金,产生需要支付给供应商的资金余额，加入到供应商的冻结资金里面
            //获取供应商
            ProviderManage providerManage = iProviderManageService.getOne(new LambdaQueryWrapper<ProviderManage>()
                    .eq(ProviderManage::getSysUserId, orderProviderList.getSysUserId()).last("limit 1"));

            if (oConvertUtils.isNotEmpty(providerManage)) {
                //生成余额明细
                iProviderRechargeRecordService.save(new ProviderRechargeRecord()
                        .setDelFlag("0")
                        .setSysUserId(providerManage.getSysUserId())
                        .setPayType("0")
                        .setGoAndCome("0")
                        .setAmount(orderProviderList.getActualPayment())
                        .setTradeStatus("0")
                        .setOrderNo(OrderNoUtils.getOrderNo())
                        .setTradeNo(orderProviderList.getOrderNo())
                        .setRemark("供应商订单[" + orderProviderList.getOrderNo() + "]")
                        .setTradeType("0"));
            }
            //保存订单信息
            iOrderProviderListService.saveOrUpdate(orderProviderList);
        }
        //子订单数(拆分供应商后写入)
        orderList.setChildOrder(new BigDecimal(goodsMap.size()));
        //设置商品总价
        orderList.setGoodsTotal(totalPrice);
        //商品总件数
        orderList.setAllNumberUnits(allNumberUnits);
        //运费
        orderList.setShipFee(freight);
        //成本价
        costPrice = costPrice.add(freight);
        orderList.setCostPrice(costPrice);
        //平台成本价
        platformProfit = platformProfit.add(freight);
        orderList.setPlatformProfit(platformProfit);
        //平台净利润
        orderList.setPlatformRetainedProfits(orderList.getCostPrice().subtract(orderList.getPlatformProfit()));

        //添加优惠金额(会员等级折扣优惠价格)
        couponTotal = couponTotal.add(memberDiscountPriceTotal);
        orderList.setCoupon(couponTotal);
        //控制优惠金额的优惠幅度
        if (totalPrice.subtract(orderList.getCoupon()).doubleValue() < 0) {
            orderList.setCoupon(totalPrice);
        }

        //应付款
        orderList.setCustomaryDues(totalPrice.subtract(orderList.getCoupon()).subtract(vipLowerTotal));
        //实付款
        orderList.setActualPayment(totalPrice.subtract(orderList.getCoupon()).subtract(vipLowerTotal));

        //添加会员等级数据
        if (memberList.getMemberType().equals("1")) {
            //会员等级折扣优惠价格
            orderList.setMemberDiscountPriceTotal(memberDiscountPriceTotal);
            //会员等级计算价格
            if (StringUtils.isNotBlank(memberList.getMemberGradeId())) {
                MemberGrade memberGrade = iMemberGradeService.getById(memberList.getMemberGradeId());
                if (memberGrade != null) {
                    //会员等级
                    orderList.setMemberGrade(memberGrade.getGradeName());
                    //添加会员等级特权信息
                    if (StringUtils.isNotBlank(memberGradeContent)) {
                        orderList.setMemberGradeContent(memberGradeContent);
                    }

                }
            }
        }

        //实付款小于等于，就设置为0
        if (orderList.getActualPayment().doubleValue() <= 0) {
            orderList.setActualPayment(new BigDecimal(0));
        }

        //给应付款和支付款加上运费
        orderList.setCustomaryDues(orderList.getCustomaryDues().add(freight));
        orderList.setActualPayment(orderList.getActualPayment().add(freight));

        //无优惠的总价
        totalPrice = totalPrice.add(freight);

        //商品利润
        orderList.setProfit(orderList.getActualPayment().subtract(costPrice));


        //添加经纬度信息
        if (StringUtils.isNotBlank(longitude)) {
            orderList.setLongitude(new BigDecimal(longitude));
        } else {
            orderList.setLongitude(new BigDecimal(0));
        }

        if (StringUtils.isNotBlank(latitude)) {
            orderList.setLatitude(new BigDecimal(latitude));
        } else {
            orderList.setLatitude(new BigDecimal(0));
        }



        //分销佣金

        //获取分销佣金的比例
        orderList.setDistributionCommission(new BigDecimal(0));

        //goods_distribution_commission_source 商品分销佣金来源：0：商品实付款；1：商品利润；
        String goodsDistributionCommissionSource = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_distribution_commission_source");

        //按照商品利润的分法
        if (goodsDistributionCommissionSource.equals("1")) {
            if (orderList.getProfit().doubleValue() > 0) {
                //建立优惠券需要扣的利润金额
                BigDecimal subProfit = new BigDecimal(0);
                //判断优惠券不可为空
                if (marketingDiscount != null && marketingDiscount.getIsDistribution().equals("0")) {
                    subProfit = totalPriceMarketingDiscount.subtract(costPriceMarketingDiscount).subtract(couponPriceMarketingDiscount);
                    if (subProfit.doubleValue() <= 0) {
                        subProfit = new BigDecimal(0);
                    }
                }
                //控制优惠金额
                if (orderList.getProfit().subtract(subProfit).doubleValue() >= 0) {
                    //获取常量信息
                    String commissionRate = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "commission_rate"), "%");
                    orderList.setDistributionCommission(orderList.getProfit().subtract(subProfit).multiply(new BigDecimal(commissionRate).divide(new BigDecimal(100))));
                }
            }
            log.info("订单分销金额按照商品利润进行分配:" + orderList.getDistributionCommission());
        }
        //按照商品实付款的分法去除运费
        if (goodsDistributionCommissionSource.equals("0")) {
            if (orderList.getActualPayment().subtract(freight).doubleValue() > 0) {

                //建立优惠券需要扣的利润金额
                BigDecimal subActualPayment = new BigDecimal(0);
                //判断优惠券不可为空
                if (marketingDiscount != null && marketingDiscount.getIsDistribution().equals("0")) {
                    subActualPayment = totalPriceMarketingDiscount.subtract(couponPriceMarketingDiscount);
                    if (subActualPayment.doubleValue() <= 0) {
                        subActualPayment = new BigDecimal(0);
                    }
                }

                if (orderList.getActualPayment().subtract(subActualPayment).doubleValue() >= 0) {
                    //获取常量信息
                    String commissionRate = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "commission_rate"), "%");
                    orderList.setDistributionCommission(orderList.getActualPayment().subtract(subActualPayment).subtract(freight).multiply(new BigDecimal(commissionRate).divide(new BigDecimal(100))));

                }


            }
            log.info("订单分销金额按照商品实付款扣除运费进行分配:" + orderList.getDistributionCommission());
        }

        //实际分销支出
        BigDecimal actualDistribution=new BigDecimal(0);

        /**
         * 订单是否分销
         */
        boolean distributionCommission=true;


        //修改中奖拼团记录信息
        if(marketingGroupRecordId!=null){
            MarketingGroupRecord marketingGroupRecord=iMarketingGroupRecordService.getById(marketingGroupRecordId);
            marketingGroupRecord.setOrderListId(orderList.getId());
            marketingGroupRecord.setStatus("1");
            marketingGroupRecord.setUserTime(new Date());
            iMarketingGroupRecordService.saveOrUpdate(marketingGroupRecord);
            //规则信息
            MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>());
            //中奖拼团不参与分销
            if(marketingGroupBaseSetting.getDistributionCommission().equals("0")){
                distributionCommission=false;
            }
        }

        if (orderList.getWelfarePayments().doubleValue() > 0) {
            //扣除积分
            iMemberWelfarePaymentsService.subtractWelfarePayments(memberList.getId(),orderList.getWelfarePayments(),"6",orderList.getOrderNo(),"");

            log.info("形成待支付订单扣减福利金：" + orderList.getWelfarePayments() + "--会员福利金：" + memberList.getWelfarePayments() + "--会员：" + memberList.getNickName() + "会员等级折扣优惠价格:" + orderList.getMemberDiscountPriceTotal());
        }

        log.info("待支付订单成功：" + orderList.getActualPayment() + "---" + orderList.getGoodsTotal() + "---" + orderList.getWelfarePayments() + "---" + orderList.getCoupon() + " --- " + orderList.getMemberDiscountPriceTotal());

        //计算分销利润
        //普通订单参与分层
        if (model == 0&&distributionCommission) {
            //分配佣金（冻结）

            //归属店铺查询条件
            LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getSysUserId, memberList.getSysUserId())
                    .in(StoreManage::getPayStatus, "1", "2");
                //获取资金分配比例
                MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>()
                        .eq(MarketingDistributionSetting::getStatus,"1")
                        .orderByDesc(MarketingDistributionSetting::getCreateTime)
                        .last("limit 1"));
            if (marketingDistributionSetting!=null) {
                //启用状态
                if (marketingDistributionSetting.getStatus().equals("1") && orderList.getDistributionCommission().doubleValue() > 0) {
                    if (StringUtils.isNotBlank(memberList.getPromoterType()) && StringUtils.isNotBlank(memberList.getPromoter())) {
                        //以及推广位会员
                        if (memberList.getPromoterType().equals("1")) {
                            //分销会员
                            MemberList firstMemberList = iMemberListService.getById(memberList.getPromoter());
                            /*if (marketingDistributionSetting.getIsThreshold().equals("0") && oConvertUtils.isNotEmpty(firstMemberList)) {*/
                            if (oConvertUtils.isNotEmpty(firstMemberList)) {
                                //无门槛分给普通会员
                                if (firstMemberList.getMemberType().equals("0")) {
                                    //获取普通一级分销比例
                                    BigDecimal commonFirst = marketingDistributionSetting.getCommonFirst();
                                    if (commonFirst.intValue() > 0) {
                                        //会员余额明细
                                        MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                                        memberRechargeRecord.setMemberListId(firstMemberList.getId());
                                        memberRechargeRecord.setPayType("3");//2020年4月17日15:07:39 : 交易类型修改,之前的类型是5(礼包推广奖励) 修改成3(分销奖励)
                                        memberRechargeRecord.setGoAndCome("0");
                                        memberRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(commonFirst.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                        memberRechargeRecord.setTradeStatus("0");
                                        memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        memberRechargeRecord.setOperator("系统");
                                        memberRechargeRecord.setRemark("订单分销奖励 (一) [" + orderList.getOrderNo() + "]");
                                        memberRechargeRecord.setPayment("0");
                                        memberRechargeRecord.setTradeNo(orderList.getOrderNo());
                                        memberRechargeRecord.setTradeType("0");
                                        memberRechargeRecord.setMemberLevel("1");
                                        memberRechargeRecord.setTMemberListId(orderList.getMemberListId());
                                        iMemberRechargeRecordService.save(memberRechargeRecord);
                                        //加入实际分销金额
                                        actualDistribution=actualDistribution.add(memberRechargeRecord.getAmount());
                                        for (Map<String, Object> gm : goodsMap) {
                                            //建立供应商商品快照
                                            List<Map<String, Object>> provideGoodsList = (List<Map<String, Object>>) gm.get("provideGoodsList");
                                            for (Map<String, Object> p : provideGoodsList) {
                                                GoodSpecification goodSpecification = iGoodSpecificationService.getById(p.get("goodSpecificationId").toString());
                                                GoodList goodList = iGoodListService.getById(p.get("goodId").toString());
                                                if (marketingDiscountGoods.contains(goodList.getId())) {
                                                    continue;
                                                }
                                                MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                                                memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                                                memberDistributionRecord.setGoodPicture(goodList.getMainPicture());
                                                memberDistributionRecord.setGoodName(goodList.getGoodName());
                                                memberDistributionRecord.setGoodSpecification(goodSpecification.getSpecification());
                                                memberDistributionRecord.setCommission(((BigDecimal) p.get("price"))
                                                        .multiply((BigDecimal) p.get("quantity"))
                                                        .divide(orderList.getGoodsTotal().subtract(new BigDecimal(marketingDiscountGoods.size())), 2, RoundingMode.DOWN)
                                                        .multiply(memberRechargeRecord.getAmount()));
                                                iMemberDistributionRecordService.save(memberDistributionRecord);

                                            }
                                        }

                                        log.info("形成待支付订单一级分销：" + memberRechargeRecord.getAmount() + "--普通会员冻结金额：" + memberList.getAccountFrozen() + "--会员：" + memberList.getNickName());
                                    }
                                }
                                if (firstMemberList.getMemberType().equals("1")) {
                                    BigDecimal vipFirst = marketingDistributionSetting.getVipFirst();
                                    if (vipFirst.intValue() > 0) {
                                        //会员余额明细
                                        MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                                        memberRechargeRecord.setMemberListId(firstMemberList.getId());
                                        memberRechargeRecord.setPayType("3");//2020年4月17日15:07:39 : 交易类型修改,之前的类型是5(礼包推广奖励) 修改成3(分销奖励)
                                        memberRechargeRecord.setGoAndCome("0");
                                        memberRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(vipFirst.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                        memberRechargeRecord.setTradeStatus("0");
                                        memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        memberRechargeRecord.setOperator("系统");
                                        memberRechargeRecord.setRemark("订单分销奖励 (一) [" + orderList.getOrderNo() + "]");
                                        memberRechargeRecord.setPayment("0");
                                        memberRechargeRecord.setTradeNo(orderList.getOrderNo());
                                        memberRechargeRecord.setTradeType("0");
                                        memberRechargeRecord.setMemberLevel("1");
                                        memberRechargeRecord.setTMemberListId(orderList.getMemberListId());
                                        iMemberRechargeRecordService.save(memberRechargeRecord);
                                        //加入实际分销金额
                                        actualDistribution=actualDistribution.add(memberRechargeRecord.getAmount());
                                        for (Map<String, Object> gm : goodsMap) {
                                            //建立供应商商品快照
                                            List<Map<String, Object>> provideGoodsList = (List<Map<String, Object>>) gm.get("provideGoodsList");
                                            for (Map<String, Object> p : provideGoodsList) {
                                                GoodSpecification goodSpecification = iGoodSpecificationService.getById(p.get("goodSpecificationId").toString());
                                                GoodList goodList = iGoodListService.getById(p.get("goodId").toString());
                                                if (marketingDiscountGoods.contains(goodList.getId())) {
                                                    continue;
                                                }
                                                MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                                                memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                                                memberDistributionRecord.setGoodPicture(goodList.getMainPicture());
                                                memberDistributionRecord.setGoodName(goodList.getGoodName());
                                                memberDistributionRecord.setGoodSpecification(goodSpecification.getSpecification());
                                                memberDistributionRecord.setCommission(((BigDecimal) p.get("price"))
                                                        .multiply((BigDecimal) p.get("quantity"))
                                                        .divide(orderList.getGoodsTotal(), 2, RoundingMode.DOWN)
                                                        .multiply(memberRechargeRecord.getAmount()));
                                                iMemberDistributionRecordService.save(memberDistributionRecord);

                                            }
                                        }
                                        log.info("形成待支付订单一级分销：" + memberRechargeRecord.getAmount() + "--vip会员冻结金额：" + memberList.getAccountFrozen() + "--会员：" + memberList.getNickName());
                                    }
                                }
                                if (StringUtils.isNotBlank(firstMemberList.getPromoterType()) && StringUtils.isNotBlank(firstMemberList.getPromoter())) {
                                    //会员
                                    if (firstMemberList.getPromoterType().equals("1")) {
                                        MemberList secondMemberList = null;
                                        //非二级延伸
                                        if(marketingDistributionSetting.getSecondaryStretching().equals("0")){
                                            secondMemberList=iMemberListService.getById(firstMemberList.getPromoter());
                                        }
                                        //二级延伸，确定是会员
                                        if(marketingDistributionSetting.getSecondaryStretching().equals("1")){
                                            secondMemberList=iMemberListService.getById(firstMemberList.getPromoter());
                                            while (true){
                                                //二级是会员身份就停止
                                                if(secondMemberList.getMemberType().equals("1")){
                                                    break;
                                                }
                                                //为空
                                                if(oConvertUtils.isNotEmpty(secondMemberList)){
                                                    break;
                                                }
                                                //上一级数据不会会员
                                                if(!secondMemberList.getPromoterType().equals("1")){
                                                    secondMemberList=null;
                                                    break;
                                                }
                                                secondMemberList=iMemberListService.getById(secondMemberList.getPromoter());
                                            }
                                        }
                                        /*if (marketingDistributionSetting.getIsThreshold().equals("0") && oConvertUtils.isNotEmpty(secondMemberList)) {*/
                                        if (oConvertUtils.isNotEmpty(secondMemberList)) {
                                            //无门槛分给普通会员
                                            if (secondMemberList.getMemberType().equals("0")) {
                                                BigDecimal secondFirst = marketingDistributionSetting.getCommonSecond();
                                                if (secondFirst.intValue() > 0) {
                                                    //会员余额明细
                                                    MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                                                    memberRechargeRecord.setMemberListId(secondMemberList.getId());
                                                    memberRechargeRecord.setPayType("3");//2020年4月17日15:07:39 : 交易类型修改,之前的类型是5(礼包推广奖励) 修改成3(分销奖励)
                                                    memberRechargeRecord.setGoAndCome("0");
                                                    memberRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(secondFirst.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                                    memberRechargeRecord.setTradeStatus("0");
                                                    memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                    memberRechargeRecord.setOperator("系统");
                                                    memberRechargeRecord.setRemark("订单分销奖励 (二) [" + orderList.getOrderNo() + "]");
                                                    memberRechargeRecord.setPayment("0");
                                                    memberRechargeRecord.setTradeNo(orderList.getOrderNo());
                                                    memberRechargeRecord.setTradeType("0");
                                                    memberRechargeRecord.setMemberLevel("2");
                                                    memberRechargeRecord.setTMemberListId(orderList.getMemberListId());
                                                    iMemberRechargeRecordService.save(memberRechargeRecord);
                                                    //加入实际分销金额
                                                    actualDistribution=actualDistribution.add(memberRechargeRecord.getAmount());
                                                    for (Map<String, Object> gm : goodsMap) {
                                                        //建立供应商商品快照
                                                        List<Map<String, Object>> provideGoodsList = (List<Map<String, Object>>) gm.get("provideGoodsList");
                                                        for (Map<String, Object> p : provideGoodsList) {
                                                            GoodSpecification goodSpecification = iGoodSpecificationService.getById(p.get("goodSpecificationId").toString());
                                                            GoodList goodList = iGoodListService.getById(p.get("goodId").toString());
                                                            if (marketingDiscountGoods.contains(goodList.getId())) {
                                                                continue;
                                                            }
                                                            MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                                                            memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                                                            memberDistributionRecord.setGoodPicture(goodList.getMainPicture());
                                                            memberDistributionRecord.setGoodName(goodList.getGoodName());
                                                            memberDistributionRecord.setGoodSpecification(goodSpecification.getSpecification());
                                                            memberDistributionRecord.setCommission(((BigDecimal) p.get("price")).multiply((BigDecimal) p.get("quantity"))
                                                                    .divide(orderList.getGoodsTotal(), 2,RoundingMode.DOWN)
                                                                    .multiply(memberRechargeRecord.getAmount()));
                                                            iMemberDistributionRecordService.save(memberDistributionRecord);


                                                        }
                                                    }

                                                    log.info("形成待支付订单二级分销：" + memberRechargeRecord.getAmount() + "--普通会员冻结金额：" + memberList.getAccountFrozen() + "--会员：" + memberList.getNickName());
                                                }
                                            }
                                            if (secondMemberList.getMemberType().equals("1")) {
                                                BigDecimal vipSecond = marketingDistributionSetting.getVipSecond();
                                                if (vipSecond.intValue() > 0) {
                                                    //会员余额明细
                                                    MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord();
                                                    memberRechargeRecord.setMemberListId(secondMemberList.getId());
                                                    memberRechargeRecord.setPayType("3");//2020年4月17日15:07:39 : 交易类型修改,之前的类型是5(礼包推广奖励) 修改成3(分销奖励)
                                                    memberRechargeRecord.setGoAndCome("0");
                                                    memberRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(vipSecond.divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                                    memberRechargeRecord.setTradeStatus("0");
                                                    memberRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                    memberRechargeRecord.setOperator("系统");
                                                    memberRechargeRecord.setRemark("订单分销奖励 (二) [" + orderList.getOrderNo() + "]");
                                                    memberRechargeRecord.setPayment("0");
                                                    memberRechargeRecord.setTradeNo(orderList.getOrderNo());
                                                    memberRechargeRecord.setTradeType("0");
                                                    memberRechargeRecord.setMemberLevel("2");
                                                    memberRechargeRecord.setTMemberListId(orderList.getMemberListId());
                                                    iMemberRechargeRecordService.save(memberRechargeRecord);
                                                    //加入实际分销金额
                                                    actualDistribution=actualDistribution.add(memberRechargeRecord.getAmount());
                                                    for (Map<String, Object> gm : goodsMap) {
                                                        //建立供应商商品快照
                                                        List<Map<String, Object>> provideGoodsList = (List<Map<String, Object>>) gm.get("provideGoodsList");
                                                        for (Map<String, Object> p : provideGoodsList) {
                                                            GoodSpecification goodSpecification = iGoodSpecificationService.getById(p.get("goodSpecificationId").toString());
                                                            GoodList goodList = iGoodListService.getById(p.get("goodId").toString());
                                                            if (marketingDiscountGoods.contains(goodList.getId())) {
                                                                continue;
                                                            }
                                                            MemberDistributionRecord memberDistributionRecord = new MemberDistributionRecord();
                                                            memberDistributionRecord.setMemberRechargeRecordId(memberRechargeRecord.getId());
                                                            memberDistributionRecord.setGoodPicture(goodList.getMainPicture());
                                                            memberDistributionRecord.setGoodName(goodList.getGoodName());
                                                            memberDistributionRecord.setGoodSpecification(goodSpecification.getSpecification());
                                                            memberDistributionRecord.setCommission(((BigDecimal) p.get("price")).multiply((BigDecimal) p.get("quantity"))
                                                                    .divide(orderList.getGoodsTotal(), 2, RoundingMode.DOWN)
                                                                    .multiply(memberRechargeRecord.getAmount()));
                                                            iMemberDistributionRecordService.save(memberDistributionRecord);

                                                        }
                                                    }

                                                    log.info("形成待支付订单二级分销：" + memberRechargeRecord.getAmount() + "--vip会员冻结金额：" + memberList.getAccountFrozen() + "--会员：" + memberList.getNickName());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //归属店铺
                    if (StringUtils.isNotBlank(memberList.getSysUserId()) && orderList.getDistributionCommission().doubleValue() > 0 && marketingDistributionSetting.getAffiliationStoreAward().doubleValue() > 0) {
                        if (iStoreManageService.count(storeManageLambdaQueryWrapper) > 0) {
                            StoreManage storeManage = iStoreManageService.list(storeManageLambdaQueryWrapper).get(0);
                            //店铺余额明细
                            StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                            storeRechargeRecord.setStoreManageId(storeManage.getId());
                            storeRechargeRecord.setPayType("5");
                            storeRechargeRecord.setGoAndCome("0");
                            storeRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(marketingDistributionSetting.getAffiliationStoreAward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            storeRechargeRecord.setTradeStatus("0");
                            storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                            storeRechargeRecord.setOperator("系统");
                            storeRechargeRecord.setRemark("订单归属店铺奖励 [" + orderList.getOrderNo() + "]");
                            storeRechargeRecord.setPayment("0");
                            storeRechargeRecord.setTradeType("0");
                            storeRechargeRecord.setTradeNo(orderList.getOrderNo());
                            iStoreRechargeRecordService.save(storeRechargeRecord);
                            //加入实际分销金额
                            actualDistribution=actualDistribution.add(storeRechargeRecord.getAmount());

                            log.info("形成待支付订单归属店铺：" + storeRechargeRecord.getAmount() + "--店铺冻结金额：" + storeManage.getAccountFrozen() + "--店铺：" + storeManage.getStoreName());
                        }
                    }

                    //渠道店铺
                    if (StringUtils.isNotBlank(orderList.getDistributionChannel()) && orderList.getDistributionCommission().doubleValue() > 0 && marketingDistributionSetting.getDistributionChannelAward().doubleValue() > 0) {
                        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                        storeManageQueryWrapper.eq("sys_user_id", orderList.getDistributionChannel());
                        storeManageQueryWrapper.in("pay_status", "1", "2");
                        if (iStoreManageService.count(storeManageQueryWrapper) > 0) {
                            StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                            //店铺余额明细
                            StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
                            storeRechargeRecord.setStoreManageId(storeManage.getId());
                            storeRechargeRecord.setPayType("6");
                            storeRechargeRecord.setGoAndCome("0");
                            storeRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(marketingDistributionSetting.getDistributionChannelAward().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            storeRechargeRecord.setTradeStatus("0");
                            storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                            storeRechargeRecord.setOperator("系统");
                            storeRechargeRecord.setRemark("订单渠道销售奖励 [" + orderList.getOrderNo() + "]");
                            storeRechargeRecord.setPayment("0");
                            storeRechargeRecord.setTradeType("0");
                            storeRechargeRecord.setTradeNo(orderList.getOrderNo());
                            iStoreRechargeRecordService.save(storeRechargeRecord);
                            //加入实际分销金额
                            actualDistribution=actualDistribution.add(storeRechargeRecord.getAmount());

                            log.info("形成待支付订单渠道店铺：" + storeRechargeRecord.getAmount() + "--店铺冻结金额：" + storeManage.getAccountFrozen() + "--店铺：" + storeManage.getStoreName());
                        }
                    }

                }
            }

            //加盟商资金设置

            boolean isExecute = true;

            //加盟商
            StoreManage storeManage = iStoreManageService.getOne(storeManageLambdaQueryWrapper);
            if (oConvertUtils.isNotEmpty(storeManage)){
                if (storeManage.getPromoterType().equals("3") || StringUtils.isNotBlank(storeManage.getAllianceUserId())) {
                    //处理加盟商业务
                    AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                            .eq(AllianceManage::getDelFlag,"0")
                            .eq(AllianceManage::getStatus,"1")
                            .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                    if (oConvertUtils.isNotEmpty(allianceManage)){
                        //独享控制
                        if (allianceManage.getProfitType().equals("0")) {
                            isExecute = false;
                        }
                        if (oConvertUtils.isNotEmpty(allianceManage) && allianceManage.getOrderCommissionRate().doubleValue() > 0 && allianceManage.getStatus().equals("1")&&orderList.getDistributionCommission().doubleValue()>0) {
                            //代理余额明细
                            AllianceRechargeRecord allianceRechargeRecord = new AllianceRechargeRecord();
                            allianceRechargeRecord.setSysUserId(allianceManage.getSysUserId());
                            allianceRechargeRecord.setPayType("0");
                            allianceRechargeRecord.setGoAndCome("0");
                            if (allianceManage.getProfitType().equals("0")){
                                allianceRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(allianceManage.getOrderCommissionRate().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }else {
                                allianceRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(allianceManage.getOrderCommissionRate().multiply(allianceManage.getFranchiseeRatio().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }
                            allianceRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                            allianceRechargeRecord.setTradeStatus("0");
                            allianceRechargeRecord.setPayment("0");
                            allianceRechargeRecord.setTradeNo(orderList.getOrderNo());
                            allianceRechargeRecord.setTradeType("0");
                            allianceRechargeRecord.setRemark("订单交易 [" + orderList.getOrderNo() + "]");
                            iAllianceRechargeRecordService.save(allianceRechargeRecord);
                            //加入实际分销金额
                            actualDistribution=actualDistribution.add(allianceRechargeRecord.getAmount());
                            log.info("形成待支付订单加盟商分配：" + allianceRechargeRecord.getAmount() + "--加盟商冻结金额：" + allianceManage.getAccountFrozen() + "--加盟商：" + allianceManage.getId());
                        }
                    }
                }
            }

            if (isExecute) {

                List<String> sysAreas = Lists.newArrayList();
                //判断经纬度大于0
                if (orderList.getLatitude().doubleValue() > 0 && orderList.getLongitude().doubleValue() > 0) {
                    //获取腾讯地图id
                    String adCode = tengxunMapUtils.findAdcode(orderList.getLatitude().toString() + "," + orderList.getLongitude().toString());
                    if (StringUtils.isNotBlank(adCode)) {
                        //通过腾讯地图id查出对应的地区
                        QueryWrapper<SysArea> sysAreaQueryWrapper = new QueryWrapper<>();
                        sysAreaQueryWrapper.eq("teng_xun_id", adCode);
                        SysArea sysArea = iSysAreaService.getOne(sysAreaQueryWrapper);
                        int level = sysArea.getLeve() - 1;
                        sysAreas.add(sysArea.getId().toString());
                        while (level >= 0) {
                            sysArea = iSysAreaService.getById(sysArea.getParentId());
                            sysAreas.add(sysArea.getId().toString());
                            level--;
                        }
                        sysAreas.forEach(sid -> {
                            SysArea sysArea1 = iSysAreaService.getById(sid);
                            if(sysArea1.getLeve()==2&&oConvertUtils.isNotEmpty(storeManage)){
                                AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                                        .eq(AllianceManage::getDelFlag,"0")
                                        .eq(AllianceManage::getStatus,"1")
                                        .eq(AllianceManage::getSysUserId, storeManage.getAllianceUserId()));
                                if (oConvertUtils.isNotEmpty(allianceManage)){
                                    if (allianceManage.getMutualAdvantages().equals("0")){
                                        if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                            AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                            if (agencyManage != null && agencyManage.getOrderCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")&&orderList.getDistributionCommission().doubleValue()>0) {
                                                //代理余额明细
                                                AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                agencyRechargeRecord.setPayType("0");
                                                agencyRechargeRecord.setGoAndCome("0");
                                                agencyRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(allianceManage.getOrderCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                                agencyRechargeRecord.setTradeStatus("0");
                                                agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                agencyRechargeRecord.setPayment("0");
                                                agencyRechargeRecord.setTradeType("0");
                                                agencyRechargeRecord.setTradeNo(orderList.getOrderNo());
                                                agencyRechargeRecord.setRemark("订单交易 [" + orderList.getOrderNo() + "]");
                                                iAgencyRechargeRecordService.save(agencyRechargeRecord);


                                                log.info("形成待支付订单代理分配：" + agencyRechargeRecord.getAmount() + "--代理冻结金额：" + agencyManage.getAccountFrozen() + "--代理：" + agencyManage.getId());
                                            }
                                        }
                                    }else {
                                        if (StringUtils.isNotBlank(allianceManage.getCountyId())){
                                            SysArea area = iSysAreaService.getById(allianceManage.getCountyId());
                                            if (StringUtils.isNotBlank(area.getAgencyManageId())){
                                                AgencyManage agencyManage = iAgencyManageService.getById(area.getAgencyManageId());
                                                if (agencyManage != null && agencyManage.getOrderCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")&&orderList.getDistributionCommission().doubleValue()>0) {
                                                    //代理余额明细
                                                    AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                                    agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                                    agencyRechargeRecord.setPayType("0");
                                                    agencyRechargeRecord.setGoAndCome("0");
                                                    agencyRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(allianceManage.getOrderCommissionRate().multiply(allianceManage.getAgencyRatio().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                                    agencyRechargeRecord.setTradeStatus("0");
                                                    agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                                    agencyRechargeRecord.setPayment("0");
                                                    agencyRechargeRecord.setTradeType("0");
                                                    agencyRechargeRecord.setTradeNo(orderList.getOrderNo());
                                                    agencyRechargeRecord.setRemark("订单交易 [" + orderList.getOrderNo() + "]");
                                                    iAgencyRechargeRecordService.save(agencyRechargeRecord);

                                                    log.info("形成待支付订单代理分配：" + agencyRechargeRecord.getAmount() + "--代理冻结金额：" + agencyManage.getAccountFrozen() + "--代理：" + agencyManage.getId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }else {
                                if (StringUtils.isNotBlank(sysArea1.getAgencyManageId())) {
                                    AgencyManage agencyManage = iAgencyManageService.getById(sysArea1.getAgencyManageId());
                                    if (agencyManage != null && agencyManage.getOrderCommissionRate().doubleValue() > 0 && agencyManage.getStatus().equals("1")&&orderList.getDistributionCommission().doubleValue()>0) {
                                        //代理余额明细
                                        AgencyRechargeRecord agencyRechargeRecord = new AgencyRechargeRecord();
                                        agencyRechargeRecord.setSysUserId(agencyManage.getSysUserId());
                                        agencyRechargeRecord.setPayType("0");
                                        agencyRechargeRecord.setGoAndCome("0");
                                        agencyRechargeRecord.setAmount(orderList.getDistributionCommission().multiply(agencyManage.getOrderCommissionRate().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                                        agencyRechargeRecord.setTradeStatus("0");
                                        agencyRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());
                                        agencyRechargeRecord.setPayment("0");
                                        agencyRechargeRecord.setTradeType("0");
                                        agencyRechargeRecord.setTradeNo(orderList.getOrderNo());
                                        agencyRechargeRecord.setRemark("订单交易 [" + orderList.getOrderNo() + "]");
                                        iAgencyRechargeRecordService.save(agencyRechargeRecord);


                                        log.info("形成待支付订单代理分配：" + agencyRechargeRecord.getAmount() + "--代理冻结金额：" + agencyManage.getAccountFrozen() + "--代理：" + agencyManage.getId());
                                    }
                                }
                            }

                        });
                    }
                }
            }
        }
        //实际分销支出
        orderList.setActualDistribution(actualDistribution);
        //净利润
        orderList.setRetainedProfits(orderList.getProfit().subtract(orderList.getActualDistribution()));
        this.saveOrUpdate(orderList);
        return orderList;
    }

    @Override
    @Transactional
    public Boolean paySuccessOrder(String id, int model, PayOrderCarLog payOrderCarLog) {
        //修改订单成功状态信息
        OrderList orderList = this.getById(id);
        if(orderList.getStatus().equals("1")){
            return true;
        }

        orderList.setStatus("1");
        orderList.setModePayment(payOrderCarLog.getPayModel());//支付方式的设定
        log.info("支付成功后日志内容："+JSON.toJSONString(payOrderCarLog));
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        if(payOrderCarLog.getPayPrice().doubleValue()!=0) {
            orderList.setPayPrice(payOrderCarLog.getPayPrice());
        }
        if(payOrderCarLog.getBalance().doubleValue()!=0) {
            orderList.setBalance(payOrderCarLog.getBalance());
        }
        if(payOrderCarLog.getWelfarePayments().doubleValue()!=0) {
            orderList.setPayWelfarePayments(payOrderCarLog.getWelfarePayments());
            orderList.setPayWelfarePaymentsPrice(orderList.getPayWelfarePayments().multiply(integralValue));
        }
        orderList.setRetainedProfits(orderList.getRetainedProfits().subtract(orderList.getWelfarePaymentsPrice()).subtract(orderList.getPayWelfarePaymentsPrice()));
        orderList.setPayTime(new Date());
        //设置交易流水号
        orderList.setSerialNumber(payOrderCarLog.getId());
        //汇付天下的交易流水号
        orderList.setHftxSerialNumber(payOrderCarLog.getSerialNumber());
        this.saveOrUpdate(orderList);

        //加盟专区分配
        if(orderList.getOrderType().equals("7")){
            iMarketingLeagueBuyerRecordService.allocation(orderList);
        }
        //修改子订单状态
        OrderProviderList orderProviderList = new OrderProviderList();
        orderProviderList.setStatus("1");
        UpdateWrapper<OrderProviderList> orderProviderListUpdateWrapper = new UpdateWrapper<>();
        orderProviderListUpdateWrapper.eq("order_list_id", orderList.getId());

        iOrderProviderListService.update(orderProviderList, orderProviderListUpdateWrapper);
        //查出供应商订单(2020年5月21日00:11:10)
        List<OrderProviderList> orderProviderLists = iOrderProviderListService.list(new LambdaQueryWrapper<OrderProviderList>()
                .eq(OrderProviderList::getOrderListId, orderList.getId()));
        //遍历供应商订单改变状态为订单已支付
        orderProviderLists.forEach(opls -> {
            iOrderProviderListService.saveOrUpdate(opls.setStatus("1"));
            if (opls.getParentId().equals("0")) {
                ProviderManage providerManage = iProviderManageService.getOne(new LambdaQueryWrapper<ProviderManage>()
                        .eq(ProviderManage::getSysUserId, opls.getSysUserId()));
                //写入供应商冻结金额
                iProviderManageService.saveOrUpdate(providerManage
                        .setAccountFrozen(providerManage.getAccountFrozen().add(opls.getActualPayment())));
                iProviderRechargeRecordService.update(new ProviderRechargeRecord().setTradeStatus("2"), new LambdaUpdateWrapper<ProviderRechargeRecord>()
                        .eq(ProviderRechargeRecord::getTradeNo, opls.getOrderNo())
                        .eq(ProviderRechargeRecord::getGoAndCome, "0")
                        .eq(ProviderRechargeRecord::getTradeType, "0")
                        .eq(ProviderRechargeRecord::getTradeStatus, "0")
                        .eq(ProviderRechargeRecord::getPayType, "0")
                        .eq(ProviderRechargeRecord::getSysUserId, providerManage.getSysUserId())
                );
            }
        });

        //判断该订单赠送福利金是否大于0
        if (orderList.getGiveWelfarePayments().doubleValue() > 0) {
            //获取购买订单的会员
            MemberList memberList = iMemberListService.getById(orderList.getMemberListId());

            //写入会员冻结福利金
            iMemberListService.saveOrUpdate(memberList
                    .setWelfarePaymentsFrozen(memberList.getWelfarePaymentsFrozen().add(orderList.getGiveWelfarePayments())));
            //生成冻结福利金记录
            iMemberWelfarePaymentsService.save(new MemberWelfarePayments()
                    .setMemberListId(orderList.getMemberListId())
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setBargainPayments(orderList.getGiveWelfarePayments())
                    .setWelfarePayments(memberList.getWelfarePaymentsFrozen())
                    .setWeType("1")
                    .setGoAndCome("平台")
                    .setWpExplain("订单交易赠送福利金[" + orderList.getOrderNo() + "]")
                    .setBargainTime(new Date())
                    .setOperator("系统")
                    .setIsPlatform("1")
                    .setIsFreeze("1")
                    .setTradeNo(orderList.getOrderNo())
                    .setTradeType("0")
                    .setTradeStatus("2")
            );
        }

        if (model == 0) {
            // 分销余额记录条件构造器
            LambdaQueryWrapper<MemberRechargeRecord> memberRechargeRecordLambdaQueryWrapper = new LambdaQueryWrapper<MemberRechargeRecord>()
                    .eq(MemberRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(MemberRechargeRecord::getTradeType, "0")
                    .eq(MemberRechargeRecord::getTradeStatus, "0")
                    .eq(MemberRechargeRecord::getPayType, "3");//2020年4月17日15:07:39 : 交易类型查询条件修改 之前是5(礼包推广奖励) 修改成3(分销奖励)
            if (iMemberRechargeRecordService.count(memberRechargeRecordLambdaQueryWrapper) > 0) {
                // 分销余额记录修改
                List<MemberRechargeRecord> memberRechargeRecords = iMemberRechargeRecordService.list(memberRechargeRecordLambdaQueryWrapper);
                memberRechargeRecords.forEach(mrrs -> {
                    //状态改成已支付
                    iMemberRechargeRecordService.saveOrUpdate(mrrs.setTradeStatus("2"));
                    MemberList memberList = iMemberListService.getById(mrrs.getMemberListId());
                    //分销佣金
                    iMemberListService.saveOrUpdate(memberList
                            .setAccountFrozen(memberList.getAccountFrozen().add(mrrs.getAmount()))
                            .setTotalCommission(memberList.getTotalCommission().add(mrrs.getAmount()))
                    );
                });
            }
            //店铺余额记录条件构造器
            LambdaQueryWrapper<StoreRechargeRecord> storeRechargeRecordLambdaQueryWrapper = new LambdaQueryWrapper<StoreRechargeRecord>()
                    .eq(StoreRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(StoreRechargeRecord::getTradeStatus, "0")
                    .eq(StoreRechargeRecord::getTradeType, "0")
                    .in(StoreRechargeRecord::getPayType, "5", "6");
            if (iStoreRechargeRecordService.count(storeRechargeRecordLambdaQueryWrapper) > 0) {
                List<StoreRechargeRecord> storeRechargeRecords = iStoreRechargeRecordService.list(storeRechargeRecordLambdaQueryWrapper);
                storeRechargeRecords.forEach(srrs -> {
                    //店铺余额记录修改
                    iStoreRechargeRecordService.saveOrUpdate(srrs.setTradeStatus("2"));
                    StoreManage storeManage = iStoreManageService.getById(srrs.getStoreManageId());
                    //分配店铺佣金
                    iStoreManageService.saveOrUpdate(storeManage
                            .setAccountFrozen(storeManage.getAccountFrozen().add(srrs.getAmount())));
                });
            }
            //加盟商余额记录条件构造器
            LambdaQueryWrapper<AllianceRechargeRecord> allianceRechargeRecordLambdaQueryWrapper = new LambdaQueryWrapper<AllianceRechargeRecord>()
                    .eq(AllianceRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(AllianceRechargeRecord::getPayType, "0")
                    .eq(AllianceRechargeRecord::getGoAndCome, "0")
                    .eq(AllianceRechargeRecord::getTradeStatus, "0")
                    .eq(AllianceRechargeRecord::getTradeType, "0")
                    .eq(AllianceRechargeRecord::getPayment, "0");

            if (iAllianceRechargeRecordService.count(allianceRechargeRecordLambdaQueryWrapper) > 0) {
                //查出加盟商
                List<AllianceRechargeRecord> allianceRechargeRecords = iAllianceRechargeRecordService.list(allianceRechargeRecordLambdaQueryWrapper);
                allianceRechargeRecords.forEach(arrs -> {
                    //修改加盟商余额明细状态
                    iAllianceRechargeRecordService.saveOrUpdate(arrs.setTradeStatus("2"));

                    AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaQueryWrapper<AllianceManage>()
                            .eq(AllianceManage::getSysUserId, arrs.getSysUserId()));
                    //划入加盟商冻结金额
                    iAllianceManageService.saveOrUpdate(allianceManage
                            .setAccountFrozen(allianceManage.getAccountFrozen().add(arrs.getAmount())));
                });
            }
            //代理余额记录条件构造器
            LambdaQueryWrapper<AgencyRechargeRecord> agencyRechargeRecordLambdaQueryWrapper = new LambdaQueryWrapper<AgencyRechargeRecord>()
                    .eq(AgencyRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(AgencyRechargeRecord::getTradeType, "0")
                    .eq(AgencyRechargeRecord::getTradeStatus, "0")
                    .eq(AgencyRechargeRecord::getPayType, "0");
            if (iAgencyRechargeRecordService.count(agencyRechargeRecordLambdaQueryWrapper) > 0) {
                //查出代理奖励
                List<AgencyRechargeRecord> agencyRechargeRecords = iAgencyRechargeRecordService.list(agencyRechargeRecordLambdaQueryWrapper);
                agencyRechargeRecords.forEach(arrs -> {
                    //代理余额记录
                    iAgencyRechargeRecordService.saveOrUpdate(arrs.setTradeStatus("2"));
                    AgencyManage agencyManage = iAgencyManageService.getOne(new LambdaQueryWrapper<AgencyManage>()
                            .eq(AgencyManage::getSysUserId, arrs.getSysUserId()));
                    //分配代理奖励
                    iAgencyManageService.saveOrUpdate(agencyManage
                            .setAccountFrozen(agencyManage.getAccountFrozen().add(arrs.getAmount())));
                });
            }


        }
        //免单专区订单
        if(model==1){
            MarketingFreeOrder marketingFreeOrder=iMarketingFreeOrderService.getOne(new LambdaQueryWrapper<MarketingFreeOrder>()
                    .eq(MarketingFreeOrder::getOrderListId,orderList.getId()));
            marketingFreeOrder.setPayTime(orderList.getPayTime());
            marketingFreeOrder.setPayType("1");
            iMarketingFreeOrderService.saveOrUpdate(marketingFreeOrder);
        }

        //生成平台利润和平台资金
        iSysBlanceService.add(orderList.getRetainedProfits(),"0",orderList.getOrderNo());
        return true;
    }

    @Override
    public IPage<Map<String, Object>> getOrderListByMemberIdAndStatus(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return orderListMapper.getOrderListByMemberIdAndStatus(page, paramMap);
    }

    @Override
    public OrderListDTO orderListDtoOne(String id) {
        List<OrderListDTO> orderListDTOList = orderListMapper.orderListDtoOne(id);
        if (orderListDTOList.size() > 0) {
            OrderListDTO orderListDTO = orderListDTOList.get(0);
            return orderListDTO;
        }
        return null;
    }

    @Override
    @Transactional
    public void abrogateOrder(String id, String closeExplain, String closeType) {
        OrderList orderList = this.getById(id);
        //买家关闭
        orderList.setCloseType(closeType);
        orderList.setCloseExplain(closeExplain);
        orderList.setCloseTime(new Date());
        //交易关闭
        orderList.setStatus("4");
        this.saveOrUpdate(orderList);
        //如果是中奖拼团在代付款的时候取消归还中奖拼团类型
        if(orderList.getStatus().equals("0")&&orderList.getOrderType().equals("4")){
            MarketingGroupRecord marketingGroupRecord=iMarketingGroupRecordService.getById(orderList.getActiveId());
            marketingGroupRecord.setOrderListId(null);
            marketingGroupRecord.setStatus("0");
            marketingGroupRecord.setUserTime(null);
            iMarketingGroupRecordService.saveOrUpdate(marketingGroupRecord);
        }
        //退回积分
        iMemberWelfarePaymentsService.addWelfarePayments(orderList.getMemberListId(),orderList.getWelfarePayments(),"17",orderList.getOrderNo(),"");


        /*退回优惠券*/
        iMarketingDiscountCouponService.sendBackOrderMarketingDiscountCoupon(orderList);

        QueryWrapper<OrderProviderList> orderProviderListQueryWrapper = new QueryWrapper<>();
        orderProviderListQueryWrapper.eq("order_list_id", id);
        List<OrderProviderList> orderProviderLists = iOrderProviderListService.list(orderProviderListQueryWrapper);
        orderProviderLists.forEach(ops -> {
            ops.setStatus("4");
            iOrderProviderListService.saveOrUpdate(ops);
            //退回库存
            QueryWrapper<OrderProviderGoodRecord> orderProviderGoodRecordQueryWrapper = new QueryWrapper<>();
            orderProviderGoodRecordQueryWrapper.eq("order_provider_list_id", ops.getId());
            List<OrderProviderGoodRecord> orderProviderGoodRecords = iOrderProviderGoodRecordService.list(orderProviderGoodRecordQueryWrapper);
            orderProviderGoodRecords.forEach(opgr -> {
                String goodListId = opgr.getGoodListId();
                String goodSpecificationId = opgr.getGoodSpecificationId();
                GoodSpecification goodSpecification = iGoodSpecificationService.getById(goodSpecificationId);
                if (goodSpecification != null) {
                    goodSpecification.setRepertory(goodSpecification.getRepertory().add(opgr.getAmount()));
                    iGoodSpecificationService.saveOrUpdate(goodSpecification);
                }
            });
            if (ops.getParentId().equals("0")) {


                //供应商资金条件
                LambdaQueryWrapper<ProviderRechargeRecord> lambdaQueryWrapper = new LambdaQueryWrapper<ProviderRechargeRecord>()
                        .eq(ProviderRechargeRecord::getSysUserId, ops.getSysUserId())
                        .eq(ProviderRechargeRecord::getTradeNo, ops.getOrderNo())
                        .eq(ProviderRechargeRecord::getGoAndCome, "0")
                        .eq(ProviderRechargeRecord::getTradeType, "0")
                        .eq(ProviderRechargeRecord::getTradeStatus, "0")
                        .eq(ProviderRechargeRecord::getPayType, "0");

                //查询供应商资金记录
                long providerCount = iProviderRechargeRecordService.count(lambdaQueryWrapper);
                //判断是否有供应商资金记录
                if (providerCount > 0) {
                    //查出供应商资金记录
                    ProviderRechargeRecord providerRechargeRecord = iProviderRechargeRecordService.list(lambdaQueryWrapper).get(0);
                    //修改供应商资金记录状态
                    iProviderRechargeRecordService.saveOrUpdate(providerRechargeRecord
                            .setTradeStatus("7"));
                }
            }
        });

        if (orderList.getOrderType().equals("0")) {

            //会员金额取消
            iMemberRechargeRecordService.update(new MemberRechargeRecord().setTradeStatus("7"), new LambdaUpdateWrapper<MemberRechargeRecord>()
                    .eq(MemberRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(MemberRechargeRecord::getTradeType, "0")
                    .eq(MemberRechargeRecord::getTradeStatus, "0")
                    .eq(MemberRechargeRecord::getPayType, "3")
            );

            //店铺余额记录修改
            iStoreRechargeRecordService.update(new StoreRechargeRecord().setTradeStatus("7"), new LambdaUpdateWrapper<StoreRechargeRecord>()
                    .eq(StoreRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(StoreRechargeRecord::getTradeType, "0")
                    .eq(StoreRechargeRecord::getTradeStatus, "0")
                    .in(StoreRechargeRecord::getPayType, "5", "6")
            );
            //加盟商余额记录修改
            iAllianceRechargeRecordService.update(new AllianceRechargeRecord().setTradeStatus("7"), new LambdaUpdateWrapper<AllianceRechargeRecord>()
                    .eq(AllianceRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(AllianceRechargeRecord::getPayType, "0")
                    .eq(AllianceRechargeRecord::getGoAndCome, "0")
                    .eq(AllianceRechargeRecord::getTradeStatus, "0")
                    .eq(AllianceRechargeRecord::getTradeType, "0")
                    .eq(AllianceRechargeRecord::getPayment, "0"));
            //代理余额记录修改
            iAgencyRechargeRecordService.update(new AgencyRechargeRecord().setTradeStatus("7"), new LambdaUpdateWrapper<AgencyRechargeRecord>()
                    .eq(AgencyRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(AgencyRechargeRecord::getPayType, "0")
                    .eq(AgencyRechargeRecord::getTradeStatus, "0")
                    .eq(AgencyRechargeRecord::getTradeType, "0")
            );

        }
    }

    @Override
    @Transactional
    public Result<?> refundAndAbrogateOrder(String id, String closeExplain, String closeType) {
        OrderList orderList=this.getById(id);
        if(orderList.getStatus().equals("0")||orderList.getStatus().equals("4")){
            return Result.error("订单状态不正确");
        }
        if (orderList.getPayPrice().doubleValue() > 0.0D) {
            Map balanceMap;
            try {
                balanceMap = this.hftxPayUtils.getSettleAccountBalance();
                if (!balanceMap.get("status").equals("succeeded")) {
                    return Result.error("汇付账户的余额查询出错");
                }

                if (Double.parseDouble(balanceMap.get("avl_balance").toString()) < orderList.getPayPrice().doubleValue()) {
                    Object var10000 = balanceMap.get("avl_balance");
                    return Result.error("汇付账户的余额：" + var10000 + "；需退金额：" + orderList.getPayPrice());
                }
            } catch (BaseAdaPayException var6) {
                var6.printStackTrace();
            }

            balanceMap = this.payUtils.refund(orderList.getPayPrice(), orderList.getSerialNumber(), orderList.getHftxSerialNumber());
            if (balanceMap.get("status").equals("failed")) {
                return Result.error("现金退款失败");
            }

            orderList.setRefundJson(JSON.toJSONString(balanceMap));
        }
        this.saveOrUpdate(orderList);
        //退回余额
        iMemberListService.addBlance(orderList.getMemberListId(),orderList.getBalance(),orderList.getOrderNo(),"2");
        //退回交易积分
        iMemberWelfarePaymentsService.addWelfarePayments(orderList.getMemberListId(),orderList.getPayWelfarePayments(),"20",orderList.getOrderNo(),"");
        //取消订单
        this.abrogateOrder(id,closeExplain,closeType);

        return Result.ok("退款成功");
    }

    @Override
    @Transactional
    public void affirmOrder(String id) {
        this.updateById(new OrderList().setId(id).setStatus("3").setDeliveryTime(new Date()));
        iOrderProviderListService.update(new OrderProviderList().setStatus("3"),new LambdaQueryWrapper<OrderProviderList>().eq(OrderProviderList::getOrderListId,id));
    }

    /**
     * //是否全部发货,修改orderList的状态内容
     *
     * @param orderProviderList
     * @return
     */
    @Override
    public void ShipmentOrderModification(OrderProviderList orderProviderList) {
        if (orderProviderList != null) {
            List<OrderProviderGoodRecord> listOrderProviderGoodRecord;
            Boolean bl = false;
            OrderList orderList = baseMapper.selectById(orderProviderList.getOrderListId());
            QueryWrapper<OrderProviderList> queryWrapper = new QueryWrapper<OrderProviderList>();
            QueryWrapper<OrderProviderGoodRecord> queryWrapperOPGR = new QueryWrapper<OrderProviderGoodRecord>();
            if (orderList != null) {
                queryWrapper.eq("order_list_id", orderList.getId());
                //queryWrapper.ne("parent_id","0");
                //判断是否有发货
                List<OrderProviderList> listOrderProviderList = orderProviderListService.list(queryWrapper);
                if (listOrderProviderList.size() > 0) {
                    for (OrderProviderList oplfh : listOrderProviderList) {

                        if (!"0".equals(oplfh.getParentId())) {
                            //部分发货
                            orderList.setIsSender("1");
                        } else {
                            //是否全部发货
                            queryWrapperOPGR = new QueryWrapper<OrderProviderGoodRecord>();
                            queryWrapperOPGR.eq("order_provider_list_id", oplfh.getId());
                            listOrderProviderGoodRecord = orderProviderGoodRecordService.list(queryWrapperOPGR);
                            if (listOrderProviderGoodRecord.size() > 0) {
                                //说明还没为发货商品
                                bl = true;
                            }
                        }
                    }
                    //bl = false 为已全部发货，改变状态为待收货
                    if (!bl) {
                        orderList.setStatus("2");
                    }
                    if (orderList.getShipmentsTime() == null || "".equals(orderList.getShipmentsTime())) {
                        //第一次发货时间
                        orderList.setShipmentsTime(new Date());
                    }
                    //子订单数量
                    orderList.setChildOrder(new BigDecimal(listOrderProviderList.size()));
                    //修改订单信息
                    baseMapper.updateById(orderList);
                }
            }
        }
    }

    /**
     * 待支付订单超时数据
     *
     * @param hour
     * @return
     */
    @Override
    public List<OrderList> getCancelOrderList(String hour) {
        return baseMapper.getCancelOrderList(hour);
    }

    /**
     * 定时器取消订单
     */
    @Override
    public void cancelOrderListJob() {
        //过期时间(小时)
        String hour = iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "common_order_payment_timeout");
        if (StringUtils.isNotBlank(hour)) {
            List<OrderList> orderListList = baseMapper.getCancelOrderList(hour);
            orderListList.forEach(ol -> {
                log.info("定时器取消 平台 待支付订单超时未支付:订单编号:" + ol.getOrderNo());
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
    public String getOrderListTimer(String id, String hour) {
        return baseMapper.getOrderListTimer(id, hour);
    }

    /**
     * 待支付订单计时器
     *
     * @return
     */
    @Override
    public String prepaidOrderTimer(String id, Integer isPlatform) {
        //过期时间(小时)
        String hour = iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "common_order_payment_timeout");
        String timer;
        if (isPlatform.intValue() == 0) {
            //店铺订单
            timer = iOrderStoreListService.getOrderStoreListTimer(id, hour);
        } else {

            //平台订单
            timer = getOrderListTimer(id, hour);
        }
        return timer;
    }

    /**
     * 返回确认收货订单计时器(秒)
     *
     * @param id
     * @param hour
     * @return
     */
    @Override
    public String getConfirmReceiptTimer(String id, String hour) {
        return baseMapper.getConfirmReceiptTimer(id, hour);
    }

    ;

    /**
     * 平台/店铺确认收货订单(秒)
     *
     * @param id
     * @param isPlatform
     * @return
     */
    @Override
    public String confirmReceiptTimer(String id, Integer isPlatform) {
        String timer;

        //订单确认收货超时时间为15天即360小时(小时)
        String hour = iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "order_confirmation_receiving_timeout");
        if (isPlatform.intValue() == 0) {
            //店铺订单
            timer = iOrderStoreListService.getStoreConfirmReceiptTimer(id, hour);
        } else {

            //平台订单
            timer = getConfirmReceiptTimer(id, hour);
        }
        return timer;
    }

    /**
     * 确认订单超时数据
     *
     * @param hour
     * @return
     */
    @Override
    public List<OrderList> getConfirmReceiptOrderList(String hour) {
        return baseMapper.getConfirmReceiptOrderList(hour);
    }

    /**
     * 定时器确认收货订单
     */
    @Override
    public void confirmReceiptOrderListJob() {
        //过期时间(小时)
        String hour = iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "order_confirmation_receiving_timeout");
        if (StringUtils.isNotBlank(hour)) {
            List<OrderList> orderListList = baseMapper.getConfirmReceiptOrderList(hour);
            orderListList.forEach(ol -> {
                log.info("定时器取消 平台 确认收货超时:订单编号:" + ol.getOrderNo());
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
            //查询出超过售后时间的订单(50条上限) 取普通订单
            List<OrderList> orderCompletion = baseMapper.getOrderCompletion(hour);
            orderCompletion.forEach(oc -> {
                log.info(" 平台 确认收货后7天完成订单:订单编号:" + oc.getOrderNo());
                accomplishOrder(oc.getId());
            });
        }

    }

    @Override
    @Transactional
    public void accomplishOrder(String id) {

        //修改订单状态
        OrderList orderList = this.getById(id);
        //记录订单完成时间
        this.saveOrUpdate(orderList
                .setCompletionTime(new Date())
                .setStatus("5"));
        //购买用户获取福利金
        MemberList member = iMemberListService.getById(orderList.getMemberListId());
        LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                .eq(MemberGrade::getDelFlag, "0")
                .eq(MemberGrade::getStatus, "1");
        String orderTransaction = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "order_transaction");
        //订单实付款大于0赠送成长值
        if (orderList.getActualPayment().doubleValue() > 0 && iMemberGradeService.count(memberGradeLambdaQueryWrapper) > 0 && orderTransaction.equals("1")) {
            if (StringUtils.isNotBlank(member.getMemberGradeId())) {
                member.setGrowthValue(member.getGrowthValue().add(orderList.getActualPayment()));

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

                    } else {
                        MemberGrade grade = iMemberGradeService.list(new LambdaQueryWrapper<MemberGrade>()
                                .eq(MemberGrade::getDelFlag, "0")
                                .eq(MemberGrade::getStatus, "1")
                                .orderByDesc(MemberGrade::getSort)).get(0);
                        if (grade.getGrowthValueBig().doubleValue() <= member.getGrowthValue().doubleValue()) {
                            member.setMemberGradeId(grade.getId());
                        }
                    }

                }
            } else {
                member.setGrowthValue(member.getGrowthValue().add(orderList.getActualPayment()));
                memberGradeLambdaQueryWrapper.eq(MemberGrade::getStatus, "1")
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
            MemberGrowthRecord memberGrowthRecord = new MemberGrowthRecord()
                    .setMemberListId(member.getId())
                    .setTradeNo(orderList.getOrderNo())
                    .setTradeType("0")
                    .setRemark("订单交易[" + orderList.getOrderNo() + "]")
                    .setGrowthValue(orderList.getActualPayment())
                    .setOrderNo(OrderNoUtils.getOrderNo());
            iMemberGrowthRecordService.save(memberGrowthRecord);
            log.info(" 平台 确认收货后7天完成订单:会员成长值:" + memberGrowthRecord.getOrderNo());
        }

        //判断订单赠送福利金是否大于0
        if (orderList.getGiveWelfarePayments().doubleValue() > 0) {

            MemberWelfarePayments memberWelfarePayments = iMemberWelfarePaymentsService.getOne(new LambdaQueryWrapper<MemberWelfarePayments>()
                    .eq(MemberWelfarePayments::getMemberListId, member.getId()) //会员id
                    .eq(MemberWelfarePayments::getTradeNo, orderList.getOrderNo())//单号
                    .eq(MemberWelfarePayments::getWeType, "1")//类型；0：支出；1：收入
                    .eq(MemberWelfarePayments::getIsFreeze, "1"));//是否冻结
            if (oConvertUtils.isNotEmpty(memberWelfarePayments)) {
                //扣除冻结福利金,添加到可用福利金中
                iMemberListService.saveOrUpdate(member
                        .setWelfarePayments(member.getWelfarePayments().add(memberWelfarePayments.getBargainPayments()))
                        .setWelfarePaymentsFrozen(member.getWelfarePaymentsFrozen().subtract(memberWelfarePayments.getBargainPayments())));
                //修改会员福利金记录
                iMemberWelfarePaymentsService.saveOrUpdate(memberWelfarePayments
                        .setBargainTime(new Date())
                        .setWelfarePayments(member.getWelfarePayments())
                        .setIsFreeze("0")
                        .setTradeStatus("5")
                );
                iMarketingWelfarePaymentsService.save(new MarketingWelfarePayments()
                        .setDelFlag("0")
                        .setMemberListId(member.getId())
                        .setSerialNumber(OrderNoUtils.getOrderNo())
                        .setBargainPayments(memberWelfarePayments.getBargainPayments())
                        .setWelfarePayments(member.getWelfarePayments())
                        .setWeType("1")
                        .setGiveExplain("订单赠送福利金[" + memberWelfarePayments.getSerialNumber() + "]")
                        .setBargainTime(new Date())
                        .setOperator("系统")
                        .setStatus("1")
                        .setPayMode("2")
                        .setSendUser("平台")
                        .setIsPlatform("1")
                        .setUserType("0")
                        .setTradeNo(memberWelfarePayments.getSerialNumber())
                        .setTradeType("0"));

                log.info(" 平台 确认收货后7天完成订单:赠送福利金编号:" + memberWelfarePayments.getTradeNo());
            }

        }

        //查出不可用福利金
        MemberWelfarePayments memberWelfarePayments = iMemberWelfarePaymentsService.getOne(new LambdaQueryWrapper<MemberWelfarePayments>()
                .eq(MemberWelfarePayments::getTradeNo, orderList.getOrderNo())
                .eq(MemberWelfarePayments::getMemberListId, orderList.getMemberListId())
                .eq(MemberWelfarePayments::getIsFreeze, "2")
                .eq(MemberWelfarePayments::getWeType, "0"));
        if (oConvertUtils.isNotEmpty(memberWelfarePayments)) {
            iMemberListService.saveOrUpdate(member
                    .setWelfarePaymentsUnusable(member.getWelfarePaymentsUnusable().subtract(memberWelfarePayments.getBargainPayments())));

            iMemberWelfarePaymentsService.saveOrUpdate(memberWelfarePayments
                    .setIsFreeze("0")
                    .setTradeStatus("5")
            );
            log.info(" 平台 确认收货后7天完成订单:扣除不可用福利金编号:" + memberWelfarePayments.getTradeNo());
        }

        //查出供应商订单集合
        List<OrderProviderList> orderProviderLists = iOrderProviderListService.list(new LambdaQueryWrapper<OrderProviderList>()
                .eq(OrderProviderList::getOrderListId, orderList.getId()));

        orderProviderLists.forEach(opl -> {
            iOrderProviderListService.saveOrUpdate(opl.setStatus("5"));
            if (opl.getParentId().equals("0")) {
                //获取订单供应商
                if (iProviderManageService.count(new LambdaQueryWrapper<ProviderManage>().eq(ProviderManage::getSysUserId, opl.getSysUserId())) > 0) {
                    ProviderManage providerManage = iProviderManageService.list(new LambdaQueryWrapper<ProviderManage>().eq(ProviderManage::getSysUserId, opl.getSysUserId())).get(0);
                    LambdaQueryWrapper<ProviderRechargeRecord> providerRechargeRecordLambdaQueryWrapper = new LambdaQueryWrapper<ProviderRechargeRecord>()
                            .eq(ProviderRechargeRecord::getPayType, "0")
                            .eq(ProviderRechargeRecord::getGoAndCome, "0")
                            .eq(ProviderRechargeRecord::getTradeStatus, "2")
                            .eq(ProviderRechargeRecord::getTradeType, "0")
                            .eq(ProviderRechargeRecord::getTradeNo, opl.getOrderNo());
                    if (iProviderRechargeRecordService.count(providerRechargeRecordLambdaQueryWrapper) > 0) {
                        //获取供应室资金记录
                        ProviderRechargeRecord providerRechargeRecord = iProviderRechargeRecordService.getOne(providerRechargeRecordLambdaQueryWrapper);
                        if (oConvertUtils.isNotEmpty(providerRechargeRecord)) {
                            //将供应商冻结金额转为金额
                            boolean b = iProviderManageService.saveOrUpdate(providerManage
                                    .setAccountFrozen(providerManage.getAccountFrozen().subtract(providerRechargeRecord.getAmount())) //扣除冻结金额
                                    .setBalance(providerManage.getBalance().add(providerRechargeRecord.getAmount())));//添加金额
                            if (b) {
                                iProviderRechargeRecordService.saveOrUpdate(providerRechargeRecord.setTradeStatus("5"));
                                //生成资金流水
                                ProviderAccountCapital providerAccountCapital = new ProviderAccountCapital();
                                providerAccountCapital.setDelFlag("0")
                                        .setSysUserId(providerManage.getSysUserId())
                                        .setPayType("0")
                                        .setGoAndCome("0")
                                        .setAmount(providerRechargeRecord.getAmount())
                                        .setOrderNo(providerRechargeRecord.getOrderNo())
                                        .setBalance(providerManage.getBalance());
                                iProviderAccountCapitalService.save(providerAccountCapital);
                            }

                        }
                    }
                }
            }
        });
        if (orderList.getDistributionCommission().doubleValue() > 0) {
            MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>()
                    .eq(MarketingDistributionSetting::getStatus,"1")
                    .orderByDesc(MarketingDistributionSetting::getCreateTime)
                    .last("limit 1"));

            //查出分销资金记录集合
            List<MemberRechargeRecord> memberRechargeRecords = iMemberRechargeRecordService.list(new LambdaQueryWrapper<MemberRechargeRecord>()
                    .eq(MemberRechargeRecord::getPayType, "3")
                    .eq(MemberRechargeRecord::getGoAndCome, "0")
                    .eq(MemberRechargeRecord::getTradeStatus, "2")
                    .eq(MemberRechargeRecord::getTradeType, "0")
                    .eq(MemberRechargeRecord::getTradeNo, orderList.getOrderNo()));
            if (oConvertUtils.isNotEmpty(memberRechargeRecords)) {
                memberRechargeRecords.forEach(mrrs -> {
                    //查出分销会员
                    MemberList distributionMember = iMemberListService.getById(mrrs.getMemberListId());

                    //扣除冻结金额,转为余额
                    iMemberListService.saveOrUpdate(distributionMember
                            .setAccountFrozen(distributionMember.getAccountFrozen().subtract(mrrs.getAmount())));

                        //将资金记录交易类型改为交易完成
                        iMemberRechargeRecordService.saveOrUpdate(mrrs
                                .setTradeStatus("5"));
                    //分销获取金额
                    if(marketingDistributionSetting.getCommissionType().equals("0")) {
                        iMemberListService.addBlance(mrrs.getMemberListId(),mrrs.getAmount(),mrrs.getOrderNo(),"3");
                    }

                    // 分销获取积分
                    if(marketingDistributionSetting.getCommissionType().equals("1")) {
                        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
                        iMemberWelfarePaymentsService.addWelfarePayments(mrrs.getMemberListId(),mrrs.getAmount().divide(integralValue,2,RoundingMode.DOWN),"19",mrrs.getOrderNo(),"");
                    }

                });
            }
            //判断会员是否有归属店铺
            if (StringUtils.isNotBlank(member.getSysUserId()) && orderList.getDistributionCommission().doubleValue() > 0) {
                //获取归属店铺和渠道集合
                List<StoreRechargeRecord> storeRechargeRecords = iStoreRechargeRecordService.list(new LambdaQueryWrapper<StoreRechargeRecord>()
                        .eq(StoreRechargeRecord::getTradeNo, orderList.getOrderNo())
                        .eq(StoreRechargeRecord::getGoAndCome, "0")
                        .eq(StoreRechargeRecord::getTradeStatus, "2")
                        .in(StoreRechargeRecord::getPayType, "5", "6")
                        .eq(StoreRechargeRecord::getTradeType, "0"));
                if (oConvertUtils.isNotEmpty(storeRechargeRecords)) {
                    storeRechargeRecords.forEach(stts -> {
                        //获取店铺
                        StoreManage storeManage = iStoreManageService.getById(stts.getStoreManageId());
                        //将店铺冻结金额转为金额
                        boolean b = iStoreManageService.saveOrUpdate(storeManage
                                .setAccountFrozen(storeManage.getAccountFrozen().subtract(stts.getAmount()))
                                .setBalance(storeManage.getBalance().add(stts.getAmount())));
                        if (b) {
                            //交易成功
                            iStoreRechargeRecordService.saveOrUpdate(stts.setTradeStatus("5"));

                            //生成资金流水记录
                            StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                            storeAccountCapital.setDelFlag("0")
                                    .setStoreManageId(storeManage.getId())
                                    .setGoAndCome("0")
                                    .setAmount(stts.getAmount())
                                    .setOrderNo(stts.getOrderNo())
                                    .setBalance(storeManage.getBalance());
                            //归属店铺
                            if (stts.getPayType().equals("5")) {
                                storeAccountCapital.setPayType("5");
                            }
                            //渠道奖励
                            if (stts.getPayType().equals("6")) {
                                storeAccountCapital.setPayType("6");
                            }
                            iStoreAccountCapitalService.save(storeAccountCapital);
                        }

                    });
                }

            }
            //加盟商余额记录条件构造器
            LambdaUpdateWrapper<AllianceRechargeRecord> allianceRechargeRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<AllianceRechargeRecord>()
                    .eq(AllianceRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(AllianceRechargeRecord::getPayType, "0")
                    .eq(AllianceRechargeRecord::getGoAndCome, "0")
                    .eq(AllianceRechargeRecord::getTradeStatus, "2")
                    .eq(AllianceRechargeRecord::getTradeType, "0")
                    .eq(AllianceRechargeRecord::getPayment, "0");
            if (iAllianceRechargeRecordService.count(allianceRechargeRecordLambdaUpdateWrapper) > 0) {
                //查出加盟商
                List<AllianceRechargeRecord> allianceRechargeRecords = iAllianceRechargeRecordService.list(allianceRechargeRecordLambdaUpdateWrapper);
                allianceRechargeRecords.forEach(arrs -> {
                    //修改加盟商余额明细
                    iAllianceRechargeRecordService.saveOrUpdate(arrs.setTradeStatus("5"));

                    AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                            .eq(AllianceManage::getSysUserId, arrs.getSysUserId()));
                    //将加盟商冻结金额划入余额中
                    iAllianceManageService.saveOrUpdate(allianceManage
                            .setBalance(allianceManage.getBalance().add(arrs.getAmount()))
                            .setAccountFrozen(allianceManage.getAccountFrozen().subtract(arrs.getAmount()))
                    );
                    iAllianceAccountCapitalService.save(new AllianceAccountCapital()
                            .setDelFlag("0")
                            .setSysUserId(allianceManage.getSysUserId())
                            .setPayType("0")
                            .setGoAndCome("0")
                            .setAmount(arrs.getAmount())
                            .setOrderNo(arrs.getOrderNo())
                            .setBalance(allianceManage.getBalance())
                    );

                });
            }
            //获取代理集合
            LambdaQueryWrapper<AgencyRechargeRecord> agencyRechargeRecordLambdaQueryWrapper = new LambdaQueryWrapper<AgencyRechargeRecord>()
                    .eq(AgencyRechargeRecord::getPayType, "0")
                    .eq(AgencyRechargeRecord::getGoAndCome, "0")
                    .eq(AgencyRechargeRecord::getTradeStatus, "2")
                    .eq(AgencyRechargeRecord::getTradeType, "0")
                    .eq(AgencyRechargeRecord::getTradeNo, orderList.getOrderNo());
            if (iAgencyRechargeRecordService.count(agencyRechargeRecordLambdaQueryWrapper) > 0) {
                List<AgencyRechargeRecord> agencyRechargeRecords = iAgencyRechargeRecordService.list(agencyRechargeRecordLambdaQueryWrapper);
                if (oConvertUtils.isNotEmpty(agencyRechargeRecords)) {
                    agencyRechargeRecords.forEach(arrs -> {

                        //获取代理
                        AgencyManage agencyManage = iAgencyManageService.list(new LambdaQueryWrapper<AgencyManage>()
                                .eq(AgencyManage::getSysUserId, arrs.getSysUserId())).get(0);

                        // 将代理冻结金额转为金额
                        iAgencyManageService.saveOrUpdate(agencyManage
                                .setAccountFrozen(agencyManage.getAccountFrozen().subtract(arrs.getAmount()))
                                .setBalance(agencyManage.getBalance().add(arrs.getAmount())));
                        //交易完成
                        iAgencyRechargeRecordService.saveOrUpdate(arrs.setTradeStatus("5"));

                        AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
                        agencyAccountCapital.setDelFlag("0")
                                .setSysUserId(agencyManage.getSysUserId())
                                .setPayType("0")
                                .setGoAndCome("0")
                                .setAmount(arrs.getAmount())
                                .setOrderNo(arrs.getOrderNo())
                                .setBalance(agencyManage.getBalance());
                        //生成资金流水记录
                        iAgencyAccountCapitalService.save(agencyAccountCapital);
                    });
                }
            }

        }

    }

    /**
     * 购买人订单随机数显示会员信息
     *
     * @return
     */
    public List<Map<String, Object>> getOrderMemberHubble() {
        return orderListMapper.getOrderMemberHubble();
    }

    /**
     * 添加消息推送
     */
    public void addSendMesege(String orderId, Integer isPlatform) {
        Map<String, Object> objectMap = Maps.newHashMap();
        OrderList orderList = orderListMapper.selectById(orderId);
        if (orderList != null) {
            objectMap.put("orderNo", orderList.getOrderNo());

        }
    }

    /**
     * 订单导出列表
     *
     * @param orderListVO
     * @return
     */
    public List<OrderListExportDTO> getOrderListDtoExport(Map<String, Object> orderListVO) {
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String sysUserId = ""; //PermissionUtils.ifPlatform();
        if (org.apache.commons.lang.StringUtils.isNotBlank(sysUserId)) {
            orderListVO.put("sysUserId", sysUserId);
        }
        List<OrderListExportDTO> orderListExportDTOList = orderListMapper.getOrderListDtoExport(orderListVO);
        return orderListExportDTOList;
    }

    @Override
    @Transactional
    public void orderOff(OrderListDTO orderListDTO) {
        OrderList orderList = this.getById(orderListDTO.getId());
        //买家关闭
        orderList.setCloseType(orderListDTO.getCloseType());
        orderList.setCloseExplain(orderListDTO.getCloseExplain());
        orderList.setCloseTime(new Date());
        //交易关闭
        orderList.setStatus("4");
        this.saveOrUpdate(orderList);
        //自选订单
        if (orderList.getWelfarePayments().doubleValue() > 0) {
            //会员福利金
            List<MemberWelfarePayments> memberWelfarePayments = iMemberWelfarePaymentsService.list(new LambdaQueryWrapper<MemberWelfarePayments>()
                    .eq(MemberWelfarePayments::getTradeNo, orderList.getOrderNo())
                    .eq(MemberWelfarePayments::getMemberListId, orderList.getMemberListId())
                    .eq(MemberWelfarePayments::getIsFreeze, "2")
                    .eq(MemberWelfarePayments::getWeType, "0"));
            if (memberWelfarePayments.size()>0) {
                memberWelfarePayments.forEach(mwps->{
                    //退还用户福利金
                    MemberList memberList = iMemberListService.getById(mwps.getMemberListId());
                    memberList.setWelfarePayments(memberList.getWelfarePayments().add(mwps.getBargainPayments()));
                    memberList.setWelfarePaymentsUnusable(memberList.getWelfarePaymentsUnusable().subtract(mwps.getBargainPayments()));
                    iMemberListService.saveOrUpdate(memberList);
                    iMemberWelfarePaymentsService.saveOrUpdate(mwps
                            .setIsFreeze("0")
                            .setWeType("1")
                            .setWelfarePayments(memberList.getWelfarePayments())
                            .setTradeStatus("7")
                            .setBargainTime(new Date())
                            .setWpExplain("取消订单[" + orderList.getId() + "]"));
                });
            }
            //判断该订单赠送福利金是否大于0
            if (orderList.getGiveWelfarePayments().doubleValue() > 0) {
                LambdaQueryWrapper<MemberWelfarePayments> memberWelfarePaymentsLambdaQueryWrapper = new LambdaQueryWrapper<MemberWelfarePayments>()
                        .eq(MemberWelfarePayments::getMemberListId, orderList.getMemberListId())
                        .eq(MemberWelfarePayments::getTradeNo, orderList.getOrderNo())
                        .eq(MemberWelfarePayments::getWeType, "1")
                        .eq(MemberWelfarePayments::getIsPlatform, "1")
                        .eq(MemberWelfarePayments::getIsFreeze, "1");
                if (iMemberWelfarePaymentsService.count() > 0) {
                    List<MemberWelfarePayments> memberWelfarePaymentsList = iMemberWelfarePaymentsService.list(memberWelfarePaymentsLambdaQueryWrapper);
                    memberWelfarePaymentsList.forEach(mwps -> {
                        iMemberWelfarePaymentsService.saveOrUpdate(mwps.setDelFlag("0"));
                        MemberList memberList = iMemberListService.getById(mwps.getMemberListId());
                        iMemberListService.saveOrUpdate(memberList.setWelfarePaymentsFrozen(memberList.getWelfarePaymentsFrozen().subtract(mwps.getBargainPayments())));
                    });
                }
            }

        }
        QueryWrapper<OrderProviderList> orderProviderListQueryWrapper = new QueryWrapper<>();
        orderProviderListQueryWrapper.eq("order_list_id", orderList.getId());
        List<OrderProviderList> orderProviderLists = iOrderProviderListService.list(orderProviderListQueryWrapper);
        orderProviderLists.forEach(ops -> {
            ops.setStatus("4");
            iOrderProviderListService.saveOrUpdate(ops);
            //退回库存
            QueryWrapper<OrderProviderGoodRecord> orderProviderGoodRecordQueryWrapper = new QueryWrapper<>();
            orderProviderGoodRecordQueryWrapper.eq("order_provider_list_id", ops.getId());
            List<OrderProviderGoodRecord> orderProviderGoodRecords = iOrderProviderGoodRecordService.list(orderProviderGoodRecordQueryWrapper);
            orderProviderGoodRecords.forEach(opgr -> {
                String goodListId = opgr.getGoodListId();
                String goodSpecificationId = opgr.getGoodSpecificationId();
                GoodSpecification goodSpecification = iGoodSpecificationService.getById(goodSpecificationId);
                if (goodSpecification != null) {
                    goodSpecification.setRepertory(goodSpecification.getRepertory().add(opgr.getAmount()));
                    iGoodSpecificationService.saveOrUpdate(goodSpecification);
                }
            });
            if (ops.getParentId().equals("0")) {


                //供应商资金条件
                LambdaQueryWrapper<ProviderRechargeRecord> lambdaQueryWrapper = new LambdaQueryWrapper<ProviderRechargeRecord>()
                        .eq(ProviderRechargeRecord::getSysUserId, ops.getSysUserId())
                        .eq(ProviderRechargeRecord::getTradeNo, ops.getOrderNo())
                        .eq(ProviderRechargeRecord::getGoAndCome, "0")
                        .eq(ProviderRechargeRecord::getTradeType, "0")
                        .eq(ProviderRechargeRecord::getPayType, "0");

                //查询供应商资金记录
                long providerCount = iProviderRechargeRecordService.count(lambdaQueryWrapper);
                //判断是否有供应商资金记录
                if (providerCount > 0) {
                    //查出供应商资金记录
                    ProviderRechargeRecord providerRechargeRecord = iProviderRechargeRecordService.list(lambdaQueryWrapper).get(0);
                    //修改供应商资金记录状态
                    iProviderRechargeRecordService.saveOrUpdate(providerRechargeRecord
                            .setTradeStatus("7"));
                    //退回冻结金额
                    ProviderManage providerManage = iProviderManageService.getOne(new LambdaUpdateWrapper<ProviderManage>()
                            .eq(ProviderManage::getSysUserId, providerRechargeRecord.getSysUserId()));
                    iProviderManageService.saveOrUpdate(providerManage
                            .setAccountFrozen(providerManage.getAccountFrozen().subtract(providerRechargeRecord.getAmount())));
                }
            }
        });

        if (orderList.getOrderType().equals("0")) {
            //会员金额取消
            LambdaUpdateWrapper<MemberRechargeRecord> memberRechargeRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<MemberRechargeRecord>()
                    .eq(MemberRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(MemberRechargeRecord::getTradeType, "0")
                    .eq(MemberRechargeRecord::getPayType, "3");
            if (iMemberRechargeRecordService.count(memberRechargeRecordLambdaUpdateWrapper) > 0) {
                List<MemberRechargeRecord> memberRechargeRecords = iMemberRechargeRecordService.list(memberRechargeRecordLambdaUpdateWrapper);
                memberRechargeRecords.forEach(mrrs -> {
                    iMemberRechargeRecordService.saveOrUpdate(mrrs.setTradeStatus("7"));

                    MemberList memberList = iMemberListService.getById(mrrs.getMemberListId());

                    iMemberListService.saveOrUpdate(memberList
                            .setAccountFrozen(memberList.getAccountFrozen().subtract(mrrs.getAmount())));
                });
            }
            //店铺余额记录修改
            LambdaUpdateWrapper<StoreRechargeRecord> storeRechargeRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<StoreRechargeRecord>()
                    .eq(StoreRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(StoreRechargeRecord::getTradeType, "0")
                    .in(StoreRechargeRecord::getPayType, "5", "6");
            if (iStoreRechargeRecordService.count(storeRechargeRecordLambdaUpdateWrapper) > 0) {
                List<StoreRechargeRecord> storeRechargeRecords = iStoreRechargeRecordService.list(storeRechargeRecordLambdaUpdateWrapper);
                storeRechargeRecords.forEach(srrs -> {
                    iStoreRechargeRecordService.saveOrUpdate(srrs.setTradeStatus("7"));

                    StoreManage storeManage = iStoreManageService.getById(srrs.getStoreManageId());

                    iStoreManageService.saveOrUpdate(storeManage
                            .setAccountFrozen(storeManage.getAccountFrozen().subtract(srrs.getAmount())));
                });
            }

            //加盟商余额记录修改
            LambdaUpdateWrapper<AllianceRechargeRecord> allianceRechargeRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<AllianceRechargeRecord>()
                    .eq(AllianceRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(AllianceRechargeRecord::getPayType, "0")
                    .eq(AllianceRechargeRecord::getGoAndCome, "0")
                    .eq(AllianceRechargeRecord::getTradeType, "0")
                    .eq(AllianceRechargeRecord::getPayment, "0");
            if (iAllianceRechargeRecordService.count(allianceRechargeRecordLambdaUpdateWrapper) > 0) {
                List<AllianceRechargeRecord> allianceRechargeRecords = iAllianceRechargeRecordService.list(allianceRechargeRecordLambdaUpdateWrapper);
                allianceRechargeRecords.forEach(arrs -> {

                    iAllianceRechargeRecordService.saveOrUpdate(arrs.setTradeStatus("7"));

                    AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                            .eq(AllianceManage::getSysUserId, arrs.getSysUserId()));

                    iAllianceManageService.saveOrUpdate(allianceManage
                            .setAccountFrozen(allianceManage.getAccountFrozen().subtract(arrs.getAmount())));
                });
            }
            //代理余额记录修改
            LambdaUpdateWrapper<AgencyRechargeRecord> agencyRechargeRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<AgencyRechargeRecord>()
                    .eq(AgencyRechargeRecord::getTradeNo, orderList.getOrderNo())
                    .eq(AgencyRechargeRecord::getPayType, "0")
                    .eq(AgencyRechargeRecord::getTradeType, "0");
            if (iAgencyRechargeRecordService.count(agencyRechargeRecordLambdaUpdateWrapper) > 0) {
                List<AgencyRechargeRecord> agencyRechargeRecords = iAgencyRechargeRecordService.list(agencyRechargeRecordLambdaUpdateWrapper);
                agencyRechargeRecords.forEach(arrs -> {
                    iAgencyRechargeRecordService.saveOrUpdate(arrs.setTradeStatus("7"));
                    AgencyManage agencyManage = iAgencyManageService.getOne(new LambdaUpdateWrapper<AgencyManage>().eq(AgencyManage::getSysUserId, arrs.getSysUserId()));
                    iAgencyManageService.saveOrUpdate(agencyManage.setAccountFrozen(agencyManage.getAccountFrozen().subtract(arrs.getAmount())));
                });
            }

        }
    }

    @Override
    public List<Map<String, Object>> orderSumList() {
        return baseMapper.orderSumList();
    }

    @Override
    public int getPrefectureGoodCount(Map<String, Object> paramMap) {
        return baseMapper.getPrefectureGoodCount(paramMap);
    }


    @Override
    public void exportXls(OrderListVO orderListVO, HttpServletResponse response) throws IOException {

        //获取所有订单
        ArrayList<OrderListDTO> list = orderListMapper.orderListDtoExport(orderListVO);

        ArrayList<OrderListExpDTO> orderListExpDTOs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            OrderListDTO orderListDTO = list.get(i);

            OrderListExpDTO orderListExpDTO = new OrderListExpDTO();
            orderListExpDTO.setIndex(i + 1);
            orderListExpDTO.setCreateTime(orderListDTO.getCreateTime());
            orderListExpDTO.setOrderNo(orderListDTO.getOrderNo());
            orderListExpDTO.setConsignee(orderListDTO.getConsignee());
            orderListExpDTO.setContactNumber(orderListDTO.getContactNumber());
            orderListExpDTO.setShippingAddress(orderListDTO.getShippingAddress());
            orderListExpDTO.setMessage(orderListDTO.getMessage());
            orderListExpDTO.setPayPrice(orderListDTO.getPayPrice());
            orderListExpDTO.setPayWelfarePayments(orderListDTO.getPayWelfarePayments());
            orderListExpDTO.setPayWelfarePaymentsPrice(orderListDTO.getPayWelfarePaymentsPrice());
            orderListExpDTO.setBalance(orderListDTO.getBalance());
            String tradeStatus = "";
            //订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败；5：交易完成；
            switch (orderListDTO.getStatus()) {
                case "0":
                    tradeStatus = "待付款";
                    break;
                case "1":
                    tradeStatus = "待发货(部分发货)";
                    break;
                case "2":
                    tradeStatus = "待收货";
                    break;
                case "3":
                    tradeStatus = "交易成功";
                    break;
                case "4":
                    tradeStatus = "交易失败";
                    break;
                case "5":
                    tradeStatus = "交易完成";
                    break;
            }
            orderListExpDTO.setStatus(tradeStatus);
            orderListExpDTO.setPayTime(orderListDTO.getPayTime());
            orderListExpDTO.setShipmentsTime(orderListDTO.getShipmentsTime());
            orderListExpDTO.setDeliveryTime(orderListDTO.getDeliveryTime());
            orderListExpDTO.setCompletionTime(orderListDTO.getCompletionTime());

            //获取供应商订单
            QueryWrapper<OrderProviderList> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("order_list_id", orderListDTO.getId());
            List<OrderProviderList> orderProviderLists = orderProviderListMapper.selectList(objectQueryWrapper);

            ArrayList<OrderProviderListExpDTO> orderProviderListExpDTOs = new ArrayList<>();
            for (OrderProviderList orderProviderList : orderProviderLists) {

                OrderProviderListExpDTO orderProviderListExpDTO = new OrderProviderListExpDTO();
                orderProviderListExpDTO.setOrderNo(orderProviderList.getOrderNo());
                orderProviderListExpDTO.setLogisticsCompany(orderProviderList.getLogisticsCompany());
                orderProviderListExpDTO.setTrackingNumber(orderProviderList.getTrackingNumber());
                //  orderProviderListExpDTO.setGoodsTotalCost(orderProviderList.getGoodsTotalCost());

                SysUser sysUser = sysUserMapper.selectById(orderProviderList.getSysUserId());
                String realname = sysUser == null ? "" : sysUser.getRealname();
                orderProviderListExpDTO.setProviderName(realname);

                List<Map<String, Object>> orderProviderTemplateMap = orderProviderTemplateMapper.getOrderProviderTemplateMap(orderProviderList.getId());
                if (orderProviderTemplateMap.size() < 0) {
                    orderProviderTemplateMap = providerTemplateMapper.getProviderTemplateMap(orderProviderList.getId());
                }
                double shipFeeCount = 0;
                for (Map<String, Object> stringObjectMap : orderProviderTemplateMap) {
                    shipFeeCount += new Double(stringObjectMap.get("shipFee").toString());
                }
                orderProviderListExpDTO.setShipFee(new BigDecimal(shipFeeCount));
                //获取商品项
                Map paramMap = new HashMap<>();
                paramMap.put("order_provider_list_id", orderProviderList.getId());
                List<OrderProviderGoodRecord> orderProviderGoodRecords = orderProviderGoodRecordMapper.selectByMap(paramMap);

                ArrayList<OrderProviderGoodRecordExpDTO> orderProviderGoodRecordExpDTOs = new ArrayList<>();
                for (OrderProviderGoodRecord orderProviderGoodRecord : orderProviderGoodRecords) {

                    OrderProviderGoodRecordExpDTO orderProviderGoodRecordExpDTO = new OrderProviderGoodRecordExpDTO();
                    Map<String, String> map = JSON.parseObject(orderProviderGoodRecord.getMainPicture(), Map.class);
                    if (StringUtils.isNotEmpty(map.get("0"))) {

//                        //String originalImg=map.get("0");
//                        String originalImg = "C:\\Users\\12257\\Desktop\\未来派城市 日落 科幻 概念艺术4k壁纸_彼岸图网.jpg";
//                        String reduceImg = uploadpath + "/reduceImg.png";
//                        //进行图片压缩
//                        FileUtil.reduceImg(originalImg, reduceImg, 50, 50, null);
//                        orderProviderGoodRecordExpDTO.setMainPicture(reduceImg);
                    }

                    orderProviderGoodRecordExpDTO.setGoodName(orderProviderGoodRecord.getGoodName());
                    orderProviderGoodRecordExpDTO.setAmount(orderProviderGoodRecord.getAmount());

                    GoodList goodList = goodListMapper.selectById(orderProviderGoodRecord.getGoodListId());
                    String goodNo = goodList == null ? "" : goodList.getGoodNo();
                    orderProviderGoodRecordExpDTO.setGoodNo(goodNo);
                    orderProviderGoodRecordExpDTOs.add(orderProviderGoodRecordExpDTO);
                    orderProviderGoodRecordExpDTO.setSpecification(orderProviderGoodRecord.getSpecification());
                    orderProviderGoodRecordExpDTO.setCostPrice(orderProviderGoodRecord.getCostPrice());
                    orderProviderGoodRecordExpDTO.setSkuNo(orderProviderGoodRecord.getSkuNo());
                }
                orderProviderListExpDTO.setOrderProviderGoodRecordExpDTOs(orderProviderGoodRecordExpDTOs);

                if (orderProviderGoodRecords.size() > 0) {
                    orderProviderListExpDTOs.add(orderProviderListExpDTO);
                }
            }
            orderListExpDTO.setOrderProviderListExpDTs(orderProviderListExpDTOs);
            orderListExpDTOs.add(orderListExpDTO);
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("订单数据", "订单数据"), OrderListExpDTO.class, orderListExpDTOs);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(barray);
        FileUtil.responseFileStream(response, is, "xls");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> ordereDlivery(String listMap) {
        Result<String> result = new Result<String>();
        JSONObject jsonObject = JSONObject.parseObject(listMap);
        List<Map<String, Object>> listObjectSec = jsonObject.getJSONArray("listMap").toJavaObject(List.class);
        OrderProviderList orderProviderList = new OrderProviderList();
        OrderProviderGoodRecord orderProviderGoodRecord;
        List<Map<String, Object>> orderProviderGoodRecordInfoList;
        String addorderProviderId;
        if (listObjectSec.size() > 0) {
            for (Map<String, Object> map : listObjectSec) {
                //包裹数据
                List<Map<String, Object>> listParcelMapSS = (List<Map<String, Object>>) (List) map.get("listParcel");
                for (Map<String, Object> listParcelMap : listParcelMapSS) {

                    //包裹内的商品修改供应商订单id
                    orderProviderGoodRecordInfoList = (List<Map<String, Object>>) (List) listParcelMap.get("orderProviderGoodRecordInfo");

                    if (orderProviderGoodRecordInfoList.size() > 0) {
                        //查询供应商订单信息
                        orderProviderList = orderProviderListService.getById(listParcelMap.get("id").toString());
                        //添加包裹,并返回新增ID
                        addorderProviderId = addorderProviderList(orderProviderList, listParcelMap.get("logisticsCompany").toString(), listParcelMap.get("trackingNumber").toString());

                        for (Map<String, Object> orderProviderGoodRecordId : orderProviderGoodRecordInfoList) {
                            //订单商品信息
                            orderProviderGoodRecord = orderProviderGoodRecordService.getById(orderProviderGoodRecordId.get("id").toString());
                            if (orderProviderGoodRecord != null) {
                                //修改商品的OrderProviderListId为包裹的已发货包裹
                                orderProviderGoodRecord.setOrderProviderListId(addorderProviderId);
                                orderProviderGoodRecordService.updateById(orderProviderGoodRecord);
                            }
                        }
                    }
                }
                //调用方法
                //是否全部发货,修改orderList的状态内容
                if (org.apache.commons.lang.StringUtils.isNotBlank(orderProviderList.getId())){
                    orderProviderListService.ShipmentOrderModification(orderProviderList);
                }
            }
            result.success("发货成功!");
        }
        return result;
    }

    /**
     * 添加已发货的供应商包裹信息
     *
     * @param orderProviderList
     * @param logisticsCompany  物流公司
     * @param trackingNumber    快递单号
     */
    public String addorderProviderList(OrderProviderList orderProviderList, String logisticsCompany, String trackingNumber) {
        if (StrUtil.hasBlank(logisticsCompany,trackingNumber)) {
            throw new JeecgBootException("物流公司/快递单号不能为空~");
        }
        //获取1688订单物流信息
//        String taoOrderId = orderProviderList.getTaoOrderId();
//        if (StrUtil.isBlank(taoOrderId)) {
//            throw new JeecgBootException("该供应商订单还未下单，请先下单~");
//        }
//        AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo logisticsItemsInfo = ali1688Service.getAlibabaOpenplatformTradeModelNativeLogisticsItemsInfo(Convert.toLong(taoOrderId));
//        if (logisticsItemsInfo == null) {
//            throw new JeecgBootException("该供应商订单暂无物流信息~");
//        }
//        if (!StrUtil.equals(logisticsItemsInfo.getLogisticsCompanyNo(),logisticsCompany)) {
//            throw new JeecgBootException("物流公司输入错误~");
//        }
//        if (!StrUtil.equals(logisticsItemsInfo.getLogisticsBillNo(),trackingNumber)) {
//            throw new JeecgBootException("快递单号输入错误~");
//        }
        OrderProviderList opl = new OrderProviderList();
        opl.setDelFlag(orderProviderList.getDelFlag());
        opl.setMemberListId(orderProviderList.getMemberListId());
        opl.setOrderListId(orderProviderList.getOrderListId());
        opl.setSysUserId(orderProviderList.getSysUserId());
        opl.setOrderNo(orderProviderList.getOrderNo());
        opl.setDistribution(orderProviderList.getDistribution());
        opl.setShipFee(orderProviderList.getShipFee());
        opl.setProviderAddressIdSender(orderProviderList.getProviderAddressIdSender());
        opl.setProviderAddressIdTui(orderProviderList.getProviderAddressIdTui());
        opl.setLogisticsTracking(orderProviderList.getLogisticsTracking());
        opl.setGoodsTotal(orderProviderList.getGoodsTotal());
        opl.setGoodsTotalCost(orderProviderList.getGoodsTotalCost());
        opl.setCustomaryDues(orderProviderList.getCustomaryDues());
        opl.setActualPayment(orderProviderList.getActualPayment());
//        opl.setTaoOrderId(orderProviderList.getTaoOrderId());
        opl.setIsSend("1");
        opl.setAutoDelivery("0");
        //修改数据
        opl.setParentId(orderProviderList.getId());
        opl.setLogisticsCompany(logisticsCompany);
        opl.setTrackingNumber(trackingNumber);
        opl.setStatus("2");
        orderProviderListService.save(opl);
        return opl.getId();
        //发送包裹消息（提醒包裹已发出）
        /*EmailSendMsgHandle eh = new EmailSendMsgHandle();
           MemberList memberList = memberListService.getById(opl.getMemberListId()) ;
        eh.SendMsg("274794391@qq.com", "系统推送","您的包裹已发出");*/
    }
}

