package com.greatmap.digital.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.greatmap.digital.mapper.DcCertTemplateMapper;
import com.greatmap.digital.model.DcCertTemplate;
import com.greatmap.digital.service.DcCertTemplateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.security.DigestException;

/**
 * <p>
 * 模板信息表。第三方系统传值json数据电子证照通过json打印证照 然后盖章。需要通过模板标识找对应模板打印再盖章 服务实现类
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Service
public class DcCertTemplateServiceImpl extends ServiceImpl<DcCertTemplateMapper, DcCertTemplate> implements DcCertTemplateService {



    @Override
    public Page<DcCertTemplate> listTemplate(Page<DcCertTemplate> page, String qxmc, String mbbs) {
        EntityWrapper<DcCertTemplate> dcCertTemplateEntityWrapper = new EntityWrapper<>();
        if(StringUtils.isNotBlank(qxmc)){
            dcCertTemplateEntityWrapper.eq("qxmc",qxmc);
        }
        if(StringUtils.isNotBlank(mbbs)){
            dcCertTemplateEntityWrapper.eq("mbbs",mbbs);
        }
        return selectPage(page, dcCertTemplateEntityWrapper);
    }

    @Override
    public boolean updateTemplate(String id,String zt) throws DigestException {
        if(StringUtils.isBlank(id)){
            throw new DigestException("模板ID不能为空");
        }
        if(StringUtils.isBlank(zt)){
            throw new DigestException("状态不能为空");
        }
        DcCertTemplate dcCertTemplate = new DcCertTemplate();
        dcCertTemplate.setId(id);
        dcCertTemplate.setZt(zt);
        return updateById(dcCertTemplate);
    }
}
