create or replace view v_ic_onhandnum7 as
select cwarehouseid1,cinvbasid1,ninassistnum,ninnum,noutassistnum,noutnum,currentrealnum
 from (select *
  from (select t.cwarehouseid cwarehouseid1,
               t.cinvbasid cinvbasid1,
               sum(t.nonhandastnum) nonhandastnum,
               sum(t.nonhandnum) nonhandnum
          from ic_onhandnum t where t.dr=0
         group by t.cinvbasid, t.cwarehouseid,t.pk_corp
         ) m,
       (select *
          from (select t.cbodywarehouseid cbodywarehouseid2,
                       t.cinvbasid cinvbasid2,
                       sum(t.ninassistnum) ninassistnum,
                       sum(t.ninnum) ninnum,
                       sum(t.noutassistnum) noutassistnum,
                       sum(t.noutnum) noutnum,
                       sum(t.vuserdef19) arrivenum,
                       nvl(sum(t.vuserdef19),0)-nvl(sum(t.noutnum),0) currentrealnum
                  from ic_general_b t
                 where t.cgeneralhid in (select t.cgeneralhid
                                           from ic_general_h t
                                          where t.fbillflag <> 1
                                            and t.dr = 0)
                   and t.dr = 0
                 group by t.cinvbasid, t.cbodywarehouseid,t.pk_corp)
         order by ninnum) n

 where m.cwarehouseid1 = n.cbodywarehouseid2
   and m.cinvbasid1 = n.cinvbasid2
);
