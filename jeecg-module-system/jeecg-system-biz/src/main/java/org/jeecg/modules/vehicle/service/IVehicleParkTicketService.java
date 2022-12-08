package org.jeecg.modules.vehicle.service;

import org.jeecg.modules.vehicle.entity.VehicleParkTicket;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 停车券
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
public interface IVehicleParkTicketService extends IService<VehicleParkTicket> {

    /**
     *
     * 券生成
     *
     * @param vehicleParkTicketId
     * @param memberId
     * @return
     */
    public boolean generate(String vehicleParkTicketId,String memberId,String activityNo);

}
