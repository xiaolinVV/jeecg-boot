package org.jeecg.modules.vehicle.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.vehicle.entity.VehicleParkBaseSetting;
import org.jeecg.modules.vehicle.entity.VehicleParkLicenseManagement;
import org.jeecg.modules.vehicle.service.IVehicleParkBaseSettingService;
import org.jeecg.modules.vehicle.service.IVehicleParkLicenseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 车辆管理
 */
@RequestMapping("after/vehicleParkLicenseManagement")
@Controller
public class AfterVehicleParkLicenseManagementController {

    @Autowired
    private IVehicleParkLicenseManagementService iVehicleParkLicenseManagementService;

    @Autowired
    private IVehicleParkBaseSettingService iVehicleParkBaseSettingService;


    /**
     *
     * 车辆首页
     *
     * @param memberId
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public Result<?> index(@RequestAttribute(value = "memberId",required = false) String memberId){
        VehicleParkBaseSetting vehicleParkBaseSetting=iVehicleParkBaseSettingService.getOne(new LambdaQueryWrapper<>());
        if(vehicleParkBaseSetting==null){
            return Result.error("车辆设置为空");
        }
        Map<String,Object> resultMap=Maps.newHashMap();
        resultMap.put("parkName",vehicleParkBaseSetting.getParkName());
        resultMap.put("detailedAddress",vehicleParkBaseSetting.getDetailedAddress());
        resultMap.put("content",vehicleParkBaseSetting.getContent());
        VehicleParkLicenseManagement vehicleParkLicenseManagement=iVehicleParkLicenseManagementService.getOne(new LambdaQueryWrapper<VehicleParkLicenseManagement>()
                .eq(VehicleParkLicenseManagement::getMemberListId,memberId)
                .eq(VehicleParkLicenseManagement::getIsDefault,"1"));
        if(vehicleParkLicenseManagement!=null){
            resultMap.put("licensePlateNumber",vehicleParkLicenseManagement.getLicensePlateNumber());
            resultMap.put("licenseType",vehicleParkLicenseManagement.getLicenseType());
        }else{
            resultMap.put("licensePlateNumber","");
            resultMap.put("licenseType","");
        }
        return Result.ok(resultMap);
    }


    /**
     * 添加车辆
     *
     * @return
     */
    @RequestMapping("addVehicleParkLicenseManagement")
    @ResponseBody
    public Result<?> addVehicleParkLicenseManagement(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                     String licenseType,String licensePlateNumber){
        //参数校验
        if(StringUtils.isBlank(licenseType)){
            return Result.error("车牌类型不能为空");
        }
        if(StringUtils.isBlank(licensePlateNumber)){
            return Result.error("车牌号不能为空");
        }
        iVehicleParkLicenseManagementService.save(new VehicleParkLicenseManagement()
                .setLicenseType(licenseType)
                .setLicensePlateNumber(licensePlateNumber)
                .setMemberListId(memberId));
        return Result.ok("车牌添加成功");
    }


    /**
     *
     * 车辆列表
     *
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getVehicleParkLicenseManagementList")
    @ResponseBody
    public Result<?> getVehicleParkLicenseManagementList(@RequestAttribute(value = "memberId",required = false) String memberId,
                                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iVehicleParkLicenseManagementService.getVehicleParkLicenseManagementList(new Page<>(pageNo,pageSize),memberId));
    }


    /**
     * 获取车辆详情
     *
     * @param vehicleParkLicenseManagementId
     * @return
     */
    @RequestMapping("getVehicleParkLicenseManagementById")
    @ResponseBody
    public Result<?> getVehicleParkLicenseManagementById(String vehicleParkLicenseManagementId){
        //参数校验
        if(StringUtils.isBlank(vehicleParkLicenseManagementId)){
            return Result.error("车辆id不能为空");
        }
        Map<String,Object> resultMap= Maps.newHashMap();
        VehicleParkLicenseManagement vehicleParkLicenseManagement=iVehicleParkLicenseManagementService.getById(vehicleParkLicenseManagementId);
        resultMap.put("licensePlateNumber",vehicleParkLicenseManagement.getLicensePlateNumber());
        resultMap.put("licenseType",vehicleParkLicenseManagement.getLicenseType());
        resultMap.put("isDefault",vehicleParkLicenseManagement.getIsDefault());
        return Result.ok(resultMap);
    }

    /**
     * 修改车辆信息
     *
     * @param vehicleParkLicenseManagementId
     * @param licenseType
     * @param licensePlateNumber
     * @return
     */
    @RequestMapping("updateVehicleParkLicenseManagementById")
    @ResponseBody
    public Result<?> updateVehicleParkLicenseManagementById(String vehicleParkLicenseManagementId,String licenseType,String licensePlateNumber){
        //参数校验
        if(StringUtils.isBlank(vehicleParkLicenseManagementId)){
            return Result.error("车辆id不能为空");
        }
        if(StringUtils.isBlank(licenseType)){
            return Result.error("车辆类型不能为空");
        }
        if(StringUtils.isBlank(licensePlateNumber)){
            return Result.error("车牌号不能为空");
        }
        VehicleParkLicenseManagement vehicleParkLicenseManagement=iVehicleParkLicenseManagementService.getById(vehicleParkLicenseManagementId);
        vehicleParkLicenseManagement.setLicenseType(licenseType);
        vehicleParkLicenseManagement.setLicensePlateNumber(licensePlateNumber);
        iVehicleParkLicenseManagementService.saveOrUpdate(vehicleParkLicenseManagement);
        return Result.ok("修改车辆信息成功");
    }


    /**
     * 车辆默认设置
     *
     * @param vehicleParkLicenseManagementId
     * @param memberId
     * @return
     */
    @RequestMapping("defaultVehicleParkLicenseManagementById")
    @ResponseBody
    public Result<?> defaultVehicleParkLicenseManagementById(String vehicleParkLicenseManagementId,
                                                             @RequestAttribute(value = "memberId",required = false) String memberId){
        //参数校验
        if(StringUtils.isBlank(vehicleParkLicenseManagementId)){
            return Result.error("车辆id不能为空");
        }
        //将用户所有的车辆设置为非默认
        iVehicleParkLicenseManagementService.update(new VehicleParkLicenseManagement().setIsDefault("0"),
                new LambdaUpdateWrapper<VehicleParkLicenseManagement>().eq(VehicleParkLicenseManagement::getMemberListId,memberId));
        iVehicleParkLicenseManagementService.updateById(new VehicleParkLicenseManagement().setIsDefault("1").setId(vehicleParkLicenseManagementId));
        return Result.ok("修改默认设置成功");
    }

    /**
     * 删除车辆信息
     * @param vehicleParkLicenseManagementId
     * @return
     */
    @RequestMapping("deleteVehicleParkLicenseManagementById")
    @ResponseBody
    public Result<?> deleteVehicleParkLicenseManagementById(String vehicleParkLicenseManagementId){
        //参数校验
        if(StringUtils.isBlank(vehicleParkLicenseManagementId)){
            return Result.error("车辆id不能为空");
        }
        iVehicleParkLicenseManagementService.removeById(vehicleParkLicenseManagementId);
        return Result.ok("删除车辆信息");
    }
}
