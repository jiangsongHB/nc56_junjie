/*
*   很简单就一句,在模板管理中就可以实现的功能. 如语句报错,请至 模板管理->D1->将zgyf字段的下拉内容更新为: 暂估=1,正常=0,回冲=2即可
*/
update pub_billtemplet_b t set t.reftype='IX,暂估=1,正常=0,回冲=2' where t.pk_billtemplet=(select t.pk_billtemplet from pub_billtemplet t where t.pk_billtypecode='D1') and t.defaultshowname='暂估应付标志';