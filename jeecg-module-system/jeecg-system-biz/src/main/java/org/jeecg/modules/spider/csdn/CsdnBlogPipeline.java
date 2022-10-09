package org.jeecg.modules.spider.csdn;

import cn.hutool.core.convert.Convert;
import org.jeecg.modules.demo.cmsCsdnBlog.entity.CmsCsdnBlog;
import org.jeecg.modules.demo.cmsCsdnBlog.service.ICmsCsdnBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author 张少林
 * @date 2022年10月09日 2:51 下午
 */
@Component
public class CsdnBlogPipeline implements Pipeline {

    @Autowired
    ICmsCsdnBlogService cmsCsdnBlogService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        CmsCsdnBlog cmsCsdnBlog = new CmsCsdnBlog()
                .setId(Convert.toStr(resultItems.get("id")))
                .setTitle(resultItems.get("title"))
                .setPostDate(resultItems.get("date"))
                .setTags(resultItems.get("tags"))
                .setCategory(resultItems.get("category"))
                .setView(resultItems.get("view"));
        cmsCsdnBlogService.save(cmsCsdnBlog);
    }
}
