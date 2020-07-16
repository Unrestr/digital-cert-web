package com.greatmap.digital.annotation;

import com.alibaba.fastjson.JSONObject;
import com.greatmap.digital.excepition.DigitalThirdException;
import com.greatmap.digital.model.DcSealLog;
import com.greatmap.digital.service.DcSealLogService;
import com.greatmap.framework.core.util.CollectionUtil;
import com.greatmap.framework.core.util.ObjectUtil;
import com.greatmap.framework.core.util.RandomUtil;
import com.greatmap.uums.sso.restClient.common.RestResult;
import com.greatmap.uums.sso.restClient.common.TokenUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author gaorui
 * @create 2020-07-01 16:25
 */
@Component
@Aspect
public class DcAopAspect {

    @Autowired
    private DcSealLogService dcSealLogService;


    @Around("@annotation(com.greatmap.digital.annotation.DcLog)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) {
        DcSealLog dcSealLog = new DcSealLog();
        dcSealLog.setId(RandomUtil.simpleUUID());
        //获取请求参数
        Map<String, Object> value = getNameAndValue(pjp);
        if (CollectionUtil.isNotEmpty(value)) {
            //遍历匹配 人员 ip 机构信息
            value.forEach((k, v) -> {
                if (StringUtils.equalsAnyIgnoreCase(k, "userName", "userId", "yhm")) {
                    //操作人员
                    dcSealLog.setCzry(ObjectUtil.isNull(v) ? null : v.toString());
                } else if (StringUtils.equalsAnyIgnoreCase(k, "ip")) {
                    //IP
                    dcSealLog.setIp(ObjectUtil.isNull(v) ? null : v.toString());
                } else if (StringUtils.equalsAnyIgnoreCase(k, "djjg", "sjjg", "jg")) {
                    //机构
                    dcSealLog.setSsjg(ObjectUtil.isNull(v) ? null : v.toString());
                } else if (StringUtils.equalsAnyIgnoreCase(k, "zh", "bdcqzh", "bdcdjzmh")) {
                    //证号
                    dcSealLog.setZh(ObjectUtil.isNull(v) ? null : v.toString());
                }
            });
        }
        //获得方法签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取方法
        Method method = signature.getMethod();
        //获取操作注释
        String operateType = method.getAnnotation(DcLog.class).operateType();
        dcSealLog.setCzxq(operateType);
        //时间
        dcSealLog.setQqsj(new Date());
        Object proceed = null;
        try {
            //TODO 执行失败可能不抛异常
            //执行方法
            proceed = pjp.proceed();
            //成功
            dcSealLog.setXyjg("1");
        } catch (Throwable throwable) {
            //失败
            dcSealLog.setXyjg("0");
            throwable.printStackTrace();
            //抛出异常信息便于开发
            throw new DigitalThirdException(throwable);
        } finally {
            //无论如何 执行插入
            dcSealLogService.insert(dcSealLog);
        }

        return proceed;
    }

    /**
     * 获取参数Map集合
     *
     * @param joinPoint
     * @return
     */
    Map<String, Object> getNameAndValue(ProceedingJoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>(16);
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], paramValues[i]);
        }
        return param;
    }
}
