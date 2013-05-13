delete from pub_alerttype e where e.pk_alerttype='1004AA10000000023N2R';
insert into pub_alerttype (BELONG_SYSTEM, BIZCONFIGCLASS, BUSI_PLUGIN, DESCRIP_RESID, DESCRIPTION, DR, NAME_RESID, PK_ALERTTYPE, TASKTYPE, TS, TYPE_NAME)
values ('so', '', 'nc.bs.so.backtask.soremain.SaleStayOrderClose', '', '留货到期自动关闭', 0, '', '1004AA10000000023N2R', 0, '2013-05-14 11:11:26', '留货单到期未发货自动整单关闭预警');

