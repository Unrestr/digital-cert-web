package com.greatmap.digital.base;

import com.baomidou.mybatisplus.toolkit.ArrayUtils;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.framework.core.exceptions.MessageException;
import com.greatmap.framework.core.exceptions.StatefulException;
import com.greatmap.framework.core.util.ClassUtil;
import com.greatmap.framework.web.controller.BaseRestController;
import com.greatmap.framework.web.controller.RestResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.sql.SQLException;

/**
 * @author greatmap
 */
public abstract class BaseController extends BaseRestController {

    @ExceptionHandler(Exception.class)
    @Override
    public RestResult exceptionHandler(Exception exception) {
        exception.printStackTrace();
        exception.getStackTrace();
        String msg = "电子证照平台服务异常";
        RestResult restResult = commonExceptionHandler(exception);
        restResult = restResult != null ? restResult : rpcExceptionHandler(exception);
        if (restResult != null) {
            return restResult;
        }
        restResult = renderException(msg, ExceptionUtils.getStackTrace(exception));
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        addSqlExceptionHandler(rootCause, restResult);
        return restResult;
    }

    /**
     * 通用异常处理
     *
     * @param exception 异常信息
     * @return
     */
    private RestResult commonExceptionHandler(Exception exception) {
        if (exception instanceof DigitalException) {
            DigitalException digitalException = (DigitalException) exception;
            return new RestResult(digitalException.getMessage(), ExceptionUtils.getStackTrace(digitalException));
        } else if (exception instanceof SQLException) {
            SQLException sqlException = (SQLException) exception;
            return new RestResult("电子证照平台数据库操作异常:" + sqlException.getMessage(), ExceptionUtils.getStackTrace(sqlException));
        } else if (exception instanceof DuplicateKeyException) {
            DuplicateKeyException duplicateKeyException = (DuplicateKeyException) exception;
            return new RestResult("电子证照平台数据库操作异常,违反唯一约束条件:" + duplicateKeyException.getMessage(), ExceptionUtils.getStackTrace(duplicateKeyException));
        } else if (exception instanceof DataAccessException) {
            DataAccessException dataAccessException = (DataAccessException) exception;
            return new RestResult("电子证照平台数据库访问异常:" + dataAccessException.getMessage(), ExceptionUtils.getStackTrace(dataAccessException));
        } else if (exception instanceof StatefulException) {
            StatefulException e = (StatefulException) exception;
            return stateExceptionHandler(e);
        } else if (exception instanceof MessageException) {
            return messageExceptionHandler((MessageException) exception);
        } else if (exception instanceof BindException) {
            return bindExceptionHandler((BindException) exception);
        } else if (exception instanceof ConstraintViolationException) {
            // TODO 违反约束
            return new RestResult("违反约束异常", ExceptionUtils.getStackTrace(exception));
        } else if (exception instanceof ValidationException) {
            // TODO 校验错误
            return new RestResult("校验错误异常", ExceptionUtils.getStackTrace(exception));
        } else if (exception instanceof UnauthorizedException) {
            //TODO 未授权
            return new RestResult("未授权异常", ExceptionUtils.getStackTrace(exception));
        } else if (exception instanceof InvalidSessionException) {
            //TODO 会话超时
            return new RestResult("会话超时异常", ExceptionUtils.getStackTrace(exception));
        }
        return new RestResult("电子证照平台服务异常", ExceptionUtils.getStackTrace(exception));
    }

    /**
     * dubbo调用 包装RuntimeException处理
     *
     * @param e
     * @return
     */
    private RestResult rpcExceptionHandler(Exception e) {
        if (e instanceof RuntimeException) {
            String exceptionMessage = e.getMessage();
            exceptionMessage = StringUtils.substringBetween(exceptionMessage, "Caused by:", System.lineSeparator());
            if (StringUtils.isBlank(exceptionMessage)) {
                return null;
            }
            exceptionMessage = exceptionMessage.trim();
            String[] split = exceptionMessage.split(":");
            if (ArrayUtils.isEmpty(split)) {
                return null;
            }
            String exceptionClassName = split[0];
            try {
                Object o = ClassUtil.newInstance(exceptionClassName);
                if (o instanceof SQLException) {
                    String message = StringUtils.substringAfter(exceptionMessage, ":");
                    return renderException(message, e.getMessage());
                }
            } catch (Exception exception) {
                return null;
            }
        }
        return null;
    }


    /**
     * 状态异常处理
     *
     * @param e
     * @return
     */
    private RestResult stateExceptionHandler(StatefulException e) {
        RestResult exceptionRestResult = getExceptionRestResult(e);
        if (e.getState() != null) {
            exceptionRestResult.setMessage(e.getState().toString());
        }
        return exceptionRestResult;
    }


}
