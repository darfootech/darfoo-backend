package com.darfoo.backend.dao;

public class CRUDEvent {
	public final static int INSERT_SUCCESS = 0X01;
	public final static int INSERT_FAIL = 0X02;
	public final static int INSERT_REPEAT = 0X03;
		
	public final static int QUERY_SUCCESS = 0X11;
	public final static int QUERY_FAIL = 0X12;

	public final static int UPDATE_SUCCESS = 0X21;
	public final static int UPDATE_FAIL = 0X22;
	
	public final static int CRUD_EXCETION = 0X04;
	
	public static String getResponse(int CRUDEvent){
		String res = "";
		switch(CRUDEvent){
			case INSERT_SUCCESS:res = "INSERT_SUCCESS";break;
			case INSERT_FAIL:	res = "INSERT_FAIL";break;
			case INSERT_REPEAT:	res = "INSERT_REPEAT";break;
			case QUERY_SUCCESS:	res = "QUERY_SUCCESS";break;
			case QUERY_FAIL:	res = "INSERT_SUCCESS";break;
			case UPDATE_SUCCESS:res = "UPDATE_SUCCESS";break;
			case UPDATE_FAIL:	res = "UPDATE_FAIL";break;
			case CRUD_EXCETION:	res = "CRUD_EXCETION";break;
			default:break;
		}
		return res;
	}
}
