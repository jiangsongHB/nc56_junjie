delete from pub_billtemplet_b b where b.pk_billtemplet_b in ('1004121000000001SIJ9','1004121000000001SIJH');

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 4, '是否钢厂重量', 'false', 0, 1, '', -1, '', 20, 'isgczl', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10001230003VP3', '1004121000000001SIJ9', '@@@@', 0, '', '', '', 'N', 1, 11, 'head', '码单入库', 0, '2012-03-20 19:18:45', 'N', '', '', '', 1, 1, 'N', 1, '', 1);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 4, '是否钢厂重量', '', 0, 1, '', -1, '', 20, 'sfgczl', null, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', '0001ZZ10001230003VP3', '1004121000000001SIJH', '', 1, '', '', '', 'N', 1, 47, 'nc_mdcrk', '码单出入库表', 0, '2012-03-20 19:18:45', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

