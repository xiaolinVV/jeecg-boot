package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysFrontSetting;
import org.jeecg.modules.system.mapper.SysFrontSettingMapper;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 小程序前端设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class SysFrontSettingServiceImpl extends ServiceImpl<SysFrontSettingMapper, SysFrontSetting> implements ISysFrontSettingService {
    /**
     * 新增
     * @param sysFrontSetting
     * @return
     */
    @Override
    public Result<SysFrontSetting> add(SysFrontSetting sysFrontSetting) {
        Result<SysFrontSetting> result = new Result<SysFrontSetting>();
        try {
            this.save(sysFrontSetting);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     * @param sysFrontSetting
     * @return
     */
    @Override
    public Result<SysFrontSetting> edit(SysFrontSetting sysFrontSetting) {
        Result<SysFrontSetting> result = new Result<SysFrontSetting>();
        SysFrontSetting sysFrontSettingEntity = this.getById(sysFrontSetting.getId());
        if(sysFrontSettingEntity==null) {
            result.error500("未找到对应实体");
        }else {
            boolean ok = this.updateById(sysFrontSetting);
            if(ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 小程序或前端设置
     * @param sysFrontSetting
     * @return
     */
    @Override
    public Result<SysFrontSetting> setSysFrontSetting(SysFrontSetting sysFrontSetting) {
        if (StringUtils.isBlank(sysFrontSetting.getId())){
            return add(sysFrontSetting);
        }else {
            return edit(sysFrontSetting);
        }
    }

    @Override
    public Result<SysFrontSetting> findSysFrontSetting() {
        Result<SysFrontSetting> result = new Result<>();
        QueryWrapper<SysFrontSetting> sysFrontSettingQueryWrapper = new QueryWrapper<>();
        sysFrontSettingQueryWrapper.eq("del_flag","0");
        SysFrontSetting one = this.getOne(sysFrontSettingQueryWrapper);
        result.setResult(one);
        return result;
    }

    @Override
    public Map<String, Object> findSysFrontSettingMap() {
        return baseMapper.findSysFrontSettingMap();
    }

}
