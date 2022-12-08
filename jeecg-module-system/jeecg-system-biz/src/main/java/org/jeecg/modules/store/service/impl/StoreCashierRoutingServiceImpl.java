package org.jeecg.modules.store.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.pay.entity.PayShouyinLog;
import org.jeecg.modules.store.dto.StoreCashierRoutingDTO;
import org.jeecg.modules.store.entity.StoreCashierRouting;
import org.jeecg.modules.store.entity.StoreCashierSetting;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.mapper.StoreCashierRoutingMapper;
import org.jeecg.modules.store.service.IStoreCashierRoutingService;
import org.jeecg.modules.store.service.IStoreCashierSettingService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺收银分账设置
 * @Author: jeecg-boot
 * @Date:   2022-06-06
 * @Version: V1.0
 */
@Service
@Log
public class StoreCashierRoutingServiceImpl extends ServiceImpl<StoreCashierRoutingMapper, StoreCashierRouting> implements IStoreCashierRoutingService {


    @Autowired
    private IStoreCashierSettingService iStoreCashierSettingService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IOrderStoreListService iOrderStoreListService;

    @Override
    public IPage<StoreCashierRoutingDTO> queryPageList(Page<StoreCashierRoutingDTO> page, QueryWrapper wrapper) {
        return baseMapper.queryPageList(page,wrapper);
    }

