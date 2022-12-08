package org.jeecg.modules.member.job;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Component
@Log
public class MemberWelfarePaymentsLeagueJob implements Job {

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMemberListService iMemberListService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("会员游戏水果回收；每1小时一次");

        iMemberListService.list(new LambdaQueryWrapper<>()).forEach(m->{
            RestTemplate restTemplate=new RestTemplate();
            String url = "http://game.gclb.vip/index.php?m=login&a=clearSelect&tel={tel}";
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("tel", m.getPhone());
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
            log.info(responseEntity.getBody());
            JSONObject jsonObject= JSON.parseObject(responseEntity.getBody());
            if(jsonObject.getString("reTips").equals("success")){
                log.info("成功："+jsonObject.getBigDecimal("count"));
                iMemberWelfarePaymentsService.addWelfarePayments(m.getId(),jsonObject.getBigDecimal("count").divide(new BigDecimal(10),2, RoundingMode.DOWN),"50",m.getId(),"");
            }else{
                log.info("失败："+jsonObject.getString("reMsg"));
            }
        });
    }
}
