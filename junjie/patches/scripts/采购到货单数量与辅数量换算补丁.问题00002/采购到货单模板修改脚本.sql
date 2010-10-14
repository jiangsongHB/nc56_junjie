update pub_billtemplet_b t set t.listshowflag='Y',t.showflag='1' where t.pk_billtemplet='40040301010000000000'and t.table_name='基本信息' and t.itemkey in ('cassistunitname' ,'convertrate', 'nassistnum')
;
update pub_billtemplet_b t set t.editformula='nassistnum->iif(convertrate==null,null , narrvnum/convertrate)' where t.pk_billtemplet='40040301010000000000'and t.table_name='基本信息' and t.itemkey='narrvnum'
