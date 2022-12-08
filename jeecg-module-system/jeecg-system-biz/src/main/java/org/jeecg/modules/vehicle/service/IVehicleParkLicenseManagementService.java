package org.jeecg.modules.vehicle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.vehicle.dto.VehicleParkLicenseManagementDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkLicenseManagement;

import java.util.Map;

/**
 * @Description: 车牌管理
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
public interface IVehicleParkLicenseManagementService extends IService<VehicleParkLicenseManagement> {

    /**
     * 获取车辆列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkLicenseManagementList(Page<Map<String,Object>> page, String memberListId);


    /**
     * 平台车辆列表，包含查询
     *
     * @param page
     * @param vehicleParkLicenseManagementDTO
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkLicenseManagementListDTO(Page<Map<String,Object>> page,VehicleParkLicenseManagementDTO vehicleParkLicenseManagementDTO);


}
