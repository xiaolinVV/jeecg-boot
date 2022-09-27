package org.jeecg.modules.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author 张少林
 * @date 2022年09月24日 10:11 下午
 */
public class ZhihuHotQuestionPageProcessor implements PageProcessor {

    private Site site = Site.me();

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String type = html.xpath("//*[@id=\"root\"]/div/main/div/div[2]/div/div/div[2]/div/a").toString();
        page.putField("type",type);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ZhihuHotQuestionPageProcessor())
                .addUrl("https://www.zhihu.com/creator/hot-question/hot/0/hour")
                .run();
    }
}
