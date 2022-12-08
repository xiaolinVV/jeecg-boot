package org.jeecg.modules.vehicle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.vehicle.dto.VehicleParkLicenseManagementDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkLicenseManagement;

import java.util.Map;

/**
 * @Description: 车牌管理
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
public interface VehicleParkLicenseManagementMapper extends BaseMapper<VehicleParkLicenseManagement> {

    /**
     * 获取车辆列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkLicenseManagementList(Page<Map<String,Object>> page,@Param("memberListId") String memberListId);


    /**
     * 平台车辆列表，包含查询
     *
     * @param page
     * @param vehicleParkLicenseManagementDTO
     * @return
     */
    public IPage<Map<String,Object>> getVehicleParkLicenseManagementListDTO(Page<Map<String,Object>> page,@Param("vehicleParkLicenseManagementDTO") VehicleParkLicenseManagementDTO vehicleParkLicenseManagementDTO);

}
