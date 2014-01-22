create or replace view v_ja_check
(checkid, checkamount, checktype, invcode, pk_invdoc, invclassname, pk_invcl, vname, vsize, quality, addr, measurename, pk_measure, count, price, amount, taxrate, tax, taxamount, vnote, def1, def2, def3, def4, def5, def6, def7, def8, def9, def10)
as
select sb.coriginalbillcode,'','���۷�Ʊ','',sb.cinvbasdocid,'',sb.cinventoryid,'','','','','','',sb.nnumber,sb.noriginalcurprice,sb.noriginalcurmny,'','',sb.nmny,'',si.creceiptcorpid,sb.blargessflag,'','','','','','','',sb.cinvoice_bid
from so_saleinvoice si
--z����si.csaleid,���ܽ��si.ntotalsummny ��������si.pk_defdoc10, ��Ʊ��λsi.creceiptcorpid,
inner join so_saleinvoice_b sb on si.csaleid=sb.csaleid where si.ts>'2013-01-01' and si.dr=0 and sb.blargessflag not in 'Y' and sb.dr=0
union all
select pb.vdef18,'','�ɹ���Ʊ','',pb.cbaseid,'',pb.cmangid,'','','','','','',pb.nassistnum,pb.noriginalcurprice,pb.noriginalcurmny,'','',pb.nmoney,'',pi.cvendormangid,'',pi.cvendormangid,'','','','','','',pb.cinvoice_bid
from po_invoice pi
--
inner join po_invoice_b pb on pi.cinvoiceid=pb.cinvoiceid
where pi.ts>'2013-01-01' and pi.dr=0 and pb.dr=0
;
