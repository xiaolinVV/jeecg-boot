package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreSetting;
import org.jeecg.modules.store.mapper.StoreSettingMapper;
import org.jeecg.modules.store.service.IStoreSettingService;
import org.springframework.stereotype.Service;

/**
 * @Description: 店铺设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class StoreSettingServiceImpl extends ServiceImpl<StoreSettingMapper, StoreSetting> implements IStoreSettingService {
    /**
     * 新增
     * @param storeSetting
     * @return
     */
    @Override
    public Result<StoreSetting> add(StoreSetting storeSetting) {
        Result<StoreSetting> result = new Result<StoreSetting>();
        try {
            this.save(storeSetting);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     * @param storeSetting
     * @return
     */
    @Override
    public Result<StoreSetting> edit(StoreSetting storeSetting) {
        Result<StoreSetting> result = new Result<StoreSetting>();
        StoreSetting storeSettingEntity = this.getById(storeSetting.getId());
        if(storeSettingEntity==null) {
            result.error500("未找到对应实体");
        }else {
            boolean ok = this.updateById(storeSetting);
            if(ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 店铺设置
     * @param storeSetting
     * @return
     */
    @Override
    public Result<StoreSetting> setStoreSetting(StoreSetting storeSetting) {
        if (StringUtils.isBlank(storeSetting.getId())){
            return add(storeSetting);
        }else {
            return edit(storeSetting);
        }
    }

    @Override
    public Result<StoreSetting> findStoreSetting() {
        Result<StoreSetting> result = new Result<>();
        QueryWrapper<StoreSetting> storeSettingQueryWrapper = new QueryWrapper<>();
        storeSettingQueryWrapper.eq("del_flag","0");
        StoreSetting one = this.getOne(storeSettingQueryWrapper);
        result.setResult(one);
        return result;
    }


}
