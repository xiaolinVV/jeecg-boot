package org.jeecg.modules.spider.csdn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 张少林
 * @desc 可以爬取指定用户的 csdn 博客所有文章，并保存到数据库中。
 * @date 2022年10月08日 10:12 下午
 */
@Component
public class CsdnBlogPageProcessor implements PageProcessor {

    public static String username = "wyljz";// 设置 csdn 用户名
    public static AtomicInteger pageNo = new AtomicInteger(0);// 共抓取到的文章数量
    public static String articleListUrl = "https://blog.csdn.net/community/home-api/v1/get-business-list?businessType=blog&page={page}&size=20&username={usrname}";

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");

    @Override
    public void process(Page page) {
        Html html = page.getHtml();

//        page.addTargetRequests(html.links().regex("https://blog.csdn.net/" + username + "/article/details/\\d+").all());

        //列表接口 json
        if (page.getUrl().toString().startsWith("https://blog.csdn.net/community/home-api/v1/get-business-list")) {
            Json json = page.getJson();
            JSONObject jsonObject = JSON.parseObject(json.toString());
            if (jsonObject.getInteger("code") == 200) {
                List<String> articleDetailUrls = jsonObject.getJSONObject("data").getJSONArray("list").stream().map(e -> ((JSONObject) e).getString("url")).collect(Collectors.toList());
                if (articleDetailUrls.size() != 0) {
                    page.addTargetRequests(articleDetailUrls);
                    int pageNum = pageNo.getAndIncrement();
                    String url = articleListUrl.replace("{page}", String.valueOf(pageNum)).replace("{usrname}", username);
                    page.addTargetRequest(url);
                }
            }
        }

        //详情页
        if (page.getUrl().regex("https://blog\\.csdn\\.net/" + username + "/article/details/\\d+").match()) {
            //列表页
            // 设置编号
            page.putField("id", Integer.parseInt(
                    page.getUrl().regex("https://blog\\.csdn\\.net/" + username + "/article/details/(\\d+)").get()));
            // 设置标题
            page.putField("title", html.$("h1.title-article", "text").toString());
            // 设置日期
            page.putField("date", html.$("span.time", "text").toString());
            // 设置标签（可以有多个，用，来分割）
            page.putField("tags", String.join(",", html.css("a.tag-link", "text").all()));
            // 设置类别（可以有多个，用，来分割）
            page.putField("category", String.join(",", html.css("a.tag-link", "text").all()));
            // 设置阅读人数
            page.putField("view", Integer.valueOf(html.css("span.read-count", "text").toString()));
            // 设置评论人数
//            page.putField("comments", html.css("span.count[style=color: rgb(153, 154, 170);]").toString());
            // 设置是否原创
//            page.putField("copyright",page.getHtml().regex("bog_copyright").match() ? 1 : 0);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//        https://blog.csdn.net/community/home-api/v1/get-business-list?page=2&size=20&businessType=blog&orderby=&noMore=false&year=&month=&username=wyljz
        // 从用户博客首页开始抓，开启 5 个线程，启动爬虫
        String url = articleListUrl.replace("{page}", String.valueOf(pageNo.getAndIncrement())).replace("{usrname}", username);

        Spider.create(new CsdnBlogPageProcessor())
                .addUrl(url)
                .thread(5)
                .run();
    }
}
