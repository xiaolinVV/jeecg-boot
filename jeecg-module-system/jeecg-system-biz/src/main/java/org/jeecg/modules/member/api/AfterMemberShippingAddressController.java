package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 收货地API址控制层
 */
@RequestMapping("after/memberShippingAddress")
@Controller
public class AfterMemberShippingAddressController {



    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;


    /**
     * 新增和修改收货地址
     * @param linkman
     * @param phone
     * @param areaAddress
     * @param houseNumber
     * @param isDefault
     * @param id
     * @return
     */
    @RequestMapping("addAddress")
    @ResponseBody
    public Result<String> addAddress(String linkman, String phone, String areaAddress, String houseNumber, Integer isDefault, String id, String sysAreaId, String areaExplan, String areaExplanIds, @RequestParam(name = "longitude",defaultValue ="0")BigDecimal longitude,@RequestParam(name = "latitude",defaultValue ="0")BigDecimal latitude, HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<String> result=new Result<>();

        //参数校验
        if(oConvertUtils.isEmpty(linkman)){
            result.error500("联系人不能为空！！！");
            return result;

        }
        if(oConvertUtils.isEmpty(phone)){
            result.error500("手机号不能为空！！！");
            return result;
        }
        if(oConvertUtils.isEmpty(areaAddress)){
            result.error500("详细地址不能为空！！！");
            return result;
        }

        if(oConvertUtils.isEmpty(sysAreaId)){
            result.error500("省级区域id不能为空！！！");
            return result;
        }
        if(oConvertUtils.isEmpty(areaAddress)){
            result.error500("详细地址不能为空！！！");
            return result;
        }
        if(oConvertUtils.isEmpty(areaExplan)){
            result.error500("城市区域描述！！！");
            return result;
        }
        if(oConvertUtils.isEmpty(isDefault)){
            result.error500("是否默认不能为空！！！");
            return result;
        }


        //默认地址的设置
        if(isDefault.intValue()==1){
            //设置默认地址为不默认
            UpdateWrapper<MemberShippingAddress> memberShippingAddressUpdateWrapper=new UpdateWrapper<>();
            memberShippingAddressUpdateWrapper.eq("member_list_id",memberId);
            MemberShippingAddress memberShippingAddress=new MemberShippingAddress();
            memberShippingAddress.setIsDefault("0");
            iMemberShippingAddressService.update(memberShippingAddress,memberShippingAddressUpdateWrapper);
        }

        MemberShippingAddress memberShippingAddress=null;

        //地址的新增和修改
        if(oConvertUtils.isEmpty(id)){
            memberShippingAddress=new MemberShippingAddress();
            memberShippingAddress.setMemberListId(memberId);
            memberShippingAddress.setDelFlag("0");
        }else{
            memberShippingAddress=iMemberShippingAddressService.getById(id);
        }

       memberShippingAddress.setLinkman(linkman);
        memberShippingAddress.setPhone(phone);
        memberShippingAddress.setAreaAddress(areaAddress);
        memberShippingAddress.setHouseNumber(houseNumber);
        memberShippingAddress.setIsDefault(isDefault.toString());
        memberShippingAddress.setSysAreaId(sysAreaId);
        memberShippingAddress.setAreaExplan(areaExplan);
        memberShippingAddress.setAreaExplanIds(areaExplanIds);
        memberShippingAddress.setLatitude(latitude);
        memberShippingAddress.setLongitude(longitude);
        iMemberShippingAddressService.saveOrUpdate(memberShippingAddress);
       result.success("新增和修改收货地址成功");
        return result;
    }


    /**
     * 查询收货地址列表
     * @param request
     * @return
     */
    @RequestMapping("getAddress")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> getAddress(HttpServletRequest request,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        String memberId=request.getAttribute("memberId").toString();
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);

        result.setResult(iMemberShippingAddressService.getMemberShippingAddressByMemberId(page,memberId));

        result.success("查询收货地址列表");
        return result;

    }


    /**
     * 删除收货地址
     * @param id
     * @return
     */
    @RequestMapping("delAddress")
    @ResponseBody
    public Result<String> delAddress(String id){
        Result<String> result=new Result<>();

        //参数判断
        if(oConvertUtils.isEmpty(id)){
            result.error500("id不能为空");
            return result;
        }
        //删除收货地址
        iMemberShippingAddressService.removeById(id);
        result.success("收货地址已删除");
        return result;
    }
}
