/* tablename: 码单现存量主表 */
create table nc_mdxcl ( 
pk_corp char(4) null 
/*公司*/,
def5 char(1) null 
/*备用字段5*/,
dmakedate char(10) null 
/*制单日期*/,
voperatorid char(20) null 
/*制单人*/,
def11 varchar(50) null 
/*备用字段11*/,
cinvbasid char(20) null 
/*存货基本档案*/,
def14 varchar(200) null 
/*备用字段14*/,
def7 varchar(20) null 
/*备用字段7*/,
def6 varchar(20) null 
/*备用字段6*/,
def15 varchar(2000) null 
/*备用字段15*/,
sum_zhishu decimal(20,8) null 
/*结存支数*/,
def10 varchar(20) null 
/*备用字段10*/,
sum_zhongliang decimal(20,8) null 
/*结存重量*/,
def13 varchar(100) null 
/*备用字段13*/,
def8 varchar(20) null 
/*备用字段8*/,
def1 decimal(20,8) null 
/*备用字段1*/,
def2 decimal(20,8) null 
/*备用字段2*/,
def12 varchar(50) null 
/*备用字段12*/,
cinventoryidb char(20) null 
/*存货管理档案*/,
cwarehouseidb char(20) null 
/*仓库PK*/,
def4 char(1) null 
/*备用字段4*/,
ccalbodyidb char(20) null 
/*库存组织*/,
def3 decimal(20,8) null 
/*备用字段3*/,
def9 varchar(20) null 
/*备用字段9*/,
pk_mdxcl char(20) not null 
/*主键*/,
ts char(19) null default convert(char(19),getdate(),20)
/*时间戳*/,
dr smallint null default 0
/*删除标记*/,
constraint pk_nc_mdxcl primary key (pk_mdxcl)
)
go

/* tablename: 码单出入库表 */
create table nc_mdcrk ( 
def5 char(1) null 
/*备用字段5*/,
md_meter decimal(20,8) null 
/*米数*/,
pk_corp char(4) null 
/*公司*/,
pk_mdxcl_b varchar(20) null 
/*码单现存量表体PK*/,
cbodybilltypecode varchar(20) null 
/*单据类型*/,
md_zlzsh varchar(200) null 
/*质保证书号*/,
crkfx smallint null 
/*出入库方向*/,
dmakedate char(10) null 
/*制单日期*/,
md_length decimal(20,8) null 
/*长度*/,
srkzs decimal(20,8) null 
/*支数*/,
voperatorid char(20) null 
/*制单人*/,
def11 varchar(50) null 
/*备用字段11*/,
def14 varchar(200) null 
/*备用字段14*/,
def7 varchar(20) null 
/*备用字段7*/,
def6 varchar(20) null 
/*备用字段6*/,
md_note varchar(200) null 
/*实测厚*宽*长*/,
def15 varchar(2000) null 
/*备用字段15*/,
md_width decimal(20,8) null 
/*宽度*/,
cgeneralbid varchar(20) null 
/*出入库单表体PK*/,
djfx smallint null 
/*单据方向*/,
def10 varchar(20) null 
/*备用字段10*/,
jbh varchar(50) null 
/*件编号*/,
md_zyh varchar(200) null 
/*资源号*/,
md_lph varchar(200) null 
/*炉批号*/,
def13 varchar(100) null 
/*备用字段13*/,
srkzl decimal(20,8) null 
/*重量*/,
def8 varchar(20) null 
/*备用字段8*/,
def1 decimal(20,8) null 
/*备用字段1*/,
remark varchar(2000) null 
/*备注*/,
cspaceid varchar(20) null 
/*货位PK*/,
def12 varchar(50) null 
/*备用字段12*/,
def2 decimal(20,8) null 
/*备用字段2*/,
pk_mdcrk char(20) not null 
/*主键*/,
sfbj char(1) null 
/*是否磅计*/,
def4 char(1) null 
/*备用字段4*/,
cwarehouseidb varchar(20) null 
/*仓库PK*/,
ccalbodyidb varchar(20) null 
/*库存组织*/,
def3 decimal(20,8) null 
/*备用字段3*/,
def9 varchar(20) null 
/*备用字段9*/,
ts char(19) null default convert(char(19),getdate(),20)
/*时间戳*/,
dr smallint null default 0
/*删除标记*/,
constraint pk_nc_mdcrk primary key (pk_mdcrk)
)
go

