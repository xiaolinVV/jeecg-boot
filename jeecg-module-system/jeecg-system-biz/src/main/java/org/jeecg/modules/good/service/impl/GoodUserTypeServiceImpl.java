package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.good.dto.GoodTypeDto;
import org.jeecg.modules.good.dto.GoodUserTypeDto;
import org.jeecg.modules.good.entity.GoodUserType;
import org.jeecg.modules.good.mapper.GoodUserTypeMapper;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.jeecg.modules.good.service.IGoodUserTypeService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 最近使用的分类
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
@Service
public class GoodUserTypeServiceImpl extends ServiceImpl<GoodUserTypeMapper, GoodUserType> implements IGoodUserTypeService {
  @Autowired(required = false)
  private GoodUserTypeMapper goodUserTypeMapper;
  @Autowired(required = false)
  private IGoodTypeService goodTypeService;
  @Autowired
  private ISysUserService iSysUserService;
  @Override
  public List<GoodTypeDto> listBySysUserName(String username) {
    SysUser sysUser=iSysUserService.getUserByName(username);
    List<GoodUserTypeDto> goodUserTypes=goodUserTypeMapper.getBySysUserId(sysUser.getId());
    List<GoodTypeDto> guts=new ArrayList<GoodTypeDto>();
    goodUserTypes.forEach(gut->{
      GoodTypeDto goodTypeDto=goodTypeService.getGoodTypeByGoodTyeId3(gut.getId());
      guts.add(goodTypeDto);
    });
    return guts;
  }
 public List<GoodUserType> getGoodUserType( String sysUserId){
    return baseMapper.getGoodUserType( sysUserId);
 }
}
