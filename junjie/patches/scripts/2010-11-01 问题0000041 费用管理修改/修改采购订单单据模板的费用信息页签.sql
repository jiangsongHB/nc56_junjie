/*
*
*
*    警告,由于原版的单据模板的默认模板名字被修改,导致原版费用信息页签注册脚本失效.现提交修改后版本,仅适用于正式库环境.原版在文件底部被注释
*
*/

--第一步,查询出采购订单所使用的当前单据模板的PK
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单';
--第二步,删除费用信息页签 数据项: vdef1-3的历史信息;
delete pub_billtemplet_b t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单') and t.table_code='jj_scm_informationcost' and t.itemkey in ('vdef1','vdef2','vdef3');
--第三步 ,更新自定义项1-3 为车船号,司机,付往方向.
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '车船号', '', 0, 0, '', -1, '', 50, 'vdef1', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单'), '0001ZZ10000000004F37', '@@@@', 1, '', '', '', 'N', 1, 33, 'jj_scm_informationcost', '费用信息', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '司机', '', 0, 0, '', -1, '', 50, 'vdef2', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单'), '0001ZZ10000000004F3A', '@@@@', 1, '', '', '', 'N', 1, 35, 'jj_scm_informationcost', '费用信息', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '付往方向', '', 0, 0, '', -1, '', 50, 'vdef3', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单'), '0001ZZ10000000004F3B', '@@@@', 1, '', '', '', 'N', 1, 36, 'jj_scm_informationcost', '费用信息', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);








/*
--第一步,查询出采购订单所使用的当前单据模板的PK
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单A';
--第二步,删除费用信息页签 数据项: vdef1-3的历史信息;
delete pub_billtemplet_b t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单A') and t.table_code='jj_scm_informationcost' and t.itemkey in ('vdef1','vdef2','vdef3');
--第三步 ,更新自定义项1-3 为车船号,司机,付往方向.
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '车船号', '', 0, 0, '', -1, '', 50, 'vdef1', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单A'), '0001ZZ10000000004F37', '@@@@', 1, '', '', '', 'N', 1, 33, 'jj_scm_informationcost', '费用信息', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '司机', '', 0, 0, '', -1, '', 50, 'vdef2', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单A'), '0001ZZ10000000004F3A', '@@@@', 1, '', '', '', 'N', 1, 35, 'jj_scm_informationcost', '费用信息', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '付往方向', '', 0, 0, '', -1, '', 50, 'vdef3', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='维护订单A'), '0001ZZ10000000004F3B', '@@@@', 1, '', '', '', 'N', 1, 36, 'jj_scm_informationcost', '费用信息', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

*/