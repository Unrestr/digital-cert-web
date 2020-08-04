package com.greatmap.digital.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.greatmap.digital.annotation.DcLog;
import com.greatmap.digital.dto.*;
import com.greatmap.digital.dto.rest.CertDto;
import com.greatmap.digital.dto.rest.Glxx;
import com.greatmap.digital.dto.rest.JcxxDto;
import com.greatmap.digital.dto.rest.QlxxDto;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.digital.excepition.DigitalThirdException;
import com.greatmap.digital.mapper.DcCertInfoMapper;
import com.greatmap.digital.mapper.DcPropertyPrintMapper;
import com.greatmap.digital.mapper.DcRegistrationPrintMapper;
import com.greatmap.digital.model.*;
import com.greatmap.digital.service.*;
import com.greatmap.digital.util.ReflectUtil;
import com.greatmap.digital.util.algorithm.base64.Base64Encoder;
import com.greatmap.digital.util.http.HttpUtils;
import com.greatmap.fms.model.FileInfo;
import com.greatmap.fms.service.FileUploadService;
import com.greatmap.framework.core.io.FileUtil;
import com.greatmap.framework.core.util.CollectionUtil;
import com.greatmap.framework.core.util.RandomUtil;
import com.greatmap.framework.web.controller.RestResult;
import com.greatmap.print.service.PrintService;
import com.greatmap.uums.sequence.service.ISequenceContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.greatmap.digital.base.DcConstants.CaHttp.*;
import static com.greatmap.digital.base.DcConstants.CaStatus.*;
import static com.greatmap.digital.base.DcConstants.DcStatus.*;
import static com.greatmap.digital.base.DcConstants.DcStatus.CANCEL;
import static com.greatmap.digital.base.DcConstants.DcType.ZM;
import static com.greatmap.digital.base.DcConstants.DcType.ZS;

