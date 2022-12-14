package org.jeecg.modules.system.api;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.service.ISysMemberMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@RequestMapping("front/SysMemberMenu")
@Controller
@Slf4j
public class FrontSysMemberMenuController {
    @Autowired
    private ISysMemberMenuService iSysMemberMenuService;

    /**
     * 用户端菜单接口
     * @return
     */
    @RequestMapping("getSysMemberMenu")
    @ResponseBody
   public Result< List<Map<String, Object>>> getSysMemberMenu(){
       Result< List<Map<String, Object>>> result = new Result<>();
       List<Map<String, Object>> sysMemberMenuListMap = iSysMemberMenuService.getSysMemberMenuListMap();

       result.setResult(sysMemberMenuListMap);
       return  result;
   }

}
