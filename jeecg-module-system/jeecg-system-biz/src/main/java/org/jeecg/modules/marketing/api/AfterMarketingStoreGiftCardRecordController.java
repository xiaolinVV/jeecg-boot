package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping("after/marketingStoreGiftCardRecord")
public class AfterMarketingStoreGiftCardRecordController {
    @Autowired
    private IMarketingStoreGiftCardRecordService iMarketingStoreGiftCardRecordService;

    /**
     * 使用明细列表
     * @param goAndCome
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingStoreGiftCardRecordList")
    @ResponseBody
    public Result<?> findMarketingStoreGiftCardRecordList(String goAndCome,
                                                          String marketingStoreGiftCardMemberListId,
                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        HashMap<String, Object> map = new HashMap<>();
        map.put("marketingStoreGiftCardMemberListId",marketingStoreGiftCardMemberListId);
        map.put("goAndCome",goAndCome);
        return Result.ok(iMarketingStoreGiftCardRecordService.findMarketingStoreGiftCardRecordList(new Page<>(pageNo,pageSize),map));
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingStoreGiftCardRecordParticulars")
    @ResponseBody
    public Result<?> findMarketingStoreGiftCardRecordParticulars(String id){
        if (StringUtils.isBlank(id)){
            return Result.error("id未传递!");
        }
        return Result.ok(iMarketingStoreGiftCardRecordService.findMarketingStoreGiftCardRecordParticulars(id));
    }
}
