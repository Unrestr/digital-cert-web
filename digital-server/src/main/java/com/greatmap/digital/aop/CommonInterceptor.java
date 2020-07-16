package com.greatmap.digital.aop;

import com.alibaba.fastjson.JSON;
import com.greatmap.digital.util.LoginUserUtil;
import com.greatmap.framework.core.util.StrUtil;
import com.greatmap.uums.sso.common.core.model.UserInfoResult;
import com.greatmap.uums.sso.restClient.common.RestResult;
import com.greatmap.uums.sso.restClient.common.TokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截token  获取用户信息
 * @author gaorui
 */
@Component
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            String token = this.parseRequest(request);
            if (StrUtil.isNotBlank(token)) {
                RestResult result = TokenUtil.verify(request);
                if (!result.isSuccess()) {
                    this.writeJsonResult(result, response);
                    Map<String,Object> map =new HashMap<>(16);
                    LoginUserUtil.setLoginUser(map);
                    return false;
                }else{
                    UserInfoResult infoResult = JSON.parseObject(JSON.toJSONString(result.getData()), UserInfoResult.class);
                    Map<String,Object> map =new HashMap<>(16);
                    map.put("user",infoResult);
                    LoginUserUtil.setLoginUser(map);
                }

                response.addHeader("token", result.getToken());
                response.addHeader("Access-Control-Expose-Headers", "token");
                return true;
            }
        }

        return true;
    }

    public String parseRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        token = StringUtils.isBlank(token) ? request.getParameter("token") : token;
        return token;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LoginUserUtil.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }


    public void writeJsonResult(Object result, HttpServletResponse response) {
        try {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

}
