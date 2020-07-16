package com.greatmap.digital.util;

import com.greatmap.uums.sso.common.core.model.UserInfoResult;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * 用于向本地线程共享变量中存取数据的工具类
 *
 * @author greatmap
 */
public class LoginUserUtil {

    private static final ThreadLocal<Map<String, Object>> THREAD_REQUEST = new ThreadLocal<>();

    /**
     * 向本地线程变量中存数据
     *
     * @param map
     */
    public static void setLoginUser(Map<String, Object> map) {
        THREAD_REQUEST.set(map);
    }

    /**
     * 通过本地线程共享变量获取登录人姓名
     *
     * @return
     */
    public static String getLoginName() {
        Map<String, Object> map = THREAD_REQUEST.get();
        if(MapUtils.isNotEmpty(map)){
            UserInfoResult infoResult = (UserInfoResult) map.get("user");
            return infoResult.getUserName();
        }
        return null;
    }

    /**
     * 通过本地线程共享变量获取登录人ID
     *
     * @return
     */
    public static String getLoginId() {
        Map<String, Object> map = THREAD_REQUEST.get();
        if(MapUtils.isNotEmpty(map)){
            UserInfoResult infoResult = (UserInfoResult) map.get("user");
            return infoResult.getUserId();
        }
        return null;
    }

    /**
     * 清除本地线程变量
     */
    public static void remove() {
        THREAD_REQUEST.remove();
    }

}
