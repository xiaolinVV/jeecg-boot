package org.jeecg.modules.vehicle.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.vehicle.dto.VehicleParkLicenseManagementDTO;
import org.jeecg.modules.vehicle.entity.VehicleParkLicenseManagement;
import org.jeecg.modules.vehicle.mapper.VehicleParkLicenseManagementMapper;
import org.jeecg.modules.vehicle.service.IVehicleParkLicenseManagementService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 车牌管理
 * @Author: jeecg-boot
 * @Date:   2021-05-24
 * @Version: V1.0
 */
@Service
public class VehicleParkLicenseManagementServiceImpl extends ServiceImpl<VehicleParkLicenseManagementMapper, VehicleParkLicenseManagement> implements IVehicleParkLicenseManagementService {

    @Override
    public IPage<Map<String,Object>> getVehicleParkLicenseManagementList(Page<Map<String,Object>> page, String memberListId) {
        return baseMapper.getVehicleParkLicenseManagementList(page,memberListId);
    }

    @Override
    public IPage<Map<String, Object>> getVehicleParkLicenseManagementListDTO(Page<Map<String, Object>> page, VehicleParkLicenseManagementDTO vehicleParkLicenseManagementDTO) {
        return baseMapper.getVehicleParkLicenseManagementListDTO(page,vehicleParkLicenseManagementDTO);
    }
}
