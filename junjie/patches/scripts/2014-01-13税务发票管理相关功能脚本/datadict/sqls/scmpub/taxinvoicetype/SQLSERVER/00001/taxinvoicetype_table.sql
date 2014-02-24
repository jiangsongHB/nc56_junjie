/* tablename: ˰��Ʊ���� */
create table bd_taxinvoicetype (
pk_taxinvoicetype char(20) NOT NULL/*��������*/  ,
pk_corp char(20) NULL/*��֯����*/  ,
code varchar(50) NULL/*����*/  ,
name varchar(200) NULL/*����*/  ,
creator char(20) NULL/*������*/  ,
creationtime char(19) NULL/*��������*/  ,
modifier char(20) NULL/*�޸���*/  ,
modifiedtime char(19) NULL/*�޸�����*/  ,
approver char(20) NULL/*�����*/  ,
approvedtime char(19) NULL/*�������*/  ,
iffee char(1) NULL/*�Ƿ��������*/  ,
isdefault char(1) NULL/*�Ƿ�Ĭ��*/  ,
isseal char(1) NULL/*�Ƿ���*/  ,
iapprovetype int NULL/*��˲���*/  ,
idealtype int NULL/*��������*/  ,
ntaxrate decimal(28,8) NULL/*˰��*/  ,
CONSTRAINT PK_BD_TAXINVOICETYPE PRIMARY KEY (pk_taxinvoicetype),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
 go
