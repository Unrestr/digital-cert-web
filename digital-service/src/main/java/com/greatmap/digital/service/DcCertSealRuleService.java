package com.greatmap.digital.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.greatmap.digital.dto.P10Dto;
import com.greatmap.digital.dto.SealDto;
import com.greatmap.digital.model.DcCertSealRule;
import com.greatmap.framework.web.controller.RestResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 签章信息服务类
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
public interface DcCertSealRuleService extends IService<DcCertSealRule> {

    /**
     * 获取签章信息列表
     *
     * @param zzmc   证照名称
     * @param qzgzmc 签章规则名称
     * @param qxdm   区县代码
     * @return
     */
    List<DcCertSealRule> findCertSealRule(String zzmc, String qzgzmc, String qxdm);

    /**
     * 获取证书签章对应关系列表
     *
     * @param page
     * @param zzmc
     * @param qzgzmc
     * @param zt
     * @param qxdm
     * @return
     */
    Page<DcCertSealRule> getCertSealTemplatePageList(Page<DcCertSealRule> page, String zzmc, String qzgzmc, Integer zt, String qxdm);

    /**
     * 添加证书签章对应关系
     *
     * @param qzgzmc
     * @param qzgzid
     * @param zzmc
     * @param zzlx
     * @param sfzxz
     * @param mbbs
     * @param qxdm
     * @param qxmc
     * @return
     */
    boolean addCertSealTemplate(String qzgzmc ,String zzmc, String zzlx, Integer sfzxz, String mbbs, String qxdm, String qxmc);

    /**
     * 删除证书签章对应关系
     *
     * @param id
     * @return
     */
    boolean deleteCertSealTemplate(String id);

    /**
     * 修改证书签章对应关系状态(停用或启用)
     *
     * @param id
     * @param zzmc
     * @param zt
     * @param qxdm
     * @return
     */
    boolean updateCertSealTemplate(String id, String zzmc, Integer zt, String qxdm);

    /**
     * 生成P10
     *
     * @param restResult
     * @param sealIds
     * @param certAlg
     * @param reponse
     * @return
     */
    List<P10Dto> createP10(RestResult restResult, String sealIds, String certAlg, HttpServletResponse reponse);

    /**
     * 上传签章信息
     *
     * @param restResult
     * @param multipartFile
     * @return
     */
    String uploadCert(RestResult restResult, MultipartFile multipartFile);

    /**
     * 修改数字签章状态
     *
     * @param restResult
     * @param sealId
     * @param status
     * @return
     */
    String moddifySealStatus(RestResult restResult, String sealId, String status);

    /**
     * 获取签章信息
     *
     * @param restResult
     * @param sealId
     * @return
     */
    SealDto getSealById(RestResult restResult, String sealId);
}
