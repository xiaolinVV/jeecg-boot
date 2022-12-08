package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.service.IMemberAccountCapitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员资金明细api
 */
@RequestMapping("after/memberAccountCapital")
@Controller
public class AfterMemberAccountCapitalController {

    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;

    /**
     * 分页查询用户会员资金列表
     *
     * @param pattern
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMemberAccountCapitalByMemberId")
    @ResponseBody
    public Result<IPage<Map<String, Object>>> findMemberAccountCapitalByMemberId(Integer pattern, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        String memberId = request.getAttribute("memberId").toString();
        Result<IPage<Map<String, Object>>> result = new Result<>();

        //组织查询参数
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        Map<String, Object> paramObjectMap = Maps.newHashMap();
        paramObjectMap.put("memberId", memberId);
        paramObjectMap.put("pattern", pattern);
        result.setResult(iMemberAccountCapitalService.findMemberAccountCapitalByMemberId(page, paramObjectMap));
        result.success("查询会员资金列表");
        return result;
    }

    /**
     * 可用余额明细
     *
     * @param pattern
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findAccountCapitalByMemberId")
    @ResponseBody
    public Result<IPage<Map<String, Object>>> findAccountCapitalByMemberId(Integer pattern,
                                                                           HttpServletRequest request,
                                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        String memberId = request.getAttribute("memberId").toString();
        Result<IPage<Map<String, Object>>> result = new Result<>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("pattern", pattern);
        IPage<Map<String, Object>> accountCapitalByMemberId = iMemberAccountCapitalService.findAccountCapitalByMemberId(page, map);
        result.setResult(accountCapitalByMemberId);
        result.success("返回可用余额明细");
        return result;
    }
}
