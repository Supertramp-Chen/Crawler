package com.github.Crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        //未处理的池
        List<String> listPool = new ArrayList<>();
        //处理的池
        Set<String> processedPool = new HashSet<>();
        //刚开始池子里的连接
        listPool.add("https://www.sina.com.cn/");
        while (true) {
            //判断池子是否未空
            if (listPool.isEmpty()) {
                break;
            }

            //每次从池子中拿个链接并且删除
            //arraylist从底部删除比较有效率，从前面删后面的元素需要挪位置 因为有空格
//            String link = listPool.remove(listPool.size()-1);
            String link = listPool.remove(0);

            //判断该链接是否已经处理过，即processedpool中是含有该链接
            if (processedPool.contains(link)) {
                continue;
            }

            //是不是我们想要的链接，contains实际上不准确
            if (isInterestingLink(link)) {//只处理新浪站内的链接
                //拿到其数据
                CloseableHttpClient httpclient = HttpClients.createDefault();
                URL link2 = new URL(link);
                HttpGet httpGet = new HttpGet(String.valueOf(link2));
                httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");

                try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                    System.out.println(response1.getStatusLine());

                    Document doc = parseHtml(response1);

                    doc.select("a").stream().map(aTag -> aTag.attr("href")).forEach(listPool::add);

                    storeIntoDatabaseIfISNewsPage(link2, doc);
                }
                processedPool.add(String.valueOf(link2));
            }
        }
    }

    private static Document parseHtml(CloseableHttpResponse response1) throws IOException {
        HttpEntity entity1 = response1.getEntity();

        //加入UTF-8后不在出现中文乱码问题
        String html = EntityUtils.toString(entity1,"UTF-8");

        //Jsoup解析html，游览器发起请求后会得到html字符串的响应 游览器解析它
        Document doc = Jsoup.parse(html);
        return doc;
    }

    private static void storeIntoDatabaseIfISNewsPage(URL link2, Document doc) {
        //判断是否是我们想要的，是则处理它
        //是否是新闻页面
        //对Element不熟悉 换成A<E>,面向对象子类可转换为父类
        ArrayList<Element> titleTags = doc.select("h1.main-title");
        if (!titleTags.isEmpty()) {
            for (Element titleTag:titleTags) {
                System.out.println("成功找到含标题的页面" + "\n" + "title 为 " + titleTag.text());
                System.out.println("当前链接为 " + link2);
            }
        }
    }

    private static boolean isInterestingLink(String link) {
        return isNewsPage(link) && link.contains("http") && !link.contains("license") && !link.contains("intro") && !link.contains("login");
    }

    private static boolean isNewsPage(String link) {
        return link.contains("sina.com.cn");
    }
}









/*

public class Main {
    public static void main(String[] args) throws IOException {
        //未处理的池
        List<String> listPool = new ArrayList<>();
        //处理的池
        Set<String> processedPool = new HashSet<>();
        //刚开始池子里的连接
        listPool.add("https://www.sina.com.cn/");
        while (true) {
            //判断池子是否未空
            if (listPool.isEmpty()) {
                break;
            }

            //每次从池子中拿个链接并且删除
            //arraylist从底部删除比较有效率，从前面删后面的元素需要挪位置 因为有空格
//            String link = listPool.remove(listPool.size()-1);
            String link = listPool.remove(0);

            //判断该链接是否已经处理过，即processedpool中是含有该链接
            if (processedPool.contains(link)) {
                continue;
            }

            //是不是我们想要的链接，contains实际上不准确
            if (link.contains("sina.com.cn") && link.contains("http") && !link.contains("license") && !link.contains("intro") && !link.contains("login")) {//只处理新浪站内的链接
                //拿到其数据
                CloseableHttpClient httpclient = HttpClients.createDefault();
                URL link2 = new URL(link);
                HttpGet httpGet = new HttpGet(String.valueOf(link2));
                httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
                try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                    System.out.println(response1.getStatusLine());
                    HttpEntity entity1 = response1.getEntity();

                    //加入UTF-8后不在出现中文乱码问题
                    String html = EntityUtils.toString(entity1,"UTF-8");

                    //Jsoup解析html，游览器发起请求后会得到html字符串的响应 游览器解析它
                    Document doc = Jsoup.parse(html);

                    //把链接丢入链接池
                    ArrayList<Element> links = doc.select("a");

                    //把链接放到listPool中
                    for (Element aTag : links) {
                        listPool.add(aTag.attr("href"));
                    }

                    //判断是否是我们想要的，是则处理它
                    //是否是新闻页面
                    //对Element不熟悉 换成A<E>,面向对象子类可转换为父类
                    ArrayList<Element> titleTags = doc.select("h1.main-title");
                    if (!titleTags.isEmpty()) {
                        for (Element titleTag:titleTags) {
                            System.out.println("成功找到含标题的页面" + "\n" + "title 为 " + titleTag.text());
                        }
                    }
                    System.out.println("当前的链接为 " + link2);
                }
                processedPool.add(String.valueOf(link2));
            }
        }
    }
}
*/



/*public static void main(String[] args) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
        HttpGet httpGet = new HttpGet("https://news.sina.com.cn/c/2022-07-31/doc-imizirav6164436.shtml");
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            System.out.println(EntityUtils.toString(entity1,"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

*//*        HttpPost httpPost = new HttpPost("http://httpbin.org/post");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
            System.out.println(response2.getCode() + " " + response2.getReasonPhrase());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }*//*
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}*/
