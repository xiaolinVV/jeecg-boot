package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 礼包记录API
 */
@RequestMapping("after/marketingGiftBagRecord")
@Controller
public class AfterMarketingGiftBagRecordController {

    @Autowired
    private IMarketingGiftBagRecordService iMarketingGiftBagRecordService;


    /**
     * 会员回去本礼包id的未支付成功的礼包记录id
     * @param id
     * @return
     */
    @RequestMapping("getMarketingGiftBagRecordByGiftId")
    @ResponseBody
    public Result<?> getMarketingGiftBagRecordByGiftId(String id, @RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap=Maps.newHashMap();
        MarketingGiftBagRecord marketingGiftBagRecord=iMarketingGiftBagRecordService.getOne(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                .eq(MarketingGiftBagRecord::getMemberListId,memberId).eq(MarketingGiftBagRecord::getMarketingGiftBagId,id)
                .ne(MarketingGiftBagRecord::getResiduePayTimes,0)
                .orderByDesc(MarketingGiftBagRecord::getCreateTime)
                .last("limit 1"));
        if(marketingGiftBagRecord!=null){
            resultMap.put("id",marketingGiftBagRecord.getId());
            return Result.ok(resultMap);
        }
        return Result.OK().success("未知错误");
    }


    /**
     * 获取礼包记录列表
     *
     * @param request
     * @return
     */
    @RequestMapping("getMarketingGiftBagRecordList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> getMarketingGiftBagRecordList( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                            HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();

        Result<IPage<Map<String,Object>>> result=new Result<>();

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("memberId",memberId);

        result.setResult(iMarketingGiftBagRecordService.getMarketingGiftBagRecordList(page,paramObjectMap));

        result.success("查询礼包列表成功");
        return result;
    }


    /**
     * 根据礼包记录id获取详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingGiftBagRecordInfo")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingGiftBagRecordInfo(String id){
        Result<Map<String,Object>> result=new Result<>();

        //参数校验
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        result.setResult(iMarketingGiftBagRecordService.findMarketingGiftBagRecordById(id));
        result.success("获取礼包详情成功");
        return result;
    }
}
