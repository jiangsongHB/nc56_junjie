/*
 * 创建日期 2005-10-26
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.vo.verifynew.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapCommonTool;
import nc.vo.arap.verifynew.UFDoubleTool;
import nc.vo.arap.verifynew.VerifyTool;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * @author xuhb
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class FenPeiUtil {
   
    public static  void  fenTanJsybjeRB(UFDouble jszk,UFDouble bcjs,UFDouble bczk,VerifyVO[] sel_vos,UFDouble zkbl,String pk_corp,UFDate date,int pzglh,UFDouble fbhl,UFDouble bbhl) throws Exception{

    	List<VerifyVO> a=new ArrayList<VerifyVO>();//正
    	List<VerifyVO> n=new ArrayList<VerifyVO>();//负
    	
    	//add by ouyangzhb 2013-11-01 计算本次需要核销的金额
    	HashMap<String, UFDouble> hxje = new HashMap<String, UFDouble>();
    	HashMap<String, UFDouble> hxshl = new HashMap<String, UFDouble>();
    	IUAPQueryBS bsQuery = NCLocator.getInstance().lookup(IUAPQueryBS.class);
    	for(int i=0;i<sel_vos.length;i++){
    		String sql = "select fb.ddhh from arap_djfb fb where fb.fb_oid='"+sel_vos[i].getM_djfbPk()+"'";
    		HashMap<String, String> map = (HashMap<String, String>) bsQuery.executeQuery(sql, new MapProcessor());
    		hxje.put(map.get("ddhh"), sel_vos[i].getM_ybje().multiply(-1));
    		hxshl.put(map.get("ddhh"), sel_vos[i].getM_shl().multiply(-1));
    	}
    	//add by ouyangzhb 2013-11-01 计算本次需要核销的金额
    	
    	
    	if(bczk==null)
		{
			bczk=new UFDouble(0);
		}

    	if(ArapCommonTool.isLargeZero(bcjs))//正
    	{
    		for(int i=0;i<sel_vos.length;i++){
        		if(ArapCommonTool.isLargeEqual(sel_vos[i].getM_ybje(),new UFDouble(0))){
        			a.add(sel_vos[i]);
        		}else{
        			n.add(sel_vos[i]);
        		//	add by ouyangzhb 2013-11-01 pzglh=3,标识是来源与费用发票的核销操作，费用发票的核销，需要按行核销
        			if(pzglh != 3){
        				sel_vos[i].setM_jsybje(sel_vos[i].getM_ybye());
        				bcjs=bcjs.add(sel_vos[i].getM_ybye().abs());
            			jszk=jszk.add(sel_vos[i].getM_ybye().abs());
        			}else{
        				sel_vos[i].setM_jsybje(hxje.get(sel_vos[i].getM_djfbPk()));
        				sel_vos[i].setM_shl(hxshl.get(sel_vos[i].getM_djfbPk()));
        			}
          			/**
        			sel_vos[i].setM_jsybje(sel_vos[i].getM_ybye());
        			bcjs=bcjs.add(sel_vos[i].getM_ybye().abs());
        			jszk=jszk.add(sel_vos[i].getM_ybye().abs());
        			*/
        			//	add by ouyangzhb 2013-11-01 pzglh=3,标识是来源与费用发票的核销操作，费用发票的核销，需要按行核销
        		}
        	}
    		VerifyVO[] temp=new VerifyVO[a.size()];
    		temp=a.toArray(temp);

    		fenTanJsybje(jszk,bcjs,temp,bczk.div(jszk),pk_corp,date,pzglh,fbhl,bbhl);
    		
    	}
    	else
    	{
    		for(int i=0;i<sel_vos.length;i++){
        		if(ArapCommonTool.isLargeEqual(sel_vos[i].getM_ybje(),new UFDouble(0))){
        			a.add(sel_vos[i]);
        			//	add by ouyangzhb 2013-11-01 pzglh=3,标识是来源与费用发票的核销操作，费用发票的核销，需要按行核销
        			if(pzglh != 3){
        				sel_vos[i].setM_jsybje(sel_vos[i].getM_ybye());
        				bcjs=bcjs.sub(sel_vos[i].getM_ybye().abs());
            			jszk=jszk.sub(sel_vos[i].getM_ybye().abs());
        			}else{
        				sel_vos[i].setM_jsybje(hxje.get(sel_vos[i].getM_djfbPk()));
        				sel_vos[i].setM_shl(hxshl.get(sel_vos[i].getM_djfbPk()));
        			}
        			/**
        		 	sel_vos[i].setM_jsybje(sel_vos[i].getM_ybye());
        			bcjs=bcjs.sub(sel_vos[i].getM_ybye().abs());
        			jszk=jszk.sub(sel_vos[i].getM_ybye().abs());
        			*/
        			//	add by ouyangzhb 2013-11-01 pzglh=3,标识是来源与费用发票的核销操作，费用发票的核销，需要按行核销
        		}else{
        			n.add(sel_vos[i]);
        		}
        	}
    		VerifyVO[] temp=new VerifyVO[n.size()];
    		temp = n.toArray(temp);

			fenTanJsybje(jszk,bcjs,temp,bczk.div(jszk),pk_corp,date,pzglh,fbhl,bbhl);
			
    	}
    	 
	}
    
    
    public static  void  fenTanJsybjeRB(UFDouble bcjs, VerifyVO[] sel_vos,UFDouble zkbl,String pk_corp,UFDate date,int pzglh,UFDouble fbhl,UFDouble bbhl) throws Exception{
    	
    	fenTanJsybjeRB(bcjs,bcjs,new UFDouble(0),sel_vos,new UFDouble(0),pk_corp,date,pzglh,fbhl,bbhl);
    	
	}
    
    public static  void  fenTanJsybje(UFDouble jszk,UFDouble bcjs,VerifyVO[] sel_vos,UFDouble zkbl,String pk_corp,UFDate date,int pzglh,UFDouble fbhl,UFDouble bbhl) throws Exception{
    	
    	fenTan(jszk,bcjs,sel_vos,new UFDouble(0),0,true,pk_corp,pzglh,date.toString(),fbhl,bbhl);
    	
		fenTan(jszk,bcjs,sel_vos,zkbl,1,false,pk_corp,pzglh,date.toString(),fbhl,bbhl);
	}
    
    public static  void  fenTanJsybje(UFDouble bcjs,VerifyVO[] sel_vos,UFDouble zkbl,String pk_corp,UFDate date,int pzglh,UFDouble fbhl,UFDouble bbhl) throws Exception{
    	
    	fenTan(bcjs,bcjs,sel_vos,new UFDouble(0),0,true,pk_corp,pzglh,date.toString(),fbhl,bbhl);
    	
	}

