package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysMemberMenu;

import java.util.List;
import java.util.Map;

/**
 * @Description: 菜单配置表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
public interface ISysMemberMenuService extends IService<SysMemberMenu> {
    //用户端菜单列表
    List<Map<String,Object>> getSysMemberMenuListMap();
}
