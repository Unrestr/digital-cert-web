package com.greatmap.digital.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.greatmap.digital.dto.SealDto;
import com.greatmap.digital.dto.SealRuleDto;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.digital.mapper.DcSealRuleMapper;
import com.greatmap.digital.model.DcCertSealRule;
import com.greatmap.digital.model.DcSealRule;
import com.greatmap.digital.service.DcCertSealRuleService;
import com.greatmap.digital.service.DcSealRuleService;
import com.greatmap.digital.util.CodeGeneratorUtil;
import com.greatmap.digital.util.http.HttpUtils;
import com.greatmap.framework.core.util.StrUtil;
import com.greatmap.framework.web.controller.RestResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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
public class DcSealRuleServiceImpl extends ServiceImpl<DcSealRuleMapper, DcSealRule> implements DcSealRuleService {

    @Value("${sign.hbca.http}")
    private String hbcaess_http;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Autowired
    private DcCertSealRuleService dcCertSealRuleService;

    /**
     * 查询规则列表
     *
     * @param page
     * @param qzgzmc
     * @param qxdm
     * @return
     */
    @Override
    public Page<DcSealRule> getSealTemplatePageList(Page<DcSealRule> page, String qzgzmc, String qxdm,String zt) {
        if (StringUtils.isBlank(qxdm)) {
            throw new DigitalException(String.format("区县代码不能为空！"));
        }
        Wrapper<DcSealRule> we = wrapper(qzgzmc, qxdm);
        if(StringUtils.isNotBlank(zt)){
            we.eq("ZT",zt);
        }
        return selectPage(page, we);
    }

    /**
     * 新增规则
     *
     * @param sealRuleDto
     * @return
     */
    @Override
    public String addSealTemplate(RestResult restResult, SealRuleDto sealRuleDto) {
        //参数校验
        verification(sealRuleDto);
        Wrapper<DcSealRule> we = wrapper(sealRuleDto.getTemplateName(), sealRuleDto.getQxdm());
        List<DcSealRule> qzgzmcList = selectList(we);
        if (CollectionUtils.isNotEmpty(qzgzmcList)) {
            throw new DigitalException(String.format("【%s】规则名称已存在", sealRuleDto.getTemplateName()));
        }
        Map<String, Object> params = JSON.parseObject(JSONObject.toJSONString(sealRuleDto), new TypeReference<Map<String, Object>>() {
        });
        String data = HttpUtils.httpPost2(hbcaess_http + URL_ADD_SEAL_TEMPLATE, params);
        String re = generateResult(restResult, data);
        if (restResult.isSuccess()) {
            DcSealRule dcSealRule = new DcSealRule();
            dcSealRule.setId(codeGeneratorUtil.generateId());
            copyBean(sealRuleDto, dcSealRule);
            //湖北CA qzxx存值处理
       /*     if (sealRuleDto.getCalx().equals(CaEnums.HBCA.toString())) {
                String[] split = re.split(":");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gzh", split[1]);
                dcSealRule.setQzxx(jsonObject.toString());
            }*/
            String[] split = re.split(":");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gzh", split[1]);
            dcSealRule.setQzxx(jsonObject.toString());
            //设置默认状态 1 正常, 0 注销
            dcSealRule.setZt("1");
            insert(dcSealRule);
            restResult.setMessage("添加成功!");
        }
        return re;
    }

