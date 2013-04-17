select sh.dbilldate,sh.vreceiptcode,e.custname,zclsumnsummny,yclsumnsummny
,bb.sumnsummny,nvl(zclsumnsummny,0)+nvl(yclsumnsummny,0)+nvl(bb.sumnsummny,0) hj,
 invoice.nsummny, invoice.vreceiptcode  invoicecode,
zclnoutnum clnoutnum,psnname ,sh.vdef9 
 from (
select * from 
(select sum(noutnum*ntaxprice ) zclsumnsummny,0 as yclsumnsummny,sum(noutnum) zclnoutnum,b.cfirstbillhid from ic_general_h h 
left join ic_general_b b on h.cgeneralhid=b.cgeneralhid
where h.dbilldate between #sdate# and #edate# and h.dr=0 and b.dr=0 and h.cbilltypecode='4C' and h.pk_corp=#pk_corp# and nvl(h.vuserdef10,'¼×µ¥')='¼×µ¥'
group by b.cfirstbillhid
union
select 0 as zclsumnsummny,sum(noutnum*ntaxprice ) yclsumnsummny,sum(noutnum) zclnoutnum,b.cfirstbillhid from ic_general_h h 
left join ic_general_b b on h.cgeneralhid=b.cgeneralhid
where h.dbilldate between #sdate# and #edate# and h.dr=0 and b.dr=0 and h.cbilltypecode='4C' and h.pk_corp=#pk_corp# and h.vuserdef10 ='ÒÒµ¥'
group by b.cfirstbillhid
 ) aa ) a
 left join so_sale sh on sh.csaleid=a.cfirstbillhid
left join bd_cumandoc d on sh.ccustomerid=d.pk_cumandoc
left join bd_cubasdoc e on e.pk_cubasdoc=d.pk_cubasdoc
left join (select sum(nsummny) sumnsummny,sh.csaleid   from so_sale sh 
left join so_saleorder_b sb on sb.csaleid=sh.csaleid
left join  bd_invbasdoc c on sb.cinvbasdocid =c.pk_invbasdoc
where sh.dbilldate  between #sdate# and #edate# and dr=0 and laborflag ='Y' and sh.pk_corp=#pk_corp#  and sb.blargessflag='N' group by sh.csaleid)
 bb on bb.csaleid= sh.csaleid
left join bd_psndoc f on sh.cemployeeid =pk_psndoc

  left join  
 ( select
  sum(so_saleinvoice_b.nsummny) nsummny,
  so_saleinvoice.vreceiptcode,
  so_saleinvoice_b.csourcebillid
    from so_saleinvoice
    left join so_saleinvoice_b on so_saleinvoice.csaleid =
                                  so_saleinvoice_b.csaleid
     where so_saleinvoice.dbilldate between #sdate# and #edate#
                   and so_saleinvoice.dr = 0
                   and so_saleinvoice_b.dr = 0
                   and so_saleinvoice.pk_corp = #pk_corp# 
    group by so_saleinvoice.vreceiptcode, so_saleinvoice_b.csourcebillid ) invoice
    on  sh.csaleid = invoice.csourcebillid