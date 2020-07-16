--2020/6/25 高锐 电子证照3.0数据库初始化表
/*==============================================================*/
/* Table: DC_CERT_ASSOTYPE 关联关系表                                     */
/*==============================================================*/
create table DC_CERT_ASSOTYPE
(
   id                 VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   zh                 VARCHAR2(32)         not null,
   glzh               VARCHAR2(255)        not null,
   glzhbh             VARCHAR2(255) not null,
   gllx               VARCHAR2(10) not null,
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
   version               NUMBER(32) ,
   constraint PK_DC_CERT_ASSOTYPE primary key (id)
);

comment on table DC_CERT_ASSOTYPE is
'关了证书信息';

comment on column DC_CERT_ASSOTYPE.id is
'ID';

comment on column DC_CERT_ASSOTYPE.zzbh is
'证照编号';

comment on column DC_CERT_ASSOTYPE.zh is
'证号';

comment on column DC_CERT_ASSOTYPE.glzh is
'关联证号';

comment on column DC_CERT_ASSOTYPE.glzhbh is
'关联证照编号';

comment on column DC_CERT_ASSOTYPE.gllx is
'关联类型(共有关联 沿革关联)';

/*==============================================================*/
/* Table: DC_CERT_FILE   文件信息表                                       */
/*==============================================================*/
create table DC_CERT_FILE
(
   id                 VARCHAR2(32)         not null,
   wjid               VARCHAR2(32)         not null,
   wjmc               VARCHAR2(32)         not null,
   xdml               VARCHAR2(32)         not null,
   ysdz               VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   zzid               VARCHAR2(32),
   zzmc               VARCHAR2(32),
   scsj               DATE,
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_CERT_FILE primary key (id)
);

comment on table DC_CERT_FILE is
'文件信息表';

comment on column DC_CERT_FILE.id is
'ID';

comment on column DC_CERT_FILE.wjid is
'文件id';

comment on column DC_CERT_FILE.wjmc is
'文件名称';

comment on column DC_CERT_FILE.xdml is
'相对路径';

comment on column DC_CERT_FILE.ysdz is
'映射地址';

comment on column DC_CERT_FILE.zzbh is
'证照编号';

comment on column DC_CERT_FILE.zzid is
'证照id';

comment on column DC_CERT_FILE.zzmc is
'证照名称';

comment on column DC_CERT_FILE.scsj is
'文件上传时间';

/*==============================================================*/
/* Table: DC_CERT_INFO  证照基础信息表                                        */
/*==============================================================*/
create table DC_CERT_INFO
(
   id                 VARCHAR2(50)         not null,
   zzbsm                VARCHAR2(50),
   ywh                VARCHAR2(50),
   zzbh               VARCHAR2(10),
   zzmc               VARCHAR2(50)         not null,
   djjg               VARCHAR2(50)         not null,
   djsj               DATE,
   djyy               VARCHAR2(255),
   djjgdm             VARCHAR2(25),
   zh                 VARCHAR2(25),
   zzfl               VARCHAR2(25),
   qxdm               VARCHAR2(20),
   qxmc               VARCHAR2(20),
   zzzxrq             DATE,
   zxyy               VARCHAR2(100),
   zxjg               VARCHAR2(100),
   zxjgdm             VARCHAR2(100),
   bfry               VARCHAR2(100),
   bfsj               DATE,
   zt                 VARCHAR2(10),
   zzly               VARCHAR2(25),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_CERT_INFO primary key (id)
);

comment on table DC_CERT_INFO is
'证照基础信息表  证照和单元信息  权利人信息存在1:N关系      权利信息1:1';

comment on column DC_CERT_INFO.id is
'ID';

comment on column DC_CERT_INFO.zzbsm is
'证照标识码';

comment on column DC_CERT_INFO.zzbh is
'证照编号';

comment on column DC_CERT_INFO.djjg is
'登记机构';

