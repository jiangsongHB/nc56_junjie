/**
 ������
 2010-10-26
 ���ݵ�������ɾ������ģ��

*/


select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02');

select * from pub_billtemplet_b  b where b.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));



select * from pub_billtemplet_t c where c.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));

-----------------------����Ϊɾ���ű�-----------------------------------

delete pub_billtemplet_b  b where b.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));

delete pub_billtemplet_t c where c.pk_billtemplet in (select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode in('H001','H002','H003','H004','MD01','MD02'));

delete pub_billtemplet where pk_billtypecode in('H001','H002','H003','H004','MD01','MD02');


