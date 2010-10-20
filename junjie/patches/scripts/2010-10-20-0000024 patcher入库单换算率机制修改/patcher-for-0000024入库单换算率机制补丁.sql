--入库单换算率随实到数量而变化.
update pub_billtemplet_b t set t.editformula=null  where t.pk_billtemplet='IC_BILL_TEMPLET_0045' and t.itemkey='ninassistnum';
update pub_billtemplet_b t set t.editformula='nmny->ninnum*nprice;nplannedmny->ninnum*nplannedprice;hsl->iif(ninassistnum==null,null, ninnum/ninassistnum)'  where t.pk_billtemplet='IC_BILL_TEMPLET_0045' and t.itemkey='ninnum';