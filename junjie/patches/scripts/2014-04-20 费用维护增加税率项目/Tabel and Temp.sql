alter jj_scm_informationcost 
add ntaxrate NUMBER(20,8);

insert into pub_billtemplet_b(CARDFLAG,DATATYPE,DEFAULTSHOWNAME,DEFAULTVALUE,DR,EDITFLAG,EDITFORMULA,FOREGROUND,IDCOLNAME,INPUTLENGTH,ITEMKEY,ITEMTYPE,LEAFFLAG,LISTFLAG,LISTSHOWFLAG,LOADFORMULA,LOCKFLAG,METADATAPATH,METADATAPROPERTY,METADATARELATION,NEWLINEFLAG,NULLFLAG,OPTIONS,PK_BILLTEMPLET,PK_BILLTEMPLET_B,PK_CORP,POS,REFTYPE,RESID,RESID_TABNAME,REVISEFLAG,SHOWFLAG,SHOWORDER,TABLE_CODE,TABLE_NAME,TOTALFLAG,TS,USERDEFFLAG,USERDEFINE1,USERDEFINE2,USERDEFINE3,USEREDITFLAG,USERFLAG,USERREVISEFLAG,USERSHOWFLAG,VALIDATEFORMULA,WIDTH) values( 1,2,'税率',null,0,1,null,-1,null,20,'ntaxrate',0,'N',1,'Y',null,0,null,null,null,'N',1,null,'0001ZZ10000000001TLB','0001AA1000000001BR58','@@@@',0,'2,,',null,null,'N',1,49,'jj_scm_informationcost','费用信息表',0,'2014-04-21 15:37:53','N',null,null,null,1,1,'N',1,null,1) ;

--入库单等其他需要维护税率的单据模板上手工增加费用页签的项目 (ntaxrate) 类型为数字，两位小数