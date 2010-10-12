package nc.vo.scm.jjvo;

import java.util.HashMap;
import java.util.Map;

import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.pub.smart.SmartVOMeta;

/**
 * @function nc.vo.po.oper.InformationCostVO��Meta��
 * 
 * @author QuSida
 * 
 * @date 2010-8-10 ����02:05:46
 *
 */
public class InformationCostVOMeta extends SmartVOMeta{
//	  public static String[] extField;
//	  public static SmartFieldMeta[] extFieldMeta;
		public InformationCostVOMeta() {
			super();
			init();
		}
		private void init() {
			setTable("jj_scm_informationcost");
			setLabel("jj_scm_informationcost");
			setPkColName("pk_informantioncost");
			
			SmartFieldMeta sm = null;
			Map columns = new HashMap();
			
			//������Ϣ������
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("pk_informantioncost");
			sm.setColumn("pk_informantioncost");
			sm.setColumnDef(null);
			sm.setAllowNull(false);
			sm.setPrecision(0);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�Ƿ���¼��
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFBOOLEAN);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("ismny");
			sm.setColumn("ismny");
			sm.setColumnDef(null);
			sm.setAllowNull(false);
			sm.setPrecision(0);
			sm.setLength(1);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�Զ�����1-10
		    for(int i = 1;i<=10;i++){		    	
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.VARCHAR);
			sm.setName("vdef"+i);
			sm.setColumn("vdef"+i);
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);			
		    }
		    
		    //ɾ����־
		    sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_INTEGER);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("dr");
			sm.setColumn("dr");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(10);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			///ʱ���
		    sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDATETIME);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("ts");
			sm.setColumn("ts");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(19);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
		    //��������
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("cbillid");
			sm.setColumn("cbillid");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//ԭ����˰����
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("noriginalcurprice");
			sm.setColumn("noriginalcurprice");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//ԭ�Ҽ�˰�ϼ�
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("noriginalcursummny");
			sm.setColumn("noriginalcursummny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//��������
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.VARCHAR);
			sm.setName("vcosttype");
			sm.setColumn("vcosttype");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(50);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//���õ�λ		
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("ccostunitid");
			sm.setColumn("ccostunitid");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			
			//��ע
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.VARCHAR);
			sm.setName("vmemo");
			sm.setColumn("vmemo");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(200);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//���ñ���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.VARCHAR);
			sm.setName("costcode");
			sm.setColumn("costcode");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(40);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//��������
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.VARCHAR);
			sm.setName("costname");
			sm.setColumn("costname");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(200);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//����
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("currtypeid");
			sm.setColumn("currtypeid");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//������λ
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("cmeasdocid");
			sm.setColumn("cmeasdocid");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//����
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("nnumber");
			sm.setColumn("nnumber");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//���Һ�˰����
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ntaxprice");
			sm.setColumn("ntaxprice");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//ԭ�Һ�˰����
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("noriginalcurtaxprice");
			sm.setColumn("noriginalcurtaxprice");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//������˰����
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("nprice");
			sm.setColumn("nprice");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//������˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("nmny");
			sm.setColumn("nmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			
			//ԭ����˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("noriginalcurmny");
			sm.setColumn("noriginalcurmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//���Ҽ�˰�ϼ�
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("nsummny");
			sm.setColumn("nsummny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//˰Ŀ˰��
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.CHAR);
			sm.setName("pk_taxitems");
			sm.setColumn("pk_taxitems");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//��˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_STRING);
			sm.setDbType(java.sql.Types.VARCHAR);
			sm.setName("vtaxtype");
			sm.setColumn("vtaxtype");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(0);
			sm.setLength(50);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼƵ�����ԭ����˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninvoriginalcurmny");
			sm.setColumn("ninvoriginalcurmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼƵ�����ԭ�Һ�˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninvoriginalcurtaxmny");
			sm.setColumn("ninvoriginalcurtaxmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼƵ����ѱ��Һ�˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninvtaxmny");
			sm.setColumn("ninvtaxmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼƵ����ѱ�����˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninvmny");
			sm.setColumn("ninvmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼ������ñ��Һ�˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninstoretaxmny");
			sm.setColumn("ninstoretaxmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼ������ñ�����˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninstoremny");
			sm.setColumn("ninstoremny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼ�������ԭ�Һ�˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninstoreoriginalcurtaxmny");
			sm.setColumn("ninstoreoriginalcurtaxmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			//�ۼ�������ԭ����˰���
			sm = new SmartFieldMeta();
			sm.setType(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			sm.setDbType(java.sql.Types.NUMERIC);
			sm.setName("ninstoreoriginalcurmny");
			sm.setColumn("ninstoreoriginalcurmny");
			sm.setColumnDef(null);
			sm.setAllowNull(true);
			sm.setPrecision(8);
			sm.setLength(20);
			sm.setPersistence(true);
			columns.put(sm.getName(), sm);
			
			
			setColumns(columns);

		}
}
