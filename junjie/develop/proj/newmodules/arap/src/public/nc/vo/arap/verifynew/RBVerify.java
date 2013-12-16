/*
 * 创建日期 2005-8-12
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.vo.arap.verifynew;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.fi.pub.Currency;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapCommonTool;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.verifynew.pub.DefaultVerifyRuleVO;
import nc.vo.verifynew.pub.IVerifyMethod;
import nc.vo.verifynew.pub.VerifyCom;
import nc.vo.verifynew.pub.VerifyLogVO;
import nc.vo.verifynew.pub.VerifyVO;

/**
 * @author xuhb
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class RBVerify implements IVerifyMethod ,IVerifyBusiness{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1869490538074904358L;

	private VerifyCom verifycom;
	
	//匹配号
	private int pph = 0; 
	private DefaultVerifyRuleVO m_rulevo = null;
	private ArrayList<VerifyLogVO> retArr = new ArrayList<VerifyLogVO>();
	private static long count=0;
	

	
/**
 * @return 返回 m_rulevo。
 */
public DefaultVerifyRuleVO getM_rulevo() {
    return m_rulevo;
}
/**
 * @param m_rulevo 要设置的 m_rulevo。
 */
public void setM_rulevo(DefaultVerifyRuleVO m_rulevo) {
    this.m_rulevo = m_rulevo;
}

	
	/**
	 *  
	 */
	public RBVerify() {
		super();
		// TODO 自动生成构造函数存根
	}
	
	public RBVerify(VerifyCom verifycom) {
		super();
		// TODO 自动生成构造函数存根
		this.verifycom=verifycom;
	}

	/**
	 * 	先按关键字数组分组,再按单据分组
	 *	关键字  表体pk  	表头pk		余额 	 单据内部红兰对冲后余额    借方（贷方）整个红兰对冲后余额
	 *	 AA		0100A	01			100			15						0
	 *	 BB     0100B	01			-80			0						0
	 *	 AA     0100C   01          20			20						5
	 *	 AA     0100D	01			-5			0						0
	 *	 BB     0200A   02          60			0						0
	 *	 AA     0200B   02          -90	    	-30						0
	 *	分组结果
	 *	HashMap AA--{01--(0100A,0100C,0100D), 02--(0200B)}
	 *	        BB--{01--(0100B),02--(0200A)}
	 *	如果是不严格匹配，需要按单据重新分组，同单不同对象，不同单不同对象，重新排序，进行红冲
	 * 
	 * 红冲顺序：
	 * 1、同对象同单
	 * 2、同对象不同单
	 * 3、不同对象同单
	 * 4、不同对象不同单
	 * 其中，3、4、当且仅当在不严格匹配时要执行
	 * 
	 * 
	 * 处理借方的红蓝对冲，经过整个循环后单据内部的对冲操作就完成了 ,红蓝对冲不处理折扣项
	 * 如果此记录借方原币为0则继续下一条记录, 如果本条记录与下一条对冲记录不是同一张单据的记录，则退出循环,如果本记录和下一条记录本币值同号，则继续下一条记录,规则不匹配则继续下一条记录
	 * 然后开始单据内部严格对冲
	 * 先比较绝对值大小，将绝对值小的置0，绝对值大的减去绝对值小的并保留符号。之后再进行主辅币换算,同时形成核销记录加到核销组件的m_logs中
	 * 如果对冲规则不要求严格对冲，则进行非严格对冲,在严格对冲的基础上在单据内部忽略其他的对冲匹配规则在进行对冲操作
	 * 以后的处理类似于单据内部对冲，只是不用检查当前记录和下一条记录是否属于同一单据了，也要进行分为严格匹配和非严格匹配，在严格匹配对冲的基础上进行非严格对冲
	 * 手工核销的红兰对冲，不严格匹配
	 * 自动核销的红兰对冲，严格匹配
	 */

		 
	public ArrayList<VerifyLogVO> onVerify(VerifyVO[] jf_vos,VerifyVO[] df_vos,DefaultVerifyRuleVO rulevo)
	   {
		long start=System.currentTimeMillis();
		
		getLogs().clear();
		if((jf_vos == null && df_vos == null )|| rulevo == null){
			return null;
		}
		setM_rulevo(rulevo);
		int flag = getHxSuanFa(rulevo);
		
		//排序
		VerifyTool.sortVector(jf_vos, flag);
		VerifyTool.sortVector(df_vos, flag);
		
		if(jf_vos!=null && jf_vos.length!=0)
		{
			String[] jf_keys = rulevo.getM_debtObjKeys();
			if(jf_keys==null)
			{
				jf_keys=new String[0];
			}
			Vector<String> temp=new Vector<String>();
			for(int i=0;i<jf_keys.length;i++)
	    	{
	    		temp.add(jf_keys[i]);
	    	}
			
			String[] keys=new String[temp.size()];
		    temp.copyInto(keys);
		    
		    temp.add("m_wldxpk");
		    String[] keys2=new String[temp.size()];
		    temp.copyInto(keys2);

		    this.contract(jf_vos, rulevo, keys , keys2);
		}
		
	    if(df_vos!=null && df_vos.length!=0)
	    {
	    	String[] df_keys = rulevo.getM_creditObjKeys();
			if(df_keys==null)
			{
				df_keys=new String[0];
			}
			Vector<String> temp1=new Vector<String>();
			for(int i=0;i<df_keys.length;i++)
	    	{
	    		temp1.add(df_keys[i]);
	    	}
		    
			String[] keys1=new String[temp1.size()];
		    temp1.copyInto(keys1);
		    
		    temp1.add("m_wldxpk");
		    String[] keys2=new String[temp1.size()];
		    temp1.copyInto(keys2);
	
		    this.contract(df_vos, rulevo, keys1, keys2);
	    }
	  	
		long end=System.currentTimeMillis();
		ExceptionHandler.debug("Rb="+(end-start));
		ExceptionHandler.debug("count="+count);
		return getLogs();
	}
	
	
	private void contract(VerifyVO[] jf_vos,DefaultVerifyRuleVO rulevo,String[] keys,String[] keys2)
	{

	    sameObjSameBillCon(jf_vos, keys2); //同往来对象同核销条件同单
	    sameObjUnsameBillCon(jf_vos, keys2);//同往来对象同核销条件不同单
	    sameObjSameBillCon(jf_vos, keys);//不同往来对象同核销条件同单
	    sameObjUnsameBillCon(jf_vos, keys);//不同往来对象同核销条件不同单


	}

	private void sameObjUnsameBillCon(VerifyVO[] jf_vos, String[] keys) {
		Hashtable<String,Vector<Vector<VerifyVO>>> hash2=new Hashtable<String, Vector<Vector<VerifyVO>>>();
	    for(int i = 0; i < jf_vos.length; i++){
			String allkeys = VerifyTool.getAllkeys(jf_vos[i], keys);
			
			if(hash2.containsKey(allkeys))
			{
				
				
				if(ArapCommonTool.isLargeZero(jf_vos[i].getM_jsybje()))
				{
					hash2.get(allkeys).get(0).add(jf_vos[i]);
					
				}
				else
				{
					hash2.get(allkeys).get(1).add(jf_vos[i]);
				}
				

			}
			else
			{
				Vector<Vector<VerifyVO>> vec=new Vector<Vector<VerifyVO>>();
				vec.add(new Vector<VerifyVO>());
				vec.add(new Vector<VerifyVO>());
				hash2.put(allkeys, vec);
				
				
				if(ArapCommonTool.isLargeZero(jf_vos[i].getM_jsybje()))
				{
					hash2.get(allkeys).get(0).add(jf_vos[i]);
					
				}
				else
				{
					hash2.get(allkeys).get(1).add(jf_vos[i]);
				}
			}
		}
	    Iterator it=hash2.keySet().iterator();
	    while(it.hasNext())
	    {
	    	
	    		Vector vec=(Vector)hash2.get(it.next());
	    	    Vector jf=(Vector)vec.get(0);
	    	    Vector df=(Vector)vec.get(1);
	    		int k=0;
				for(int i =0; i< jf.size();i++){
					VerifyVO vo =(VerifyVO)jf.elementAt(i);	
					
					//add by ouyangzhb 2013-10-29 获取核销单据主表主键，如果来源是同一张单的，则不需要进行核销
					String djpk=vo.getM_djzfpk();
					
					//add by ouyangzhb 2013-11-01 获取表体主键，用来比较核销的下游行
					String djfbpk=vo.getM_djfbPk();
					
					if(ArapCommonTool.isZero(vo.getM_jsybje()))
						continue;
					for( int j = k;j < df.size();j++){
						VerifyVO vo2 = (VerifyVO)df.elementAt(j);
						if(ArapCommonTool.isZero(vo2.getM_jsybje()))
							continue;
							
						//add by ouyangzhb 2013-10-29 获取核销单据主表主键，如果来源是同一张单的，则不需要进行核销
						String djpk2=vo2.getM_djzfpk();
						if(djpk == djpk2){
							continue;
						}
						//add by ouyangzhb 2013-10-29 获取核销单据主表主键，如果来源是同一张单的，则不需要进行核销end 
						//add by ouyangzhb 2013-11-01 如果来源是暂估应付回冲的，所核销的单据必须是有上下游关系的单据才能核销
						IUAPQueryBS bsQuery = NCLocator.getInstance().lookup(IUAPQueryBS.class);
						if(vo2.getZgyf() == 2){
							String sql = "select fb.ddhh from arap_djfb fb where fb.fb_oid='"+vo2.getM_djfbPk()+"'";
				    		try {
								HashMap<String, String> map = (HashMap<String, String>) bsQuery.executeQuery(sql, new MapProcessor());
								String djpk3 = map.get("ddhh");
								if(djpk3 != null &&  !djfbpk.equals(djpk3)){
									continue ;
								}
							} catch (BusinessException e) {
								e.printStackTrace();
							}
						}
						//add by ouyangzhb 2013-11-01 如果来源是暂估应付回冲的，所核销的单据必须是有上下游关系的单据才能核销
				
						
						doBusiness(vo, vo2);
						if(ArapCommonTool.isZero(vo.getM_jsybje()))
						{
							k=j;
							break;
						}
							
					}
				}
	    	}
	}
	private void sameObjSameBillCon(VerifyVO[] jf_vos, String[] keys) {
		Hashtable<String,Hashtable<String,Vector<Vector<VerifyVO>>>> hash1=new Hashtable<String, Hashtable<String,Vector<Vector<VerifyVO>>>>();
	    for(int i = 0; i < jf_vos.length; i++){
			String allkeys = VerifyTool.getAllkeys(jf_vos[i], keys);
			String djpk=jf_vos[i].getM_djzfpk();
			if(hash1.containsKey(allkeys))
			{
				if(hash1.get(allkeys).containsKey(djpk))
				{
					if(ArapCommonTool.isLargeZero(jf_vos[i].getM_jsybje()))
					{
						if(hash1.get(allkeys).get(djpk).get(0)!=null)
						  hash1.get(allkeys).get(djpk).get(0).add(jf_vos[i]);
						else
						{
							Vector<VerifyVO> vect=new Vector<VerifyVO>();
							hash1.get(allkeys).get(djpk).set(0, vect);
							vect.add(jf_vos[i]);
						}
					}
					else
					{
						if(hash1.get(allkeys).get(djpk).get(1)!=null)
						   hash1.get(allkeys).get(djpk).get(1).add(jf_vos[i]);
						else
						{
							Vector<VerifyVO> vect=new Vector<VerifyVO>();
							hash1.get(allkeys).get(djpk).set(1, vect);
							vect.add(jf_vos[i]);
						}
					}
				}
				else
				{
					
					Vector<Vector<VerifyVO>> vec=new Vector<Vector<VerifyVO>>();
					vec.add(new Vector<VerifyVO>());
					vec.add(new Vector<VerifyVO>());
					hash1.get(allkeys).put(djpk, vec);
					if(ArapCommonTool.isLargeZero(jf_vos[i].getM_jsybje()))
					{
						hash1.get(allkeys).get(djpk).get(0).add(jf_vos[i]);
					}
					else
					{
						hash1.get(allkeys).get(djpk).get(1).add(jf_vos[i]);
					}
					
				}
			}
			else
			{
				Hashtable<String,Vector<Vector<VerifyVO>>> table=new Hashtable<String, Vector<Vector<VerifyVO>>>();
				hash1.put(allkeys, table);
				Vector<Vector<VerifyVO>> vec=new Vector<Vector<VerifyVO>>();
				vec.add(new Vector<VerifyVO>());
				vec.add(new Vector<VerifyVO>());
				hash1.get(allkeys).put(djpk, vec);
				if(ArapCommonTool.isLargeZero(jf_vos[i].getM_jsybje()))
				{
					hash1.get(allkeys).get(djpk).get(0).add(jf_vos[i]);
				}
				else
				{
					hash1.get(allkeys).get(djpk).get(1).add(jf_vos[i]);
				}
			}
		}
	    
	    Iterator it=hash1.keySet().iterator();
	    while(it.hasNext())
	    {
	    	Hashtable table=(Hashtable)hash1.get(it.next());
	    	Iterator it1=table.keySet().iterator();
	    	while(it1.hasNext())
	    	{
	    		Vector vec=(Vector)table.get(it1.next());
	    	    Vector jf=(Vector)vec.get(0);
	    	    Vector df=(Vector)vec.get(1);
	    		int k=0;
				for(int i =0; i< jf.size();i++){
					VerifyVO vo =(VerifyVO)jf.elementAt(i);
					
					//add by ouyangzhb 2013-10-29 获取核销单据主表主键，如果来源是同一张单的，则不需要进行核销
					String djpk=vo.getM_djzfpk();
					
					if(ArapCommonTool.isZero(vo.getM_jsybje()))
						continue;
					for( int j = k;j < df.size();j++){
						VerifyVO vo2 = (VerifyVO)df.elementAt(j);
						if(ArapCommonTool.isZero(vo2.getM_jsybje()))
							continue;
							
						//add by ouyangzhb 2013-10-29 获取核销单据主表主键，如果来源是同一张单的，则不需要进行核销
						String djpk2=vo2.getM_djzfpk();
						if(djpk == djpk2){
							continue;
						}
						//add by ouyangzhb 2013-10-29 获取核销单据主表主键，如果来源是同一张单的，则不需要进行核销end 
						
						doBusiness(vo, vo2);
						if(ArapCommonTool.isZero(vo.getM_jsybje()))
						{
							k=j;
							break;
						}
							
					}
				}
	    	}
	    }
	}

	private ArrayList<VerifyLogVO> getLogs(){
		return retArr;
	}
	
	//判断该vo在红冲过程中是在借方还是在贷方
	private boolean isJieFang(VerifyVO vo){
		if(vo == null){
			return false;
		}
		try{
			return vo.getM_Direct().intValue() ==0 ;
		}catch(Exception e){
			ExceptionHandler.debug(e.getMessage());
		}
		return false;
	}
	
	private boolean isYbyeDayu0(VerifyVO vo){
		if(vo == null){
			return false;
		}
		try{
			return UFDoubleTool.isDayu0(vo.getM_ybye()) ;
		}catch(Exception e){
			ExceptionHandler.consume(e);
		}
		return false;
	}
