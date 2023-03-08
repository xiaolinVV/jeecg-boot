package org.jeecg.modules.store.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.entity.StoreWithdrawDeposit;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.store.service.IStoreWithdrawDepositService;
import org.jeecg.modules.store.vo.StoreWithdrawDepositVO;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("back/storeRechargeRecord")
@Slf4j
public class BackStoreRechargeRecordController {
    @Autowired
    private IStoreRechargeRecordService storeRechargeRecordService;
    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IStoreWithdrawDepositService storeWithdrawDepositService;

    @Autowired
    private ISysDictService iSysDictService;
    @AutoLog(value = "店铺余额记录-充值记录")
    @ApiOperation(value="店铺余额记录-充值记录", notes="店铺余额记录-充值记录")
    @RequestMapping("findRechargeRecord")
    @ResponseBody
    public Result<Map<String, Object>> findRechargeRecord(
                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                 HttpServletRequest request) {//StoreRechargeRecord storeRechargeRecord,
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        Map<String,Object> objectMap= Maps.newHashMap();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUserId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");
        if(iStoreManageService.count(storeManageQueryWrapper)<=0){
            result.error500("查询不到开店用户信息！！！");
            return result;
        }
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
       //查询提现数据
        QueryWrapper<StoreRechargeRecord> queryWrapper = new QueryWrapper<>(); //QueryGenerator.initQueryWrapper(storeRechargeRecord, request.getParameterMap());
        queryWrapper.select("id,create_time AS createTime,amount,trade_status AS tradeStatus,order_no AS orderNo,payment");
        queryWrapper.eq("del_flag","0");
        queryWrapper.eq("pay_type","4");
        queryWrapper.eq("store_manage_id",storeManage.getId());
        queryWrapper.orderByDesc("create_time","amount");
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        IPage<Map<String, Object>> pageList = storeRechargeRecordService.pageMaps(page, queryWrapper);
        objectMap.put("storeRechargeRecordList",pageList);
        objectMap.put("sysUserId",sysUserId);
        result.setSuccess(true);
        result.setCode(200);
        result.setResult(objectMap);
        return result;
    }
    @AutoLog(value = "店铺余额记录-提现记录")
    @ApiOperation(value="店铺余额记录-提现记录", notes="店铺余额记录-提现记录")
    @RequestMapping("findPayRecord")
    @ResponseBody
    public Result<Map<String, Object>> findPayRecord(String id,
                                                     String status,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        Map<String,Object> objectMap= Maps.newHashMap();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUserId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");
        if(iStoreManageService.count(storeManageQueryWrapper)<=0){
            result.error500("查询不到店铺用户信息！！！");
            return result;
        }
        StoreWithdrawDepositVO storeWithdrawDepositVO  = new StoreWithdrawDepositVO();
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
        if (StringUtils.isNotBlank(id) ){
            storeWithdrawDepositVO.setId(id);
        }
        //添加状态查询条件
        if(StringUtils.isNotBlank(status)){
            storeWithdrawDepositVO.setStatus(status);
        }
        storeWithdrawDepositVO.setStoreManageId(storeManage.getId());
        Page<StoreWithdrawDeposit> page = new Page<StoreWithdrawDeposit>(pageNo, pageSize);
        IPage<Map<String,Object>> pageList = storeWithdrawDepositService.getStoreWithdrawDepositMap(page,storeWithdrawDepositVO);
        objectMap.put("storeRechargeRecordList",pageList);
        objectMap.put("sysUserId",sysUserId);
        result.setSuccess(true);
        result.setCode(200);
        result.setResult(objectMap);
        return result;
    }
    //post
    @AutoLog(value = "提现")
    @RequestMapping("cashOut")
    @ResponseBody
    public Result<?> cashOut(BigDecimal amount ,
                                                HttpServletRequest request){
        String withdrawMinimum = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_minimum");
        if (amount.doubleValue()<Double.valueOf(withdrawMinimum)){
            return Result.error("提现金额低于最低提现金额");
        }
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        String s = iStoreManageService.cashOut(sysUserId, amount);
        if (s.contains("200")){
            result.success("提现成功,待审核通过后打款");
        }else {
            result.error500(s.split(":")[1]);
        }
        return result;
    }