    /**
     * 删除规则
     *
     * @param restResult
     * @param qzgzmc
     * @param qxdm
     * @return
     */
    @Override
    public String logoffSealTemplate(RestResult restResult, String qzgzid, String qzgzmc, String qxdm) {
        Wrapper<DcCertSealRule> dwe = wrapper(qzgzmc, qxdm);
        List<DcCertSealRule> dcCertSealRuleList = dcCertSealRuleService.selectList(dwe.eq("QZGZID", qzgzid));
        if (CollectionUtils.isNotEmpty(dcCertSealRuleList) && dcCertSealRuleList.stream().filter(item -> item.getZt().equals("1")).count() > 0) {
            throw new DigitalException(String.format("规则正在启用，无法注销！"));
        }
        Wrapper<DcSealRule> we = wrapper(qzgzmc, qxdm);
        DcSealRule dcSealRule = selectOne(we.eq("ID", qzgzid));
        Map<String, Object> map = Maps.newHashMap();
      /*  if (dcSealRule.getCalx().equals(CaEnums.HBCA.toString())) {
            String qzxx = dcSealRule.getQzxx();
            String gzh = JSONObject.parseObject(qzxx).getString("gzh");
            map.put("templateId", gzh);
        }*/
        String qzxx = dcSealRule.getQzxx();
        if(StringUtils.isNotBlank(qzxx)){
            String gzh = JSONObject.parseObject(qzxx).getString("gzh");
            map.put("templateId", gzh);
        }
        String data = HttpUtils.httpPost2(hbcaess_http + URL_OFF_SEAL_TEMPLATE, map);
        String re = generateResult(restResult, data);
        if (restResult.isSuccess()) {
            DcSealRule dcSealRule1 = new DcSealRule();
            dcSealRule1.setZt("0");
            update(dcSealRule1,we.eq("ID", qzgzid));
        }
        return re;
    }

    /**
     * 修改规则
     *
     * @param sealRuleDto
     * @return
     */
    @Override
    public String updateSealTemplate(RestResult restResult, SealRuleDto sealRuleDto) {
        //参数校验
        verification(sealRuleDto);
        Wrapper<DcSealRule> cwe = wrapper(sealRuleDto.getTemplateName(), sealRuleDto.getQxdm());
        DcSealRule dcSealRule = selectOne(cwe.ne("ID", sealRuleDto.getTemplateId()));
        if (dcSealRule != null) {
            throw new DigitalException(String.format("【%s】规则名称已存在", sealRuleDto.getTemplateName()));
        }
        DcSealRule sealRule = selectById(Long.valueOf(sealRuleDto.getTemplateId()));
        //取消CA参数
        /*if (sealRuleDto.getCalx().equals(CaEnums.HBCA.toString())) {
            String qzxx = sealRule.getQzxx();
            String gzh = JSONObject.parseObject(qzxx).getString("gzh");
            sealRuleDto.setTemplateId(gzh);
        }*/
        String qzxx = sealRule.getQzxx();
        String gzh = JSONObject.parseObject(qzxx).getString("gzh");
        sealRuleDto.setTemplateId(gzh);
        Map<String, Object> params = JSON.parseObject(JSONObject.toJSONString(sealRuleDto), new TypeReference<Map<String, Object>>() {
        });
        String data = HttpUtils.httpPost2(hbcaess_http + URL_UPDATE_SEAL_TEMPLATE, params);
        String re = generateResult(restResult, data);
        if (restResult.isSuccess()) {
            copyBean(sealRuleDto, sealRule);
            updateById(sealRule);
        }
        return re;
    }

