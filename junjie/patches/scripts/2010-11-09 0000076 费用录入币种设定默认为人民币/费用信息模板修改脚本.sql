/*
*
*               ����: �˽ű�����Ҳ����ģ�嶨����ʵ��.Ŀ�����ڱ��������������һ����ʾ��ʽ,�����о���ʵ�ַ���.
*
*/




--��ȡ��ǰϵͳ������Ϣģ������
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001';
--ɾ����ǰ�Ѷ���ı����������
delete pub_billtemplet_b t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001') and t.itemkey='currtype';
--�����������¶���
insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 5, '����', '', 0, 1, 'currtypeid->getColValue(bd_currtype,pk_currtype,pk_currtype,currtypeid);currtype->getColValue(bd_currtype,currtypename,pk_currtype,currtypeid)', -1, 'currtypeid', 20, 'currtype', 0, 'N', 1, 'Y', 'currtypeid->"00010000000000000001";currtype->getColValue(bd_currtype,currtypename,pk_currtype,currtypeid)', 0, '', '', '', 'N', 1, '', (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='HJ1001'), '0001ZZ10000000004PRN', '@@@@', 0, '��ҵ���', '', '', 'N', 1, 8, to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), '������Ϣ��', 0, '2010-11-09 11:11:51', 'N', '', '', '', 1, 1, 'N', 1, '', 1);
