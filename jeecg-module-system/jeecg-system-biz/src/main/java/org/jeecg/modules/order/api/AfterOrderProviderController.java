package org.jeecg.modules.order.api;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.dto.OrderProviderListDTO;
import org.jeecg.modules.order.dto.OrderStoreSubListDTO;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.*;
import org.jeecg.modules.provider.dto.ProviderAddressDTO;
import org.jeecg.modules.provider.service.IProviderAddressService;
import org.jeecg.modules.store.dto.StoreAddressDTO;
import org.jeecg.modules.store.service.IStoreAddressService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
@RequestMapping("after/orderProvider")
@Controller
@Slf4j
public class AfterOrderProviderController {
    @Autowired
    private IOrderProviderListService orderProviderListService;
    @Autowired
        private ISysUserService sysUserService;
    @Autowired
    private IProviderAddressService providerAddressService;
    @Autowired
    private IOrderProviderGoodRecordService orderProviderGoodRecordService;
    @Autowired
    private IMemberListService memberListService;
    @Autowired
    private IOrderStoreSubListService orderStoreSubListService;
    @Autowired
    private IOrderStoreListService orderStoreListService;
    @Autowired
    private IOrderStoreGoodRecordService orderStoreGoodRecordService;
    @Autowired
    private IStoreAddressService storeAddressService;
    @Autowired
    private ISysDictService iSysDictService;
    /**
     * 包裹信息
     * @param orderListId
     * @return
     */
    @RequestMapping("parcelInformation")
    @ResponseBody
    public Result<Map<String,Object>> parcelInformation(String orderListId,
                                                        Integer isPlatform,
                                                        HttpServletRequest request){
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();

        Map<String,Object> objectMap= Maps.newHashMap();
        String memberId=request.getAttribute("memberId").toString();
        //数据封装用
         List<Map<String,Object>> listMap = Lists.newArrayList();
        if(StringUtils.isBlank(orderListId)){
            result.error500("id不能为空！！！");
            return result;
        }
        //参数校验
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }
        MemberList memberList = memberListService.getById(memberId);
        if(isPlatform.intValue()==0){
            OrderStoreList orderStoreList = orderStoreListService.getById(orderListId);
            String UserId = orderStoreList.getSysUserId();
            //店铺包裹
            List<OrderStoreSubListDTO>  orderStoreSubLists = orderStoreSubListService.selectorderStoreListId(orderListId,UserId,null,"0");
            if(orderStoreSubLists.size()>0){
                orderStoreSubLists.forEach(opl -> {
                    SysUser sysUser = sysUserService.getById(opl.getSysUserId());
                    if(sysUser!=null){
                        opl.setSysUserName(sysUser.getRealname());
                    }
                    //获取物流数据JSON
                    //获取物流数据JSON
                    if( StringUtils.isNotBlank(opl.getLogisticsTracking())){
                        JSONObject jsonObject = JSONObject.parseObject(opl.getLogisticsTracking());
                        if(jsonObject.get("status").equals("0")){
                            //已签收
                            JSONObject jsonObjectResult = JSONObject.parseObject(jsonObject.get("result").toString());

                            if(jsonObjectResult.get("issign").equals("1")){
                                //不做查询物流接口
                            }else{
                                //请求接口更新数据接口
                                String string = orderStoreSubListService.listSkip(opl.getId());
                                opl.setLogisticsTracking(string);
                            }
                        }
                    }else{
                        String string = orderStoreSubListService.listSkip(opl.getId());
                        opl.setLogisticsTracking(string);
                    }
                    //供应商发货信息
                    Map<String,String> paramMap = Maps.newHashMap();
                    paramMap.put("id",opl.getStoreAddressIdSender());
                    if(opl.getStoreAddressIdSender()==null || "".equals( opl.getStoreAddressIdSender())){
                        paramMap.put("sysUserId",opl.getSysUserId());
                        paramMap.put("isDeliver","1");//发货默认
                        paramMap.put("isReturn","");//退货
                    }
                    List<StoreAddressDTO> listStoreAddressDTO= storeAddressService.getlistStoreAddress(paramMap);
                    if(listStoreAddressDTO.size()>0){
                        opl.setStoreAddressDTOFa(listStoreAddressDTO.get(0));
                    }
                    //供应商退信息
                    Map<String,String> paramMaptui = Maps.newHashMap();
                    paramMaptui.put("id",opl.getStoreAddressIdTui());
                    if(opl.getStoreAddressIdTui()== null || "".equals(opl.getStoreAddressIdTui())){
                        paramMaptui.put("sysUserId",opl.getSysUserId());
                        paramMaptui.put("isDeliver","");//发货默认
                        paramMaptui.put("isReturn","1");//退货
                    }
                    List<StoreAddressDTO> listStoreAddressDTOTui= storeAddressService.getlistStoreAddress(paramMaptui);
                    if(listStoreAddressDTOTui.size()>0){
                        opl.setStoreAddressDTOTui(listStoreAddressDTOTui.get(0));
                    }
                    //添加商品信息
                    List<OrderStoreGoodRecord>  orderStoreGoodRecords= orderStoreGoodRecordService.selectOrderStoreSubListId(opl.getId());
                    //添加供应商订单商品记录
                    if(orderStoreGoodRecords.size()>0){
                        opl.setOrderStoreGoodRecords(orderStoreGoodRecords);
                    }
                });
                String logisticsCompany = "";
                //包裹数据分装到前端
                for(OrderStoreSubListDTO orderStoreSubListDTO:orderStoreSubLists){
                    Map<String,Object> map =Maps.newHashMap();
                 //字典数据
                    logisticsCompany = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_text", "item_value", orderStoreSubListDTO.getLogisticsCompany());

                    map.put("logisticsCompany",logisticsCompany) ;
                    map.put("trackingNumber",orderStoreSubListDTO.getTrackingNumber());
                    map.put("logisticsTracking",orderStoreSubListDTO.getLogisticsTracking());
                    if(orderStoreSubListDTO.getOrderStoreGoodRecords().size()>0){
                        map.put("mainPicture",orderStoreSubListDTO.getOrderStoreGoodRecords().get(0).getMainPicture());
                    }else{
                        map.put("mainPicture","");

                    }
                    listMap.add(map);
                }
            }else{
                result.error500("未查到订单信息!");
                return result;
            }

            //是否还有未发货商品
            List<OrderStoreSubListDTO>  listOrderStoreSubList = orderStoreSubListService.selectorderStoreListId(orderListId,UserId,"0",null);
            if(listOrderStoreSubList.size()>0){
                for(OrderStoreSubListDTO lopl:listOrderStoreSubList){
                    SysUser sysUser = sysUserService.getById(lopl.getSysUserId());
                    if(sysUser!=null){
                        lopl.setSysUserName(sysUser.getRealname());
                    }
                    //添加商品信息
                    List<OrderStoreGoodRecord>  orderStoreGoodRecords= orderStoreGoodRecordService.selectOrderStoreSubListId(lopl.getId());
                    //添加供应商订单商品记录
                    if(orderStoreGoodRecords.size()>0){
                        lopl.setOrderStoreGoodRecords(orderStoreGoodRecords);
                        //还有未发货的商品添加一个空包裹
                        orderStoreSubLists.add(lopl);
                        break;
                    }
                }
            }


        }else if(isPlatform.intValue()==1){
            //平台包裹
            //查询包裹集合，parentId ！=0 为包裹数据
            List<OrderProviderListDTO> orderProviderLists= orderProviderListService.selectorderListId(orderListId,null,null,"0");
            if(orderProviderLists.size()>0){
                orderProviderLists.forEach(opl -> {

                    SysUser sysUser = sysUserService.getById(opl.getSysUserId());
                    if(sysUser!=null){
                        opl.setSysUserName(sysUser.getRealname());
                    }
                    //获取物流数据JSON
                    if(StringUtils.isNotBlank(opl.getLogisticsTracking())){
                        JSONObject jsonObject = JSONObject.parseObject(opl.getLogisticsTracking());
                        if(jsonObject.get("status").equals("0")){
                            //请求成功
                            JSONObject jsonObjectResult = JSONObject.parseObject(jsonObject.get("result").toString());
                            //已签收
                            if(jsonObjectResult.get("issign")!=null&&jsonObjectResult.get("issign").equals("1")){
                                //不做查询物流接口
                            }else{
                                //请求接口更新物流数据接口
                                String string =orderProviderListService.listSkip(opl.getId());
                                opl.setLogisticsTracking(string);
                            }
                        }
                    }else{
                        //请求接口更新物流数据接口
                        String string =orderProviderListService.listSkip(opl.getId());
                        opl.setLogisticsTracking(string);
                    }
                    //供应商发货信息
                    Map<String,String> paramMap = Maps.newHashMap();
                    paramMap.put("id",opl.getProviderAddressIdSender());
                    if(opl.getProviderAddressIdSender()==null || "".equals( opl.getProviderAddressIdSender())){
                        paramMap.put("sysUserId",opl.getSysUserId());
                        paramMap.put("isDeliver","1");//发货默认
                        paramMap.put("isReturn","");//退货
                    }
                    List<ProviderAddressDTO> listProviderAddressDTO= providerAddressService.getlistProviderAddress(paramMap);
                    if(listProviderAddressDTO.size()>0){
                        opl.setProviderAddressDTOFa(listProviderAddressDTO.get(0));
                    }
                    //供应商退信息
                    Map<String,String> paramMaptui = Maps.newHashMap();
                    paramMaptui.put("id",opl.getProviderAddressIdTui());
                    if(opl.getProviderAddressIdTui()== null || "".equals(opl.getProviderAddressIdTui())){
                        paramMaptui.put("sysUserId",opl.getSysUserId());
                        paramMaptui.put("isDeliver","");//发货默认
                        paramMaptui.put("isReturn","1");//退货
                    }
                    List<ProviderAddressDTO> listProviderAddressDTOTui= providerAddressService.getlistProviderAddress(paramMaptui);
                    if(listProviderAddressDTOTui.size()>0){
                        opl.setProviderAddressDTOTui(listProviderAddressDTOTui.get(0));
                    }
                    //添加商品信息
                    List<OrderProviderGoodRecord>  orderProviderGoodRecords= orderProviderGoodRecordService.selectOrderProviderListId(opl.getId());
                    //添加供应商订单商品记录
                    if(orderProviderGoodRecords.size()>0){
                        opl.setOrderProviderGoodRecords(orderProviderGoodRecords);
                    }
                });
                String logisticsCompany = "";
                //包裹数据分装到前端
                for(OrderProviderListDTO orderProviderListDTO:orderProviderLists){
                    Map<String,Object> map =Maps.newHashMap();
                    //字典数据
                    logisticsCompany = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_text", "item_value",orderProviderListDTO.getLogisticsCompany());

                    map.put("logisticsCompany",logisticsCompany) ;
                    map.put("trackingNumber",orderProviderListDTO.getTrackingNumber());
                    map.put("logisticsTracking",orderProviderListDTO.getLogisticsTracking());
                    if(CollUtil.isNotEmpty(orderProviderListDTO.getOrderProviderGoodRecords())){
                        map.put("mainPicture",orderProviderListDTO.getOrderProviderGoodRecords().get(0).getMainPicture());
                    }
                    listMap.add(map);
                }
            }
            //是否还有未发货商品
            List<OrderProviderListDTO>  listOrderProviderList = orderProviderListService.selectorderListId(orderListId,null,"0",null);
            if(listOrderProviderList.size()>0){
                for(OrderProviderListDTO lopl:listOrderProviderList){
                    SysUser sysUser = sysUserService.getById(lopl.getSysUserId());
                    if(sysUser!=null){
                        lopl.setSysUserName(sysUser.getRealname());
                    }
                    //添加商品信息
                    List<OrderProviderGoodRecord>  orderProviderGoodRecords= orderProviderGoodRecordService.selectOrderProviderListId(lopl.getId());
                    //添加供应商订单商品记录
                    if(orderProviderGoodRecords.size()>0){
                        lopl.setOrderProviderGoodRecords(orderProviderGoodRecords);
                        //还有未发货的商品添加一个空包裹
                        orderProviderLists.add(lopl);
                        break;
                    }
                }
            }

        }
        objectMap.put("orderProviderLists",listMap);
        result.setResult(objectMap);
        result.success("包裹信息查询");
        return result;
    }

}
