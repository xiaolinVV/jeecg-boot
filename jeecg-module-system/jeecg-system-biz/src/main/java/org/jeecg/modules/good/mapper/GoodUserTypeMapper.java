package org.jeecg.modules.good.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.good.dto.GoodUserTypeDto;
import org.jeecg.modules.good.entity.GoodUserType;

import java.util.List;

/**
 * @Description: 最近使用的分类
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
public interface GoodUserTypeMapper extends BaseMapper<GoodUserType> {

  List<GoodUserTypeDto> getBySysUserId(@Param("sysUserId") String sysUserId);
  List<GoodUserType> getGoodUserType(@Param("sysUserId") String sysUserId);

}
