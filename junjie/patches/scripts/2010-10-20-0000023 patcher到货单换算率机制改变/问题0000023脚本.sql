--取消实到辅数量及实到数量之间的公式关系.
update pub_billtemplet_b t set t.editformula=null  where t.pk_billtemplet='40040301010000000000' and t.itemkey in ('narrvnum','nassistnum')