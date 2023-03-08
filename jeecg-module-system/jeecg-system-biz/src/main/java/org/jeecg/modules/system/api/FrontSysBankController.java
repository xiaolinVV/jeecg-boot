package org.jeecg.modules.system.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysBank;
import org.jeecg.modules.system.service.ISysBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/sysBank")
public class FrontSysBankController {

    @Autowired
    private ISysBankService iSysBankService;

    /**
     * 获取银行卡列表
     *
     * @return
     */
    @PostMapping("getSysBankList")
    public Result<?> getSysBankList(){
        return Result.ok(iSysBankService.list(new LambdaQueryWrapper<SysBank>().orderByAsc(SysBank::getSort).orderByAsc(SysBank::getCreateTime)));
    }
}
