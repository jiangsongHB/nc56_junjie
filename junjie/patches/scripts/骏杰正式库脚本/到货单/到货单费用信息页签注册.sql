--��ѯģ��PK
select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='23' and t.bill_templetname='ά��������';

--ɾ��ģ��ҳǩע����Ϣ
delete pub_billtemplet_t t where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='23' and t.bill_templetname='ά��������');


--����ע��ҳǩ��Ϣ
insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='23' and t.bill_templetname='ά��������'), '0001AA10000000009AMB', 1, 1, '', 'jj_scm_informationcost', 1, '������Ϣ', '2010-10-20 09:43:46', '', '', '');

insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='23' and t.bill_templetname='ά��������'), '0001A310000000000PNX', 0, null, '', 'main', 0, '����', '2010-11-15 17:19:12', '', '', '');

insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='23' and t.bill_templetname='ά��������'), '0001A310000000000PNY', 2, null, '', 'main', 0, '����', '2010-11-15 17:19:12', '', '', '');

insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='23' and t.bill_templetname='ά��������'), '0001A310000000000PNZ', 1, 1, '', 'table', 0, '�����Ϣ', '2010-11-15 17:19:12', '', '', '');

insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='23' and t.bill_templetname='ά��������'), '0001A310000000000PO0', 2, null, '', 'tail', 1, '����', '2010-11-15 17:19:12', '', '', '');

