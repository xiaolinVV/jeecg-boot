package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * 收货地API址控制层
 */
@RequestMapping("back/memberShippingAddress")
@Controller
public class BackMemberShippingAddressController {
    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;
    @Autowired
    private IStoreManageService iStoreManageService;
    /**
     * 查询收货地址列表
     * @param request
     * @return
     */
    @RequestMapping("getAddress")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> getAddress(HttpServletRequest request, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        String sysUserId=request.getAttribute("sysUserId").toString();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUserId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");
        if(iStoreManageService.count(storeManageQueryWrapper)<=0){
            result.error500("查询不到开店用户信息！！！");
            return result;
        }
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);


        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);

        result.setResult(iMemberShippingAddressService.getMemberShippingAddressByMemberId(page,storeManage.getMemberListId()));

        result.success("查询收货地址列表");
        return result;

    }
}
