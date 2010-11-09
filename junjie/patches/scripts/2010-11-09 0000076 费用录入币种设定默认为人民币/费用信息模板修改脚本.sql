/*
*
*               警告: 此脚本操作也可在模板定义中实现.目的是在币种数据项中添加一项显示公式,请自行决定实现方法.
*
*/




--获取当前系统费用信息模板主键
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001';
--删除当前已定义的币种数据项定义
delete pub_billtemplet_b t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001') and t.itemkey='currtype';
--插入数据项新定义
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 5, '币种', '', 0, 1, 'currtypeid->getColValue(bd_currtype,pk_currtype,pk_currtype,currtypeid);currtype->getColValue(bd_currtype,currtypename,pk_currtype,currtypeid)', -1, 'currtypeid', 20, 'currtype', 0, 'N', 1, 'Y', 'currtypeid->"00010000000000000001";currtype->getColValue(bd_currtype,currtypename,pk_currtype,currtypeid)', 0, '', '', '', 'N', 1, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001'), '0001ZZ10000000004PRN', '@@@@', 0, '外币档案', '', '', 'N', 1, 8, to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '费用信息表', 0, '2010-11-09 11:11:51', 'N', '', '', '', 1, 1, 'N', 1, '', 1);
