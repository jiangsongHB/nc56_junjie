/* tablename: 税务发票主表 */

create table so_taxinvoice (
ctaxinvoiceid char(20) NOT NULL 
 /*发票主键*/  ,
vinvoiceno varchar(40) NULL 
 /*发票号*/  ,
vbillno varchar(50) NULL 
 /*单据号*/  ,
dinvoicedate char(10) NULL 
 /*发票日期*/  ,
pk_corp char(20) NULL 
 /*收票公司*/  ,
cbilltype char(20) NULL 
 /*系统单据类型*/  ,
ibillstatus smallint NULL 
 /*单据状态*/  ,
ccreator char(20) NULL 
 /*创建人*/  ,
dcreatedate char(19) NULL 
 /*创建日期*/  ,
cmodifier char(20) NULL 
 /*修改人*/  ,
dmodifydate char(19) NULL 
 /*修改日期*/  ,
capprover char(20) NULL 
 /*审核人*/  ,
dapprovedate char(19) NULL 
 /*审核日期*/  ,
cinvoicemamid char(20) NULL 
 /*发票客商*/  ,
dreceivedate char(10) NULL 
 /*票到日期*/  ,
cordermanid char(20) NULL 
 /*订单客商*/  ,
cservicemanid char(20) NULL 
 /*服务单位*/  ,
cinvoicetype char(20) NULL 
 /*发票类型*/  ,
ispray char(1) NULL 
 /*预开票标志*/  ,
csaletype varchar(100) NULL 
 /*销售类型*/  ,
cpersonid char(20) NULL 
 /*收票人*/  ,
cdeptid char(20) NULL 
 /*收票部门*/  ,
ccurrencyid char(20) NULL 
 /*币种*/  ,
nroe decimal(28,8) NULL 
 /*汇率*/  ,
vmemo varchar(200) NULL 
 /*备注*/  ,
cbusitype char(20) NULL 
 /*业务流程*/  ,
vdef1 varchar(100) NULL 
 /*自定义项1*/  ,
vdef2 varchar(100) NULL 
 /*自定义项2*/  ,
vdef3 varchar(100) NULL 
 /*自定义项3*/  ,
vdef4 varchar(100) NULL 
 /*自定义项4*/  ,
vdef5 varchar(100) NULL 
 /*自定义项5*/  ,
vdef6 varchar(100) NULL 
 /*自定义项6*/  ,
vdef7 varchar(100) NULL 
 /*自定义项7*/  ,
vdef8 varchar(100) NULL 
 /*自定义项8*/  ,
vdef9 varchar(100) NULL 
 /*自定义项9*/  ,
vdef10 varchar(100) NULL 
 /*自定义项10*/  ,
vapprovnote varchar(200) NULL 
 /*批语*/  ,
ntaxrate decimal(28,8) NULL 
 /*整单税率*/  ,
CONSTRAINT PK_SO_TAXINVOICE PRIMARY KEY (ctaxinvoiceid),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
go 


/* tablename: 税务发票子表 */

create table so_taxinvoice_b (
ctaxinvoice_bid char(20) NOT NULL 
 /*发票子表主键*/  ,
cinvname varchar(200) NULL 
 /*发票项目*/  ,
cinvclassid char(20) NULL 
 /*存货分类*/  ,
cinventoryid char(20) NULL 
 /*存货*/  ,
nnumber decimal(28,8) NULL 
 /*数量*/  ,
nprice decimal(28,8) NULL 
 /*单价*/  ,
ntaxprice decimal(28,8) NULL 
 /*含税单价*/  ,
nmny decimal(28,8) NULL 
 /*金额*/  ,
ntaxman decimal(28,8) NULL 
 /*税额*/  ,
ntaxrate decimal(28,8) NULL 
 /*税率*/  ,
nsummny decimal(28,8) NULL 
 /*价税合计*/  ,
ncurprice decimal(28,8) NULL 
 /*原币单价*/  ,
ncurtaxprice decimal(28,8) NULL 
 /*原币含税单价*/  ,
ncurmny decimal(28,8) NULL 
 /*原币金额*/  ,
ncurtaxmny decimal(28,8) NULL 
 /*原币税金*/  ,
ncursummny decimal(28,8) NULL 
 /*原币价税合计*/  ,
ntotaldealnum decimal(28,8) NULL 
 /*累计核销数量*/  ,
ntotaldealmny decimal(28,8) NULL 
 /*累计核销金额*/  ,
irowclose char(1) NULL 
 /*行关闭*/  ,
vrowmemo varchar(200) NULL 
 /*行备注*/  ,
vbdef1 varchar(100) NULL 
 /*表体自定义项1*/  ,
vbdef2 varchar(50) NULL 
 /*表体自定义项2*/  ,
vbdef3 varchar(50) NULL 
 /*表体自定义项3*/  ,
vbdef4 varchar(50) NULL 
 /*表体自定义项4*/  ,
vbdef5 varchar(50) NULL 
 /*表体自定义项5*/  ,
vbdef6 varchar(50) NULL 
 /*表体自定义项6*/  ,
vbdef7 varchar(50) NULL 
 /*表体自定义项7*/  ,
vbdef8 varchar(50) NULL 
 /*表体自定义项8*/  ,
vbdef9 varchar(50) NULL 
 /*表体自定义项9*/  ,
vbdef10 varchar(50) NULL 
 /*表体自定义项10*/  ,
crowno varchar(50) NULL 
 /*发票行号*/  ,
ctaxinvoiceid char(20) NULL 
 /*税务发票主表_主键*/  ,
CONSTRAINT PK_SO_TAXINVOICE_B PRIMARY KEY (ctaxinvoice_bid),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
go 


/* tablename: 税务发票子子表 */

create table so_taxinvoice_bb1 (
ctaxinvoice_bbid char(20) NOT NULL 
 /*发票核销明细id*/  ,
csourcebilltype char(20) NULL 
 /*对方单据类型*/  ,
csourcebillid char(20) NULL 
 /*对方单据表头id*/  ,
csourcebillrowid char(20) NULL 
 /*对方单据表体id*/  ,
ddealdate char(10) NULL 
 /*核销日期*/  ,
ndealnum decimal(28,8) NULL 
 /*核销数量*/  ,
ndealmny decimal(28,8) NULL 
 /*核销金额*/  ,
cdealoper char(20) NULL 
 /*核销操作人*/  ,
nwriteinvoicenum decimal(28,8) NULL 
 /*核销虚拟发票数量*/  ,
nwriteinvoicemny decimal(28,8) NULL 
 /*核销虚拟发票金额*/  ,
ctaxinvoiceid char(20) NULL 
 /*税务发票主键*/  ,
ctaxinvoice_bid char(20) NULL 
 /*发票子表主键*/  ,
CONSTRAINT PK_SO_TAXINVOICE_BB1 PRIMARY KEY (ctaxinvoice_bbid),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
go 


