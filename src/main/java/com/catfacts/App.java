package com.catfacts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class App {

    private static final String URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    private static final ObjectMapper mapper = new ObjectMapper();

    public void run() {

        List<Post> data = null;

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        ) {

            HttpGet request = new HttpGet(URI);
            CloseableHttpResponse resp = httpClient.execute(request);


            data = mapper.readValue(
                    resp.getEntity().getContent(),
                    new TypeReference<List<Post>>() {
                    }
            );

            List<Post> res = data.stream().filter(x -> x.getUpvotes() != null && x.getUpvotes() > 0).toList();


            res.forEach(System.out::println);

        } catch (IOException ex) {
            System.err.println(ex.toString());
        }

    }
}
