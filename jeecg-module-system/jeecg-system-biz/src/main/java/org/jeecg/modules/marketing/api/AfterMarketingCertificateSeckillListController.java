package org.jeecg.modules.marketing.api;

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
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillList;
import org.jeecg.modules.marketing.service.IMarketingCertificateSeckillListService;
import org.jeecg.modules.marketing.vo.MarketingCertificateSeckillListVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 限时抢券
 * @Author: jeecg-boot
 * @Date: 2021-03-29
 * @Version: V1.0
 */
@RequestMapping("after/marketingCertificateSeckillList")
@Controller
@Slf4j
public class AfterMarketingCertificateSeckillListController {
    @Autowired
    private IMarketingCertificateSeckillListService iMarketingCertificateSeckillListService;
    @RequestMapping("marketingCertificateSeckillListPay")
    @ResponseBody
    public Result<?> marketingCertificateSeckillListPay(String id,
                                                        Long number,
                                                        String money,
                                                        @RequestHeader(defaultValue = "") String longitude,
                                                        @RequestHeader(defaultValue = "") String latitude,
                                                        @RequestHeader(defaultValue = "") String softModel,
                                                        HttpServletRequest request){
        String memberId = request.getAttribute("memberId").toString();
        return null;
    }
}
