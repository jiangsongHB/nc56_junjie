/*
*
*
*    ����,����ԭ��ĵ���ģ���Ĭ��ģ�����ֱ��޸�,����ԭ�������Ϣҳǩע��ű�ʧЧ.���ύ�޸ĺ�汾,����������ʽ�⻷��.ԭ�����ļ��ײ���ע��
*
*/

--��һ��,��ѯ���ɹ�������ʹ�õĵ�ǰ����ģ���PK
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������';
--�ڶ���,ɾ��������Ϣҳǩ ������: vdef1-3����ʷ��Ϣ;
delete pub_billtemplet_b t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������') and t.table_code='jj_scm_informationcost' and t.itemkey in ('vdef1','vdef2','vdef3');
--������ ,�����Զ�����1-3 Ϊ������,˾��,��������.
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '������', '', 0, 0, '', -1, '', 50, 'vdef1', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������'), '0001ZZ10000000004F37', '@@@@', 1, '', '', '', 'N', 1, 33, 'jj_scm_informationcost', '������Ϣ', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '˾��', '', 0, 0, '', -1, '', 50, 'vdef2', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������'), '0001ZZ10000000004F3A', '@@@@', 1, '', '', '', 'N', 1, 35, 'jj_scm_informationcost', '������Ϣ', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '��������', '', 0, 0, '', -1, '', 50, 'vdef3', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������'), '0001ZZ10000000004F3B', '@@@@', 1, '', '', '', 'N', 1, 36, 'jj_scm_informationcost', '������Ϣ', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);








/*
--��һ��,��ѯ���ɹ�������ʹ�õĵ�ǰ����ģ���PK
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������A';
--�ڶ���,ɾ��������Ϣҳǩ ������: vdef1-3����ʷ��Ϣ;
delete pub_billtemplet_b t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������A') and t.table_code='jj_scm_informationcost' and t.itemkey in ('vdef1','vdef2','vdef3');
--������ ,�����Զ�����1-3 Ϊ������,˾��,��������.
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '������', '', 0, 0, '', -1, '', 50, 'vdef1', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������A'), '0001ZZ10000000004F37', '@@@@', 1, '', '', '', 'N', 1, 33, 'jj_scm_informationcost', '������Ϣ', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '˾��', '', 0, 0, '', -1, '', 50, 'vdef2', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������A'), '0001ZZ10000000004F3A', '@@@@', 1, '', '', '', 'N', 1, 35, 'jj_scm_informationcost', '������Ϣ', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '��������', '', 0, 0, '', -1, '', 50, 'vdef3', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 0, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21' and t.bill_templetname='ά������A'), '0001ZZ10000000004F3B', '@@@@', 1, '', '', '', 'N', 1, 36, 'jj_scm_informationcost', '������Ϣ', 0, (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')), 'N', '', '', '', 1, 1, 'N', 1, '', 100);

*/