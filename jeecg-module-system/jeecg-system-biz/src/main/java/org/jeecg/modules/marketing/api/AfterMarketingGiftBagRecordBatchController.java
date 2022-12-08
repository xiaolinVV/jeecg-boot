package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchDeliveryService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 礼包需要的API接口
 */
@RequestMapping("after/marketingGiftBagRecordBatch")
@Controller
@Slf4j
public class AfterMarketingGiftBagRecordBatchController {
    @Autowired
    private IMarketingGiftBagRecordBatchDeliveryService iMarketingGiftBagRecordBatchDeliveryService;

    @Autowired
    private IMarketingGiftBagRecordBatchService iMarketingGiftBagRecordBatchService;
    @RequestMapping("findMarketingGiftBagRecordBatchDeliveryById")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingGiftBagRecordBatchDeliveryById(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                     String id,
                                                                                     String phone,
                                                                                     HttpServletRequest request){
        Result<IPage<Map<String, Object>>> result = new Result<>();
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("phone",phone);
        result.setResult(iMarketingGiftBagRecordBatchDeliveryService.findMarketingGiftBagRecordBatchDeliveryById(page,map));
        result.success("返回配送信息");
        return result;
    }


    @RequestMapping("findMarketingGiftBagRecordBatchInfoById")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingGiftBagRecordBatchInfoById(String id ,
                                                                              HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();

        HashMap<String, Object> map = new HashMap<>();
        MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = iMarketingGiftBagRecordBatchService.getById(id);
        map.put("id",marketingGiftBagRecordBatch.getId());
        map.put("giftName",marketingGiftBagRecordBatch.getGiftName());
        map.put("coverPlan",marketingGiftBagRecordBatch.getCoverPlan());
        map.put("posters",marketingGiftBagRecordBatch.getPosters());
        map.put("giftDeals",marketingGiftBagRecordBatch.getGiftDeals());
        map.put("price",marketingGiftBagRecordBatch.getPrice());
        result.setResult(map);
        result.success("返回已购买礼包详情");
        return result;
    }
}
