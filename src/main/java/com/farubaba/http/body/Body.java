package com.farubaba.http.body;

public abstract class Body{
	
	private String mimeType;

	public String getMimeType() {
		return mimeType;
	}

	public Body setMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}

}
