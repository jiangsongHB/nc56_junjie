package nc.bs.so.backtask.soremain;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.impl.scm.so.so001.JunJieSoDMO;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.impl.scm.sp.sp015.PreOrderAlartFormatMsg;
import nc.itf.scm.so.so001.ISaleOrderQuery;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ic.sd.MdsdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pa.Key;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * @author OUYANGZHB 2013-3-15
 * 
 */
public class SaleStayOrderClose implements IBusinessPlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.bs.pub.pa.IBusinessPlugin#getImplmentsType()
	 */
	public int getImplmentsType() {
		// TODO Auto-generated method stub
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.bs.pub.pa.IBusinessPlugin#getKeys()
	 */
	public Key[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.bs.pub.pa.IBusinessPlugin#getTypeDescription()
	 */
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.bs.pub.pa.IBusinessPlugin#getTypeName()
	 */
	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.bs.pub.pa.IBusinessPlugin#implementReturnFormatMsg(nc.vo.pub.pa.Key[],
	 * java.lang.String, nc.vo.pub.lang.UFDate)
	 */
	public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		UFDate serverDate = new UFDate(System.currentTimeMillis());
		// 输出预警信息
		PreOrderAlartFormatMsg msg = new PreOrderAlartFormatMsg();
		
		BaseDAO baseDao = new BaseDAO();
		ArrayList<SaleorderBVO> bVOList = new ArrayList<SaleorderBVO>();
		SqlBuilder sql = new SqlBuilder();
//	 
		sql.append("  so_saleorder_b.nnumber>nvl(so_saleexecute.ntotalreceivenumber,0) ");
		sql.append(" and nvl(so_saleexecute.bifinventoryfinish,'N')='N'");
//		sql.append(" and NVL(so_saleexecute.bifinvoicefinish,'N') ='N'");
//		sql.append(" and NVL(so_saleexecute.bifpayfinish,'N')='N'");
//		sql.append(" and NVL(so_saleexecute.bifreceivefinish,'N')='N'");
		sql.append("and so_sale.vdef13 is not null and so_sale.vdef12 like '%留货%' ");
		sql.append(" and (to_date('"+serverDate.toString()+"', 'yyyy-mm-dd') - to_date(so_sale.dbilldate, 'yyyy-mm-dd')) >= so_sale.vdef13 ");
		
		SaleOrderDMO dmo = null;
		ISaleOrderQuery bo = (ISaleOrderQuery) NCLocator.getInstance().lookup(ISaleOrderQuery.class);
		SaleorderBVO[] bVOs =bo.queryOrderEnd(sql.toString());
		if(bVOs==null||bVOs.length<=0)
			return null ;
		JunJieSoDMO sodmo = new JunJieSoDMO();
		
		for (int i = 0; i < bVOs.length; i++) {
			String tssql = "select ts,coperatorid  from so_sale where csaleid = '"+bVOs[i].getCsaleid()+"'";
			Object[] headts = (Object[]) baseDao.executeQuery(tssql, new ArrayProcessor());
			 bVOs[i].m_headts = new UFDateTime(headts[0].toString());
			 bVOs[i].setCoperatorid(headts[1].toString());
			 nc.vo.scm.pub.session.ClientLink cllink=new nc.vo.scm.pub.session.ClientLink(
							corpPK, 
							headts[1].toString(),
							clientLoginDate,
							null, null, null,
							null, null, null,
							false,  null,null,
							null) ;
			 
			//记录界面修改前的处理出库原始状态
			 bVOs[i].setBifinventoryfinish_init(bVOs[i].getBifinventoryfinish());
			 bVOs[i].setBifinvoicefinish_init(bVOs[i].getBifinvoicefinish());
			 bVOs[i].setBsquareendflag_init(bVOs[i].getBsquareendflag());
			
			// 发货安排关闭
			bVOs[i].setBarrangedflag(new UFBoolean(true));
			// 发货关闭
			bVOs[i].setBifreceivefinish(new UFBoolean(true));
			// 出库关闭
			bVOs[i].setBifinventoryfinish(new UFBoolean(true));
			// 运输关闭
			bVOs[i].setBiftransfinish(new UFBoolean(true));
			// 开票关闭
			bVOs[i].setBifinvoicefinish(new UFBoolean(true));
			// 结算关闭
			bVOs[i].setBsquareendflag(new UFBoolean(true));
			// 整单关闭
			bVOs[i].setFinished(new UFBoolean(true));
			bVOs[i].setClientLink(cllink);
			
			//关闭前，需要先删除码单锁定
			String sqlnc = "select * from nc_mdsd  where xsddbt_pk = '"+bVOs[i].getCorder_bid()+"'  and nvl(dr,0)=0 ";
			ArrayList<MdsdVO> listvo =  (ArrayList<MdsdVO>) baseDao.executeQuery(sqlnc, new BeanListProcessor(MdsdVO.class));
			while(listvo.size()>0){
				sodmo.freeMdsdBybid(bVOs[i].getCorder_bid());
				listvo=null;
				listvo = (ArrayList<MdsdVO>) baseDao.executeQuery(sqlnc, new BeanListProcessor(MdsdVO.class));
			}
			
		}
		try {
			dmo = new SaleOrderDMO();
			// 释放数量
			dmo.updateOrderEnd(bVOs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		//构建表头信息
		String hidstr = "(";
		for (int i = 0; i < bVOs.length; i++) {
			hidstr += "'"+bVOs[i].getCsaleid()+"'";
			if(i+1<bVOs.length){
				hidstr += ",";
				}
		}
		hidstr += ")";
		String hsql = "select e.vreceiptcode,e.dbilldate,e.vdef13 from so_sale e where e.csaleid in "+hidstr+"  and nvl(dr,0)=0 ";
		ArrayList<SaleorderHVO> voBeanList =  (ArrayList<SaleorderHVO>) baseDao.executeQuery(hsql.toString(), new BeanListProcessor(SaleorderHVO.class));
		if(voBeanList==null || voBeanList.size()<=0){
			return null ;
		}
		String[][] datas = new String[voBeanList.size()][3];
		for(int i=0;i<voBeanList.size();i++){
			SaleorderHVO hvo =  (SaleorderHVO) voBeanList.get(i);
			datas[i][0] = hvo.getVreceiptcode();
			datas[i][1] = hvo.getDbilldate().toString();
			int day =hvo.getVdef13()==null?0:Integer.parseInt(hvo.getVdef13());
			datas[i][2] = hvo.getDbilldate().getDateAfter(day).toString();
			
		}
		msg.setTitle("留货单到期未发货自动整单关闭预警！");
			msg.setBodyFields(new String[] {
						"订单号","单据日期","留货截止日"});
				msg.setBodyValue(datas);
				msg.setTop(new String[] {
						 "当前服务器日期"
						 , new UFDate().toString()});
				msg.setBodyWidths(new float[] { 0.13f, 0.13f, 0.17f});
				
				return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.bs.pub.pa.IBusinessPlugin#implementReturnMessage(nc.vo.pub.pa.Key[],
	 * java.lang.String, nc.vo.pub.lang.UFDate)
	 */
	public String implementReturnMessage(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.bs.pub.pa.IBusinessPlugin#implementReturnObject(nc.vo.pub.pa.Key[],
	 * java.lang.String, nc.vo.pub.lang.UFDate)
	 */
	public Object implementReturnObject(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.bs.pub.pa.IBusinessPlugin#implementWriteFile(nc.vo.pub.pa.Key[],
	 * java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate)
	 */
	public boolean implementWriteFile(Key[] keys, String fileName,
			String corpPK, UFDate clientLoginDate) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

}
