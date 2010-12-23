-- Create table
 create table SCM_INVDETAIL_C
 /*存货明细子表*/
 (
 PK_INVDETAIL_C CHAR(20) not null,
 /*存货明细子表主键*/
 PK_INVDETAIL CHAR(20),
 /*存货明细主表主键*/
 PK_MDCRK CHAR(20),
 /*码单主键*/
 TS CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
 /*时间戳*/
 DR NUMBER(10) default 0,
 /*删除标记*/
 CGENERALBID CHAR(20)
 /*采购入库单表体主键*/
 )
 
