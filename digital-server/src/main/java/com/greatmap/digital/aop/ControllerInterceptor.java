package com.greatmap.digital.aop;

import com.alibaba.fastjson.JSON;
import com.greatmap.digital.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Configuration
@Aspect
@Order(1)
@Slf4j
public class ControllerInterceptor {

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution (* com.greatmap.*.controller..*Controller.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = ((MethodSignature) pjp.getSignature());
        boolean ignoreReq = false;
        boolean ignoreResp = false;
        Method method = pjp.getTarget().getClass()
                .getMethod(pjp.getSignature().getName(), methodSignature.getParameterTypes());


        beforeInterceptorLog(pjp, ignoreReq);

        Object result = pjp.proceed();


        afterInterceptorLog(pjp, result, ignoreResp);
        return result;
    }


    /**
     * 拦截器前打LOG的工具类
     *
     * @param pjp
     */
    private void beforeInterceptorLog(ProceedingJoinPoint pjp, boolean ignoreReq) {
        try {
            String className = pjp.getTarget().getClass().getSimpleName();
            String methodName = pjp.getSignature().getName();
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("uri[").append(request.getRequestURI()).append("], ");
            stringBuilder.append("ip[").append(IpUtil.getRequestIp(request)).append("], ");
            stringBuilder.append("method[").append(className).append("@").append(methodName)
                    .append("] ");

            for (int i = 0; i < pjp.getArgs().length; i++) {
                stringBuilder.append("参数名:");
                stringBuilder.append(methodSignature.getParameterNames()[i]);
                stringBuilder.append(",参数值:");
                stringBuilder.append(pjp.getArgs()[i]);
            }
            if (ignoreReq) {
                log.debug(stringBuilder.toString());
            } else {
                log.info(stringBuilder.toString());
            }
        } catch (Exception e) {
            log.error("打印日志异常", e);
        }
    }

    /**
     * 拦截器后打LOG的工具类
     *
     * @param pjp
     */
    private void afterInterceptorLog(ProceedingJoinPoint pjp, Object result, boolean ignoreResp) {
        try {
            String className = pjp.getTarget().getClass().getSimpleName();
            String methodName = pjp.getSignature().getName();
            if (ignoreResp) {
                log.debug(StringUtils
                        .join("method[", className, "@", methodName, "], 结果为: ", JSON.toJSONString(result)));
            } else {
                log.info(StringUtils
                        .join("method[", className, "@", methodName, "], 结果为: ", JSON.toJSONString(result)));
            }
        } catch (Exception e) {
            log.error("打印日志异常", e);
        }
    }

}
