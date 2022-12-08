package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.mapper.SysSmallcodeMapper;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 系统二维码表
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
@Service
public class SysSmallcodeServiceImpl extends ServiceImpl<SysSmallcodeMapper, SysSmallcode> implements ISysSmallcodeService {

    @Override
    public String getShareAddress(String marketingGiftBagId,String memberListId) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("marketingGiftBagId",marketingGiftBagId);
        paramMap.put("memberListId",memberListId);
        return baseMapper.getShareAddress(paramMap);
    }
}
