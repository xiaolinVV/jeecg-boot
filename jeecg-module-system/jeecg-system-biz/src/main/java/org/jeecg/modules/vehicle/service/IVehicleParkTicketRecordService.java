package org.jeecg.modules.vehicle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.vehicle.dto.VehicleParkTicketRecordDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkTicketRecord;

import java.util.Map;

/**
 * @Description: 券记录
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
public interface IVehicleParkTicketRecordService extends IService<VehicleParkTicketRecord> {

    /**
     * 获取终端券列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkTicketRecordList(Page<Map<String,Object>> page,Map<String,Object> paramMap);


    /**
     * 券记录列表平台
     *
     * @param page
     * @param vehicleParkTicketRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkTicketRecordListDTO(Page<Map<String,Object>> page,VehicleParkTicketRecordDTO vehicleParkTicketRecordDTO);

    /**
     * 超时处理
     */
    public void timeOut();

}
