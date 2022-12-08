package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingTextbookColumn;

import java.util.List;
import java.util.Map;

/**
 * @Description: 教程素材
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
public interface IMarketingTextbookColumnService extends IService<MarketingTextbookColumn> {

    List<Map<String,Object>> findMarketingTextbookColumn();



}
