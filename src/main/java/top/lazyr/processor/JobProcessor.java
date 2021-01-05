package top.lazyr.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.regex.Pattern;

public class JobProcessor implements PageProcessor {
    /**
     * 利用正则表达式匹配标签下的书籍列表界面URL
     * 例如：https://book.douban.com/tag/名著
     */
    static String tagLinks="https://book.douban.com/tag/.*";
    static Pattern tagLinksPattern= Pattern.compile(tagLinks);



    // 负责解析页面
    public void process(Page page) {
        // 解析返回的数据page，并把解析的结果放到ResultItems中
        Html html=page.getHtml();

        /**
         *豆瓣读书标签列表页面
         *在这个界面获取标签的URL，并加入爬取URL队列
         */
        if (page.getUrl().toString().equals("https://book.douban.com/tag/?view=type")) {
            String douban="https://book.douban.com/tag/";
            //匹配<table class="tagCol">下的<tbody>标签下的<tr>标签下的<td>标签下的<a>中的内容
            List<String> tags=html.xpath("//table[@class='tagCol']/tbody/tr/td/a/text()").all();
            //补全URL，并加入爬去队列
            for (String string : tags) {
                page.addTargetRequest(douban+string);
            }
        }

        /**
         *豆瓣读书标签下的书籍列表界面
         *输出标签名以及书籍信息
         */
        else if(tagLinksPattern.matcher(page.getUrl().toString()).matches()) {

            //获取标签名
            String tag=html.xpath("//div[@id='content']/h1/text()").get();
            String[] tagSplit=tag.split(" ");
            //输出标签名
            System.out.println("<----"+tagSplit[1]+"---->");

            //获取书籍信息
            List<String> circles=html.xpath("//li[@class='subject-item']/div[@class='info']/h2/a/text()").all();
            List<String> circleUrls=html.xpath("//li[@class='subject-item']/div[@class='info']/h2/a/@href").all();
            //输出书籍信息
            for (int k=0;k<circles.size();k++) {
                System.out.print("序号： "+(k+1)+" 书名：");
                System.out.print(circles.get(k)+" 地址：");
                System.out.print(circleUrls.get(k)+" ID：");
                String[] ids=circleUrls.get(k).split("/");
                System.out.println(ids[ids.length-1]);

            }
            //换行
            System.out.println();
        }

    }

    public Site getSite() {
        return  Site.me()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .setRetryTimes(3)
                    .setSleepTime(1000);
    }

    public static void main(String[] args) {
        Spider.create(new JobProcessor())
                .addUrl("https://book.douban.com/tag/?view=type")
                .run();
    }
}