    //获取提现说明
    @RequestMapping("getWithdrawWarmPrompt")
    @ResponseBody
    public Result<?> getWithdrawWarmPrompt(){
        //获取提现说明
        String queryTableDictTextByKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_warm_prompt");
        return Result.ok(queryTableDictTextByKey);
    }
    //获取最低提现金额
    @RequestMapping("getWithdrawMinimum")
    @ResponseBody
    public Result<?> getWithdrawMinimum(){
        //获取最低提现金额
        String queryTableDictTextByKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_minimum");
        return Result.ok(queryTableDictTextByKey);
    }
    /**
     * 提现金额计算
     * @param money 金额
     * @return
     */
    @RequestMapping("withdrawXchanger")
    @ResponseBody
    public Result<?> withdrawXchanger(@RequestParam(required = false,defaultValue = "0")String money){
        HashMap<String, Object> map = new HashMap<>();
        //最小提现金额
        String withdrawMinimum = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_minimum");
        //获取字典配置提现阈值，大于此数据值则手续费按照大于的取，小于等于此数据值则手续费小于的取
        String withdrawThreshold = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_threshold");
        //提现手续费小于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeLessType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_type");
        //提现手续费大于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeGreaterType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_type");
        //小于阈值固定费用（fixed）
        String withdrawServiceChargeLessFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_fixed");
        //小于阈值百分比（percent）
        String withdrawServiceChargeLessPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_percent");
        //大于阈值固定费用（fixed）
        String withdrawServiceChargeGreaterFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_fixed");
        //大于阈值百分比（percent）
        String withdrawServiceChargeGreaterPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_percent");
        if (Double.valueOf(money)<Double.valueOf(withdrawMinimum)){
            map.put("serviceCharge",withdrawServiceChargeLessFixed);
            map.put("amount",0);
            map.put("withdrawExplain",withdrawServiceChargeLessFixed);
            map.put("withdrawType",0);
        }else {
            if (Double.valueOf(money)<=Double.valueOf(withdrawThreshold)){
                if (withdrawServiceChargeLessType.equals("0")){
                    map.put("serviceCharge",new BigDecimal(withdrawServiceChargeLessFixed));
                    map.put("amount",new BigDecimal(money).subtract(new BigDecimal(withdrawServiceChargeLessFixed)));
                    map.put("withdrawExplain",withdrawServiceChargeLessFixed);
                    map.put("withdrawType",0);
                }else {
                    BigDecimal decimal = new BigDecimal(money);
                    map.put("serviceCharge",decimal.multiply(new BigDecimal(withdrawServiceChargeLessPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    map.put("amount",decimal.subtract(new BigDecimal(String.valueOf(map.get("serviceCharge")))));
                    map.put("withdrawExplain",withdrawServiceChargeLessPercent);
                    map.put("withdrawType",1);
                }
            }else {
                if (withdrawServiceChargeGreaterType.equals("0")){
                    map.put("serviceCharge",new BigDecimal(withdrawServiceChargeGreaterFixed));
                    map.put("amount",new BigDecimal(money).subtract(new BigDecimal(String.valueOf(map.get("serviceCharge")))));
                    map.put("withdrawExplain",withdrawServiceChargeGreaterFixed);
                    map.put("withdrawType",2);
                }else {
                    map.put("serviceCharge",new BigDecimal(money).multiply(new BigDecimal(withdrawServiceChargeGreaterPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    map.put("amount",new BigDecimal(money).subtract(new BigDecimal(String.valueOf(map.get("serviceCharge")))));
                    map.put("withdrawExplain",withdrawServiceChargeGreaterPercent);
                    map.put("withdrawType",3);
                }
            }
        }
        return Result.ok(map);
    }
    @RequestMapping("findStoreRechargeRecordInfo")
    @ResponseBody
    public Result<IPage<Map<String, Object>>> findStoreRechargeRecordInfo(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                  @RequestParam(value="state",required = true) String state,
                                                                  @RequestParam(value="id",required = true) String id,
                                                                  HttpServletRequest request){
        Result<IPage<Map<String, Object>>> result = new Result<>();
        Page<StoreAccountCapital> page = new Page<StoreAccountCapital>(pageNo, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("state",state);
        IPage<Map<String, Object>> storeRechargeRecordInfo = storeRechargeRecordService.findStoreRechargeRecordInfo(page, map);
        result.success("返回明细成功!");
        result.setResult(storeRechargeRecordInfo);
        return result;
    }
}
