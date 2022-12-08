package org.jeecg.modules.good.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.good.entity.GoodStoreUserType;

import java.util.List;

/**
 * @Description: 店铺商品常用分类记录
 * @Author: jeecg-boot
 * @Date:   2019-12-02
 * @Version: V1.0
 */
public interface GoodStoreUserTypeMapper extends BaseMapper<GoodStoreUserType> {

    List<GoodStoreUserType> getGoodStoreUserType(@Param("sysUserId") String sysUserId);
}
