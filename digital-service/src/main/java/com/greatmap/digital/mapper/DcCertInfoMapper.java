package com.greatmap.digital.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.dto.CertBaseDto;
import com.greatmap.digital.dto.CertInfoDto;
import com.greatmap.digital.dto.CertificateDto;
import com.greatmap.digital.model.DcCertInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Mapper
public interface DcCertInfoMapper extends BaseMapper<DcCertInfo> {

    /**
     * 获取证照分页信息
     * @param page
     * @param bfjg
     * @param qlr
     * @param zt
     * @param zzlx
     * @param zzbh
     * @return
     */
    List<CertInfoDto> findCertInfoPage(Page<CertInfoDto> page, @Param("bfjg")String bfjg, @Param("qlr")String qlr, @Param("zt")String zt, @Param("zzlx")String zzlx, @Param("zzbh")String zzbh);


    /**
     * 根据证照ID获取证照基本信息
     * @param id
     * @return
     */
    CertBaseDto getBasicCertInfo(@Param("id") String id);

    /**
     * 按时间分组,获取电子证书,电子证明,其他证明的数量
     * @param djjgdm
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> selectNumByDay(@Param("djjgdm")String djjgdm,@Param("startDate") Date startDate, @Param("endDate")Date endDate);

    /**
     * 获取证照信息
     * @param zh
     * @param qlrmc
     * @param qlrzjh
     * @return
     */
    List<CertificateDto> queryCertificateList(@Param("zh") String zh, @Param("qlrmc") String qlrmc, @Param("qlrzjh") String qlrzjh);

    /**
     * 获取证照信息 带文件
     * @param zh
     * @param qlrmc
     * @param qlrzjh
     * @return
     */
    List<CertificateDto> queryCertificateListEx(@Param("zh")String zh,  @Param("qlrmc")String qlrmc, @Param("qlrzjh")String qlrzjh);
}