//	先比较绝对值大小，将绝对值小的置0，绝对值大的减去绝对值小的并保留符号。之后再进行主辅币换算
//	同时形成核销记录加到核销组件的m_logs中
	private void hongchong(VerifyVO vo,VerifyVO vo2){
		if(vo == null || vo2 == null){
			return ;
		}
		
		UFDouble jsybje = vo.getM_jsybje();
		UFDouble jsybje2 = vo2.getM_jsybje();
		
		if(UFDoubleTool.isZero(jsybje)|| UFDoubleTool.isZero(jsybje2)){
			return ;
		}
		if(UFDoubleTool.isTonghao(jsybje, jsybje2)){
			return ;
		}
		
		VerifyVO jf_vo = null;
		VerifyVO df_vo = null;
		
		if(isJieFang(vo) && isYbyeDayu0(vo) || !isJieFang(vo) && !isYbyeDayu0(vo)){
			jf_vo = vo;//本方为借方
			df_vo = vo2;//对方为贷方
		}else{
			jf_vo = vo2;
			df_vo = vo;
		}
		hongchongJD(jf_vo, df_vo);
		
	}
	/**
	 * @param vo
	 * @param vo2
	 * @param ybye
	 * @param ybye2
	 * @param jf_vo
	 * @param df_vo
	 */
	private void hongchongJD( VerifyVO jf_vo, VerifyVO df_vo) {
		
		UFDouble jf_jsybje =jf_vo.getM_jsybje();
		UFDouble df_jsybje = df_vo.getM_jsybje();
		
		UFDouble fu1 = new UFDouble(-1);
			
		//数量
		UFDouble jf_dj = jf_vo.getVerifyDj();
		UFDouble df_dj = df_vo.getVerifyDj();
		UFDouble jf_shl = null;
		UFDouble df_shl = null;
		
		//原付本币处理金额
		UFDouble jf_clybje  = null;
		UFDouble df_clybje = null;
		
		boolean isJfClear=false;
		boolean isDfClear=false;
		if(UFDoubleTool.isXiangFan(jf_jsybje, df_jsybje)){
			jf_clybje = jf_jsybje;
			df_clybje = df_jsybje;
			
			isDfClear=true;
			isJfClear=true;
		}

		if(UFDoubleTool.isAbsDayu(jf_jsybje, df_jsybje)){
			isDfClear=true;
			jf_clybje = df_jsybje.multiply(fu1);
			df_clybje = df_jsybje;
		}else{
			isJfClear=true;
			jf_clybje = jf_jsybje;
			df_clybje = jf_jsybje.multiply(fu1);
		}
		
		
		jf_vo.setM_jsybje(jf_vo.getM_jsybje().sub(jf_clybje));
		
		df_vo.setM_jsybje(df_vo.getM_jsybje().sub(df_clybje));

		VerifyLogVO logvo = new VerifyLogVO();	
		try
		{
			jf_clybje.setScale(Currency.getCurrDigit(jf_vo.getM_CurrPk()), UFDouble.ROUND_HALF_DOWN);
		}
		catch(Exception e)
		{
			ExceptionHandler.debug(e.getMessage());
		}
		logvo.setM_clybje(jf_clybje);
		
		UFDouble[] verifyRate = VerifyTool.getVerifyRate(jf_vo, df_vo);
		//处理本币与辅币
		VerifyTool.bb_fb_jisuan_new(jf_vo,verifyRate,logvo,getDate(),getPk_corp());

		
//		处理标志（clbz）：-2—折扣、-1、异币种核销、0、同币种核销，1、汇兑损益、
//		2—红票对冲、票据贴现; 3、坏账发生; 4、坏账收回; 5、并帐转出；6、并帐转入
		logvo.setM_clbz(new Integer(2));
		
		logvo.setM_jfverifyVO(jf_vo);
		logvo.setM_dfverifyVO(df_vo);
		
//		处理日期
		logvo.setM_clrq(getM_rulevo().getM_Clrq());
		logvo.setM_clr(getM_rulevo().getM_clr());
		logvo.setM_clnd(getM_rulevo().getM_Clnd());
		logvo.setM_clqj(getM_rulevo().getM_Clqj());
		

		Integer inte_pph = new Integer(pph++);
		logvo.setM_pph(inte_pph);//匹配号
		
		//匹配号放到核销上下文中
		getVerifycom().getM_context().put("PPH", inte_pph);
		
		//数量
		if(isJfClear){
			logvo.setM_clshl(jf_vo.getM_shl());
		}else if(jf_dj!=null && !UFDoubleTool.isZero(jf_dj)){
		    if(ArapCommonTool.isZero(jf_vo.getM_jsybje()))
			{
		        jf_shl=jf_vo.getM_shl();
		        jf_vo.setM_shl(new UFDouble(0));
			}
		    else
		    {
		        jf_shl = jf_clybje.div(jf_dj,getM_rulevo().getM_shlPrecision());//数量取精度
		        jf_vo.setM_shl(jf_vo.getM_shl().sub(jf_shl));
		    }
			
			logvo.setM_clshl(jf_shl);
		}else {
			logvo.setM_clshl(ArapConstant.DOUBLE_ZERO);
		}
		
		Integer jdbz; //设置借贷标志位
		
		if(jf_vo.getM_Direct().intValue()==1)
		{
			jdbz=new Integer(0);
		}
		else
		{
			jdbz=new Integer(1);
		}
		
//		借
		logvo.setM_jdbz(jdbz);
		logvo.setM_dwbm(this.m_rulevo.getM_dwbm());
		getLogs().add(logvo);
		
	//----------------------------------------------------------------------------	
//		贷
		VerifyLogVO dai_logvo = logvo.deepclone();	
		dai_logvo.setM_jfverifyVO(df_vo);
		dai_logvo.setM_dfverifyVO(jf_vo);
		
		dai_logvo.setM_clybje(df_clybje);
		
		VerifyTool.bb_fb_jisuan_new(df_vo,verifyRate,dai_logvo,getDate(),getPk_corp());
		
		//数量
		if(isDfClear){
			dai_logvo.setM_clshl(df_vo.getM_shl());
		}else if(df_dj!=null && !UFDoubleTool.isZero(df_dj)){
		    if(ArapCommonTool.isZero(df_vo.getM_jsybje()))
			{
		        df_shl=df_vo.getM_shl();
		        df_vo.setM_shl(new UFDouble(0));
			}
		    else
		    {
		        df_shl = df_clybje.div(df_dj,getM_rulevo().getM_shlPrecision().intValue());
		        df_vo.setM_shl(df_vo.getM_shl().sub(df_shl));
		    }
			
			dai_logvo.setM_clshl(df_shl);
		}else{
			dai_logvo.setM_clshl(ArapConstant.DOUBLE_ZERO);
		}
		
		getLogs().add(dai_logvo);
		this.createZKLogvo(jf_vo,verifyRate,inte_pph);
		this.createZKLogvo(df_vo,verifyRate,inte_pph);
	}
	
	private void createZKLogvo(VerifyVO jf_vo,  UFDouble[] verifyRate, Integer inte_pph) {
		if(jf_vo == null){
			return;
		}
		UFDouble jf_zk = jf_vo.getM_bczk();//折扣
		//折扣
		if(jf_zk!=null && !UFDoubleTool.isZero(jf_zk)){
			VerifyLogVO zk_logvo = new VerifyLogVO();
			//处理日期，处理人、年度、期间
			setVerifyEnv(zk_logvo);
			zk_logvo.setM_clybje(jf_zk);
			zk_logvo.setM_jdbz(jf_vo.getM_Direct().intValue()==1?ArapConstant.INT_ZERO:ArapConstant.INT_ONE);
			zk_logvo.setM_clbz(new Integer(-2));	
			zk_logvo.setM_jfverifyVO(jf_vo);
			zk_logvo.setM_dfverifyVO(jf_vo);
			zk_logvo.setM_pph(inte_pph);
			
//			处理辅币金额和处理本币金额,借方和贷方相同的辅币汇率和相同的本币汇率
			VerifyTool.bb_fb_jisuan_new(jf_vo,verifyRate,zk_logvo,getDate(),getPk_corp());
			getLogs().add(zk_logvo);
			jf_vo.setM_bczk(null);
		}
	}
	
	private void setVerifyEnv(VerifyLogVO logvo) {
		//处理日期
		logvo.setM_clrq(getRuleVO().getM_Clrq());
		
		//处理人
		logvo.setM_clr(getRuleVO().getM_clr());
		
		logvo.setM_dwbm(getRuleVO().getM_dwbm());
		
		//处理期间
		logvo.setM_clqj(getRuleVO().getM_Clqj());
		
		//处理年度
		logvo.setM_clnd(getRuleVO().getM_Clnd());
	}


	private DefaultVerifyRuleVO getRuleVO( ){
		return m_rulevo;
	}
