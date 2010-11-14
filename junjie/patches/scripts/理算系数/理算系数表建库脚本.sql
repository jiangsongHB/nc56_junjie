--����ϵ����
create or replace table JJ_BD_ADJUSTMENTCOEFFICIENT  (
--����ϵ��������
   pk_adjustmentcoefficient char(20), 
--�����������ID                        
   pk_invbasdoc       char(20) ,
--���
   vinvspec          varcahr2(50),
--����ϵ��
   nadjustmentcoefficient number(20,8),
--��ע
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
   constraint PK_JJ_BD_ADJUSTMENTCOEFFICIENT primary key (pk_adjustmentcoefficient),
   constraint UN_JJ_BD_ADJUSTMENTCOEFFICIENT unique (pk_invbasdoc)
)


--�°�
-- Create table
create table JJ_BD_ADJUSTMENTCOEFFICIENT
(
  PK_ADJUSTMENTCOEFFICIENT CHAR(20) not null,
  PK_INVBASDOC             CHAR(20),
  VINVSPEC                 VARCHAR2(50),
  NADJUSTMENTCOEFFICIENT   NUMBER(20,8),
  VMEMO                    VARCHAR2(200),
  TS                       CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
  DR                       NUMBER(10),
  VDEF1                    VARCHAR2(20),
  VDEF2                    VARCHAR2(20),
  VDEF3                    VARCHAR2(20),
  VDEF4                    VARCHAR2(20),
  VDEF5                    VARCHAR2(20),
  VDEF6                    VARCHAR2(20),
  VDEF7                    VARCHAR2(20),
  VDEF8                    VARCHAR2(20),
  VDEF9                    VARCHAR2(20),
  VDEF10                   VARCHAR2(20)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 256
    next 256
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table JJ_BD_ADJUSTMENTCOEFFICIENT
  add constraint PK_JJ_BD_ADJUSTMENTCOEFFICIENT primary key (PK_ADJUSTMENTCOEFFICIENT)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
