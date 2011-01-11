create or replace view testview1 as
select "VBILLCODE","GRAPHID","NINNUM"
    from (select t.vbillcode,ttt.graphid,tt.ninnum from ic_general_h t left join ic_general_b tt on t.cgeneralhid=tt.cgeneralhid and tt.dr=0 and nvl(tt.ncorrespondnum,0)<tt.ninnum left join bd_invbasdoc ttt on tt.cinvbasid=ttt.pk_invbasdoc and tt.dr=0 and ttt.dr=0 where t.dr=0 and t.vuserdef20='Y' and t.fbillflag=3  order by t.vbillcode);