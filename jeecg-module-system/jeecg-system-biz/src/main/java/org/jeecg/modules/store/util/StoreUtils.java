package org.jeecg.modules.store.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class StoreUtils {


    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    private IStoreManageService iStoreManageService;

    /**
     * 店铺位置设置
     *
     * @param longitude
     * @param latitude
     * @param mapsMap
     */
    public void setLocation(String longitude,
                            String latitude,Map<String, Object> mapsMap){
        String floorState= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","floor_state");
        if(floorState.equals("0")){
            if (StringUtils.isNotBlank(latitude) && StringUtils.isNotBlank(longitude)) {
                String s1 = latitude + "," + longitude;
                JSONArray mapJsonArray = JSON.parseArray(tengxunMapUtils.findDistance(s1, StringUtils.join(mapsMap.keySet(), ";")));
                if (mapJsonArray != null) {
                    mapJsonArray.stream().forEach(mj -> {
                        JSONObject jb = (JSONObject) mj;
                        Map<String, Object> s = (Map<String, Object>) mapsMap.get(StringUtils.substringBefore(jb.getJSONObject("to").getString("lat"), ".") +
                                "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lat"), "."), 6, "0") +
                                "," + StringUtils.substringBefore(jb.getJSONObject("to").getString("lng"), ".") +
                                "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lng"), "."), 6, "0"));
                        BigDecimal dis = new BigDecimal(jb.getString("distance"));
                        if (dis.doubleValue() > 1000) {
                            s.put("distance", dis.divide(new BigDecimal(1000), 2, RoundingMode.DOWN) + "km");
                        } else {
                            s.put("distance", dis + "m");
                        }

                    });
                }
            }
        }
    }


    /**
     * 设置楼层信息
     *
     * @param resultMap
     */
    public void setFloor(Map<String,Object> resultMap,String storeManageId){
        String floorState= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","floor_state");
        if(floorState.equals("1")){
            StoreManage storeManage=iStoreManageService.getById(storeManageId);
            resultMap.put("distance", storeManage.getFloorName());
        }else{
            resultMap.put("distance", "");
        }
    }
}
