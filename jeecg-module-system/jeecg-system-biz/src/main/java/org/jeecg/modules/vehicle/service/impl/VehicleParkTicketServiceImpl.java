package org.jeecg.modules.vehicle.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.vehicle.entity.VehicleParkTicket;
import org.jeecg.modules.vehicle.entity.VehicleParkTicketRecord;
import org.jeecg.modules.vehicle.mapper.VehicleParkTicketMapper;
import org.jeecg.modules.vehicle.service.IVehicleParkTicketRecordService;
import org.jeecg.modules.vehicle.service.IVehicleParkTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 停车券
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Service
public class VehicleParkTicketServiceImpl extends ServiceImpl<VehicleParkTicketMapper, VehicleParkTicket> implements IVehicleParkTicketService {

    @Autowired
    private IVehicleParkTicketRecordService iVehicleParkTicketRecordService;

    @Override
    public boolean generate(String vehicleParkTicketId, String memberId,String activityNo) {
        VehicleParkTicket vehicleParkTicket=this.getById(vehicleParkTicketId);
        VehicleParkTicketRecord vehicleParkTicketRecord=new VehicleParkTicketRecord()
                .setSerialNumber(vehicleParkTicket.getSerialNumber())
                .setTicketName(vehicleParkTicket.getTicketName())
                .setMainPicture(vehicleParkTicket.getMainPicture())
                .setPreferentialTime(vehicleParkTicket.getPreferentialTime())
                .setTimeType(vehicleParkTicket.getTimeType())
                .setEffectiveDays(vehicleParkTicket.getEffectiveDays())
                .setTicketNo(OrderNoUtils.getOrderNo())
                .setMemberListId(memberId)
                .setVehicleParkTicketId(vehicleParkTicketId)
                .setActivityNo(activityNo)
                .setGetTime(new Date());
        //计算开始和结束时间
        DateTime startTime = DateUtil.beginOfDay(DateUtil.date());
        vehicleParkTicketRecord.setStartTime(startTime);
        DateTime endTime = DateUtil.endOfDay(DateUtil.offsetDay(startTime, vehicleParkTicketRecord.getEffectiveDays().intValue() - 1));
        vehicleParkTicketRecord.setEndTime(endTime);
        iVehicleParkTicketRecordService.save(vehicleParkTicketRecord);
        return true;
    }
}
