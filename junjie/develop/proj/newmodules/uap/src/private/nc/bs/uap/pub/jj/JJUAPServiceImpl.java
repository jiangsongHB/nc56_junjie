package nc.bs.uap.pub.jj;


import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.vo.ic.md.MdcrkVO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;


public class JJUAPServiceImpl implements nc.itf.uap.pub.jj.IJJUAPService {

	/**
	 * @function 根据存货基本档案查出附加值
	 *
	 * @author MeiChao
	 *
	 * @param List<MdcrkVO>
	 * @return List<MdcrkVO>
	 * @throws Exception 
	 *
	 * @date 2010-11-05 下午04:12:17
	 */
	public List<MdcrkVO> queryAdditionalvalue(List<MdcrkVO> mdvo) throws Exception {
		BaseDAO dao = new BaseDAO();
		for (int i = 0; i < mdvo.size(); i++) {
			// 首先根据码单中对应存货基本档案id去查询是否有对应的附加值系数
			String sql = "select nadditionalvalue from jj_bd_additionalvalue where pk_invbasdoc ='"
					+ mdvo.get(i).getDef10() + "'";
			Object o = null;
			try {
				o = dao.executeQuery(sql, new ColumnProcessor());
				if (o != null) {// 如果根据存货基本档案查询出了附加值系数,那么直接存入def11字段中,并继续循环
					mdvo.get(i).setDef11(o.toString());
					continue;
				} else if (o == null) {//如果根据存货基本档案无法查询出结果,那么根据存货分类及宽度去查询
//					if(mdvo.get(i).getMd_width()==null){
					if(mdvo.get(i).getDef8()==null){   //wanglei 2011-07-26 调整为验收宽度计算
						//throw new Exception("宽度为空!");
						mdvo.get(i).setDef12("宽度为空!");
						continue;//如果宽度为空,直接跳过.
					}
//					String width=mdvo.get(i).getMd_width().toString();//存货宽度
					String width=mdvo.get(i).getDef8().toString();//存货宽度   //wanglei 2011-07-26 调整为验收宽度计算
					String invbasid=mdvo.get(i).getDef10().toString();//存货基本档案pk
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
					//查詢出當前存貨基本檔案中對應分類的PK,編碼,層級.以逗號相隔.
					String invbasresult=dao.executeQuery(invclpksql, new ColumnProcessor()).toString();
					String[] invbaseresultArray=invbasresult.split(",");
					String invspec=invbaseresultArray[0].toString();//存货规格,既厚度
					String invclpk=invbaseresultArray[1].toString();//末级存货分类pk
					String invclcode=invbaseresultArray[2].toString();//末级存货分类编码
					Integer invcllev=Integer.valueOf(invbaseresultArray[3].toString());//末级存货分类层级
					//String[] invclAllCode=new String[(invclcode.length()+1)/2];//存货分类所有所属层级的编码
					for(int j=0;j<invcllev;j++){//使用末级存货分类层级号作为循环长度控制符
						if(j!=0){//如果不是第一次循环,那么需要查询出上级的存货分类PK作为查询条件
							//每次循环,都将末级存货分类编码的后2*j个字符去掉,即为上一级的分类编码
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
							continue;//如果查询结果为空,那么查询下一级.
						}else{
							mdvo.get(i).setDef11(result.toString());
							break;//如果已获取到附加值,那么跳出内循环,结束当前存货的附加值查询,进行下个查询.
						}
					}
				}
			} catch (Exception e) {
				mdvo.get(i).setDef12("查詢第"+i+"条记录的时候出错!");
				continue;
			}
		}
	     return mdvo;
	}

	/**
	 * @function 根据存货基本档案查出理算系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 下午04:13:07
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
	 * @function 根据存货基本档案查出毛边系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 下午04:13:46
	 */
	public Object queryBurrcoefficient(String pk_invbasdoc) throws Exception {
		BaseDAO dao = new BaseDAO();
		
		//begin 2010-10-12 MeiChao 修改内容: 将此查询毛边系数sql的表名jj_bd_additionalvalue修改为正确的表名jj_bd_burrcoefficient
//		String sql = "select nflash from jj_bd_additionalvalue where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		String sql = "select nflash from jj_bd_burrcoefficient where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		//end 2010-10-12 MeiChao 修改内容: 将此查询毛边系数sql的表名jj_bd_additionalvalue修改为正确的表名jj_bd_burrcoefficient
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function 传入SQL查出你想要的
	 *
	 * @author QuSida
	 *
	 * @param sql
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 下午04:14:19
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
