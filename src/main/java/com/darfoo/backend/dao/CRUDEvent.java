package com.darfoo.backend.dao;

/**
 * @author YY
 * **/
public class CRUDEvent {
	public final static int INSERT_SUCCESS = 0X01;  //插入成功
	public final static int INSERT_FAIL = 0X02;		//插入失败
	public final static int INSERT_REPEAT = 0X03;	//重复插入,插入失败
		
	public final static int QUERY_SUCCESS = 0X11;   //查询成功
	public final static int QUERY_FAIL = 0X12;		//查询失败

	public final static int UPDATE_SUCCESS = 0X21;	//更新成功
	public final static int UPDATE_FAIL = 0X22;		//更新失败
	public final static int UPDATE_VIDEO_NOTFOUND = 0X23; //VIDEO不存在
	public final static int UPDATE_VIDEOAUTHOR_NOTFOUND = 0X24;//作者不存在
	public final static int UPDATE_VIDEOIMAGE_NOTFOUND = 0X25; //图片不存在
	public final static int UPDATE_VIDEOTITLE = 0X25; //返回该参数表示title(video_key)需要更新
	
	public final static int DELETE_SUCCESS = 0X31;  //删除成功
	public final static int DELETE_FAIL = 0X32;		//删除失败
	public final static int DELETE_NOTFOUND = 0X33; //无法找到要删除的对象
	
	public final static int CRUD_EXCETION = 0X04; 	//捕获异常
	
	public static String getResponse(int CRUDEvent){
		String res = "";
		switch(CRUDEvent){
			case INSERT_SUCCESS:res = "INSERT_SUCCESS";break;
			case INSERT_FAIL:	res = "INSERT_FAIL";break;
			case INSERT_REPEAT:	res = "INSERT_REPEAT";break;
			case QUERY_SUCCESS:	res = "QUERY_SUCCESS";break;
			case QUERY_FAIL:	res = "QUERY_FAIL";break;
			case UPDATE_SUCCESS:res = "UPDATE_SUCCESS";break;
			case UPDATE_FAIL:	res = "UPDATE_FAIL";break;
			case UPDATE_VIDEO_NOTFOUND:res = "UPDATE_VIDEO_NOTFOUND";break;
			case DELETE_SUCCESS:res = "DELETE_SUCCESS";break;
			case DELETE_FAIL:	res = "DELETE_FAIL";break;
			case DELETE_NOTFOUND:res = "DELETE_NOTFOUND";break;
			case CRUD_EXCETION:	res = "CRUD_EXCETION";break;
			default:break;
		}
		return res;
	}
}
