/* tablename: �뵥�ִ������� */
create table nc_mdxcl ( 
pk_corp char(4) null 
/*��˾*/,
def5 char(1) null 
/*�����ֶ�5*/,
dmakedate char(10) null 
/*�Ƶ�����*/,
voperatorid char(20) null 
/*�Ƶ���*/,
def11 varchar2(50) null 
/*�����ֶ�11*/,
cinvbasid char(20) null 
/*�����������*/,
def14 varchar2(200) null 
/*�����ֶ�14*/,
def7 varchar2(20) null 
/*�����ֶ�7*/,
def6 varchar2(20) null 
/*�����ֶ�6*/,
def15 varchar2(2000) null 
/*�����ֶ�15*/,
sum_zhishu number(20,8) null 
/*���֧��*/,
def10 varchar2(20) null 
/*�����ֶ�10*/,
sum_zhongliang number(20,8) null 
/*�������*/,
def13 varchar2(100) null 
/*�����ֶ�13*/,
def8 varchar2(20) null 
/*�����ֶ�8*/,
def1 number(20,8) null 
/*�����ֶ�1*/,
def2 number(20,8) null 
/*�����ֶ�2*/,
def12 varchar2(50) null 
/*�����ֶ�12*/,
cinventoryidb char(20) null 
/*���������*/,
cwarehouseidb char(20) null 
/*�ֿ�PK*/,
def4 char(1) null 
/*�����ֶ�4*/,
ccalbodyidb char(20) null 
/*�����֯*/,
def3 number(20,8) null 
/*�����ֶ�3*/,
def9 varchar2(20) null 
/*�����ֶ�9*/,
pk_mdxcl char(20) not null 
/*����*/,
ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')
/*ʱ���*/,
dr number(10) default 0
/*ɾ�����*/,
constraint pk_nc_mdxcl primary key (pk_mdxcl)
)
;

/* tablename: �뵥������ */
create table nc_mdcrk ( 
def5 char(1) null 
/*�����ֶ�5*/,
md_meter number(20,8) null 
/*����*/,
pk_corp char(4) null 
/*��˾*/,
pk_mdxcl_b varchar2(20) null 
/*�뵥�ִ�������PK*/,
cbodybilltypecode varchar2(20) null 
/*��������*/,
md_zlzsh varchar2(200) null 
/*�ʱ�֤���*/,
crkfx number null 
/*����ⷽ��*/,
dmakedate char(10) null 
/*�Ƶ�����*/,
md_length number(20,8) null 
/*����*/,
srkzs number(20,8) null 
/*֧��*/,
voperatorid char(20) null 
/*�Ƶ���*/,
def11 varchar2(50) null 
/*�����ֶ�11*/,
def14 varchar2(200) null 
/*�����ֶ�14*/,
def7 varchar2(20) null 
/*�����ֶ�7*/,
def6 varchar2(20) null 
/*�����ֶ�6*/,
md_note varchar2(200) null 
/*ʵ���*��*��*/,
def15 varchar2(2000) null 
/*�����ֶ�15*/,
md_width number(20,8) null 
/*���*/,
cgeneralbid varchar2(20) null 
/*����ⵥ����PK*/,
djfx number null 
/*���ݷ���*/,
def10 varchar2(20) null 
/*�����ֶ�10*/,
jbh varchar2(50) null 
/*�����*/,
md_zyh varchar2(200) null 
/*��Դ��*/,
md_lph varchar2(200) null 
/*¯����*/,
def13 varchar2(100) null 
/*�����ֶ�13*/,
srkzl number(20,8) null 
/*����*/,
def8 varchar2(20) null 
/*�����ֶ�8*/,
def1 number(20,8) null 
/*�����ֶ�1*/,
remark varchar2(2000) null 
/*��ע*/,
cspaceid varchar2(20) null 
/*��λPK*/,
def12 varchar2(50) null 
/*�����ֶ�12*/,
def2 number(20,8) null 
/*�����ֶ�2*/,
pk_mdcrk char(20) not null 
/*����*/,
sfbj char(1) null 
/*�Ƿ����*/,
def4 char(1) null 
/*�����ֶ�4*/,
cwarehouseidb varchar2(20) null 
/*�ֿ�PK*/,
ccalbodyidb varchar2(20) null 
/*�����֯*/,
def3 number(20,8) null 
/*�����ֶ�3*/,
def9 varchar2(20) null 
/*�����ֶ�9*/,
ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')
/*ʱ���*/,
dr number(10) default 0
/*ɾ�����*/,
constraint pk_nc_mdcrk primary key (pk_mdcrk)
)
;

