package org.jeecg.modules.agency.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.service.ISysAreaService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class AgentOverdueJob implements Job {
    @Autowired
    private IAgencyManageService agencyManageService;
    @Autowired
    private ISysAreaService sysAreaService;
    /**
     * 代理过期每天凌晨执行
      */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(" Jeecg-Boot 普通定时任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
        System.out.println("121");
      QueryWrapper<AgencyManage> queryWrapper = new QueryWrapper<>();
      queryWrapper.lt("end_time",new Date());
      queryWrapper.lt("status","1");
      //查询时间过期还启用的代理
      List<AgencyManage> agencyManageList = agencyManageService.list(queryWrapper);
      //修改代理状态为过期
     if(agencyManageList.size()>0){
         agencyManageList.forEach(am->{
             am.setStatus("0");
             //修改代理区域为空
             //停用后清空所有代理区域
             QueryWrapper<SysArea> queryWrapperSysArea = new QueryWrapper<>();
             queryWrapperSysArea.eq("agency_manage_id",am.getId());
             List<SysArea> areaList = sysAreaService.list(queryWrapperSysArea);
                 areaList.forEach(al->{
                     al.setAgencyManageId("");
                     sysAreaService.updateById(al);
                 });
             agencyManageService.updateById(am);
             //消息模板推送，告知过期


         });

     }

  }

}
