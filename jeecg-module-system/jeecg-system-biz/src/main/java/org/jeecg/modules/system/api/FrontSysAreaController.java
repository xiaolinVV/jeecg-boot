package org.jeecg.modules.system.api;


import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 区域控制层接口
 */
@RequestMapping("front/sysArea")
@Controller
public class FrontSysAreaController {


    @Autowired
    private ISysAreaService iSysAreaService;


    /**
     * 根据父id查询区域
     * @param parentId
     * @return
     */
    @RequestMapping("findSysAreaByParentId")
    @ResponseBody
    public Result<List<SysArea>> findSysAreaByParentId(String parentId){
        Result<List<SysArea>> result=new Result<>();

        //参数验证
        if(StringUtils.isBlank(parentId)){
            result.error500("parentId 参数不能为空！！！");
            return result;
        }

        result.setResult(iSysAreaService.findByParentId(Integer.parseInt(parentId)));
        result.success("区域列表查询成功");
        return result;
    }
}
