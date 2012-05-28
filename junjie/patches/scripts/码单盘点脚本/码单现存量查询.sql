create or replace view v__md_onhandnum
as
SELECT h.pk_corp,
       h.ccalbodyidb ccalbodyid,
       h.CWAREHOUSEIDB cwarehouseid,
       b.cspaceid,
       h.cinventoryidb cinventoryid,
       b.jbh as  vdef1,
       b.def7 as vdef2,
       b.def8 as vdef3,
       b.def9 as vdef4,
       b.pk_mdxcl_b as vdef5,
       b.remark as vfree1,
       COALESCE(b.def1, 0.0) as ninspacenum,
       COALESCE(b.zhishu,  0.0) AS ninspaceassistnum,
       CAST(NULL AS numeric(20, 8)) AS noutspacenum,
       CAST(NULL AS numeric(20, 8)) AS noutspaceassistnum,
       COALESCE(b.def1, 0.0) as ningrossnum,
       CAST(NULL AS numeric(20, 8)) AS noutgrossnum,
       h.cinvbasid,
       s.pk_measdoc as castunitid,
       s.mainmeasrate as hsl 
  FROM nc_mdxcl h
  LEFT OUTER JOIN nc_mdxcl_b b
    ON h.pk_mdxcl = b.pk_mdxcl
    LEFT OUTER JOIN bd_convert s
    ON h.cinvbasid = s.pk_invbasdoc
   WHERE  nvl(h.dr,0)=0
   and nvl(b.dr,0)=0
   and  COALESCE(b.zhishu,  0.0)>0