/**
 * <p>
 * 证照信息服务实现类
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Service
public class DcCertInfoServiceImpl extends ServiceImpl<DcCertInfoMapper, DcCertInfo> implements DcCertInfoService {

    @Autowired(required = false)
    private DcCertInfoMapper dcCertInfoMapper;

    @Autowired
    private DcRightholderService dcRightholderService;

    @Autowired
    private DcRightInfoService dcRightInfoService;

    @Autowired
    private DcUnitInfoService dcUnitInfoService;

    @Autowired
    private DcCertMapService dcCertMapService;

    @Autowired
    private DcCertFileService dcCertFileService;

    @Autowired
    private PrintService printService;

    @Autowired
    private DcCertTemplateService dcCertTemplateService;

    @Autowired
    private DcCertSealRuleService dcCertSealRuleService;

    @Autowired
    private DcCertAssotypeService dcCertAssotypeService;

    @Autowired
    private DcPropertyPrintMapper dcPropertyPrintMapper;


    @Autowired
    private DcRegistrationPrintMapper dcRegistrationPrintMapper;

    @Reference
    private FileUploadService fileUploadService;

    @Reference(registry = "uums", version = "1.0.0")
    private ISequenceContext sequenceContext;

    @Value("${tempDirectory}")
    private String tempDir;

    @Value("${sign.hbca.http}")
    private String hbcaess_http;

    private final Logger logger = Logger.getLogger(DcCertInfoServiceImpl.class);


    @Override
    public Page<CertInfoDto> findCertInfoPage(Page<CertInfoDto> page, String bfjg, String qlr, String zt, String zzlx, String zzbh) {
        if (StringUtils.isBlank(zzlx)) {
            throw new DigitalException("证照类型不能为空");
        }
        if (!("1".equals(zzlx) || "2".equals(zzlx))) {
            throw new DigitalException("证照类型传参有误,非证书证明对应字典值");
        }
        List<CertInfoDto> list = dcCertInfoMapper.findCertInfoPage(page, bfjg, qlr, zt, zzlx, zzbh);
        page.setRecords(list);
        return page;
    }

    @Override
    public CertDetailDto getCertInfoById(String id) {

        CertDetailDto certDetailDto = new CertDetailDto();

        if (StringUtils.isBlank(id)) {
            throw new DigitalException("证照ID不能为空");
        }
        //获取证照基础信息
        CertBaseDto certBaseDto = dcCertInfoMapper.getBasicCertInfo(id);
        String zzbh = null;
        //根据证照类型,设置不动产权证号,不动产权证明号的值
        if (certBaseDto != null) {
            if (ZS.equals(certBaseDto.getZzfl())) {
                certBaseDto.setBdcqzh(certBaseDto.getZh());
            }
            if (ZM.equals(certBaseDto.getZzfl())) {
                certBaseDto.setBdcdjzmh(certBaseDto.getZh());
            }
            zzbh = certBaseDto.getZzbh();
        }
        certDetailDto.setCertBaseDto(certBaseDto);
        //根据证照编号获取关联的权利人信息列表
        if (StringUtils.isBlank(zzbh)) {
            throw new DigitalException("证照信息的证照编号不能为空");
        }
        EntityWrapper<DcRightholder> dcRightholderEntityWrapper = new EntityWrapper<>();
        dcRightholderEntityWrapper.eq("ryfl", "1");
        dcRightholderEntityWrapper.eq("zzbh", zzbh);
        List<DcRightholder> dcRightholderList = dcRightholderService.selectList(dcRightholderEntityWrapper);
        //循环权利人实体,复制数据到dto
        List<RightHolderDto> holderDtoList = new ArrayList<>();
        if (dcRightholderList != null && dcRightholderList.size() > 0) {
            for (DcRightholder dcRightholder : dcRightholderList) {
                RightHolderDto rightHolderDto = ReflectUtil.createAndCopyBean(dcRightholder, RightHolderDto.class);
                holderDtoList.add(rightHolderDto);
            }
        }
        certDetailDto.setRightHolderDtoList(holderDtoList);
        //获取权利信息
        EntityWrapper<DcRightInfo> dcRightInfoEntityWrapper = new EntityWrapper<>();
        dcRightInfoEntityWrapper.eq("zzbh", zzbh);
        List<DcRightInfo> dcRightInfos = dcRightInfoService.selectList(dcRightInfoEntityWrapper);
        if (dcRightInfos != null && dcRightInfos.size() > 0) {
            RightInfoDto rightInfoDto = ReflectUtil.createAndCopyBean(dcRightInfos.get(0), RightInfoDto.class);
            List<DcRegistrationPrint> registrationPrints = dcRegistrationPrintMapper.selectList(new EntityWrapper<DcRegistrationPrint>().eq("ZZBH", zzbh));
            List<DcPropertyPrint> propertyPrints = dcPropertyPrintMapper.selectList(new EntityWrapper<DcPropertyPrint>().eq("ZZBH", zzbh));
            if (CollectionUtil.isNotEmpty(registrationPrints)) {
                rightInfoDto.setQllx(registrationPrints.get(0).getZmqlhsx());
            }else if (CollectionUtil.isNotEmpty(propertyPrints)){
                rightInfoDto.setQllx(propertyPrints.get(0).getQllx());
            }
            rightInfoDto.setFj(dcRightInfos.get(0).getFj());
            rightInfoDto.setQlqtqk(dcRightInfos.get(0).getQlqtzk());
            rightInfoDto.setSyqq(dcRightInfos.get(0).getSyqqssj());
            rightInfoDto.setSyqz(dcRightInfos.get(0).getSyqjssj());
            certDetailDto.setRightInfoDto(rightInfoDto);
        }
        //获取不动产单元信息
        List<UnitDto> dcUnitInfos = new ArrayList<>();
        EntityWrapper<DcUnitInfo> dcUnitInfoEntityWrapper = new EntityWrapper<>();
        dcUnitInfoEntityWrapper.eq("zzbh", zzbh);
        List<DcUnitInfo> dcUnitInfoList = dcUnitInfoService.selectList(dcUnitInfoEntityWrapper);
        if (dcUnitInfoList != null && dcUnitInfoList.size() > 0) {
            for (DcUnitInfo dcUnitInfo : dcUnitInfoList) {
                UnitDto unitDto = ReflectUtil.createAndCopyBean(dcUnitInfo, UnitDto.class);
                unitDto.setTdytdm(dcUnitInfo.getYtdm());
                unitDto.setMj(dcUnitInfo.getMj());
                unitDto.setMjdw(dcUnitInfo.getMjdw());
                dcUnitInfos.add(unitDto);
            }
        }
        certDetailDto.setUnitDtoList(dcUnitInfos);
        //获取附图信息
        EntityWrapper<DcCertMap> dcCertMapEntityWrapper = new EntityWrapper<>();
        dcCertMapEntityWrapper.eq("zzbh", zzbh);
        List<DcCertMap> dcCertMapList = dcCertMapService.selectList(dcCertMapEntityWrapper);
        Map<String, List<DcCertMap>> dcCertMap = dcCertMapList.stream().collect(Collectors.groupingBy(DcCertMap::getBdcdyh));
        certDetailDto.setCertMap(dcCertMap);
        //获取电子证照的fmsId
        EntityWrapper<DcCertFile> dcCertFileEntityWrapper = new EntityWrapper<>();
        dcCertFileEntityWrapper.eq("zzbh", zzbh);
        List<DcCertFile> dcCertFiles = dcCertFileService.selectList(dcCertFileEntityWrapper);
        if (dcCertFiles != null && dcCertFiles.size() > 0) {
            certDetailDto.setZzfmsid(dcCertFiles.get(0).getWjid());
        }
        return certDetailDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DcLog(operateType = "生成证照")
    public Map<String, String> addCertificate(CertDto certDto, String ip, String sljg, String zh, String yhm) {
        //获取模板标识Code
        DcCertTemplate certTemplate = dcCertTemplateService.selectOne(new EntityWrapper<DcCertTemplate>().eq("QXDM", certDto.getQxdm()).eq("ZZMC", certDto.getZzmc()));
        //签章规则
        DcCertSealRule sealRule = dcCertSealRuleService.selectOne(new EntityWrapper<DcCertSealRule>().eq("QXDM", certDto.getQxdm()).eq("ZZMC", certDto.getZzmc()).eq("ZT", "1"));
        if (certTemplate == null || sealRule == null) {
            throw new DigitalThirdException("未配置打印或者签章信息!");
        }
        //生成不带签章PDF 返回映射路径
        String pdf = null;
        if (certDto.getZzlx().equals(ZS)) {
            //证书
            certDto.getZsxx().setZh(zh);
            certDto.getZsxx().setZzbh(certDto.getZzbh());
            pdf = printService.printPDF(certDto.getZsxx(), certTemplate.getMbbs(), true);
        } else if (certDto.getZzlx().equals(ZM)) {
            //证明
            certDto.getZmxx().setZh(zh);
            certDto.getZmxx().setZzbh(certDto.getZzbh());
            pdf = printService.printPDF(certDto.getZmxx(), certTemplate.getMbbs(), true);
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(pdf) || !pdf.endsWith(".pdf")) {
            throw new DigitalThirdException("打印证照信息失败,请检查wcf配置。");
        }
        logger.info("源PDF生成成功,地址" + pdf);
        File file = new File(pdf);
        //下载服务器pdf到本地
        HttpUtil.downloadFile(pdf, FileUtil.file(tempDir));
        String pdfTemp = null;
        try {
            pdfTemp = tempDir + File.separator + URLEncoder.encode(file .getName(), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            logger.error("文件编码失败", e);
            throw new DigitalThirdException("文件编码失败!", e);
        }
        //获取本地PDF对象
        File pdfFile = new File(pdfTemp);
        if (!pdfFile.exists()) {
            throw new DigitalThirdException("无法获取PDF文件对象!");
        }
        //判断有效还是注销章
        FileInfo fileInfo;
        if (StringUtils.isNotBlank(certDto.getJcxx().getSign())) {
            DcCertInfo certInfo = selectOne(new EntityWrapper<DcCertInfo>().eq("ZH", certDto.getJcxx().getYzh()));
            if (certInfo != null) {
                //如果存在注销前PDF  取注销恰PDF
                DcCertFile certFile = dcCertFileService.selectOne(new EntityWrapper<DcCertFile>().eq("ZZBH", certInfo.getZzbh()));
                byte[] fileByte = fileUploadService.getFileByte(certFile.getWjid());
                pdfFile = com.greatmap.digital.util.FileUtil.getFileByBytes(fileByte, tempDir, RandomUtil.simpleUUID() + ".pdf");
            }
            //注销章
            fileInfo = signCancel(sealRule.getQzgzid(), pdfFile, certDto.getJcxx().getSign());
        } else {
            //有效章
            fileInfo = signValid(sealRule.getQzgzid(), pdfFile);
        }
        return saveZsInfo(certDto, fileInfo);
    }

    private String generateResult(RestResult restResult, String values) {
        if (StringUtils.isEmpty(values)) {
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

    /**
     * 保存证书信息
     *
     * @param certDto
     * @return
     */
    private Map<String, String> saveZsInfo(CertDto certDto, FileInfo fileInfo) {
        if (StringUtils.isBlank(certDto.getYwh())) {
            certDto.setYwh(certDto.getJcxx().getYwh());
        }
        //该证号是否已经盖章   如果已经盖章则删除原来数据
        DcCertInfo certInfo = selectOne(new EntityWrapper<DcCertInfo>().eq("ZH", certDto.getZh()));

        if (certInfo!=null) {
            String subZzbh = certInfo.getZzbh();
            dcCertInfoMapper.delete(new EntityWrapper<DcCertInfo>().eq("ZZBH",subZzbh));
            dcCertAssotypeService.delete(new EntityWrapper<DcCertAssotype>().eq("ZZBH",subZzbh));
            dcCertFileService.delete(new EntityWrapper<DcCertFile>().eq("ZZBH",subZzbh));
            dcRightholderService.delete(new EntityWrapper<DcRightholder>().eq("ZZBH",subZzbh));
            dcUnitInfoService.delete(new EntityWrapper<DcUnitInfo>().eq("ZZBH",subZzbh));
            dcPropertyPrintMapper.delete(new EntityWrapper<DcPropertyPrint>().eq("ZZBH",subZzbh));
            dcRegistrationPrintMapper.delete(new EntityWrapper<DcRegistrationPrint>().eq("ZZBH",subZzbh));
        }

        //第三方传过来则用第三方证照编号  否则用自己
        String zzbh = null;
        if (StringUtils.isNotBlank(certDto.getZzbh())) {
            zzbh = certDto.getZzbh();
        }else {
            zzbh = IdWorker.getIdStr();
        }

        String zzbsm = IdWorker.getIdStr();
        //证照标识码
        // String zzbsm = sequenceContext.apply("MsgID");
        //保存基础信息
        DcCertInfo dcCertInfo = saveDcInfo(certDto);
        dcCertInfo.setZzbsm(zzbsm);
        dcCertInfo.setZzbh(zzbh);
        //保存权利信息
        DcRightInfo dcRightInfo = saveRightInfo(certDto);
        dcRightInfo.setZzbh(zzbh);
        //保存权利人信息
        List<DcRightholder> dcRightholders = saveRightHolder(certDto);
        //设置证照编号
        String finalZzbh = zzbh;
        dcRightholders.forEach(m -> m.setZzbh(finalZzbh));
        //保存单元信息
        List<DcUnitInfo> dcUnitInfos = saveUnitInfo(certDto);
        dcUnitInfos.forEach(m -> m.setZzbh(finalZzbh));
        //处理关联关系
        DcCertAssotype certAssoType = saveAssotype(certDto);
        certAssoType.setZzbh(zzbh);
        DcCertFile certFile = new DcCertFile();
        certFile.setId(RandomUtil.simpleUUID());
        //文件信息
        certFile.setWjid(fileInfo.getId());
        certFile.setWjmc(fileInfo.getFileName());
        certFile.setScsj(new Date());
        certFile.setXdml(fileInfo.getFilePathUrl());
        certFile.setZzbh(zzbh);
        certFile.setZzmc(certDto.getZzlx().equals(ZS) ? "不动产权证书" : "不动产登记证明");
        if (certDto.getZzlx().equals(ZS)) {
            DcPropertyPrint propertyPrint = ReflectUtil.createAndCopyBean(certDto.getZsxx(), DcPropertyPrint.class);
            propertyPrint.setZzbh(zzbh);
            propertyPrint.setId(RandomUtil.simpleUUID());
            dcPropertyPrintMapper.insert(propertyPrint);
        }else {
            DcRegistrationPrint registrationPrint = ReflectUtil.createAndCopyBean(certDto.getZmxx(), DcRegistrationPrint.class);
            registrationPrint.setZzbh(zzbh);
            registrationPrint.setId(RandomUtil.simpleUUID());
            dcRegistrationPrintMapper.insert(registrationPrint);
        }
        //数据入库
        insert(dcCertInfo);
        dcCertFileService.insert(certFile);
        dcRightInfoService.insert(dcRightInfo);
        dcRightholderService.insertBatch(dcRightholders);
        dcUnitInfoService.insertBatch(dcUnitInfos);
        dcCertAssotypeService.insert(certAssoType);
        Map<String, String> zzbhMap = new HashMap<>();
        zzbhMap.put("zzbh", zzbh);
        return zzbhMap;
    }

    private DcCertAssotype saveAssotype(CertDto certDto) {
        DcCertAssotype certAssotype = ReflectUtil.createAndCopyBean(certDto.getGlxx(), DcCertAssotype.class);
        Glxx glxx = certDto.getGlxx();
        certAssotype.setId(RandomUtil.simpleUUID());
        //关联类型
        certAssotype.setGllx(glxx.getGllx());
        //关联证号
        certAssotype.setGlzh(glxx.getGlzh());
        //证号
        certAssotype.setZh(glxx.getZh());
        //获取关联证照编号
        List<DcCertInfo> certInfos = dcCertInfoMapper.selectList(new EntityWrapper<DcCertInfo>().in("ZH", glxx.getGlzh()));
        if (CollectionUtil.isNotEmpty(certInfos)) {
            String glzzbh = certInfos.stream().map(DcCertInfo::getZzbh).collect(Collectors.joining(","));
            certAssotype.setGlzhbh(glzzbh);
        }
        return certAssotype;
    }

    private List<DcUnitInfo> saveUnitInfo(CertDto certDto) {
        return certDto.getDyxxList().stream().map(m -> {
            DcUnitInfo unitInfo = ReflectUtil.createAndCopyBean(m, DcUnitInfo.class);
            unitInfo.setId(RandomUtil.simpleUUID());
            //证号
            unitInfo.setZh(certDto.getZh());
            //证明号 如果是证明号则需带关联证号
            unitInfo.setBdcdjzmh(m.getBdcdjzmh());
            //单元号
            unitInfo.setBdcdyh(m.getBdcdyh());
            //分摊面积
            unitInfo.setFtmj(m.getFtmj());
            //坐落
            unitInfo.setZl(m.getZl());
            //用途
            unitInfo.setYt(m.getYt());
            unitInfo.setTdyt(m.getTdyt());
            unitInfo.setMj(m.getJzmj());
            unitInfo.setTdsyqmj(m.getTdsyqmj());
            //套内面积
            unitInfo.setTnmj(unitInfo.getTnmj());
            unitInfo.setZt(VALID);
            return unitInfo;
        }).collect(Collectors.toList());
    }

    private List<DcRightholder> saveRightHolder(CertDto certDto) {
        return certDto.getQlrList().stream().map(m -> {
            DcRightholder rightholder = ReflectUtil.createAndCopyBean(m, DcRightholder.class);
            rightholder.setId(RandomUtil.simpleUUID());
            //人员分类
            rightholder.setMc(m.getQlrmc());
            //不动产单元号
            rightholder.setBdcdyh(m.getBdcdyh());
            //关系
            rightholder.setGx(m.getGx());
            //权证号
            rightholder.setZh(certDto.getZh());
            //证照类型
            rightholder.setZjlx(m.getZjlx());
            //证件号
            rightholder.setZjh(m.getZjh());
            //共有信息
            rightholder.setGyfs(m.getGyfs());
            rightholder.setGybl(m.getGybl());
            rightholder.setGyqk(m.getGyqk());
            //是否持证
            rightholder.setSfcz(m.getSfcz());
            //电话
            rightholder.setLxdh(m.getLxdh());
            //人员分类
            rightholder.setRyfl("1");
            //处理共有人
            rightholder.setGyr(m.getGyr());
            rightholder.setGyrzjh(m.getGyrzjh());
            return rightholder;
        }).collect(Collectors.toList());
    }

    private DcRightInfo saveRightInfo(CertDto certDto) {
        DcRightInfo dcRightInfo = ReflectUtil.createAndCopyBean(certDto.getQlxx(), DcRightInfo.class);
        QlxxDto qlxx = certDto.getQlxx();
        dcRightInfo.setId(RandomUtil.simpleUUID());
        dcRightInfo.setZzlx(certDto.getZzlx().equals(ZS) ? "不动产权证书" : "不动产登记证明");
        //权利性质
        dcRightInfo.setQlxz(qlxx.getQlxz());
        //使用权时间
        dcRightInfo.setSyqqssj(qlxx.getSyqqssj());
        dcRightInfo.setSyqjssj(qlxx.getSyqjssj());
        //权利其他状况
        dcRightInfo.setQlqtzk(qlxx.getQlqtzk());
        //t土地权利类型
        dcRightInfo.setTdqllx(qlxx.getTdqllx());
        //土地权利性质
        dcRightInfo.setTdqlxz(qlxx.getTdqlxz());
        //附记
        dcRightInfo.setFj(qlxx.getFj());
        return dcRightInfo;
    }

    private DcCertInfo saveDcInfo(CertDto certDto) {
        DcCertInfo dcCertInfo = ReflectUtil.createAndCopyBean(certDto.getJcxx(), DcCertInfo.class);
        dcCertInfo.setId(RandomUtil.simpleUUID());
        dcCertInfo.setZzmc(certDto.getZzlx().equals(ZS) ? "不动产权证书" : "不动产登记证明");
        JcxxDto jcxx = certDto.getJcxx();
        //登记机构
        dcCertInfo.setDjjg(jcxx.getDjjg());
        //业务号
        dcCertInfo.setYwh(certDto.getYwh());
        //登记时间
        dcCertInfo.setDjsj(jcxx.getDjsj());
        //颁发时间
        dcCertInfo.setBfsj(new Date());
        //登记原因
        dcCertInfo.setDjyy(jcxx.getDjyy());
        //登记机构代码
        dcCertInfo.setDjjgdm(jcxx.getDjjgdm());
        //证号
        dcCertInfo.setZh(certDto.getZh());
        //证照分类
        dcCertInfo.setZzfl(certDto.getZzlx());
        //区县信息
        dcCertInfo.setQxdm(certDto.getQxdm());
        dcCertInfo.setQxmc(certDto.getQxmc());
        //注销机构
        dcCertInfo.setZxjg(jcxx.getZxjg());
        dcCertInfo.setZxjgdm(jcxx.getZxjgdm());
        //注销原因
        dcCertInfo.setZxyy(jcxx.getZxyy());
        //颁发信息
        dcCertInfo.setBfry(jcxx.getBfry());
        dcCertInfo.setBfsj(jcxx.getBfsj());
        //有效
        dcCertInfo.setZt(StringUtils.isBlank(certDto.getJcxx().getSign()) ? VALID : HISTORY);
        //TODO 抵押注销多笔单元
        //如果原证号不为空  修改上笔证照状态 为作废
        if (StringUtils.isNotBlank(jcxx.getYzh())) {
            //上笔证号信息
            DcCertInfo yCrtInfo = selectOne(new EntityWrapper<DcCertInfo>().eq("ZH", jcxx.getYzh()));
            if (yCrtInfo != null) {
                yCrtInfo.setZt(CANCEL);
                updateById(yCrtInfo);
            }
        }

        return dcCertInfo;
    }

    /**
     * 签有效章
     *
     * @param templateId
     * @param file
     * @return
     */
    public FileInfo signValid(String templateId, File file) {
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

    /**
     * 签注销章
     *
     * @param templateId 签章规则
     * @param file
     * @param sign       签章信息
     * @return
     */
    private FileInfo signCancel(String templateId, File file, String sign) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("templateIds", templateId);
        Base64Encoder encoder = new Base64Encoder();
        HashMap<String, Object> imageBase64 = encoder.createImageBase64(sign);
        //获取注销图片
        String responseDate = HttpUtils.httpPost2(hbcaess_http + CREATE_IMAGE_BASE64, imageBase64);
        Map responseMap = new HashMap<String, Object>(2);
        responseMap = JSON.parseObject(responseDate, Map.class);
        params.put("imageBase64", responseMap.get("data").toString());
        //注销章
        byte[] bytes = HttpUtils.httpPost(hbcaess_http + URL_SIGN_PDF_EX, params, "pdfFile", file);
        return fileUploadService.uploadByByte(file.getName(), bytes);
    }

    @Override
    public List<Map<String, Object>> selectNumByDay(String djjgdm, Date startDate, Date endDate) {
        return dcCertInfoMapper.selectNumByDay(djjgdm, startDate, endDate);
    }

    @Override
    @DcLog(operateType = "证照查验")
    public RestResult verifyCertificate(RestResult restResult, String zzmc, String zh, String base64File, String yhm, String jg, String ip) {
        DcCertInfo dcCertInfo = selectOne(new EntityWrapper<DcCertInfo>().eq("ZZMC", zzmc).eq("ZH", zh).eq("ZT", "1"));
        if (dcCertInfo == null) {
            restResult.setSuccess(false);
            restResult.setMessage(zh + "已注销或不存在");
            return restResult;
        }
        File file = com.greatmap.digital.util.FileUtil.base64ToFile(base64File);
        byte[] verifyInfo = HttpUtils.httpPost(hbcaess_http + URL_VERIFY_CERT, null, "pdfFile", file);
        if (verifyInfo == null || verifyInfo.length == 0) {
            throw new DigitalException("验签失败  返回结果为空！");
        }
        String verify = new String(verifyInfo, StandardCharsets.UTF_8);
        //解析响应结果
        generateResult(restResult, verify);
        if (restResult.isSuccess()) {
            switch (restResult.getMessage()) {
                case "1":
                    restResult.setSuccess(true);
                    restResult.setMessage("验签通过");
                    break;
                case "2":
                    restResult.setSuccess(false);
                    restResult.setMessage(restResult.getMessage() + ",电子证照文件已被修改！");
                    break;
                case "3":
                    restResult.setSuccess(false);
                    restResult.setMessage("未经认证的电子证照文件！");
                    break;
                default:
                    break;
            }
        }
        return restResult;
    }


}
