package org.jeecg.modules.vehicle.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.vehicle.service.IVehicleParkTicketRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("after/vehicleParkTicketRecord")
public class AfterVehicleParkTicketRecordController {


    @Autowired
    private IVehicleParkTicketRecordService iVehicleParkTicketRecordService;


    /**
     * 获取券列表
     *
     * @param status  状态；0：未使用；1：已使用；2：已过期；3：已作废
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @return
     */
    @RequestMapping("getVehicleParkTicketRecordList")
    @ResponseBody
    public Result<?> getVehicleParkTicketRecordList(String status,  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                    @RequestAttribute(value = "memberId",required = false) String memberId){
        //参数校验
        if(StringUtils.isBlank(status)){
            return Result.error("券状态不能为空");
        }
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("status",status);
        paramMap.put("memberListId",memberId);
        return Result.ok(iVehicleParkTicketRecordService.getVehicleParkTicketRecordList(new Page<>(pageNo,pageSize),paramMap));
    }
}
