�����ύ��: Meichao  Date: 2010-10-11   �޸�����: 2010-10-12

insert into pub_billtemplet_b (CARDFLAG, DATATYPE, DEFAULTSHOWNAME, DEFAULTVALUE, DR, EDITFLAG, EDITFORMULA, FOREGROUND, IDCOLNAME, INPUTLENGTH, ITEMKEY, ITEMTYPE, LEAFFLAG, LISTFLAG, LISTSHOWFLAG, LOADFORMULA, LOCKFLAG, METADATAPATH, METADATAPROPERTY, METADATARELATION, NEWLINEFLAG, NULLFLAG, OPTIONS, PK_BILLTEMPLET, PK_BILLTEMPLET_B, PK_CORP, POS, REFTYPE, RESID, RESID_TABNAME, REVISEFLAG, SHOWFLAG, SHOWORDER, TABLE_CODE, TABLE_NAME, TOTALFLAG, TS, USERDEFFLAG, USERDEFINE1, USERDEFINE2, USERDEFINE3, USEREDITFLAG, USERFLAG, USERREVISEFLAG, USERSHOWFLAG, VALIDATEFORMULA, WIDTH)
values (1, 2, '����', '', 0, 1, 'nassistnum->iif(nconvertrate==null,null ,div( nordernum,nconvertrate ) )', 0, '', 20, 'nordernum', 0, 'N', 1, 'Y', '', 0, '', '', '', 'N', 1, '<root><tab code="table_arr" showflag="N" listshowflag="N" /><tab code="table_pol" showflag="N" listshowflag="N" /><tab code="table_exe" showflag="N" listshowflag="N" /></root>', '0001ZZ10000000004EZ6', '0001ZZ10000000004F3W', '@@@@', 1, ',,,,Y,', '', '', 'Y', 1, 150, 'table', '�����Ϣ', 1, '2010-10-11 15:02:03', 'N', '', '', '', 1, 1, 'N', 1, '', 100);

����Ϊ��Oracle�е��������,ʵ�����������ִ��,���䷴Ӧ����ģ����޸�λ��.

�����������,�޸ĳ���update�������. 
˵��: �ڵ�������"21"�µ�ģ������������"nordernum"��editformula����ӱ༭��ʽ:nassistnum->iif(nconvertrate==null,null ,div( nordernum,nconvertrate ) )
�˲���Ҳ���ڲ�Ʒģ���ʼ���������.���������ǵ�Ч��.

update pub_billtemplet_b t set t.editformula='nassistnum->iif(nconvertrate==null,null ,div( nordernum,nconvertrate ) )' where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='21'and t.pk_corp='0001') and t.itemkey='nordernum'