package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.entity.StoreFranchiser;
import org.jeecg.modules.store.entity.StoreFranchiserMember;
import org.jeecg.modules.store.mapper.StoreFranchiserMapper;
import org.jeecg.modules.store.service.IStoreFranchiserMemberService;
import org.jeecg.modules.store.service.IStoreFranchiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Description: 店铺经销商
 * @Author: jeecg-boot
 * @Date:   2022-10-26
 * @Version: V1.0
 */
@Service
public class StoreFranchiserServiceImpl extends ServiceImpl<StoreFranchiserMapper, StoreFranchiser> implements IStoreFranchiserService {

    @Autowired
    private IStoreFranchiserMemberService iStoreFranchiserMemberService;

    @Autowired
    private IMemberListService iMemberListService;


    @Override
    public StoreFranchiser findStoreFranchiser(String memberId,String storeManageId) {

        StoreFranchiser storeFranchiser=null;

        storeFranchiser=this.getOne(new LambdaQueryWrapper<StoreFranchiser>()
                .eq(StoreFranchiser::getStoreManageId,storeManageId)
                .eq(StoreFranchiser::getMemberListId,memberId));

        if(storeFranchiser==null){

            StoreFranchiserMember storeFranchiserMember=iStoreFranchiserMemberService.getOne(new LambdaQueryWrapper<StoreFranchiserMember>()
                    .eq(StoreFranchiserMember::getStoreManageId,storeManageId)
                    .eq(StoreFranchiserMember::getMemberListId,memberId));
            if(storeFranchiserMember!=null){
                storeFranchiser= this.getById(storeFranchiserMember.getStoreFranchiserId());
            }

        }
        return storeFranchiser;
    }

    @Override
    public void joinStoreFranchiser(StoreFranchiser storeFranchiser, String memberId) {
        StoreFranchiserMember storeFranchiserMember=new StoreFranchiserMember();
        storeFranchiserMember.setStoreFranchiserId(storeFranchiser.getId());
        storeFranchiserMember.setMemberListId(memberId);
        storeFranchiserMember.setStoreManageId(storeFranchiser.getStoreManageId());
        iStoreFranchiserMemberService.save(storeFranchiserMember);
    }

    @Override
    public void awardStoreFranchiser(StoreFranchiser storeFranchiser, BigDecimal award,String buyMemberId) {
        if(award.doubleValue()>0){
            iMemberListService.addBlance(storeFranchiser.getMemberListId(),award,buyMemberId,"51");
        }
    }
}
