package org.nju.se.flink.job.update.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RequestHelper {

    private static final CloseableHttpClient client = HttpClientBuilder.create().build();

    private static final CountDownLatch latch = new CountDownLatch(1);

    private static String token;

    public static void setToken(String token) {
        RequestHelper.token = token;
    }

    public static String get(String url) {
        try {
            latch.await(750, TimeUnit.MILLISECONDS);
            HttpGet httpGet = new HttpGet(url);
            System.out.println(url);
            httpGet.setHeader("Authorization", String.format("Bearer %s", token));
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity());
                response.getEntity().getContent().close();
                return content;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
