package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupRecord;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("after/marketingZoneGroupRecord")
@Controller
@Slf4j
public class AfterMarketingZoneGroupRecordController {
    @Autowired
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;

    /**
     * 我的拼团记录列表
     * @param memberId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingZoneGroupRecordByMemberId")
    @ResponseBody
    public Result<?> getMarketingZoneGroupRecordByMemberId(@RequestAttribute(required = false, name = "memberId") String memberId,
                                                           String status,
                                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("status",status);
        return Result.ok(iMarketingZoneGroupRecordService.getMarketingZoneGroupRecordByMemberId(new Page<Map<String,Object>>(pageNo,pageSize),map));
    }

    /**
     * 拼团记录详情
     * @param id
     * @return
     */
    @RequestMapping("getMarketingZoneGroupRecordDetails")
    @ResponseBody
    public Result<?> getMarketingZoneGroupRecordDetails(String id){
        if (StringUtils.isBlank(id)){
            return Result.error("前端id未传递!");
        }
        MarketingZoneGroupRecord marketingZoneGroupRecord = iMarketingZoneGroupRecordService.getById(id);
        if (marketingZoneGroupRecord!=null){
            return Result.ok(iMarketingZoneGroupRecordService.getMarketingZoneGroupRecordDetails(marketingZoneGroupRecord));
        }else {
            return Result.error("未找到对应的记录");
        }

    }
}
