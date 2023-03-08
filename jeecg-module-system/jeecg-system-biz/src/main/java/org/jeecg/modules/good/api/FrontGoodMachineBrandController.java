package org.jeecg.modules.good.api;


import org.jeecg.modules.good.service.IGoodMachineBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/goodMachineBrand")
public class FrontGoodMachineBrandController {


    @Autowired
    private IGoodMachineBrandService iGoodMachineBrandService;


}