comment on column DC_CERT_INFO.djsj is
'登记时间';

comment on column DC_CERT_INFO.djyy is
'登记原因';

comment on column DC_CERT_INFO.djjgdm is
'登记机构代码';

comment on column DC_CERT_INFO.zh is
'证号';

comment on column DC_CERT_INFO.zzfl is
'证照分类';

comment on column DC_CERT_INFO.qxdm is
'区县代码';

comment on column DC_CERT_INFO.qxmc is
'区县名称';

comment on column DC_CERT_INFO.zzzxrq is
'证照注销日期';

comment on column DC_CERT_INFO.zzzxyy is
'证照注销原因';

comment on column DC_CERT_INFO.zxjg is
'证照注销机构';

comment on column DC_CERT_INFO.zxjgdm is
'证照注销机构代码';

comment on column DC_CERT_INFO.bfsj is
'颁发时间';

comment on column DC_CERT_INFO.bfry is
'颁发人员';

comment on column DC_CERT_INFO.zt is
'状态';

comment on column DC_CERT_INFO.zzly is
'证照来源';

comment on column DC_CERT_INFO.zzbsm is
'证照标识码';

comment on column DC_CERT_INFO.zzmc is
'证照名称';

comment on column DC_CERT_INFO.zxyy is
'注销原因';

/*==============================================================*/
/* Table: DC_CERT_MAP  附图信息                                          */
/*==============================================================*/
create table DC_CERT_MAP
(
   id                 VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   tpmc               VARCHAR2(32),
   tpxx              VARCHAR2(32),
   jzd                VARCHAR2(100),
   bdcdyh             VARCHAR2(32),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_CERT_MAP primary key (id)
);

comment on table DC_CERT_MAP is
'附图信息 宗地图和房产分户图';

comment on column DC_CERT_MAP.id is
'ID';

comment on column DC_CERT_MAP.zzbh is
'证照编号';

comment on column DC_CERT_MAP.tpmc is
'图片名称';

comment on column DC_CERT_MAP.tpxx is
'图片信息';

comment on column DC_CERT_MAP.jzd is
'界址点';

comment on column DC_CERT_MAP.bdcdyh is
'不动产单元号';


create table DC_CERT_SEAL_RULE
(
   id                 VARCHAR2(32)         not null,
   zzmc               VARCHAR2(32)         not null,
   zzlx               VARCHAR2(32)         not null,
   qzlx               VARCHAR2(32)         not null,
   qzgzmc             VARCHAR2(32)         not null,
   qzgzid             VARCHAR2(32)         not null,
   qxdm               VARCHAR2(32),
   qxmc               VARCHAR2(32)         not null,
   zt                 VARCHAR2(32),
   bz                 VARCHAR2(32),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_CERT_SEAL_RULE primary key (id)
);

comment on table DC_CERT_SEAL_RULE is
'证照规则表';

comment on column DC_CERT_SEAL_RULE.id is
'ID';

comment on column DC_CERT_SEAL_RULE.zzmc is
'证照名称';

comment on column DC_CERT_SEAL_RULE.zzlx is
'证照类型';

comment on column DC_CERT_SEAL_RULE.qzlx is
'签章类型';

comment on column DC_CERT_SEAL_RULE.qzgzmc is
'签章规则名称';

comment on column DC_CERT_SEAL_RULE.qzgzmcid is
'签章规则名称id';

comment on column DC_CERT_SEAL_RULE.qxdm is
'区县代码';

comment on column DC_CERT_SEAL_RULE.qxmc is
'区县名称';

comment on column DC_CERT_SEAL_RULE.zt is
'状态';

comment on column DC_CERT_SEAL_RULE.qzgzid is
'签章规则id';

comment on column DC_CERT_SEAL_RULE.bz is
'备注';


