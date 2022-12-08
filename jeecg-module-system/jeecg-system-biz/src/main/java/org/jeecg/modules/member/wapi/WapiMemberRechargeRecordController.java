package org.jeecg.modules.member.wapi;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.dto.MemberRechargeRecordDTO;
import org.jeecg.modules.member.entity.MemberRechargeRecord;
import org.jeecg.modules.member.service.IMemberRechargeRecordService;
import org.jeecg.modules.member.vo.MemberRechargeRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("wapi/memberRechargeRecord")
@Controller
public class WapiMemberRechargeRecordController {
    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @RequestMapping("pageList")
    @ResponseBody
    public Result<?> pageList(MemberRechargeRecordVO memberRechargeRecordVO,
                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                              HttpServletRequest req){
        Result<IPage<MemberRechargeRecordVO>> result = new Result<IPage<MemberRechargeRecordVO>>();
        Page<MemberRechargeRecord> page = new Page<MemberRechargeRecord>(pageNo, pageSize);
        MemberRechargeRecordDTO memberRechargeRecordDTO = new MemberRechargeRecordDTO();
        BeanUtils.copyProperties(memberRechargeRecordVO,memberRechargeRecordDTO);
        IPage<MemberRechargeRecordVO> pageList = iMemberRechargeRecordService.queryPageList(page, memberRechargeRecordDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

}
