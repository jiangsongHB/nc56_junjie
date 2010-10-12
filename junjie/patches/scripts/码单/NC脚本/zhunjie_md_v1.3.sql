/*==============================================================*/
/* DBMS name:      Oracle 10g                  */
/* Created on:     2010-9-15 16:05:58                           */
/*==============================================================*/


/*==============================================================*/
/* Table: nc_mdcrk                                              */
/*==============================================================*/
create table nc_mdcrk (
   pk_mdcrk             char(20)             not null,
   pk_corp              char(4)              null,
   cbodybilltypecode    varchar(20)          null,
   pk_mdxcl_b           varchar(20)          null,
   cgeneralbid          varchar(20)          null,
   ccalbodyidb          varchar(20)          null,
   cwarehouseidb        varchar(20)          null,
   cspaceid             varchar(20)          null,
   jbh                  varchar(50)          null,
   md_width             decimal(20,8)        null,
   md_length            decimal(20,8)        null,
   md_meter             decimal(20,8)        null,
   md_note              varchar(200)         null,
   md_lph               varchar(200)         null,
   md_zyh               varchar(200)         null,
   md_zlzsh             varchar(200)         null,
   remark               varchar(2000)        null,
   srkzs                decimal(20,8)        null,
   srkzl                decimal(20,8)        null,
   crkfx                smallint             null,
   djfx                 smallint             null,
   sfbj                 char(1)              null,
   dmakedate            char(10)             null,
   voperatorid          char(20)             null,
   dr                   smallint             null,
   ts                   char(19)             null,
   def1                 decimal(20,8)        null,
   def2                 decimal(20,8)        null,
   def3                 decimal(20,8)        null,
   def4                 char(1)              null,
   def5                 char(1)              null,
   def6                 varchar(20)          null,
   def7                 varchar(20)          null,
   def8                 varchar(20)          null,
   def9                 varchar(20)          null,
   def10                varchar(20)          null,
   def11                varchar(50)          null,
   def12                varchar(50)          null,
   def13                varchar(100)         null,
   def14                varchar(200)         null,
   def15                varchar(2000)        null,
   constraint PK_NC_MDCRK primary key  (pk_mdcrk)
)
;

/*==============================================================*/
/* Table: nc_mdsd                                               */
/*==============================================================*/
create table nc_mdsd (
   pk_mdsd              char(20)             not null,
   pk_corp              char(4)              null,
   pk_mdxcl_b           char(20)             null,
   xsddbt_pk            char(20)             null,
   sdrq                 char(10)             null,
   sxrq                 char(10)             null,
   sdzs                 decimal(20,8)        null,
   sfsx                 char(1)              null,
   dmakedate            char(10)             null,
   voperatorid          char(20)             null,
   dr                   smallint             null,
   ts                   char(19)             null,
   def1                 decimal(20,8)        null,
   def2                 decimal(20,8)        null,
   def3                 decimal(20,8)        null,
   def4                 char(1)              null,
   def5                 char(1)              null,
   def6                 varchar(20)          null,
   def7                 varchar(20)          null,
   def8                 varchar(20)          null,
   def9                 varchar(20)          null,
   def10                varchar(20)          null,
   def11                varchar(50)          null,
   def12                varchar(50)          null,
   def13                varchar(100)         null,
   def14                varchar(200)         null,
   def15                varchar(2000)        null,
   constraint PK_NC_MDSD primary key  (pk_mdsd)
)
;

/*==============================================================*/
/* Table: nc_mdxcl                                              */
/*==============================================================*/
create table nc_mdxcl (
   pk_mdxcl             char(20)             not null,
   pk_corp              char(4)              null,
   ccalbodyidb          char(20)             null,
   cwarehouseidb        char(20)             null,
   cinvbasid            char(20)             null,
   cinventoryidb        char(20)             null,
   sum_zhishu           decimal(20,8)        null,
   sum_zhongliang       decimal(20,8)        null,
   dmakedate            char(10)             null,
   voperatorid          char(20)             null,
   dr                   smallint             null,
   ts                   char(19)             null,
   def1                 decimal(20,8)        null,
   def2                 decimal(20,8)        null,
   def3                 decimal(20,8)        null,
   def4                 char(1)              null,
   def5                 char(1)              null,
   def6                 varchar(20)          null,
   def7                 varchar(20)          null,
   def8                 varchar(20)          null,
   def9                 varchar(20)          null,
   def10                varchar(20)          null,
   def11                varchar(50)          null,
   def12                varchar(50)          null,
   def13                varchar(100)         null,
   def14                varchar(200)         null,
   def15                varchar(2000)        null,
   constraint PK_NC_MDXCL primary key  (pk_mdxcl)
)
;

/*==============================================================*/
/* Table: nc_mdxcl_b                                            */
/*==============================================================*/
create table nc_mdxcl_b (
   pk_mdxcl_b           char(20)             not null,
   pk_mdxcl             char(20)             null,
   cspaceid             char(20)             null,
   jbh                  varchar(50)          null,
   md_width             decimal(20,8)        null,
   md_length            decimal(20,8)        null,
   md_meter             decimal(20,8)        null,
   md_note              varchar(200)         null,
   md_lph               varchar(200)         null,
   md_zyh               varchar(200)         null,
   md_zlzsh             varchar(200)         null,
   remark               varchar(2000)        null,
   zhishu               decimal(20,8)        null,
   zhongliang           decimal(20,8)        null,
   dr                   smallint             null,
   ts                   char(19)             null,
   def1                 decimal(20,8)        null,
   def2                 decimal(20,8)        null,
   def3                 decimal(20,8)        null,
   def4                 char(1)              null,
   def5                 char(1)              null,
   def6                 varchar(20)          null,
   def7                 varchar(20)          null,
   def8                 varchar(20)          null,
   def9                 varchar(20)          null,
   def10                varchar(20)          null,
   def11                varchar(50)          null,
   def12                varchar(50)          null,
   def13                varchar(100)         null,
   def14                varchar(200)         null,
   def15                varchar(2000)        null,
   constraint PK_NC_MDXCL_B primary key  (pk_mdxcl_b)
)
;

