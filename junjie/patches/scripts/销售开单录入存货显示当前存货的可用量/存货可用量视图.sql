create or replace view v__jj_chkylnum
as
select sum(nvl(nonhandnum, 0)) +
       sum(nvl(nonrequirenum, 0) + nvl(nonponum, 0) + nvl(nonreceivenum, 0) +
           nvl(ntraninnum, 0) + nvl(nmponum, 0) + nvl(nmonum, 0) +
           nvl(nonwwnum, 0) + nvl(nontranspraynum, 0)) -
       sum(nvl(nonsonum, 0) + nvl(nonreceiptnum, 0) + nvl(ntranoutnum, 0) +
           nvl(npickmnum, 0) + nvl(nonpreordernum, 0)) as kyl,
       corp,
       ccalbodyid,
       cinventoryid
  from (select corp,
               ccalbodyid,
               cinventoryid,
               nmonum,
               nonrequirenum,
               nonponum,
               nonwwnum,
               nonreceivenum,
               nmponum,
               nonsonum,
               nonreceiptnum,
               npickmnum,
               ntraninnum,
               ntranoutnum,
               nontranspraynum,
               nonpreordernum,
               nonhandnum,
               nborrownum,
               nfreezenum
          from (SELECT ic_atp.pk_corp as corp,
                       ic_atp.ccalbodyid as ccalbodyid,
                       ic_atp.cinventoryid as cinventoryid,
                       sum(nvl(nmonum, 0)) AS nmonum,
                       sum(nvl(nonrequirenum, 0)) AS nonrequirenum,
                       sum(nvl(nonponum, 0)) AS nonponum,
                       sum(nvl(nonwwnum, 0)) AS nonwwnum,
                       sum(nvl(nonreceivenum, 0)) AS nonreceivenum,
                       sum(nvl(nmponum, 0)) AS nmponum,
                       sum(nvl(nonsonum, 0)) AS nonsonum,
                       sum(nvl(nonreceiptnum, 0)) AS nonreceiptnum,
                       sum(nvl(npickmnum, 0)) AS npickmnum,
                       sum(nvl(nrsvnum1, 0)) AS ntraninnum,
                       sum(nvl(nrsvnum2, 0)) AS ntranoutnum,
                       sum(nvl(nontranspraynum, 0)) AS nontranspraynum,
                       sum(nvl(nonpreordernum, 0)) AS nonpreordernum,
                       0 nonhandnum,
                       0 nborrownum,
                       0 nfreezenum
                  from ic_atp ic_atp
                  left outer join bd_stordoc
                    on ic_atp.cwarehouseid = bd_stordoc.pk_stordoc
                   and COALESCE(bd_stordoc.isdirectstore, 'N') = 'N'
                 INNER JOIN bd_invmandoc
                    ON ic_atp.cinventoryid = bd_invmandoc.pk_invmandoc
                 INNER JOIN bd_invbasdoc
                    ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc
                 INNER JOIN bd_invcl
                    ON bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl
                 where ccalbodyid is not null
                   and (cwarehouseid is null or isatpaffected = 'Y')
                 group by ic_atp.pk_corp,
                          ic_atp.ccalbodyid,
                          ic_atp.cinventoryid) tmp
         INNER join bd_produce
            on tmp.cinventoryid = bd_produce.pk_invmandoc
           and tmp.ccalbodyid = bd_produce.pk_calbody
        
        union all
        
        SELECT ic_atp.pk_corp,
               ic_atp.ccalbodyid,
               ic_atp.cinventoryid,
               sum(nvl(nmonum, 0)) AS nmonum,
               sum(nvl(nonrequirenum, 0)) AS nonrequirenum,
               sum(nvl(nonponum, 0)) AS nonponum,
               sum(nvl(nonwwnum, 0)) AS nonwwnum,
               sum(nvl(nonreceivenum, 0)) AS nonreceivenum,
               sum(nvl(nmponum, 0)) AS nmponum,
               sum(nvl(nonsonum, 0)) AS nonsonum,
               sum(nvl(nonreceiptnum, 0)) AS nonreceiptnum,
               sum(nvl(npickmnum, 0)) AS npickmnum,
               sum(nvl(nrsvnum1, 0)) AS ntraninnum,
               sum(nvl(nrsvnum2, 0)) AS ntranoutnum,
               sum(nvl(nontranspraynum, 0)) AS nontranspraynum,
               sum(nvl(nonpreordernum, 0)) AS nonpreordernum,
               0 nonhandnum,
               0 nborrownum,
               0 nfreezenum
          from ic_atp_f ic_atp
          left outer join bd_stordoc
            on ic_atp.cwarehouseid = bd_stordoc.pk_stordoc
           and COALESCE(bd_stordoc.isdirectstore, 'N') = 'N'
         INNER JOIN bd_invmandoc
            ON ic_atp.cinventoryid = bd_invmandoc.pk_invmandoc
         INNER JOIN bd_invbasdoc
            ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc
         INNER JOIN bd_invcl
            ON bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl
         where ccalbodyid is not null
           and (cwarehouseid is null or isatpaffected = 'Y')
         group by ic_atp.pk_corp, ic_atp.ccalbodyid, ic_atp.cinventoryid
        union all
        select corp,
               ccalbodyid,
               cinventoryid,
               nmonum,
               nonrequirenum,
               nonponum,
               nonwwnum,
               nonreceivenum,
               nmponum,
               nonsonum,
               nonreceiptnum,
               npickmnum,
               ntraninnum,
               ntranoutnum,
               nontranspraynum,
               nonpreordernum,
               tmp.nonhandnum,
               tmp.nborrownum,
               nfreezenum
          from (SELECT ic_onhandnum.pk_corp as corp,
                       ccalbodyid,
                       cinventoryid,
                       SUM(nvl(nonhandnum, 0)) AS nonhandnum,
                       SUM(nvl(nnum1, 0)) AS nborrownum,
                       0 nmonum,
                       0 nonrequirenum,
                       0 nonponum,
                       0 nonwwnum,
                       0 nonreceivenum,
                       0 nmponum,
                       0 nonsonum,
                       0 nonreceiptnum,
                       0 npickmnum,
                       0 ntraninnum,
                       0 ntranoutnum,
                       0 nontranspraynum,
                       0 nonpreordernum,
                       0 nfreezenum
                  FROM ic_onhandnum ic_onhandnum
                 inner join bd_stordoc
                    on ic_onhandnum.cwarehouseid = bd_stordoc.pk_stordoc
                 inner JOIN bd_invmandoc
                    ON ic_onhandnum.cinventoryid = bd_invmandoc.pk_invmandoc
                 inner JOIN bd_invbasdoc
                    ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc
                 inner JOIN bd_invcl
                    ON bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl
                 where 0 = 0
                   and bd_stordoc.isatpaffected = 'Y'
                 group by ic_onhandnum.pk_corp, ccalbodyid, cinventoryid) tmp
         INNER join bd_produce
            on tmp.cinventoryid = bd_produce.pk_invmandoc
           and tmp.ccalbodyid = bd_produce.pk_calbody
        union all
        select corp,
               ccalbodyid,
               cinventoryid,
               nmonum,
               nonrequirenum,
               nonponum,
               nonwwnum,
               nonreceivenum,
               nmponum,
               nonsonum,
               nonreceiptnum,
               npickmnum,
               ntraninnum,
               ntranoutnum,
               nontranspraynum,
               nonpreordernum,
               nonhandnum,
               nborrownum,
               nfreezenum
          from (SELECT freeze.pk_corp as corp,
                       ccalbodyid,
                       cinventoryid,
                       SUM(nvl(nfreezenum, 0)) AS nfreezenum,
                       0 nmonum,
                       0 nonrequirenum,
                       0 nonponum,
                       0 nonwwnum,
                       0 nonreceivenum,
                       0 nmponum,
                       0 nonsonum,
                       0 nonreceiptnum,
                       0 npickmnum,
                       0 ntraninnum,
                       0 ntranoutnum,
                       0 nontranspraynum,
                       0 nonpreordernum,
                       0 nonhandnum,
                       0 nborrownum
                  FROM ic_freeze freeze
                 inner join bd_stordoc
                    on freeze.cwarehouseid = bd_stordoc.pk_stordoc
                 inner JOIN bd_invmandoc
                    ON freeze.cinventoryid = bd_invmandoc.pk_invmandoc
                 inner JOIN bd_invbasdoc
                    ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc
                 inner JOIN bd_invcl
                    ON bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl
                 where bd_stordoc.isatpaffected = 'Y'
                   and (cthawpersonid IS NULL AND
                       (ccorrespondbid IS NULL or
                       freeze.ccorrespondtype = 'WS'))
                 group by freeze.pk_corp, ccalbodyid, cinventoryid) tmp
         INNER join bd_produce
            on tmp.cinventoryid = bd_produce.pk_invmandoc
           and tmp.ccalbodyid = bd_produce.pk_calbody)

 group by corp, ccalbodyid, cinventoryid
