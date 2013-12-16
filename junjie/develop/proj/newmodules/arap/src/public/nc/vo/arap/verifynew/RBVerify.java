/*
 * �������� 2005-8-12
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class RBVerify implements IVerifyMethod ,IVerifyBusiness{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1869490538074904358L;

	private VerifyCom verifycom;
	
	//ƥ���
	private int pph = 0; 
	private DefaultVerifyRuleVO m_rulevo = null;
	private ArrayList<VerifyLogVO> retArr = new ArrayList<VerifyLogVO>();
	private static long count=0;
	

	
/**
 * @return ���� m_rulevo��
 */
public DefaultVerifyRuleVO getM_rulevo() {
    return m_rulevo;
}
/**
 * @param m_rulevo Ҫ���õ� m_rulevo��
 */
public void setM_rulevo(DefaultVerifyRuleVO m_rulevo) {
    this.m_rulevo = m_rulevo;
}

	
	/**
	 *  
	 */
	public RBVerify() {
		super();
		// TODO �Զ����ɹ��캯�����
	}
	
	public RBVerify(VerifyCom verifycom) {
		super();
		// TODO �Զ����ɹ��캯�����
		this.verifycom=verifycom;
	}

	/**
	 * 	�Ȱ��ؼ����������,�ٰ����ݷ���
	 *	�ؼ���  ����pk  	��ͷpk		��� 	 �����ڲ������Գ�����    �跽�����������������Գ�����
	 *	 AA		0100A	01			100			15						0
	 *	 BB     0100B	01			-80			0						0
	 *	 AA     0100C   01          20			20						5
	 *	 AA     0100D	01			-5			0						0
	 *	 BB     0200A   02          60			0						0
	 *	 AA     0200B   02          -90	    	-30						0
	 *	������
	 *	HashMap AA--{01--(0100A,0100C,0100D), 02--(0200B)}
	 *	        BB--{01--(0100B),02--(0200A)}
	 *	����ǲ��ϸ�ƥ�䣬��Ҫ���������·��飬ͬ����ͬ���󣬲�ͬ����ͬ�����������򣬽��к��
	 * 
	 * ���˳��
	 * 1��ͬ����ͬ��
	 * 2��ͬ����ͬ��
	 * 3����ͬ����ͬ��
	 * 4����ͬ����ͬ��
	 * ���У�3��4�����ҽ����ڲ��ϸ�ƥ��ʱҪִ��
	 * 
	 * 
	 * ����跽�ĺ����Գ壬��������ѭ���󵥾��ڲ��ĶԳ����������� ,�����Գ岻�����ۿ���
	 * ����˼�¼�跽ԭ��Ϊ0�������һ����¼, ���������¼����һ���Գ��¼����ͬһ�ŵ��ݵļ�¼�����˳�ѭ��,�������¼����һ����¼����ֵͬ�ţ��������һ����¼,����ƥ���������һ����¼
	 * Ȼ��ʼ�����ڲ��ϸ�Գ�
	 * �ȱȽϾ���ֵ��С��������ֵС����0������ֵ��ļ�ȥ����ֵС�Ĳ��������š�֮���ٽ��������һ���,ͬʱ�γɺ�����¼�ӵ����������m_logs��
	 * ����Գ����Ҫ���ϸ�Գ壬����з��ϸ�Գ�,���ϸ�Գ�Ļ������ڵ����ڲ����������ĶԳ�ƥ������ڽ��жԳ����
	 * �Ժ�Ĵ��������ڵ����ڲ��Գ壬ֻ�ǲ��ü�鵱ǰ��¼����һ����¼�Ƿ�����ͬһ�����ˣ�ҲҪ���з�Ϊ�ϸ�ƥ��ͷ��ϸ�ƥ�䣬���ϸ�ƥ��Գ�Ļ����Ͻ��з��ϸ�Գ�
	 * �ֹ������ĺ����Գ壬���ϸ�ƥ��
	 * �Զ������ĺ����Գ壬�ϸ�ƥ��
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
		
		//����
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

	    sameObjSameBillCon(jf_vos, keys2); //ͬ��������ͬ��������ͬ��
	    sameObjUnsameBillCon(jf_vos, keys2);//ͬ��������ͬ����������ͬ��
	    sameObjSameBillCon(jf_vos, keys);//��ͬ��������ͬ��������ͬ��
	    sameObjUnsameBillCon(jf_vos, keys);//��ͬ��������ͬ����������ͬ��


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
					
					//add by ouyangzhb 2013-10-29 ��ȡ�����������������������Դ��ͬһ�ŵ��ģ�����Ҫ���к���
					String djpk=vo.getM_djzfpk();
					
					//add by ouyangzhb 2013-11-01 ��ȡ���������������ȽϺ�����������
					String djfbpk=vo.getM_djfbPk();
					
					if(ArapCommonTool.isZero(vo.getM_jsybje()))
						continue;
					for( int j = k;j < df.size();j++){
						VerifyVO vo2 = (VerifyVO)df.elementAt(j);
						if(ArapCommonTool.isZero(vo2.getM_jsybje()))
							continue;
							
						//add by ouyangzhb 2013-10-29 ��ȡ�����������������������Դ��ͬһ�ŵ��ģ�����Ҫ���к���
						String djpk2=vo2.getM_djzfpk();
						if(djpk == djpk2){
							continue;
						}
						//add by ouyangzhb 2013-10-29 ��ȡ�����������������������Դ��ͬһ�ŵ��ģ�����Ҫ���к���end 
						//add by ouyangzhb 2013-11-01 �����Դ���ݹ�Ӧ���س�ģ��������ĵ��ݱ������������ι�ϵ�ĵ��ݲ��ܺ���
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
						//add by ouyangzhb 2013-11-01 �����Դ���ݹ�Ӧ���س�ģ��������ĵ��ݱ������������ι�ϵ�ĵ��ݲ��ܺ���
				
						
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
					
					//add by ouyangzhb 2013-10-29 ��ȡ�����������������������Դ��ͬһ�ŵ��ģ�����Ҫ���к���
					String djpk=vo.getM_djzfpk();
					
					if(ArapCommonTool.isZero(vo.getM_jsybje()))
						continue;
					for( int j = k;j < df.size();j++){
						VerifyVO vo2 = (VerifyVO)df.elementAt(j);
						if(ArapCommonTool.isZero(vo2.getM_jsybje()))
							continue;
							
						//add by ouyangzhb 2013-10-29 ��ȡ�����������������������Դ��ͬһ�ŵ��ģ�����Ҫ���к���
						String djpk2=vo2.getM_djzfpk();
						if(djpk == djpk2){
							continue;
						}
						//add by ouyangzhb 2013-10-29 ��ȡ�����������������������Դ��ͬһ�ŵ��ģ�����Ҫ���к���end 
						
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
	
	//�жϸ�vo�ں����������ڽ跽�����ڴ���
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
//	�ȱȽϾ���ֵ��С��������ֵС����0������ֵ��ļ�ȥ����ֵС�Ĳ��������š�֮���ٽ��������һ���
//	ͬʱ�γɺ�����¼�ӵ����������m_logs��
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
			jf_vo = vo;//����Ϊ�跽
			df_vo = vo2;//�Է�Ϊ����
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
			
		//����
		UFDouble jf_dj = jf_vo.getVerifyDj();
		UFDouble df_dj = df_vo.getVerifyDj();
		UFDouble jf_shl = null;
		UFDouble df_shl = null;
		
		//ԭ�����Ҵ�����
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
		//�������븨��
		VerifyTool.bb_fb_jisuan_new(jf_vo,verifyRate,logvo,getDate(),getPk_corp());

		
//		�����־��clbz����-2���ۿۡ�-1������ֺ�����0��ͬ���ֺ�����1��������桢
//		2����Ʊ�Գ塢Ʊ������; 3�����˷���; 4�������ջ�; 5������ת����6������ת��
		logvo.setM_clbz(new Integer(2));
		
		logvo.setM_jfverifyVO(jf_vo);
		logvo.setM_dfverifyVO(df_vo);
		
//		��������
		logvo.setM_clrq(getM_rulevo().getM_Clrq());
		logvo.setM_clr(getM_rulevo().getM_clr());
		logvo.setM_clnd(getM_rulevo().getM_Clnd());
		logvo.setM_clqj(getM_rulevo().getM_Clqj());
		

		Integer inte_pph = new Integer(pph++);
		logvo.setM_pph(inte_pph);//ƥ���
		
		//ƥ��ŷŵ�������������
		getVerifycom().getM_context().put("PPH", inte_pph);
		
		//����
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
		        jf_shl = jf_clybje.div(jf_dj,getM_rulevo().getM_shlPrecision());//����ȡ����
		        jf_vo.setM_shl(jf_vo.getM_shl().sub(jf_shl));
		    }
			
			logvo.setM_clshl(jf_shl);
		}else {
			logvo.setM_clshl(ArapConstant.DOUBLE_ZERO);
		}
		
		Integer jdbz; //���ý����־λ
		
		if(jf_vo.getM_Direct().intValue()==1)
		{
			jdbz=new Integer(0);
		}
		else
		{
			jdbz=new Integer(1);
		}
		
//		��
		logvo.setM_jdbz(jdbz);
		logvo.setM_dwbm(this.m_rulevo.getM_dwbm());
		getLogs().add(logvo);
		
	//----------------------------------------------------------------------------	
//		��
		VerifyLogVO dai_logvo = logvo.deepclone();	
		dai_logvo.setM_jfverifyVO(df_vo);
		dai_logvo.setM_dfverifyVO(jf_vo);
		
		dai_logvo.setM_clybje(df_clybje);
		
		VerifyTool.bb_fb_jisuan_new(df_vo,verifyRate,dai_logvo,getDate(),getPk_corp());
		
		//����
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
		UFDouble jf_zk = jf_vo.getM_bczk();//�ۿ�
		//�ۿ�
		if(jf_zk!=null && !UFDoubleTool.isZero(jf_zk)){
			VerifyLogVO zk_logvo = new VerifyLogVO();
			//�������ڣ������ˡ���ȡ��ڼ�
			setVerifyEnv(zk_logvo);
			zk_logvo.setM_clybje(jf_zk);
			zk_logvo.setM_jdbz(jf_vo.getM_Direct().intValue()==1?ArapConstant.INT_ZERO:ArapConstant.INT_ONE);
			zk_logvo.setM_clbz(new Integer(-2));	
			zk_logvo.setM_jfverifyVO(jf_vo);
			zk_logvo.setM_dfverifyVO(jf_vo);
			zk_logvo.setM_pph(inte_pph);
			
//			�����ҽ��ʹ����ҽ��,�跽�ʹ�����ͬ�ĸ��һ��ʺ���ͬ�ı��һ���
			VerifyTool.bb_fb_jisuan_new(jf_vo,verifyRate,zk_logvo,getDate(),getPk_corp());
			getLogs().add(zk_logvo);
			jf_vo.setM_bczk(null);
		}
	}
	
	private void setVerifyEnv(VerifyLogVO logvo) {
		//��������
		logvo.setM_clrq(getRuleVO().getM_Clrq());
		
		//������
		logvo.setM_clr(getRuleVO().getM_clr());
		
		logvo.setM_dwbm(getRuleVO().getM_dwbm());
		
		//�����ڼ�
		logvo.setM_clqj(getRuleVO().getM_Clqj());
		
		//�������
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
	 * ���и��Һͱ������ļ���
	 * 
	 * @deprecated
	 * 
	 * �°��Ƴ�
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
			
			

			
//			�跽���㸨�ҽ����㱾�ҽ��
			UFDouble jsfbje = ArapConstant.DOUBLE_ZERO;
			UFDouble jsbbje = ArapConstant.DOUBLE_ZERO;
			
			try {
			    if(fbhl==null || fbhl.equals(ArapConstant.DOUBLE_ZERO)){//���һ���Ϊ���ʾ�����Һ�����߱����Ǳ���
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
	 * @return 0,��������  1�������
	 */
	private int getHxSuanFa(DefaultVerifyRuleVO rulevo) {
		if(rulevo == null){
			return 0;
		}
		Integer inte = rulevo.getM_verifySeq();
		int flag = 0;//Ĭ��Ϊ������
		try{
			flag = inte.intValue();
		}catch(Exception e){
			ExceptionHandler.consume(e);
//			Logger.info("####ERROR:��������verifySeq=0�� �������verifySeq������verifySeq="+inte);
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
