package com.greatmap.digital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.greatmap.digital.dto.P10Dto;
import com.greatmap.digital.dto.SealDto;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.digital.mapper.DcCertSealRuleMapper;
import com.greatmap.digital.model.DcCertSealRule;
import com.greatmap.digital.service.DcCertSealRuleService;
import com.greatmap.digital.util.CodeGeneratorUtil;
import com.greatmap.digital.util.FileUtil;
import com.greatmap.digital.util.http.HttpUtils;
import com.greatmap.framework.core.io.IoUtil;
import com.greatmap.framework.core.util.CollectionUtil;
import com.greatmap.framework.web.controller.RestResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.greatmap.digital.base.DcConstants.CaFile.CERT_FILE;
import static com.greatmap.digital.base.DcConstants.CaHttp.*;
import static com.greatmap.digital.base.DcConstants.CaStatus.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Service
public class DcCertSealRuleServiceImpl extends ServiceImpl<DcCertSealRuleMapper, DcCertSealRule> implements DcCertSealRuleService {

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Value("${sign.hbca.http}")
    private String hbcaess_http;

    /**
     * 获取签章信息列表
     *
     * @param zzmc   证照名称
     * @param qzgzmc 签章规则名称
     * @param qxdm   区县代码
     * @return
     */
    @Override
    public List<DcCertSealRule> findCertSealRule(String zzmc, String qzgzmc, String qxdm) {
        Wrapper<DcCertSealRule> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(zzmc)) {
            wrapper.eq("ZZMC", zzmc);
        }
        if (StringUtils.isNotBlank(qzgzmc)) {
            wrapper.eq("QZGZMC", qzgzmc);
        }
        if (StringUtils.isNotBlank(qxdm)) {
            wrapper.eq("QXDM", qxdm);
        }
        return selectList(wrapper);
    }

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
    @Override
    public Page<DcCertSealRule> getCertSealTemplatePageList(Page<DcCertSealRule> page, String zzmc, String qzgzmc, Integer zt, String qxdm) {
        Wrapper<DcCertSealRule> we = new EntityWrapper<>();
        if (StringUtils.isNotBlank(zzmc)) {
            we.like("ZZMC", zzmc);
        }
        if (StringUtils.isNotBlank(qzgzmc)) {
            we.like("QZGZMC", qzgzmc);
        }
        if (zt != null) {
            we.eq("ZT", zt);
        }
        if (StringUtils.isNotBlank(qxdm)) {
            we.eq("QXDM", qxdm);
        }
        return selectPage(page, we);
    }

    /**
     * 添加证书签章对应关系
     *
     * @param qzgzmc
     * @param qzgzid
     * @param zzmc
     * @param zzlx
     * @param sfzxz
     * @param qxdm
     */
    @Override
    public boolean addCertSealTemplate(String qzgzmc, String qzgzid, String zzmc, String zzlx, Integer sfzxz,String mbbs, String qxdm, String qxmc) {
        Integer zt = 1;
        List<DcCertSealRule> certSealRuleList = selectList(new EntityWrapper<DcCertSealRule>().eq("ZZMC", zzmc).eq("QXDM", qxdm));
        if (CollectionUtils.isNotEmpty(certSealRuleList)) {
            if (certSealRuleList.stream().filter(m -> m.getQzgzid().equals(qzgzid)).count() > 0) {
                throw new DigitalException("添加失败，已经存在对应关系！");
            }
            //如果已经有状态为1的，则当前新增的状态则为0
            if (certSealRuleList.stream().filter(m -> m.getZt() == 1).count() > 0) {
                zt = 0;
            }
        }
        DcCertSealRule dcCertSealRule = new DcCertSealRule();
        dcCertSealRule.setId(codeGeneratorUtil.generateId());
        dcCertSealRule.setZzmc(zzmc);
        dcCertSealRule.setZzlx(zzlx);
        dcCertSealRule.setZt(zt);
        dcCertSealRule.setSfzxz(sfzxz);
        dcCertSealRule.setQzgzmc(qzgzmc);
        dcCertSealRule.setQzgzid(qzgzid);
        dcCertSealRule.setQxdm(qxdm);
        dcCertSealRule.setQxmc(qxmc);
        return insert(dcCertSealRule);
    }

    /**
     * 删除证书签章对应关系
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteCertSealTemplate(String id) {
        return deleteById(Long.valueOf(id));
    }

    /**
     * 修改证书签章对应关系状态(停用或启用)
     *
     * @param id
     * @param zt
     * @param qxdm
     * @return
     */
    @Override
    public boolean updateCertSealTemplate(String id, String zzmc, Integer zt, String qxdm) {
        boolean flag;
        //停用
        if (zt == 0) {
            DcCertSealRule dcCertSealRule = selectById(Long.valueOf(id));
            dcCertSealRule.setZt(zt);
            flag = updateById(dcCertSealRule);
        } else {
            //启用
            List<DcCertSealRule> certSealRuleList = selectList(new EntityWrapper<DcCertSealRule>().eq("ZZMC", zzmc).eq("QXDM", qxdm));
            if (CollectionUtils.isEmpty(certSealRuleList)) {
                throw new DigitalException(String.format("未找到当前【%s】对应记录！", zzmc));
            }
            if (certSealRuleList.stream().filter(item -> item.getZt() == 1).count() > 0) {
                throw new DigitalException(String.format("【%s】已有启用的规则!", zzmc));
            }
            DcCertSealRule dcCertSealRule = new DcCertSealRule();
            for (DcCertSealRule dc : certSealRuleList) {
                if (dc.getId().equals(Long.valueOf(id))) {
                    BeanUtils.copyProperties(dc, dcCertSealRule);
                    break;
                }
            }
            dcCertSealRule.setZt(zt);
            flag = updateById(dcCertSealRule);
        }
        return flag;
    }

    @Override
    public List<P10Dto> createP10(RestResult restResult, String sealIds, String certAlg, HttpServletResponse response) {
        if (StringUtils.isEmpty(certAlg)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        // params.put("sealIds", sealIds);
        params.put("certAlg", certAlg);
        String data = HttpUtils.httpPost2(hbcaess_http + URL_CREATE_P10, params);
        data = generateResult(restResult, data);
        if (org.apache.commons.lang.StringUtils.isEmpty(data)) {
            return null;
        }
        List<P10Dto> p10Dtos = JSONObject.parseArray("[" + data + "]", P10Dto.class);
        if (CollectionUtil.isEmpty(p10Dtos)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sealName = simpleDateFormat.format(Calendar.getInstance().getTime()) + ".txt";
        StringBuffer content = new StringBuffer();
        for (int ii = 0; ii < p10Dtos.size(); ii++) {
            content.append(p10Dtos.get(ii).toString());
        }
        byte[] bytes = content.toString().getBytes();
        BufferedOutputStream bufferedOutputStream = null;
        try {
            response.setContentType("application/force-download");
            String name = URLEncoder.encode(sealName, "UTF-8");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + name);
            response.setHeader("Content-Length", String.valueOf(bytes.length));

            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            bufferedOutputStream.write(bytes, 0, bytes.length);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedOutputStream != null) {
                IoUtil.close(bufferedOutputStream);
            }
        }

        return p10Dtos;
    }

    @Override
    public java.lang.String uploadCert(RestResult restResult, MultipartFile multipartFile) {
        String url = hbcaess_http + URL_UPLOAD_CERT;
        if (restResult == null) {
            restResult = new RestResult();
        }
        if (multipartFile == null || multipartFile.isEmpty()) {
            restResult.setSuccess(false);
            restResult.setMessage("上传证书文件为空！");
            return null;
        }
        File file = FileUtil.multipartFileToFile(multipartFile);
        if (file == null) {
            restResult.setSuccess(false);
            restResult.setMessage("创建临时文件失败！");
            return null;
        }
        byte[] bytes = HttpUtils.httpPost(url, null, CERT_FILE, file);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String res = null;
        res = new String(bytes, StandardCharsets.UTF_8);
        res = generateResult(restResult, res);
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (RuntimeException ex) {
            restResult.setSuccess(false);
            restResult.setMessage(ex.getMessage());
        }

        return res;
    }

    @Override
    public String moddifySealStatus(RestResult restResult, String sealId, String status) {
        if (org.apache.commons.lang.StringUtils.isEmpty(sealId) || org.apache.commons.lang.StringUtils.isEmpty(status)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("sealId", sealId);
        params.put("status", status);
        String data = HttpUtils.httpPost2(hbcaess_http + URL_EDIT_SEALSTATUS, params);
        data = generateResult(restResult, data);
        if (org.apache.commons.lang.StringUtils.isEmpty(data)) {
            return null;
        }
        return data;
    }

    /**
     * 获取签章详情
     *
     * @param sealId
     * @return
     */
    @Override
    public SealDto getSealById(RestResult restResult, String sealId) {
        if (org.apache.commons.lang.StringUtils.isEmpty(sealId)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("sealId", sealId);
        String data = HttpUtils.httpPost2(hbcaess_http + URL_GET_SEAL_DETAIL, params);
        data = generateResult(restResult, data);
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        SealDto sealDto = JSONObject.parseObject(data, SealDto.class);
        return sealDto;
    }

    /**
     * 生成结果对象
     *
     * @param restResult
     * @param values
     * @return 返回data数据
     */
    private String generateResult(RestResult restResult, String values) {
        if (org.apache.commons.lang.StringUtils.isEmpty(values)) {
            return null;
        }
        if (restResult == null) {
            restResult = new RestResult();
            restResult.setSuccess(true);
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(values);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            restResult.setSuccess(false);
            restResult.setMessage(ex.getMessage());
        }
        if (jsonObject == null) {
            return null;
        }
        String valueResult = jsonObject.getString(RESULT_CODE);
        if (valueResult.equals(RESULT_CODE_SUCCESS) || RESULT_CODE_SUCCESS_1.equals(valueResult) || RESULT_CODE_SUCCESS_2.equals(valueResult)
                || RESULT_CODE_SUCCESS_3.equals(valueResult)) {
            restResult.setSuccess(true);
        } else {
            restResult.setSuccess(false);
        }
        restResult.setMessage(jsonObject.getString(RESULT_MSG));
        if (jsonObject.containsKey(RESULT_DATA)) {
            return jsonObject.getString(RESULT_DATA);
        }
        return null;
    }
}
