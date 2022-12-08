package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.store.entity.StorePermission;
import org.jeecg.modules.store.entity.StorePermissionUid;
import org.jeecg.modules.store.mapper.StorePermissionMapper;
import org.jeecg.modules.store.mapper.StorePermissionUidMapper;
import org.jeecg.modules.store.service.IStorePermissionUidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 客户端菜单授权
 * @Author: jeecg-boot
 * @Date:   2020-04-03
 * @Version: V1.0
 */
@Service
public class StorePermissionUidServiceImpl extends ServiceImpl<StorePermissionUidMapper, StorePermissionUid> implements IStorePermissionUidService {
    @Autowired(required = false)
    private StorePermissionMapper storePermissionMapper;

    /**
     * 查询商家端菜单
     * @param sysUserId
     * @return
     */
    @Override
    public List<Map<String,Object>> getStorePermissionUidMap(String sysUserId){


            QueryWrapper<StorePermission> queryWrapperStorePermission= new QueryWrapper();
            queryWrapperStorePermission.select("id,name,status,status as isStatus");
            queryWrapperStorePermission.eq("del_flag","0");
        List<Map<String,Object>>  storePermissionMapList = storePermissionMapper.selectMaps(queryWrapperStorePermission);

            QueryWrapper<StorePermissionUid> queryWrapperStorePermissionUid= new QueryWrapper();
            queryWrapperStorePermissionUid.select("id,name,store_permission_id as storePermissionId");
            queryWrapperStorePermissionUid.eq("del_flag","0");
            queryWrapperStorePermissionUid.eq("sys_user_id",sysUserId);
        List<Map<String,Object>>  storePermissionUidMapList  = baseMapper.selectMaps(queryWrapperStorePermissionUid);
        if(storePermissionUidMapList.size()>0){
            Integer  ii= 0;
            for(Map<String,Object> storePermissionMap:storePermissionMapList){
                ii= 0;
                for(Map<String,Object> storePermissionUidMap:storePermissionUidMapList){
                    //启用状态才做是否选中处理
                  if("1".equals(storePermissionMap.get("status"))){
                      //是否被选中
                      if(storePermissionUidMap.get("storePermissionId").equals(storePermissionMap.get("id"))){
                          ii = 1;
                      }
                  }

                }
                if(ii == 1){
                    //选中
                    storePermissionMap.put("status","1");
                }else{
                    //未选中
                    storePermissionMap.put("status","0");
                }
            }
        }


        return storePermissionMapList;
    }

}
