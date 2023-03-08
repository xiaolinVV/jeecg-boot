package org.jeecg.modules.pay.utils;


import com.alibaba.fastjson.JSON;
import com.huifu.adapay.Adapay;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import com.huifu.adapay.model.Drawcash;
import com.huifu.adapay.model.Member;
import com.huifu.adapay.model.SettleAccount;
import lombok.extern.java.Log;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 汇付天下操作工具
 */
@Component
@Log
public class HftxPayUtils {


    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 创建对私用户信息
     *
     * @param memberId
     * @param nickname
     * @return
     */
    public boolean createMemberPrivate(String memberId,String nickname){
        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");
        try {
            //查询用户是否存在
            Map<String, Object> memberParamsQuery = new HashMap<String, Object>(2);
            memberParamsQuery.put("member_id", memberId);
            memberParamsQuery.put("app_id", appid);
            Map<String, Object> memberQuery = Member.query(memberParamsQuery);
            log.info("查询汇付天下会员："+ JSON.toJSONString(memberQuery));
            //如果会员id不存在就创建会员id
            if(memberQuery.get("status").toString().equals("failed")){
                Map<String, Object> memberParams = new  HashMap<String, Object>(7);
                memberParams.put("member_id",memberId);
                memberParams.put("app_id",appid);
                memberParams.put("nickname",nickname);
                Map<String, Object> member = Member.create(memberParams);
                log.info("创建汇付天下会员："+JSON.toJSONString(member));
            }
        } catch (BaseAdaPayException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    /**
     *
     * 创建结算账户对私
     *
     * @param memberId    会员id
     * @return
     */
    public Map<String,Object> createSettleAccountPrivate(String memberId,Map<String,Object> paramMap) {

        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");

        try {
                Map<String, Object> settleCountParams = new  HashMap<String, Object>(4);
                Map<String, Object> accountInfo = new  HashMap<String, Object>(9);
                accountInfo.put("card_id",paramMap.get("card_id"));
                accountInfo.put("card_name",paramMap.get("card_name"));
                accountInfo.put("cert_id",paramMap.get("cert_id"));
                accountInfo.put("tel_no",paramMap.get("tel_no"));
                accountInfo.put("bank_acct_type",paramMap.get("bank_acct_type"));
                accountInfo.put("cert_type","00");
                settleCountParams.put("member_id", memberId);
                settleCountParams.put("app_id", appid);
                settleCountParams.put("channel","bank_account");//银行卡
                settleCountParams.put("account_info", accountInfo);
                Map<String, Object> settleCount = SettleAccount.create(settleCountParams);
                log.info("创建结算账户信息："+JSON.toJSONString(settleCount));
                return settleCount;
        } catch (BaseAdaPayException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     *
     * 修改结算账户对私
     *
     * @param memberId    会员id
     * @return
     */
    public Map<String,Object> updateSettleAccountPrivate(String memberId,Map<String,Object> paramMap,String settleAccountId) {
        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");
        Map<String, Object> settleCountParams = new  HashMap<String, Object>(2);
        settleCountParams.put("settle_account_id", settleAccountId);
        settleCountParams.put("member_id",memberId);
        settleCountParams.put("app_id", appid);
        try {
            Map<String, Object> settleCount = SettleAccount.delete(settleCountParams);
            log.info("删除结算信息数据："+JSON.toJSONString(settleCount));
            if(settleCount.get("status").toString().equals("succeeded")){
                return this.createSettleAccountPrivate(memberId,paramMap);
            }else{
                return this.createSettleAccountPrivate(memberId,paramMap);
            }
        } catch (BaseAdaPayException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 本商户取现接口
     *
     * @param orderNo  交易流水号
     * @param cashAmt  交易金额
     * @return
     */
    public Map<String, Object> withdrawDepositBase(String orderNo,String cashAmt) throws BaseAdaPayException{
        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");
            //提现操作
            Map<String, Object> cashParam = new  HashMap<String, Object>(5);
            cashParam.put("order_no", orderNo);
            cashParam.put("app_id", appid);
            cashParam.put("cash_type", "T1");
            cashParam.put("cash_amt", cashAmt);
            cashParam.put("member_id", "0");
            return Drawcash.create(cashParam);
    }


    /**
     * 查询本商户余额
     *
     * @return
     * @throws BaseAdaPayException
     */
    public Map<String, Object> getSettleAccountBalance() throws BaseAdaPayException{
        //获取常量信息
        String appid=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "hftx_app_id");
        Map<String, Object> queryParams = new  HashMap<String, Object>(5);
        queryParams.put("member_id", "0");
        queryParams.put("app_id", appid);
        return  SettleAccount.balance(queryParams, Adapay.defaultMerchantKey);
    }

}
