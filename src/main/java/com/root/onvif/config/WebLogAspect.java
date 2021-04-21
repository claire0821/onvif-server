package com.root.onvif.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import com.root.onvif.model.WebLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;


@Aspect //注解 使之成为切面类
@Component //注解 把切面类加入到IOC容器中

//@Aspect：用于定义切面
//@Before：通知方法会在目标方法调用之前执行
//@After：通知方法会在目标方法返回或抛出异常后执行
//@AfterReturning：通知方法会在目标方法返回后执行
//@AfterThrowing：通知方法会在目标方法抛出异常后执行
//@Around：通知方法会将目标方法封装起来
//@Pointcut：定义切点表达式
public class WebLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);


    @Pointcut("execution(public * com.root.onvif.controller.*.*(..))")
    public void webLog() {
    }

    // 在连接点执行之前执行的通知
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    /**
     * 只有正常返回才会执行此方法
     * 如果程序执行失败，则不执行此方法
     */
    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {
//        System.out.println("doAfterReturning");
    }

    /**
     * 当接口报错时执行此方法
     */
    @AfterThrowing(pointcut = "webLog()")
    public void doAfterThrowing(JoinPoint joinPoint) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        log.info("接口访问失败，URI:[{}], 耗费时间:[{}] ms", request.getRequestURI(), System.currentTimeMillis() - startTime.get());
    }


    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Date now = new Date();
        long startTime = now.getTime();// System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        WebLog webLog = new WebLog();
        Object result = joinPoint.proceed();//调用 proceed() 方法才会真正的执行实际被代理的方法
        long endTime = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        String urlStr = request.getRequestURL().toString();


        webLog.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
        webLog.setIp(request.getRemoteAddr());
        webLog.setMethod(request.getMethod());

        webLog.setParameter(request.getParameterMap());
        webLog.setResult(result);
        webLog.setSpendTime((int) (endTime - startTime));
        webLog.setStartTime(now);
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(request.getRequestURL().toString());
        System.out.println(webLog.toString());

        //#endregion
        return result;
    }

}
