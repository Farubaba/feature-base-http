package com.farubaba.http.manager;

import com.farubaba.http.impl.HttpAdapter;
import com.farubaba.http.impl.RequestContext;
import com.farubaba.http.impl.RequestHandler;
import com.farubaba.root.interf.IModel;

/**
 * 单例HttpDataCenter，可以通过设置不同的HTTPClient来实现不同的http控制；
 * 例如:readTimeout，cache，fore request，header 等控制
 * @author violet
 *
 */
public class HttpManager implements HttpAdapter{
	
	private static HttpManager instance = new HttpManager();
	private HttpAdapter httpAdapter;
	private HttpManager(){
		
	}
	
	public static HttpManager getInstance(){
		return instance; 
	}

	public static HttpManager getInstance(HttpAdapter httpAdapter){
		if(httpAdapter != null){
			instance.setHttpAdapter(httpAdapter);
		}
		return instance;
	}
	public HttpAdapter getHttpAdapter() {
		//return httpAdapter == null ? setHttpAdapter(new OkHttpAdapter())  : httpAdapter;
		return httpAdapter;
	}

	public HttpManager setHttpAdapter(HttpAdapter httpAdapter) {
		this.httpAdapter = httpAdapter;
		return this;
	}

	@Override
	public <M extends IModel> RequestHandler sendDefaultRequest(
			RequestContext<M> requestContext) {
		return getHttpAdapter().sendDefaultRequest(requestContext);
	}

	@Override
	public <M extends IModel> RequestHandler sendTimeoutRequest(RequestContext<M> requestContext) {
		return getHttpAdapter().sendTimeoutRequest(requestContext);
	}
	
	@Override
	public <M extends IModel> RequestHandler sendAuthenticatorRequest(RequestContext<M> requestContext) {
		return getHttpAdapter().sendAuthenticatorRequest(requestContext);
	}

	@Override
	public <M extends IModel> RequestHandler sendInterceptorRequest(RequestContext<M> requestContext) {
		return getHttpAdapter().sendInterceptorRequest(requestContext);
	}

}
