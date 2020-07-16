package com.greatmap.digital.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.greatmap.digital.dto.CertDetailDto;
import com.greatmap.digital.dto.CertInfoDto;
import com.greatmap.digital.dto.rest.CertDto;
import com.greatmap.digital.model.DcCertInfo;
import com.greatmap.framework.web.controller.RestResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  有效证照信息服务类
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
public interface DcCertInfoService extends IService<DcCertInfo> {


    /**
     * 获取证照分页信息
     * @param page
     * @param bfjg 颁发机构
     * @param qlr 权利人
     * @param zt 状态
     * @param zzlx 证照类型
     * @param zzbh 证照编号
     * @return
     */
    Page<CertInfoDto> findCertInfoPage(Page<CertInfoDto> page, String bfjg, String qlr, String zt, String zzlx, String zzbh);

    /**
     * 根据id获取证照详细信息
     * @param id
     * @return
     */
    CertDetailDto getCertInfoById(String id);

    /**
     * 添加不动产证书信息
     * @param certDto
     * @return
     */
    boolean addCertificate(CertDto certDto,String ip,String sljg,String zh,String yhm);

    /**
     * 按时间分组,获取电子证书,电子证明,其他证明的数量
     * @param djjgdm
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> selectNumByDay(String djjgdm, Date startDate, Date endDate);

    /**
     * 证照查验
     * @param zzmc 证照名称
     * @param zh 证号
     * @param multipartFile 文件
     * @param yh 用户
     * @param jg 机构
     * @param ip ip请求地址
     * @return
     */
    RestResult verifyCertificate(RestResult restResult,String zzmc, String zh, String file, String yh, String jg, String ip);
}
