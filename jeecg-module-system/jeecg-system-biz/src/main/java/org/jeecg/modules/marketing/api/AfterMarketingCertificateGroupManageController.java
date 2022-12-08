package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupManage;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupRecord;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupListService;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupRecordService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("after/marketingCertificateGroupManage")
@Controller
@Slf4j
public class AfterMarketingCertificateGroupManageController {
    @Autowired
    private IMarketingCertificateGroupManageService iMarketingCertificateGroupManageService;
    @Autowired
    private IMarketingCertificateGroupRecordService iMarketingCertificateGroupRecordService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMarketingCertificateGroupBaseSettingService iMarketingCertificateGroupBaseSettingService;
    @Autowired
    private IMarketingCertificateGroupListService iMarketingCertificateGroupListService;

    /**
     * 我的拼团列表
     * @param status
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("myMarketingCertificateGroupManage")
    @ResponseBody
    public Result<?> myMarketingCertificateGroupManage(String status,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest request){
        String memberId = request.getAttribute("memberId").toString();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        return Result.ok(iMarketingCertificateGroupManageService.myMarketingCertificateGroupManage(page,memberId,status));
    }

    /**
     * 我的拼团详情
     * @return
     */
    @RequestMapping("myMarketingCertificateGroupManageDetails")
    @ResponseBody
    public Result<?> myMarketingCertificateGroupManageDetails(String id,
                                                              HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(id)){
            return Result.error("id为空");
        }
        MarketingCertificateGroupRecord marketingCertificateGroupRecord = iMarketingCertificateGroupRecordService.getById(id);
        if (marketingCertificateGroupRecord ==null){
            return Result.error("id传输错误");
        }

        //拼团id
        map.put("id",marketingCertificateGroupRecord.getMarketingCertificateGroupManageId());
        //状态
        map.put("status",marketingCertificateGroupRecord.getStatus());

        //成团时间
        Map<String, Object> info = iMarketingCertificateGroupManageService.getInfo(marketingCertificateGroupRecord.getMarketingCertificateGroupManageId());
        //成团时间
        map.put("cloudsTime",info.get("surplusTime"));
        MarketingCertificateGroupBaseSetting certificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0"));
        //分享图
        map.put("coverPlan",certificateGroupBaseSetting.getCoverPlan());
        if (marketingCertificateGroupRecord.getStatus().equals("1")){
            map.put("marketingCertificateRecordId",marketingCertificateGroupRecord.getMarketingCertificateRecordId());
        }
        if (marketingCertificateGroupRecord.getStatus().equals("2")){
            map.put("marketingCertificateGroupListId",marketingCertificateGroupRecord.getMarketingCertificateGroupListId());
        }

        List<Map<String, Object>> memberListByMarketingCertificateGroupManageId = iMarketingCertificateGroupRecordService.getMemberListByMarketingCertificateGroupManageId(marketingCertificateGroupRecord.getMarketingCertificateGroupManageId());
        //参团人
        map.put("memberList",memberListByMarketingCertificateGroupManageId);
        //兑换券名称
        map.put("certificateName",marketingCertificateGroupRecord.getCertificateName());
        //主图
        map.put("mainPicture",marketingCertificateGroupRecord.getMainPicture());
        //市场价
        map.put("marketPrice",marketingCertificateGroupRecord.getMarketPrice());
        //活动价
        map.put("activityPrice",marketingCertificateGroupRecord.getActivityPrice());
        //优惠金额
        map.put("price",marketingCertificateGroupRecord.getMarketPrice().subtract(marketingCertificateGroupRecord.getActivityPrice()));
        //活动别名
        map.put("anotherName",certificateGroupBaseSetting.getAnotherName());
        //活动标签
        map.put("label",certificateGroupBaseSetting.getLabel());
        return Result.ok(map);
    }
    @RequestMapping("findMarketingCertificateGroupManageDetailsByManageId")
    @ResponseBody
    public Result<?>findMarketingCertificateGroupManageDetailsByManageId(String id,
                                                                         HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<>();
        String memberId = request.getAttribute("memberId").toString();
        MarketingCertificateGroupManage marketingCertificateGroupManage = iMarketingCertificateGroupManageService.getById(id);
        //拼团id
        map.put("id",marketingCertificateGroupManage.getId());
        //状态
        map.put("status",marketingCertificateGroupManage.getStatus());
        if (marketingCertificateGroupManage.getStatus().equals("1")){
            if (iMarketingCertificateGroupRecordService.count(new LambdaQueryWrapper<MarketingCertificateGroupRecord>()
                    .eq(MarketingCertificateGroupRecord::getDelFlag,"0")
                    .eq(MarketingCertificateGroupRecord::getMarketingCertificateGroupManageId,marketingCertificateGroupManage.getId())
                    .eq(MarketingCertificateGroupRecord::getMemberListId,memberId)
            )>0){
                map.put("status","1");
            }else {
                map.put("status","3");
            }
        }
        if (marketingCertificateGroupManage.getStatus().equals("2")){
            if (marketingCertificateGroupManage.getEndTime().compareTo(new Date())>-1){
                map.put("status","4");
            }
        }
        Map<String, Object> info = iMarketingCertificateGroupManageService.getInfo(marketingCertificateGroupManage.getId());
        //成团时间
        map.put("cloudsTime",info.get("surplusTime"));
        MarketingCertificateGroupBaseSetting certificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0"));
        //分享图
        map.put("coverPlan",certificateGroupBaseSetting.getCoverPlan());

        List<Map<String, Object>> memberListByMarketingCertificateGroupManageId = iMarketingCertificateGroupRecordService.getMemberListByMarketingCertificateGroupManageId(marketingCertificateGroupManage.getId());
        //参团人
        map.put("memberList",memberListByMarketingCertificateGroupManageId);
        //兑换券名称
        map.put("certificateName",marketingCertificateGroupManage.getCertificateName());
        //主图
        map.put("mainPicture",marketingCertificateGroupManage.getMainPicture());
        //市场价
        map.put("marketPrice",marketingCertificateGroupManage.getMarketPrice());
        //活动价
        map.put("activityPrice",marketingCertificateGroupManage.getActivityPrice());
        map.put("price",marketingCertificateGroupManage.getMarketPrice().subtract(marketingCertificateGroupManage.getActivityPrice()));
        map.put("marketingCertificateGroupListId",marketingCertificateGroupManage.getMarketingCertificateGroupListId());
        return Result.ok(map);
    }
}
