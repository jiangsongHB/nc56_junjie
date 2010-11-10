/**
 何意求
 2010-10-26
 根据单据类型删除单据模板

*/


select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02');

select * from pub_billtemplet_b  b where b.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));



select * from pub_billtemplet_t c where c.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));

-----------------------以下为删除脚本-----------------------------------

delete pub_billtemplet_b  b where b.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));

delete pub_billtemplet_t c where c.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));

delete pub_billtemplet where pk_billtypecode in('H001','H002','H003','H004','MD01','MD02');


