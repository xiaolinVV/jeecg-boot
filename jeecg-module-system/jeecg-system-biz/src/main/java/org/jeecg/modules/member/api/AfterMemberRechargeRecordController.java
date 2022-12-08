package org.jeecg.modules.member.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberRechargeRecord;
import org.jeecg.modules.member.service.IMemberDistributionRecordService;
import org.jeecg.modules.member.service.IMemberRechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 余额记录的api
 */
@RequestMapping("after/memberRechargeRecord")
@Controller
public class AfterMemberRechargeRecordController {


    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private IMemberDistributionRecordService iMemberDistributionRecordService;

    /**
     * 佣金明细列表
     * @param pattern  -1:全部；0：待付款；1：已付款；5：已完成
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("findMemberRechargeRecordProtomerList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>>   findMemberRechargeRecordProtomerList(Integer pattern ,
                                                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest request){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        String memberId=request.getAttribute("memberId").toString();
        if(pattern==null){
            result.error500("pattern 参数不能为空！！！   ");
            return  result;
        }

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("memberId",memberId);
        IPage<Map<String,Object>> mapIPage=iMemberRechargeRecordService.findMemberRechargeRecordProtomerList(page,paramObjectMap);
        for (Map<String,Object> m:mapIPage.getRecords()) {
            m.put("memberDistributionRecords",iMemberDistributionRecordService.findMemberDistributionRecordByMrrId(m.get("id").toString()));
        }
        result.setResult(mapIPage);
        result.success("佣金明细查询成功");
        return result;
    }

    /**
     * 用户端小程序待结算明细and不可用余额明细接口
     * @param pageNo
     * @param pageSize
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("findMemberRechargeRecordPage")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMemberRechargeRecordPage(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                          @RequestParam(value="isPlatform",required = true) String isPlatform,
                                                                          HttpServletRequest request){
        Result<IPage<Map<String, Object>>> result = new Result<>();
        Page<MemberRechargeRecord> page = new Page<MemberRechargeRecord>(pageNo, pageSize);
        String memberId = request.getAttribute("memberId").toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",memberId);
        map.put("isPlatform",isPlatform);
        IPage<Map<String, Object>> memberRechargeRecordPage = iMemberRechargeRecordService.findMemberRechargeRecordPage(page, map);
        result.setResult(memberRechargeRecordPage);
        result.success("返回明细");
        return result;
    }
}
