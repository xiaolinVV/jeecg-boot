package org.jeecg.modules.good.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Log
public class GoodUtils {


    /**
     * 图片数据格式的转换
     *
     * @param img
     * @return
     */
    public String imgTransition(String img){
        Object ob=JSON.parse(img);
        if(ob instanceof JSONObject){
            JSONObject jsonObject=(JSONObject)ob;
            return JSON.toJSONString(jsonObject.values());
        }
        return img;
    }

    /**
     * 旧的规格数据转换成功新的规格数据
     *
     * @param specification
     * @return
     */
    public String oldSpecificationToNew(String specification,boolean ispool){
        List<Object> objectList= Lists.newArrayList();
        if(StringUtils.isNotBlank(specification)){
            JSON.parseArray(specification).forEach(s->{
                JSONObject jsonObject=(JSONObject)s;
                Map<String,Object> stringObjectMap= Maps.newHashMap();
                stringObjectMap.put("name",jsonObject.getString("CommodityStyle"));
                List<Object> objectListsub= Lists.newArrayList();
                jsonObject.getJSONArray("classification").forEach(c->{
                    JSONObject jsonObject2=(JSONObject)c;
                    Map<String,Object> stringObjectMapSub= Maps.newHashMap();
                    stringObjectMapSub.put("pName",jsonObject2.getString("value"));
                    stringObjectMapSub.put("salesPrice",0);
                    stringObjectMapSub.put("vipPrice",0);
                    stringObjectMapSub.put("vipTwoPrice",0);
                    stringObjectMapSub.put("vipThirdPrice",0);
                    stringObjectMapSub.put("vipFouthPrice",0);
                    stringObjectMapSub.put("costPrice",0);
                    stringObjectMapSub.put("supplyPrice",0);
                    stringObjectMapSub.put("weight",0);
                    stringObjectMapSub.put("repertory",0);
                    stringObjectMapSub.put("skuNo","");
                    stringObjectMapSub.put("imgUrl","");
                    stringObjectMapSub.put("barCode","");
                    objectListsub.add(stringObjectMapSub);
                });
                stringObjectMap.put("spes",objectListsub);
                objectList.add(stringObjectMap);
            });
        }
        return JSON.toJSONString(objectList);
    }

}
