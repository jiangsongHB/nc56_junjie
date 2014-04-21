package nc.impl.arap.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.NamingException;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.DataManageObject;
import nc.itf.arap.report.IExtInfo;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.ep.dj.DJFBVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.smart.SmartVO;

public class ExtInfoImpl extends DataManageObject implements IExtInfo {

	public ExtInfoImpl() throws NamingException {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExtInfoImpl(String dbName) throws NamingException {
		super(dbName);
		// TODO Auto-generated constructor stub
	}

	public HashMap qryBusiPsnByCust(String pk_org, String ccustbasdoc) throws DAOException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub

		nc.bs.dao.BaseDAO dao = new nc.bs.dao.BaseDAO();
		// 得到业务员和部门信息，以及最新单据日期
		StringBuffer sb = new StringBuffer(
				" select  t.ywybm , u.psnname , d.deptcode, d.deptname ,t.billdate" + 
				" from arap_djfb t left outer join  bd_psndoc u  on t.ywybm = u.pk_psndoc left outer join bd_deptdoc d on u.pk_deptdoc = d.pk_deptdoc " + 
				" where t.dr=0 and t.dwbm = ?  and t.hbbm = ?  order by t.billdate desc ");

		SQLParameter param = new SQLParameter();
		// 设置参数
		param.addParam(pk_org);
		param.addParam(ccustbasdoc);
		// 得到结果
		ArrayList reslist =  (ArrayList) dao.executeQuery(sb.toString(), param,
				new ArrayListProcessor());
		HashMap<String, Object> h = new HashMap();
		if (reslist != null && reslist.size() != 0) {
			Object[] objs= (Object[]) reslist.get(0);
			
			h.put("ywybm", objs[0]==null?null:objs[0].toString());
			h.put("ywymc", objs[1]==null?null:objs[1].toString());
			h.put("bmbm", objs[2]==null?null:objs[2].toString());
			h.put("bmmc", objs[3]==null?null:objs[3].toString());
			h.put("djrq", objs[4]==null?null:objs[4].toString());
		}
		// 结果设置到vo中
		return h;
	}

	public UFDate qryBusiDatebyCust(String pk_org, String ccustbasdoc) throws  DAOException {
		// TODO Auto-generated method stub
		UFDate billdate = null;
		nc.bs.dao.BaseDAO dao = new nc.bs.dao.BaseDAO();
		// 得到最早欠款日期
		StringBuffer sb = new StringBuffer(
				" select min(t.billdate)  from arap_djfb t  where t.dwbm = ?  and t.hbbm = ?  and  t.bbye > 0 and t.djdl = 'ys' and t.dr=0 ");

		SQLParameter param = new SQLParameter();
		// 设置参数
		param.addParam(pk_org);
		param.addParam(ccustbasdoc);
		// 得到结果
		ResultSetProcessor processor = new ColumnProcessor();
		Object obj =  dao.executeQuery(sb.toString(), param,
				processor);
		billdate = obj==null? null: new UFDate((String)obj);
		// 结果设置到vo中
		
		return billdate;
	}

}
