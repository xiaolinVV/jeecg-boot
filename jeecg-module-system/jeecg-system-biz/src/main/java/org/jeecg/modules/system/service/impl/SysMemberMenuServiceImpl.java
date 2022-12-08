package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysMemberMenu;
import org.jeecg.modules.system.mapper.SysMemberMenuMapper;
import org.jeecg.modules.system.service.ISysMemberMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 菜单配置表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
@Service
public class SysMemberMenuServiceImpl extends ServiceImpl<SysMemberMenuMapper, SysMemberMenu> implements ISysMemberMenuService {
    //用户端菜单列表
    @Override
    public List<Map<String,Object>> getSysMemberMenuListMap(){
        return  baseMapper.getSysMemberMenuListMap();
    };
}
