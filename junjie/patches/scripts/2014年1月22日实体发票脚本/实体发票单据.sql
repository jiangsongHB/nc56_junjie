/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2014-01-15 18:29:34                          */
/*==============================================================*/



/*==============================================================*/
/* Table: FA_ENTITYRECEIPT_B                                    */
/*==============================================================*/
create table JA_ENTITY_RECEIPT_B  (
   PK_ENTITYRECEIPT_B   CHAR(20)                        not null,
   PK_ENTITY_RECEIPT    CHAR(20),
   PK_CORP              CHAR(4),
   ROWNO                VARCHAR2(20),
   INVCODE              VARCHAR2(20),
   PK_INVDOC            VARCHAR2(20),
   INVCLASSNAME         VARCHAR2(20),
   PK_INVCL             VARCHAR2(20),
   COUNT                NUMBER(20,8),
   PRICE                NUMBER(20,8),
   AMOUNT               NUMBER(20,8),
   TAXRATE              NUMBER(20,8),
   TAX                  NUMBER(20,8),
   TAXAMOUNT            NUMBER(20,8),
   TOTALAMOUNT          NUMBER(20,8),
   VNOTE                VARCHAR2(100),
   VLASTBILLROWID       VARCHAR2(30),
   VLASTBILLTYPE        VARCHAR2(30),
   VLASTBILLID          VARCHAR2(30),
   VSOURCEBILLTYPE      VARCHAR2(30),
   VSOURCEBILLID        VARCHAR2(30),
   VSOURCEBILLROWID     VARCHAR2(30),
   CSOURCEBILLBODYID    CHAR(20),
   CSOURCEBILLID        CHAR(20),
   DEF1                 VARCHAR2(30),
   DEF2                 VARCHAR2(30),
   DEF3                 VARCHAR2(30),
   DEF4                 VARCHAR2(30),
   DEF5                 VARCHAR2(30),
   DEF6                 VARCHAR2(30),
   DEF7                 VARCHAR2(30),
   DEF8                 VARCHAR2(30),
   DEF9                 VARCHAR2(30),
   DEF10                VARCHAR2(30),
   DEF11                VARCHAR2(30),
   DEF12                VARCHAR2(30),
   DEF13                VARCHAR2(30),
   DEF14                VARCHAR2(30),
   DEF15                VARCHAR2(30),
   DEF16                VARCHAR2(30),
   DEF17                VARCHAR2(30),
   DEF18                VARCHAR2(30),
   DEF19                VARCHAR2(30),
   DEF20                VARCHAR2(30),
   DEF21                VARCHAR2(30),
   DEF22                VARCHAR2(30),
   DEF23                VARCHAR2(30),
   DEF24                VARCHAR2(30),
   DEF25                VARCHAR2(30),
   DEF26                VARCHAR2(30),
   DEF27                VARCHAR2(30),
   DEF28                VARCHAR2(30),
   DEF29                VARCHAR2(30),
   DEF30                VARCHAR2(30),
   constraint PK_JA_ENTITYRECEIPT_B primary key (PK_ENTITYRECEIPT_B)
,
  DR           NUMBER(10) default 0,
  TS           CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')

);

/*==============================================================*/
/* Table: FA_ENTITY_RECEIPT                                     */
/*==============================================================*/
create table JA_ENTITY_RECEIPT  (
   PK_ENTITY_RECEIPT    CHAR(20)                        not null,
   VBILLNO              CHAR(20),
   PK_BILLTYPE          VARCHAR2(20),
   PK_BUSITYPE          VARCHAR2(20),
   VBILLSTATUS          SMALLINT,
   VOPERATORID          VARCHAR2(20),
   DMAKEDATE            CHAR(10),
   VAPPROVEID           VARCHAR2(20),
   DAPPROVEDATE         CHAR(10),
   VAPPROVENOTE         VARCHAR2(100),
   PK_CORP              CHAR(4),
   RECEIPTNO            VARCHAR2(20),
   DORECEIPTDATE        CHAR(10),
   PK_CUSTRECEIPT       VARCHAR2(20),
   PK_CUSTORDER         VARCHAR2(20),
   SALETYPE             VARCHAR2(20),
   BUSSTYPE             VARCHAR2(20),
   DORECEIPTTYPE        VARCHAR2(20),
   RECEIPTTYPE          VARCHAR2(20),
   PK_CUMANDOC          VARCHAR2(20),
   VNOTE                VARCHAR2(100),
   DEF1                 VARCHAR2(30),
   DEF2                 VARCHAR2(30),
   DEF3                 VARCHAR2(30),
   DEF4                 VARCHAR2(30),
   DEF5                 VARCHAR2(30),
   DEF6                 VARCHAR2(30),
   DEF7                 VARCHAR2(30),
   DEF8                 VARCHAR2(30),
   DEF9                 VARCHAR2(30),
   DEF10                VARCHAR2(30),
   DEF11                VARCHAR2(30),
   DEF12                VARCHAR2(30),
   DEF13                VARCHAR2(30),
   DEF14                VARCHAR2(30),
   DEF15                VARCHAR2(30),
   DEF16                VARCHAR2(30),
   DEF17                VARCHAR2(30),
   DEF18                VARCHAR2(30),
   DEF19                VARCHAR2(30),
   DEF20                VARCHAR2(30),
   DEF21                VARCHAR2(30),
   DEF22                VARCHAR2(30),
   DEF23                VARCHAR2(30),
   DEF24                VARCHAR2(30),
   DEF25                VARCHAR2(30),
   DEF26                VARCHAR2(30),
   DEF27                VARCHAR2(30),
   DEF28                VARCHAR2(30),
   DEF29                VARCHAR2(30),
   DEF30                VARCHAR2(30),
   constraint PK_JA_ENTITY_RECEIPT primary key (PK_ENTITY_RECEIPT)
,
  DR           NUMBER(10) default 0,
  TS           CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')

);

alter table JA_ENTITY_RECEIPT_B
   add constraint FK_JA_ENTIT_REFERENCE_JA_ENTIT foreign key (PK_ENTITY_RECEIPT)
      references JA_ENTITY_RECEIPT (PK_ENTITY_RECEIPT);

