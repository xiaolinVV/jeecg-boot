package org.jeecg.modules.member.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PromotionCodeUtils {

    @Autowired
    private IMemberListService iMemberListService;

    public String getCode(){
        String code="";
        while (true){
            code= oConvertUtils.randomGen(6);
            if(iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPromotionCode,code))==0){
                break;
            }
        }
        return code;
    }
}
