package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingMaterialDianzan;
import org.jeecg.modules.marketing.service.IMarketingMaterialDianzanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 素材库栏目接口控制层
 */
@RequestMapping("after/marketingMaterialDianzan")
@Controller
public class AfterMarketingMaterialDianzanController {

    @Autowired
    private IMarketingMaterialDianzanService iMarketingMaterialDianzanService;
    /**
     * 添加/取消点赞记录
     * @param isDianzan 0:点赞 1.取消点赞
     * @param marketingMaterialListId 素材列表id
     * @param request
     * @return
     */
    @RequestMapping("addAndDelectMarketingMaterialDianzan")
    @ResponseBody
    public Result<Map<String,Object>> addAndDelectMarketingMaterialDianzan(String isDianzan,String marketingMaterialListId,HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        String memberId=request.getAttribute("memberId").toString();
        if(StringUtils.isBlank(isDianzan)){
           return result.error500("点赞状态不能为空!");
        }
        if(StringUtils.isBlank(marketingMaterialListId)){
            return result.error500("素材id 不能为空!");
        }
        if(isDianzan.equals("0")){
            //添加点赞
            MarketingMaterialDianzan marketingMaterialDianzan = new MarketingMaterialDianzan();
            marketingMaterialDianzan.setMarketingMaterialListId(marketingMaterialListId);
            marketingMaterialDianzan.setMemberListId(memberId);
            marketingMaterialDianzan.setBrowseTime(new Date());
            iMarketingMaterialDianzanService.save(marketingMaterialDianzan);
        }else if(isDianzan.equals("1")){
            //删除点赞
            List<MarketingMaterialDianzan> marketingMaterialDianzanList = iMarketingMaterialDianzanService.list(new LambdaQueryWrapper<MarketingMaterialDianzan>()
                    .eq(MarketingMaterialDianzan::getMarketingMaterialListId,marketingMaterialListId)
                    .eq(MarketingMaterialDianzan::getMemberListId,memberId));


            marketingMaterialDianzanList.forEach(mmd->{
                iMarketingMaterialDianzanService.removeById(mmd.getId());
            });
        }
        if(isDianzan.equals("0")){
            result.success("点赞成功!");
        }else{
            result.success("取消成功!");
        }
     return result;
    }

}
