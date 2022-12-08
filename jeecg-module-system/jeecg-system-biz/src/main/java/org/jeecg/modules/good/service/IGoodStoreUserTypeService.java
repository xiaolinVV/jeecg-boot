package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.entity.GoodStoreUserType;

import java.util.List;

/**
 * @Description: 店铺商品常用分类记录
 * @Author: jeecg-boot
 * @Date:   2019-12-02
 * @Version: V1.0
 */
public interface IGoodStoreUserTypeService extends IService<GoodStoreUserType> {
    List<GoodStoreUserType> getGoodStoreUserType(String sysUserId);

}
