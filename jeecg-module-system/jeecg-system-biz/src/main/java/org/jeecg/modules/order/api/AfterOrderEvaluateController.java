package org.jeecg.modules.order.api;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.*;
import org.jeecg.modules.order.vo.EvaluateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 评价接口控制器
 */
@Slf4j
@RequestMapping("after/orderEvaluate")
@Controller
public class AfterOrderEvaluateController {

    @Autowired
    private IOrderEvaluateService iOrderEvaluateService;

    @Autowired
    private IOrderEvaluateStoreService iOrderEvaluateStoreService;
    @Autowired
    private IOrderListService iOrderListService;
    @Autowired
    private IOrderStoreListService iOrderStoreListService;
    @Autowired
    private IOrderStoreGoodRecordService iOrderStoreGoodRecordService;
    @Autowired
    private IOrderProviderGoodRecordService iOrderProviderGoodRecordService;


    /**
     * 评价页面
     * @param id
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("getOrderEvaluate")
    @ResponseBody
    public Result<Map<String,Object>> getOrderEvaluate(String id,Integer isPlatform,HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(id)){
            result.error500("orderId订单id不能为空！！！");
            return result;
        }
        //店铺订单
        if(isPlatform.intValue()==0){
            OrderStoreList orderStoreList=iOrderStoreListService.getById(id);
            objectMap.put("id",id);
            objectMap.put("logisticsStar","5");//物流星级
            objectMap.put("shippingStar","5");//发货星级
            objectMap.put("serviceStar","5");//服务星级
            objectMap.put("isPlatform","0");//店铺标识
            //订单商品
            objectMap.put("goods",iOrderStoreGoodRecordService.getOrderStoreGoodRecordByOrderIdEvaluate(orderStoreList.getId()));
        }else{

            //平台订单
            if(isPlatform.intValue()==1){
                OrderList orderList=iOrderListService.getById(id);
                objectMap.put("id",id);
                objectMap.put("logisticsStar","5");//物流星级
                objectMap.put("shippingStar","5");//发货星级
                objectMap.put("serviceStar","5");//服务星级
                objectMap.put("isPlatform","1");//店铺标识

                //订单商品
                objectMap.put("goods",iOrderProviderGoodRecordService.getOrderProviderGoodRecordByOrderIdEvaluate(orderList.getId()));
            }
        }

        result.setResult(objectMap);
        result.success("查询成功!");
        return  result;

    }

    /**
     * 添加评价信息
     * @param evaluateVO
     * @param request
     * @return
     */
    @RequestMapping("addOrderEvaluate")
    @ResponseBody
    public Result<String>  addOrderEvaluate(EvaluateVO  evaluateVO, HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();
        log.info("发送参数:"+JSONObject.toJSONString(evaluateVO));
        Result<String> result=new Result<>();
        if(evaluateVO == null){
            result.error500("评论参数不能为空!");
            return result;
        }
            //参数判断
            if(StringUtils.isBlank(evaluateVO.getIsPlatform())){
                result.error500("isPlatform是否平台类型参数不能为空！！！");
                return result;
            }
            //添加店铺评价
            if("0".equals(evaluateVO.getIsPlatform())){
                iOrderEvaluateStoreService.addEvaluate(evaluateVO,memberId);
            }else if("1".equals(evaluateVO.getIsPlatform())){
                //添加平台评价
                iOrderEvaluateService.addEvaluate(evaluateVO,memberId);
            }

        result.success("添加成功!");
        return result;
    }




}
