create or replace view v_ja_check
(checkid, checkamount, checktype, invcode, pk_invdoc, invclassname, pk_invcl, vname, vsize, quality, addr, measurename, pk_measure, count, price, amount, taxrate, tax, taxamount, vnote, def1, def2, def3, def4, def5, def6, def7, def8, def9, def10)
as
select si.vreceiptcode,'','销售发票',
         '',sb.cinvbasdocid,(select decode('Y',inv.laborflag,'费用','钢材')  from bd_invbasdoc inv where inv.pk_invbasdoc=sb.cinvbasdocid) ,sb.cinventoryid,'','','','','','',sb.nnumber,sb.noriginalcurprice,sb.noriginalcurmny,'','',sb.nmny,'',
         si.creceiptcorpid,'','','','',si.vdef10,si.dbilldate,'','',sb.cinvoice_bid
  from so_saleinvoice si
  inner join so_saleinvoice_b sb on si.csaleid=sb.csaleid
  where si.dbilldate>='2014-01-01' and si.dr=0 and sb.blargessflag not in 'Y' and sb.dr=0
union all
  select pi.vinvoicecode,'','采购发票',
         '',pb.cbaseid,(select decode('Y',inv.laborflag,'费用','钢材')  from bd_invbasdoc inv where inv.pk_invbasdoc=pb.cbaseid),pb.cmangid,'','','','','','',pb.nassistnum,pb.noriginalcurprice,pb.noriginalcurmny,'','',pb.nmoney,'',
         pi.cvendormangid,'','','','','',pi.dinvoicedate,'','',pb.cinvoice_bid
  from po_invoice pi
  inner join po_invoice_b pb on pi.cinvoiceid=pb.cinvoiceid
  where pi.dinvoicedate>='2014-01-01' and pi.dr=0 and pb.dr=0;