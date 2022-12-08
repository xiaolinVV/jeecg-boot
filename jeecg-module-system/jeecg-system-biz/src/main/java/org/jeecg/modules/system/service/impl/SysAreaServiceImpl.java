package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.mapper.SysAreaMapper;
import org.jeecg.modules.system.service.ISysAreaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 省市区县
 * @Author: jeecg-boot
 * @Date:   2019-10-13
 * @Version: V1.0
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Override
    public List<SysArea> findByParentId(Integer id) {
        List<SysArea> byParentId = baseMapper.findByParentId(id);
        return byParentId;
    }

    @Override
    public List<SysArea> getList() {
        List<SysArea> list = baseMapper.getList();
        return list;
    }

    /**
     *  查询ids的父级集合数据“,”号分隔，代理地区回选用
     * @param ids
     * @return
     */
    @Override
    public Map<String,Object>  getparentIdList(List<String> ids){
        return baseMapper.getparentIdList(ids);
    };
}
