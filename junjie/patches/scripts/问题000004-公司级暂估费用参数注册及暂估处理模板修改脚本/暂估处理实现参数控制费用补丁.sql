--���ϵͳ����ģ����--����: PO85
insert into pub_sysinittemp (AFTERCLASS, CHECKCLASS, CORPFLAG, DATACLASS, DEFAULTVALUE, DOMAINFLAG, DR, EDITCLASS, EDITCOMPONENTCTRLCLASS, GROUPCODE, GROUPNAME, INITCODE, INITNAME, INITTYPE, JZFLAG, MAINFLAG, MUTEXFLAG, PARATYPE, PK_REFINFO, PK_SYSINITTEMP, REMARK, SHOWFLAG, STATEFLAG, SYSFLAG, SYSINDEX, TS, VALUELIST, VALUETYPE)
values ('', '', 0, '', '0', '4004', 0, '', '', 'PO33', '�ݹ�', 'PO85', '��ⵥ�����ݹ�����Ĭ�Ϸ��ö���', 2, 'N', 'N', 0, '2', '', '40040000000000000085', '�����������ñ���(��ȷ���˱����Ӧ�������)', 'Y', 0, 'N', 85, to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '', 2);


--Ϊ�¼���˾����ݹ����ò����趨��(1002��˾)
insert into pub_sysinit (CONTROLFLAG, DR, EDITFLAG, INITCODE, INITNAME, MAKEDATE, PK_CORP, PK_ORG, PK_SYSINIT, PKVALUE, SYSINIT, TS, VALUE)
values ('0', null, 'Y', 'PO85', '��ⵥ�����ݹ�����Ĭ�Ϸ��ö���', '2010-10-15', '', '1002', '0001A91000000000AAAB', '', '40040000000000000085', to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '');
--Ϊ�¼���˾����ݹ����ò����趨��(1003��˾)
insert into pub_sysinit (CONTROLFLAG, DR, EDITFLAG, INITCODE, INITNAME, MAKEDATE, PK_CORP, PK_ORG, PK_SYSINIT, PKVALUE, SYSINIT, TS, VALUE)
values ('0', null, 'Y', 'PO85', '��ⵥ�����ݹ�����Ĭ�Ϸ��ö���', '2010-10-15', '', '1003', '0001A91000000000AAAC', '', '40040000000000000085', to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '');
--Ϊ�¼���˾����ݹ����ò����趨��(1004��˾)
insert into pub_sysinit (CONTROLFLAG, DR, EDITFLAG, INITCODE, INITNAME, MAKEDATE, PK_CORP, PK_ORG, PK_SYSINIT, PKVALUE, SYSINIT, TS, VALUE)
values ('0', null, 'Y', 'PO85', '��ⵥ�����ݹ�����Ĭ�Ϸ��ö���', '2010-10-15', '', '1004', '0001A91000000000AAAD', '', '40040000000000000085', to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '');
--Ϊ�¼���˾����ݹ����ò����趨��(1005��˾)
insert into pub_sysinit (CONTROLFLAG, DR, EDITFLAG, INITCODE, INITNAME, MAKEDATE, PK_CORP, PK_ORG, PK_SYSINIT, PKVALUE, SYSINIT, TS, VALUE)
values ('0', null, 'Y', 'PO85', '��ⵥ�����ݹ�����Ĭ�Ϸ��ö���', '2010-10-15', '', '1005', '0001A91000000000AAAE', '', '40040000000000000085', to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '');
--Ϊ�¼���˾����ݹ����ò����趨��(1006��˾)
insert into pub_sysinit (CONTROLFLAG, DR, EDITFLAG, INITCODE, INITNAME, MAKEDATE, PK_CORP, PK_ORG, PK_SYSINIT, PKVALUE, SYSINIT, TS, VALUE)
values ('0', null, 'Y', 'PO85', '��ⵥ�����ݹ�����Ĭ�Ϸ��ö���', '2010-10-15', '', '1006', '0001A91000000000AAAF', '', '40040000000000000085', to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '');


--�����ݹ�����ģ��
delete from pub_billtemplet_b t where t.pk_billtemplet='40040503010000000000' and t.itemkey='cfycode';
--�����ݹ�����ģ��
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, '���ñ���', '', 0, 1, 'cfyname->getColValue(bd_invbasdoc,invname,invcode,cfycode)', -1, '', 20, 'cfycode', 0, 'N', 1, 'Y', 'getcolvalue2(pub_sysinit , value, initcode,PO85, pk_org, 1002);cfyname->getColValue(bd_invbasdoc,invname,invcode,cfycode);cfeeid->getcolvalue2(bd_invmandoc , pk_invmandoc, pk_invbasdoc, getColValue(bd_invbasdoc,pk_invbasdoc,invcode,cfycode), pk_corp, 1002)', 0, '', '', '', 'N', 0, '<root><tab code="zgyf_table" showflag="N" listshowflag="N" /></root>', '40040503010000000000', '4004050301000000UZQV', '@@@@', 1, '', 'UC000-0003868', '', 'N', 1, 70, 'table', '�ݹ��ɱ�', 0, '2010-10-15 19:27:07', 'N', '', '', '', 1, 1, 'N', 1, '', 100);
--�����ݹ�����ģ��
delete from pub_billtemplet_b t where t.pk_billtemplet='40040503010000000000' and t.itemkey='cfeeid';
--�����ݹ�����ģ��
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 0, 'cfeeid', '', 0, 1, '', -1, '', 20, 'cfeeid', 0, 'N', 1, 'N', '', 0, '', '', '', 'N', 0, '', '40040503010000000000', '4004050301000000UZQX', '@@@@', 1, '', '', '', 'N', 0, 70, 'table', '�ݹ��ɱ�', 0, to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 'N', '', '', '', 1, 1, 'N', 1, '', 100);
