package org.jeecg.modules.good.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodMachineModel;
import org.jeecg.modules.good.service.IGoodMachineModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/goodMachineModel")
public class FrontGoodMachineModelController {

    @Autowired
    private IGoodMachineModelService iGoodMachineModelService;



}