    @Override
    public List<Map<String, String>> independentAccountShouYin(PayShouyinLog payShouyinLog) {
        List<Map<String, String>> divMembers = new ArrayList<>();
        BigDecimal allTotalPrice=payShouyinLog.getAllTotalPrice();
        //收银设置
        StoreCashierSetting storeCashierSetting=iStoreCashierSettingService.getOne(new LambdaQueryWrapper<StoreCashierSetting>().eq(StoreCashierSetting::getStoreManageId,payShouyinLog.getStoreManageId()));
        if(storeCashierSetting!=null&&storeCashierSetting.getIsCashier().equals("1")){
            //分账设置
            List<StoreCashierRouting> storeCashierRoutings=this.list(new LambdaQueryWrapper<StoreCashierRouting>()
                    .eq(StoreCashierRouting::getStoreManageId,payShouyinLog.getStoreManageId())
                    .eq(StoreCashierRouting::getFashionableType,"0")
                    .in(StoreCashierRouting::getAccountType,"0","1"));

            if(storeCashierRoutings.size()>0){
                BigDecimal collectingCommission=allTotalPrice;
                for (StoreCashierRouting storeCashierRouting:storeCashierRoutings) {
                    Map<String, String> divMember2 = Maps.newHashMap();
                    divMember2.put("member_id", storeCashierRouting.getId());
                    BigDecimal div=new BigDecimal(0);
                    if(storeCashierRouting.getFashionableWay().equals("0")){
                        div=allTotalPrice.multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
                    if(storeCashierRouting.getFashionableWay().equals("1")){
                        div=allTotalPrice.multiply(storeCashierSetting.getPresentProportion().divide(new BigDecimal(100),2,RoundingMode.DOWN)).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
                    if(storeCashierRouting.getFashionableWay().equals("2")){
                        div=allTotalPrice.multiply(new BigDecimal(1).subtract(storeCashierSetting.getPresentProportion().multiply(storeCashierSetting.getIntegratedWorth()).divide(new BigDecimal(10000),2,RoundingMode.DOWN))).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
                    divMember2.put("amount",div.toString());
                    divMember2.put("fee_flag", "N");
                    if(div.doubleValue()<=0){
                        continue;
                    }
                    divMembers.add(divMember2);
                    collectingCommission=collectingCommission.subtract(div);
                    if(storeCashierRouting.getIsStore().equals("1")){
                        payShouyinLog.setSettlementAmount(div);
                    }
                }
                Map<String, String> divMember = new HashMap<>(3);
                divMember.put("member_id", "0");
                divMember.put("amount",collectingCommission .toString());
                divMember.put("fee_flag", "Y");
                divMembers.add(divMember);

                //分账记录

            }
        }
        return divMembers;
    }

    @Override
    public void independentAccountShouYinBalance(PayShouyinLog payShouyinLog) {
        BigDecimal allTotalPrice=payShouyinLog.getAllTotalPrice();
        //收银设置
        StoreCashierSetting storeCashierSetting=iStoreCashierSettingService.getOne(new LambdaQueryWrapper<StoreCashierSetting>().eq(StoreCashierSetting::getStoreManageId,payShouyinLog.getStoreManageId()));
        if(storeCashierSetting!=null&&storeCashierSetting.getIsCashier().equals("1")){
            //分账设置
            List<StoreCashierRouting> storeCashierRoutings=this.list(new LambdaQueryWrapper<StoreCashierRouting>()
                    .eq(StoreCashierRouting::getStoreManageId,payShouyinLog.getStoreManageId())
                    .eq(StoreCashierRouting::getFashionableType,"0")
                    .eq(StoreCashierRouting::getAccountType,"2"));

            if(storeCashierRoutings.size()>0){
                BigDecimal collectingCommission=allTotalPrice;
                for (StoreCashierRouting storeCashierRouting:storeCashierRoutings) {
                    BigDecimal div=new BigDecimal(0);
                    if(storeCashierRouting.getFashionableWay().equals("0")){
                        div=allTotalPrice.multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
                    if(storeCashierRouting.getFashionableWay().equals("1")){
                        div=allTotalPrice.multiply(storeCashierSetting.getPresentProportion().divide(new BigDecimal(100),2,RoundingMode.DOWN)).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
                    if(storeCashierRouting.getFashionableWay().equals("2")){
                        div=allTotalPrice.multiply(new BigDecimal(1).subtract(storeCashierSetting.getPresentProportion().multiply(storeCashierSetting.getIntegratedWorth()).divide(new BigDecimal(10000),2,RoundingMode.DOWN))).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
                    if(div.doubleValue()<=0){
                        continue;
                    }
                    collectingCommission=collectingCommission.subtract(div);
                    if(storeCashierRouting.getIsStore().equals("1")){
                        payShouyinLog.setSettlementAmount(div);
                    }
                    //增加会员余额
                    if(storeCashierRouting.getIsStore().equals("0")) {
                        if (StringUtils.isNotBlank(storeCashierRouting.getMemberListId())) {
                            iMemberListService.addBlance(storeCashierRouting.getMemberListId(), div, payShouyinLog.getId(), "50");
                        }
                    }
                    //增加商户余额
                    if(storeCashierRouting.getIsStore().equals("1")) {
                        iStoreManageService.addStoreBlance(storeCashierRouting.getStoreManageId(),div,payShouyinLog.getId(),"16");
                    }

                    //分账记录


                }
            }
        }
    }

    @Override
    public List<Map<String, String>> independentAccountOrder(List<Map<String, String>> divMembers,String orderStoreListId) {
        OrderStoreList orderStoreList=iOrderStoreListService.getById(orderStoreListId);
        BigDecimal allTotalPrice=orderStoreList.getActualPayment().subtract(orderStoreList.getShipFee());
        StoreManage storeManage=iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId,orderStoreList.getSysUserId())
                .in(StoreManage::getPayStatus,"1","2")
                .eq(StoreManage::getStatus,"1")
                .last("limit 1"));
            //分账设置
            List<StoreCashierRouting> storeCashierRoutings=this.list(new LambdaQueryWrapper<StoreCashierRouting>()
                    .eq(StoreCashierRouting::getStoreManageId,storeManage.getId())
                    .eq(StoreCashierRouting::getFashionableType,"1")
                    .in(StoreCashierRouting::getAccountType,"0","1"));

            if(storeCashierRoutings.size()>0){
                BigDecimal collectingCommission=allTotalPrice;
                for (StoreCashierRouting storeCashierRouting:storeCashierRoutings) {
                    BigDecimal div=new BigDecimal(0);
                    if(storeCashierRouting.getFashionableWay().equals("0")){
                        div=allTotalPrice.multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
/*                    if(storeCashierRouting.getFashionableWay().equals("1")){
                        div=allTotalPrice.multiply(storeCashierSetting.getPresentProportion().divide(new BigDecimal(100),2,RoundingMode.DOWN)).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }
                    if(storeCashierRouting.getFashionableWay().equals("2")){
                        div=allTotalPrice.multiply(new BigDecimal(1).subtract(storeCashierSetting.getPresentProportion().multiply(storeCashierSetting.getIntegratedWorth()).divide(new BigDecimal(10000),2,RoundingMode.DOWN))).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                    }*/
                    if(div.doubleValue()<=0){
                        continue;
                    }
                    boolean isNewDiv=true;
                    Map<String, String> divMember2 = Maps.newHashMap();
                    for (Map<String, String> m:divMembers) {
                        if(m.get("member_id").equals(storeCashierRouting.getId())){
                            isNewDiv=false;
                            divMember2=m;
                            break;
                        }
                    }

                    if(isNewDiv){
                        divMember2.put("member_id", storeCashierRouting.getId());
                        divMember2.put("amount",div.toString());
                        divMember2.put("fee_flag", "N");
                        divMembers.add(divMember2);
                    }else{
                        divMember2.put("amount",new BigDecimal(divMember2.get("amount")).add(div).toString());
                    }

                    collectingCommission=collectingCommission.subtract(div);
                }
                boolean isNewDiv=true;
                Map<String, String> divMember = new HashMap<>(3);
                for (Map<String, String> m:divMembers) {
                    if(m.get("member_id").toString().equals("0")){
                        isNewDiv=false;
                        divMember=m;
                        break;
                    }
                }

                if(isNewDiv) {
                    divMember.put("member_id", "0");
                    divMember.put("amount", collectingCommission.add(orderStoreList.getShipFee()).toString());
                    divMember.put("fee_flag", "Y");
                    divMembers.add(divMember);
                }else{
                    divMember.put("amount",new BigDecimal(divMember.get("amount")).add(collectingCommission.add(orderStoreList.getShipFee())).toString());
                }

                //分账记录

            }
        return divMembers;
    }

    @Override
    public void independentAccountOrderBalance(OrderStoreList orderStoreList) {
            BigDecimal allTotalPrice=orderStoreList.getActualPayment().subtract(orderStoreList.getShipFee());

        StoreManage storeManage=iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId,orderStoreList.getSysUserId())
                .in(StoreManage::getPayStatus,"1","2")
                .eq(StoreManage::getStatus,"1")
                .last("limit 1"));

                //分账设置
                List<StoreCashierRouting> storeCashierRoutings=this.list(new LambdaQueryWrapper<StoreCashierRouting>()
                        .eq(StoreCashierRouting::getStoreManageId,storeManage.getId())
                        .eq(StoreCashierRouting::getFashionableType,"1")
                        .eq(StoreCashierRouting::getAccountType,"2"));

                log.info("店铺余额分账:"+ JSON.toJSONString(storeCashierRoutings));

                if(storeCashierRoutings.size()>0){
                    BigDecimal collectingCommission=allTotalPrice;
                    for (StoreCashierRouting storeCashierRouting:storeCashierRoutings) {
                        BigDecimal div=new BigDecimal(0);
                        if(storeCashierRouting.getFashionableWay().equals("0")){
                            div=allTotalPrice.multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                        }
/*                        if(storeCashierRouting.getFashionableWay().equals("1")){
                            div=allTotalPrice.multiply(storeCashierSetting.getPresentProportion().divide(new BigDecimal(100),2,RoundingMode.DOWN)).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                        }
                        if(storeCashierRouting.getFashionableWay().equals("2")){
                            div=allTotalPrice.multiply(new BigDecimal(1).subtract(storeCashierSetting.getPresentProportion().multiply(storeCashierSetting.getIntegratedWorth()).divide(new BigDecimal(10000),2,RoundingMode.DOWN))).multiply(storeCashierRouting.getFashionableProportion().divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN);
                        }*/
                        log.info("店铺分账金额："+div);
                        if(div.doubleValue()<=0){
                            continue;
                        }
                        collectingCommission=collectingCommission.subtract(div);

                        //增加会员余额
                        if(storeCashierRouting.getIsStore().equals("0")) {
                            if (StringUtils.isNotBlank(storeCashierRouting.getMemberListId())) {
                                iMemberListService.addBlance(storeCashierRouting.getMemberListId(), div, orderStoreList.getId(), "49");
                            }
                        }
                        //增加商户余额
                        if(storeCashierRouting.getIsStore().equals("1")) {
                            iStoreManageService.addStoreBlance(storeCashierRouting.getStoreManageId(),div,orderStoreList.getId(),"15");
                        }

                        //分账记录


                    }
                }
    }
}
