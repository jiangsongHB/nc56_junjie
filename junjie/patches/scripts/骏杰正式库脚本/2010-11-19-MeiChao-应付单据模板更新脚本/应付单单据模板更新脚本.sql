/*
*   �ܼ򵥾�һ��,��ģ������оͿ���ʵ�ֵĹ���. ����䱨��,���� ģ�����->D1->��zgyf�ֶε��������ݸ���Ϊ: �ݹ�=1,����=0,�س�=2����
*/
update pub_billtemplet_b t set t.reftype='IX,�ݹ�=1,����=0,�س�=2' where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='D1') and t.defaultshowname='�ݹ�Ӧ����־';