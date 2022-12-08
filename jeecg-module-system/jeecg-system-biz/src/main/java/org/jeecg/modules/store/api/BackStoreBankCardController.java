package org.jeecg.modules.store.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreBankCardService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.vo.StoreBankCardVO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    private RedisUtil redisUtil;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 获取用户绑定手机号
     * @param request
     * @return
     */
    @RequestMapping("getSysUserPhone")
    @ResponseBody
    public Result<Map<String,Object>> getSysUserPhone(HttpServletRequest request){
        String sysUserId = request.getAttribute("sysUserId").toString();
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        SysUser sysUser = sysUserService.getById(sysUserId);
        if(StringUtils.isBlank(sysUser.getPhone())){
            result.error500("未找到用户绑定手机号!");
            return result;
        }
        objectMap.put("sysUserId",sysUserId);
        objectMap.put("phone",sysUser.getPhone());
        result.setCode(200);
        result.setResult(objectMap);
        return result;
    }

    /**
     * 添加银行卡(post)
     * @param storeBankCardVO
     * @param request
     * @return
     */
    @RequestMapping("addStoreBankCard")
    @ResponseBody
    public Result<String> addStoreBankCard( StoreBankCardVO storeBankCardVO,
                                            HttpServletRequest request){
        String sysUserId = request.getAttribute("sysUserId").toString();
        Result<String> result=new Result<>();
        if(StringUtils.isBlank(storeBankCardVO.getSbCode())){
            return result.error500("手机验证码不能为空!");
        }
        //验证码验证
        String smscode = storeBankCardVO.getSbCode();
        Object code = redisUtil.get(storeBankCardVO.getPhone());
        if(code==null){
            result.error500("手机号找不到验证码");
            return result;
        }
        if(!code.toString().equals(smscode)){
            result.error500("验证码输入不正确，请重新输入");
            return result;
        }
        SysUser sysUser = sysUserService.getById(sysUserId);
        if(!sysUser.getPhone().equals(storeBankCardVO.getPhone())){
            result.error500("手机号不是绑定手机号!");
            return result;
        }
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
        //查询是否已存在银行卡
        QueryWrapper<StoreBankCard> queryWrapperStoreBankCard = new QueryWrapper();
        queryWrapperStoreBankCard.eq("store_manage_id",storeManage.getId());
        queryWrapperStoreBankCard.eq("car_type","0");
        if( storeBankCardService.count(queryWrapperStoreBankCard)>0){
            result.error500("已有绑定银行卡信息！！！");
            return result;
        }
        StoreBankCard  storeBankCard= new StoreBankCard();
        //拷贝数据
        BeanUtils.copyProperties(storeBankCardVO,storeBankCard);
        storeBankCard.setCarType("0");
        storeBankCard.setStoreManageId(storeManage.getId());
        storeBankCardService.saveOrUpdate(storeBankCard);

        result.setCode(200);
        result.setMessage("添加成功");
        return result;
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
     * 获取银行卡信息列表
     *
     * @return
     */
    @RequestMapping("getBlankList")
    @ResponseBody
    public Result<?> getBlankList(){
        try {
            Map<String,Object> resultMap= Maps.newHashMap();
            IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("blank.properties"),"utf-8").forEach(blance->{
                resultMap.put(org.apache.commons.lang.StringUtils.substringBefore(blance,"="), org.apache.commons.lang.StringUtils.substringAfter(blance,"="));
            });

            return Result.ok(resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }
    /**
     * 获取银行卡城市
     * @return
     */
    @RequestMapping("findSrea")
    @ResponseBody
    public Result<?> findSrea(){
        try {
            return Result.ok(StringEscapeUtils.unescapeJava(org.apache.commons.lang.StringUtils.join(IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("pay_area.properties"),"utf-8"),"")));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("异常");
        }
    }
    /**
     * 绑定银行卡必填项
     * @return
     */
    @RequestMapping("getWithdrawalBankCardRequired")
    @ResponseBody
    public Result<?> getWithdrawalBankCardRequired(){
        HashMap<String, Object> map = new HashMap<>();
        String withdrawalBankCardRequired = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdrawal_bank_card_required");
        map.put("storeWithdrawalBankCardRequired",withdrawalBankCardRequired);
        return Result.ok(map);
    }
}
