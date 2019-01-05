package test;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpsPostUtil {
	
	public final static void main(String[] args) throws Exception {
		
    	String body = "";
    	
    	//�����ƹ���֤�ķ�ʽ����https����  
        SSLContext sslcontext = createIgnoreVerifySSL();  
        //����Э��http��https��Ӧ�Ĵ���socket���ӹ����Ķ���  
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
            .register("http", PlainConnectionSocketFactory.INSTANCE)  
            .register("https", new SSLConnectionSocketFactory(sslcontext))  
            .build();  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
        HttpClients.custom().setConnectionManager(connManager); 
    	
        //�����Զ����httpclient����  
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();  
        //CloseableHttpClient client = HttpClients.createDefault();
        
        try{
	        //����post��ʽ�������  
	        HttpPost httpPost = new HttpPost("https://api.douban.com/v2/book/1220562"); 
	        /*Map<String, String> map = new HashMap<String, String>();  
	      
	        map.put("city", "�Ϻ�");  
	        map.put("charset", "utf-8");
	        
	      //װ�����  
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	        if(map!=null){  
	            for (Entry<String, String> entry : map.entrySet()) {  
	                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	            }  
	        } 
	        System.out.println("���������"+nvps.toString());  
	        
	        //���ò��������������  
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));*/
	      
	        //ָ������ͷContent-type��User-Agent
	        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
	        
	        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
	     
	        
	        //ִ��������������õ������ͬ��������  
	        CloseableHttpResponse response = client.execute(httpPost);  
	        
	        //��ȡ���ʵ��  
	        HttpEntity entity = response.getEntity(); 
	        if (entity != null) {  
	            //��ָ������ת�����ʵ��ΪString����  
	            body = EntityUtils.toString(entity, "UTF-8");  
	        }  
	        
	        EntityUtils.consume(entity);  
	        //�ͷ�����  
	        response.close(); 
	        System.out.println("body:" + body);
        }finally{
        	client.close();
        }
	}
        
        
	/** 
     * �ƹ���֤ 
     *   
     * @return 
     * @throws NoSuchAlgorithmException  
     * @throws KeyManagementException  
     */  
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
        SSLContext sc = SSLContext.getInstance("SSLv3");  
      
        // ʵ��һ��X509TrustManager�ӿڣ������ƹ���֤�������޸�����ķ���  
        X509TrustManager trustManager = new X509TrustManager() {  
            @Override  
            public void checkClientTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            @Override  
            public void checkServerTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            @Override  
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                return null;  
            }  
        };  
      
        sc.init(null, new TrustManager[] { trustManager }, null);  
        return sc;  
    }
}
