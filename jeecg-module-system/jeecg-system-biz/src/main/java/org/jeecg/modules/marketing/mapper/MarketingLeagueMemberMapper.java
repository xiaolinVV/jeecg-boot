package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingLeagueMemberDTO;
import org.jeecg.modules.marketing.entity.MarketingLeagueMember;

import java.util.Map;

/**
 * @Description: 加盟专区-人员关系
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
public interface MarketingLeagueMemberMapper extends BaseMapper<MarketingLeagueMember> {

    /**
     * 查询加盟商会员列表
     *
     * @param page
     * @param marketingLeagueMemberDTO
     * @marketingLeagueMemberDTO
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param("marketingLeagueMemberDTO") MarketingLeagueMemberDTO marketingLeagueMemberDTO);


    /**
     * 查询加盟商会员列表通过获得方式
     *
     * @param page
     * @param marketingLeagueMemberDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageListByGetWay(Page<Map<String,Object>> page,@Param("marketingLeagueMemberDTO") MarketingLeagueMemberDTO marketingLeagueMemberDTO);

    /**
     * 获取人员列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingLeagueMemberList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);
}
