package com.safelogic.autodex.web;

public class NaasException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6477663198173267740L;

	public NaasException() {
		super();
	}
	
	public NaasException(Throwable t) {
		super(t);
	}
	
	public NaasException(String meString){
		super(meString);
	}
}
