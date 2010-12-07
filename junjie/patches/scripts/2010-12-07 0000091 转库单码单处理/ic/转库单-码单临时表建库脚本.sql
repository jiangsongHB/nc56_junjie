-- Create table 除表名外,其字段含义与nc_mdcrk表完全相同
create table NC_MDCRK_TEMP
(
  PK_MDCRK          CHAR(20) not null,
  PK_CORP           CHAR(4),
  CBODYBILLTYPECODE VARCHAR2(20),
  PK_MDXCL_B        VARCHAR2(20),
  CGENERALBID       VARCHAR2(20),
  CCALBODYIDB       VARCHAR2(20),
  CWAREHOUSEIDB     VARCHAR2(20),
  CSPACEID          VARCHAR2(20),
  JBH               VARCHAR2(50),
  MD_WIDTH          NUMBER(20,8),
  MD_LENGTH         NUMBER(20,8),
  MD_METER          NUMBER(20,8),
  MD_NOTE           VARCHAR2(200),
  MD_LPH            VARCHAR2(200),
  MD_ZYH            VARCHAR2(200),
  MD_ZLZSH          VARCHAR2(200),
  REMARK            VARCHAR2(2000),
  SRKZS             NUMBER(20,8),
  SRKZL             NUMBER(20,8),
  CRKFX             INTEGER,
  DJFX              INTEGER,
  SFBJ              CHAR(1),
  DMAKEDATE         CHAR(10),
  VOPERATORID       CHAR(20),
  DR                INTEGER,
  TS                CHAR(19),
  DEF1              NUMBER(20,8),
  DEF2              NUMBER(20,8),
  DEF3              NUMBER(20,8),
  DEF4              CHAR(1),
  DEF5              CHAR(1),
  DEF6              VARCHAR2(20),
  DEF7              VARCHAR2(20),
  DEF8              VARCHAR2(20),
  DEF9              VARCHAR2(20),
  DEF10             VARCHAR2(20),
  DEF11             VARCHAR2(50),
  DEF12             VARCHAR2(50),
  DEF13             VARCHAR2(100),
  DEF14             VARCHAR2(200),
  DEF15             VARCHAR2(2000)
)
tablespace NNC_INDEX01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 128
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table NC_MDCRK_TEMP
  add constraint PK_NC_MDCRK_TEMP primary key (PK_MDCRK)
  using index 
  tablespace NNC_INDEX01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 128K
    next 128K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
