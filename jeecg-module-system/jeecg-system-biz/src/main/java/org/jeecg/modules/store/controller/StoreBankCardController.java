package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.message.api.AfterMessageController;
import org.jeecg.modules.store.dto.StoreBankCardDTO;
import org.jeecg.modules.store.entity.StoreBankCard;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreBankCardService;
import org.jeecg.modules.store.service.IStoreManageService;
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
 * @Description: 店铺银行卡
 * @Author: jeecg-boot
 * @Date: 2019-10-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "店铺银行卡")
@RestController
@RequestMapping("/storeBankCard/storeBankCard")
public class StoreBankCardController {
    @Autowired
    private IStoreBankCardService storeBankCardService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private AfterMessageController afterMessageController;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 获取银行卡信息
     *
     * @param storeManageId
     * @return
     */
    @GetMapping("getStoreBankCardByStoreManageId")
    public Result<?> getStoreBankCardByStoreManageId(String storeManageId){
        return Result.ok(storeBankCardService.getStoreBankCardByStoreManageId(storeManageId,"0"));
    }


    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "店铺银行卡-分页列表查询")
    @ApiOperation(value = "店铺银行卡-分页列表查询", notes = "店铺银行卡-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<StoreBankCard>> queryPageList(StoreBankCardDTO storeBankCardDTO,
                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest req) {
        Result<IPage<StoreBankCard>> result = new Result<IPage<StoreBankCard>>();
        Page<StoreBankCard> page = new Page<StoreBankCard>(pageNo, pageSize);
        IPage<StoreBankCard> pageList = storeBankCardService.queryPageList(page, storeBankCardDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     * 弃用
     * @param storeBankCard
     * @return
     */
    @AutoLog(value = "店铺银行卡-添加")
    @ApiOperation(value = "店铺银行卡-添加", notes = "店铺银行卡-添加")
    @PostMapping(value = "/add")
    public Result<StoreBankCard> add(@RequestBody StoreBankCard storeBankCard) {
        Result<StoreBankCard> result = new Result<StoreBankCard>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", userId);
        StoreManage one = iStoreManageService.getOne(storeManageQueryWrapper);
        try {
            storeBankCard.setStoreManageId(one.getId());
            storeBankCardService.save(storeBankCard);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }
    /**
     *   添加
     * @param storeBankCard
     * @return
     */
    @AutoLog(value = "店铺银行卡-添加")
    @ApiOperation(value="店铺银行卡-添加", notes="店铺银行卡-添加")
    @PostMapping(value = "/added")
    public Result<StoreBankCard> added(@RequestBody StoreBankCard storeBankCard) {
        Result<StoreBankCard> result = new Result<StoreBankCard>();
        try {
            storeBankCardService.save(storeBankCard);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }
    /**
     * 编辑
     *
     * @param storeBankCard
     * @return
     */
    @AutoLog(value = "店铺银行卡-编辑")
    @ApiOperation(value = "店铺银行卡-编辑", notes = "店铺银行卡-编辑")
    @PostMapping(value = "/edit")
    public Result<StoreBankCard> edit(@RequestBody StoreBankCard storeBankCard) {
        Result<StoreBankCard> result = new Result<StoreBankCard>();
        StoreBankCard storeBankCardEntity = storeBankCardService.getById(storeBankCard.getId());
        if (storeBankCardEntity == null) {
            result.error500("未找到对应实体");
        } else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            boolean ok = storeBankCardService.updateById(storeBankCard
                    .setUpdateBy(sysUser.getUsername())
                    .setUpdateTime(new Date())
            );
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
    @AutoLog(value = "店铺银行卡-通过id删除")
    @ApiOperation(value = "店铺银行卡-通过id删除", notes = "店铺银行卡-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            storeBankCardService.removeById(id);
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
    @AutoLog(value = "店铺银行卡-批量删除")
    @ApiOperation(value = "店铺银行卡-批量删除", notes = "店铺银行卡-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<StoreBankCard> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<StoreBankCard> result = new Result<StoreBankCard>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.storeBankCardService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "店铺银行卡-通过id查询")
    @ApiOperation(value = "店铺银行卡-通过id查询", notes = "店铺银行卡-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<StoreBankCard> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<StoreBankCard> result = new Result<StoreBankCard>();
        StoreBankCard storeBankCard = storeBankCardService.getById(id);
        if (storeBankCard == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(storeBankCard);
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
        QueryWrapper<StoreBankCard> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                StoreBankCard storeBankCard = JSON.parseObject(deString, StoreBankCard.class);
                queryWrapper = QueryGenerator.initQueryWrapper(storeBankCard, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<StoreBankCard> pageList = storeBankCardService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "店铺银行卡列表");
        mv.addObject(NormalExcelConstants.CLASS, StoreBankCard.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺银行卡列表数据", "导出人:Jeecg", "导出信息"));
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
                List<StoreBankCard> listStoreBankCards = ExcelImportUtil.importExcel(file.getInputStream(), StoreBankCard.class, params);
                storeBankCardService.saveBatch(listStoreBankCards);
                return Result.ok("文件导入成功！数据行数:" + listStoreBankCards.size());
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

    @AutoLog(value = "店铺银行卡-返显")
    @ApiOperation(value = "店铺银行卡-返显", notes = "店铺银行卡-返显")
    @RequestMapping(value = "findStoreBankCard", method = RequestMethod.GET)
    public Result<Map<String,Object>> findStoreBankCard() {
        return storeBankCardService.findStoreBankCard();
    }

    @AutoLog(value = "店铺银行卡-短信验证")
    @ApiOperation(value = "店铺银行卡-短信验证", notes = "店铺银行卡-短信验证")
    @RequestMapping("bankCardByPhone")
    public Result<String> bankCardByPhone(@RequestParam(value = "phone", required = true) String phone) {
        return afterMessageController.verificationCode(phone);
    }

    @AutoLog(value = "店铺银行卡-设置银行卡")
    @ApiOperation(value = "店铺银行卡-设置银行卡", notes = "店铺银行卡-设置银行卡")
    @RequestMapping(value = "setBankCard", method = RequestMethod.POST)
    public Result<StoreBankCard> setBankCard(@RequestBody StoreBankCardDTO storeBankCardDTO) {
        Result<StoreBankCard> result = new Result<>();
        //判断验证码
        Object code = redisUtil.get(storeBankCardDTO.getPhone());
        if (!storeBankCardDTO.getSbCode().equals(code)) {
            result.error500("验证码错误");
        } else {
            //获取当前登录人
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //获取当前登录人店铺
            QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
            storeManageQueryWrapper.eq("sys_user_id",sysUser.getId());
            StoreManage one = iStoreManageService.getOne(storeManageQueryWrapper);
            StoreBankCard storeBankCard = new StoreBankCard();
            BeanUtils.copyProperties(storeBankCardDTO,storeBankCard);
            if (StringUtils.isBlank(storeBankCard.getId())){
                storeBankCard.setStoreManageId(one.getId());
                this.added(storeBankCard);
            }else {
                this.edit(storeBankCard);
            }
        }
        return result;
    }
    @AutoLog(value = "店铺银行卡-通过店铺id返回银行卡信息")
    @ApiOperation(value = "店铺银行卡-通过店铺id返回银行卡信息", notes = "店铺银行卡-通过店铺id返回银行卡信息")
    @RequestMapping(value = "findBankCardById", method = RequestMethod.GET)
    public List<StoreBankCardDTO> findBankCardById(@RequestParam(value = "id",required = true)String id){
        return storeBankCardService.findBankCardById(id);
    }
}
