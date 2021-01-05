package top.lazyr.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class ComplexProcessor implements PageProcessor {



    public void process(Page page) {
        // 获取链接
//        List<String> all = page.getHtml().css(".content>.title").links().all();
        // 发送请求
//        page.addTargetRequests(all);
//        for (String url : all) {
//            System.out.println("链接=>"+url);
//        }
        // 此时page已经变成了新发送请求的页面信息
//        String s = page.getHtml().css(".content>h1").get();
//        System.out.println("page=>"+page.getRequest());


        System.out.println(page.getHtml());
    }

    public Site getSite() {
        return  Site.me()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.60")
                .setRetryTimes(3)
                .setSleepTime(1000);
    }

    public static void main(String[] args) {
        // 1、创建下载器
        HttpClientDownloader downloader = new HttpClientDownloader();
        // 2、给下载器设置代理服务器信息
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("49.71.140.4",4257)));


        Spider.create(new ComplexProcessor())
                .addUrl("https://maoyan.com/query?kw=%E7%8E%A9%E5%85%B7%E6%80%BB%E5%8A%A8%E5%91%98")
//                .addUrl("https://www.douban.com/link2/?url=https%3A%2F%2Fmovie.douban.com%2Fsubject%2F6850547%2F&amp;query=%E7%8E%A9%E5%85%B7%E6%80%BB%E5%8A%A8%E5%91%98&amp;cat_id=1002&amp;type=search&amp;pos=1")
//                .addUrl("https://movie.douban.com/subject/6850547/")
//                .addUrl("https://www.douban.com/
//                .addUrl("https://www.douban.com/search?q=玩具总动员")
                // 设置下载器
                .setDownloader(downloader)
                .run();
    }
}
