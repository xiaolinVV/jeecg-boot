package org.jeecg.modules.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.trade.param.AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.gongke.HttpUtils;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.dto.OrderEvaluateDTO;
import org.jeecg.modules.order.dto.OrderListDTO;
import org.jeecg.modules.order.dto.OrderProviderGoodRecordDTO;
import org.jeecg.modules.order.dto.OrderProviderListDTO;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.mapper.*;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.jeecg.modules.order.vo.OrderProviderListVO;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.mapper.ProviderTemplateMapper;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.taobao.service.IAli1688Service;
import org.jeecg.modules.taobao.utils.Ali1688Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 供应商订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Service
@Slf4j
public class OrderProviderListServiceImpl extends ServiceImpl<OrderProviderListMapper, OrderProviderList> implements IOrderProviderListService {

    @Autowired
    private IOrderProviderGoodRecordService iOrderProviderGoodRecordService;

    @Autowired
    private ISysDictService sysDictService;

    @Resource
    private OrderProviderGoodRecordMapper orderProviderGoodRecordMapper;
    @Resource
    private OrderListMapper orderListMapper;
    @Resource
    private OrderProviderTemplateMapper orderProviderTemplateMapper;
    @Resource
    private ProviderTemplateMapper providerTemplateMapper;

    @Resource
    private OrderEvaluateMapper orderEvaluateMapper;

    @Autowired
    private IProviderManageService iProviderManageService;

    @Autowired
    private Ali1688Utils ali1688Utils;

    @Autowired
    @Lazy
    private IOrderListService iOrderListService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IAli1688Service ali1688Service;

    @Autowired
    private IOrderRefundListService orderRefundListService;


    @Override
    public List<OrderProviderListDTO> selectorderListId(String orderListId, String sysUserId, String parentId, String notParentId) {
        return baseMapper.selectorderListId(orderListId, sysUserId, parentId, notParentId);
    }

