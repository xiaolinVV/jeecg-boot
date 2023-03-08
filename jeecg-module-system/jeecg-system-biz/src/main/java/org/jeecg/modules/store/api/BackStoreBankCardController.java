package org.jeecg.modules.store.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreBankCardService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("back/storeBankCard")
@Slf4j
public class BackStoreBankCardController {
   @Autowired
   private IStoreBankCardService storeBankCardService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 添加银行卡(post)
     * @param storeBankCard
     * @return
     */
    @RequestMapping("addStoreBankCard")
    @ResponseBody
    public Result<?> addStoreBankCard( StoreBankCard storeBankCard,
                                            @RequestHeader(name = "sysUserId",defaultValue = "") String sysUserId){
        //查询店铺
        StoreManage storeManage = iStoreManageService.getStoreManageBySysUserId(sysUserId);
        if(storeManage==null){
           return Result.error("查询不到开店用户信息！！！");
        }
        //查询是否已存在银行卡
        StoreBankCard storeBankCard1=storeBankCardService.getStoreBankCardByStoreManageId(storeManage.getId(),"0");
        if( storeBankCard1!=null){
            storeBankCard.setId(storeBankCard1.getId());
            storeBankCard.setCreateTime(storeBankCard1.getCreateTime());
            storeBankCard.setCreateBy(storeBankCard1.getCreateBy());
        }
        storeBankCard.setStoreManageId(storeManage.getId());
        storeBankCardService.saveOrUpdate(storeBankCard);
        //是否同步汇付银行卡信息
        String huifuBankcardVerify = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "huifu_bankcard_verify");
        if(huifuBankcardVerify.equals("1")){
            if(StringUtils.isBlank(storeBankCard.getSettleAccount())){
                return storeBankCardService.createMemberPrivate(storeBankCard);
            }else{
                return storeBankCardService.updateSettleAccountPrivate(storeBankCard);
            }
        }
        return Result.ok("添加成功");
    }

    /**
     * 跳转申请提现页面
     * @param request
     * @return
     */
    @RequestMapping("getJumpWithdrawal")
    @ResponseBody
    public Result<Map<String,Object>> getJumpWithdrawal(HttpServletRequest request){
        String sysUserId = request.getAttribute("sysUserId").toString();
        Result<Map<String,Object>> result=new Result<>();
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
        StoreManage storeManage=iStoreManageService.list(storeManageQueryWrapper).get(0);
        QueryWrapper<StoreBankCard> queryWrapperStoreBankCard = new QueryWrapper();
        queryWrapperStoreBankCard.select("id,bank_name,bank_card,car_type");
        queryWrapperStoreBankCard.eq("store_manage_id",storeManage.getId());
        queryWrapperStoreBankCard.eq("car_type","0");

       List<Map<String,Object>> storeBankCardListMap= storeBankCardService.listMaps(queryWrapperStoreBankCard);
        //获取手续费比例
        String withdrawalServiceCharge = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item",
                "item_value", "item_text", "withdrawal_service_charge"), "%");
        if(storeBankCardListMap.size()==0){
            objectMap.put("bankName","");
            objectMap.put("bankCard","");
            objectMap.put("carType","");
            objectMap.put("isBankCard","0");//没有银行卡
            objectMap.put("withdrawalServiceCharge",withdrawalServiceCharge);
        }else{
            Map<String,Object>  storeBankCardMap = storeBankCardListMap.get(0);
            //参数处理
            objectMap.put("bankName",storeBankCardMap.get("bank_name"));//开户行
            objectMap.put("bankCard",storeBankCardMap.get("bank_card").toString().substring(storeBankCardMap.get("bank_card").toString().length() - 4));//银行卡截取最后四位
            objectMap.put("carType",storeBankCardMap.get("car_type"));//卡类型；0：银行卡；1：支付宝
            objectMap.put("isBankCard","1");//有银行卡
            objectMap.put("withdrawalServiceCharge",withdrawalServiceCharge);
        }
        objectMap.put("balance",storeManage.getBalance());//店铺余额
        result.setCode(200);
        result.setResult(objectMap);

     return   result;
    }


    /**
     * 绑定银行卡必填项
     * @return
     */
    @RequestMapping("getWithdrawalBankCardRequired")
    @ResponseBody
    public Result<?> getWithdrawalBankCardRequired(@RequestHeader(name = "sysUserId",defaultValue = "") String sysUserId){
        HashMap<String, Object> map = new HashMap<>();
        String withdrawalBankCardRequired = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdrawal_bank_card_required");
        map.put("storeWithdrawalBankCardRequired",withdrawalBankCardRequired);
        SysUser sysUser = sysUserService.getById(sysUserId);
        if(StringUtils.isBlank(sysUser.getPhone())){
           return Result.error("未找到商户手机号");
        }
        map.put("phone",sysUser.getPhone());
        return Result.ok(map);
    }


    /**
     *
     * 根据店铺id获取银行卡信息（每个店铺一张银行卡）
     *
     * @param sysUserId
     * @return
     */
    @RequestMapping("getStoreBankCardByStoreManageId")
    @ResponseBody
    public Result<?> getStoreBankCardByStoreManageId(@RequestHeader(defaultValue = "",name = "sysUserId") String sysUserId){
        StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(sysUserId);
        if(storeManage==null){
            return Result.error("未找到店铺");
        }
        return Result.ok(storeBankCardService.getStoreBankCardByStoreManageId(storeManage.getId(),"0"));
    }
}
