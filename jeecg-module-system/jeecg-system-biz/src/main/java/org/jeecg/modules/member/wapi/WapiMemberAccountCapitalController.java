package org.jeecg.modules.member.wapi;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.dto.MemberAccountCapitalDTO;
import org.jeecg.modules.member.entity.MemberAccountCapital;
import org.jeecg.modules.member.service.IMemberAccountCapitalService;
import org.jeecg.modules.member.vo.MemberAccountCapitalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("wapi/memberAccountCapital")
@Controller
@Log
public class WapiMemberAccountCapitalController {
    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;
    @RequestMapping("pageList")
    @ResponseBody
    public Result<?> pageList(MemberAccountCapitalVO memberAccountCapitalVO,
                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<MemberAccountCapitalDTO>> result = new Result<IPage<MemberAccountCapitalDTO>>();
        Page<MemberAccountCapital> page = new Page<MemberAccountCapital>(pageNo, pageSize);
        IPage<MemberAccountCapitalDTO> pageList = iMemberAccountCapitalService.getMemberAccountCapitalList(page, memberAccountCapitalVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
}
