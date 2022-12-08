package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingGroupRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 中奖拼团记录列表
 * 张靠勤   2021-4-5
 */
@RequestMapping("after/marketingGroupRecord")
@Controller
public class AfterMarketingGroupRecordController {


    @Autowired
    private IMarketingGroupRecordService iMarketingGroupRecordService;



    /**
     * 根据参团记录id，查询参团数据
     *
     * @param marketingGroupRecordId
     * @return
     */
    @RequestMapping("getMarketingGroupRecordById")
    @ResponseBody
    public Result<Map<String,Object>> getMarketingGroupRecordById(String marketingGroupRecordId){
        Result<Map<String,Object>> result=new Result<>();
        result.setResult(iMarketingGroupRecordService.getMarketingGroupRecordById(marketingGroupRecordId));
        result.success("查询参团记录成功");
        return result;
    }


    /**
     *
     * 查询中奖拼团记录列表
     *
     * 张靠勤   2021-4-5
     *
     * @param memberId
     * @return
     */
    @RequestMapping("getMarketingGroupRecordByMemberId")
    @ResponseBody
    public Result<?> getMarketingGroupRecordByMemberId(String status
            ,@RequestAttribute(value = "memberId",required = false) String memberId
            ,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo
            ,@RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String, Object>>> result=new Result<>();

        if(StringUtils.isBlank(status)){
            return result.error500("中奖状态值不能为空");
        }
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("status",status);
        paramObjectMap.put("memberId",memberId);
        result.setResult(iMarketingGroupRecordService.getMarketingGroupRecordByMemberId(page,paramObjectMap));
        result.success("查询中奖拼团记录列表成功！！！");
        return result;
    }
}