/*==============================================================*/
/* Table: DC_CERT_TEMPLATE  证照模板                                    */
/*==============================================================*/
create table DC_CERT_TEMPLATE
(
   id                 VARCHAR2(32)         not null,
   mbbs               VARCHAR2(32)         not null,
   zzmc               VARCHAR2(32)         not null,
   qxdm               VARCHAR2(32 BYTE),
   qxmc               VARCHAR2(32)         not null,
   zt                 VARCHAR2(32),
   bz                 VARCHAR2(32),
   mbsl               VARCHAR2(100 BYTE),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_CERT_TEMPLATE primary key (id)
);

comment on table DC_CERT_TEMPLATE is
'模板信息表。第三方系统传值json数据电子证照通过json打印证照 然后盖章。需要通过模板标识找对应模板打印再盖章';

comment on column DC_CERT_TEMPLATE.id is
'ID';

comment on column DC_CERT_TEMPLATE.mbbs is
'模板名称';

comment on column DC_CERT_TEMPLATE.zzmc is
'证照名称';

comment on column DC_CERT_TEMPLATE.qxdm is
'区县代码';

comment on column DC_CERT_TEMPLATE.zt is
'状态';

comment on column DC_CERT_TEMPLATE.qxmc is
'区县名称';

comment on column DC_CERT_TEMPLATE.bz is
'备注';

comment on column DC_CERT_TEMPLATE.mbsl is
'模板示例';

*==============================================================*/
/* Table: DC_PROPERTY_PRINT 证书打印信息表                                    */
/*==============================================================*/
create table DC_PROPERTY_PRINT
(
   id                 VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   jc                 VARCHAR2(32)         not null,
   nf                 VARCHAR2(10),
   x                  VARCHAR2(10)          not null,
   djsj               DATE,
   bh                 VARCHAR2(32),
   qlr                VARCHAR2(32),
   gyqk               VARCHAR2(32),
   zl                 VARCHAR2(255),
   bdcdyh             VARCHAR2(100),
   qllx               VARCHAR2(50),
   qlxz               VARCHAR2(32),
   yt                 VARCHAR2(32),
   mj                 NUMBER(32),
   syqs               DATE,
   qlqtqk             VARCHAR2(255),
   fj                 VARCHAR2(255),
   cxewm              VARCHAR2(255),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_PROPERTY_PRINT primary key (id)
);

comment on table DC_PROPERTY_PRINT is
'证书打印信息';

comment on column DC_PROPERTY_PRINT.id is
'ID';

comment on column DC_PROPERTY_PRINT.zzbh is
'证照编号';

comment on column DC_PROPERTY_PRINT.jc is
'简称';

comment on column DC_PROPERTY_PRINT.nf is
'年份';

comment on column DC_PROPERTY_PRINT.x is
'县';

comment on column DC_PROPERTY_PRINT.djsj is
'登记时间';

comment on column DC_PROPERTY_PRINT.bh is
'编号';

comment on column DC_PROPERTY_PRINT.qlr is
'权利人';

comment on column DC_PROPERTY_PRINT.gyqk is
'共有情况';

comment on column DC_PROPERTY_PRINT.zl is
'坐落';

comment on column DC_PROPERTY_PRINT.bdcdyh is
'不动产单元号';

comment on column DC_PROPERTY_PRINT.qllx is
'权利类型';

comment on column DC_PROPERTY_PRINT.qllz is
'权利性质';

comment on column DC_PROPERTY_PRINT.yt is
'用途';

comment on column DC_PROPERTY_PRINT.mj is
'面积';

comment on column DC_PROPERTY_PRINT.syqx is
'使用期限';

comment on column DC_PROPERTY_PRINT.qlqtqk is
'权利其他状况';

comment on column DC_PROPERTY_PRINT.qlxz is
'权利性质';

comment on column DC_PROPERTY_PRINT.syqx is
'使用期限';

comment on column DC_PROPERTY_PRINT.fj is
'附记';

comment on column DC_PROPERTY_PRINT.cxewm is
'查询二维码';