//    public static void  fenTanZk(UFDouble jszk,UFDouble bcjs,VerifyVO[] sel_vos,UFDouble zkbl,String pk_corp,int pzglh) throws Exception{
//    	
//    	fenTan(jszk,sel_vos,new UFDouble(0),0,pk_corp,pzglh,null,null,null);
//
//		fenTan(jszk,sel_vos,zkbl,1,pk_corp,pzglh,null,null,null);
//		
//	}
    
    @SuppressWarnings("unchecked")
	private static void fenTan(UFDouble bcjs,UFDouble bcjsP,VerifyVO[] sel_vos,UFDouble zkbl,int flag,boolean first,String pk_corp,int pzglh,String date,UFDouble fbhl,UFDouble bbhl) throws Exception{
		if(bcjs==null||UFDoubleTool.isZero(bcjs) || sel_vos == null || sel_vos.length ==0){
			return;
		}
		String HxMode ="最早余额法";/*-=notranslate=-*/
		if(pzglh==0){
    		HxMode = SysInit.getParaString(pk_corp, "AR1");
   		 }else 	if(pzglh==1){
    		HxMode = SysInit.getParaString(pk_corp, "AP1");
   		 }else if(pzglh==2){
    		HxMode = SysInit.getParaString(pk_corp, "EC1");
    	}
		//add by ouyangzhb 2013-10-31 
   		 else 	if(pzglh==3){
    		HxMode = SysInit.getParaString(pk_corp, "AP1");
   		 }

    	boolean iszzye= HxMode.equals("最早余额法"); /*-=notranslate=-*/
	
  		Arrays.sort(sel_vos,new CompareVerifyZZYE(iszzye));
  		  
		UFDouble bcjs2 = bcjs.abs();
		UFDouble zero = new UFDouble(0);
		UFDouble bcjsSum = new UFDouble(0);
		int digit=2;
		
		if(sel_vos[0]!=null&&sel_vos[0].getM_CurrPk()!=null)
		{
			try{
				digit=Currency.getCurrDigit(sel_vos[0].getM_CurrPk());
			}catch (Exception e) {
				ExceptionHandler.consume(e);
			}
		}
		
		/**add by ouyangzhb 2013-10-31 先对暂估回冲的单据进行核销，核销完了再考虑其余单据的核销*/
		ArrayList<String> pks = new ArrayList<String>();
		if(pzglh == 3){
			for(int i=0;i<sel_vos.length;i++){
	  			if(sel_vos[i].getZgyf()==2){
	  				if(UFDoubleTool.isZero(bcjs2)){
	  					sel_vos[i].setM_isSelected(UFBoolean.FALSE);
	  					if(flag == 0){
	  						sel_vos[i].setM_jsybje(zero);
	  						sel_vos[i].setM_jsfbje(zero);
	  						sel_vos[i].setM_jsbbje(zero);
	  					}else if(flag ==1){
	  						sel_vos[i].setM_bczk(zero);
	  					}
	  					continue;
	  				}
	  				sel_vos[i].setM_isSelected(UFBoolean.TRUE);
	  				
	  				//可用于本次结算的原币余额，与折扣按比例分摊
	  				UFDouble ybye_bcjs = zero;
	  				UFDouble jszkje = null;
	  				UFDouble jsybje = null;
	  				
	  				if(first){//第一次，全额结算
	  					ybye_bcjs = getYbye_bcjs(sel_vos[i]);
	  					jszkje=getYbye_bczk(sel_vos[i]);
	  				}else {//
	  					UFDouble[] jsje_zkje=getYbje_Zkje(zkbl, sel_vos[i],digit);
	  					ybye_bcjs = jsje_zkje[0];
	  					jszkje=jsje_zkje[1];
	  				}
	  				
	  				if(UFDoubleTool.isAbsDayu(ybye_bcjs, bcjs2)){//ybye_bcjs_bczk大于bcjs,结算bcjs
	  					jsybje=UFDoubleTool.isDayu0(bcjs)?bcjs2:bcjs2.multiply(-1);	
	  				}else{
	  					jsybje=ybye_bcjs;	
	  				}
	  				
//	  				结算原币金额
	  				sel_vos[i].setM_jsybje(jsybje);
	  				sel_vos[i].setM_bczk(jszkje);
	  				
	  				bcjsSum=bcjsSum.add(jsybje);
	  				
	  				//结算辅币和本币金额
	  				
	  				fenTanFbBb(sel_vos[i],jsybje,pk_corp,date,fbhl,bbhl);
	  				

	  				bcjs2 = bcjs2.sub(jsybje.abs()).abs();	
	  				
	  				pks.add(sel_vos[i].getM_djfbPk());
	  			}
	  				
	  		}
		}
		
		
		/**add by ouyangzhb 2013-10-31 先对暂估回冲的单据进行核销，核销完了再考虑其余单据的核销 end */
		
		for(int i = 0; i< sel_vos.length; i++){
			
			//add by ouyangzhb 2013-10-31 如果已经分摊了核销的，就跳过，不做分摊
			if(pks.contains(sel_vos[i].getM_djfbPk())){
				continue;
			}
			
			if(UFDoubleTool.isZero(bcjs2)){
				sel_vos[i].setM_isSelected(UFBoolean.FALSE);
				if(flag == 0){
					sel_vos[i].setM_jsybje(zero);
					sel_vos[i].setM_jsfbje(zero);
					sel_vos[i].setM_jsbbje(zero);
				}else if(flag ==1){
					sel_vos[i].setM_bczk(zero);
				}
				continue;
			}
			sel_vos[i].setM_isSelected(UFBoolean.TRUE);
			
			//可用于本次结算的原币余额，与折扣按比例分摊
			UFDouble ybye_bcjs = zero;
			UFDouble jszkje = null;
			UFDouble jsybje = null;
			
			if(first){//第一次，全额结算
				ybye_bcjs = getYbye_bcjs(sel_vos[i]);
				jszkje=getYbye_bczk(sel_vos[i]);
			}else {//
				UFDouble[] jsje_zkje=getYbje_Zkje(zkbl, sel_vos[i],digit);
				ybye_bcjs = jsje_zkje[0];
				jszkje=jsje_zkje[1];
			}
			
			if(UFDoubleTool.isAbsDayu(ybye_bcjs, bcjs2)){//ybye_bcjs_bczk大于bcjs,结算bcjs
				jsybje=UFDoubleTool.isDayu0(bcjs)?bcjs2:bcjs2.multiply(-1);	
			}else{
				jsybje=ybye_bcjs;	
			}
			
//			结算原币金额
			sel_vos[i].setM_jsybje(jsybje);
			sel_vos[i].setM_bczk(jszkje);
			
			bcjsSum=bcjsSum.add(jsybje);
			
			//结算辅币和本币金额
			
			fenTanFbBb(sel_vos[i],jsybje,pk_corp,date,fbhl,bbhl);
			

			bcjs2 = bcjs2.sub(jsybje.abs()).abs();	
		}		
		
		if(flag==1&&!first&&!UFDoubleTool.isZero(bcjsP.sub(bcjsSum, digit)))
		{
			UFDouble chae=bcjsP.sub(bcjsSum, digit);
			for(int i = 0; i< sel_vos.length; i++){
				
				if(ArapCommonTool.isLargeEqual(sel_vos[i].getM_ybye().abs(),(sel_vos[i].getM_jsybje().add(chae)).abs()))
				{
					sel_vos[i].setM_isSelected(UFBoolean.TRUE);
					
					//可用于本次结算的原币余额，与折扣按比例分摊
					UFDouble jszkje = null;
					UFDouble jsybje = null;
					
					jsybje=sel_vos[i].getM_jsybje().add(chae);
					jszkje=sel_vos[i].getM_bczk().sub(chae);

					sel_vos[i].setM_jsybje(jsybje);
					sel_vos[i].setM_bczk(jszkje);
					
					//结算辅币和本币金额
					
					fenTanFbBb(sel_vos[i],jsybje,pk_corp,date,fbhl,bbhl);
					
					
					break;
				}
				
			}	
		}
			
	}
    
    private static UFDouble getYbye_bcjs( VerifyVO vo) {
		UFDouble ybye = (UFDouble)vo.getAttributeValue("ybye");
//		UFDouble ybye_bczk = ybye.multiply(zkbl);
//		
//		UFDouble ybye_bcjs=null;
//		ybye_bcjs = ybye.sub(ybye_bczk);
		return ybye;
	}
    
    private static UFDouble getYbye_bczk( VerifyVO vo){
//		UFDouble ybye = (UFDouble)vo.getAttributeValue("ybye");
//		UFDouble ybye_bczk = ybye.multiply(zkbl);
		return new UFDouble(0);
	}
    
    
    private static UFDouble[] getYbje_Zkje(UFDouble zkbl, VerifyVO vo,int digit) {
		UFDouble ybye = (UFDouble)vo.getAttributeValue("jsybje");
		UFDouble ybye_bczk = ybye.multiply(zkbl,digit);
		
		UFDouble ybye_bcjs=null;
		ybye_bcjs = ybye.sub(ybye_bczk);
		return new UFDouble[]{ybye_bcjs,ybye_bczk};
	}
    
    private static void fenTanFbBb(VerifyVO vo, UFDouble jsybje,String pk_corp,String date,UFDouble fbhl,UFDouble bbhl ) throws Exception{

		String pk_currtype = vo.getM_CurrPk();
//	
//		UFDouble jsfbje =getFbBb(pk_currtype,fbhl,bbhl,jsybje,pk_corp)[0];
//		UFDouble jsbbje = 	getFbBb(pk_currtype,fbhl,bbhl,jsybje,pk_corp)[1];
		UFDouble[] values = Currency.computeYFB(pk_corp, Currency.Change_YBCurr, pk_currtype, jsybje, null,null, fbhl,bbhl, date);
		vo.setM_jsfbje(values[1]);
		vo.setM_jsbbje(values[2]);

	}
       
    public static  Hashtable createHash(VerifyVO[] vos,DefaultVerifyRuleVO rule,Integer seq){
		if(vos == null || vos.length == 0){
			return null;
		}
		Hashtable<String, Vector<VerifyVO>> h = new Hashtable<String, Vector<VerifyVO>>();
		for(int i = 0; i < vos.length; i++){
			if(vos[i]==null){
				continue;
			}
			//主表vouchid
			String vouchid = (String)vos[i].getAttributeValue("vouchid");
			String fb_oid = (String)vos[i].getAttributeValue("fb_oid");
			if(vouchid == null || vouchid.trim().length() == 0
					|| fb_oid == null || fb_oid.trim().length() == 0){
				continue;
			}
			String key = null;
			if(rule.getM_verifyMode().intValue()==0){
				key = vouchid;
			}else{
				key = fb_oid;
			}

			Vector<VerifyVO> v = null;
			if(h.containsKey(key)){
				v =h.get(key);
			}else{
				v = new Vector<VerifyVO>();
			}
			v.add(vos[i]);
			h.put(key,v);
		}
		sort(h,seq);
		return h;
	}
    
    private static  void sort(Hashtable h,Integer seq){
		if(h == null || h.isEmpty()){
			return ;
		}
		Iterator iter =h.keySet().iterator();
		for(;iter.hasNext();){
			Vector v = (Vector)h.get(iter.next());
			VerifyTool.sortVector(v, seq.intValue());
		}
		
	
	}
    
}
