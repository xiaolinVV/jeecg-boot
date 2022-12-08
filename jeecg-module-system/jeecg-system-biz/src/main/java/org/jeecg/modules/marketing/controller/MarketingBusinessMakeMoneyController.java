package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoney;
import org.jeecg.modules.marketing.service.IMarketingBusinessCapitalService;
import org.jeecg.modules.marketing.service.IMarketingBusinessMakeMoneyService;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyVO;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @Description: 创业进账资金
 * @Author: jeecg-boot
 * @Date: 2021-08-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "创业进账资金")
@RestController
@RequestMapping("/marketingBusinessMakeMoney/marketingBusinessMakeMoney")
public class MarketingBusinessMakeMoneyController extends JeecgController<MarketingBusinessMakeMoney, IMarketingBusinessMakeMoneyService> {
    @Autowired
    private IMarketingBusinessMakeMoneyService marketingBusinessMakeMoneyService;
    @Autowired
    private IMarketingBusinessCapitalService iMarketingBusinessCapitalService;
    @Autowired
    private IMemberListService iMemberListService;

    /**
     * 分页列表查询
     *
     * @param marketingBusinessMakeMoney
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "创业进账资金-分页列表查询")
    @ApiOperation(value = "创业进账资金-分页列表查询", notes = "创业进账资金-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MarketingBusinessMakeMoney marketingBusinessMakeMoney,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return Result.ok(marketingBusinessMakeMoneyService.queryPageList(new Page<MarketingBusinessMakeMoneyVO>(pageNo, pageSize),
                QueryGenerator.initQueryWrapper(marketingBusinessMakeMoney, req.getParameterMap()).eq("go_and_conme","0")));
    }
    @AutoLog(value = "创业出账资金-分页列表查询")
    @ApiOperation(value = "创业出账资金-分页列表查询", notes = "创业出账资金-分页列表查询")
    @GetMapping(value = "/comeList")
    public Result<?> comeList(MarketingBusinessMakeMoney marketingBusinessMakeMoney,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return Result.ok(marketingBusinessMakeMoneyService.queryPageList(new Page<MarketingBusinessMakeMoneyVO>(pageNo, pageSize),
                QueryGenerator.initQueryWrapper(marketingBusinessMakeMoney, req.getParameterMap()).eq("go_and_conme","1")));
    }
    /**
     * 添加
     *
     * @param marketingBusinessMakeMoney
     * @return
     */
    @AutoLog(value = "创业进账资金-添加")
    @ApiOperation(value = "创业进账资金-添加", notes = "创业进账资金-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MarketingBusinessMakeMoney marketingBusinessMakeMoney) {
        marketingBusinessMakeMoneyService.save(marketingBusinessMakeMoney);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param marketingBusinessMakeMoney
     * @return
     */
    @AutoLog(value = "创业进账资金-编辑")
    @ApiOperation(value = "创业进账资金-编辑", notes = "创业进账资金-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody MarketingBusinessMakeMoney marketingBusinessMakeMoney) {
        marketingBusinessMakeMoneyService.updateById(marketingBusinessMakeMoney);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "创业进账资金-通过id删除")
    @ApiOperation(value = "创业进账资金-通过id删除", notes = "创业进账资金-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        marketingBusinessMakeMoneyService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "创业进账资金-批量删除")
    @ApiOperation(value = "创业进账资金-批量删除", notes = "创业进账资金-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.marketingBusinessMakeMoneyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "创业进账资金-通过id查询")
    @ApiOperation(value = "创业进账资金-通过id查询", notes = "创业进账资金-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MarketingBusinessMakeMoney marketingBusinessMakeMoney = marketingBusinessMakeMoneyService.getById(id);
        return Result.ok(marketingBusinessMakeMoney);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param marketingBusinessMakeMoney
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request,
                                  MarketingBusinessMakeMoney marketingBusinessMakeMoney) {
        QueryWrapper<MarketingBusinessMakeMoney> businessMakeMoneyQueryWrapper = QueryGenerator.initQueryWrapper(marketingBusinessMakeMoney, request.getParameterMap()).eq("go_and_conme", "0");
        return null;
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
        return super.importExcel(request, response, MarketingBusinessMakeMoney.class);
    }

    /**
     * 手动出账
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("manualChargeOff")
    public Result<?> manualChargeOff(@RequestBody JSONObject jsonObject) {
        String marketingBusinessCapitalId = jsonObject.getString("marketingBusinessCapitalId");
        String memberListId = jsonObject.getString("memberListId");
        BigDecimal amount = jsonObject.getBigDecimal("amount");
        if (StringUtils.isBlank(marketingBusinessCapitalId)) {
            return Result.error("资金池id未传递");
        }
        if (StringUtils.isBlank(memberListId)) {
            return Result.error("会员id未传递");
        }
        if (amount.doubleValue() <= 0) {
            return Result.error("金额必须大于0");
        }
        MarketingBusinessCapital marketingBusinessCapital = iMarketingBusinessCapitalService.getById(marketingBusinessCapitalId);
        if (marketingBusinessCapital == null) {
            return Result.error("未找到资金池,id是否传递正确");
        }
        MemberList memberList = iMemberListService.getById(memberListId);
        if (memberList == null) {
            return Result.error("未找到会员,id是否传递正确");
        }
        return Result.ok("操作成功");
    }
}
