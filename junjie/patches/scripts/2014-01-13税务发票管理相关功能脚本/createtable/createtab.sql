-- 发票类型定义表

  CREATE TABLE BD_TAXINVOICETYPE
   (	APPROVEDTIME CHAR(19 BYTE), 
	APPROVER CHAR(20 BYTE), 
	CODE VARCHAR2(50 BYTE), 
	CREATIONTIME CHAR(19 BYTE), 
	CREATOR CHAR(20 BYTE), 
	DR NUMBER(10,0) DEFAULT 0, 
	IFFEE CHAR(1 BYTE), 
	ISDEFAULT CHAR(1 BYTE), 
	ISSEAL CHAR(1 BYTE), 
	MODIFIEDTIME CHAR(19 BYTE), 
	MODIFIER CHAR(20 BYTE), 
	NAME VARCHAR2(200 BYTE), 
	PK_CORP CHAR(20 BYTE), 
	PK_TAXINVOICETYPE CHAR(20 BYTE) NOT NULL ENABLE, 
	TS CHAR(19 BYTE) DEFAULT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	IAPPROVETYPE NUMBER(38,0), 
	 CONSTRAINT PK_BD_TAXINVOICETYPE PRIMARY KEY (PK_TAXINVOICETYPE)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01 ;

  --税务发票主表
  
  CREATE TABLE SO_TAXINVOICE 
   (	CAPPROVER CHAR(20 BYTE), 
	CBILLTYPE CHAR(20 BYTE), 
	CBUSITYPE CHAR(20 BYTE), 
	CCREATOR CHAR(20 BYTE), 
	CCURRENCYID CHAR(20 BYTE), 
	CDEPTID CHAR(20 BYTE), 
	CINVOICEMAMID CHAR(20 BYTE), 
	CMODIFIER CHAR(20 BYTE), 
	CORDERMANID CHAR(20 BYTE), 
	CPERSONID CHAR(20 BYTE), 
	CSALETYPE VARCHAR2(100 BYTE), 
	CSERVICEMANID CHAR(20 BYTE), 
	CTAXINVOICEID CHAR(20 BYTE) NOT NULL ENABLE, 
	DAPPROVEDATE CHAR(19 BYTE), 
	DCREATEDATE CHAR(19 BYTE), 
	DINVOICEDATE CHAR(10 BYTE), 
	DMODIFYDATE CHAR(19 BYTE), 
	DR NUMBER(10,0) DEFAULT 0, 
	DRECEIVEDATE CHAR(10 BYTE), 
	IBILLSTATUS NUMBER(38,0), 
	IINVOICETYPE CHAR(20 BYTE), 
	ISPRAY CHAR(1 BYTE), 
	NROE NUMBER(28,8), 
	PK_CORP CHAR(20 BYTE), 
	TS CHAR(19 BYTE) DEFAULT TO_CHAR(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	VAPPROVNOTE VARCHAR2(200 BYTE), 
	VDEF1 VARCHAR2(100 BYTE), 
	VDEF10 VARCHAR2(100 BYTE), 
	VDEF2 VARCHAR2(100 BYTE), 
	VDEF3 VARCHAR2(100 BYTE), 
	VDEF4 VARCHAR2(100 BYTE), 
	VDEF5 VARCHAR2(100 BYTE), 
	VDEF6 VARCHAR2(100 BYTE), 
	VDEF7 VARCHAR2(100 BYTE), 
	VDEF8 VARCHAR2(100 BYTE), 
	VDEF9 VARCHAR2(100 BYTE), 
	VINVOICENO VARCHAR2(40 BYTE), 
	VMEMO VARCHAR2(200 BYTE), 
	CINVOICETYPE CHAR(20 BYTE), 
	VBILLNO VARCHAR2(40 BYTE), 
	 CONSTRAINT PK_SO_TAXINVOICE PRIMARY KEY (CTAXINVOICEID)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01 ;

  --税务发票附表
  
  
  CREATE TABLE SO_TAXINVOICE_B 
   (	CINVCLASSID CHAR(20 BYTE), 
	CINVENTORYID CHAR(20 BYTE), 
	CINVNAME VARCHAR2(200 BYTE), 
	CROWNO VARCHAR2(50 BYTE), 
	CTAXINVOICE_BID CHAR(20 BYTE) NOT NULL ENABLE, 
	CTAXINVOICEID CHAR(20 BYTE), 
	DR NUMBER(10,0) DEFAULT 0, 
	IROWCLOSE CHAR(1 BYTE), 
	NCURMNY NUMBER(28,8), 
	NCURPRICE NUMBER(28,8), 
	NCURSUMMNY NUMBER(28,8), 
	NCURTAXMNY NUMBER(28,8), 
	NMNY NUMBER(28,8), 
	NNUMBER NUMBER(28,8), 
	NPRICE NUMBER(28,8), 
	NSUMMNY NUMBER(28,8), 
	NTAXMAN NUMBER(28,8), 
	NTAXRATE NUMBER(28,8), 
	NTOTALDEALMNY NUMBER(28,8), 
	NTOTALDEALNUM NUMBER(28,8), 
	TS CHAR(19 BYTE) DEFAULT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	VBDEF1 VARCHAR2(100 BYTE), 
	VBDEF10 VARCHAR2(50 BYTE), 
	VBDEF2 VARCHAR2(50 BYTE), 
	VBDEF3 VARCHAR2(50 BYTE), 
	VBDEF4 VARCHAR2(50 BYTE), 
	VBDEF5 VARCHAR2(50 BYTE), 
	VBDEF6 VARCHAR2(50 BYTE), 
	VBDEF7 VARCHAR2(50 BYTE), 
	VBDEF8 VARCHAR2(50 BYTE), 
	VBDEF9 VARCHAR2(50 BYTE), 
	VROWMEMO VARCHAR2(200 BYTE), 
	NCURTAXPRICE NUMBER(28,8), 
	NTAXPRICE NUMBER(28,8), 
	 CONSTRAINT PK_SO_TAXINVOICE_B PRIMARY KEY (CTAXINVOICE_BID)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01  ENABLE, 
	 CONSTRAINT FK_XINVOICE_B_XINVOICEID567704 FOREIGN KEY (CTAXINVOICEID)
	  REFERENCES SO_TAXINVOICE (CTAXINVOICEID) ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01 ;

  
  --税务发票核销明细
  
  CREATE TABLE SO_TAXINVOICE_BB1 
   (	CDEALOPER CHAR(20 BYTE), 
	CSOURCEBILLID CHAR(20 BYTE), 
	CSOURCEBILLROWID CHAR(20 BYTE), 
	CSOURCEBILLTYPE CHAR(20 BYTE), 
	CTAXINVOICE_BBID CHAR(20 BYTE) NOT NULL ENABLE, 
	CTAXINVOICE_BID CHAR(20 BYTE), 
	DDEALDATE CHAR(10 BYTE), 
	DR NUMBER(10,0) DEFAULT 0, 
	NDEALMNY NUMBER(28,8), 
	NDEALNUM NUMBER(28,8), 
	NWRITEINVOICEMNY NUMBER(28,8), 
	NWRITEINVOICENUM NUMBER(28,8), 
	TS CHAR(19 BYTE) DEFAULT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	CTAXINVOICEID CHAR(20 BYTE), 
	 CONSTRAINT PK_SO_TAXINVOICE_BB1 PRIMARY KEY (CTAXINVOICE_BBID)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01  ENABLE, 
	 CONSTRAINT FK_NVOICE_BB1_BID221644 FOREIGN KEY (CTAXINVOICE_BID)
	  REFERENCES SO_TAXINVOICE_B (CTAXINVOICE_BID) ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE NNC_DATA01 ;
  
  
  --税务发票类型参照定义

Insert into "bd_refinfo" (CODE,DR,ISNEEDPARA,ISSPECIALREF,METADATATYPENAME,MODULE,NAME,PARA1,PARA2,PARA3,PK_REFINFO,REFCLASS,REFSYSTEM,REFTYPE,RESERV1,RESERV2,RESERV3,RESID,RESIDPATH,TS) values ('XX',0,null,null,'TaxInvoiceType','scmpub','税务发票类型',null,null,null,'0001ZZ10000000019DG9','nc.ui.scmpub.TaxInvoiceTypeRefModel',null,0,null,null,null,'税务发票类型','scmpub','2014-01-25 10:13:19');