/*==============================================================*/
/* Table: DC_REGISTRATION_PRINT  证明打印信息表                               */
/*==============================================================*/
create table DC_REGISTRATION_PRINT
(
   id                 VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   jc                 VARCHAR2(32)         not null,
   nf                 VARCHAR2(10),
   x                  VARCHAR2(10)          not null,
   djsj               DATE,
   bh                 VARCHAR2(50),
   zmqlhsx            VARCHAR2(255),
   qlr                VARCHAR2(32),
   ywr                VARCHAR2(32),
   zl                 VARCHAR2(255),
   bdcdyh             VARCHAR2(50),
   qt                 VARCHAR2(255),
   fj                 VARCHAR2(255),
   cxewm              VARCHAR2(255),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_REGISTRATION_PRINT primary key (id)
);

comment on table DC_REGISTRATION_PRINT is
'证明打印信息';

comment on column DC_REGISTRATION_PRINT.id is
'ID';

comment on column DC_REGISTRATION_PRINT.zzbh is
'证照编号';

comment on column DC_REGISTRATION_PRINT.jc is
'简称';

comment on column DC_REGISTRATION_PRINT.nf is
'年份';

comment on column DC_REGISTRATION_PRINT.x is
'县';

comment on column DC_REGISTRATION_PRINT.bh is
'编号';

comment on column DC_REGISTRATION_PRINT.zmqlhsx is
'证明权利或事项';

comment on column DC_REGISTRATION_PRINT.qlr is
'权利人(申请人)';

comment on column DC_REGISTRATION_PRINT.ywr is
'义务人';

comment on column DC_REGISTRATION_PRINT.zl is
'坐落';

comment on column DC_REGISTRATION_PRINT.djsj is
'登记时间';

comment on column DC_REGISTRATION_PRINT.bdcdyh is
'不动产单元号';

comment on column DC_REGISTRATION_PRINT.qt is
'其他';

comment on column DC_REGISTRATION_PRINT.fj is
'附记';

comment on column DC_REGISTRATION_PRINT.cxewm is
'查询二维码';

/*==============================================================*/
/* Table: DC_RIGHTHOLDER   权利人信息                                     */
/*==============================================================*/
create table DC_RIGHTHOLDER
(
   id                 VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   zh                 VARCHAR2(32)         not null,
   mc                 VARCHAR2(10),
   gx                 VARCHAR2(32)         not null,
   zjlx               VARCHAR2(32),
   zjh                VARCHAR2(32),
   gyfs               VARCHAR2(10),
   gybl               VARCHAR2(10),
   sfcz               VARCHAR2(10),
   lxdh               VARCHAR2(10 BYTE),
   gyqk               VARCHAR2(255 BYTE),
   ryfl               VARCHAR2(10 BYTE),
   bdcdyh             VARCHAR2(32 BYTE),
   gyr                VARCHAR2(50 BYTE),
   gyrzjh             VARCHAR2(255 BYTE),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_RIGHTHOLDER primary key (id)
);

comment on table DC_RIGHTHOLDER is
'权利人信息 ';

comment on column DC_RIGHTHOLDER.id is
'ID';

comment on column DC_RIGHTHOLDER.zzbh is
'证照编号';

comment on column DC_RIGHTHOLDER.zh is
'证号';

comment on column DC_RIGHTHOLDER.mc is
'名称';

comment on column DC_RIGHTHOLDER.gx is
'关系';

comment on column DC_RIGHTHOLDER.zjlx is
'证件类型';

comment on column DC_RIGHTHOLDER.zjh is
'权利人证件号';

comment on column DC_RIGHTHOLDER.gyfs is
'共有方式';

comment on column DC_RIGHTHOLDER.gybl is
'共有比例';

comment on column DC_RIGHTHOLDER.sfcz is
'是否持证';

comment on column DC_RIGHTHOLDER.lxdh is
'联系电话';

comment on column DC_RIGHTHOLDER.gyqk is
'共有情况';

