package com.safelogic.autodex.web.validator;

public enum ValidationEnum {
	
	SAVE(1),
	UPDATE(2),
	GET(3),
	DELETE(4);
	
	private int val;
	
	private ValidationEnum(int val){
		this.val = val;
	}
	
	public int getVal()
	{
		return this.val;
	}
}
