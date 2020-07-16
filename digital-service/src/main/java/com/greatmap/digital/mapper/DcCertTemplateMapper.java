package com.greatmap.digital.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.greatmap.digital.model.DcCertTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 模板信息表。第三方系统传值json数据电子证照通过json打印证照 然后盖章。需要通过模板标识找对应模板打印再盖章 Mapper 接口
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Mapper
public interface DcCertTemplateMapper extends BaseMapper<DcCertTemplate> {

}