/* tablename: �뵥������ */
create table nc_mdsd ( 
pk_corp char(4) null 
/*��˾*/,
def5 char(1) null 
/*�����ֶ�5*/,
pk_mdxcl_b char(20) null 
/*�뵥�ִ�������PK*/,
dmakedate char(10) null 
/*�Ƶ�����*/,
voperatorid char(20) null 
/*�Ƶ���*/,
sfsx char(1) null 
/*�Ƿ���Ч*/,
sdrq char(10) null 
/*��������*/,
sdzs number(20,8) null 
/*����֧��*/,
def11 varchar2(50) null 
/*�����ֶ�11*/,
def14 varchar2(200) null 
/*�����ֶ�14*/,
def7 varchar2(20) null 
/*�����ֶ�7*/,
def6 varchar2(20) null 
/*�����ֶ�6*/,
def15 varchar2(2000) null 
/*�����ֶ�15*/,
def10 varchar2(20) null 
/*�����ֶ�10*/,
sxrq char(10) null 
/*ʧЧ����*/,
def13 varchar2(100) null 
/*�����ֶ�13*/,
def8 varchar2(20) null 
/*�����ֶ�8*/,
def1 number(20,8) null 
/*�����ֶ�1*/,
def2 number(20,8) null 
/*�����ֶ�2*/,
def12 varchar2(50) null 
/*�����ֶ�12*/,
def4 char(1) null 
/*�����ֶ�4*/,
xsddbt_pk char(20) null 
/*���۶�������PK*/,
def3 number(20,8) null 
/*�����ֶ�3*/,
def9 varchar2(20) null 
/*�����ֶ�9*/,
pk_mdsd char(20) not null 
/*����*/,
ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')
/*ʱ���*/,
dr number(10) default 0
/*ɾ�����*/,
constraint pk_nc_mdsd primary key (pk_mdsd)
)
;

/* tablename: �뵥�ִ����ӱ� */
create table nc_mdxcl_b ( 
md_meter number(20,8) null 
/*����*/,
def5 char(1) null 
/*�����ֶ�5*/,
pk_mdxcl_b char(20) not null 
/*����*/,
md_zlzsh varchar2(200) null 
/*�ʱ�֤���*/,
md_length number(20,8) null 
/*����*/,
def11 varchar2(50) null 
/*�����ֶ�11*/,
def14 varchar2(200) null 
/*�����ֶ�14*/,
def7 varchar2(20) null 
/*�����ֶ�7*/,
def6 varchar2(20) null 
/*�����ֶ�6*/,
md_note varchar2(200) null 
/*ʵ���*��*��*/,
def15 varchar2(2000) null 
/*�����ֶ�15*/,
md_width number(20,8) null 
/*���*/,
def10 varchar2(20) null 
/*�����ֶ�10*/,
md_zyh varchar2(200) null 
/*��Դ��*/,
jbh varchar2(50) null 
/*�����*/,
md_lph varchar2(200) null 
/*¯����*/,
def13 varchar2(100) null 
/*�����ֶ�13*/,
def8 varchar2(20) null 
/*�����ֶ�8*/,
zhishu number(20,8) null 
/*֧��*/,
def1 number(20,8) null 
/*�����ֶ�1*/,
remark varchar2(2000) null 
/*��ע*/,
cspaceid char(20) null 
/*��λPK*/,
zhongliang number(20,8) null 
/*����*/,
def12 varchar2(50) null 
/*�����ֶ�12*/,
def2 number(20,8) null 
/*�����ֶ�2*/,
def4 char(1) null 
/*�����ֶ�4*/,
def3 number(20,8) null 
/*�����ֶ�3*/,
def9 varchar2(20) null 
/*�����ֶ�9*/,
pk_mdxcl char(20) null 
/*��������*/,
ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')
/*ʱ���*/,
dr number(10) default 0
/*ɾ�����*/,
constraint pk_nc_mdxcl_b primary key (pk_mdxcl_b)
)
;

