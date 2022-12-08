package org.jeecg.modules.store.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStorePermissionUidService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("back/storeAccountCapital")
@Slf4j
public class BackStoreAccountCapitalController {
    @Autowired
    private IStoreAccountCapitalService iStoreAccountCapitalService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IOrderStoreListService orderStoreListService;

    @Autowired
    private IStorePermissionUidService storePermissionUidService;

    /**
     * 资金管理
     * @param goAndCome
     * @param request
     * @return
     */
    @RequestMapping(value = "getStoreAccountCapital")
    @ResponseBody
    public Result<Map<String,Object>> getStoreAccountCapital(String goAndCome,
                                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                             HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        Map<String,Object> objectMap= Maps.newHashMap();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUserId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");
        if(iStoreManageService.count(storeManageQueryWrapper)<=0){
            result.error500("查询不到开店用户信息！！！");
            return result;
        }
        StoreManage storeManage=iStoreManageService.list(storeManageQueryWrapper).get(0);
        //资金流水 信息 store_manage_id
        QueryWrapper<StoreAccountCapital> queryWrapperStoreAccountCapital = new QueryWrapper();
        queryWrapperStoreAccountCapital.select("id,create_time as createTime,amount,balance,go_and_come as goAndCome,pay_type as payType");
        queryWrapperStoreAccountCapital.eq("store_manage_id",storeManage.getId());
        queryWrapperStoreAccountCapital.eq("del_flag","0");
        if(StringUtils.isNotBlank(goAndCome)){
            //添加收入跟支出条件
            queryWrapperStoreAccountCapital.eq("go_and_come",goAndCome);
        }
        queryWrapperStoreAccountCapital.orderByDesc("create_time","balance");
        IPage<Map<String, Object>> page=new Page<Map<String, Object>>(pageNo,pageSize);
        IPage<Map<String,Object>> storeAccountCapitalListMap = iStoreAccountCapitalService.pageMaps(page,queryWrapperStoreAccountCapital);
        storeAccountCapitalListMap.getRecords().forEach(sac->{
            //处理资金流水金额
            if(sac.get("goAndCome").equals("0")){
                //收入
                sac.put("balance", new BigDecimal(sac.get("balance").toString()));
            }else{
                //支出
                sac.put("balance", new BigDecimal(sac.get("balance").toString()));
            }

        });

        //参数处理
        objectMap.put("id",storeManage.getId());//店面id
        objectMap.put("sysUserId",storeManage.getSysUserId());//用户uid
        objectMap.put("balance",storeManage.getBalance());//余额
        objectMap.put("storeAccountCapitalListMap",storeAccountCapitalListMap);//资金流水
        result.setCode(200);
        result.setResult(objectMap);
        return result;
    }
    /**
     * 商家端工作台
     * @param request
     * @return
     */
    @RequestMapping("getWorkbench")
    @ResponseBody
    public Result<Map<String,Object>> getWorkbench(Integer isPlatform,HttpServletRequest request){
        String sysUserId = request.getAttribute("sysUserId").toString();
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUserId);
        //storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");

        if(iStoreManageService.count(storeManageQueryWrapper)<=0){
            result.error500("查询不到开店用户信息！！！");
            return result;
        }
        StoreManage storeManage=iStoreManageService.list(storeManageQueryWrapper).get(0);
        Map<String,String> paramMap = Maps.newHashMap();
        paramMap.put("storeManageId",storeManage.getId());
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记
        paramMap.put("createTime",sdf.format(new Date()));
        //收款金额
        BigDecimal storeIncomeSummation=storeManage.getBalance();
        if(storeIncomeSummation == null){
            storeIncomeSummation = new BigDecimal(0)  ;
        }
        //新增用户
        QueryWrapper<MemberList> queryWrapperMemberList = new QueryWrapper<>();
        queryWrapperMemberList.eq("sys_user_id",storeManage.getSysUserId());
        Long   memberCount = iMemberListService.count(queryWrapperMemberList);
        //待发货订单
        QueryWrapper<OrderStoreList> queryWrapperOrderStoreList = new QueryWrapper<>();
        queryWrapperOrderStoreList.eq("status","1");
        queryWrapperOrderStoreList.eq("sys_user_id",storeManage.getSysUserId());
        Long  orderStoreObligationCount= orderStoreListService.count(queryWrapperOrderStoreList);
        //待处理退款单

        objectMap.put("pendingRefund","0");
        //商家端菜单
        List<Map<String,Object>> storePermissionUidList = storePermissionUidService.getStorePermissionUidMap(sysUserId);

        //封装参数
        objectMap.put("storeIncomeSummation",storeIncomeSummation);
        objectMap.put("memberCount",memberCount);
        objectMap.put("orderStoreObligationCount",orderStoreObligationCount);
        objectMap.put("pendingRefund",orderStoreObligationCount);
        objectMap.put("attestationStatus",storeManage.getAttestationStatus());
        objectMap.put("storePermissionUidList",storePermissionUidList);
        result.setResult(objectMap);
        result.setCode(200);
        return result;
    }

    /**
     * 获取当前登录人店铺信息
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("getStoreManage")
    @ResponseBody
    public Result<StoreManage>  getStoreManage(Integer isPlatform,HttpServletRequest request){
        String sysUserId = request.getAttribute("sysUserId").toString();
        Result<StoreManage> result=new Result<>();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id",sysUserId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");
        List<StoreManage> storeManageList =  iStoreManageService.list(storeManageQueryWrapper);
      if(storeManageList.size()>0){
        result.setResult(storeManageList.get(0));
      }
        result.setCode(200);
        return result;
    }
    @RequestMapping("findStoreAccountCapitalInfo")
    @ResponseBody
    public Result<IPage<Map<String, Object>>>findStoreAccountCapitalInfo(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                 @RequestParam(name="isPlatform", defaultValue="2") String isPlatform,
                                                                 @RequestParam(value="id",required = true) String id,
                                                                 HttpServletRequest request){
        Result<IPage<Map<String, Object>>> result = new Result<>();
        Page<StoreAccountCapital> page = new Page<StoreAccountCapital>(pageNo, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("isPlatform",isPlatform);
        IPage<Map<String, Object>> storeAccountCapitalInfo = iStoreAccountCapitalService.findStoreAccountCapitalInfo(page, map);
        result.setResult(storeAccountCapitalInfo);
        result.success("返回可用余额明细成功!");
        return result;
    }
}
