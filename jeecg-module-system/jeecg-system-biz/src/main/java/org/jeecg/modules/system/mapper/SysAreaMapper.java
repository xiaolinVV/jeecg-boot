package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysArea;

import java.util.List;
import java.util.Map;

/**
 * @Description: 省市区县
 * @Author: jeecg-boot
 * @Date:   2019-10-13
 * @Version: V1.0
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {
    /**
     * 根据parentId查询
     * @param id
     * @return
     */
    public List<SysArea>findByParentId(@Param("id") Integer id);

    /**
     * 返回省级集合
     * @return
     */
    public List<SysArea>getList();

    /**
     *  查询ids的父级集合数据“,”号分隔，代理地区回选用
     * @param ids
     * @return
     */

     Map<String,Object>  getparentIdList(@Param("ids") List<String> ids);
}
