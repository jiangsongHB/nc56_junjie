--ɾ����������ҳǩ��¼
delete pub_billtemplet_t t where t.pk_billtemplet=(select a.pk_billtemplet from pub_billtemplet a where a.pk_billtypecode='4A' and a.bill_templetname='SYSTEM');


--����ע�����ҳǩ˳��
insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, 'IC_BILL_TEMPLET_004A', '0001JJ10000000005V5Y', 1, 1, '', 'jj_scm_informationcost', 4, '������Ϣ', '2010-11-19 16:09:18', '', '', '');

insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, 'IC_BILL_TEMPLET_004A', '1004AA10000000000669', 0, null, '', 'main', 0, '����', '2010-11-19 16:04:17', '', '', '');

insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, 'IC_BILL_TEMPLET_004A', '1004AA1000000000066A', 1, 1, '', 'table', 0, '�����Ϣ', '2010-11-19 16:04:17', '', '', '');

insert into pub_billtemplet_t (BASETAB, DR, METADATACLASS, METADATAPATH, MIXINDEX, PK_BILLTEMPLET, PK_BILLTEMPLET_T, POS, POSITION, RESID, TABCODE, TABINDEX, TABNAME, TS, VDEF1, VDEF2, VDEF3)
values ('', 0, '', '', null, 'IC_BILL_TEMPLET_004A', '1004AA1000000000066B', 2, null, '', 'tail', 0, '����', '2010-11-19 16:04:17', '', '', '');

