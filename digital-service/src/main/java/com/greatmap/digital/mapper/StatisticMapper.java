package com.greatmap.digital.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.greatmap.digital.model.DcCertInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  统计Mapper 接口
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Mapper
public interface StatisticMapper{

    /**
     * 统计每日/月/年 指定证照名称的不同状态证照数量
     * @param qxdm 区县代码
     * @param zzmc 证照名称
     * @param type 统计类型
     * @return
     */
    List<Map<String, Long>> statictisEveryType(@Param("qxdm") String qxdm, @Param("zzmc") String zzmc, @Param("type") String type);
}
