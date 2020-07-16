package com.greatmap.digital.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gaorui
 * @create 2019-11-23 17:11
 */
@Component
public class CodeGeneratorUtil {

    @Autowired
    private SnowFlakeIdWorker snowFlakeIdWorker;

    public Long generateId(){
        return snowFlakeIdWorker.nextId();
    }

}
