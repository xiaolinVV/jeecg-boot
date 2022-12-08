package org.jeecg.modules.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.provider.dto.ProviderRechargeRecordDTO;
import org.jeecg.modules.provider.entity.ProviderBankCard;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.entity.ProviderRechargeRecord;
import org.jeecg.modules.provider.mapper.ProviderRechargeRecordMapper;
import org.jeecg.modules.provider.service.IProviderBankCardService;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.service.IProviderRechargeRecordService;
import org.jeecg.modules.provider.vo.ProviderRechargeRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 供应商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
@Service
public class ProviderRechargeRecordServiceImpl extends ServiceImpl<ProviderRechargeRecordMapper, ProviderRechargeRecord> implements IProviderRechargeRecordService {
@Autowired(required = false)
private  ProviderRechargeRecordMapper providerRechargeRecordMapper;
    @Autowired
    private IProviderManageService providerManageService;
    @Autowired
    private IProviderBankCardService providerBankCardService;



  public  IPage<ProviderRechargeRecordDTO> getProviderRechargeRecord(Page<ProviderRechargeRecord> page, ProviderRechargeRecordVO providerRechargeRecordVO){
      return providerRechargeRecordMapper.getProviderRechargeRecord(page,providerRechargeRecordVO);
  };

    /**
     *系统自动提现调用
     */
    public void  addStoreRechargeRecord(){
        //查询供应商列表
        QueryWrapper<ProviderManage> queryWrapperProviderManage = new QueryWrapper<>();
        queryWrapperProviderManage.eq("status","1");
        queryWrapperProviderManage.gt("balance",0);
        List<ProviderManage> providerManageList= providerManageService.list(queryWrapperProviderManage);

        providerManageList.forEach(pm -> {
            QueryWrapper<ProviderBankCard> queryWrapper = new QueryWrapper();
            queryWrapper.eq("sys_user_id",pm.getSysUserId());
            //店铺银行
            ProviderBankCard  providerBankCard = providerBankCardService.getOne(queryWrapper);
                /*   if(providerBankCard == null){
                       //模板消息推送给供应商添加银行

                    //   result.error500("请先去设置您的银行卡信息");
                   }else {*/
            ProviderRechargeRecord providerRechargeRecord = new ProviderRechargeRecord();
            //         providerRechargeRecord.setProviderBankCardId(providerBankCard.getId());
            //可以提现
            providerRechargeRecord.setDelFlag("0");
            providerRechargeRecord.setPayType("1");
            providerRechargeRecord.setGoAndCome("1");
            providerRechargeRecord.setTradeStatus("1");
            providerRechargeRecord.setSysUserId(pm.getSysUserId());
            providerRechargeRecord.setCreateTime(new Date());
            providerRechargeRecord.setCreateBy("系统");
            providerRechargeRecord.setPayment("0");
            //提现金额 等于 供应商可用金额总和
            providerRechargeRecord.setAmount(pm.getBalance());
            //生成流水号
            String orderNo = OrderNoUtils.getOrderNo();
            providerRechargeRecord.setOrderNo(orderNo);
                     /*  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                       if (sysUser != null) {
                           providerRechargeRecord.setOperator(sysUser.getUsername());
                       }*/
            providerRechargeRecord.setOperator("系统");
            providerRechargeRecord.setRemark("系统自动提现");
            baseMapper.insert(providerRechargeRecord);
            //生成提现订单，减去
            pm.setBalance(pm.getBalance().subtract(providerRechargeRecord.getAmount()));
            pm.setAccountFrozen(pm.getAccountFrozen().add(providerRechargeRecord.getAmount()));
            providerManageService.updateById(pm);
            // }

        });
    }

    @Override
    public IPage<ProviderRechargeRecordVO> queryPageList(Page<ProviderRechargeRecord> page, ProviderRechargeRecordDTO providerRechargeRecordDTO) {
        return baseMapper.queryPageList(page,providerRechargeRecordDTO);
    }

}
