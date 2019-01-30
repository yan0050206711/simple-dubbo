package com.tstd2.soa.remoting.http.client;

import com.google.common.base.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http客户端工具类。
 */
public class HttpClientUtils {
	
	private static final CloseableHttpClient HTTP_CLIENT;

	private static final int DEFAULT_TIMEOUT = 3000;

	private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
			.setSocketTimeout(DEFAULT_TIMEOUT)
			.setConnectTimeout(DEFAULT_TIMEOUT)
			.setConnectionRequestTimeout(DEFAULT_TIMEOUT)
			.build();

	static{

		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new org.apache.http.ssl.TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", sslsf).build();

			SocketConfig socketConfig = SocketConfig.custom()
					.setSoTimeout(10000)
					.setTcpNoDelay(true)
					.build();

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			connectionManager.setMaxTotal(500);
			connectionManager.setDefaultMaxPerRoute(500);
			connectionManager.setDefaultSocketConfig(socketConfig);
			HTTP_CLIENT = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().removeShutdownHook(new Thread(){
			@Override
			public void run() {
				try {
					HTTP_CLIENT.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	/**
	 * 通过get方式请求给定的Url，并以字符串形式返回内容。
	 * 
	 * @param url 目标url。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String get(String url){
		return get(url, null, null, null);
	}

	/**
	 * 通过get方式请求给定的Url，并以字符串形式返回内容。
	 *
	 * @param url 目标url。
	 * @param timeout 超时时间，单位毫秒。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String get(String url, int timeout){
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(timeout)
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.build();
		return get(url, null, null, config);
	}

	/**
	 * 通过get方式请求给定的Url，并以字符串形式返回内容。
	 * 
	 * @param url 目标url。
	 * @param charset 字符集。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String get(String url, Charset charset){
		return get(url, null, charset, null);
	}

	/**
	 * 通过get方式请求给定的Url，并以字符串形式返回内容。
	 *
	 * @param url 目标url。
	 * @param charset 字符集。
	 * @param timeout 超时时间，单位毫秒。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String get(String url, Charset charset, int timeout){
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(timeout)
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.build();
		return get(url, null, charset, config);
	}
	
	/**
	 * 通过get方式请求给定的Url，并以字符串形式返回内容。
	 * 
	 * @param url 目标url。
	 * @param headers 请求头信息。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String get(String url, Map<String, String> headers){
		return get(url, headers, null, null);
	}

	/**
	 * 通过get方式请求给定的Url，并以字符串形式返回内容。
	 * 
	 * @param url 目标url。
	 * @param headers 请求头信息。
	 * @param charset 字符集。
	 * @param config 请求配置。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String get(String url, Map<String, String> headers, Charset charset, RequestConfig config){
        HttpGet request = null;
		CloseableHttpResponse response = null;
		if(config == null){
			config = DEFAULT_REQUEST_CONFIG;
		}
		try {
			request = new HttpGet(url);
			request.setConfig(config);
			//设置http头信息。
			if(headers != null && !headers.isEmpty()){
				for(String key : headers.keySet()){
					request.addHeader(key, headers.get(key));
				}
			}
			//执行http请求。
			response = HTTP_CLIENT.execute(request);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			HttpEntity entity = response.getEntity();
			String content = "";
			if(entity != null){
				content = EntityUtils.toString(entity, charset);
			}
			//判断http状态码。
			if(HttpStatus.SC_OK == statusCode){
                return content;
			}else{
                throw new RuntimeException("通过get方式请求url["+url+"]发生异常!Http状态码为["+statusCode+"],响应内容为:" + content);
			}
		} catch (Exception e) {
			throw new RuntimeException("通过get方式请求url["+url+"]发生异常!", e);
		} finally{
			close(request, response);
		}
	}

	/**
	 * 通过post方式请求给定的Url，并以字符串形式返回内容。
	 *
	 * @param url 目标url。
	 * @param params 请求参数。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String post(String url, Map<String, String> params){
		return post(url, null, params, null, null);
	}

	/**
	 * 通过post方式请求给定的Url，并以字符串形式返回内容。
	 *
	 * @param url 目标url。
	 * @param params 请求参数。
	 * @param timeout 超时时间，单位毫秒。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String post(String url, Map<String, String> params, int timeout){
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(timeout)
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.build();
		return post(url, null, params, null, config);
	}
	
	/**
	 * 通过post方式请求给定的Url，并以字符串形式返回内容。
	 * 
	 * @param url 目标url。
	 * @param params 请求参数。
	 * @param charset 字符集。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String post(String url, Map<String, String> params, Charset charset){
		return post(url, null, params, charset, null);
	}

	/**
	 * 通过post方式请求给定的Url，并以字符串形式返回内容。
	 *
	 * @param url 目标url。
	 * @param params 请求参数。
	 * @param charset 字符集。
	 * @param timeout 超时时间，单位毫秒。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String post(String url, Map<String, String> params, Charset charset, int timeout){
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(timeout)
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.build();
		return post(url, null, params, charset, config);
	}
	
	/**
	 * 通过post方式请求给定的Url，并以字符串形式返回内容。
	 * 
	 * @param url 目标url。
	 * @param headers 请求头信息。
	 * @param params 请求参数。
	 * @param charset 字符集。
	 * @param config 请求配置。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String post(String url, Map<String, String> headers, Map<String, String> params,
							  Charset charset, RequestConfig config){
        HttpPost request = null;
		CloseableHttpResponse response = null;
		if(config == null){
			config = DEFAULT_REQUEST_CONFIG;
		}
		try {
			request = new HttpPost(url);
			request.setConfig(config);
			//设置http头信息。
			if(headers != null && !headers.isEmpty()){
				for(String key : headers.keySet()){
					request.addHeader(key, headers.get(key));
				}
			}
			//设置请求参数。
			if(params != null && !params.isEmpty()){
				List<NameValuePair> paramList = new ArrayList<>(params.size());
				for(String key : params.keySet()){
					paramList.add(new BasicNameValuePair(key, params.get(key)));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, charset);
				request.setEntity(entity);
			}
			//执行http请求。
			response = HTTP_CLIENT.execute(request);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			HttpEntity entity = response.getEntity();
			String content = "";
			if(entity != null){
				content = EntityUtils.toString(entity, charset);
			}
			//判断http状态码。
			if(HttpStatus.SC_OK == statusCode){
				return content;
			}else{
				throw new RuntimeException("通过post方式请求url["+url+"]发生异常!Http状态码为["+statusCode+"],请求参数为"+params + ",响应内容为:" + content);
			}
		} catch (Exception e) {
			throw new RuntimeException("通过post方式请求url["+url+"]发生异常!", e);
		} finally{
			close(request, response);
		}
	}

	public static String post(String url, String body){
		return post(url, null, body, Charsets.UTF_8, null);
	}

	public static String post(String url, String body, int timeout){
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(timeout)
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.build();
		return post(url, null, body, Charsets.UTF_8, config);
	}

	public static String post(String url, String body, Charset charset){
		return post(url, null, body, charset, null);
	}

    public static String post(String url, String body,
                              Charset charset, RequestConfig config){
        return post(url, null, body, charset, config);
    }

	/**
	 * 通过post方式请求给定的Url，并以字符串形式返回内容。
	 *
	 * @param url 目标url。
     * @param headers 请求头信息。
	 * @param body 请求头信息。
	 * @param charset 字符集。
	 * @param config 请求配置。
	 * @return
	 *      字符串形式的内容。
	 * @throws RuntimeException 如果目标url为空；
	 *                          或者发生网络异常；
	 *                          或者Http状态码不是200。
	 */
	public static String post(String url, Map<String, String> headers, String body,
							  Charset charset, RequestConfig config){
        HttpPost request = null;
		CloseableHttpResponse response = null;
		if(config == null){
			config = DEFAULT_REQUEST_CONFIG;
		}
		try {
			request = new HttpPost(url);
			request.setConfig(config);
            //设置http头信息。
            if(headers != null && !headers.isEmpty()){
                for(String key : headers.keySet()){
                    request.addHeader(key, headers.get(key));
                }
            }
			//设置请求参数。
			StringEntity entity = new StringEntity(body, charset);
			request.setEntity(entity);
			//执行http请求。
			response = HTTP_CLIENT.execute(request);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			HttpEntity responseEntity = response.getEntity();
			String content = "";
			if(responseEntity != null){
				content = EntityUtils.toString(responseEntity, charset);
			}
			//判断http状态码。
			if(HttpStatus.SC_OK == statusCode){
				return content;
			}else{
				throw new RuntimeException("通过post方式请求url["+url+"]发生异常!Http状态码为["+statusCode+"],请求参数为["+body+"]!");
			}
		} catch (Exception e) {
			throw new RuntimeException("通过post方式请求url["+url+"]发生异常!", e);
		} finally{
			close(request, response);
		}
	}

	/**
	 * 探测URL，判断URL是否可以正常访问。
	 *
	 * @param url 目标URL。
	 * @return
	 * 		返回Http响应状态码。
	 * @throws RuntimeException 如果发生任何异常。
	 */
	public static int ping(String url){
		HttpHead request = null;
		CloseableHttpResponse response = null;
		try {
			request = new HttpHead(url);
			request.setConfig(DEFAULT_REQUEST_CONFIG);
			//执行http请求。
			response = HTTP_CLIENT.execute(request);
			StatusLine statusLine = response.getStatusLine();
			return statusLine.getStatusCode();
		} catch (Exception e) {
			throw new RuntimeException("访问url["+url+"]发生异常!", e);
		} finally{
			close(request, response);
		}
	}

	private static void close(AbstractExecutionAwareRequest request, CloseableHttpResponse response){
		try {
			if(request != null){
				request.abort();
			}
			if(response != null){
				EntityUtils.consumeQuietly(response.getEntity());
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HttpClientUtils(){}

}
