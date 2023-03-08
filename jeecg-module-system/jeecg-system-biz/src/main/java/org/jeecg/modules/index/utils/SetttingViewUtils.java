package org.jeecg.modules.index.utils;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log
@Component
public class SetttingViewUtils<T> {

    public void setView(Map<String,Object> objectMap, String mapName, Map<String,Object> settingMap,String subMapName, T t,String softModel){
        try {
            //基础设置信息不能为空
            if(t==null){
                settingMap.put(subMapName, "0");
            }else {
                log.info("分端控制：softModel=" + softModel);
                String pointsDisplay= ReflectUtil.getFieldValue(t,"pointsDisplay").toString();
                if (pointsDisplay.equals("0")) {
                    settingMap.put(subMapName, "1");
                } else
                    //小程序
                    if (softModel.equals("0") && pointsDisplay.equals("1")) {
                        settingMap.put(subMapName, "1");
                    } else if ((softModel.equals("1") || softModel.equals("2")) && pointsDisplay.equals("2")) {
                        settingMap.put(subMapName, "1");
                    } else {
                        settingMap.put(subMapName, "0");
                    }
            }
            objectMap.put(mapName,settingMap);
        } catch (UtilException e) {
            e.printStackTrace();
        }
    }
}
