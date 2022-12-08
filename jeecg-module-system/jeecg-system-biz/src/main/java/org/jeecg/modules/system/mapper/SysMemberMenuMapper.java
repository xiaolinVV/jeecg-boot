package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysMemberMenu;

import java.util.List;
import java.util.Map;

/**
 * @Description: 菜单配置表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
public interface SysMemberMenuMapper extends BaseMapper<SysMemberMenu> {
    //用户端菜单列表
    List<Map<String,Object>> getSysMemberMenuListMap();
}
