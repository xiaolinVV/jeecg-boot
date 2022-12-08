package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.entity.StorePermissionUid;

import java.util.List;
import java.util.Map;

/**
 * @Description: 客户端菜单授权
 * @Author: jeecg-boot
 * @Date:   2020-04-03
 * @Version: V1.0
 */
public interface IStorePermissionUidService extends IService<StorePermissionUid> {
    /**
     * 查询商家端菜单
     * @param sysUserId
     * @return
     */
     List<Map<String,Object>> getStorePermissionUidMap(String sysUserId);
}
