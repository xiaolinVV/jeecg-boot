package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.store.dto.StoreBankCardDTO;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.mapper.StoreBankCardMapper;
import org.jeecg.modules.store.service.IStoreBankCardService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.vo.StoreBankCardVO;
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
    public IPage<StoreBankCardVO> queryPageList(Page<StoreBankCardVO> page, StoreBankCardDTO storeBankCardDTO) {
        return baseMapper.queryPageList(page,storeBankCardDTO);
    }
}
