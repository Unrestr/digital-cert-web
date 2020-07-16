package com.greatmap.digital.annotation;

import com.alibaba.dubbo.config.annotation.Reference;
import com.greatmap.digital.util.IpUtils;
import com.greatmap.digital.util.LoginUserUtil;
import com.greatmap.uums.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * 切点类
 * @author greatmap
 *
 */
@Aspect
@Component
public class SystemLogAspect {

	private String appKey;

	private Boolean devMode;

	private Boolean fileLog;

	private Boolean enable;

	@Reference(registry = "uums")
	private LogService logService;

    public SystemLogAspect() {
        enable = true;
        fileLog = false;
        devMode = false;
    }

    public Boolean getFileLog() {
        return fileLog;
    }

    public void setFileLog(Boolean fileLog) {
        this.fileLog = fileLog;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Boolean getDevMode() {
        return devMode;
    }

    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
	 * 本地异常日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

	 
	/**
	 * 自定义注解切点
	 */
	@Pointcut("@annotation(com.greatmap.digital.annotation.SystemLog)")
	public void systemLogAspect() {
	}

    /**
     * swagger Api切点
     */
    @Pointcut("@annotation(io.swagger.annotations.Api)")
    public void apiAspect() {
    }

    /**
     * swagger ApiOperation切点
     */
    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void apiOperationAspect() {
    }

	//web包的子包里面任何方法
//	@Pointcut("execution(public * com.greatmap.*.web.*.*(..))")
//	public void controllerAspect(){
//	}


	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * @param joinPoint 切点
	 * @throws Exception
	 */
    @Before("systemLogAspect() || apiOperationAspect()")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        if (!enable) {return;}
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取当前用户
        String name = LoginUserUtil.getLoginName();
        if (StringUtils.isNotBlank(name) || devMode) {
            String loginName = StringUtils.isNotBlank(name)?"developer":name;
            // 请求的IP
            String ip = IpUtils.getIpAddr(request);
            // 请求方式
            String type = request.getMethod();
            // 请求URI
            String uri = request.getRequestURI();
            // 方法描述
            String menu = getControllerMethodDescription(joinPoint);
            if(StringUtils.isBlank(menu)){
                return;
            }
            logService.save(loginName, menu, type, uri, ip, "DIGITAL_CERTIFICATES");

        }
    }

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * @param joinPoint  切点
	 * @return 方法描述
	 * @throws Exception
	 */
	private String getControllerMethodDescription(JoinPoint joinPoint) {
	    //获取对象描述
        Class<?> aClass = joinPoint.getTarget().getClass();
        String description = getClassLogDescription(aClass);
        //获取方法描述
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		Method targetMethod = methodSignature.getMethod();
        String description2 = getMehthodLogDescription(targetMethod);
        if(StringUtils.isBlank(description2)){
            return null;
        }
		return description + "-" +description2;
	}

    /**
     * 获取连接点所在的目标对象的@SystemLog描述或@Api描述
     * @param aClass 目标对象
     * @return 描述信息
     */
	private String getClassLogDescription(Class<?> aClass){
        String description = "";
        if (aClass.isAnnotationPresent(SystemLog.class)) {
            description = aClass.getAnnotation(SystemLog.class).description();
        }else if(aClass.isAnnotationPresent(Api.class)){
            description = aClass.getAnnotation(Api.class).description();
        }
        return description;
    }

    /**
     * 获取连接点所在的目标方法的@SystemLog描述或@ApiOperation描述
     * @param method 目标对象
     * @return 描述信息
     */
    private String getMehthodLogDescription(Method method){
        String description2 = "";
        if(method.isAnnotationPresent(SystemLog.class)){
            description2 = method.getAnnotation(SystemLog.class).description();
        }else if(method.isAnnotationPresent(ApiOperation.class)){
            description2 = method.getAnnotation(ApiOperation.class).value();
        }
        return description2;
    }
}