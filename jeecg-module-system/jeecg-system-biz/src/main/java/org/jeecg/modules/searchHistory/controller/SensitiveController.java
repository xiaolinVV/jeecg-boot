package org.jeecg.modules.searchHistory.controller;

import org.jeecg.modules.searchHistory.filter.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张少林
 * @date 2023年04月04日 11:00 上午
 */

@RestController
public class SensitiveController {

    @Autowired
    SensitiveFilter sensitiveFilter;


    @GetMapping("/sensitive")
    public String sensitive(String keyword) {
        String s = sensitiveFilter.replaceSensitiveWord(keyword);
        return s;
    }
}
