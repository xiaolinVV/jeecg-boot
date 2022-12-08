package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysBackSetting;
import org.jeecg.modules.system.mapper.SysBackSettingMapper;
import org.jeecg.modules.system.service.ISysBackSettingService;
import org.springframework.stereotype.Service;

/**
 * @Description: 后台设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class SysBackSettingServiceImpl extends ServiceImpl<SysBackSettingMapper, SysBackSetting> implements ISysBackSettingService {
    /**
     * 新增
     * @param sysBackSetting
     * @return
     */
    @Override
    public Result<SysBackSetting> add(SysBackSetting sysBackSetting) {
        Result<SysBackSetting> result = new Result<SysBackSetting>();
        try {
            this.save(sysBackSetting);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     * @param sysBackSetting
     * @return
     */
    @Override
    public Result<SysBackSetting> edit(SysBackSetting sysBackSetting) {
        Result<SysBackSetting> result = new Result<SysBackSetting>();
        SysBackSetting sysBackSettingEntity = this.getById(sysBackSetting.getId());
        if(sysBackSettingEntity==null) {
            result.error500("未找到对应实体");
        }else {
            boolean ok = this.updateById(sysBackSetting);
            if(ok) {
                result.success("修改成功!");
              }
        }

        return result;
    }

    /**
     * 后台设置
     * @param sysBackSetting
     * @return
     */
    @Override
    public Result<SysBackSetting> setSysBackSetting(SysBackSetting sysBackSetting) {
        if (StringUtils.isBlank(sysBackSetting.getId())){
            return add(sysBackSetting);
        }else {
            return edit(sysBackSetting);
        }
    }

    /**
     * 后台设置返显
     * @return
     */
    @Override
    public Result<SysBackSetting> findSysBackSetting() {
        Result<SysBackSetting> result = new Result<>();
        QueryWrapper<SysBackSetting> sysBackSettingQueryWrapper = new QueryWrapper<>();
        sysBackSettingQueryWrapper.eq("del_flag","0");
        SysBackSetting one = this.getOne(sysBackSettingQueryWrapper);
        if (oConvertUtils.isEmpty(one)){
            return result.error500("后台未设置");
        }
        result.setCode(200);
        result.setResult(one);
        return result;
    }
}
