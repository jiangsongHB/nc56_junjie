/* tablename: ˰��Ʊ���� */

create table so_taxinvoice (
ctaxinvoiceid char(20) NOT NULL 
 /*��Ʊ����*/  ,
vinvoiceno varchar(40) NULL 
 /*��Ʊ��*/  ,
vbillno varchar(50) NULL 
 /*���ݺ�*/  ,
dinvoicedate char(10) NULL 
 /*��Ʊ����*/  ,
pk_corp char(20) NULL 
 /*��Ʊ��˾*/  ,
cbilltype char(20) NULL 
 /*ϵͳ��������*/  ,
ibillstatus smallint NULL 
 /*����״̬*/  ,
ccreator char(20) NULL 
 /*������*/  ,
dcreatedate char(19) NULL 
 /*��������*/  ,
cmodifier char(20) NULL 
 /*�޸���*/  ,
dmodifydate char(19) NULL 
 /*�޸�����*/  ,
capprover char(20) NULL 
 /*�����*/  ,
dapprovedate char(19) NULL 
 /*�������*/  ,
cinvoicemamid char(20) NULL 
 /*��Ʊ����*/  ,
dreceivedate char(10) NULL 
 /*Ʊ������*/  ,
cordermanid char(20) NULL 
 /*��������*/  ,
cservicemanid char(20) NULL 
 /*����λ*/  ,
cinvoicetype char(20) NULL 
 /*��Ʊ����*/  ,
ispray char(1) NULL 
 /*Ԥ��Ʊ��־*/  ,
csaletype varchar(100) NULL 
 /*��������*/  ,
cpersonid char(20) NULL 
 /*��Ʊ��*/  ,
cdeptid char(20) NULL 
 /*��Ʊ����*/  ,
ccurrencyid char(20) NULL 
 /*����*/  ,
nroe decimal(28,8) NULL 
 /*����*/  ,
vmemo varchar(200) NULL 
 /*��ע*/  ,
cbusitype char(20) NULL 
 /*ҵ������*/  ,
vdef1 varchar(100) NULL 
 /*�Զ�����1*/  ,
vdef2 varchar(100) NULL 
 /*�Զ�����2*/  ,
vdef3 varchar(100) NULL 
 /*�Զ�����3*/  ,
vdef4 varchar(100) NULL 
 /*�Զ�����4*/  ,
vdef5 varchar(100) NULL 
 /*�Զ�����5*/  ,
vdef6 varchar(100) NULL 
 /*�Զ�����6*/  ,
vdef7 varchar(100) NULL 
 /*�Զ�����7*/  ,
vdef8 varchar(100) NULL 
 /*�Զ�����8*/  ,
vdef9 varchar(100) NULL 
 /*�Զ�����9*/  ,
vdef10 varchar(100) NULL 
 /*�Զ�����10*/  ,
vapprovnote varchar(200) NULL 
 /*����*/  ,
ntaxrate decimal(28,8) NULL 
 /*����˰��*/  ,
CONSTRAINT PK_SO_TAXINVOICE PRIMARY KEY (ctaxinvoiceid),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
go 


/* tablename: ˰��Ʊ�ӱ� */

create table so_taxinvoice_b (
ctaxinvoice_bid char(20) NOT NULL 
 /*��Ʊ�ӱ�����*/  ,
cinvname varchar(200) NULL 
 /*��Ʊ��Ŀ*/  ,
cinvclassid char(20) NULL 
 /*�������*/  ,
cinventoryid char(20) NULL 
 /*���*/  ,
nnumber decimal(28,8) NULL 
 /*����*/  ,
nprice decimal(28,8) NULL 
 /*����*/  ,
ntaxprice decimal(28,8) NULL 
 /*��˰����*/  ,
nmny decimal(28,8) NULL 
 /*���*/  ,
ntaxman decimal(28,8) NULL 
 /*˰��*/  ,
ntaxrate decimal(28,8) NULL 
 /*˰��*/  ,
nsummny decimal(28,8) NULL 
 /*��˰�ϼ�*/  ,
ncurprice decimal(28,8) NULL 
 /*ԭ�ҵ���*/  ,
ncurtaxprice decimal(28,8) NULL 
 /*ԭ�Һ�˰����*/  ,
ncurmny decimal(28,8) NULL 
 /*ԭ�ҽ��*/  ,
ncurtaxmny decimal(28,8) NULL 
 /*ԭ��˰��*/  ,
ncursummny decimal(28,8) NULL 
 /*ԭ�Ҽ�˰�ϼ�*/  ,
ntotaldealnum decimal(28,8) NULL 
 /*�ۼƺ�������*/  ,
ntotaldealmny decimal(28,8) NULL 
 /*�ۼƺ������*/  ,
irowclose char(1) NULL 
 /*�йر�*/  ,
vrowmemo varchar(200) NULL 
 /*�б�ע*/  ,
vbdef1 varchar(100) NULL 
 /*�����Զ�����1*/  ,
vbdef2 varchar(50) NULL 
 /*�����Զ�����2*/  ,
vbdef3 varchar(50) NULL 
 /*�����Զ�����3*/  ,
vbdef4 varchar(50) NULL 
 /*�����Զ�����4*/  ,
vbdef5 varchar(50) NULL 
 /*�����Զ�����5*/  ,
vbdef6 varchar(50) NULL 
 /*�����Զ�����6*/  ,
vbdef7 varchar(50) NULL 
 /*�����Զ�����7*/  ,
vbdef8 varchar(50) NULL 
 /*�����Զ�����8*/  ,
vbdef9 varchar(50) NULL 
 /*�����Զ�����9*/  ,
vbdef10 varchar(50) NULL 
 /*�����Զ�����10*/  ,
crowno varchar(50) NULL 
 /*��Ʊ�к�*/  ,
ctaxinvoiceid char(20) NULL 
 /*˰��Ʊ����_����*/  ,
CONSTRAINT PK_SO_TAXINVOICE_B PRIMARY KEY (ctaxinvoice_bid),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
go 


/* tablename: ˰��Ʊ���ӱ� */

create table so_taxinvoice_bb1 (
ctaxinvoice_bbid char(20) NOT NULL 
 /*��Ʊ������ϸid*/  ,
csourcebilltype char(20) NULL 
 /*�Է���������*/  ,
csourcebillid char(20) NULL 
 /*�Է����ݱ�ͷid*/  ,
csourcebillrowid char(20) NULL 
 /*�Է����ݱ���id*/  ,
ddealdate char(10) NULL 
 /*��������*/  ,
ndealnum decimal(28,8) NULL 
 /*��������*/  ,
ndealmny decimal(28,8) NULL 
 /*�������*/  ,
cdealoper char(20) NULL 
 /*����������*/  ,
nwriteinvoicenum decimal(28,8) NULL 
 /*�������ⷢƱ����*/  ,
nwriteinvoicemny decimal(28,8) NULL 
 /*�������ⷢƱ���*/  ,
ctaxinvoiceid char(20) NULL 
 /*˰��Ʊ����*/  ,
ctaxinvoice_bid char(20) NULL 
 /*��Ʊ�ӱ�����*/  ,
CONSTRAINT PK_SO_TAXINVOICE_BB1 PRIMARY KEY (ctaxinvoice_bbid),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
go 


