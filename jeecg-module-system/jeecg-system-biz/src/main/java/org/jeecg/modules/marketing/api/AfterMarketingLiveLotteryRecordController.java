package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingLiveLotteryRecord;
import org.jeecg.modules.marketing.service.IMarketingLiveLotteryRecordService;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("after/marketingLiveLotteryRecord")
@Controller
public class AfterMarketingLiveLotteryRecordController {
    @Autowired
    private IMarketingLiveLotteryRecordService iMarketingLiveLotteryRecordService;
    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;
    /**
     * 获取奖品列表
     * @param memberId
     * @param marketingLiveStreamingId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingLiveLotteryRecordList")
    @ResponseBody
    public Result<?> getMarketingLiveLotteryRecordList(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                       String marketingLiveStreamingId,
                                                       @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        if (StringUtils.isBlank(marketingLiveStreamingId)){
            return Result.error("前端直播间id未传递!");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("marketingLiveStreamingId",marketingLiveStreamingId);
        return Result.ok(iMarketingLiveLotteryRecordService.getMarketingLiveLotteryRecordList(new Page<Map<String,Object>>(pageNo,pageSize),map));
    }

    /**
     * 领取礼品
     * @param id
     * @return
     */
    @RequestMapping("marketingLiveLotteryRecordGet")
    @ResponseBody
    public Result<?> marketingLiveLotteryRecordGet(String id,
                                                   String memberShippingAddressId){
        if (StringUtils.isBlank(id)){
            return Result.error("前端id未传递!");
        }
        if (StringUtils.isBlank(memberShippingAddressId)){
            return Result.error("前端收货地址id未传递");
        }
        MarketingLiveLotteryRecord marketingLiveLotteryRecord = iMarketingLiveLotteryRecordService.getById(id);
        if (marketingLiveLotteryRecord!=null){
            MemberShippingAddress memberShippingAddress = iMemberShippingAddressService.getById(memberShippingAddressId);
            if (memberShippingAddress==null){
                return Result.error("收货地址id,传递有误!");
            }
            marketingLiveLotteryRecord.setConsignee(memberShippingAddress.getLinkman());
            marketingLiveLotteryRecord.setContactNumber(memberShippingAddress.getPhone());
            marketingLiveLotteryRecord.setShippingAddress(memberShippingAddress.getAreaExplan()+memberShippingAddress.getAreaAddress());
            marketingLiveLotteryRecord.setSysAreaId(memberShippingAddress.getSysAreaId());
            marketingLiveLotteryRecord.setDrawStatus("1");
            marketingLiveLotteryRecord.setDrawTime(new Date());
            boolean b = iMarketingLiveLotteryRecordService.updateById(marketingLiveLotteryRecord);
            if (b){
                return Result.ok("领取成功!");
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("领取失败,请重试");
            }
        }else {
            return Result.error("id传递有误,未找到实体");
        }

    }

    /**
     * 我的直播奖品列表
     * @param memberId
     * @param drawStatus
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingLiveLotteryRecordPage")
    @ResponseBody
    public Result<?> findMarketingLiveLotteryRecordPage(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                        String drawStatus,
                                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){

        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("drawStatus",drawStatus);
        return Result.ok(iMarketingLiveLotteryRecordService.getMarketingLiveLotteryRecordList(new Page<Map<String,Object>>(pageNo,pageSize),map));
    }

    /**
     * 开奖
     * @param memberId
     * @param duration
     * @param marketingLiveLotteryId
     * @return
     */
    @RequestMapping("runLottery")
    @ResponseBody
    public Result<?>runLottery(@RequestAttribute(value = "memberId",required = false) String memberId,
                               long duration,
                               String marketingLiveLotteryId){
        return Result.ok(iMarketingLiveLotteryRecordService.runLottery(memberId,duration,marketingLiveLotteryId));
    }
}
