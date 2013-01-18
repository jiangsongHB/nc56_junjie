package nc.bs.so.backtask.soremain;

import java.util.HashMap;

import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;


//销售订单留货时间超期后台任务   chenjianhua 2013-01-18
public class RemainOvertimeCheckTask implements IBackgroundWorkPlugin {
	private IPFBusiAction baService;

	public String executeTask(BgWorkingContext bgwc) throws BusinessException{
				
		UFDate today=new UFDate();	
        String sWhere="";
        //so_sale.fstatus =2  审批通过
        sWhere  +=" so_sale.vdef13 > 0 and so_sale.fstatus =2 ";
        
        sWhere  +=" and (to_date('"+today.toString()+"', 'yyyy-mm-dd') - to_date(so_sale.dbilldate, 'yyyy-mm-dd')) > so_sale.vdef13 ";
        
        SaleOrderDMO soDmo=null;
		try {
			soDmo = new SaleOrderDMO();
		} catch (NamingException e1) {
			nc.bs.logging.Logger.error(e1.getMessage());		
			//e1.printStackTrace();			
			return e1.getMessage();
		}
        	
		AggregatedValueObject[] sbillvos= soDmo.queryDataByWhere(sWhere);
		
		if(sbillvos==null || sbillvos.length==0){
			return "后台任务完成: 没有数据可处理。";
		}
		String currTime = nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();
		HashMap<String, String> eParam = new HashMap<String, String>();
		eParam.put(PfUtilBaseTools.PARAM_NOAPPROVE, PfUtilBaseTools.PARAM_NOAPPROVE);///不启动流程
		for(int i=0;i<sbillvos.length;i++){
		
		  try {
				//Object processAction= 
			  //调用关闭脚本
		       getBaService().processAction("OrderFinish","30",currTime,null,sbillvos[i],null, eParam);
			} catch (BusinessException e) {
				nc.bs.logging.Logger.error(e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
		String msg="销售订单留货时间超期后台任务完成,";
		msg +=" 本次任务一共关闭"+sbillvos.length+"张销售单。";
					
	     return msg;
		
		
		//Object processAction=null;
		//IPFBusiAction baService = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
				
		/*try {
			 processAction = baService.processAction("SAVEBASE","20",currTime,null,prayvo,null, null);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			 nc.bs.logging.Logger.error(e.getMessage());
		}*/
		/*HashMap eParam=new HashMap();
		eParam.put("nosendmessage", false);
		processAction = getBaService().processAction("APPROVE","20",currTime,null,prayvo,null, eParam);
		return (PraybillVO)processAction;*/
		//return null;
		
	}
	
	private IPFBusiAction getBaService() {
		 if(this.baService==null){
			baService = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
		 }
		 return  (IPFBusiAction) baService ;
	  }
	
	
	//

}
