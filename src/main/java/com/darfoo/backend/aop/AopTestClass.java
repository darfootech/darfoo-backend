package com.darfoo.backend.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AopTestClass {
	Logger logger = Logger.getLogger(AopTestClass.class);
	final String TAG = "";
	//下面这个居然不执行，好吧，可能是我记错了~这只是一个Pointcut而已，具体还是靠advisor
	@Pointcut("execution (* com.darfoo.backend.dao.VideoDao.*(..))")
	public void weaveTestInfo(){
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$weave Test Info$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	}
	
	@After("execution (* com.darfoo.backend.dao.VideoDao.*(..))")
	public void weaveTestInfoAfter(){
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$weave Test Info After execution$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	}
}
