package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.system.entity.SysWithdrawDeposit;
import org.jeecg.modules.system.service.ISysWithdrawDepositService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
* @Description: 总账提现
* @Author: jeecg-boot
* @Date:   2021-05-15
* @Version: V1.0
*/
@Slf4j
@Api(tags="总账提现")
@RestController
@RequestMapping("/system/sysWithdrawDeposit")
public class SysWithdrawDepositController {
   @Autowired
   private ISysWithdrawDepositService sysWithdrawDepositService;

   @Autowired
   private HftxPayUtils hftxPayUtils;


    /**
     * 总账提现通用参数
     * @return
     */
   @RequestMapping("getPublicParam")
   @ResponseBody
   public Result<?> getPublicParam(){
       Map<String,Object> resultMap= Maps.newHashMap();
       try {
           Map<String,Object> balanceMap=hftxPayUtils.getSettleAccountBalance();
           resultMap.put("avlBalance",balanceMap.get("avl_balance"));
           resultMap.put("cashType","T1");
       } catch (BaseAdaPayException e) {
           e.printStackTrace();
       }
       return Result.ok(resultMap);
   }


    /**
     * 提现
     *
     * @param balance
     * @return
     */
   @RequestMapping("withdrawDeposit")
   @ResponseBody
   public Result<?> withdrawDeposit(@RequestParam(required = false,defaultValue = "0") BigDecimal balance){
       if(balance.doubleValue()<=0){
           return Result.error("提现金额不能为0");
       }
       try {
           Map<String,Object> resultMap=hftxPayUtils.withdrawDepositBase(OrderNoUtils.getOrderNo(),balance.toString());
           log.info("汇付天下提现返回："+JSON.toJSONString(resultMap));
           SysWithdrawDeposit sysWithdrawDeposit=new SysWithdrawDeposit();
           sysWithdrawDeposit.setOrderNo(resultMap.get("order_no").toString());//单号
           sysWithdrawDeposit.setCashType(resultMap.get("cash_type").toString());//取现类型：T1-T+1取现；D1-D+1取现；D0-即时取现。
           sysWithdrawDeposit.setCashAmt(new BigDecimal(resultMap.get("cash_amt").toString()));//取现金额，必须大于0，保留两位小数点，如0.10、100.05等
           sysWithdrawDeposit.setRealAmt(new BigDecimal(resultMap.get("real_amt").toString()));//取现成功后的到账金额，值为取现金额 - 取现手续费金额。
           sysWithdrawDeposit.setFeeAmt(new BigDecimal(resultMap.get("fee_amt").toString()));//取现手续费金额
           sysWithdrawDeposit.setStatus("1");
           sysWithdrawDepositService.save(sysWithdrawDeposit);
       } catch (BaseAdaPayException e) {
           e.printStackTrace();
       }
       return Result.ok();
   }


   /**
     * 分页列表查询
    * @param sysWithdrawDeposit
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "总账提现-分页列表查询")
   @ApiOperation(value="总账提现-分页列表查询", notes="总账提现-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<SysWithdrawDeposit>> queryPageList(SysWithdrawDeposit sysWithdrawDeposit,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<SysWithdrawDeposit>> result = new Result<IPage<SysWithdrawDeposit>>();
       QueryWrapper<SysWithdrawDeposit> queryWrapper = QueryGenerator.initQueryWrapper(sysWithdrawDeposit, req.getParameterMap());
       Page<SysWithdrawDeposit> page = new Page<SysWithdrawDeposit>(pageNo, pageSize);
       IPage<SysWithdrawDeposit> pageList = sysWithdrawDepositService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }



   /**
     * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "总账提现-通过id查询")
   @ApiOperation(value="总账提现-通过id查询", notes="总账提现-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<SysWithdrawDeposit> queryById(@RequestParam(name="id",required=true) String id) {
       Result<SysWithdrawDeposit> result = new Result<SysWithdrawDeposit>();
       SysWithdrawDeposit sysWithdrawDeposit = sysWithdrawDepositService.getById(id);
       if(sysWithdrawDeposit==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(sysWithdrawDeposit);
           result.setSuccess(true);
       }
       return result;
   }

 /**
     * 导出excel
  *
  * @param request
  * @param response
  */
 @RequestMapping(value = "/exportXls")
 public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
     // Step.1 组装查询条件
     QueryWrapper<SysWithdrawDeposit> queryWrapper = null;
     try {
         String paramsStr = request.getParameter("paramsStr");
         if (oConvertUtils.isNotEmpty(paramsStr)) {
             String deString = URLDecoder.decode(paramsStr, "UTF-8");
             SysWithdrawDeposit sysWithdrawDeposit = JSON.parseObject(deString, SysWithdrawDeposit.class);
             queryWrapper = QueryGenerator.initQueryWrapper(sysWithdrawDeposit, request.getParameterMap());
         }
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }

     //Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     List<SysWithdrawDeposit> pageList = sysWithdrawDepositService.list(queryWrapper);
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "总账提现列表");
     mv.addObject(NormalExcelConstants.CLASS, SysWithdrawDeposit.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("总账提现列表数据", "导出人:Jeecg", "导出信息"));
     mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
     return mv;
 }

 /**
     * 通过excel导入数据
  *
  * @param request
  * @param response
  * @return
  */
 @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
 public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
     MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
     Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
     for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
         MultipartFile file = entity.getValue();// 获取上传文件对象
         ImportParams params = new ImportParams();
         params.setTitleRows(2);
         params.setHeadRows(1);
         params.setNeedSave(true);
         try {
             List<SysWithdrawDeposit> listSysWithdrawDeposits = ExcelImportUtil.importExcel(file.getInputStream(), SysWithdrawDeposit.class, params);
             sysWithdrawDepositService.saveBatch(listSysWithdrawDeposits);
             return Result.ok("文件导入成功！数据行数:" + listSysWithdrawDeposits.size());
         } catch (Exception e) {
             log.error(e.getMessage(),e);
             return Result.error("文件导入失败:"+e.getMessage());
         } finally {
             try {
                 file.getInputStream().close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
     return Result.ok("文件导入失败！");
 }

}