comment on column DC_RIGHTHOLDER.bdcdyh is
'不动产单元号';

comment on column DC_RIGHTHOLDER.lxdh is
'联系电话';

comment on column DC_RIGHTHOLDER.ryfl is
'人员分类';

comment on column DC_RIGHTHOLDER.gyr is
'共有人';

comment on column DC_RIGHTHOLDER.gyrzjh is
'共有人证件号';
/*==============================================================*/
/* Table: DC_RIGHT_INFO                                         */
/*==============================================================*/
create table DC_RIGHT_INFO
(
   id                 VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   zzlx               VARCHAR2(32)         not null,
   qlxz               VARCHAR2(10),
   syqqssj            DATE                 not null,
   syqjssj            DATE,
   qlqtzk             VARCHAR2(255),
   tdqllx             VARCHAR2(32),
   tdqlxz             VARCHAR2(32),
   tdqlrxz            VARCHAR2(32),
   fj                 VARCHAR2(50),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_RIGHT_INFO primary key (id)
);

comment on table DC_RIGHT_INFO is
'权利信息';

comment on column DC_RIGHT_INFO.id is
'ID';

comment on column DC_RIGHT_INFO.zzbh is
'证照编号';

comment on column DC_RIGHT_INFO.zzlx is
'证照类型';

comment on column DC_RIGHT_INFO.qlxz is
'权利性质';

comment on column DC_RIGHT_INFO.syqqssj is
'使用权起始时间';

comment on column DC_RIGHT_INFO.syqjssj is
'使用权结束时间';

comment on column DC_RIGHT_INFO.qlqtzk is
'权利其他状况';

comment on column DC_RIGHT_INFO.tdqlxz is
'土地权利性质';

comment on column DC_RIGHT_INFO.tdqllx is
'土地权利类型';

comment on column DC_RIGHT_INFO.tdqlrxz is
'土地权利人性质(1：全体业主共有 2：个人所有)';

comment on column DC_RIGHT_INFO.fj is
'附记';

/*==============================================================*/
/* Table: DC_SEAL_LOG                                           */
/*==============================================================*/
create table DC_SEAL_LOG
(
   id                 VARCHAR2(32)         not null,
   qqly               VARCHAR2(32)         not null,
   qqcz               VARCHAR2(32)         not null,
   ssjg               VARCHAR2(32),
   ip                 VARCHAR2(32)         not null,
   qqsj               VARCHAR2(32)         not null,
   xyzt               VARCHAR2(32)         not null,
   constraint PK_DC_SEAL_LOG primary key (id)
);

comment on table DC_SEAL_LOG is
'日志信息';

comment on column DC_SEAL_LOG.id is
'ID';

comment on column DC_SEAL_LOG.qqly is
'请求来源';

comment on column DC_SEAL_LOG.qqcz is
'请求操作';

comment on column DC_SEAL_LOG.ssjg is
'所属机构';

comment on column DC_SEAL_LOG.ip is
'IP地址';

comment on column DC_SEAL_LOG.qqsj is
'请求时间';

comment on column DC_SEAL_LOG.xyzt is
'响应结果';

/*==============================================================*/
/* Table: DC_SEAL_RULE                                          */
/*==============================================================*/
create table DC_SEAL_RULE
(
   id                 VARCHAR2(32)         not null,
   gzmc               VARCHAR2(32)         not null,
   qzgzlx             VARCHAR2(32)         not null,
   qzid               VARCHAR2(32),
   hzpy               VARCHAR2(32),
   zzpy               VARCHAR2(32)         not null,
   qzgd               VARCHAR2(32)         not null,
   qzkd               VARCHAR2(32)         not null,
   ym                 VARCHAR2(35),
   qzxx               VARCHAR2(32)         not null,
   qxdm               VARCHAR2(32),
   qxmc               VARCHAR2(32),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_SEAL_RULE primary key (id)
);

