package com.ck.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.ck.it
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 20:14
 */
@SpringBootTest
public class HttpClientTest {
	/*
	 *	测试通过httpclient发送get方式的请求
	 */
	@Test
	public void testGet() throws IOException {
		/// 创建httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		/// 创建请求对象
		HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");

		/// 发送请求接收响应结果
		CloseableHttpResponse response = httpClient.execute(httpGet);

		/// 获取服务端返回的状态码
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("服务端响应的状态码：" + statusCode);

		HttpEntity entity = response.getEntity();
		String string = EntityUtils.toString(entity);
		System.out.println("服务端返回的数据为： " + string);

		/// 关闭资源
		response.close();
		httpClient.close();
	}

	/*
	 *	测试通过httpclient发送get方式的请求
	 */
	@Test
	public void testPost() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
		httpPost.setHeader("Accept","application/json");
		httpPost.setHeader("Content-Type","application/json");

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String,String> map = new HashMap<>();
		map.put("username","admin");
		map.put("password","123456");

		String json = objectMapper.writeValueAsString(map);

		StringEntity stringEntity = new StringEntity(json);
		stringEntity.setContentEncoding("utf-8");
		stringEntity.setContentType("applicatoin/json");


		httpPost.setEntity(stringEntity);

		CloseableHttpResponse response = httpClient.execute(httpPost);

		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("响应状态码为： " + statusCode);

		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, "UTF-8");
		System.out.println("响应数据为： " + result);

		response.close();
		httpClient.close();
	}
}
