package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.good.entity.GoodStoreUserType;
import org.jeecg.modules.good.mapper.GoodStoreUserTypeMapper;
import org.jeecg.modules.good.service.IGoodStoreUserTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 店铺商品常用分类记录
 * @Author: jeecg-boot
 * @Date:   2019-12-02
 * @Version: V1.0
 */
@Service
public class GoodStoreUserTypeServiceImpl extends ServiceImpl<GoodStoreUserTypeMapper, GoodStoreUserType> implements IGoodStoreUserTypeService {
  public List<GoodStoreUserType> getGoodStoreUserType( String sysUserId){
      return baseMapper.getGoodStoreUserType(sysUserId);
  };
}