    /**
     * 获取数字签章列表
     *
     * @param restResult
     * @param pageNo
     * @param pageSize
     * @param sealId
     * @param sealName
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Page<SealDto> getSealPageList(RestResult restResult, Integer pageNo, Integer pageSize, String sealId, String sealName, String status, String bindingState , Date startDate, Date endDate) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        params.put("sealId", sealId);
        params.put("sealName", sealName);
        params.put("status", status);
        params.put("bindingState", bindingState);
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (startDate != null) {
            params.put("startDate", sdf.format(startDate));
        }
        if (endDate != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(endDate);
            calendar.add(Calendar.DATE,1);
            endDate=calendar.getTime();
            params.put("endDate", sdf.format(endDate));
        }
        String data = HttpUtils.httpPost2(hbcaess_http + URL_GET_SEAL_LIST, params);
        data = generateResult(restResult, data);
        Page<SealDto> sealDtoPage = new Page<>();
        generatePage(sealDtoPage, data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        if(null == jsonObject){
            return sealDtoPage;
        }
        List<SealDto> list = JSONObject.parseArray(jsonObject.getString("list"), SealDto.class);
        sealDtoPage.setRecords(list);
        return sealDtoPage;
    }

    /**
     * 生成结果对象
     *
     * @param restResult
     * @param values
     * @return 返回data数据
     */
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
     * 新增修改规则时参数校验
     *
     * @param sealRuleDto
     */
    public void verification(SealRuleDto sealRuleDto) {
        Integer offsetMin = -499999;
        Integer offsetMax = 499999;
        if ("positionType3".equals(sealRuleDto.getSealPositionType()) || "positionType7".equals(sealRuleDto.getSealPositionType())) {
            offsetMax = 24999;
            offsetMin = -25000;
        }
        if (sealRuleDto == null) {
            throw new DigitalException(String.format("请求参数不能为空！"));
        }
        if (StringUtils.isEmpty(sealRuleDto.getTemplateName()) || StrUtil.isEmpty(sealRuleDto.getSealPositionType())) {
            throw new DigitalException(String.format("规则名称、位置类型不能为空！"));
        }
        if (sealRuleDto.getX() != null) {
            if (sealRuleDto.getX() > 49999 || sealRuleDto.getX() < 1) {
                throw new DigitalException(String.format("横向坐标超出范围！"));
            }
        }
        if (sealRuleDto.getY() != null) {
            if (sealRuleDto.getY() > 49999 || sealRuleDto.getY() < 1) {
                throw new DigitalException(String.format("纵向坐标超出范围！"));
            }
        }
        if (sealRuleDto.getOffsetY() != null) {
            if (sealRuleDto.getOffsetY() > offsetMax || sealRuleDto.getOffsetY() < offsetMin) {
                throw new DigitalException(String.format("纵向偏移量超出范围！"));
            }
        }
        if (sealRuleDto.getQxdm() == null || sealRuleDto.getQxmc() == null) {
            throw new DigitalException(String.format("区县代码、区县名称不能为空！"));
        }
     /*   if (sealRuleDto.getCalx() == null) {
            throw new DigitalException(String.format("CA类型不能为空！"));
        }*/
    }

    /**
     * 封装分页结果
     *
     * @param page
     * @param data
     * @param <T>
     * @return
     */
    private <T> Page<T> generatePage(Page<T> page, String data) {
        if (page == null || StringUtils.isEmpty(data)) {
            return page;
        }
        JSONObject jsonObject = JSONObject.parseObject(data);
        page.setTotal(jsonObject.getInteger("total"));
        page.setCurrent(jsonObject.getInteger("pageNum"));
        page.setSize(jsonObject.getInteger("pageSize"));
        return page;
    }

    public void copyBean(SealRuleDto sealRuleDto, DcSealRule sealRule) {
        sealRule.setQzgzmc(sealRuleDto.getTemplateName());
        sealRule.setQzgzlx(sealRuleDto.getSealPositionType());
        sealRule.setSealid(sealRuleDto.getSealId());
        sealRule.setLeftx(sealRuleDto.getX());
        sealRule.setLefty(sealRuleDto.getY());
        sealRule.setPage(sealRuleDto.getSpacePage());
        sealRule.setCalx(sealRuleDto.getCalx());
        sealRule.setQxdm(sealRuleDto.getQxdm());
        sealRule.setQxmc(sealRuleDto.getQxmc());
    }

    public <T> Wrapper<T> wrapper(String qzgzmc, String qxdm) {
        Wrapper<T> we = new EntityWrapper<>();
        if (StringUtils.isNotBlank(qzgzmc)) {
            we.eq("QZGZMC", qzgzmc);
        }
        if (StringUtils.isNotBlank(qxdm)) {
            we.eq("QXDM", qxdm);
        }
        return we;
    }
}
