package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.store.dto.StoreBankCardDTO;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.mapper.StoreBankCardMapper;
import org.jeecg.modules.store.service.IStoreBankCardService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺银行卡
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
@Service
public class StoreBankCardServiceImpl extends ServiceImpl<StoreBankCardMapper, StoreBankCard> implements IStoreBankCardService {
    @Autowired
    @Lazy
    private IStoreManageService iStoreManageService;

    @Autowired
    private HftxPayUtils hftxPayUtils;

    @Override
    public List<StoreBankCardDTO> findBankCardById(String id) {
        return baseMapper.findBankCardById(id);
    }

    @Override
    public Result<Map<String,Object>> findStoreBankCard() {
        Result<Map<String,Object>> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUser.getId());
        StoreManage one = iStoreManageService.getOne(storeManageQueryWrapper);
        QueryWrapper<StoreBankCard> storeBankCardQueryWrapper = new QueryWrapper<>();
        storeBankCardQueryWrapper.eq("store_manage_id",one.getId());
        List<StoreBankCard> storeBankCards = baseMapper.selectList(storeBankCardQueryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        for (StoreBankCard storeBankCard : storeBankCards) {
            if (storeBankCard.getCarType().equals("0")){
                map.put("bankCard",storeBankCard);
            }else if (storeBankCard.getCarType().equals("1")){
                map.put("alipay",storeBankCard);
            }else {
                return result.error500("未绑定");
            }
        }
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    @Override
    public IPage<StoreBankCard> queryPageList(Page<StoreBankCard> page, StoreBankCardDTO storeBankCardDTO) {
        return baseMapper.queryPageList(page,storeBankCardDTO);
    }

    @Override
    public StoreBankCard getStoreBankCardByStoreManageId(String storeManageId, String carType) {
        return this.getOne(new LambdaQueryWrapper<StoreBankCard>()
                .eq(StoreBankCard::getStoreManageId,storeManageId)
                .eq(StoreBankCard::getCarType,carType)
                .last("limit 1"));
    }

    @Override
    public Result<?> createMemberPrivate(StoreBankCard storeBankCard) {
        //新增会员信息和结算账户对象
        if (hftxPayUtils.createMemberPrivate(storeBankCard.getId(), storeBankCard.getCardholder())) {
            //新增结算账户信息
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("card_id", storeBankCard.getBankCard());
            paramMap.put("card_name", storeBankCard.getCardholder());
            paramMap.put("cert_id", storeBankCard.getIdentityNumber());
            paramMap.put("tel_no", storeBankCard.getPhone());
            paramMap.put("bank_acct_type", "2");
            Map<String, Object> resultMap = hftxPayUtils.createSettleAccountPrivate(storeBankCard.getId(), paramMap);
            if (resultMap.get("status").toString().equals("succeeded")) {
                if (StringUtils.isNotBlank(resultMap.get("id").toString())) {
                    storeBankCard.setSettleAccount(resultMap.get("id").toString());
                    this.saveOrUpdate(storeBankCard);
                    return Result.ok("设置成功");
                } else {
                    return Result.error(String.valueOf(resultMap.get("error_msg")));
                }
            } else {
                return Result.error(String.valueOf(resultMap.get("error_msg")));
            }
        } else {
            return Result.error("分账设置失败");
        }
    }

    @Override
    public Result<?> updateSettleAccountPrivate(StoreBankCard storeBankCard) {
        //修改结算账户对象
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("card_id", storeBankCard.getBankCard());
        paramMap.put("card_name", storeBankCard.getCardholder());
        paramMap.put("cert_id", storeBankCard.getIdentityNumber());
        paramMap.put("tel_no", storeBankCard.getPhone());
        paramMap.put("bank_acct_type", "2");
        Map<String, Object> resultMap = hftxPayUtils.updateSettleAccountPrivate(storeBankCard.getId(), paramMap, storeBankCard.getSettleAccount());
        if (resultMap.get("status").toString().equals("succeeded")) {
            storeBankCard.setSettleAccount(resultMap.get("id").toString());
            this.saveOrUpdate(storeBankCard);
            return Result.ok("设置成功");
        } else {
            storeBankCard.setSettleAccount("");
            this.saveOrUpdate(storeBankCard);
            return Result.error(String.valueOf(resultMap.get("error_msg")));
        }
    }
}
