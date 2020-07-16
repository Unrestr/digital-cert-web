package com.greatmap.digital.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.greatmap.digital.mapper.StatisticMapper;
import com.greatmap.digital.model.DcCertInfo;
import com.greatmap.digital.model.DcSealLog;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计实现类
 *
 * @author gaorui
 * @create 2020-01-09 11:13
 */
@Service
public class StatisticsService {

    @Autowired(required = false)
    private StatisticMapper statisticMapper;

    @Autowired
    private DcCertInfoService dcCertInfoService;

    @Autowired
    private DcSealLogService dcSealLogService;


    /**
     * 隔天凌晨
     */
    private static  LocalDateTime lastDayOfDay =  LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

    /**
     * 本周最后一天
     */
    private static LocalDateTime lastDayOfWeek = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(7 - LocalDateTime.now().getDayOfWeek().getValue());

    /**
     * 本月最后一天
     */
    private static LocalDateTime lastDayOfMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth());

    /**
     * 本年最后一天
     */
    private static LocalDateTime lastDayOfYear = LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear());

    /**
     *
     * @param djjgdm
     * @param startDate
     * @param endDate
     * @param status 0实发,1有效
     * @param zzfl 1证书, 2证明
     * @return
     */
    public Integer selectRealCertificateNum(String djjgdm, Date startDate, Date endDate,Integer status,Integer zzfl) {
        EntityWrapper<DcCertInfo> dcCertInfoEntityWrapper = new EntityWrapper<>();
        if(StringUtils.isNotBlank(djjgdm)){
            dcCertInfoEntityWrapper.eq("djjgdm",djjgdm);
        }
        if(null != endDate){
            dcCertInfoEntityWrapper.le("bfsj",endDate);
        }
        if(null != startDate){
            dcCertInfoEntityWrapper.ge("bfsj",startDate);
        }
        if(1==status){
            dcCertInfoEntityWrapper.isNull("zzzxrq");
        }
        if(1==zzfl){
            dcCertInfoEntityWrapper.eq("zzfl",1);
        }
        if(2==zzfl){
            dcCertInfoEntityWrapper.eq("zzfl",2);
        }
        return dcCertInfoService.selectCount(dcCertInfoEntityWrapper);
    }

    public List<Map<String, Object>> selectNumByDay(String djjgdm, Date startDate, Date endDate) {
        return dcCertInfoService.selectNumByDay(djjgdm,startDate,endDate);
    }

    public List<Map<String, Object>> statisticCertInfoOperation(String djjg, Date startDate, Date endDate) {
        return dcSealLogService.statisticCertInfoOperation(djjg,startDate,endDate);
    }

    public List<Map<String,Object>> statisticInspectionQuantity(String czxq) {
        //获取某操作的所有日志记录量
        EntityWrapper<DcSealLog> dcSealLogEntityWrapper = new EntityWrapper<>();
        dcSealLogEntityWrapper.eq("czxq",czxq);
        int total = dcSealLogService.selectCount(dcSealLogEntityWrapper);
        if(total==0){
            return null;
        }
        List<Map<String, Object>> list = dcSealLogService.statisticInspectionQuantity(czxq);
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        if(list!=null && list.size()>0){
            for(Map<String,Object> map:list){
                BigDecimal num = (BigDecimal)map.get("NUM");
                String result = numberFormat.format(num.doubleValue() / total * 100)+"%";
                map.put("proportion",result);
            }
        }
        return list;
    }
}
