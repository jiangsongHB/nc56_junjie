--理算系数表
create table JJ_BD_ADJUSTMENTCOEFFICIENT  (
--理算系数表主键
   pk_adjustmentcoefficient char(20), 
--存货基本档案ID                        
   pk_invbasdoc       char(20),
--规格
   vinvspec          varcahr2(50),
--理算系数
   nadjustmentcoefficient number(20,8),
--备注
   vmemo              varchar2(200),
   ts                 char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
   dr                 number(10),
   vdef1              varchar2(20),
   vdef2              varchar2(20),
   vdef3              varchar2(20),
   vdef4              varchar2(20),
   vdef5              varchar2(20),
   vdef6              varchar2(20),
   vdef7              varchar2(20),
   vdef8              varchar2(20),
   vdef9              varchar2(20),
   vdef10             varchar2(20),
   constraint PK_JJ_BD_ADJUSTMENTCOEFFICIENT primary key (pk_adjustmentcoefficient)
)