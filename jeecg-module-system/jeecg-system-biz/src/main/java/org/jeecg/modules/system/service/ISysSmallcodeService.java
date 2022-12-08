package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysSmallcode;

/**
 * @Description: 系统二维码表
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
public interface ISysSmallcodeService extends IService<SysSmallcode> {

    /**
     *
     * 获取礼包记录分享码
     *
     * @param marketingGiftBagId
     * @param memberListId
     * @return
     */
    public String getShareAddress(String marketingGiftBagId,String memberListId);

}
