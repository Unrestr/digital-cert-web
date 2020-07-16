package com.greatmap.digital.util;

import com.greatmap.digital.excepition.DigitalException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 * @author gaorui
 * @create 2019-11-23 17:10
 */
@Component
public class ReflectUtil {

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @SuppressWarnings("rawtypes")
    public Object autoSetValue(Class classType) {

        Object bean = null;
        try {
            bean = classType.newInstance();

            Field[] fs = classType.getDeclaredFields();

            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                //设置字段属性可以访问
                f.setAccessible(true);
                //获取字段属性类型名
                String type = f.getType().getName();
                autoSetValueByType(codeGeneratorUtil, type, bean, f);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return bean;
    }


    private static void autoSetValueByType(CodeGeneratorUtil codeGeneratorUtil, String type, Object bean, Field f) throws IllegalArgumentException, IllegalAccessException {
        float min = 1f;
        float max = 10f;
        float floatBounded = min + new Random().nextFloat() * (max - min);
        double minDou = 1.0;
        double maxDou = 10.0;
        double boundedDouble = minDou + new Random().nextDouble() * (maxDou - minDou);
        boundedDouble = RandomUtils.nextInt(1, 9999);
        if (type.endsWith("Long")) {
            f.set(bean, codeGeneratorUtil.generateId());
        } else if (type.endsWith("String")) {
            f.set(bean, RandomStringUtils.randomAlphanumeric(4));
        } else if (type.endsWith("Integer")) {
            f.set(bean, RandomUtils.nextInt(1, 9));
        } else if ("Date".equalsIgnoreCase(type) || "java.util.Date".equalsIgnoreCase(type)) {
            f.set(bean, new Date());
        } else if (type.endsWith("boolean")) {
            f.set(bean, Boolean.TRUE);
        } else if (type.endsWith("double")) {
            f.set(bean, boundedDouble);
        } else if (type.endsWith("float")) {
            f.set(bean, floatBounded);
        } else if (type.endsWith("BigDecimal")) {
            f.set(bean, new BigDecimal(boundedDouble));
        }
    }

    /**
     * 创建clazz对象，并将orig对象的属性拷贝
     *
     * @param orig  原始对象
     * @param clazz 目标对象的class
     */
    public static <T> T createAndCopyBean(Object orig, Class<T> clazz) {
        try {
            T dest = clazz.newInstance();
            BeanUtils.copyProperties(orig, dest);
            return dest;
        } catch (Exception e) {
            throw new DigitalException("实体类复制失败", e);
        }
    }
}
