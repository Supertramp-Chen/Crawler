package com.github.Crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://sina.cn");
            try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                System.out.println(EntityUtils.toString(entity1));
            } catch (IOException e) {
                e.printStackTrace();
            }

        /*HttpPost httpPost = new HttpPost("http://httpbin.org/post");
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
        }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
