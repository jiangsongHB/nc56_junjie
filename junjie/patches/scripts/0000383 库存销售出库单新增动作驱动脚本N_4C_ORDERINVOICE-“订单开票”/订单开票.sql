delete from pub_billaction where PK_BILLACTION='1004891000000000C9D0';
delete from pub_busiclass where PK_BUSICLASS='NC55AA10000000004C07';
insert into pub_busiclass (ACTIONTYPE, CLASSNAME, DR, ISBEFORE, PK_BILLTYPE, PK_BUSICLASS, PK_BUSINESSTYPE, PK_CORP, TS)
values ('ORDERINVOICE', 'N_4C_ORDERINVOICE', 0, 'N', '4C', 'NC55AA10000000004C07', '', '', '2006-04-10 20:07:07');

insert into pub_billaction (ACTIONNOTE, ACTIONSTYLE, ACTIONSTYLEREMARK, ACTIONTYPE, CONSTRICTFLAG, CONTROLFLAG, DR, FINISHFLAG, PK_BILLACTION, PK_BILLTYPE, SHOWHINT, TS)
values ('������Ʊ', '0', '', 'ORDERINVOICE', 'Y', 'Y', null, 'Y', '1004891000000000C9D0', '4C', '', '2011-07-01 10:34:09');

