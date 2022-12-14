package org.jeecg.modules.system.api;


import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 字典api接口
 */
@RequestMapping("front/sysDict")
@Controller
public class FrontSysDictController {


    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 获取相关编号的字段列表
     * @param code
     * @return
     */
    @RequestMapping("getDicts")
    @ResponseBody
    public Result<List<DictModel>> getDicts(String code){
        Result<List<DictModel>> result=new Result<>();
        result.setResult(iSysDictService.queryDictItemsByCode(code));
        result.success("字典列表获取成功");
        return result;
    }
}
