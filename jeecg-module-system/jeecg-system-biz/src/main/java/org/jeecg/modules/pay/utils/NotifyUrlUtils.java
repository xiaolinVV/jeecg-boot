package org.jeecg.modules.pay.utils;

import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 回调地址工具
 */

@Component
public class
NotifyUrlUtils {

    @Autowired
    private ISysDictService iSysDictService;


    /**
     * 获取回调地址
     *
     * @param sysDictText
     * @return
     */
    public String getNotify(String sysDictText){
        String sysBaseUrlApi=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "sys_base_url_api ");
        String api=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", sysDictText);
        return sysBaseUrlApi+api;
    }

    /**
     * 获取地址
     *
     * @param sysDictText
     * @return
     */
    public String getBseUrl(String sysDictText){
        String sysBaseUrl=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "sys_base_url ");
        String api=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", sysDictText);
        return sysBaseUrl+api;
    }

    /**
     * 获取图片地址
     *
     * @param sysDictText
     * @return
     */
    public String getImgUrl(String sysDictText){
        String sysBaseImgUrl=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "sys_base_img_url ");
        String api=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", sysDictText);
        return sysBaseImgUrl+api;
    }

}
