package com.greatmap.digital.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.greatmap.digital.dto.DcCertTemplateDto;
import com.greatmap.digital.model.DcCertSealRule;
import com.greatmap.digital.model.DcCertTemplate;
import com.greatmap.fms.model.FileInfo;

import java.io.IOException;
import java.security.DigestException;
import java.util.List;

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

    /**
     * 获取规则应用管理信息
     * @param page
     * @param qxmc
     * @param zzmc
     * @param gzmc
     * @param zt
     * @return
     */
    Page<DcCertTemplateDto> listTemplatePage(Page<DcCertTemplateDto> page, String qxmc, String zzmc, String gzmc, String zt);

    List<DcCertTemplate> getTemplateList(String qxmc, String zzmc, String zt);

    boolean saveTemplate(String mbbs, String zzmc, String qxdm, String qxmc, String bz, String zt, String fileId);

    boolean delTemplate(String id);

    /**
     * 预览签章
     * @param mbbs
     * @return
     */
    FileInfo preview(String mbbs);

    FileInfo previewQz(String qzgzid) throws IOException;

    List<DcCertSealRule> list(String qxdm, String gzmc, String zxz, String zzlx);

    List<DcCertTemplate> getMbbsList(String qxdm, String zzlx);
}
