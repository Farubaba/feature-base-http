package com.farubaba.http.impl;

import java.util.List;

import com.farubaba.root.interf.DefaultDataCallback;
import com.farubaba.root.interf.impl.ErrorResult;

/**
 * 
 * @author violet
 *
 * @param <RESULT> 返回类型是一个泛型对象可以支持多种数据结构：<br>
 * 例如：Object，List<T>等
 */
public abstract class RequestCallback<M> extends DefaultDataCallback<M,ErrorResult>{
	@Override
	public void onSuccess(List<M> result) {
		
	}
}
