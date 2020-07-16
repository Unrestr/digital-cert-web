package com.greatmap.digital.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.greatmap.digital.model.DcSealLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志信息 Mapper 接口
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Mapper
public interface DcSealLogMapper extends BaseMapper<DcSealLog> {



    List<Map<String, Object>> statisticInspectionQuantity(@Param("czxq")String czxq);

    List<String> selectCzxqList();

    List<Map<String, Object>> statisticCertInfoOperation(@Param("czxqList")List<String> czxqList,@Param("djjg") String djjg,@Param("startDate") Date startDate, @Param("endDate")Date endDate);
}
