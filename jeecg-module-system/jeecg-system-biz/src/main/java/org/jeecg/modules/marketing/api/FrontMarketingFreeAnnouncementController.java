package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingFreeAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 免单公告接口
 */
@RequestMapping("front/marketingFreeAnnouncement")
@Controller
public class FrontMarketingFreeAnnouncementController {

    @Autowired
    private IMarketingFreeAnnouncementService iMarketingFreeAnnouncementService;

    /**
     * 免单公告列表查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("selectMarketingFreeAnnouncement")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> selectMarketingFreeAnnouncement(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);

        result.setResult(iMarketingFreeAnnouncementService.selectMarketingFreeAnnouncement(page));

        result.success("查询免单公告列表成功！！！");
        return result;
    }


    /**
     * 查询免单公告详情
     *
     * @param marketingFreeAnnouncementId
     * @return
     */
    @RequestMapping("getMarketingFreeAnnouncementById")
    @ResponseBody
    public Result<Map<String,Object>> getMarketingFreeAnnouncementById(String marketingFreeAnnouncementId){
        Result<Map<String,Object>> result=new Result<>();

        //参数判断
        if(StringUtils.isBlank(marketingFreeAnnouncementId)){
            result.error500("免单公告id不能为空！！！   ");
            return  result;
        }

        result.setResult(iMarketingFreeAnnouncementService.getMarketingFreeAnnouncementById(marketingFreeAnnouncementId));

        result.success("公告详情！！！");
        return result;
    }

}
