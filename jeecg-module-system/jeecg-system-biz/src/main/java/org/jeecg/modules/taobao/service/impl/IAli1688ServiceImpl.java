package org.jeecg.modules.taobao.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.alibaba.product.param.*;
import com.alibaba.trade.param.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodType;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.jeecg.modules.system.service.ISysAreaService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.taobao.service.IAli1688Service;
import org.jeecg.modules.taobao.utils.Ali1688Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 张少林
 * @date 2023年03月23日 6:20 下午
 */
@Slf4j
@Service
public class IAli1688ServiceImpl implements IAli1688Service {

    @Autowired
    private Ali1688Utils ali1688Utils;
    @Autowired
    private IGoodListService goodListService;

    @Autowired
    private IGoodSpecificationService goodSpecificationService;

    @Autowired
    private IGoodTypeService iGoodTypeService;


    @Autowired
    private ISysAreaService iSysAreaService;

    @Autowired
    private ISysDictService iSysDictService;

    @Override
    public boolean addShop(Long offerId, String title) {
        //供应链用户id
        String sysUserId = "220ebc6c395bdb6cfc7c5b721746a79c";//1688供应商

        String goodTypeId = "";

        if (StrUtil.isBlank(title)) {
            //先判断平台中是否存在商品, 用于 1688 自动推送更新场景 @zhangshaolin
            LambdaQueryWrapper<GoodList> goodListLambdaQueryWrapper = new LambdaQueryWrapper<>();
            goodListLambdaQueryWrapper.eq(GoodList::getGoodNo, offerId).eq(GoodList::getDelFlag, "0");
            GoodList existGoodList = goodListService.getOne(goodListLambdaQueryWrapper, false);
            if (existGoodList == null) {
                return false;
            }
            goodTypeId = existGoodList.getGoodTypeId();
        } else {
            // 用于定时任务同步数据场景 @zhangshaolin
            long count = iGoodTypeService.count(new LambdaQueryWrapper<GoodType>().eq(GoodType::getName, title).eq(GoodType::getStatus, "1").eq(GoodType::getLevel, "3"));
            if (count == 0) {
                log.info("没有被允许的商品分类：offerId=" + offerId + "；title=" + title);
                return false;
            }
            goodTypeId = iGoodTypeService.getOne(new LambdaQueryWrapper<GoodType>()
                    .eq(GoodType::getName, title).eq(GoodType::getStatus, "1")
                    .eq(GoodType::getLevel, "3")
                    .orderByDesc(GoodType::getCreateTime).last("limit 1")).getId();
        }

        SDKResult<AlibabaCpsMediaProductInfoResult> productInfoResult = getProductInfoResult(offerId);
        if (productInfoResult.getResult() == null) {
            log.info("商品不存在" + offerId);
            return false;
        }
        if (!productInfoResult.getResult().getSuccess()) {
            log.error("错误商品id：" + offerId + "；错误代码：" + productInfoResult.getResult().getErrorCode() + "；错误信息：" + productInfoResult.getResult().getErrorMsg());
            return false;
        }

//        title = productInfoResult.getResult().getProductInfo().getCategoryName();

        String marketingPrefectureTypeId = "";

        //常量
        String baseUrl = "cbu01.alicdn.com/";

        if (productInfoResult.getResult().getSuccess()) {
            AlibabaProductProductInfo alibabaProductProductInfo = productInfoResult.getResult().getProductInfo();

            GoodList goodList = new GoodList();
            goodList.setSysUserId(sysUserId);//供应链用户id
            goodList.setGoodTypeId(goodTypeId);//goodTypeId
            if (alibabaProductProductInfo.getImage() == null) {
                log.error("商品id：" + offerId + "因为没有主图被过滤！！！");
                return false;
            }
            //商品主图
            String[] mainPictures = alibabaProductProductInfo.getImage().getImages();
            Map<String, String> mainPictureMap = Maps.newLinkedHashMap();
            int mi = 0;
            for (String picture : mainPictures) {
                mainPictureMap.put(mi + "", StringUtils.substringAfter(picture, baseUrl));
                mi++;
            }
            goodList.setMainPicture(JSON.toJSONString(mainPictureMap));
            goodList.setGoodName(alibabaProductProductInfo.getSubject());//商品名称
            goodList.setGoodDescribe(alibabaProductProductInfo.getCategoryName());//商品描述

            //详情图
            if (alibabaProductProductInfo.getIntelligentInfo() != null) {
                String[] productInfos = alibabaProductProductInfo.getIntelligentInfo().getDescriptionImages();
                Map<String, String> productInfosMap = Maps.newLinkedHashMap();
                int pi = 0;
                for (String productInfo : productInfos) {
                    productInfosMap.put(pi + "", StringUtils.substringAfter(productInfo, baseUrl));
                    pi++;
                }

                goodList.setDetailsGoods(JSON.toJSONString(productInfosMap));
            }
            //销售信息
            AlibabaProductProductSaleInfo alibabaProductProductSaleInfo = alibabaProductProductInfo.getSaleInfo();

//            goodList.setRepertory(new BigDecimal(alibabaProductProductSaleInfo.getAmountOnSale())); //库存
            if (alibabaProductProductSaleInfo.getAmountOnSale() <= 0) {
                log.error("商品id：" + offerId + "因为没有库存被过滤！！！");
                return false;
            }


            BigDecimal addPrice = new BigDecimal(0);


            //查询以及区域列表

            AlibabaProductDeliveryRateDetailDTO a = null;

            boolean isenter = true;

            //物流信息
            AlibabaProductProductShippingInfo alibabaProductProductShippingInfo = alibabaProductProductInfo.getShippingInfo();


            if (alibabaProductProductShippingInfo == null) {
                log.error("错误商品id：" + offerId + "；过滤运费模板不存在的商品！！！");
                return false;
            }

            if (alibabaProductProductShippingInfo.getFreightTemplate() == null) {
                log.error("运费模板不存在id：" + offerId + "；过滤运费模板不存在的商品！！！");
                return false;
            }
            AlibabaProductDeliveryRateDetailDTO[] alibabaProductDeliveryRateDetailDTOS = alibabaProductProductShippingInfo.getFreightTemplate()[0].getExpressSubTemplate().getRateList();
            for (AlibabaProductDeliveryRateDetailDTO alibabaProductDeliveryRateDetailDTO : alibabaProductDeliveryRateDetailDTOS) {
                if (StringUtils.isNotBlank(alibabaProductDeliveryRateDetailDTO.getToAreaCodeText())) {
                    if (StringUtils.indexOf(alibabaProductDeliveryRateDetailDTO.getToAreaCodeText(), "贵阳") > -1) {
                        isenter = false;
                        //系统模板
                        if (alibabaProductDeliveryRateDetailDTO.getIsSysRate()) {
                            AlibabaProductDeliverySysRateDTO alibabaProductDeliverySysRateDTO = alibabaProductDeliveryRateDetailDTO.getSysRateDTO();
                            //包邮条件
                            if (alibabaProductDeliverySysRateDTO.getFirstUnitFee() == 0 && alibabaProductDeliverySysRateDTO.getNextUnitFee() == 0) {
                            } else {
                                //非包邮

                                BigDecimal price = new BigDecimal(alibabaProductDeliverySysRateDTO.getFirstUnitFee() + "").divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
                                if (price.subtract(addPrice).doubleValue() > 0) {
                                    addPrice = price;
                                }

                            }

                        } else {
                            //包邮条件
                            AlibabaProductDeliveryRateDTO alibabaProductDeliveryRateDTO = alibabaProductDeliveryRateDetailDTO.getRateDTO();
                            if (alibabaProductDeliveryRateDTO.getFirstUnitFee() == 0 && alibabaProductDeliveryRateDTO.getNextUnitFee() == 0) {

                            } else {
                                //非包邮
                                BigDecimal price = new BigDecimal(alibabaProductDeliveryRateDTO.getFirstUnitFee() + "").divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
                                if (price.subtract(addPrice).doubleValue() > 0) {
                                    addPrice = price;
                                }
                            }
                        }
                        break;
                    }
                } else {
                    a = alibabaProductDeliveryRateDetailDTO;
                }
            }
            if (isenter && a != null) {
                //系统模板
                if (a.getIsSysRate()) {
                    AlibabaProductDeliverySysRateDTO alibabaProductDeliverySysRateDTO = a.getSysRateDTO();
                    //包邮条件
                    if (alibabaProductDeliverySysRateDTO.getFirstUnitFee() == 0 && alibabaProductDeliverySysRateDTO.getNextUnitFee() == 0) {
                    } else {
                        //非包邮
                        BigDecimal price = new BigDecimal(alibabaProductDeliverySysRateDTO.getFirstUnitFee() + "").divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
                        if (price.subtract(addPrice).doubleValue() > 0) {
                            addPrice = price;
                        }
                    }

                } else {
                    //包邮条件
                    AlibabaProductDeliveryRateDTO alibabaProductDeliveryRateDTO = a.getRateDTO();
                    if (alibabaProductDeliveryRateDTO.getFirstUnitFee() == 0 && alibabaProductDeliveryRateDTO.getNextUnitFee() == 0) {

                    } else {
                        //非包邮
                        BigDecimal price = new BigDecimal(alibabaProductDeliveryRateDTO.getFirstUnitFee() + "").divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
                        if (price.subtract(addPrice).doubleValue() > 0) {
                            addPrice = price;
                        }
                    }
                }
            }


            log.info("增加邮费的价格：" + addPrice);


            goodList.setGoodNo(alibabaProductProductInfo.getProductID() + "");//商品编号

            List<Map<String, Object>> specificationMap = Lists.newArrayList();//整理商品列表需要的规格数据

            List<GoodSpecification> goodSpecifications = Lists.newArrayList();//规格列表数据


            //sku的具体信息
            AlibabaProductProductSKUInfo[] alibabaProductProductSKUInfos = alibabaProductProductInfo.getSkuInfos();
            if (alibabaProductProductSKUInfos != null && alibabaProductProductSKUInfos.length > 0) {
                //有规格
                goodList.setIsSpecification("1");
                //遍历规格数据
                for (AlibabaProductProductSKUInfo alibabaProductProductSKUInfo : alibabaProductProductSKUInfos) {
                    //判断库存
                    if (alibabaProductProductSKUInfo.getAmountOnSale() > 10) {

                        GoodSpecification goodSpecification = new GoodSpecification();
                        Double costPrice = null;//成本价
                        if (alibabaProductProductSKUInfo.getChannelPrice() != null) {
                            costPrice = alibabaProductProductSKUInfo.getChannelPrice();
                        }
                        if (costPrice == null) {
                            costPrice = alibabaProductProductSKUInfo.getConsignPrice();
                        }
                        if (costPrice == null || costPrice == 0) {
                            log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "因为没有规格的进货价价格被过滤！！！");
                            continue;
                        }
                        goodSpecification.setCostPrice(new BigDecimal(costPrice + "").add(addPrice));//商品成本价
                        //商品价格设置
                        goodSpecification.setPrice(goodSpecification.getCostPrice().divide(new BigDecimal(0.16), 2, RoundingMode.HALF_UP));

                        goodSpecification.setRepertory(new BigDecimal(alibabaProductProductSKUInfo.getAmountOnSale()));//库存
                        List<String> attributeNames = Lists.newArrayList();//属性描述
                        List<String> attributeValues = Lists.newArrayList();//属性值描述
                        //遍历属性
                        AlibabaProductSKUAttrInfo[] alibabaProductProductAttributes = alibabaProductProductSKUInfo.getAttributes();
                        for (AlibabaProductSKUAttrInfo alibabaProductSKUAttrInfo : alibabaProductProductAttributes) {
                            attributeNames.add(alibabaProductSKUAttrInfo.getAttributeName());
                            attributeValues.add(alibabaProductSKUAttrInfo.getAttributeValue());
                        }
                        if (specificationMap.size() > 0) {
                            List<Map<String, Object>> classification = (List<Map<String, Object>>) specificationMap.get(0).get("classification");
                            Map<String, Object> valueMap = Maps.newLinkedHashMap();
                            valueMap.put("value", StringUtils.join(attributeValues, "-"));
                            if (valueMap.get("value").toString().contains("定制")) {
                                log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "；规格信息含有敏感字而过滤！！！");
                                return false;
                            }

                            if (valueMap.get("value").toString().contains("客服")) {
                                log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "；规格信息含有敏感字而过滤！！！");
                                return false;
                            }
                            classification.add(valueMap);
                        } else {
                            Map<String, Object> commodityStyle = Maps.newLinkedHashMap();
                            commodityStyle.put("CommodityStyle", StringUtils.join(attributeNames, "-"));
                            List<Map<String, Object>> classification = Lists.newArrayList();
                            Map<String, Object> valueMap = Maps.newLinkedHashMap();
                            valueMap.put("value", StringUtils.join(attributeValues, "-"));
                            if (valueMap.get("value").toString().contains("定制")) {
                                log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "；规格信息含有敏感字而过滤！！！");
                                return false;
                            }

                            if (valueMap.get("value").toString().contains("客服")) {
                                log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "；规格信息含有敏感字而过滤！！！");
                                return false;
                            }
                            classification.add(valueMap);
                            commodityStyle.put("classification", classification);
                            specificationMap.add(commodityStyle);
                        }

