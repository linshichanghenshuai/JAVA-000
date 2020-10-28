package com.lsc.geek.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GeekHttpClient {
    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://time.geekbang.org/");
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = client.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200){
                //得到网页源码
                System.out.println(EntityUtils.toString(httpResponse.getEntity(),"UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