comment on table DC_SEAL_RULE is
'规则详情';

comment on column DC_SEAL_RULE.id is
'ID';

comment on column DC_SEAL_RULE.gzmc is
'规则名称';

comment on column DC_SEAL_RULE.qzgzlx is
'签章规则类型';

comment on column DC_SEAL_RULE.qzid is
'签章ID';

comment on column DC_SEAL_RULE.hzpy is
'横轴偏移';

comment on column DC_SEAL_RULE.zzpy is
'纵轴偏移';

comment on column DC_SEAL_RULE.qzgd is
'签章高度';

comment on column DC_SEAL_RULE.qzkd is
'签章宽度';

comment on column DC_SEAL_RULE.ym is
'页码';

comment on column DC_SEAL_RULE.qzxx is
'签章信息';

comment on column DC_SEAL_RULE.qxdm is
'区县代码';

comment on column DC_SEAL_RULE.qxmc is
'区县名称';


/*==============================================================*/
/* Table: DC_UNIT_INFO 单元信息表                                         */
/*==============================================================*/
create table DC_UNIT_INFO
(
   id                 VARCHAR2(32)         not null,
   zzbh               VARCHAR2(32)         not null,
   zh                 VARCHAR2(32)         not null,
   bdcdjzmh           VARCHAR2(50 BYTE),
   bdcdyh             VARCHAR2(32),
   zl                 VARCHAR2(32)         not null,
   yt                 VARCHAR2(32),
   tdyt               VARCHAR2(32 BYTE),
   mj                 NUMBER(32),
   tdsyqmj            NUMBER(32),
   ftmj               NUMBER(32),
   tnmj               NUMBER(32),
   mjdw               VARCHAR2(20),
   ytdm               VARCHAR2(32),
   tdytdm             VARCHAR2(32),
   tdmjdw             VARCHAR2(32),
   mjdwdm             VARCHAR2(32),
   tdmjdwdm           VARCHAR2(32),
   zt                 VARCHAR2(10),
   cjr                 VARCHAR2(32)         ,
   gxr                 VARCHAR2(32)        ,
   cjsj               DATE  ,
   gxsj               DATE ,
    version               NUMBER(32) ,
   constraint PK_DC_UNIT_INFO primary key (id)
);

comment on table DC_UNIT_INFO is
'单元信息';

comment on column DC_UNIT_INFO.id is
'ID';

comment on column DC_UNIT_INFO.zzbh is
'证照编号';

comment on column DC_UNIT_INFO.zh is
'证号';

comment on column DC_UNIT_INFO.bdcdjzmh is
'不动产登记证明号';

comment on column DC_UNIT_INFO.bdcdyh is
'不动产单元号';

comment on column DC_UNIT_INFO.zl is
'坐落';

comment on column DC_UNIT_INFO.yt is
'用途(单独房屋 单独土地存值 多个用/分隔)';

comment on column DC_UNIT_INFO.tdyt is
'土地用途(房地一体存土地用途)';

comment on column DC_UNIT_INFO.mj is
'面积(房屋存建筑面积 土地存使用权面积)';

comment on column DC_UNIT_INFO.tdsyqmj is
'土地使用权面积';

comment on column DC_UNIT_INFO.ftmj is
'分摊面积';

comment on column DC_UNIT_INFO.tnmj is
'套内面积';

comment on column DC_UNIT_INFO.mjdw is
'面积单位';

comment on column DC_UNIT_INFO.ytdm is
'用途代码(多个用^分隔)';

comment on column DC_UNIT_INFO.tdytdm is
'土地用途代码(多个用^分隔)';

comment on column DC_UNIT_INFO.tdmjdw is
'土地面积单位';

comment on column DC_UNIT_INFO.mjdwdm is
'面积单位代码';

comment on column DC_UNIT_INFO.tdmjdwdm is
'土地面积单位代码';

comment on column DC_UNIT_INFO.zt is
'状态';


