create or replace view v_ja_check
(checkid, checkamount, checktype, invcode, pk_invdoc, invclassname, pk_invcl, vname, vsize, quality, addr, measurename, pk_measure, count, price, amount, taxrate, tax, taxamount, vnote, def1, def2, def3, def4, def5, def6, def7, def8, def9, def10)
as
select sb.coriginalbillcode,'','销售发票','',sb.cinvbasdocid,'',sb.cinventoryid,'','','','','','',sb.nnumber,sb.noriginalcurprice,sb.noriginalcurmny,'','',sb.nmny,'',si.creceiptcorpid,sb.blargessflag,'','','','','','','',sb.cinvoice_bid
from so_saleinvoice si
--z主键si.csaleid,，总金额si.ntotalsummny 销售类型si.pk_defdoc10, 开票单位si.creceiptcorpid,
inner join so_saleinvoice_b sb on si.csaleid=sb.csaleid where si.ts>'2013-01-01' and si.dr=0 and sb.blargessflag not in 'Y' and sb.dr=0
union all
select pb.vdef18,'','采购发票','',pb.cbaseid,'',pb.cmangid,'','','','','','',pb.nassistnum,pb.noriginalcurprice,pb.noriginalcurmny,'','',pb.nmoney,'',pi.cvendormangid,'',pi.cvendormangid,'','','','','','',pb.cinvoice_bid
from po_invoice pi
--
inner join po_invoice_b pb on pi.cinvoiceid=pb.cinvoiceid
where pi.ts>'2013-01-01' and pi.dr=0 and pb.dr=0
;
