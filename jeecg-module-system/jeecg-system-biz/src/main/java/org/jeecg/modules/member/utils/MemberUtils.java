package org.jeecg.modules.member.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@Component
@Log
public class MemberUtils {

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysDictService iSysDictService;


    /**
     * 注册时间限制
     *
     * @return
     */
    public boolean registrationDate(){
        String registeredLimit = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "registered_limit");

        if(StringUtils.isNotBlank(registeredLimit)&&registeredLimit.equals("1")) {
            String openRegistrationTime = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "open_registration_time");
            String closeRegistrationTime = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "close_registration_time");
            try {
                Date startTime = DateUtils.parseDate(DateUtils.formatDate() + " " + openRegistrationTime, "yyyy-MM-dd HH:mm:ss");
                Date endTime = DateUtils.parseDate(DateUtils.formatDate() + " " + closeRegistrationTime, "yyyy-MM-dd HH:mm:ss");
                if (new Date().getTime() >= startTime.getTime() && new Date().getTime() <= endTime.getTime()) {
                    return false;
                } else {
                    log.info("不允许用户在这个时间注册");
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 注册上线人数限制
     *
     * @return
     */
    public boolean registerOnline(){
        String registeredLimit = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "registered_limit");
        if(StringUtils.isNotBlank(registeredLimit)&&registeredLimit.equals("1")) {
            String limitRegisterNumber = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "limit_register_number");
            if (StringUtils.isNotBlank(limitRegisterNumber) && !limitRegisterNumber.equals("-1")) {
                log.info("今日当前系统注册上线值：" + limitRegisterNumber);
                Calendar calendar = Calendar.getInstance();
                long count = iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getYear, calendar.get(Calendar.YEAR))
                        .eq(MemberList::getMonth, calendar.get(Calendar.MONTH) + 1)
                        .eq(MemberList::getDay, calendar.get(Calendar.DAY_OF_MONTH)));
                log.info("今日系统注册人数：" + count);
                if (count >= new BigDecimal(limitRegisterNumber).intValue()) {
                    log.info("会员达到上限注册数量");
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取推荐人信息
     *
     * @param promoterType
     * @param promoter
     * @return
     */
    public String getPromoterMan(String promoterType,String promoter){
        if(StringUtils.isNotBlank(promoterType)){
            if(promoterType.equals("0")){
                //店铺
                StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                        .eq(StoreManage::getSysUserId,promoter)
                        .in(StoreManage::getPayStatus,"1","2")
                        .last("limit 1"));
                if (storeManage != null){
                    if (storeManage.getSubStoreName() == null) {
                        return storeManage.getStoreName();
                    } else {
                        return storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")";
                    }
                }else {
                    return "无";
                }

            }
            if(promoterType.equals("1")){
                //会员
                MemberList promoterMemberList=iMemberListService.getById(promoter);
                if(promoterMemberList!=null) {
                    if (StringUtils.isNotBlank(promoterMemberList.getNickName())) {
                        return promoterMemberList.getNickName();
                    } else {
                        return promoterMemberList.getPhone();
                    }
                }else{
                   return "无";
                }
            }
            //平台
            if (promoterType.equals("2")){
                return "平台";
            }
        }else{
           return "无";
        }
        return "无";
    }
}
