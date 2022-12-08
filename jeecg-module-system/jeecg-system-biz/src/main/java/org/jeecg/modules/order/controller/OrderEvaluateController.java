package org.jeecg.modules.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.dto.OrderEvaluateDTO;
import org.jeecg.modules.order.dto.OrderListDTO;
import org.jeecg.modules.order.entity.OrderEvaluate;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.service.IOrderEvaluateService;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台商品评价
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "平台商品评价")
@RestController
@RequestMapping("/orderEvaluate/orderEvaluate")
public class OrderEvaluateController {
    @Autowired
    private IOrderEvaluateService orderEvaluateService;
    @Autowired
    private IOrderListService orderListService;
    @Autowired
    private IMemberListService memberListService;

    /**
     * 分页列表查询
     *
     * @param orderEvaluate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "平台商品评价-分页列表查询")
    @ApiOperation(value = "平台商品评价-分页列表查询", notes = "平台商品评价-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OrderEvaluate>> queryPageList(OrderEvaluate orderEvaluate,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        Result<IPage<OrderEvaluate>> result = new Result<IPage<OrderEvaluate>>();
        QueryWrapper<OrderEvaluate> queryWrapper = QueryGenerator.initQueryWrapper(orderEvaluate, req.getParameterMap());
        Page<OrderEvaluate> page = new Page<OrderEvaluate>(pageNo, pageSize);
        IPage<OrderEvaluate> pageList = orderEvaluateService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param orderEvaluate
     * @return
     */
    @AutoLog(value = "平台商品评价-添加")
    @ApiOperation(value = "平台商品评价-添加", notes = "平台商品评价-添加")
    @PostMapping(value = "/add")
    public Result<OrderEvaluate> add(@RequestBody OrderEvaluate orderEvaluate) {
        Result<OrderEvaluate> result = new Result<OrderEvaluate>();
        try {
            orderEvaluateService.save(orderEvaluate);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param orderEvaluate
     * @return
     */
    @AutoLog(value = "平台商品评价-编辑")
    @ApiOperation(value = "平台商品评价-编辑", notes = "平台商品评价-编辑")
    @PutMapping(value = "/edit")
    public Result<OrderEvaluate> edit(@RequestBody OrderEvaluate orderEvaluate) {
        Result<OrderEvaluate> result = new Result<OrderEvaluate>();
        OrderEvaluate orderEvaluateEntity = orderEvaluateService.getById(orderEvaluate.getId());
        if (orderEvaluateEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = orderEvaluateService.updateById(orderEvaluate);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "平台商品评价-通过id删除")
    @ApiOperation(value = "平台商品评价-通过id删除", notes = "平台商品评价-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            orderEvaluateService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "平台商品评价-批量删除")
    @ApiOperation(value = "平台商品评价-批量删除", notes = "平台商品评价-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<OrderEvaluate> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<OrderEvaluate> result = new Result<OrderEvaluate>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.orderEvaluateService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "平台商品评价-通过id查询")
    @ApiOperation(value = "平台商品评价-通过id查询", notes = "平台商品评价-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OrderEvaluate> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OrderEvaluate> result = new Result<OrderEvaluate>();
        OrderEvaluate orderEvaluate = orderEvaluateService.getById(id);
        if (orderEvaluate == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(orderEvaluate);
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
        QueryWrapper<OrderEvaluate> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                OrderEvaluate orderEvaluate = JSON.parseObject(deString, OrderEvaluate.class);
                queryWrapper = QueryGenerator.initQueryWrapper(orderEvaluate, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<OrderEvaluate> pageList = orderEvaluateService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "平台商品评价列表");
        mv.addObject(NormalExcelConstants.CLASS, OrderEvaluate.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台商品评价列表数据", "导出人:Jeecg", "导出信息"));
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
                List<OrderEvaluate> listOrderEvaluates = ExcelImportUtil.importExcel(file.getInputStream(), OrderEvaluate.class, params);
                orderEvaluateService.saveBatch(listOrderEvaluates);
                return Result.ok("文件导入成功！数据行数:" + listOrderEvaluates.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
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

    /**
     * 通过id查询修改状态:启用停用
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品管理-通过id查询")
    @ApiOperation(value = "商品管理-通过id查询", notes = "商品管理-通过id查询")
    @GetMapping(value = "/updateStatus")
    public Result<OrderEvaluate> updateStatus(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "status", required = true) String status, String closeExplain) {
        Result<OrderEvaluate> result = new Result<OrderEvaluate>();

        OrderList orderList = orderListService.getById(id);
        if (orderList == null) {
            result.error500("未找到对应实体");
            return result;
        }
        QueryWrapper<OrderEvaluate> queryWrapperOrderEvaluate = new QueryWrapper();
        queryWrapperOrderEvaluate.eq("order_list_id", orderList.getId());
        List<OrderEvaluate> orderEvaluateList = orderEvaluateService.list(queryWrapperOrderEvaluate);
        if (orderEvaluateList.size() == 0) {
            result.error500("未找到评论信息!");
            return result;
        }
        for (OrderEvaluate orderEvaluate : orderEvaluateList) {
            orderEvaluate.setStatus(status);
            orderEvaluate.setCloseExplain(closeExplain);
            orderEvaluate.setAuditTime(new Date());
            orderEvaluateService.updateById(orderEvaluate);
        }
        result.success("修改成功!");
        return result;
    }


    /**
     * 评论内容
     *
     * @param orderListId
     * @param type        0:平台,1:供应商
     * @return
     */
    @GetMapping(value = "/reviewInformation")
    public Result<OrderListDTO> reviewInformation(@RequestParam(name = "orderListId", required = true) String orderListId) {
        Result<OrderListDTO> result = new Result<OrderListDTO>();

        OrderList orderList = orderListService.getById(orderListId);

        if (orderList != null) {
            MemberList memberList = memberListService.getById(orderList.getMemberListId());

            OrderListDTO orderListDTO = new OrderListDTO();

            BeanUtils.copyProperties(orderList, orderListDTO);
            if (memberList != null) {
                orderListDTO.setNickName(memberList.getNickName());
            }
            List<OrderEvaluateDTO> orderEvaluateDTOList = orderEvaluateService.discussList(orderListDTO.getId());
            if (orderEvaluateDTOList.size() > 0) {
                orderListDTO.setOrderEvaluateDTOList(orderEvaluateDTOList);
                result.setResult(orderListDTO);
                result.setSuccess(true);
            } else {
                result.error500("未找到对应实体");
            }
        } else {

            result.error500("未找到对应实体");
        }

        return result;
    }
}