//	public Currency getCurrArith() {
//		if(getPk_corp()!=null){
//			try{
//			return new Currency();
//			}catch(Exception e){
//				
//			}
//		}
//		return new Currency();
//
//    }
    public java.lang.String getPk_corp() {
    	try{
    		return  this.getRuleVO().getM_dwbm();
    	}catch(Exception e){
    		ExceptionHandler.consume(e);
    	}
    	return null;
    }
	private String getDate(){
//		try{
//			return ClientEnvironment.getInstance().getBusinessDate().toString();
//		}catch(Exception e){
//			
//		}
//		return new Date(System.currentTimeMillis()).toString();
	    return getRuleVO().getM_Clrq().toString();
	}
	
	/**
	 * 
	 * 进行辅币和本币余额的计算
	 * 
	 * @deprecated
	 * 
	 * 下版移除
	 */
	private void bb_fb_jisuan(VerifyVO vo,VerifyLogVO logvo) {
		if(vo == null || logvo == null){
			return;
		}

		UFDouble jsybje = logvo.getM_clybje();
		String pk_currtype = vo.getM_CurrPk();
		String date = getDate();

		try
		{
			UFDouble bbhl=Currency.getRateBoth(this.getPk_corp(),pk_currtype,date)[1];
			UFDouble fbhl=Currency.getRateBoth(this.getPk_corp(),pk_currtype,date)[0];
			
			if(vo.getM_Direct().intValue()==-1){
			    bbhl = getRuleVO().getM_creditoBBExchange_rate();
				fbhl = getRuleVO().getM_creditoFBExchange_rate();
			}
			else
			{
				bbhl = getRuleVO().getM_debttoBBExchange_rate();
				fbhl = getRuleVO().getM_debttoFBExchange_rate();
			}
			
			String fbpk = getRuleVO().getM_fbpk();
			String bbpk = getRuleVO().getM_bbpk();
			
			

			
//			借方结算辅币金额，结算本币金额
			UFDouble jsfbje = ArapConstant.DOUBLE_ZERO;
			UFDouble jsbbje = ArapConstant.DOUBLE_ZERO;
			
			try {
			    if(fbhl==null || fbhl.equals(ArapConstant.DOUBLE_ZERO)){//辅币汇率为零表示单主币核算或者币种是本币
			        jsbbje = Currency.getAmountByOpp(this.getPk_corp(),pk_currtype, bbpk, jsybje, bbhl, date);	
			    }else{
			        jsfbje = Currency.getAmountByOpp(this.getPk_corp(),pk_currtype, fbpk, jsybje, fbhl, date);
					jsbbje = Currency.getAmountByOpp(this.getPk_corp(),fbpk, bbpk, jsfbje, bbhl, date);	
			    }
						 
			} catch (Exception e) {
				ExceptionHandler.consume(e);
			}
			logvo.setM_clfbje(jsfbje);
			logvo.setM_clbbje(jsbbje);
		}
		catch(Exception e)
		{
			ExceptionHandler.consume(e);
		}
		

	}
/**
	 * @param rulevo
	 * @return 0,最早余额法，  1，最近余额法
	 */
	private int getHxSuanFa(DefaultVerifyRuleVO rulevo) {
		if(rulevo == null){
			return 0;
		}
		Integer inte = rulevo.getM_verifySeq();
		int flag = 0;//默认为最早余额法
		try{
			flag = inte.intValue();
		}catch(Exception e){
			ExceptionHandler.consume(e);
//			Logger.info("####ERROR:最早余额法：verifySeq=0， 最近余额法：verifySeq，现在verifySeq="+inte);
		}
		return flag;
	}





	public VerifyCom getVerifycom() {
		return verifycom;
	}
	public void setVerifycom(VerifyCom verifycom) {
		this.verifycom = verifycom;
	}

	/* 
	 * @see nc.vo.arap.verifynew.IVerifyBusiness#doBusiness()
	 */
	public void doBusiness(VerifyVO vo, VerifyVO vo2) {
		count++;
		hongchong(vo,vo2);
	}
}
