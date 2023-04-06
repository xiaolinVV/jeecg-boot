package org.jeecg.modules.searchHistory.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.searchHistory.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 张少林
 * @date 2023年04月04日 11:01 上午
 */
@RestController
@RequestMapping("/front/searchHistory")
public class SearchHistoryController {
    @Autowired
    SearchHistoryService searchHistoryService;

    /**
     * 添加个人搜索历史数据
     *
     * @param userId    用户id
     * @param searchKey 搜索内容
     * @return
     */
    @PostMapping("/add")
    public String addSearchHistoryByUserId(@RequestParam("userId") String userId, @RequestParam("searchKey") String searchKey) {
        searchHistoryService.addSearchHistoryByUserId(userId, searchKey);
        searchHistoryService.incrementScore(searchKey);
        return "添加成功";
    }

    /**
     * 删除个人历史数据
     */
    @PostMapping("/del")
    public Result<?> delSearchHistoryByUserId(@RequestParam(value = "userId",required = false) String userId, @RequestParam(value = "searchKey",required = false) String searchKey,HttpServletRequest request) {
        String memberId = Convert.toStr(request.getAttribute("memberId"));
        userId = StrUtil.blankToDefault(userId, memberId);
        return Result.ok(searchHistoryService.delSearchHistoryByUserId(userId, searchKey));
    }

    /**
     * 获取个人历史数据列表
     */
    @GetMapping("/getUser")
    public Result<?> getSearchHistoryByUserId(@RequestParam(value = "userId", required = false) String userId, HttpServletRequest request) {
        String memberId = Convert.toStr(request.getAttribute("memberId"));
        userId = StrUtil.blankToDefault(userId, memberId);
        return Result.ok(searchHistoryService.getSearchHistoryByUserId(userId));
    }


    /**
     * 根据searchKey搜索其相关最热的前十名 (如果searchKey为null空，则返回redis存储的前十最热词条)
     */
    @GetMapping("/getHot")
    public Result<?> getHotList(String searchKey) {
        return Result.ok(searchHistoryService.getHotList(searchKey));
    }

}
