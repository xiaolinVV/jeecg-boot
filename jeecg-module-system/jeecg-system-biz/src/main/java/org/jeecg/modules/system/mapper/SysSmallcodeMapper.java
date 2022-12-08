package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysSmallcode;

import java.util.Map;

/**
 * @Description: 系统二维码表
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
public interface SysSmallcodeMapper extends BaseMapper<SysSmallcode> {


    /**
     *
     * 获取礼包记录分享码
     *
     * @param paramMap
     * @return
     */
    public String getShareAddress(@Param("paramMap") Map<String,Object> paramMap);


}
