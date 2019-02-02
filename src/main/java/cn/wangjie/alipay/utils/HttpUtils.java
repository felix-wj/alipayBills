package cn.wangjie.alipay.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * UttpUtils
 * @author lin
 * 2016-10-15
 */
public class HttpUtils {
	
	private static CloseableHttpClient httpClient = HttpClients.createDefault();
	private static HttpContext httpContext = new BasicHttpContext();
	private static HttpClientContext context = HttpClientContext.adapt(httpContext);

	private HttpUtils() {

	}

	/**
	 * 发送GET命令
	 */
	public static String sendGet(String url) {
		CloseableHttpResponse response = null;
		String content = null;
		try {
			HttpGet get = new HttpGet(url);
			
			//设置httpGet的头部参数信息   
	        get.setHeader("Accept", "text/html, application/xhtml+xml, */*");  
	        get.setHeader("Accept-Encoding", "gzip, deflate");  
	        get.setHeader("Accept-Language", "en-US");  
	        get.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)");
	        
			response = httpClient.execute(get, context);
			
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			
			EntityUtils.consume(entity);//关闭
			
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return content;
	}
	
	/**
	 * 发送GET命令,并设置Cookies.
	 */
	public static String sendGet(String url,String host,Set<Cookie> cookies) {
		
		/**
		 * 设置Cookie，这里把Selenium登录成功的Cookie放进HttpClient的context
		 */
        CookieStore cookieStore = new BasicCookieStore(); 
        for(Cookie cookie : cookies){
        	BasicClientCookie bc = new BasicClientCookie(cookie.getName(),cookie.getValue());
        	bc.setPath("/");
        	bc.setDomain(host);
        	bc.setVersion(0);
        	cookieStore.addCookie(bc);
        }
//        httpClient = HttpClients.custom()
//                .setDefaultCookieStore(cookieStore) 
//                .build();
        //httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore); 
        context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		
		CloseableHttpResponse response = null;
		String content = null;
		try {
			HttpGet get = new HttpGet(url);
			
			//设置httpGet的头部参数信息   
	        get.setHeader("Accept", "text/html, application/xhtml+xml, */*");  
	        get.setHeader("Accept-Encoding", "gzip, deflate");  
	        get.setHeader("Accept-Language", "en-US");  
	        get.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)");  
	        
			response = httpClient.execute(get, context);
			
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			
			EntityUtils.consume(entity);//关闭
			
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return content;
	}

	/**
	 * 发送POST命令
	 */
	public static String sendPost(String url, List<NameValuePair> nvps) {
		CloseableHttpResponse response = null;
		String content = null;
		try {
			//　HttpClient中的post请求包装类
			HttpPost post = new HttpPost(url);
			// nvps是包装请求参数的list
			if (nvps != null) {
				post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			}
			
			//設置httpGet的头部參數信息   
	        post.setHeader("Accept", "text/html, application/xhtml+xml, */*");  
	        post.setHeader("Accept-Encoding", "gzip, deflate");  
	        post.setHeader("Accept-Language", "en-US");  
	        post.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)");  
			
			// 执行请求用execute方法，content用来帮我们附带上额外信息
			response = httpClient.execute(post, context);
			// 得到相应实体、包括响应头以及相应内容
			HttpEntity entity = response.getEntity();
			// 得到response的内容
			content = EntityUtils.toString(entity);
			//　关闭输入流
			EntityUtils.consume(entity);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

}
