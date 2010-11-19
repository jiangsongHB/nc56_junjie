/**
*
*             警告! 建表前请确认数据无异常并手动在数据库内删除对应表
*
*/


-- Create table
create table JJ_SCM_INFORMATIONCOST
(
  PK_INFORMANTIONCOST       CHAR(20) not null,
  CBILLID                   CHAR(20),
  VCOSTTYPE                 VARCHAR2(50),
  CCOSTUNITID               CHAR(20),
  VMEMO                     VARCHAR2(200),
  TS                        CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
  DR                        NUMBER(10),
  VDEF1                     VARCHAR2(20),
  VDEF2                     VARCHAR2(20),
  VDEF3                     VARCHAR2(20),
  VDEF4                     VARCHAR2(20),
  VDEF5                     VARCHAR2(20),
  VDEF6                     VARCHAR2(20),
  VDEF7                     VARCHAR2(20),
  VDEF8                     VARCHAR2(20),
  VDEF9                     VARCHAR2(20),
  VDEF10                    VARCHAR2(20),
  COSTCODE                  VARCHAR2(40),
  COSTNAME                  VARCHAR2(200),
  CURRTYPEID                CHAR(20),
  CMEASDOCID                CHAR(20),
  NNUMBER                   NUMBER(20,8),
  NTAXPRICE                 NUMBER(20,8),
  NORIGINALCURTAXPRICE      NUMBER(20,8),
  NPRICE                    NUMBER(20,8),
  NMNY                      NUMBER(20,8),
  NORIGINALCURMNY           NUMBER(20,8),
  NSUMMNY                   NUMBER(20,8),
  PK_TAXITEMS               CHAR(20),
  VTAXTYPE                  VARCHAR2(50),
  NINVORIGINALCURMNY        NUMBER(20,8),
  NINVORIGINALCURTAXMNY     NUMBER(20,8),
  NINVTAXMNY                NUMBER(20,8),
  NINVMNY                   NUMBER(20,8),
  NINSTORETAXMNY            NUMBER(20,8),
  NINSTOREMNY               NUMBER(20,8),
  NINSTOREORIGINALCURTAXMNY NUMBER(20,8),
  NINSTOREORIGINALCURMNY    NUMBER(20,8),
  NORIGINALCURPRICE         NUMBER(20,8),
  NORIGINALCURSUMMNY        NUMBER(20,8),
  ISMNY                     CHAR(1) default 'N'
)