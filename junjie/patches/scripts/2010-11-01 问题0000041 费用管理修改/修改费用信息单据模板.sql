--获取费用信息单据模板的PK
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001';
--修改费用信息单据模板中的单价和数量的显示顺序.
update pub_billtemplet_b t set t.showorder='16' where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001')and t.itemkey='nnumber';
update pub_billtemplet_b t set t.showorder='17' where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001')and t.itemkey='noriginalcurprice';
--更新自定义字段1 2 3 的显示规则.
--先删除
delete pub_billtemplet_b t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001')and t.itemkey in ('vdef1','vdef2','vdef3');
--后插入
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '车船号', '', 0, 1, '', -1, '', 50, 'vdef1', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001'), '0001ZZ10000000001TLI', '@@@@', 0, '', '', '', 'N', 1, 33, 'jj_scm_informationcost', '费用信息表', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 1);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '司机', '', 0, 1, '', -1, '', 50, 'vdef2', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001'), '0001ZZ10000000001TLP', '@@@@', 0, '', '', '', 'N', 1, 35, 'jj_scm_informationcost', '费用信息表', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 1);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '付往方向', '', 0, 1, '', -1, '', 50, 'vdef3', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001'), '0001ZZ10000000001TLJ', '@@@@', 0, '', '', '', 'N', 1, 36, 'jj_scm_informationcost', '费用信息表', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 1);

