package com.greatmap.digital.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.greatmap.digital.model.DcCertTemplate;

import java.security.DigestException;

/**
 * <p>
 * 模板信息表。第三方系统传值json数据电子证照通过json打印证照 然后盖章。需要通过模板标识找对应模板打印再盖章 服务类
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
public interface DcCertTemplateService extends IService<DcCertTemplate> {

    /**
     * 根据区县名称,模板标识获取模板列表
     * @param page
     * @param qxmc
     * @param mbbs
     * @return
     */
    Page<DcCertTemplate> listTemplate(Page<DcCertTemplate> page, String qxmc, String mbbs);

    /**
     * 根据id修改模板的状态
     * @param id
     * @return
     */
    boolean updateTemplate(String id,String zt) throws DigestException;
}