                        goodSpecification.setSpecification(StringUtils.join(attributeValues, "-"));

                        if (goodSpecification.getSpecification().contains("定制")) {
                            log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "；规格信息含有敏感字而过滤！！！");
                            return false;
                        }

                        if (goodSpecification.getSpecification().contains("客服")) {
                            log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "；规格信息含有敏感字而过滤！！！");
                            return false;
                        }

                        goodSpecification.setSkuNo(alibabaProductProductSKUInfo.getSpecId());//sku编码
                        Long skuId = alibabaProductProductSKUInfo.getSkuId();
                        goodSpecification.setSkuId(Convert.toStr(skuId));
                        if (alibabaProductProductInfo.getShippingInfo() != null && alibabaProductProductInfo.getShippingInfo().getUnitWeight() != null) {
                            goodSpecification.setWeight(new BigDecimal(alibabaProductProductInfo.getShippingInfo().getUnitWeight() + ""));
                        }
                        goodSpecification.setSpecificationPicture(StringUtils.substringAfter(alibabaProductProductSKUInfo.getAttributes()[0].getSkuImageUrl(), baseUrl));//规格图
                        goodSpecifications.add(goodSpecification);

                    } else {
                        log.error("商品id：" + offerId + "；规格id：" + alibabaProductProductSKUInfo.getSpecId() + "；由于没有库存被过滤!!!");
                    }
                }


                goodList.setSpecification(JSON.toJSONString(specificationMap));//规格描述

            } else {
                //无规格
                //判断库存
                if (alibabaProductProductSaleInfo.getAmountOnSale() > 10) {
                    goodList.setIsSpecification("0");
                    goodList.setSpecification("");//规格数据
                    GoodSpecification goodSpecification = new GoodSpecification();
                    Double costPrice = null;//成本价
                    if (alibabaProductProductSaleInfo.getChannelPrice() != null) {
                        costPrice = alibabaProductProductSaleInfo.getChannelPrice();
                    }
                    if (costPrice == null) {
                        costPrice = alibabaProductProductSaleInfo.getConsignPrice();
                    }
                    if (costPrice == null) {
                        log.error("商品id：" + offerId + "因为没有无规格的进货价价格被过滤！！！");
                        return false;
                    }
                    goodSpecification.setCostPrice(new BigDecimal(costPrice + "").add(addPrice));//商品成本价
                    goodSpecification.setRepertory(new BigDecimal(alibabaProductProductSaleInfo.getAmountOnSale()));//库存
                    goodSpecification.setSpecification("无");//规格名称，按照顺序逗号隔开
                    goodSpecification.setSkuNo("无");//sku编码
                    if (alibabaProductProductInfo.getShippingInfo() != null && alibabaProductProductInfo.getShippingInfo().getUnitWeight() != null) {
                        goodSpecification.setWeight(new BigDecimal(alibabaProductProductInfo.getShippingInfo().getUnitWeight() + ""));
                    }
                    //商品价格
                    goodSpecification.setPrice(goodSpecification.getCostPrice().divide(new BigDecimal(0.16), 2, RoundingMode.HALF_UP));

                    //无规格没有规格图
                    goodSpecifications.add(goodSpecification);
                } else {
                    log.error("商品id：" + offerId + "；由于没有库存被过滤!!!");
                }

            }

            if (goodSpecifications.size() <= 0) {
                log.error("商品id：" + offerId + "因为过滤后不存在规格商品而被过滤！！！");
                return false;
            }

            goodList.setSourceType("4");//来源分类

            goodList.setSource("2");//来源

            log.info(JSON.toJSONString(goodList));
            log.info(JSON.toJSONString(goodSpecifications));
            //关注商品
            ali1688Utils.followGood(offerId);
            submitPrefectureGoods(JSON.toJSONString(goodList), JSON.toJSONString(goodSpecifications), marketingPrefectureTypeId);
        }
        return true;
    }

    @Override
    public void submitPrefectureGoods(String goodListJson, String goodSpecificationsJson, String marketingPrefectureTypeId) {
        //会员价比例 28%
        String membershipRate = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "会员价比例"), "%");
        //供货价比例 12%
        String supplyPriceRatio = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "供货价比例"), "%");


        //组织商品数据
        GoodList goodList = JSON.parseObject(goodListJson, GoodList.class);

        goodList.setStatus("1");//状态：0：停用；1：启用
        goodList.setFrameStatus("1");//上下架；0：下架；1：上架
        goodList.setFrameExplain("接口上架");//上下架说明
        goodList.setAuditStatus("2");//审核状态：0:草稿；1：待审核；2：审核通过；3：审核不通过
