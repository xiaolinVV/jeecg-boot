package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.marketing.entity.MarketingBusinessCapital;
import org.jeecg.modules.marketing.entity.MarketingBusinessDesignation;
import org.jeecg.modules.marketing.service.IMarketingBusinessCapitalService;
import org.jeecg.modules.marketing.service.IMarketingBusinessDesignationService;
import org.jeecg.modules.marketing.vo.MarketingBusinessCapitalVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 创业资金池配置
 * @Author: jeecg-boot
 * @Date: 2021-08-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "创业资金池配置")
@RestController
@RequestMapping("/marketingBusinessCapital/marketingBusinessCapital")
public class MarketingBusinessCapitalController extends JeecgController<MarketingBusinessCapital, IMarketingBusinessCapitalService> {
    @Autowired
    private IMarketingBusinessCapitalService marketingBusinessCapitalService;
    @Autowired
    private IMarketingBusinessDesignationService iMarketingBusinessDesignationService;
    /**
     * 分页列表查询
     *
     * @param marketingBusinessCapital
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "创业资金池配置-分页列表查询")
    @ApiOperation(value = "创业资金池配置-分页列表查询", notes = "创业资金池配置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MarketingBusinessCapital marketingBusinessCapital,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MarketingBusinessCapital> queryWrapper = QueryGenerator.initQueryWrapper(marketingBusinessCapital, req.getParameterMap());
        Page<MarketingBusinessCapital> page = new Page<MarketingBusinessCapital>(pageNo, pageSize);
        IPage<MarketingBusinessCapital> pageList = marketingBusinessCapitalService.page(page, queryWrapper);
        List<MarketingBusinessCapital> records = pageList.getRecords();
        IPage<MarketingBusinessCapitalVO> pageListNew = new Page<>();
        ArrayList<MarketingBusinessCapitalVO> marketingBusinessCapitalVOS = new ArrayList<>();
        for (MarketingBusinessCapital record : records) {
            MarketingBusinessCapitalVO marketingBusinessCapitalVO = new MarketingBusinessCapitalVO();
            BeanUtils.copyProperties(record,marketingBusinessCapitalVO);
            marketingBusinessCapitalVO.setIsView(record.getIsView().equals("1")?true:false);
            marketingBusinessCapitalVOS.add(marketingBusinessCapitalVO);
        }
        pageListNew.setRecords(marketingBusinessCapitalVOS);
        pageListNew.setPages(pageList.getPages());
        pageListNew.setSize(pageList.getSize());
        pageListNew.setTotal(pageList.getTotal());
        return Result.ok(pageListNew);
    }

    /**
     * 添加
     *
     * @param marketingBusinessCapital
     * @return
     */
    @AutoLog(value = "创业资金池配置-添加")
    @ApiOperation(value = "创业资金池配置-添加", notes = "创业资金池配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MarketingBusinessCapital marketingBusinessCapital) {
        if (marketingBusinessCapital.getCapitalType().equals("1")){
            MarketingBusinessDesignation marketingBusinessDesignation = iMarketingBusinessDesignationService.getById(marketingBusinessCapital.getMarketingBusinessDesignationId());
            marketingBusinessCapital
                    .setCapitalName(marketingBusinessDesignation!=null?marketingBusinessDesignation.getDesignationName():"");
        }
        if (marketingBusinessCapital.getSessionControl().equals("0")){

        }
        marketingBusinessCapitalService.save(marketingBusinessCapital);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param marketingBusinessCapital
     * @return
     */
    @AutoLog(value = "创业资金池配置-编辑")
    @ApiOperation(value = "创业资金池配置-编辑", notes = "创业资金池配置-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody MarketingBusinessCapital marketingBusinessCapital) {
        if (StringUtils.isBlank(marketingBusinessCapital.getId())) {
            return Result.error("前端id未传递!");
        }
        MarketingBusinessCapital marketingBusinessCapitalEntity = marketingBusinessCapitalService.getById(marketingBusinessCapital.getId());
        if (marketingBusinessCapitalEntity != null) {
            if (marketingBusinessCapital.getCapitalType().equals("1")){
                MarketingBusinessDesignation marketingBusinessDesignation = iMarketingBusinessDesignationService.getById(marketingBusinessCapital.getMarketingBusinessDesignationId());
                marketingBusinessCapital
                        .setCapitalName(marketingBusinessDesignation!=null?marketingBusinessDesignation.getDesignationName():"");
            }
            marketingBusinessCapitalService.updateById(marketingBusinessCapital);
            return Result.ok("编辑成功!");
        } else {
            return Result.error("未找到对应实体");
        }

    }
    @PostMapping(value = "/isViewUpdate")
    public Result<?> isViewUpdate(@RequestBody MarketingBusinessCapitalVO marketingBusinessCapitalVO){
        if (StringUtils.isBlank(marketingBusinessCapitalVO.getId())){
            return Result.error("前端id未传递!");
        }
        MarketingBusinessCapital marketingBusinessCapital = marketingBusinessCapitalService.getById(marketingBusinessCapitalVO.getId());
        marketingBusinessCapitalService.updateById(marketingBusinessCapital
                .setIsView(marketingBusinessCapitalVO.getIsView()?"1":"0"));
        return Result.ok("操作成功");
    }
    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "创业资金池配置-通过id删除")
    @ApiOperation(value = "创业资金池配置-通过id删除", notes = "创业资金池配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        marketingBusinessCapitalService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "创业资金池配置-批量删除")
    @ApiOperation(value = "创业资金池配置-批量删除", notes = "创业资金池配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.marketingBusinessCapitalService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "创业资金池配置-通过id查询")
    @ApiOperation(value = "创业资金池配置-通过id查询", notes = "创业资金池配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MarketingBusinessCapital marketingBusinessCapital = marketingBusinessCapitalService.getById(id);
        return Result.ok(marketingBusinessCapital);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param marketingBusinessCapital
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MarketingBusinessCapital marketingBusinessCapital) {
        return super.exportXls(request, marketingBusinessCapital, MarketingBusinessCapital.class, "创业资金池配置");
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
        return super.importExcel(request, response, MarketingBusinessCapital.class);
    }

    /**
     * 获取全部资金池
     * @return
     */
    @GetMapping("findMarketingBusinessCapitalMaps")
    public Result<?> findMarketingBusinessCapitalMaps() {
        return Result.ok(marketingBusinessCapitalService.listMaps(new QueryWrapper<MarketingBusinessCapital>()
                .select("id,capital_name as capitalName")
                .eq("del_flag","0")
        ));
    }

    /**
     * 获取非称号资金池
     * @return
     */
    @GetMapping("findMarketingBusinessCapitalMapsByCapitalType")
    public Result<?> findMarketingBusinessCapitalMapsByCapitalType() {
        return Result.ok(marketingBusinessCapitalService.listMaps(new QueryWrapper<MarketingBusinessCapital>()
                .select("id,capital_name as capitalName,balance")
                .eq("capital_type","0")
                .eq("del_flag","0")
        ));
    }
}
