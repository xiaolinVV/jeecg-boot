package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.dto.GoodTypeDto;
import org.jeecg.modules.good.entity.GoodUserType;

import java.util.List;

/**
 * @Description: 最近使用的分类
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
public interface IGoodUserTypeService extends IService<GoodUserType> {

  List<GoodTypeDto> listBySysUserName(String username);

  List<GoodUserType> getGoodUserType( String sysUserId);
}
