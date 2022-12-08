package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberEquities;
import org.jeecg.modules.member.service.IMemberEquitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页请求需要的控制器
 */

@Controller
@RequestMapping("front/memberEquities")
@Slf4j
public class FrontMemberEquitiesController {


    @Autowired
    private IMemberEquitiesService iMemberEquitiesService;

    @RequestMapping("findMemberEquities")
    @ResponseBody
    public Result<Map<String,Object>> findMemberEquities(HttpServletRequest request){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();

        List<MemberEquities> memberEquitiesList = iMemberEquitiesService.list(new LambdaQueryWrapper<MemberEquities>()
                .eq(MemberEquities::getDelFlag, "0"));
        if (memberEquitiesList.size()>0){
            MemberEquities memberEquities = memberEquitiesList.get(0);
            map.put("title",memberEquities.getTitle());
            map.put("equities",memberEquities.getEquities());
            map.put("coverPlan",memberEquities.getCoverPlan());
            map.put("posters",memberEquities.getPosters());
        }
        result.setResult(map);
        result.success("返回会员权益");
        return result;
    }
}
