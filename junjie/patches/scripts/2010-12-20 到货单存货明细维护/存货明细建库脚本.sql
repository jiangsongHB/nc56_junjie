/*==============================================================*/
/* DBMS name: ORACLE Version 8i2 (8.1.6) */
/* Created on: 2010-12-13 14:26:26 */
/*==============================================================*/


drop table scm_invdetail cascade constraints
/

/*==============================================================*/
/* Table: scm_invdetail */
/*==============================================================*/

/* tablename:���(�ְ�)��ϸ�� */

create table scm_invdetail (
   pk_invdetail CHAR(20) not null
         /*�����ϸ������*/,
   pk_corp CHAR(4) 
         /*��˾����*/,
   pk_invbasdoc CHAR(20) 
         /*�����������PK*/,
   pk_invmandoc CHAR(20) 
         /*���������PK*/,
   ordernumber decimal(20,8) 
         /*��������*/,
   arrivenumber decimal(20,8) 
         /*�ۼƵ�������*/,
   Warehousenumber decimal(20,8) 
         /*�ۼ�������*/,
   Conversionrates decimal(20,10) 
         /*������*/,
   contractWeight decimal(20,8) 
         /*�ֳ�����*/,
   arriveWeight decimal(20,8) 
         /*��������*/,
   sellWeight decimal(20,8) 
         /*��������*/,
   Createoperator CHAR(20) 
         /*¼����*/,
   Createdate char(10) 
         /*¼������*/,
   Createtime CHAR(19) 
         /*¼��ʱ��*/,
   modifyoperator CHAR(20) 
         /*�޸���*/,
   modifydate char(10) 
         /*�޸�����*/,
   modifytime CHAR(19) 
         /*�޸�ʱ��*/,
   Contractthick varchar2(20) 
         /*�ֳ����*/,
   Contractwidth varchar2(20) 
         /*�ֳ����*/,
   Contractlength varchar2(20) 
         /*�ֳ�����*/,
   Contractmeter varchar(100) 
         /*�ֳ�����*/,
   arrivethick varchar2(20) 
         /*���պ��*/,
   arrivewidth varchar2(20) 
         /*���տ��*/,
   arrivelength varchar2(20) 
         /*���ճ���*/,
   arrivemeter varchar(100) 
         /*��������*/,
   corder_bid CHAR(20) 
         /*�ɹ���������PK*/,
   carriveorder_bid CHAR(20) 
         /*�ɹ�����������PK*/,
   cgeneralbid CHAR(20) 
         /*�ɹ���ⵥ����PK*/,
   vdef20 varchar(100) 
         /*�Զ����ֶ�20*/,
   vdef19 varchar(100) 
         /*�Զ����ֶ�19*/,
   vdef18 varchar(100) 
         /*�Զ����ֶ�18*/,
   vdef17 varchar(100) 
         /*�Զ����ֶ�17*/,
   vdef16 varchar(100) 
         /*�Զ����ֶ�16*/,
   vdef15 varchar(100) 
         /*�Զ����ֶ�15*/,
   vdef14 varchar(100) 
         /*�Զ����ֶ�14*/,
   vdef13 varchar(100) 
         /*�Զ����ֶ�13*/,
   vdef12 varchar(100) 
         /*�Զ����ֶ�12*/,
   vdef11 varchar(100) 
         /*�Զ����ֶ�11*/,
   vdef10 varchar(100) 
         /*�Զ����ֶ�10*/,
   vdef9 varchar(100) 
         /*�Զ����ֶ�9*/,
   vdef8 varchar(100) 
         /*�Զ����ֶ�8*/,
   vdef7 varchar(100) 
         /*�Զ����ֶ�7*/,
   vdef6 varchar(100) 
         /*�Զ����ֶ�6*/,
   vdef5 varchar(100) 
         /*�Զ����ֶ�5*/,
   vdef4 varchar(100) 
         /*�Զ����ֶ�4*/,
   vdef3 varchar(100) 
         /*�Զ����ֶ�3*/,
   vdef1 varchar(100) 
         /*�Զ����ֶ�1*/,
   vdef2 varchar(100) 
         /*�Զ����ֶ�2*/,
   booleandef5 char(1) 
         /*�Զ��岼���ֶ�5*/,
   booleandef4 char(1) 
         /*�Զ��岼���ֶ�4*/,
   booleandef3 char(1) 
         /*�Զ��岼���ֶ�3*/,
   booleandef2 char(1) 
         /*�Զ��岼���ֶ�2*/,
   booleandef1 char(1) 
         /*�Զ��岼���ֶ�1*/,
   note varchar2(300) 
         /*��ע*/,
   price decimal(20,8) 
         /*����*/,
   Amount decimal(20,8) 
         /*���*/,
   constraint PK_SCM_INVDETAIL primary key (pk_invdetail),
   ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
   dr number(10) default 0
)
/