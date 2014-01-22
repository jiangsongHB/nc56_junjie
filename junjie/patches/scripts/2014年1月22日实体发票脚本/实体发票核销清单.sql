/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2014-01-16 10:52:41                          */
/*==============================================================*/


/*==============================================================*/
/* Table: JA_ENTITY_RECEIPT_CK                                  */
/*==============================================================*/
create table JA_ENTITY_RECEIPT_CK  (
   PK_ENTITY_RECEIPT_CK CHAR(20)                        not null,
   PK_ENTITY_RECEIPT    VARCHAR2(20),
   PK_CORP              CHAR(4),
   ROWNO                VARCHAR2(20),
   INVCODE              VARCHAR2(20),
   PK_INVDOC            VARCHAR2(20),
   INVCLASSNAME         VARCHAR2(20),
   PK_INVCL             VARCHAR2(20),
   VNAME                VARCHAR2(50),
   VSIZE                VARCHAR2(50),
   QUALITY              VARCHAR2(50),
   ADDR                 VARCHAR2(100),
   MEASURENAME          VARCHAR2(20),
   PK_MEASURE           VARCHAR2(20),
   COUNT                NUMBER(20,8),
   PRICE                NUMBER(20,8),
   AMOUNT               NUMBER(20,8),
   TAXRATE              NUMBER(20,8),
   TAX                  NUMBER(20,8),
   TAXAMOUNT            NUMBER(20,8),
   VNOTE                VARCHAR2(100),
   DEF1                 VARCHAR2(20),
   DEF2                 VARCHAR2(20),
   DEF3                 VARCHAR2(20),
   DEF4                 VARCHAR2(20),
   DEF5                 VARCHAR2(20),
   DEF6                 VARCHAR2(20),
   DEF7                 VARCHAR2(20),
   DEF8                 VARCHAR2(20),
   DEF9                 VARCHAR2(20),
   DEF10                VARCHAR2(20),
   DEF11                VARCHAR2(20),
   DEF12                VARCHAR2(20),
   DEF13                VARCHAR2(20),
   DEF14                VARCHAR2(20),
   DEF15                VARCHAR2(20),
   DEF16                VARCHAR2(20),
   DEF17                VARCHAR2(20),
   DEF18                VARCHAR2(20),
   DEF19                VARCHAR2(20),
   DEF20                VARCHAR2(20),
   DEF21                VARCHAR2(20),
   DEF22                VARCHAR2(20),
   DEF23                VARCHAR2(20),
   DEF24                VARCHAR2(20),
   DEF25                VARCHAR2(20),
   DEF26                VARCHAR2(20),
   DEF27                VARCHAR2(20),
   DEF28                VARCHAR2(20),
   DEF29                VARCHAR2(20),
   DEF30                VARCHAR2(20),
   constraint PK_JA_ENTITY_RECEIPT_CK primary key (PK_ENTITY_RECEIPT_CK)
,
  DR           NUMBER(10) default 0,
  TS           CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')

);

