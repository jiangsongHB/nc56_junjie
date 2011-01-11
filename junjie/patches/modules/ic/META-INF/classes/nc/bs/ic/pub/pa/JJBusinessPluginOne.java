package nc.bs.ic.pub.pa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

public class JJBusinessPluginOne implements IBusinessPlugin {

	public int getImplmentsType() {
		// TODO Auto-generated method stub
		return IBusinessPlugin.IMPLEMENT_RETURNMESSAGE;
	}

	public Key[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public String implementReturnMessage(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
//		String checkSQL="select t.vbillcode,t.cgeneralhid " +
//				"from ic_general_h t where" +
//				" t.dr=0 and t.vuserdef20='Y' and t.fbillflag=3 " +
//				"and t.cgeneralhid in " +
//				"(select distinct t.cgeneralhid from ic_general_b t where t.dr=0 and nvl(t.ncorrespondnum,0) <t.ninnum)";
//			String checkSQL="select t.vbillcode,ttt.graphid,tt.ninnum " +
//					"from ic_general_h t left join ic_general_b tt " +
//					"on t.cgeneralhid=tt.cgeneralhid and tt.dr=0 and nvl(tt.ncorrespondnum,0)<tt.ninnum " +
//					"left join bd_invbasdoc ttt " +
//					"on tt.cinvbasid=ttt.pk_invbasdoc and tt.dr=0 and ttt.dr=0 where t.dr=0 and t.vuserdef20='Y' and t.fbillflag=3 order by t.vbillcode";
			String checkSQL="select * from testview1";
			BaseDAO query=new BaseDAO();
			//System.out.println("查询语句"+checkSQL);
			//Object result1= query.executeQuery(checkSQL, new MapListProcessor());
			List result= (List) query.executeQuery(checkSQL, new ArrayListProcessor());
			//System.out.println("预警长度"+result.size());
			StringBuffer HintMessage=new StringBuffer();
			HintMessage.append("<div> <h1>");
			HintMessage.append("有直调入库商品未销售: </h1> <div><h2>入库单: </h2></div> </div>");
			HintMessage.append("<div><table border=1><tr><th>入库单号</th><th>品名</th><th>直调数量</th></tr>");
			String vbillcode="";
			Map hintMap=new HashMap();
			List oneBillData=null;
			for(int i=0;i<result.size();i++){
				if(!vbillcode.equals(((Object[])result.get(i))[0].toString())){
					if(i!=0){
						hintMap.put(vbillcode, oneBillData);
					}
					oneBillData=new ArrayList();
					vbillcode=((Object[])result.get(i))[0].toString();
				}
				oneBillData.add(result.get(i));
				if(i==result.size()-1){
					hintMap.put(vbillcode, oneBillData);
				}
			}
			Set vbillcodes=hintMap.keySet();
			for(Object billcode : vbillcodes){
				List currBill=(List)hintMap.get(billcode);
				HintMessage.append("<tr><td rowspan=");
				HintMessage.append(currBill.size());
				HintMessage.append(">");
				HintMessage.append(billcode.toString());
				HintMessage.append("</td>");
				for(int i=0;i<currBill.size();i++){
					HintMessage.append("<td>");
					HintMessage.append(((Object[])currBill.get(i))[1].toString());
					HintMessage.append("</td>");
					HintMessage.append("<td>");
					HintMessage.append(((Object[])currBill.get(i))[2].toString());
					HintMessage.append("</td>");
					HintMessage.append("</tr><tr>");
				}
			}
			HintMessage.append("</table></div>");
			return HintMessage.toString();
	}

	public Object implementReturnObject(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean implementWriteFile(Key[] keys, String fileName,
			String corpPK, UFDate clientLoginDate) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

}
