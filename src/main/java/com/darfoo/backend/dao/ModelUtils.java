package com.darfoo.backend.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModelUtils {

	public static String dateFormat(Long timeInMillis,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(timeInMillis));
	}
}
