package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.MapHandleUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingIntegralSetting;
import org.jeecg.modules.marketing.service.IMarketingIntegralRecordService;
import org.jeecg.modules.marketing.service.IMarketingIntegralSettingService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.marketing.vo.MarketingIntegralRecordVO;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 积分记录
 */
@RequestMapping("after/marketingIntegralRecord")
@Controller
public class AfterMarketingIntegralRecordController {

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMarketingIntegralSettingService iMarketingIntegralSettingService;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMarketingIntegralRecordService iMarketingIntegralRecordService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    private IMemberListService iMemberListService;

    /**
     * 积分转化
     *
     * @param integral
     * @param memberId
     * @return
     */
    @RequestMapping("toWelfarePayments")
    @ResponseBody
    public Result<?> toWelfarePayments(BigDecimal integral,
                                       @RequestAttribute("memberId") String memberId){

        MarketingIntegralSetting marketingIntegralSetting=iMarketingIntegralSettingService.getOne(new LambdaQueryWrapper<MarketingIntegralSetting>()
                .eq(MarketingIntegralSetting::getStatus,"1"));

        String exchangeWelfareMinimum=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "exchange_welfare_minimum");

        if(integral.doubleValue()<=0){
            return Result.error("积分不能为空");
        }
        if (iMemberListService.getById(memberId).getIntegral().doubleValue()<integral.doubleValue()){
            return Result.error("积分不足");
        }
        BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
        BigDecimal welfarePayments=integral.multiply(marketingIntegralSetting.getPrice()).divide(integralValue,2, RoundingMode.DOWN);
        if(welfarePayments.subtract(new BigDecimal(exchangeWelfareMinimum)).doubleValue()<0){
            return Result.error("您兑换的额度太小");
        }

        //减少积分
        iMarketingIntegralRecordService.subtractMarketingIntegralRecord("6",integral,memberId, OrderNoUtils.getOrderNo());
        //增加第一积分
        iMemberWelfarePaymentsService.addWelfarePayments(memberId,welfarePayments,"18",OrderNoUtils.getOrderNo(),"");

        return Result.ok("转换成功");
    }

    /**
     * 明细
     * @param marketingIntegralRecordVO
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @param req
     * @return
     */
    @RequestMapping("findMarketingIntegralRecordList")
    @ResponseBody
    public Result<?> findMarketingIntegralRecordList(MarketingIntegralRecordVO marketingIntegralRecordVO,
                                                     @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                     @RequestAttribute("memberId") String memberId,
                                                     HttpServletRequest req){
        Page<MarketingIntegralRecordVO> page = new Page<MarketingIntegralRecordVO>(pageNo, pageSize);
        QueryWrapper<MarketingIntegralRecordVO> queryWrapper = QueryGenerator.initQueryWrapper(marketingIntegralRecordVO, req.getParameterMap());
        queryWrapper.eq("member_list_id",memberId);
        return Result.ok(iMarketingIntegralRecordService.queryPageList(page,queryWrapper, MapHandleUtils.handleRequestMap(req.getParameterMap())));
    }

    /**
     * 任务记录
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @return
     */
    @RequestMapping("getMarketingIntegralRecordPageMap")
    @ResponseBody
    public Result<?> getMarketingIntegralRecordPageMap(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                       @RequestAttribute("memberId") String memberId){
        return Result.ok(iMarketingIntegralRecordService.getMarketingIntegralRecordPageMap(new Page<Map<String,Object>>(pageNo,pageSize),memberId));
    }
}
