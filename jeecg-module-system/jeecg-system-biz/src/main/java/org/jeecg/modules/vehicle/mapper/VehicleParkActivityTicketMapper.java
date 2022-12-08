package org.jeecg.modules.vehicle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.vehicle.dto.VehicleParkActivityTicketDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkActivityTicket;

import java.util.Map;

/**
 * @Description: 送券活动
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
public interface VehicleParkActivityTicketMapper extends BaseMapper<VehicleParkActivityTicket> {

    /**
     * 券活动列表
     *
     * @param page
     * @param vehicleParkActivityTicketDTO
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkActivityTicketDTO(Page<Map<String,Object>> page,@Param("vehicleParkActivityTicketDTO") VehicleParkActivityTicketDTO vehicleParkActivityTicketDTO);

}
