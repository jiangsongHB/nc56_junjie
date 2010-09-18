delete from pub_billtemplet_b where table_code = 'jj_scm_informationcost' and pk_billtemplet = '0001ZZ10000000004EZ6';

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '单据主键', '', 0, 0, '', -1, '', 50, 'cbillid', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F25', '@@@@', 1, '', '', '', 'N', 0, 1, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '计量单位', '', 0, 0, '', -1, '', 50, 'cmeasdocid', 0, 'N', 1, 'N', 'mea->getColValue(bd_measdoc,measname , pk_measdoc, cmeasdocid)', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2D', '@@@@', 1, '', '', '', 'N', 0, 2, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '费用编码', '', 0, 0, '', -1, '', 50, 'costcode', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2E', '@@@@', 1, '', '', '', 'N', 1, 3, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '费用名称', '', 0, 0, '', -1, '', 50, 'costname', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2F', '@@@@', 1, '', '', '', 'N', 1, 4, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '费用单位', '', 0, 0, '', -1, '', 50, 'ccostunitid', 0, 'N', 1, 'N', 'cusbasdocid->getColValue(bd_cumandoc,pk_cubasdoc , pk_cumandoc,ccostunitid );costunit->getColValue(bd_cubasdoc, custname,pk_cubasdoc,cusbasdocid )', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2G', '@@@@', 1, '', '', '', 'N', 0, 6, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '本币含税单价', '', 0, 0, '', -1, '', 50, 'ntaxprice', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2I', '@@@@', 1, '', '', '', 'N', 0, 8, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '本币无税单价', '', 0, 0, '', -1, '', 50, 'nprice', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2J', '@@@@', 1, '', '', '', 'N', 0, 9, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '本币无税金额', '', 0, 0, '', -1, '', 50, 'nmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2K', '@@@@', 1, '', '', '', 'N', 0, 10, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '本币价税合计', '', 0, 0, '', -1, '', 50, 'nsummny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2L', '@@@@', 1, '', '', '', 'N', 0, 11, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '扣税类别', '', 0, 0, '', -1, '', 50, 'vtaxtype', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2M', '@@@@', 1, '', '', '', 'N', 0, 12, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '税目税率', '', 0, 0, '', -1, '', 50, 'pk_taxitems', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2O', '@@@@', 1, '', '', '', 'N', 0, 13, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 1, '删除标志', '', 0, 0, '', -1, '', 50, 'dr', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2P', '@@@@', 1, '', '', '', 'N', 0, 14, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计入库费用本币无税金额', '', 0, 0, '', -1, '', 50, 'ninstoremny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2Q', '@@@@', 1, '', '', '', 'N', 0, 15, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计入库费用金额', '', 0, 0, '', -1, '', 50, 'ninstoreoriginalcurmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2R', '@@@@', 1, '', '', '', 'N', 0, 16, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计入库费用含税金额', '', 0, 0, '', -1, '', 50, 'ninstoreoriginalcurtaxmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2S', '@@@@', 1, '', '', '', 'N', 0, 17, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计入库费用本币含税金额', '', 0, 0, '', -1, '', 50, 'ninstoretaxmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2T', '@@@@', 1, '', '', '', 'N', 0, 18, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计到货费本币无税金额', '', 0, 0, '', -1, '', 50, 'ninvmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2U', '@@@@', 1, '', '', '', 'N', 0, 19, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计到货费金额', '', 0, 0, '', -1, '', 50, 'ninvoriginalcurmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2V', '@@@@', 1, '', '', '', 'N', 0, 20, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计到货费含税金额', '', 0, 0, '', -1, '', 50, 'ninvoriginalcurtaxmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2W', '@@@@', 1, '', '', '', 'N', 0, 21, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '累计到货费本币含税金额', '', 0, 0, '', -1, '', 50, 'ninvtaxmny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2Y', '@@@@', 1, '', '', '', 'N', 0, 22, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '单价', '', 0, 0, '', -1, '', 50, 'noriginalcurprice', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2Z', '@@@@', 1, '', '', '', 'N', 1, 24, 'jj_scm_informationcost', 'jj_scm_informationcost', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '数量', '', 0, 0, '', -1, '', 50, 'nnumber', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F30', '@@@@', 1, '', '', '', 'N', 1, 25, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '计量单位', '', 0, 1, '', -1, '', 20, 'mea', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F2H', '@@@@', 1, '', '', '', 'N', 1, 7, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 4, '是否金额录入', '', 0, 1, '', -1, '', 1, 'ismny', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004Y8Y', '@@@@', 1, '', '', '', 'N', 1, 23, 'jj_scm_informationcost', 'jj_scm_informationcost', 0, '2010-09-14 14:11:22', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '金额', '', 0, 0, '', -1, '', 50, 'noriginalcurmny', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F31', '@@@@', 1, '', '', '', 'N', 1, 26, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '币种', '', 0, 0, '', -1, '', 50, 'currtypeid', 0, 'N', 1, 'N', 'currname->getColValue(bd_currtype,currtypename,pk_currtype,currtypeid)', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F32', '@@@@', 1, '', '', '', 'N', 0, 27, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '含税单价', '', 0, 0, '', -1, '', 50, 'noriginalcurtaxprice', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F33', '@@@@', 1, '', '', '', 'N', 0, 29, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '费用信息表主键', '', 0, 1, '', -1, '', 50, 'pk_informantioncost', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F34', '@@@@', 1, '', '', '', 'N', 0, 30, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '时间戳', '', 0, 0, '', -1, '', 50, 'ts', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F35', '@@@@', 1, '', '', '', 'N', 0, 31, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '费用类型', '', 0, 0, '', -1, '', 50, 'vcosttype', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F36', '@@@@', 1, '', '', '', 'N', 1, 32, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项1', '', 0, 0, '', -1, '', 50, 'vdef1', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F37', '@@@@', 1, '', '', '', 'N', 0, 33, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项10', '', 0, 0, '', -1, '', 50, 'vdef10', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F39', '@@@@', 1, '', '', '', 'N', 0, 34, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项2', '', 0, 0, '', -1, '', 50, 'vdef2', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3A', '@@@@', 1, '', '', '', 'N', 0, 35, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项3', '', 0, 0, '', -1, '', 50, 'vdef3', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3B', '@@@@', 1, '', '', '', 'N', 0, 36, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项4', '', 0, 0, '', -1, '', 50, 'vdef4', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3C', '@@@@', 1, '', '', '', 'N', 0, 37, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项5', '', 0, 0, '', -1, '', 50, 'vdef5', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3D', '@@@@', 1, '', '', '', 'N', 0, 38, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项6', '', 0, 0, '', -1, '', 50, 'vdef6', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3E', '@@@@', 1, '', '', '', 'N', 0, 39, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项7', '', 0, 0, '', -1, '', 50, 'vdef7', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3F', '@@@@', 1, '', '', '', 'N', 0, 40, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项8', '', 0, 0, '', -1, '', 50, 'vdef8', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3G', '@@@@', 1, '', '', '', 'N', 0, 41, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '自定义项9', '', 0, 0, '', -1, '', 50, 'vdef9', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3H', '@@@@', 1, '', '', '', 'N', 0, 42, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '备注', '', 0, 0, '', -1, '', 50, 'vmemo', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3I', '@@@@', 1, '', '', '', 'N', 1, 43, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '价税合计', '', 0, 0, '', -1, '', 50, 'noriginalcursummny', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 1, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3K', '@@@@', 1, '', '', '', 'N', 0, 44, 'jj_scm_informationcost', 'jj_scm_informationcost', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '费用单位', '', 0, 1, '', -1, '', 20, 'costunit', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004WPH', '@@@@', 1, '', '', '', 'N', 1, 5, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, 'cusbasdocid', '', 0, 1, '', -1, '', 20, 'cusbasdocid', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004WPU', '@@@@', 1, '', '', '', 'N', 0, 45, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '币种', '', 0, 1, '', -1, '', 20, 'currname', null, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10000000004EZ6', '0001ZZ10000000004PR7', '', 1, '', '', '', 'N', 1, 28, 'jj_scm_informationcost', '费用信息', 0, '2010-09-14 14:11:21', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

