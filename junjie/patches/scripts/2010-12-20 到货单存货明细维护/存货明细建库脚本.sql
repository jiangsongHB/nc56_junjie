/*==============================================================*/
/* DBMS name: ORACLE Version 8i2 (8.1.6) */
/* Created on: 2010-12-13 14:26:26 */
/*==============================================================*/


drop table scm_invdetail cascade constraints
/

/*==============================================================*/
/* Table: scm_invdetail */
/*==============================================================*/

/* tablename:存货(钢板)明细表 */

create table scm_invdetail (
   pk_invdetail CHAR(20) not null
         /*存货明细表主键*/,
   pk_corp CHAR(4) 
         /*公司编码*/,
   pk_invbasdoc CHAR(20) 
         /*存货基本档案PK*/,
   pk_invmandoc CHAR(20) 
         /*存货管理档案PK*/,
   ordernumber decimal(20,8) 
         /*订单件数*/,
   arrivenumber decimal(20,8) 
         /*累计到货件数*/,
   Warehousenumber decimal(20,8) 
         /*累计入库件数*/,
   Conversionrates decimal(20,10) 
         /*换算率*/,
   contractWeight decimal(20,8) 
         /*钢厂重量*/,
   arriveWeight decimal(20,8) 
         /*验收重量*/,
   sellWeight decimal(20,8) 
         /*销售重量*/,
   Createoperator CHAR(20) 
         /*录入人*/,
   Createdate char(10) 
         /*录入日期*/,
   Createtime CHAR(19) 
         /*录入时间*/,
   modifyoperator CHAR(20) 
         /*修改人*/,
   modifydate char(10) 
         /*修改日期*/,
   modifytime CHAR(19) 
         /*修改时间*/,
   Contractthick varchar2(20) 
         /*钢厂厚度*/,
   Contractwidth varchar2(20) 
         /*钢厂宽度*/,
   Contractlength varchar2(20) 
         /*钢厂长度*/,
   Contractmeter varchar(100) 
         /*钢厂米数*/,
   arrivethick varchar2(20) 
         /*验收厚度*/,
   arrivewidth varchar2(20) 
         /*验收宽度*/,
   arrivelength varchar2(20) 
         /*验收长度*/,
   arrivemeter varchar(100) 
         /*验收米数*/,
   corder_bid CHAR(20) 
         /*采购订单表体PK*/,
   carriveorder_bid CHAR(20) 
         /*采购到货单表体PK*/,
   cgeneralbid CHAR(20) 
         /*采购入库单表体PK*/,
   vdef20 varchar(100) 
         /*自定义字段20*/,
   vdef19 varchar(100) 
         /*自定义字段19*/,
   vdef18 varchar(100) 
         /*自定义字段18*/,
   vdef17 varchar(100) 
         /*自定义字段17*/,
   vdef16 varchar(100) 
         /*自定义字段16*/,
   vdef15 varchar(100) 
         /*自定义字段15*/,
   vdef14 varchar(100) 
         /*自定义字段14*/,
   vdef13 varchar(100) 
         /*自定义字段13*/,
   vdef12 varchar(100) 
         /*自定义字段12*/,
   vdef11 varchar(100) 
         /*自定义字段11*/,
   vdef10 varchar(100) 
         /*自定义字段10*/,
   vdef9 varchar(100) 
         /*自定义字段9*/,
   vdef8 varchar(100) 
         /*自定义字段8*/,
   vdef7 varchar(100) 
         /*自定义字段7*/,
   vdef6 varchar(100) 
         /*自定义字段6*/,
   vdef5 varchar(100) 
         /*自定义字段5*/,
   vdef4 varchar(100) 
         /*自定义字段4*/,
   vdef3 varchar(100) 
         /*自定义字段3*/,
   vdef1 varchar(100) 
         /*自定义字段1*/,
   vdef2 varchar(100) 
         /*自定义字段2*/,
   booleandef5 char(1) 
         /*自定义布尔字段5*/,
   booleandef4 char(1) 
         /*自定义布尔字段4*/,
   booleandef3 char(1) 
         /*自定义布尔字段3*/,
   booleandef2 char(1) 
         /*自定义布尔字段2*/,
   booleandef1 char(1) 
         /*自定义布尔字段1*/,
   note varchar2(300) 
         /*备注*/,
   price decimal(20,8) 
         /*单价*/,
   Amount decimal(20,8) 
         /*金额*/,
   constraint PK_SCM_INVDETAIL primary key (pk_invdetail),
   ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
   dr number(10) default 0
)
/