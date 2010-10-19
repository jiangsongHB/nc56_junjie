/* ¿â´æ²É¹ºÈë¿âµ¥*/
update pub_billtemplet_b
   set listshowflag = 'N', showflag = '0'
 where pk_billtemplet = 'IC_BILL_TEMPLET_0045'
   and table_code = 'jj_scm_informationcost'
   and itemkey = 'ninvoriginalcurmny'
    or itemkey = 'ninstoreoriginalcurmny';