//        goodList.setGoodForm("0");//商品形态；0：常规商品；1：特价商品
        //解析规格
        List<GoodSpecification> goodSpecifications = JSON.parseArray(goodSpecificationsJson, GoodSpecification.class);


        GoodSpecification smallGoodSpecification = null;

        GoodSpecification bigGoodSpecification = null;


        //设置计算规格需要的价格
        for (GoodSpecification goodSpecification : goodSpecifications) {
            goodSpecification.setSupplyPrice(goodSpecification.getCostPrice().multiply(new BigDecimal(1).add(new BigDecimal(supplyPriceRatio).divide(new BigDecimal(100)))).setScale(2, RoundingMode.DOWN));//供货价
            goodSpecification.setVipPrice(goodSpecification.getPrice().subtract(goodSpecification.getSupplyPrice()).multiply(new BigDecimal(membershipRate).divide(new BigDecimal(100))).add(goodSpecification.getSupplyPrice()).setScale(2, RoundingMode.DOWN));//vip价格
            //获取最小价格的规格
            if (smallGoodSpecification == null) {
                smallGoodSpecification = goodSpecification;
            } else {
                if (smallGoodSpecification.getPrice().subtract(goodSpecification.getPrice()).doubleValue() > 0) {
                    smallGoodSpecification = goodSpecification;
                }
            }
            //获取最大价格的规格
            if (bigGoodSpecification == null) {
                bigGoodSpecification = goodSpecification;
            } else {
                if (bigGoodSpecification.getPrice().subtract(goodSpecification.getPrice()).doubleValue() < 0) {
                    bigGoodSpecification = goodSpecification;
                }
            }
        }
        /*//最低商品价格
        goodList.setSmallPrice(smallGoodSpecification.getPrice()+"");
        //最低vip价格
        goodList.setSmallVipPrice(smallGoodSpecification.getVipPrice()+"");
        //最低成本价
        goodList.setCostPrice(smallGoodSpecification.getCostPrice()+"");
        //最低供货价
        goodList.setSmallSupplyPrice(smallGoodSpecification.getSupplyPrice()+"");

        goodList.setSmallCostPrice(smallGoodSpecification.getCostPrice()+"");*/

