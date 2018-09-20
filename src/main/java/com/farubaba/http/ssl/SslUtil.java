package com.farubaba.http.ssl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SslUtil{
	public static final String TLS = "TLS";
	public static final String X509 = "X.509";
	public static String certificateEntryName = "farubaba-certificate-alias";
	public static String certificateFileName = "farubaba.cer";
	public static List<String> trustHostNames = new ArrayList<>();
	
	static {
		trustHostNames.add("localhost");
		trustHostNames.add("127.0.0.1");
		trustHostNames.add("www.farubaba.com");
	}
	
	public static void setCertificateEntryName(String certificateEntryName) {
		SslUtil.certificateEntryName = certificateEntryName;
	}
	
	public static void setCertificateFileName(String certificateFileName) {
		SslUtil.certificateFileName = certificateFileName;
	}
	
	public static void setTrustHostNames(List<String> trustHostNames) {
		SslUtil.trustHostNames = trustHostNames;
	}
	
	public static SSLSocketFactory getSSLSocketFactory() throws NoSuchAlgorithmException,
		KeyStoreException, CertificateException, IOException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(TLS);
        sslContext.init(null, getTrustManagers(), new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		return sslSocketFactory;
	}
	
	public static X509TrustManager getX509TrustManager() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore keystore = getAppKeystore();
		trustManagerFactory.init(keystore);
	 
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
          throw new IllegalStateException("Unexpected default trust managers:"
              + Arrays.toString(trustManagers));
        }
        X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
	    return x509TrustManager;
	}
	
	public static TrustManager[] getTrustManagers() throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore keystore = getAppKeystore();
		trustManagerFactory.init(keystore);
	 
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
		return trustManagers;
	}
	
	public static KeyStore getKeystoreFromCer(String certificateFileName, String certificateType) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		InputStream certificate = new BufferedInputStream(new FileInputStream(certificateFileName));
	  	return getKeystoreWithX509Certificate(certificate);
	}
	
	public static KeyStore getKeystoreWithX509Certificate(InputStream certificateInputStream) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
	  	KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	    keyStore.load(null,null);
	    keyStore.setCertificateEntry(certificateEntryName, certificateFactory.generateCertificate(certificateInputStream));
		return keyStore;
	}
	
	public static KeyStore getKeystoreWithX509Certificate(String certificateFileName) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		return getKeystoreFromCer(certificateFileName, X509);
	}
	
	public static KeyStore getAppKeystore() throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		return getKeystoreFromCer(certificateFileName, X509);
	}
	
	/**
	 * 发生异常才会回调verify方法
	 * @return
	 */
	public static HostnameVerifier getHostNameVerifier() {
		return new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return isTrustServer(hostname, session);
			}
			
		};
	}
	
	public static boolean isTrustServer(String hostname, SSLSession session) {
		String peerHost = session.getPeerHost();//服务器返回的域名
		System.out.println("peerHost = "+ peerHost + " hostname = "+ hostname);
//		//建立一个白名单和黑名单
		for(String trustHostName : trustHostNames) {
			if(trustHostName.equals(peerHost)) {
				return true;
			}
		}
		return false;
//		//FIXME 
//		peerHost = hostname = "www.farubaba.com";
//        try {
//            X509Certificate[] peerCertificates = (X509Certificate[]) session.getPeerCertificates();
//            for (X509Certificate c : peerCertificates) {
//                X500Principal subjectX500Principal = c.getSubjectX500Principal();
//                String name = subjectX500Principal.getName();
//                String[] split = name.split(",");
//                for (String s : split) {
//                    if (s.startsWith("CN")) {
//                        if (s.contains(hostname) && s.contains(peerHost)) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        } catch (SSLPeerUnverifiedException e) {
//            e.printStackTrace();
//        }
//        return false;
	}
	
	public static void sslBuildFlowTemplateCode() {
//		 try{
//			    InputStream certificate = new BufferedInputStream(new FileInputStream("farubaba.cer"));
//			  	CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//			    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			    keyStore.load(null,null);
//			    keyStore.setCertificateEntry("ca", certificateFactory.generateCertificate(certificate));
//			        
//		        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//		        trustManagerFactory.init(keyStore);
//		        
//		        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//		        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//		          throw new IllegalStateException("Unexpected default trust managers:"
//		              + Arrays.toString(trustManagers));
//		        }
//		        X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
//		        
//		        SSLContext sslContext = SSLContext.getInstance("TLS");
//		        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
//		        SSLSocketFactory ssSocketFactory = sslContext.getSocketFactory();
//		        
//		        okHttpClient = builder.sslSocketFactory(ssSocketFactory, x509TrustManager)
//	        		.hostnameVerifier(new HostnameVerifier() {
//						@Override
//						public boolean verify(String hostname, SSLSession session) {
//							return "localhost".equals(hostname) || "127.0.0.1".equals(hostname);
//						}
//					})
//	        		//设置代理，让Charles拦截请求 , 在https访问时，会出现400错误，暂时没有解决，所以不设置Proxy
////					.proxy(new Proxy(Proxy.Type.HTTP, 
////							new InetSocketAddress(AppConfig.getInstance().getProxyHost(), 
////									AppConfig.getInstance().getProxyPort())))
//	        		.build();
//			    } catch (Exception e){
//			    	e.printStackTrace();
//			    }
	}
	
}
