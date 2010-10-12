补丁提交人: Meichao  Date: 2010-10-11   修改日期: 2010-10-12

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '数量', '', 0, 1, 'nassistnum->iif(nconvertrate==null,null ,div( nordernum,nconvertrate ) )', 0, '', 20, 'nordernum', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 1, '<root><tab code="table_arr" showflag="N" listshowflag="N" /><tab code="table_pol" showflag="N" listshowflag="N" /><tab code="table_exe" showflag="N" listshowflag="N" /></root>', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3W', '@@@@', 1, ',,,,Y,', '', '', 'Y', 1, 150, 'table', '存货信息', 1, '2010-10-11 15:02:03', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

以上为从Oracle中导出的语句,实际情况并不能执行,但其反应出了模板的修改位置.

根据以上语句,修改出的update更新语句. 
说明: 在单据类型"21"下的模板表体的数据项"nordernum"的editformula列添加编辑公式:nassistnum->iif(nconvertrate==null,null ,div( nordernum,nconvertrate ) )
此操作也可在产品模板初始化界面进行.二者作用是等效的.

update pub_billtemplet_b t set t.editformula='nassistnum->iif(nconvertrate==null,null ,div( nordernum,nconvertrate ) )' where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21'and t.pk_corp='0001') and t.itemkey='nordernum'