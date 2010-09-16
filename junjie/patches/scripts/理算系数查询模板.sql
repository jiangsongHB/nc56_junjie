insert into pub_query_templet (DESCRIBE, DR, FIXCONDITION, ID, METACLASS, MODEL_CODE, MODEL_NAME, NODE_CODE, PK_CORP, RESID, TS)
values ('', null, '', '0001ZZ10000000000EP0', '', '40950101', '理算系数查询模板', '40950101', '@@@@', '', '2010-08-05 14:15:38');

insert into pub_query_condition (CONSULT_CODE, DATA_TYPE, DISP_SEQUENCE, DISP_TYPE, DISP_VALUE, DR, FIELD_CODE, FIELD_NAME, GUIDELINE, ID, IF_AUTOCHECK, IF_DATAPOWER, IF_DEFAULT, IF_DESC, IF_GROUP, IF_IMMOBILITY, IF_MUST, IF_ORDER, IF_SUM, IF_USED, INSTRUMENTSQL, ISCONDITION, MAX_LENGTH, OPERA_CODE, OPERA_NAME, ORDER_SEQUENCE, PK_CORP, PK_TEMPLET, PRERESTRICT, RESID, RETURN_TYPE, TABLE_CODE, TABLE_NAME, TS, USERDEFFLAG, VALUE)
values ('存货基本档案', 5, 100, 0, '', 0, 'jj_bd_adjustmentcoefficient.invname', '存货名称', '', '0001ZZ10000000000EP3', 'N', 'N', 'Y', '', 'N', 'N', 'N', 'N', 'N', 'Y', '', 'Y', null, '=@>@>=@<@<=@like@', '等于@大于@大于等于@小于@小于等于@相似@', 0, '@@@@', '0001ZZ10000000000EP0', '', '', 0, '', '', '2010-08-05 10:48:49', '', '');

insert into pub_query_condition (CONSULT_CODE, DATA_TYPE, DISP_SEQUENCE, DISP_TYPE, DISP_VALUE, DR, FIELD_CODE, FIELD_NAME, GUIDELINE, ID, IF_AUTOCHECK, IF_DATAPOWER, IF_DEFAULT, IF_DESC, IF_GROUP, IF_IMMOBILITY, IF_MUST, IF_ORDER, IF_SUM, IF_USED, INSTRUMENTSQL, ISCONDITION, MAX_LENGTH, OPERA_CODE, OPERA_NAME, ORDER_SEQUENCE, PK_CORP, PK_TEMPLET, PRERESTRICT, RESID, RETURN_TYPE, TABLE_CODE, TABLE_NAME, TS, USERDEFFLAG, VALUE)
values ('-99', 0, 100, 0, '', null, 'jj_bd_adjustmentcoefficient.vinvspec', '规格', '', '0001ZZ10000000000EP4', 'N', 'N', 'Y', '', 'N', 'N', 'N', 'N', 'N', 'Y', '', 'Y', null, '=@>@>=@<@<=@like@', '等于@大于@大于等于@小于@小于等于@包含@', 0, '@@@@', '0001ZZ10000000000EP0', '', '', 0, '', '', '2010-08-05 14:15:38', '', '');

insert into pub_query_condition (CONSULT_CODE, DATA_TYPE, DISP_SEQUENCE, DISP_TYPE, DISP_VALUE, DR, FIELD_CODE, FIELD_NAME, GUIDELINE, ID, IF_AUTOCHECK, IF_DATAPOWER, IF_DEFAULT, IF_DESC, IF_GROUP, IF_IMMOBILITY, IF_MUST, IF_ORDER, IF_SUM, IF_USED, INSTRUMENTSQL, ISCONDITION, MAX_LENGTH, OPERA_CODE, OPERA_NAME, ORDER_SEQUENCE, PK_CORP, PK_TEMPLET, PRERESTRICT, RESID, RETURN_TYPE, TABLE_CODE, TABLE_NAME, TS, USERDEFFLAG, VALUE)
values ('-99', 2, 100, 0, '', 0, 'jj_bd_adjustmentcoefficient.nadjustmentcoefficient', '理算系数', '', '0001ZZ10000000000EP5', 'N', 'N', 'Y', '', 'N', 'N', 'N', 'N', 'N', 'Y', '', 'Y', null, '=@>@>=@<@<=@like@', '等于@大于@大于等于@小于@小于等于@相似@', 0, '@@@@', '0001ZZ10000000000EP0', '', '', 0, '', '', '2010-08-05 10:48:49', '', '');



