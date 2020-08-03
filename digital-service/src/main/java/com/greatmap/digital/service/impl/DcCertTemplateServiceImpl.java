package com.greatmap.digital.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.greatmap.digital.dto.DcCertTemplateDto;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.digital.mapper.DcCertTemplateMapper;
import com.greatmap.digital.model.DcCertSealRule;
import com.greatmap.digital.model.DcCertTemplate;
import com.greatmap.digital.service.DcCertSealRuleService;
import com.greatmap.digital.service.DcCertTemplateService;
import com.greatmap.digital.util.FileUtil;
import com.greatmap.digital.util.http.HttpUtils;
import com.greatmap.fms.model.FileInfo;
import com.greatmap.fms.service.FileInfoService;
import com.greatmap.fms.service.FileUploadService;
import com.greatmap.framework.core.util.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.greatmap.digital.base.DcConstants.CaHttp.URL_SIGN_PDF;

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

    @Autowired
    private DcCertTemplateMapper dcCertTemplateMapper;

    @Autowired
    private DcCertSealRuleService dcCertSealRuleService;

    @Reference
    private FileUploadService fileUploadService;

    @Reference
    private FileInfoService fileInfoService;

    @Value("${tempDirectory}")
    private String tempDir;

    @Value("${sign.hbca.http}")
    private String hbcaess_http;

    @Override
    public Page<DcCertTemplate> listTemplate(Page<DcCertTemplate> page, String qxmc, String mbbs) {
        Wrapper<DcCertTemplate> wrapper = new EntityWrapper<DcCertTemplate>().orderBy("cjsj");
        if (StringUtils.isNotBlank(qxmc)) {
            wrapper.like("qxmc", qxmc.replaceAll("(0)+$", ""), SqlLike.RIGHT);
        }
        if (StringUtils.isNotBlank(mbbs)) {
            wrapper.eq("mbbs", mbbs);
        }
        return selectPage(page, wrapper);
    }

    @Override
    public boolean updateTemplate(String id, String zt) throws DigestException {
        if (StringUtils.isBlank(id)) {
            throw new DigestException("模板ID不能为空");
        }
        if (StringUtils.isBlank(zt)) {
            throw new DigestException("状态不能为空");
        }
        DcCertTemplate dcCertTemplate = new DcCertTemplate();
        dcCertTemplate.setId(id);
        dcCertTemplate.setZt(zt);
        return updateById(dcCertTemplate);
    }

    @Override
    public Page<DcCertTemplateDto> listTemplatePage(Page<DcCertTemplateDto> page, String qxmc, String zzmc, String gzmc, String zt) {
        List<DcCertTemplateDto> certTemplateDtoList = dcCertTemplateMapper.listTemplatePage(page, qxmc, zzmc, gzmc, zt);
        page.setRecords(certTemplateDtoList);
        return page;
    }

    @Override
    public List<DcCertTemplate> getTemplateList(String qxmc, String zzmc, String zt) {
        EntityWrapper<DcCertTemplate> entityWrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(qxmc)) {
            entityWrapper.eq("QXMC", qxmc);
        }
        if (StringUtils.isNotBlank(zzmc)) {
            entityWrapper.eq("ZZMC", zzmc);
        }
        if (StringUtils.isNotBlank(zt)) {
            entityWrapper.eq("ZT", zt);
        }
        return selectList(entityWrapper);
    }

    @Override
    public boolean saveTemplate(String mbbs, String zzmc, String qxdm, String qxmc, String bz, String zt, String fileId) {
        DcCertTemplate templates = selectOne(new EntityWrapper<DcCertTemplate>().eq("MBBS", mbbs));
        if (templates == null) {
            templates = new DcCertTemplate();
            templates.setId(RandomUtil.simpleUUID());
        }
        templates.setBz(bz);
        templates.setMbbs(mbbs);
        templates.setQxdm(qxdm);
        templates.setQxmc(qxmc);
        templates.setZt(zt);
        templates.setZzmc(zzmc);
        templates.setMbsl(fileId);
        return insertOrUpdate(templates);
    }

    @Override
    public boolean delTemplate(String id) {
        return deleteById(id);
    }

    @Override
    public FileInfo preview(String mbbs) {
        DcCertTemplate template = selectOne(new EntityWrapper<DcCertTemplate>().eq("MBBS", mbbs));
        byte[] bytes = fileUploadService.getFileByte(template.getMbsl());
        File file = FileUtil.getFileByBytes(bytes, tempDir, RandomUtil.simpleUUID() + ".pdf");
        DcCertSealRule dcCertSealRule = dcCertSealRuleService.selectOne(new EntityWrapper<DcCertSealRule>().eq("QXDM", template.getQxdm()).eq("ZZMC", template.getZzmc()).eq("ZT", "1"));
        FileInfo fileInfo = signValid(dcCertSealRule.getQzgzid(), file);

        return fileInfo;
    }

    @Override
    public FileInfo previewQz(String qzgzid) throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("preview.pdf");
        byte[] bytes = FileUtil.toByteArray(resourceAsStream);
        File file = FileUtil.getFileByBytes(bytes, tempDir, RandomUtil.simpleUUID() + ".pdf");
        return signValid(qzgzid, file);
    }

    @Override
    public List<DcCertSealRule> list(String qxdm, String gzmc, String zxz, String zzlx) {
        Wrapper<DcCertSealRule> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(qxdm)) {
            wrapper.eq("QXDM",qxdm);
        }
        if (StringUtils.isNotBlank(gzmc)) {
            wrapper.eq("QZGZMC",gzmc);
        }
        if (StringUtils.isNotBlank(zxz)) {
            wrapper.eq("SFZXZ",zxz);
        }
        if (StringUtils.isNotBlank(zzlx)) {
            wrapper.eq("ZZLX",zzlx);
        }
        return dcCertSealRuleService.selectList(wrapper);
    }

    @Override
    public List<DcCertTemplate> getMbbsList(String qxdm, String zzmc) {
        Wrapper<DcCertTemplate> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(qxdm)) {
            wrapper.eq("QXDM",qxdm);
        }
        if (StringUtils.isNotBlank(zzmc)) {
            wrapper.eq("ZZMC",zzmc);
        }
        return selectList(wrapper);
    }

    /**
     * 签有效章
     *
     * @param templateId
     * @param file
     * @return
     */
    private FileInfo signValid(String templateId, File file) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("templateIds", templateId);
        //获取带签章PDF
        byte[] pdfFiles = HttpUtils.httpPost(hbcaess_http + URL_SIGN_PDF, params, "pdfFile", file);
        if (pdfFiles == null || pdfFiles.length == 0) {
            throw new DigitalException("盖章失败  返回结果为空！");
        }
        String fileName = null;
        try {
            fileName = URLDecoder.decode(file.getName(), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileUploadService.uploadByByte(fileName, pdfFiles);
    }

}