    /**
     * 物流信息查询接口
     *
     * @param orderProviderId
     * @return
     */
    @Override
    public String listSkip(String orderProviderId) {
        OrderProviderList orderProviderList = baseMapper.selectById(orderProviderId);//getById(orderProviderId)
        if (orderProviderList != null) {
            //全国快递物流查询 - 快递查询接口：https://market.aliyun.com/products/57126001/cmapi021863.html#sku=yuncode1586300000
            String host = "https://wuliu.market.alicloudapi.com";
            String path = "/kdi";
            String method = "GET";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            List<DictModel> ls = sysDictService.queryDictItemsByCode("logistics_query_config");
            if (ls.size() > 0) {
                headers.put("Authorization", "APPCODE " + ls.get(0).getValue());//ls.get(0).getValue()
            }
            // headers.put("Authorization", "APPCODE " + appcode);
            Map<String, String> querys = new HashMap<String, String>();
            //送货上门
            if ("SHSM".equals(orderProviderList.getLogisticsCompany())) {
                return "{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"无\",\"type\":\"SHSM\",\"list\":[{\"time\":\"无\",\"status\":\"送货上门\"}],\"deliverystatus\":\"3\",\"issign\":\"1\",\"expName\":\"送货上门\",\"expSite\":\"www.yto.net.cn \",\"expPhone\":\"95554\",\"logo\":\"http:\\/\\/img3.fegine.com\\/express\\/yto.jpg\",\"courier\":\"\",\"courierPhone\":\"\",\"updateTime\":\"无\",\"takeTime\":\"无\"}}";
            } else {
                String string = null;
                if (StringUtils.isBlank(orderProviderList.getTaoOrderId())) {
                    querys.put("no", orderProviderList.getTrackingNumber());// !!! 请求参数 快递单号tracking_number orderProviderList.getTrackingNumber()
                    querys.put("type", orderProviderList.getLogisticsCompany());// !!! 请求参数 快递名称对应缩写 logistics_company orderProviderList.getLogisticsCompany()
                    try {
                        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
                        string = EntityUtils.toString(response.getEntity());
                        JSONObject jsonObjectHttp = JSONObject.parseObject(string);

                        if (jsonObjectHttp.get("status").equals("0")) {
                            //已签收
                            JSONObject jsonObjectResult = JSONObject.parseObject(jsonObjectHttp.get("result").toString());
                            if ("1".equals(jsonObjectResult.get("issign"))) {
                                orderProviderList.setStatus("3");
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    string = JSON.toJSONString(ali1688Utils.getLogisticsTraceInfo(Long.parseLong(orderProviderList.getTaoOrderId())));
                }
                //添加物流数据
                orderProviderList.setLogisticsTracking(string);
                baseMapper.updateById(orderProviderList);
                return string;

            }

        }
        return null;
    }

    @Override
    public Map<String, Object> getOrderProviderLisById(String id) {
        return baseMapper.getOrderProviderLisById(id);
    }

    @Override
    public IPage<OrderProviderListDTO> queryPageList(Page<OrderProviderList> page, OrderProviderListVO orderProviderListVO, String userId) throws IllegalAccessException {

        orderProviderListVO.setParentId("0");
        orderProviderListVO.setDelFlag("0");
        //获取供应商主单信息
        IPage<OrderProviderListDTO> orderProviderListDTOs = baseMapper.queryPageList(page, orderProviderListVO);

        for (OrderProviderListDTO orderProviderListDTO : orderProviderListDTOs.getRecords()) {
            //获取供应商子单信息
            List<OrderProviderListDTO> orderProviderLists = baseMapper.selectByParentId(orderProviderListDTO.getId());


            OrderProviderList orderProviderList1 = this.getById(orderProviderListDTO.getId());
            if (StringUtils.isNotBlank(orderProviderList1.getTaoOrderId())) {
                //下单状态
                long count = iOrderProviderGoodRecordService.count(new LambdaQueryWrapper<OrderProviderGoodRecord>().eq(OrderProviderGoodRecord::getOrderProviderListId, orderProviderListDTO.getId()).isNull(OrderProviderGoodRecord::getTaoOrderId));
                if (count > 0) {
                    orderProviderListDTO.setOrderStatus("1");
                } else {
                    orderProviderListDTO.setOrderStatus("2");
                }
            } else {
                orderProviderListDTO.setOrderStatus("0");
            }

            List<OrderEvaluateDTO> orderEvaluateDTOS = new ArrayList<>();
            for (OrderProviderListDTO orderProviderList : orderProviderLists) {
                //获取子单商品项信息
                QueryWrapper<OrderProviderGoodRecord> orderProviderListQueryWrapper = new QueryWrapper<>();
                orderProviderListQueryWrapper.eq("order_provider_list_id", orderProviderList.getId());

                List<OrderProviderGoodRecordDTO> orderProviderGoodRecords = orderProviderGoodRecordMapper.selectByOrderProviderListId(orderProviderList.getId());
                orderProviderList.setOrderProviderGoodRecordDTOList(orderProviderGoodRecords);

                //获取评价信息
                if (orderProviderList.getStatus() != null && orderProviderList.getStatus().equals("3") || orderProviderList.getStatus().equals("5")) {
                    List<OrderEvaluateDTO> list = orderEvaluateMapper.selectByOderProviderListId(orderProviderList.getId());
                    orderEvaluateDTOS.addAll(list);
                }
            }
            orderProviderListDTO.setOrderEvaluateDTOList(orderEvaluateDTOS);
            orderProviderListDTO.setOrderProviderListDTOs(orderProviderLists);
            //获取主单下的商品
            List<OrderProviderGoodRecordDTO> orderProviderGoodRecords = orderProviderGoodRecordMapper.selectByOrderProviderListId(orderProviderListDTO.getId());
            orderProviderListDTO.setOrderProviderGoodRecordDTOList(orderProviderGoodRecords);

            //获取平台订单信息
            OrderListDTO orderListDTO = orderListMapper.selectOrderListById(orderProviderListDTO.getOrderListId());
            orderListDTO.setMemberList(iMemberListService.getById(orderListDTO.getMemberListId()));
            orderProviderListDTO.setOrderList(orderListDTO);
            //获取运费模板
            List<Map<String, Object>> orderProviderTemplateMap = orderProviderTemplateMapper.getOrderProviderTemplateMap(orderProviderListDTO.getId());
            if (CollUtil.isEmpty(orderProviderTemplateMap)) {
                orderProviderTemplateMap = providerTemplateMapper.getProviderTemplateMap(orderProviderListDTO.getId());
            }
            orderProviderListDTO.setProviderTemplateMaps(orderProviderTemplateMap);
        }


        return orderProviderListDTOs;
    }

    @Override
    @Transactional
    public void taobaoShipments(String taobaoOrderId) {
        OrderProviderGoodRecord orderProviderGoodRecord = iOrderProviderGoodRecordService.getOne(new LambdaQueryWrapper<OrderProviderGoodRecord>()
                .eq(OrderProviderGoodRecord::getTaoOrderId, taobaoOrderId)
                .orderByDesc(OrderProviderGoodRecord::getCreateTime)
                .last("limit 1"));
        log.info("进入发货状态：" + taobaoOrderId);
        if (orderProviderGoodRecord == null) {
            return;
        }
        LambdaQueryWrapper<OrderRefundList> orderRefundListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderRefundListLambdaQueryWrapper
                .eq(OrderRefundList::getOrderGoodRecordId, orderProviderGoodRecord.getId())
                .in(OrderRefundList::getRefundType, "0", "1")
                .in(OrderRefundList::getStatus, "0", "1", "2", "3", "4", "5");
        if (orderRefundListService.count(orderRefundListLambdaQueryWrapper) > 0) {
            return;
        }
        OrderProviderList orderProviderList = this.getById(orderProviderGoodRecord.getOrderProviderListId());
        //获取物流信息
        AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo itemsInfo = ali1688Service.getAlibabaOpenplatformTradeModelNativeLogisticsItemsInfo(Convert.toLong(taobaoOrderId));
        if (itemsInfo != null) {
            orderProviderList.setParentId(orderProviderList.getId())
                    .setId(null)
                    .setLogisticsCompany(itemsInfo.getLogisticsCompanyNo())
                    .setTrackingNumber(itemsInfo.getLogisticsBillNo())
                    .setStatus("2")
                    .setIsSend("1")
                    .setTaoOrderId(taobaoOrderId)
                    .setAutoDelivery("1");
            this.save(orderProviderList);

            iOrderProviderGoodRecordService.saveOrUpdate(orderProviderGoodRecord.setOrderProviderListId(orderProviderList.getId()));
            //判断订单的状态
            long count = iOrderProviderGoodRecordService.count(new LambdaQueryWrapper<OrderProviderGoodRecord>().eq(OrderProviderGoodRecord::getOrderProviderListId, orderProviderList.getParentId()));
            OrderList orderList = iOrderListService.getById(orderProviderList.getOrderListId());
            OrderProviderList orderProviderList1 = this.getById(orderProviderList.getParentId());
            if (orderList.getShipmentsTime() == null) {
                orderList.setShipmentsTime(new Date());
            }
            //直接发货
            if (count == 0) {
                orderList.setStatus("2");
                orderProviderList1.setStatus("2");
                orderList.setIsSender("0");
                iOrderListService.saveOrUpdate(orderList);
                this.saveOrUpdate(orderProviderList1);
            }
            //部分发货
            if (count > 0) {
                orderList.setIsSender("1");
                iOrderListService.saveOrUpdate(orderList);
            }
        }

    }


    @Override
    public List<OrderProviderListDTO> selectByParentId(String orderListId) {
        return baseMapper.selectByParentId(orderListId);
    }

    @Override
    public void ShipmentOrderModification(OrderProviderList orderProviderList) {
        OrderList orderList = orderListMapper.selectById(orderProviderList.getOrderListId());
        // 修复订单表首次发货时间 shipments_time 为空，导致获取自动收货倒计时接口报错问题---------- by 张少林
        if (orderList.getShipmentsTime() == null) {
            orderList.setShipmentsTime(new Date());
        }
        LambdaQueryWrapper<OrderProviderGoodRecord> goodRecordLambdaQueryWrapper = new LambdaQueryWrapper<OrderProviderGoodRecord>()
                .eq(OrderProviderGoodRecord::getDelFlag, "0")
                .eq(OrderProviderGoodRecord::getOrderProviderListId, orderProviderList.getId());
        if (orderProviderGoodRecordMapper.selectCount(goodRecordLambdaQueryWrapper) <= 0) {
            this.updateById(orderProviderList.setStatus("2"));

            LambdaQueryWrapper<OrderProviderList> orderProviderListLambdaQueryWrapper = new LambdaQueryWrapper<OrderProviderList>()
                    .eq(OrderProviderList::getDelFlag, "0")
                    .eq(OrderProviderList::getOrderListId, orderList.getId())
                    .eq(OrderProviderList::getParentId, "0");
            if (this.count(orderProviderListLambdaQueryWrapper) == this.count(orderProviderListLambdaQueryWrapper.eq(OrderProviderList::getStatus, "2"))) {
                orderList.setStatus("2");
                orderListMapper.updateById(orderList);
            } else {
                orderList.setIsSender("1");
                iOrderListService.saveOrUpdate(orderList);
            }
        } else {
            goodRecordLambdaQueryWrapper.in(OrderProviderGoodRecord::getStatus, "0", "2", "4", "5");
            if (orderProviderGoodRecordMapper.selectCount(goodRecordLambdaQueryWrapper) > 0) {
                orderList.setIsSender("1");
                iOrderListService.saveOrUpdate(orderList);
            } else {
                this.updateById(orderProviderList.setStatus("2"));
                orderList.setStatus("2");
                orderListMapper.updateById(orderList);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getOrderProviderListAndGoodListByOrderId(String orderListId) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        //获取供应商订单信息
        baseMapper.selectList(new LambdaQueryWrapper<OrderProviderList>()
                .eq(OrderProviderList::getOrderListId, orderListId)).forEach(op -> {
            Map<String, Object> opMap = Maps.newHashMap();
            opMap.put("providerManageName", iProviderManageService.getOne(new LambdaQueryWrapper<ProviderManage>().eq(ProviderManage::getSysUserId, op.getSysUserId())).getName());//供应商名称
            opMap.put("orderNo", op.getOrderNo());//供应商订单编号
            List<Map<String, Object>> goodMapList = Lists.newArrayList();
            //获取供应商订单的商品
            iOrderProviderGoodRecordService.list(new LambdaQueryWrapper<OrderProviderGoodRecord>()
                    .eq(OrderProviderGoodRecord::getOrderProviderListId, op.getId())).forEach(opl -> {
                Map<String, Object> orderProviderListMap = Maps.newHashMap();
                orderProviderListMap.put("goodNo", opl.getGoodNo());//商品编号
                orderProviderListMap.put("mainPicture", opl.getMainPicture());
                orderProviderListMap.put("goodName", opl.getGoodName());
                orderProviderListMap.put("specification", opl.getSpecification());
                orderProviderListMap.put("unitPrice", opl.getUnitPrice());
                orderProviderListMap.put("amount", opl.getAmount());
                orderProviderListMap.put("total", opl.getTotal());
                orderProviderListMap.put("weight", opl.getWeight());
                goodMapList.add(orderProviderListMap);
            });
            opMap.put("goodMapList", goodMapList);
            mapList.add(opMap);
        });
        return mapList;
    }

    @Override
    public Map<String, BigDecimal> placeOrder(String orderProviderListIds) {
        Map<String, BigDecimal> resultObjectMap = Maps.newHashMap();
        BigDecimal successCount = BigDecimal.ZERO;
        BigDecimal errorCount = BigDecimal.ZERO;
        resultObjectMap.put("successCount", successCount);
        resultObjectMap.put("errorCount", errorCount);
        Arrays.asList(StringUtils.split(orderProviderListIds, ",")).forEach(orderProviderListId -> {
            Map<String, Object> orderProviderListMap = this.getOrderProviderLisById(orderProviderListId);
            if (orderProviderListMap != null) {
                OrderProviderList orderProviderList = this.getById(orderProviderListId);
                if (StringUtils.isNotBlank(orderProviderList.getTaoOrderId())) {
                    log.info("本供应商订单已经下过单：" + orderProviderListId);
                    resultObjectMap.put("errorCount", resultObjectMap.get("errorCount").add(BigDecimal.ONE));
                } else {
                    iOrderProviderGoodRecordService.getGoodListByOrderProviderListId(orderProviderListId).forEach(goodListMap -> {
                        if (goodListMap.get("skuNo").equals("无")) {
                            goodListMap.put("skuNo", "");
                        }
                        if (goodListMap.get("isSend").equals("0")) {
                            Map<String, Object> resultMap = ali1688Utils.placeOrder(orderProviderListMap, goodListMap);
                            if (resultMap.get("code").equals("1")) {
                                //下单成功
                                OrderProviderGoodRecord orderProviderGoodRecord = iOrderProviderGoodRecordService.getById(goodListMap.get("id").toString());
                                orderProviderGoodRecord.setTaoOrderId(resultMap.get("taoOrderId").toString());
                                orderProviderGoodRecord.setIsSend("1");
                                orderProviderGoodRecord.setMessage(String.valueOf(resultMap.get("message")));
                                iOrderProviderGoodRecordService.saveOrUpdate(orderProviderGoodRecord);
                                resultObjectMap.put("successCount", resultObjectMap.get("successCount").add(BigDecimal.ONE));
                                if (StringUtils.isBlank(orderProviderList.getTaoOrderId())) {
                                    orderProviderList.setTaoOrderId(resultMap.get("taoOrderId").toString());
                                } else {
                                    orderProviderList.setTaoOrderId(orderProviderList.getTaoOrderId() + "," + resultMap.get("taoOrderId").toString());
                                }
                                this.saveOrUpdate(orderProviderList);
                            } else {
                                //下单不成
                                OrderProviderGoodRecord orderProviderGoodRecord = iOrderProviderGoodRecordService.getById(goodListMap.get("id").toString());
                                orderProviderGoodRecord.setIsSend("2");
                                orderProviderGoodRecord.setMessage(String.valueOf(resultMap.get("message")));
                                iOrderProviderGoodRecordService.saveOrUpdate(orderProviderGoodRecord);
                                resultObjectMap.put("errorCount", resultObjectMap.get("errorCount").add(BigDecimal.ONE));
                            }
                        } else {
                            resultObjectMap.put("errorCount", resultObjectMap.get("errorCount").add(BigDecimal.ONE));
                        }
                    });
                }
            } else {
                resultObjectMap.put("errorCount", resultObjectMap.get("errorCount").add(BigDecimal.ONE));
            }
        });
        return resultObjectMap;
    }

    @Override
    public Map<String, BigDecimal> replenishment(String orderProviderGoodRecordId) {
        Map<String, BigDecimal> resultObjectMap = Maps.newHashMap();
        BigDecimal successCount = BigDecimal.ZERO;
        BigDecimal errorCount = BigDecimal.ZERO;
        resultObjectMap.put("successCount", successCount);
        resultObjectMap.put("errorCount", errorCount);

        OrderProviderList orderProviderList = this.getById(iOrderProviderGoodRecordService.getById(orderProviderGoodRecordId).getOrderProviderListId());

        Map<String, Object> orderProviderListMap = this.getOrderProviderLisById(orderProviderList.getId());
        Map<String, Object> goodListMap = iOrderProviderGoodRecordService.iOrderProviderGoodRecordById(orderProviderGoodRecordId);
        if (goodListMap.get("skuNo").equals("无")) {
            goodListMap.put("skuNo", "");
        }
        if (!goodListMap.get("isSend").equals("1")) {
            Map<String, Object> resultMap = ali1688Utils.placeOrder(orderProviderListMap, goodListMap);
            if (resultMap.get("code").equals("1")) {
                //下单成功
                OrderProviderGoodRecord orderProviderGoodRecord = iOrderProviderGoodRecordService.getById(goodListMap.get("id").toString());
                orderProviderGoodRecord.setTaoOrderId(resultMap.get("taoOrderId").toString());
                orderProviderGoodRecord.setIsSend("1");
                orderProviderGoodRecord.setMessage(String.valueOf("message"));
                iOrderProviderGoodRecordService.saveOrUpdate(orderProviderGoodRecord);
                resultObjectMap.put("successCount", resultObjectMap.get("successCount").add(BigDecimal.ONE));
                if (StringUtils.isBlank(orderProviderList.getTaoOrderId())) {
                    orderProviderList.setTaoOrderId(resultMap.get("taoOrderId").toString());
                } else {
                    orderProviderList.setTaoOrderId(orderProviderList.getTaoOrderId() + "," + resultMap.get("taoOrderId").toString());
                }
                this.saveOrUpdate(orderProviderList);
            } else {
                //下单不成
                OrderProviderGoodRecord orderProviderGoodRecord = iOrderProviderGoodRecordService.getById(goodListMap.get("id").toString());
                orderProviderGoodRecord.setIsSend("2");
                orderProviderGoodRecord.setMessage(String.valueOf("message"));
                iOrderProviderGoodRecordService.saveOrUpdate(orderProviderGoodRecord);
                resultObjectMap.put("errorCount", resultObjectMap.get("errorCount").add(BigDecimal.ONE));
            }
        } else {
            resultObjectMap.put("errorCount", resultObjectMap.get("errorCount").add(BigDecimal.ONE));
        }
        return resultObjectMap;
    }

}
