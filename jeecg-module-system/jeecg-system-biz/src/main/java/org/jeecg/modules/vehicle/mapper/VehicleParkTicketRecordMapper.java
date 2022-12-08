package org.jeecg.modules.vehicle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.vehicle.dto.VehicleParkTicketRecordDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkTicketRecord;

import java.util.Map;

/**
 * @Description: 券记录
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
public interface VehicleParkTicketRecordMapper extends BaseMapper<VehicleParkTicketRecord> {

    /**
     * 券记录列表终端
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkTicketRecordList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 券记录列表平台
     *
     * @param page
     * @param vehicleParkTicketRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkTicketRecordListDTO(Page<Map<String,Object>> page,@Param("vehicleParkTicketRecordDTO") VehicleParkTicketRecordDTO vehicleParkTicketRecordDTO);

}
