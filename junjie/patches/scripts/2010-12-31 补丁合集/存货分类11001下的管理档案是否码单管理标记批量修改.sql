update  bd_invmandoc t set t.def18='Y',t.def19='Y',t.def20='Y'  where rownum<200 and t.pk_corp='1004' and t.dr=0 and t.pk_invbasdoc in(select pk_invbasdoc from bd_invbasdoc where invcode like '11001%');