/* tablename: 码单锁定表 */
create table nc_mdsd ( 
pk_corp char(4) null 
/*公司*/,
def5 char(1) null 
/*备用字段5*/,
pk_mdxcl_b char(20) null 
/*码单现存量表体PK*/,
dmakedate char(10) null 
/*制单日期*/,
voperatorid char(20) null 
/*制单人*/,
sfsx char(1) null 
/*是否生效*/,
sdrq char(10) null 
/*锁定日期*/,
sdzs decimal(20,8) null 
/*锁定支数*/,
def11 varchar(50) null 
/*备用字段11*/,
def14 varchar(200) null 
/*备用字段14*/,
def7 varchar(20) null 
/*备用字段7*/,
def6 varchar(20) null 
/*备用字段6*/,
def15 varchar(2000) null 
/*备用字段15*/,
def10 varchar(20) null 
/*备用字段10*/,
sxrq char(10) null 
/*失效日期*/,
def13 varchar(100) null 
/*备用字段13*/,
def8 varchar(20) null 
/*备用字段8*/,
def1 decimal(20,8) null 
/*备用字段1*/,
def2 decimal(20,8) null 
/*备用字段2*/,
def12 varchar(50) null 
/*备用字段12*/,
def4 char(1) null 
/*备用字段4*/,
xsddbt_pk char(20) null 
/*销售订单表体PK*/,
def3 decimal(20,8) null 
/*备用字段3*/,
def9 varchar(20) null 
/*备用字段9*/,
pk_mdsd char(20) not null 
/*主键*/,
ts char(19) null default convert(char(19),getdate(),20)
/*时间戳*/,
dr smallint null default 0
/*删除标记*/,
constraint pk_nc_mdsd primary key (pk_mdsd)
)
go

/* tablename: 码单现存量子表 */
create table nc_mdxcl_b ( 
md_meter decimal(20,8) null 
/*米数*/,
def5 char(1) null 
/*备用字段5*/,
pk_mdxcl_b char(20) not null 
/*主键*/,
md_zlzsh varchar(200) null 
/*质保证书号*/,
md_length decimal(20,8) null 
/*长度*/,
def11 varchar(50) null 
/*备用字段11*/,
def14 varchar(200) null 
/*备用字段14*/,
def7 varchar(20) null 
/*备用字段7*/,
def6 varchar(20) null 
/*备用字段6*/,
md_note varchar(200) null 
/*实测厚*宽*长*/,
def15 varchar(2000) null 
/*备用字段15*/,
md_width decimal(20,8) null 
/*宽度*/,
def10 varchar(20) null 
/*备用字段10*/,
md_zyh varchar(200) null 
/*资源号*/,
jbh varchar(50) null 
/*件编号*/,
md_lph varchar(200) null 
/*炉批号*/,
def13 varchar(100) null 
/*备用字段13*/,
def8 varchar(20) null 
/*备用字段8*/,
zhishu decimal(20,8) null 
/*支数*/,
def1 decimal(20,8) null 
/*备用字段1*/,
remark varchar(2000) null 
/*备注*/,
cspaceid char(20) null 
/*货位PK*/,
zhongliang decimal(20,8) null 
/*重量*/,
def12 varchar(50) null 
/*备用字段12*/,
def2 decimal(20,8) null 
/*备用字段2*/,
def4 char(1) null 
/*备用字段4*/,
def3 decimal(20,8) null 
/*备用字段3*/,
def9 varchar(20) null 
/*备用字段9*/,
pk_mdxcl char(20) null 
/*主表主键*/,
ts char(19) null default convert(char(19),getdate(),20)
/*时间戳*/,
dr smallint null default 0
/*删除标记*/,
constraint pk_nc_mdxcl_b primary key (pk_mdxcl_b)
)
go

