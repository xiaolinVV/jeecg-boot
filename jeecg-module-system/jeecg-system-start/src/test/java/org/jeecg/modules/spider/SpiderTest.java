package org.jeecg.modules.spider;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.modules.spider.csdn.CsdnBlogPageProcessor;
import org.jeecg.modules.spider.csdn.CsdnBlogPipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * @author 张少林
 * @date 2022年10月09日 2:49 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = JeecgSystemApplication.class)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class SpiderTest {

    @Autowired
    CsdnBlogPageProcessor csdnBlogPageProcessor;

    @Autowired
    CsdnBlogPipeline csdnBlogPipeline;


    @Test
    public void testCsdnBlogSpider() {
        String url = CsdnBlogPageProcessor.articleListUrl.replace("{page}", String.valueOf(CsdnBlogPageProcessor.pageNo.getAndIncrement())).replace("{usrname}", CsdnBlogPageProcessor.username);
        Spider.create(csdnBlogPageProcessor)
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .addPipeline(csdnBlogPipeline)
                .thread(10)
                .run();
    }
}
