--由于原先的公式已无法生效,已将此公式移至代码中,故删除模板中的显示公式.
update pub_billtemplet_b t set t.loadformula=null where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='@4004022') and t.itemkey='cfycode'