package com.gameserver.common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.LogManager;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.common.constants.Loggers;
import com.google.api.client.repackaged.com.google.common.base.Joiner;

public class HttpsUtil {

	public static Logger log = Loggers.activityLogger;
	
	private static PoolingHttpClientConnectionManager connMgr;  
    private static RequestConfig requestConfig;  
    private static final int MAX_TIMEOUT = 7000;  
  
    static {  
        // 设置连接池  
        connMgr = new PoolingHttpClientConnectionManager();  
        // 设置连接池大小  
        connMgr.setMaxTotal(100);  
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());  
  
        RequestConfig.Builder configBuilder = RequestConfig.custom();  
        // 设置连接超时  
        configBuilder.setConnectTimeout(MAX_TIMEOUT);  
        // 设置读取超时  
        configBuilder.setSocketTimeout(MAX_TIMEOUT);  
        // 设置从连接池获取连接实例的超时  
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);  
       
        // 在提交请求之前 测试连接是否可用  
        //configBuilder.setStaleConnectionCheckEnabled(true);  
        requestConfig = configBuilder.build();  
    }
    
    
    
    /** 
     * 发送 GET 请求（HTTP），K-V形式 
     * 
     * @param url 
     * @param params 
     * @return 
     */  
    public static String doGet(String url, Map<String, String> params,boolean isHttps) {  
        HttpResponse response=null; 
        
        String str = Joiner.on("&").skipNulls().join(params.entrySet());
        
        url = Joiner.on("?").skipNulls().join(url,str);
        
        String result = null;  
        
        CloseableHttpClient httpclient =null;  
        if(isHttps){  
            httpclient=createSSLClientDefault();  
        }else {  
            httpclient=HttpClients.createDefault();  
        }  
        try {  
            HttpGet httpGet = new HttpGet(url);  
            response = httpclient.execute(httpGet);  
            int statusCode = response.getStatusLine().getStatusCode();  
            if(statusCode != HttpStatus.SC_OK){
    			return null;
    		}
            HttpEntity entity = response.getEntity();  
            if (entity != null) {   
                result=EntityUtils.toString(entity, "UTF-8");  
                log.info("[DOGET]----------------------------------[doGET访问:result===="+result+"]");
            }  
        } catch (IOException e) {  
            log.error("", e);
        }finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                	log.error("", e);
                }  
            }  
        }  
        return result;  
    }  
    public static String doGetWithHeader(String url, Map<String, String> params,Map<String, String> headers,boolean isHttps) {  
    	HttpResponse response=null; 
    	
    	String result = null;  
    	
    	CloseableHttpClient httpclient =null;  
    	if(isHttps){  
    		httpclient=createSSLClientDefault();  
    	}else {  
    		httpclient=HttpClients.createDefault();  
    	}  
    	try {  
    		HttpGet httpGet = new HttpGet(url);  
    		if(headers != null && headers.size()>0){
    			for(Map.Entry<String, String> entry:headers.entrySet()){
    				String key = entry.getKey();
    				String value = entry.getValue();
    				httpGet.setHeader(key,value);
    			}
    		}
    		response = httpclient.execute(httpGet);  
    		int statusCode = response.getStatusLine().getStatusCode();  
    		log.info("[DOGET]----------------------------------[doGET访问:response:"+response+"]");
    		log.info("[DOGET]----------------------------------[doGET访问:url:"+url+"]"+"[params:"+params+"]");
    		log.info("[DOGET]----------------------------------[doGET访问:statusCode"+statusCode+"]");
    		if(statusCode != HttpStatus.SC_OK){
    			return null;
    		}
    		HttpEntity entity = response.getEntity();  
    		if (entity != null) {   
    			result=EntityUtils.toString(entity, "UTF-8");  
    		}  
    	} catch (IOException e) {  
    		log.error("", e);
    	}finally {  
    		if (response != null) {  
    			try {  
    				EntityUtils.consume(response.getEntity());  
    			} catch (IOException e) {  
    				log.error("", e);
    			}  
    		}  
    	}  
    	return result;  
    }  
    /** 
     * 发送 GET 请求（HTTP），K-V形式 
     * 
     * @param url 
     * @param params 
     * @return 
     */  
    public static String doPost(String url, Map<String, String> params,boolean isHttps) {  
    	HttpResponse response=null; 
    	
    	String result = null;  
    	
    	CloseableHttpClient httpclient =null;  
    	if(isHttps){  
    		httpclient=createSSLClientDefault();  
    	}else {  
    		httpclient=HttpClients.createDefault();  
    	}  
    	try {
    		
    	    List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
    	    for(Map.Entry<String,String> entry:params.entrySet()){
    	    	urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    	    }

    	    HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
    	    HttpPost httpPost = new HttpPost(url);  
    	    httpPost.setEntity(postParams);
    		
    		response = httpclient.execute(httpPost);  
    		int statusCode = response.getStatusLine().getStatusCode();  
    		log.info("[DOPOST]----------------------------------[DOPOST访问:response:"+response+"]");
    		log.info("[DOPOST]----------------------------------[DOPOST访问:url:"+url+"]"+"[params:"+params+"]");
    		log.info("[DOPOST]----------------------------------[DOPOST访问:statusCode"+statusCode+"]");
    		if(statusCode != HttpStatus.SC_OK){
    			return null;
    		}
    		HttpEntity entity = response.getEntity();  
    		if (entity != null) {   
    			result=EntityUtils.toString(entity, "UTF-8");  
    		}  
    	} catch (IOException e) {  
    		log.error("", e);
    	}finally {  
    		if (response != null) {  
    			try {  
    				EntityUtils.consume(response.getEntity());  
    			} catch (IOException e) {  
    				log.error("", e);
    			}  
    		}  
    	}  
    	return result;  
    }  
    
    public static String doPostSSL(String url,Map<String,String> params){
    	
    	CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
    	
    	HttpPost httpPost = new HttpPost(url);
    	
    	
    	CloseableHttpResponse response = null;
    	
    	String result = null;
    	try{
    		httpPost.setConfig(requestConfig);
    		
    		List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
    		
    		for(Entry<String,String> en : params.entrySet()){
    			NameValuePair pair = new BasicNameValuePair(en.getKey(), en.getValue());
    			pairList.add(pair);
    		}
    		
    		httpPost.setEntity(new UrlEncodedFormEntity(pairList,Charset.forName("utf-8")));
    		
    		response = httpClient.execute(httpPost);
    		
    		int statusCode = response.getStatusLine().getStatusCode();
    		if(statusCode != HttpStatus.SC_OK){
    			log.info(JSON.toJSONString(response));
    			return null;
    		}
    		
    		HttpEntity httpEntity = response.getEntity();
    		
    		if(httpEntity == null){
    			return null;
    		}
    		
    		result = EntityUtils.toString(httpEntity, "UTF-8");
    		
    	}catch(Exception e){
    		log.error("",e);
    	}finally {
			if(response != null){
				try{
					EntityUtils.consume(response.getEntity());
				}catch(Exception e){
					
				}
			}
		}
    	return result;
    }
   public static void main(String[] args) {
	String dd = "https://sandbox.itunes.apple.com/verifyReceipt";
	String str = "{\"receipt_data\":\"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\"}";
	log.info("======="+str);
	String tt = doPostSslJSON(dd,str);
	log.info(tt);
}
    
    public static String doPostSslJSON(String url,String json){
    	json=json.replace("receipt_data", "receipt-data");
    	log.info("333333"+json);
    	log.info(url);
    	CloseableHttpClient httpClient = null;
    	try{
    		httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	log.info("--------ssl:::"+httpClient);
    	HttpPost httpPost = new HttpPost(url);
    	
    	CloseableHttpResponse response = null;
    	
    	String result = null;
    	try{
    		httpPost.setConfig(requestConfig);
   		String str =  new String(json.getBytes("UTF-8"));
    		httpPost.setHeader("content-type", "application/json");
    		log.info("------------start----stringEntity");
    		StringEntity stringEntity = new StringEntity(str);
    		stringEntity.setContentEncoding("UTF-8");    
    		stringEntity.setContentType("application/json");  
    		
    		log.info("------------stringEntity"+stringEntity);
    		httpPost.setEntity(stringEntity);
    		
    		log.info("start 开始请求===statusCode::");
    		response = httpClient.execute(httpPost);
    		int statusCode = response.getStatusLine().getStatusCode();
    		log.info("response=======statusCode::"+statusCode);
    		if(statusCode != HttpStatus.SC_OK){
    			return null;
    		}
    		
    		HttpEntity httpEntity = response.getEntity();
    		log.info("response=============================httpEntity::"+httpEntity);
    		if(httpEntity == null){
    			return null;
    		}
    		
    		result = EntityUtils.toString(httpEntity, "UTF-8");
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally {
    		if(response != null){
    			try{
    				EntityUtils.consume(response.getEntity());
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		}
    	}
    	return result;
    }
    
    private static SSLConnectionSocketFactory createSSLConnSocketFactory(){
    	SSLConnectionSocketFactory sslsf = null;
    	try{
    		
    		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy(){
				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
    			
    		}).build();
    		
    		sslsf = new SSLConnectionSocketFactory(sslContext);
    		
    	}catch(Exception e){
    		
    	}
    	return sslsf;
    }
    
    private static CloseableHttpClient createSSLClientDefault(){  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {  
                //信任所有  
                public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException {  
                    return true;  
                }  
            }).build();  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);  
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();  
        } catch (Exception e) {  
        	log.error("", e);
        } 
        return  HttpClients.createDefault();  
    }  
    
  /* public static void main(String[] args) {
	String url = "https://accounts.google.com/o/oauth2/token";
	
	Map<String,String> paraMap = new HashMap<String,String>();
	paraMap.put("FacserviceID", "nfeiGC");
	String result = doGet(url,paraMap,true);//doPostSSL(url,paraMap);
	
	
	
	log.info.println(result);
	   String url = "https://www.googleapis.com/androidpublisher/v2/applications";
	   Map<String,String> param = new HashMap<String,String>();
	   param.put("a", "111");
	   param.put("b", "222");
	   param.put("c", "333");
	   String str = Joiner.on("&").skipNulls().join(param.entrySet());
	
	   url = Joiner.on("?").skipNulls().join(url,str);
	   
	   log.info(url);
	   
	   String strs = "%s/%s/purchases/products/%s/tokens/%s?access_token=%s";
	   CharMatcher.noneOf(strs);
   }*/
   
 
    
}
