package com.july.leafClient.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by haoyifen on 2017/5/22 14:40.
 */
public class RESTUtil {
    private static Logger logger = LoggerFactory.getLogger(RESTUtil.class);
    private static CloseableHttpClient client;
    private static ObjectMapper objectMapper = ObjectMappers.myObjectMapper();

    static {
        RequestConfig config = RequestConfig.custom()
                //从连接池中获取连接的时间
                .setConnectionRequestTimeout(5 * 1000)
                //建立连接的时间
                .setConnectTimeout(5 * 1000)
                //读取数据的时间
                .setSocketTimeout(15 * 1000).build();
        client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build();
    }


    public static <T> T post(String url, Map<String, String> params, TypeReference<T> typeReference) {
        List<BasicNameValuePair> pairs = params.entrySet().stream().map(it ->
                new BasicNameValuePair(it.getKey(), it.getValue())
        ).collect(Collectors.toList());
        HttpEntity entity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        String resString = "";
        try {
            HttpResponse response = client.execute(httpPost);
            resString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.warn("get result wrong for url: {}, reason: {}", url, e);
        }
        T result = null;
        try {
            result = objectMapper.readerFor(typeReference).readValue(resString);
        } catch (IOException e) {
            logger.warn("translate res: {} to type: {} error", resString, typeReference.getType());
        }
        return result;
    }
}
