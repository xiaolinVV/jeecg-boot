package org.jeecg.modules.vehicle.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.vehicle.dto.VehicleParkActivityTicketDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkActivityTicket;
import org.jeecg.modules.vehicle.mapper.VehicleParkActivityTicketMapper;
import org.jeecg.modules.vehicle.service.IVehicleParkActivityTicketService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 送券活动
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Service
public class VehicleParkActivityTicketServiceImpl extends ServiceImpl<VehicleParkActivityTicketMapper, VehicleParkActivityTicket> implements IVehicleParkActivityTicketService {

    @Override
    public IPage<Map<String, Object>> getVehicleParkActivityTicketDTO(Page<Map<String, Object>> page, VehicleParkActivityTicketDTO vehicleParkActivityTicketDTO) {
        return baseMapper.getVehicleParkActivityTicketDTO(page,vehicleParkActivityTicketDTO);
    }
}
