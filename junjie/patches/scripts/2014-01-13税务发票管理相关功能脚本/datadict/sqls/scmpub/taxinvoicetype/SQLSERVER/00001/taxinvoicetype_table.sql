/* tablename: 税务发票类型 */
create table bd_taxinvoicetype (
pk_taxinvoicetype char(20) NOT NULL/*档案主键*/  ,
pk_corp char(20) NULL/*组织主键*/  ,
code varchar(50) NULL/*编码*/  ,
name varchar(200) NULL/*名称*/  ,
creator char(20) NULL/*创建人*/  ,
creationtime char(19) NULL/*创建日期*/  ,
modifier char(20) NULL/*修改人*/  ,
modifiedtime char(19) NULL/*修改日期*/  ,
approver char(20) NULL/*审核人*/  ,
approvedtime char(19) NULL/*审核日期*/  ,
iffee char(1) NULL/*是否费用类型*/  ,
isdefault char(1) NULL/*是否默认*/  ,
isseal char(1) NULL/*是否封存*/  ,
iapprovetype int NULL/*审核策略*/  ,
idealtype int NULL/*核销方法*/  ,
ntaxrate decimal(28,8) NULL/*税率*/  ,
CONSTRAINT PK_BD_TAXINVOICETYPE PRIMARY KEY (pk_taxinvoicetype),
ts char(19) null default convert(char(19),getdate(),20),
 dr smallint null default 0,
)
 go
