package com.greatmap.digital.config;

import com.greatmap.digital.excepition.DigitalException;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<String, Date> {

    private static final String [] formats = {"yyyy-MM-dd","yyyyMMdd","yyyy-MM-dd HH:mm:ss","yyyy/MM/dd"};

    @Override
    public Date convert(String source) {
        Date date = null;
        for (String format:formats) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                date = simpleDateFormat.parse(source);
            } catch (ParseException e) {
                // do nothing
            }
            if (date != null) {
                return date;
            }
        }
        throw new DigitalException(String.format("数据格式[%s]错误",source));
    }
}
