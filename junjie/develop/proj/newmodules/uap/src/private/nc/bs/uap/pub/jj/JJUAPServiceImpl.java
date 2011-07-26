package nc.bs.uap.pub.jj;


import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.vo.ic.md.MdcrkVO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;


public class JJUAPServiceImpl implements nc.itf.uap.pub.jj.IJJUAPService {

	/**
	 * @function ���ݴ�����������������ֵ
	 *
	 * @author MeiChao
	 *
	 * @param List<MdcrkVO>
	 * @return List<MdcrkVO>
	 * @throws Exception 
	 *
	 * @date 2010-11-05 ����04:12:17
	 */
	public List<MdcrkVO> queryAdditionalvalue(List<MdcrkVO> mdvo) throws Exception {
		BaseDAO dao = new BaseDAO();
		for (int i = 0; i < mdvo.size(); i++) {
			// ���ȸ����뵥�ж�Ӧ�����������idȥ��ѯ�Ƿ��ж�Ӧ�ĸ���ֵϵ��
			String sql = "select nadditionalvalue from jj_bd_additionalvalue where pk_invbasdoc ='"
					+ mdvo.get(i).getDef10() + "'";
			Object o = null;
			try {
				o = dao.executeQuery(sql, new ColumnProcessor());
				if (o != null) {// ������ݴ������������ѯ���˸���ֵϵ��,��ôֱ�Ӵ���def11�ֶ���,������ѭ��
					mdvo.get(i).setDef11(o.toString());
					continue;
				} else if (o == null) {//������ݴ�����������޷���ѯ�����,��ô���ݴ�����༰���ȥ��ѯ
//					if(mdvo.get(i).getMd_width()==null){
					if(mdvo.get(i).getDef8()==null){   //wanglei 2011-07-26 ����Ϊ���տ�ȼ���
						//throw new Exception("���Ϊ��!");
						mdvo.get(i).setDef12("���Ϊ��!");
						continue;//������Ϊ��,ֱ������.
					}
//					String width=mdvo.get(i).getMd_width().toString();//������
					String width=mdvo.get(i).getDef8().toString();//������   //wanglei 2011-07-26 ����Ϊ���տ�ȼ���
					String invbasid=mdvo.get(i).getDef10().toString();//�����������pk
					String invclpksql = "select t.invspec||" +
					        "','||" +
							"t.pk_invcl||" +
							"','||" +
							"i.invclasscode||" +
							"','||" +
							"i.invclasslev " +
							"from bd_invbasdoc t " +
							"left join " +
							"bd_invcl i " +
							"on t.pk_invcl=i.pk_invcl " +
							"where t.pk_invbasdoc='"+invbasid+"'";
					//��ԃ����ǰ��؛�����n���Ќ������PK,���a,�Ӽ�.�Զ�̖���.
					String invbasresult=dao.executeQuery(invclpksql, new ColumnProcessor()).toString();
					String[] invbaseresultArray=invbasresult.split(",");
					String invspec=invbaseresultArray[0].toString();//������,�Ⱥ��
					String invclpk=invbaseresultArray[1].toString();//ĩ���������pk
					String invclcode=invbaseresultArray[2].toString();//ĩ������������
					Integer invcllev=Integer.valueOf(invbaseresultArray[3].toString());//ĩ���������㼶
					//String[] invclAllCode=new String[(invclcode.length()+1)/2];//����������������㼶�ı���
					for(int j=0;j<invcllev;j++){//ʹ��ĩ���������㼶����Ϊѭ�����ȿ��Ʒ�
						if(j!=0){//������ǵ�һ��ѭ��,��ô��Ҫ��ѯ���ϼ��Ĵ������PK��Ϊ��ѯ����
							//ÿ��ѭ��,����ĩ������������ĺ�2*j���ַ�ȥ��,��Ϊ��һ���ķ������
							invclpk=dao.executeQuery("select t.pk_invcl from bd_invcl t where t.invclasscode='"+invclcode.substring(0, invclcode.length()-j*2)+"'", new ColumnProcessor()).toString();
						}
						String additionalValueSQL="select t.nadditionalvalue  " +
								"from jj_bd_additionalvalue t " +
								"where t.pk_invcl=('" +
								invclpk+
								"') and t.specdmin<"+
								invspec+
								" and t.specdmax>="+
								invspec+
								" and t.widthwmin<"+
								width+
								" and t.widthwmax>="+
								width+"";
						Object result=dao.executeQuery(additionalValueSQL, new ColumnProcessor());
						if(result==null||"".equals(result.toString())){
							continue;//�����ѯ���Ϊ��,��ô��ѯ��һ��.
						}else{
							mdvo.get(i).setDef11(result.toString());
							break;//����ѻ�ȡ������ֵ,��ô������ѭ��,������ǰ����ĸ���ֵ��ѯ,�����¸���ѯ.
						}
					}
				}
			} catch (Exception e) {
				mdvo.get(i).setDef12("��ԃ��"+i+"����¼��ʱ�����!");
				continue;
			}
		}
	     return mdvo;
	}

	/**
	 * @function ���ݴ�����������������ϵ��
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 ����04:13:07
	 */
	public Object queryAdjustmentcoefficient(String pk_invbasdoc)
			throws Exception {
		BaseDAO dao = new BaseDAO();
		String sql = "select nadjustmentcoefficient from jj_bd_adjustmentcoefficient where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function ���ݴ�������������ë��ϵ��
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 ����04:13:46
	 */
	public Object queryBurrcoefficient(String pk_invbasdoc) throws Exception {
		BaseDAO dao = new BaseDAO();
		
		//begin 2010-10-12 MeiChao �޸�����: ���˲�ѯë��ϵ��sql�ı���jj_bd_additionalvalue�޸�Ϊ��ȷ�ı���jj_bd_burrcoefficient
//		String sql = "select nflash from jj_bd_additionalvalue where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		String sql = "select nflash from jj_bd_burrcoefficient where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		//end 2010-10-12 MeiChao �޸�����: ���˲�ѯë��ϵ��sql�ı���jj_bd_additionalvalue�޸�Ϊ��ȷ�ı���jj_bd_burrcoefficient
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function ����SQL�������Ҫ��
	 *
	 * @author QuSida
	 *
	 * @param sql
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 ����04:14:19
	 */
	public Object queryService(String sql) throws Exception {
		BaseDAO dao = new BaseDAO();
		Object o = null ;
		o = dao.executeQuery(sql, new ArrayListProcessor());
	     return o;	
	}
	
	public static void main(String args[]){
		JJUAPServiceImpl newone=new JJUAPServiceImpl();
		List<MdcrkVO> mdvo =new ArrayList<MdcrkVO>();
		MdcrkVO newmd=new MdcrkVO();
		newmd.setDef10("0001A910000000000UR3");
		mdvo.add(newmd);
		try {
			newone.queryAdditionalvalue(mdvo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
