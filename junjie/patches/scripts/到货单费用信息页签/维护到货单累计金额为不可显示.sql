/*维护到货单*/
update pub_billtemplet_b
   set listshowflag = 'N', showflag = '0'
 where pk_billtemplet = '40040301010000000000'
   and table_code = 'jj_scm_informationcost'
   and itemkey = 'ninvoriginalcurmny';
