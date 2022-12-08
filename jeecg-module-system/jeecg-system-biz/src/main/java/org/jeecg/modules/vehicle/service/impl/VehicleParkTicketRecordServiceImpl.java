package org.jeecg.modules.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.jeecg.modules.vehicle.dto.VehicleParkTicketRecordDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkTicketRecord;
import org.jeecg.modules.vehicle.mapper.VehicleParkTicketRecordMapper;
import org.jeecg.modules.vehicle.service.IVehicleParkTicketRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.Map;

/**
 * @Description: 券记录
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Service
@Log
public class VehicleParkTicketRecordServiceImpl extends ServiceImpl<VehicleParkTicketRecordMapper, VehicleParkTicketRecord> implements IVehicleParkTicketRecordService {

    @Override
    public IPage<Map<String, Object>> getVehicleParkTicketRecordList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getVehicleParkTicketRecordList(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> getVehicleParkTicketRecordListDTO(Page<Map<String, Object>> page, VehicleParkTicketRecordDTO vehicleParkTicketRecordDTO) {
        return baseMapper.getVehicleParkTicketRecordListDTO(page,vehicleParkTicketRecordDTO);
    }

    @Override
    public void timeOut() {
        this.list(new LambdaQueryWrapper<VehicleParkTicketRecord>()
                .eq(VehicleParkTicketRecord::getStatus,"0")
                .le(VehicleParkTicketRecord::getEndTime,new Date())
                .last("limit 10")).forEach(vehicleParkTicketRecord->{
                    log.info("停车券超时："+vehicleParkTicketRecord.getId());
                    vehicleParkTicketRecord.setStatus("2");
                    this.saveOrUpdate(vehicleParkTicketRecord);
        });
    }
}