/*
        if(smallGoodSpecification.getPrice().subtract(bigGoodSpecification.getPrice()).doubleValue()<0){
            goodList.setPriceRange("1");
        }else{
            goodList.setPriceRange("0");
        }

        if(goodList.getPriceRange().equals("0")) {
            goodList.setPrice(smallGoodSpecification.getPrice().setScale(2,RoundingMode.HALF_UP)+"");
            goodList.setSupplyPrice(smallGoodSpecification.getSupplyPrice()+"");
            goodList.setVipPrice(smallGoodSpecification.getVipPrice()+"");
            goodList.setCostPrice(smallGoodSpecification.getCostPrice()+"");
        }else{
            goodList.setPrice(smallGoodSpecification.getPrice().setScale(2,RoundingMode.HALF_UP)+"--"+bigGoodSpecification.getPrice().setScale(2,RoundingMode.HALF_UP));
            goodList.setSupplyPrice(smallGoodSpecification.getSupplyPrice()+"--"+bigGoodSpecification.getSupplyPrice());
            goodList.setVipPrice(smallGoodSpecification.getVipPrice()+"--"+bigGoodSpecification.getVipPrice());
            goodList.setCostPrice(smallGoodSpecification.getCostPrice()+"--"+bigGoodSpecification.getCostPrice());
        }*/


        String marketPrice = bigGoodSpecification.getPrice().multiply(new BigDecimal(1.1)).setScale(2, RoundingMode.DOWN) + "";//商品市场价
        goodList.setMarketPrice(marketPrice);

        //设置运费模板
        goodList.setProviderTemplateId("324dd14ecbb9f3d0f83215782c3607db");

        //查询商品是否存在
        QueryWrapper<GoodList> goodListQueryWrapper = new QueryWrapper<>();
        goodListQueryWrapper.eq("good_no", goodList.getGoodNo());
        if (goodListService.count(goodListQueryWrapper) > 0) {
            GoodList goodListSrc = goodListService.getOne(goodListQueryWrapper);
            goodList.setMainPicture(goodListSrc.getMainPicture());
            goodList.setGoodDescribe(goodListSrc.getGoodDescribe());
            goodList.setGoodName(goodListSrc.getGoodName());
            goodList.setFrameStatus(goodListSrc.getFrameStatus());
            goodList.setId(goodListSrc.getId());
        }

        //保存商品
        goodListService.saveOrUpdate(goodList);

        //保存规格
        for (GoodSpecification goodSpecification : goodSpecifications) {
            //设置商品id
            goodSpecification.setGoodListId(goodList.getId());
            if (goodSpecificationService.count(new LambdaQueryWrapper<GoodSpecification>().eq(GoodSpecification::getSpecification, goodSpecification.getSpecification()).eq(GoodSpecification::getGoodListId, goodSpecification.getGoodListId())) != 0) {
                goodSpecification.setId(goodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>().eq(GoodSpecification::getSpecification, goodSpecification.getSpecification()).eq(GoodSpecification::getGoodListId, goodSpecification.getGoodListId())).getId());
            }
            goodSpecificationService.saveOrUpdate(goodSpecification);
        }
    }

    @Override
    public SDKResult<AlibabaCpsMediaProductInfoResult> getProductInfoResult(Long offerId) {
        //获取商品详情
        AlibabaCpsMediaProductInfoParam alibabaCpsMediaProductInfoParam = new AlibabaCpsMediaProductInfoParam();
        alibabaCpsMediaProductInfoParam.setNeedCpsSuggestPrice(true);
        alibabaCpsMediaProductInfoParam.setNeedIntelligentInfo(true);
        alibabaCpsMediaProductInfoParam.setOfferId(offerId);
        SDKResult<AlibabaCpsMediaProductInfoResult> productInfoResult = ali1688Utils.getApiExecutor().execute(alibabaCpsMediaProductInfoParam, ali1688Utils.createToken());
//        log.info("采集的商品信息：" + JSON.toJSONString(productInfoResult.getResult()));
        return productInfoResult;
    }

    @Override
    public SDKResult<AlibabaTradeGetBuyerViewResult> getAlibabaTradeGetBuyerViewResult(Long taobaoOrderId) {
        //获取1688订单详情
        AlibabaTradeGetBuyerViewParam alibabaTradeGetBuyerViewParam = new AlibabaTradeGetBuyerViewParam();
        alibabaTradeGetBuyerViewParam.setOrderId(taobaoOrderId);
        alibabaTradeGetBuyerViewParam.setWebSite("1688");
        SDKResult<AlibabaTradeGetBuyerViewResult> result = ali1688Utils.getApiExecutor()
                .execute(alibabaTradeGetBuyerViewParam, ali1688Utils.createToken());
        if (!result.getResult().getSuccess().equals("true")) {
            return null;
        }
        return result;
    }

    @Override
    public AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo getAlibabaOpenplatformTradeModelNativeLogisticsItemsInfo(Long taobaoOrderId) {
        SDKResult<AlibabaTradeGetBuyerViewResult> result = getAlibabaTradeGetBuyerViewResult(taobaoOrderId);
        if (result == null) {
            return null;
        }
        //获取物流信息
        AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo[] logisticsItemsInfos = Optional.ofNullable(result).map(SDKResult::getResult)
                .map(AlibabaTradeGetBuyerViewResult::getResult)
                .map(AlibabaOpenplatformTradeModelTradeInfo::getNativeLogistics)
                .map(AlibabaOpenplatformTradeModelNativeLogisticsInfo::getLogisticsItems)
                .orElse(null);
        return ArrayUtil.get(logisticsItemsInfos, 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSkuId() {
        LambdaQueryWrapper<GoodList> goodListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        goodListLambdaQueryWrapper.eq(GoodList::getSysUserId, "220ebc6c395bdb6cfc7c5b721746a79c")
                .eq(GoodList::getDelFlag, "0")
                .eq(GoodList::getIsSpecification, "1")
                .orderByDesc(GoodList::getCreateTime);
        int pageNo = 1;
        while (true) {
            //分页查询商品列表
            IPage<GoodList> page = new Page<>(pageNo, 30, false);
            List<GoodList> goodLists = goodListService.page(page, goodListLambdaQueryWrapper).getRecords();
            log.info("平台商品信息：pageNo=" + pageNo + "页数量" + goodLists.size());
            if (CollUtil.isEmpty(goodLists)) {
                break;
            }
            List<String> goodIds = goodLists.stream().map(GoodList::getId).collect(Collectors.toList());
            //查询对应规格列表
            LambdaQueryWrapper<GoodSpecification> goodSpecificationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            goodSpecificationLambdaQueryWrapper.in(GoodSpecification::getGoodListId, goodIds);
            Map<String, List<GoodSpecification>> goodSpeListMap = goodSpecificationService.list(goodSpecificationLambdaQueryWrapper).stream().collect(Collectors.groupingBy(GoodSpecification::getGoodListId));
            for (GoodList goodList : goodLists) {
                Long offerId = Convert.toLong(goodList.getGoodNo());
                ThreadUtil.sleep(RandomUtil.randomDouble(1, 3), TimeUnit.SECONDS);
                SDKResult<AlibabaCpsMediaProductInfoResult> productInfoResult = getProductInfoResult(offerId);

                if (productInfoResult.getResult() == null) {
                    log.info("商品不存在" + offerId);
                    continue;
                }
                if (!productInfoResult.getResult().getSuccess()) {
                    log.error("错误商品id：" + offerId + "；错误代码：" + productInfoResult.getResult().getErrorCode() + "；错误信息：" + productInfoResult.getResult().getErrorMsg());
                    continue;
                }
                List<GoodSpecification> goodSpecifications = goodSpeListMap.getOrDefault(goodList.getId(), CollUtil.newArrayList());

                AlibabaProductProductInfo alibabaProductProductInfo = productInfoResult.getResult().getProductInfo();
                //sku的具体信息
                AlibabaProductProductSKUInfo[] alibabaProductProductSKUInfos = alibabaProductProductInfo.getSkuInfos();
                if (ArrayUtil.isEmpty(alibabaProductProductSKUInfos)) {
                    log.info("没有具体规格信息");
                    continue;
                }
                Map<String, AlibabaProductProductSKUInfo> productProductSKUInfoMap = Arrays.stream(alibabaProductProductSKUInfos).collect(Collectors.toMap(AlibabaProductProductSKUInfo::getSpecId, alibabaProductProductSKUInfo -> alibabaProductProductSKUInfo));

                List<GoodSpecification> updateGoodSpecifications = goodSpecifications.stream().filter(goodSpecification -> productProductSKUInfoMap.containsKey(goodSpecification.getSkuNo())).peek(goodSpecification -> {
                    AlibabaProductProductSKUInfo alibabaProductProductSKUInfo = productProductSKUInfoMap.get(goodSpecification.getSkuNo());
                    goodSpecification.setSkuId(Convert.toStr(alibabaProductProductSKUInfo.getSkuId()));
                }).collect(Collectors.toList());

                if (CollUtil.isNotEmpty(updateGoodSpecifications)) {
                    goodSpecificationService.updateBatchById(updateGoodSpecifications);
                }
            }
//            if (pageNo == 20) {
//                break;
//            }
            pageNo++;
        }
    }
}
