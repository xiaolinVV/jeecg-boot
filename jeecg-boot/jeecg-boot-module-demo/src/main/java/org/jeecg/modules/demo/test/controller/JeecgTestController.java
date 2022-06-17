package org.jeecg.modules.demo.test.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.LimitSubmit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author 张少林
 * @date 2022年06月17日 9:49 下午
 */
@Slf4j
@Api(tags = "测试DEMO")
@RestController
@RequestMapping("/test")
public class JeecgTestController {


    @ApiOperation(value = "测试限制重复提交注解", notes = "测试限制重复提交注解")
    @LimitSubmit(key = "testLimit:%s:#orderId", limit = 10, needAllWait = true)
    @GetMapping(value = "/testLimit")
    public Result<String> testLimit(@RequestParam(name = "orderId", defaultValue = "order1123123") String orderId) {
        return Result.OK("请求成功，orderId：".concat(orderId));
    }

    public static void main(String[] args) {
        ThreadUtil.concurrencyTest(30, () -> {
            String urlStr = "http://127.0.0.1:8080/jeecg-boot/test/testLimit";
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTU0NzgxMzAsInVzZXJuYW1lIjoiYWRtaW4ifQ.YcWTJ8qWw95EhUrsWhPGzLpaPeyoG23qdkwWViIL04A";
            String result = HttpRequest
                    .get(urlStr)
                    .header("X-Access-Token", token, true)
                    .form(Dict.create().set("orderId", "123456")).execute()
                    .body();
            System.out.println(JSONUtil.toJsonPrettyStr(result)) ;
        });
    }
}
