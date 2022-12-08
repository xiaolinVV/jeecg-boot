package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingRushGroup;
import org.jeecg.modules.marketing.entity.MarketingRushType;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
import org.jeecg.modules.marketing.service.IMarketingRushGroupService;
import org.jeecg.modules.marketing.service.IMarketingRushTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("after/maketingRushGroup")
@Controller
public class AfterMaketingRushGroupController {
    @Autowired
    private IMarketingRushGroupService iMarketingRushGroupService;

    @Autowired
    private IMarketingRushTypeService iMarketingRushTypeService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IMarketingPrefectureTypeService iMarketingPrefectureTypeService;


    /**
     * 寄售记录列表
     * @param memberId
     * @param consignmentStatus
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingRushGroup")
    @ResponseBody
    public Result<?> findMarketingRushGroup(@RequestAttribute(value = "memberId",required = false) String memberId,
                                            String consignmentStatus,
                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iMarketingRushGroupService.findMarketingRushGroup(new Page<>(pageNo,pageSize),memberId,consignmentStatus));
    }

    /**
     * 寄售商品详情页
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingRushGroupParticulars")
    @ResponseBody
    public Result<?>getMarketingRushGroupParticulars(String id,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        if (StringUtils.isBlank(id)){
            return Result.error("前端id未传递!");
        }
        return Result.ok(iMarketingRushGroupService.getMarketingRushGroupParticulars(new Page<>(pageNo,pageSize),id));
    }

    /**
     * 寄售
     * @param id
     * @return
     */
    @RequestMapping("consignmentSales")
    @ResponseBody
    public Result<?> consignmentSales(String id){
        if (StringUtils.isBlank(id)){
            return Result.error("前端分类id未传递!");
        }
        MarketingRushGroup marketingRushGroup = iMarketingRushGroupService.getById(id);
        if (marketingRushGroup==null){
            return Result.error("id传递有误,找不到实体");
        }else {
            if(marketingRushGroup.getIfPurchase().equals("0")){
                MarketingRushType marketingRushType=iMarketingRushTypeService.getById(marketingRushGroup.getMarketingRushTypeId());
                Map<String, Object> resultMap= Maps.newHashMap();
                resultMap.put("marketingPrefectureId",marketingRushType.getMarketingPrefectureId());
                resultMap.put("marketingPrefectureTypeId",marketingRushType.getMarketingPrefectureTypeId());
                resultMap.put("marketingRushGroupId",marketingRushGroup.getId());
                resultMap.put("prefectureName", iMarketingPrefectureService.getById(marketingRushType.getMarketingPrefectureId()).getPrefectureName());
                resultMap.put("typeName",iMarketingPrefectureTypeService.getById(marketingRushType.getMarketingPrefectureTypeId()).getTypeName());
                return Result.ok(resultMap);
            }
            if (marketingRushGroup.getConsignmentStatus().equals("1")){
                if (iMarketingRushGroupService.consignmentSales(marketingRushGroup)){
                    return Result.ok("寄售成功");
                }else {
                    return Result.error("操作失败");
                }
            }else {
                return Result.error("商品状态不可寄售");
            }

        }

    }
}
