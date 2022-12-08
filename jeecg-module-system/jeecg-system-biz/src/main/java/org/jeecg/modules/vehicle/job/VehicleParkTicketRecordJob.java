package org.jeecg.modules.vehicle.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.vehicle.service.IVehicleParkTicketRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class VehicleParkTicketRecordJob implements Job {

    @Autowired
    private IVehicleParkTicketRecordService iVehicleParkTicketRecordService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("停车券超时定时器；5分钟执行一次");
        iVehicleParkTicketRecordService.timeOut();
    }
}
