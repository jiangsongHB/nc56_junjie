-- Create table
 create table SCM_INVDETAIL_C
 /*�����ϸ�ӱ�*/
 (
 PK_INVDETAIL_C CHAR(20) not null,
 /*�����ϸ�ӱ�����*/
 PK_INVDETAIL CHAR(20),
 /*�����ϸ��������*/
 PK_MDCRK CHAR(20),
 /*�뵥����*/
 TS CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
 /*ʱ���*/
 DR NUMBER(10) default 0,
 /*ɾ�����*/
 CGENERALBID CHAR(20)
 /*�ɹ���ⵥ��������*/
 )
 
