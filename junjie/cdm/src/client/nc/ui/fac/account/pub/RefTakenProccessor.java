package nc.ui.fac.account.pub;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.itf.cdm.util.CommonUtil;
import nc.itf.cdm.util.MathUtil;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefValueVO;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.tm.framework.util.ClientInfo;
import nc.ui.tm.framework.util.CurrencyClientUtil;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IExAggVO;

public abstract class RefTakenProccessor implements IRefTakenProccessor, ICreateMap<String, Object>{
	
	public final static int CACHE_BATCH = 0;
	public final static int CACHE_ROW = 1;
	public final static int CACHE_CELL = 2;
	
	private static CurrencyClientUtil currencyClientUtil = ClientInfo.getCurrencyClientUtil();

	private BillListPanel listPanel = null;

	private BillCardPanel cardPanel = null;

	private ITakenColUtil takenColUtil = null;

	private HashMap<String,String> tabCodeRefMap = null;

	public static String LIST = "**list**";

	public static String CARD = "**card**";
	
	public static String HEAD = "**head**";
	public static String BODY = "**body**";
	public static String TAIL = "**tail**";
	
	private HashMap<String,Object> cacheMap = new HashMap<String,Object>();
	
	private CurrtypeQuery m_currtype; //CurrType是专门为币种的默认实现而加的
	
	private int maxCount = 100;
	
	private int cacheType = CACHE_ROW;
	private int cacheRowCount = 25;
	
	private AggregatedValueObject cardVo = null;


	/**
	 * 作者：lisg <br>
	 * 日期：2006-12-19
	 *
	 * @see nc.ui.fac.account.pub.IRefTakenProccessor#getCacheMap()
	 */
	public final HashMap<String,Object> getCacheMap() {
		return cacheMap;
	}

	public RefTakenProccessor(BillListPanel listpanel, BillCardPanel cardpanel) {
		listPanel = listpanel;
		if (listPanel != null) {
			try {
				//初始化列表表头
				Field f_templatevo = listPanel.getBillListData().getClass().getDeclaredField("billTempletVO");
				f_templatevo.setAccessible(true);
				BillTempletVO templatevo = (BillTempletVO)f_templatevo.get(listPanel.getBillListData());
				for(BillTabVO tabvo : templatevo.getHeadVO().getStructvo().getBillTabVOs()){
					if(tabvo.getPos() == IBillItem.BODY){
						initTabCodes(new String[]{tabvo.getTabcode()}, LIST, BODY);
					}else{
						initTabCodes(new String[]{tabvo.getTabcode()}, LIST, HEAD);
					}
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(),e);
			}
		}

		cardPanel = cardpanel;
		if (cardPanel != null) {
			initTabCodes(cardPanel.getBillData().getBodyTableCodes(), CARD, BODY);
			initTabCodes(cardPanel.getBillData().getHeadTableCodes(), CARD, HEAD);
		}
	}

	private void initTabCodes(String[] codes, String pos, String detail) {
		for (int i = 0; codes != null && i < codes.length; i++) {
			getMap().put(codes[i]+"_"+pos, detail);
		}
	}

	private ReverseRefTakenVO getReverseRefTakenVO(String toBeRenderedItemKey) {
		return getTakenColUtil().getRefTakenVOMap().getReverseRefTakenVO(
				toBeRenderedItemKey);
	}
	
	private ReverseRefTakenVO getReverseRefTakenVO(String tablecode,String toBeRenderedItemKey) {
		return getTakenColUtil().getRefTakenVOMap().getReverseRefTakenVO(tablecode,
				toBeRenderedItemKey);
	}

	private RefTakenVO getRefTakenVO(String sourceItemKey) {
		return getTakenColUtil().getRefTakenVOMap()
				.getRefTakenVO(sourceItemKey);
	}
	
	private RefTakenVO getRefTakenVO(String tablecode,String sourceItemKey) {
		return getTakenColUtil().getRefTakenVOMap()
				.getRefTakenVO(tablecode,sourceItemKey);
	}

	private BillModel getBillModel(String tabcode, String pos) {
		BillModel ret = null;
		if(tabcode!=null && getMap().containsKey(tabcode+"_"+pos)){
			if (pos.equals(LIST)) {
					String[] bodycodes = getListPanel().getBillListData().getBodyTableCodes();
					for(int i = 0; i < CommonUtil.getArrayLength(bodycodes); i++){
						if(tabcode.equals(bodycodes[i])){
							ret = getListPanel().getBodyBillModel(tabcode);
						}
					}
					if(ret == null){
						ret = getListPanel().getHeadBillModel();
					}
			} else if(pos.equals(CARD)){
				ret = getCardPanel().getBillModel(tabcode);
			}
		}
		
		return ret;
	}
	
	private BillModel getBillModel(String tabcode) {
		BillModel bm = null;
		if(getListPanel()!=null && getCardPanel()!=null){
			Container c = getAbstractUI(getCardPanel());
			if(c instanceof BillManageUI){
				if(((BillManageUI)c).isListPanelSelected()){
					bm = getBillModel(tabcode,LIST);
				}else{
					bm = getBillModel(tabcode,CARD);
					if(bm == null){
						bm = getBillModel(tabcode,LIST);
					}
				}
			}
			
		}else if(getListPanel() == null){
			bm = getBillModel(tabcode,CARD);
		}else if(getCardPanel() == null){
			bm = getBillModel(tabcode,LIST);
		}
		
		return bm;
	}
	
	private String getPosition(){
		String ret = LIST;
		if(getListPanel()!=null && getCardPanel()!=null){
			Container c = getCardPanel().getParent().getParent();
			if(c instanceof BillManageUI){
				if(((BillManageUI)c).isListPanelSelected()){
					ret = LIST;
				}else{
					ret = CARD;
				}
			}
		}else if(getListPanel() == null){
			ret = CARD;
		}else if(getCardPanel() == null){
			ret = LIST;
		}
		
		return ret;
	}
	
	/**
	 * 该函数由一个地方调用
	 * 界面显示的时候
	 * @param sourceItem
	 * @param targetItem
	 * @param isEdit
	 * @return
	 */
	protected boolean isTakenInCard(BillItem sourceItem, BillItem targetItem, boolean isEdit){
		boolean ret = (targetItem!=null) && (CommonUtil.isNull(targetItem.getValue()) || isEdit);
		
		if(!isEdit && targetItem!=null){
			String tabcode = targetItem.getTableCode();
			String itemkey = targetItem.getKey();
			if(getTakenColUtil().getTabCodeSpecialMap().containsKey(tabcode)){
				SpecialTakenVO vo = getTakenColUtil().getTabCodeSpecialMap().get(tabcode);
				if(vo.isExist(itemkey)){
					ret = false;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 该函数由两个地方调用
	 * 1.列表编辑的时候takenListRefValue, isEdit = true;
	 * 2.列表非编辑的时候走renderCell, isEdit = false;
	 * @param sourceItem
	 * @param targetItem
	 * @param isEdit
	 * @return
	 */
	protected boolean isTakenInList(BillItem sourceItem, BillItem targetItem, boolean isEdit){
		boolean ret = true;
		if(targetItem!=null){
			String tabcode = targetItem.getTableCode();
			String itemkey = targetItem.getKey();
			if(getTakenColUtil().getTabCodeSpecialMap().containsKey(tabcode)){
				SpecialTakenVO vo = getTakenColUtil().getTabCodeSpecialMap().get(tabcode);
				if(vo.isExist(itemkey)){
					ret = false;
				}
			}
		}
		
		return ret;
	}

	public void takenCardRefValue(String sourceItemKey, boolean isEdit) {
		RefTakenVO sourceRefTakenVO = getRefTakenVO(sourceItemKey);
		if (sourceRefTakenVO != null) {
			BillItem sourceItem = getCardPanel().getHeadItem(
					sourceRefTakenVO.getSourceRefKey());
			if(sourceItem == null){
				return;
			}
			BillItem targetItem = null;

			UIRefPane ref = ((UIRefPane) sourceItem.getComponent());

			String[] toBeRenderedKeys = sourceRefTakenVO.getToBeRenderedKeys();
			String[] refModelAttrKeys = sourceRefTakenVO.getRefModelAttrKeys();
			for (int j = 0; j < toBeRenderedKeys.length; j++) {
				targetItem = getCardPanel().getHeadItem(
						sourceRefTakenVO.getToBeRenderedKeys()[j]);
				if(!isTakenInCard(sourceItem, targetItem, isEdit)){
					continue;
				}else{ 
					//解决携带的值不能放到bufferdata中的问题
					Object refValue = ref.getRefValue(refModelAttrKeys[j]);
					if(refValue !=null){
						HashMap<String , CurrencyDecimalVO> currencyDecimalVOs = getCurrencyDecimalVO(targetItem.getTableCode());
						if(currencyDecimalVOs != null){							
							CurrencyDecimalVO currencyDecimalVO = currencyDecimalVOs.get(targetItem.getKey());
							if(currencyDecimalVO != null){								
								String pk_currency = (String)getValueByTakenInCard(currencyDecimalVO.getCurrencyKey());
								if(pk_currency != null){						
									getCurrencyClientUtil().setPk_currtype_y(pk_currency);
									String moneyKeyType = currencyDecimalVO.getTypeByItemKey(targetItem.getKey());
									int digit = getDecimalDigit(moneyKeyType);
									if(digit != -1){
										targetItem.setDecimalDigits(digit);
										refValue = new UFDouble(refValue.toString());
										refValue = ((UFDouble) refValue).setScale(0 - digit, UFDouble.ROUND_HALF_UP);
									}
								}
							}
						}
					}
					if(refValue != null && refValue instanceof RefValueVO){
						refValue = ((RefValueVO)refValue).getOriginValue();
					}
					
					if(refValue == null && !sourceRefTakenVO.isTakenValueIfNull()){
						continue;
					}
					targetItem.setValue(refValue);
					refValue = targetItem.converType(targetItem.getValueObject());
					if(!isEdit){
						fixSelfData(targetItem,refValue, -1);
					}

					if (targetItem.getDataType() == IBillItem.UFREF
							|| targetItem.getDataType() == IBillItem.USERDEF) {
						takenCardRefValue(sourceRefTakenVO.getToBeRenderedKeys()[j], isEdit);
					}
				}

			}
		}else{
//			//处理币种变化的时候，汇率的变化
//			BillItem sourceItem = getCardPanel().getHeadItem(
//					sourceRefTakenVO.getSourceRefKey());
//			HashMap<String, CurrencyDecimalVO> currencyDecimalVOs = getCurrencyDecimalVO(sourceItem.getTableCode());
//			if(currencyDecimalVOs!=null 
//					&& currencyDecimalVOs.get(sourceRefTakenVO.getSourceRefKey())!=null
//					&& currencyDecimalVOs.get(sourceRefTakenVO.getSourceRefKey()).getCurrencyKey().equals(sourceRefTakenVO.getSourceRefKey())){
//				//币种的监听变化
//				CurrencyDecimalVO currencyDecimalVO = currencyDecimalVOs.get(sourceRefTakenVO.getSourceRefKey());
//				String pk_currency = (String)getValueByTakenInCard(sourceRefTakenVO.getSourceRefKey());
//				getCurrencyPublicUtil().setPk_currtype_y(pk_currency);
//				UFDate exChangeDate = null;
//				if(currencyDecimalVO.getItemKeyByType(CurrencyDecimalVO.ZD_ITEMKEY)!=null){
//					exChangeDate = (UFDate)getValueByTakenInCard(currencyDecimalVO.getItemKeyByType(CurrencyDecimalVO.ZD_ITEMKEY));
//				}
//				if(exChangeDate == null){
//					exChangeDate = ClientInfo.getCurrentDate();
//				}
//				getCurrencyPublicUtil().getExchangeRate(exChangeDate.toString());
//			}
			
		}

	}
	
	private AbstractBillUI getAbstractUI(Component c){
		Component retValue=null;
		retValue = c;
		while (!(retValue == null || retValue instanceof AbstractBillUI)){
			retValue = retValue.getParent();
		}
		return (AbstractBillUI)retValue;
	}
	
	protected AbstractBillUI getAbstractUI(){
		AbstractBillUI ui = null;
		if(getCardPanel()!=null){
			ui = getAbstractUI(getCardPanel());
		}
		
		if(ui == null && getListPanel()!=null){
			ui = getAbstractUI(getListPanel());
		}
		return ui;
	}
	
	protected void fixSelfData(BillItem targetItem, Object refValue, int row){

		AbstractBillUI ui = getAbstractUI();
			
		if(ui==null){
			return;
		}
		
		AggregatedValueObject vo = null;
		if(row == -1){
			//说明是卡片界面，而且是表头
			vo = ui.getBufferData().getCurrentVO();
			if(vo!=null)
				vo.getParentVO().setAttributeValue(targetItem.getKey(), refValue);
		}else{
			String tableCode = targetItem.getTableCode();
			//说明是走render的时候，可能是卡片界面也可能是列表界面	
			String detail = getMap().get(tableCode+"_"+getPosition());
			if(detail!=null){
				if(detail.equals(HEAD)){
					vo = ui.getBufferData().getVOByRowNo(row);
					//说明是表头的列表render
					if(vo!=null)
						vo.getParentVO().setAttributeValue(targetItem.getKey(), refValue);
				}else{		
					vo = ui.getBufferData().getCurrentVO();
					if(vo!=null){
						//说明表体
						CircularlyAccessibleValueObject[] childvos = null;
						if(vo instanceof IExAggVO){
							childvos = ((IExAggVO)vo).getTableVO(tableCode);
						}else{
							childvos = vo.getChildrenVO();
						}
						
						if(row < CommonUtil.getArrayLength(childvos)){
							childvos[row].setAttributeValue(targetItem.getKey(), refValue);
						}
					}
				}
			}
		}
	
		
	}

	/**
	 * 作者：lisg <br>
	 * 日期：2006-12-19
	 * 
	 * @see nc.ui.fac.account.pub.IRefTakenProccessor#takenListRefValue(java.lang.String)
	 */
	public void takenListRefValue(String sourceItemKey, int row) {
		RefTakenVO vo = getRefTakenVO(sourceItemKey);
		if (vo != null) {
			BillModel bm = getBillModel(vo.getTabCode());
			if (bm != null) {
				BillItem sourceItem = bm.getItemByKey(vo.getSourceRefKey());
				BillItem targetItem = null;

				UIRefPane ref = ((UIRefPane) sourceItem.getComponent());

				String[] toBeRenderedKeys = vo.getToBeRenderedKeys();
				String[] refModelAttrKeys = vo.getRefModelAttrKeys();
				for (int j = 0; j < toBeRenderedKeys.length; j++) {
					targetItem = bm.getItemByKey(vo.getToBeRenderedKeys()[j]);
					
					if(targetItem == null){
						continue;
					}
					if(!isTakenInList(sourceItem, targetItem, true)){
						continue;
					}else{				
						Object renderedValue = ref.getRefValue(refModelAttrKeys[j]);
						Object value = null;
						if (targetItem.getDataType() == BillItem.COMBO) {
							UIComboBox com = (UIComboBox) targetItem.getComponent();
							value = com.getItemNameAt(com.getItemIndexByValue(renderedValue));
						}else{
							value = renderedValue;
						}
						
						if(value != null && value instanceof RefValueVO){
							value = ((RefValueVO)value).getOriginValue();
						}
						bm.setValueAt(value, row, vo.getToBeRenderedKeys()[j]);
						
						//如果为字符类型的数据放到缓存中，added by wes
//						if (targetItem.getDataType() == IBillItem.STRING
//								|| targetItem.getDataType() == IBillItem.STRING) {
//							fixSelfData(targetItem,value, row);// 加入到缓存当中
//						}
						
						if (targetItem.getDataType() == IBillItem.UFREF
								|| targetItem.getDataType() == IBillItem.USERDEF) {
							takenListRefValue(vo.getToBeRenderedKeys()[j],row);
						}
					}
				}
			}
		}

	}

	/**
	 * <p>
	 *列表编辑时携带(多页签编辑)
	 * <p>
	 * 作者：lpf <br>
	 * 日期：2008-04-16
	 * @param sourceItemKey
	 * @param row
	 */
	public void takenListRefValue(String tablecode,String sourceItemKey, int row){
		RefTakenVO vo = getRefTakenVO(tablecode,sourceItemKey);
		if (vo != null) {
			BillModel bm = getBillModel(vo.getTabCode());
			if (bm != null) {
				BillItem sourceItem = bm.getItemByKey(vo.getSourceRefKey());
				BillItem targetItem = null;

				UIRefPane ref = ((UIRefPane) sourceItem.getComponent());

				String[] toBeRenderedKeys = vo.getToBeRenderedKeys();
				String[] refModelAttrKeys = vo.getRefModelAttrKeys();
				for (int j = 0; j < toBeRenderedKeys.length; j++) {
					targetItem = bm.getItemByKey(vo.getToBeRenderedKeys()[j]);
					
					if(!isTakenInList(sourceItem, targetItem, true)){
						continue;
					}else{				
						Object renderedValue = ref.getRefValue(refModelAttrKeys[j]);
						Object value = null;
						if (targetItem.getDataType() == BillItem.COMBO) {
							UIComboBox com = (UIComboBox) targetItem.getComponent();
							value = com.getItemNameAt(com.getItemIndexByValue(renderedValue));
						}else{
							value = renderedValue;
						}
						
						if(value != null && value instanceof RefValueVO){
							value = ((RefValueVO)value).getOriginValue();
						}
						bm.setValueAt(value, row, vo.getToBeRenderedKeys()[j]);
						if (targetItem.getDataType() == IBillItem.UFREF
								|| targetItem.getDataType() == IBillItem.USERDEF) {
							takenListRefValue(vo.getToBeRenderedKeys()[j],row);
						}
					}
				}
			}
		}
	}
	
	public void resetDecimalDigits(AggregatedValueObject vo){
		if (getTakenColUtil() != null && vo !=null) {
			String[] currTabcodes = getCurrTabCode();
			if(currTabcodes != null && currTabcodes.length > 0){
				for(int i = 0;i < currTabcodes.length;i ++){
					if(getBillModel(currTabcodes[i],CARD)!=null){
						continue;
					}
					
					HashMap<String,CurrencyDecimalVO> currencyDecimalVOs = getCurrencyDecimalVO(currTabcodes[i]);
					Iterator<String> iterator = currencyDecimalVOs.keySet().iterator();
					while(iterator.hasNext()){
						String itemKey = iterator.next();
						CurrencyDecimalVO currencyDecimalVO = currencyDecimalVOs.get(itemKey);
						//设置ItemKey的千分位
						String[] moneyItemKeys = new String[]{itemKey};
						setHeadTailShowThMark(moneyItemKeys,true);
						int digit = 2;
						String pk_currency = (String)getValueByTakenInVO(currTabcodes[i], vo.getParentVO(), currencyDecimalVO.getCurrencyKey());
						if(pk_currency != null){						
							getCurrencyClientUtil().setPk_currtype_y(pk_currency);
							String moneyKeyType = currencyDecimalVO.getTypeByItemKey(itemKey);
							digit = getDecimalDigit(moneyKeyType);
							if(digit != -1){
								//直接从headItem取就可以了，这个时候携带已经完成
								BillItem item = getCardPanel().getHeadItem(itemKey);
								item.setDecimalDigits(digit);
							}
						}
					}
				}
			}
		}
	}

	public void takenRefValueForQuery() {
		if (getTakenColUtil() != null) {
			//处理携带
			RefTakenVOMap refTakenVOMap = getTakenColUtil().getRefTakenVOMap();			
			if (getMap().keySet() != null) {				
				String[] tabcodes = (String[]) getMap().keySet().toArray(new String[0]);
				for (int i = 0; tabcodes != null && i < tabcodes.length; i++) {	
					String[] tokens = praseTabCode(tabcodes[i]);
					String[] allKey = refTakenVOMap.getAllRefTakenKeys(tokens[0]);
					if (!CommonUtil.isNull(allKey)) {
						for (int j = 0; j < allKey.length; j++) {
							takenCardRefValue(allKey[j], false);
						}
					}
				}
			}
			
			cleanCache();
//			resetDecimalDigits();
		}

	}
	
	private int getDecimalDigit(String moneyKeyType){
		int digit = -1;
		
		try {
			if(moneyKeyType == null){
			}else if(moneyKeyType.equals(CurrencyDecimalVO.YB_ITEMKEY)){
				digit = getCurrencyClientUtil().getYDecimalDigit();
			}else if(moneyKeyType.equals(CurrencyDecimalVO.FB_ITEMKEY)){
				digit = getCurrencyClientUtil().getFBDecimalDigit()[0];
			}else if(moneyKeyType.equals(CurrencyDecimalVO.BB_ITEMKEY)){
				digit = getCurrencyClientUtil().getFBDecimalDigit()[1];
			}else if(moneyKeyType.equals(CurrencyDecimalVO.ZF_ITEMKEY)){
				digit = getCurrencyClientUtil().getExchangeRateDigit()[0];
			}else if(moneyKeyType.equals(CurrencyDecimalVO.ZB_ITEMKEY)){
				digit = getCurrencyClientUtil().getExchangeRateDigit()[1];
			}
		} catch (BusinessException e) {
			digit = -1;
			Logger.error(e.getMessage(),e);
		}
		
		return digit;
	}
	
	private int getMoneyValue(CurrencyDecimalVO currencyDecimalVO, String moneyKeyType){
		int digit = -1;
		
		try {
			if(moneyKeyType == null){
			}else if(moneyKeyType.equals(CurrencyDecimalVO.YB_ITEMKEY)){
				digit = getCurrencyClientUtil().getYDecimalDigit();
			}else if(moneyKeyType.equals(CurrencyDecimalVO.FB_ITEMKEY)){
				digit = getCurrencyClientUtil().getFBDecimalDigit()[0];
			}else if(moneyKeyType.equals(CurrencyDecimalVO.BB_ITEMKEY)){
				digit = getCurrencyClientUtil().getFBDecimalDigit()[1];
			}else if(moneyKeyType.equals(CurrencyDecimalVO.ZF_ITEMKEY)){
				digit = getCurrencyClientUtil().getExchangeRateDigit()[0];
			}else if(moneyKeyType.equals(CurrencyDecimalVO.ZB_ITEMKEY)){
				digit = getCurrencyClientUtil().getExchangeRateDigit()[1];
			}
		} catch (BusinessException e) {
			digit = -1;
			Logger.error(e.getMessage(),e);
		}
		
		return digit;
	}
	
	private void setHeadTailShowThMark(String[] itemKeys, boolean bShow) {
		if(itemKeys != null && itemKeys.length > 0){			
			BillItem[] items = new BillItem[itemKeys.length];
			for(int i = 0;i < itemKeys.length;i ++){
				items[i] = getCardPanel().getHeadTailItem(itemKeys[i]);				
			}
			getCardPanel().setHeadTailShowThMark(items, bShow);
		}
	}


	public ITakenColUtil getTakenColUtil() {
		if (takenColUtil == null) {
			takenColUtil = createTakenColUtil();
		}
		return takenColUtil;
	}

	protected abstract ITakenColUtil createTakenColUtil();

	/**
	 * @return the map
	 */
	private HashMap<String,String> getMap() {
		if (tabCodeRefMap == null) {
			tabCodeRefMap = new HashMap<String,String>();
		}
		return tabCodeRefMap;
	}

	/**
	 * @return the listPanel
	 */
	private BillListPanel getListPanel() {
		return listPanel;
	}

	/**
	 * @return the cardPanel
	 */
	private BillCardPanel getCardPanel() {
		return cardPanel;
	}
	
	private HashMap<String,CurrencyDecimalVO> getCurrencyDecimalVO(String tabCode){
		if(getTakenColUtil().getTabCodeCurrMap().containsKey(tabCode)){
			return getTakenColUtil().getTabCodeCurrMap().get(tabCode);
		}
		
		return null;
	}
	
	private String[] getCurrTabCode(){
		return (String[])getTakenColUtil().getTabCodeCurrMap().keySet().toArray(new String[0]);
	}
	
	
	
	
	
	/**
	 * 作者：yjfeng 
	 * <br>
	 * 列表单元格渲染处理
	 * 日期：2006-12-27
	 *
	 * @see nc.ui.fac.account.pub.IRefTakenProccessor#renderCell(java.lang.Object, int, int, nc.ui.pub.bill.BillItem, nc.ui.pub.bill.BillModel)
	 */
	public Object renderCell(Object value, int row, int col,BillItem billItem,BillModel listModel){

		Object renderedValue = null;
		if(value == null){
			String tablecode = billItem.getTableCode();
			ReverseRefTakenVO reverseRefTakenVO = getReverseRefTakenVO(tablecode,billItem.getKey());
			if (reverseRefTakenVO == null) {
				return value;
			}
			BillItem sourceRefItem = listModel.getItemByKey(reverseRefTakenVO.getSource());
			
			if(!isTakenInList(sourceRefItem, billItem, false)){
				return value;
			}
			
			Object sourcevalue = getValueByTakenInList(reverseRefTakenVO.getTabCode(), reverseRefTakenVO.getSource(), row);
			if ((sourceRefItem.getDataType() == BillItem.UFREF 
					|| sourceRefItem.getDataType() == BillItem.USERDEF)
					&& sourcevalue != null) {
				boolean isenable=sourceRefItem.isEnabled();
				((UIRefPane)sourceRefItem.getComponent()).setEnabled(false);
				// 创建key
				String key = buildKey(sourcevalue, reverseRefTakenVO.getTarget());
				if (getCacheMap().containsKey(key)) {
				}else{
					getAndCache(billItem,listModel,reverseRefTakenVO.getSource(),reverseRefTakenVO.getTarget(), row);
				}
				renderedValue = getCacheMap().get(key);	
				
				//针对携带的bufferData的问题，进行解决
				if(( renderedValue!=null 
						&& (renderedValue instanceof String?!CommonUtil.isNull((String)renderedValue):true)) 
						&& setChangeTable(listModel,false)){
					listModel.setValueAt(renderedValue, row, billItem.getKey());
					if (billItem.getDataType() == BillItem.UFREF || billItem.getDataType() == BillItem.USERDEF){
						value = renderedValue;
					} else {
						value = billItem.converType(renderedValue);
					}
					fixSelfData(billItem, value, row);
					setChangeTable(listModel,true);
				}
				
				if (billItem.getDataType() == BillItem.COMBO) {
					UIComboBox com = (UIComboBox) billItem.getComponent();
					value = com.getItemNameAt(com.getItemIndexByValue(renderedValue));
				}
				
				((UIRefPane)sourceRefItem.getComponent()).setEnabled(isenable);
			}
		}

		return value;
	}

	/**
	 * 返回值表示是否设置成功
	 * @param listModel
	 * @param b
	 * @return
	 */
	private boolean setChangeTable(BillModel listModel, boolean b){
		boolean ret = false;
		try {
			Method m = listModel.getClass().getDeclaredMethod("setChangeTable", boolean.class);
			m.setAccessible(true);
			m.invoke(listModel, b);
			ret = true;
		} catch (Exception e) {
			Logger.error(e.getMessage(),e);
			ret = false;
		} 
		
		return ret;
	}	
	
	
	/**
	 * <p>
	 *从参照取值并缓存
	 * <p>
	 * 作者：Administrator <br>
	 * 日期：2006-12-27
	 * @param billItem
	 * @param listModel
	 * @param sourceKey
	 * @param targetKey
	 */
	private void getAndCache(BillItem billItem,BillModel listModel,String sourceKey,String targetKey, int rowindex){
		switch(getCacheType()){
			case CACHE_BATCH:
				batchGetAndCache(billItem, listModel, sourceKey, targetKey);
				break;
			case CACHE_CELL:
				cellGetAndCache(billItem, listModel, sourceKey, targetKey, rowindex);
				break;
			default:
				rowGetAndCache(billItem, listModel, sourceKey, targetKey, rowindex);
		}
	}

	private void rowGetAndCache(BillItem billItem, BillModel listModel,
			String sourceKey, String targetKey, int rowindex) {
		BillItem sourceRefItem = listModel.getItemByKey(sourceKey);
		UIRefPane sourceRefPane = ((UIRefPane) sourceRefItem.getComponent());
		String tableCode = billItem.getTableCode();
		String pk = (String) getValueByTakenInList(tableCode, sourceKey, rowindex);
		if(pk!=null){
			String tempkey = buildKey(pk, targetKey);
			if(getCacheMap().containsKey(tempkey)){
				/**
				 * 需要立即得到的在其中就不再进行其他的渲染，
				 * 如果本身不在缓存中，除渲染本身外也将其他的顺便渲染
				 */
			}else{
				RefTakenVO refTakenVO = getRefTakenVO(tableCode,sourceKey);
				
				ArrayList<String> pkList = new ArrayList<String>();
				pkList.add(pk);
				int maxIndex = ((rowindex + cacheRowCount) > listModel.getRowCount())?listModel.getRowCount():(rowindex + cacheRowCount);
				for (int i = rowindex+1; i < maxIndex; i++) {
					pk = (String) getValueByTakenInList(tableCode, sourceKey, i);
					if(pk!=null){
						tempkey = buildKey(pk, targetKey);
						if(getCacheMap().containsKey(tempkey)) {
							/**
							 * 已经存在的不需要进行渲染
							 */
						}else{
							pkList.add(pk);							
						}
					}
				}
				sourceRefPane.setPKs(pkList.toArray(new String[0]));
				Vector selData = sourceRefPane.getSelectedData();
				if(selData!=null){
					HashMap selMap = new HashMap();
					final AbstractRefModel sourceRefModel = sourceRefPane.getRefModel();
					for (Iterator iter = selData.iterator(); iter.hasNext();) {
						Vector element = (Vector) iter.next();
						final int PKIndex = sourceRefModel.getFieldIndex(sourceRefModel.getPkFieldCode());
						selMap.put(element.elementAt(PKIndex), element);
					}
					
					final String[] refModelAttrKeys = refTakenVO.getRefModelAttrKeys();
					final String[] reftobeRenderedKeys = refTakenVO.getToBeRenderedKeys();
					
					for (int columnIndex = 0; columnIndex < refModelAttrKeys.length; ++columnIndex) {
						int refIndex = sourceRefModel.getFieldIndex(refModelAttrKeys[columnIndex]);
						
						//add by ouyangzhb 2012-03-03 如果对应的字段不存在，refIndex 为-1 时，会报错
						if(refIndex<0){
							continue ;
						}
						//add by ouyangzhb 2012-03-03
						
						for (int rowIndex = 0; rowIndex < pkList.size(); rowIndex++) {
							Vector element = (Vector)selMap.get(pkList.get(rowIndex));
							String valuekey = buildKey(pkList.get(rowIndex), refTakenVO.getToBeRenderedKeys()[columnIndex]);
							if (element == null || element.size() < refIndex) {
								getCacheMap().put(valuekey, null);
							} else {
								Object result = element.elementAt(refIndex);
								BillItem tobeRenderedbillitem = getBillModel(tableCode).getItemByKey(reftobeRenderedKeys[columnIndex]);
								if(tobeRenderedbillitem == null){
									continue;
								}
								
								putInCacheMap(valuekey,result);
							}
						}
					}	
				}
			}
		}
	}

	private void cellGetAndCache(BillItem billItem, BillModel listModel,
			String sourceKey, String targetKey, int rowindex) {
		BillItem sourceRefItem = listModel.getItemByKey(sourceKey);
		UIRefPane sourceRefPane = ((UIRefPane) sourceRefItem.getComponent());
		String pk = (String) getValueByTakenInList(billItem.getTableCode(), sourceKey, rowindex);
		if(pk!=null){
			String tempkey = buildKey(pk, targetKey);
			if(!getCacheMap().containsKey(tempkey)) {			
				ReverseRefTakenVO rrvo = getReverseRefTakenVO(targetKey);
				if(rrvo!=null){
					sourceRefPane.setPK(pk);
					Object value = sourceRefPane.getRefValue(rrvo.getRefFeild());
					putInCacheMap(tempkey, value);
				}
			}
		}
	}

	/**
	 * 从参照取值并缓存
	 * 批执行
	 * @param billItem
	 * @param listModel
	 * @param sourceKey
	 * @param targetKey
	 */
	private void batchGetAndCache(BillItem billItem, BillModel listModel,
			String sourceKey, String targetKey) {
		int totalCount = listModel.getRowCount();
		int times = totalCount/maxCount + (totalCount - (totalCount/maxCount)*maxCount)==0?0:1;
		
		for(int roll = 0; roll < times; roll++){
			int beginindex = roll * maxCount;
			int endindex = (roll == (times - 1))?totalCount:(maxCount + beginindex);
			
			ArrayList<String> pkList = new ArrayList<String>();
			for (int i = beginindex; i < endindex; i++) {
				String temppk = (String) getValueByTakenInList(billItem.getTableCode(), sourceKey, i);
//			String temppk = (String) listModel.getValueAt(i,sourceKey);
				if (temppk != null) {
					String tempkey = buildKey(temppk, targetKey);
					if (!getCacheMap().containsKey(tempkey)) {
						pkList.add(temppk);
					}
				}
			}
			BillItem sourceRefItem = listModel.getItemByKey(sourceKey);
			UIRefPane sourceRefPane = ((UIRefPane) sourceRefItem.getComponent());
			String[] pks = new String[pkList.size()];
			pkList.toArray(pks);
			sourceRefPane.setPKs(pks);
			// 维护缓存
			
			
			RefTakenVO refTakenVO = getRefTakenVO(sourceKey);
			
			final String[] refModelAttrKeys = refTakenVO.getRefModelAttrKeys();
			final String[] reftobeRenderedKeys = refTakenVO.getToBeRenderedKeys();
			
			Vector selData = sourceRefPane.getSelectedData();
			
			if(selData!=null){
				HashMap selMap = new HashMap();
				final AbstractRefModel sourceRefModel = sourceRefPane.getRefModel();
				for (Iterator iter = selData.iterator(); iter.hasNext();) {
					Vector element = (Vector) iter.next();
					final int PKIndex = sourceRefModel.getFieldIndex(sourceRefModel.getPkFieldCode());
					selMap.put(element.elementAt(PKIndex), element);
				}
				
				
				for (int columnIndex = 0; columnIndex < refModelAttrKeys.length; columnIndex++) {
					int refIndex = sourceRefModel.getFieldIndex(refModelAttrKeys[columnIndex]);
					for (int rowIndex = 0; rowIndex < pks.length; rowIndex++) {
						Vector element = (Vector)selMap.get(pks[rowIndex]);
						String valuekey = buildKey(pks[rowIndex], refTakenVO.getToBeRenderedKeys()[columnIndex]);
						if (element == null || element.size() < refIndex) {
							putInCacheMap(valuekey, null);
						} else {
							Object result = element.elementAt(refIndex);
							BillItem tobeRenderedbillitem = getBillModel(billItem.getTableCode()).getItemByKey(reftobeRenderedKeys[columnIndex]);
							if(tobeRenderedbillitem == null){
								continue;
							}
							
							putInCacheMap(valuekey, result);
						}
					}
				}
			}
		}
	}

	/**
	 * @param billItem
	 * @param listModel
	 * @param curvo
	 * @param rowIndex
	 * @return
	 */
	private Object getPKCurrency(BillItem billItem, BillModel listModel, int rowIndex) {
		HashMap<String, CurrencyDecimalVO> curvos = getCurrencyDecimalVO(billItem.getTableCode());
		if(curvos==null 
				|| !curvos.containsKey(billItem.getKey())){
			return null;
		}

		CurrencyDecimalVO curvo = curvos.get(billItem.getKey());
		Object pk_curreny = getOrgVauleFromBillModel(listModel,rowIndex, curvo.getCurrencyKey());
		ReverseRefTakenVO rvo = getReverseRefTakenVO(curvo.getCurrencyKey());
		if(pk_curreny == null && rvo != null){							
			String reverseSourceKey = rvo.getSource();
			Object sourcePk = getOrgVauleFromBillModel(listModel, rowIndex, reverseSourceKey);
			String currencyCacheKey = buildKey(sourcePk, curvo.getCurrencyKey());
			if(!getCacheMap().containsKey(currencyCacheKey)){
				getAndCache(billItem,listModel,rvo.getSource(),rvo.getTarget(), rowIndex);
			}
			pk_curreny = getCacheMap().get(currencyCacheKey);
		}
		
		return pk_curreny;
	}

		
	private Object setCurrencyDecimal(BillItem billitem, Object result, String currencyKey) {
		UFDouble resultd = MathUtil.ZERO_UFDOUBLE;
		HashMap<String,CurrencyDecimalVO> currencyDecimalVOs = getCurrencyDecimalVO(billitem.getTableCode());
		if(result != null 
				&& currencyKey != null
				&& currencyDecimalVOs!=null
				&& currencyDecimalVOs.containsKey(billitem.getKey())){
			getCurrencyClientUtil().setPk_currtype_y(currencyKey);
			CurrencyDecimalVO currencyDecimalVO = currencyDecimalVOs.get(billitem.getKey());
			String moneyKeyType = currencyDecimalVO.getTypeByItemKey(billitem.getKey());
			int decimal = getDecimalDigit(moneyKeyType);
			if(decimal != -1){
				resultd = new UFDouble(result.toString());
				resultd = ((UFDouble) resultd).setScale(0 - decimal, UFDouble.ROUND_HALF_UP);
			}
		}
		return resultd;
	}
	
	private Object setCurrencyDecimal(String tabCode, String itemKey, Object result, String currencyKey) {
		UFDouble resultd = MathUtil.ZERO_UFDOUBLE;
		HashMap<String,CurrencyDecimalVO> currencyDecimalVOs = getCurrencyDecimalVO(tabCode);
		if(result != null 
				&& currencyKey != null
				&& currencyDecimalVOs!=null
				&& currencyDecimalVOs.containsKey(itemKey)){
			getCurrencyClientUtil().setPk_currtype_y(currencyKey);
			CurrencyDecimalVO currencyDecimalVO = currencyDecimalVOs.get(itemKey);
			String moneyKeyType = currencyDecimalVO.getTypeByItemKey(itemKey);
			int decimal = getDecimalDigit(moneyKeyType);
			if(decimal != -1){
				resultd = new UFDouble(result.toString());
				resultd = ((UFDouble) resultd).setScale(0 - decimal, UFDouble.ROUND_HALF_UP);
			}
		}
		return resultd;
	}
	
    private int getDecimalFromSource(Object pkValue) {
        CurrtypeVO vo = (CurrtypeVO) getCurrtype().getCurrtypeVO(pkValue.toString());
        if (vo != null) {
            return vo.getCurrdigit().intValue();
        }
        return -1;
    }

	private CurrtypeQuery getCurrtype() {
        if (m_currtype == null)
            m_currtype = CurrtypeQuery.getInstance();
        return m_currtype;
	}

	
	protected String buildKey(Object o, String s) {
		String key = biuldHashKey(o);

		return o == null ? key : (key + "&" + s);
	}
	
	private String biuldHashKey(Object str){
		/*
		 * 为缓存够造键值
		 */
		String strr = null;
		String key = "&&NULL";
		if(str!=null) {
			strr=str.toString();
		}
		if (strr != null){
			if (strr.length() == 0){
				key = "&&ZERO";
			} else {
				key = strr;
			}
			
		}
		return key;
	}

	public Object getValueByTakenInCard(String itemKey) {
		if(getCardPanel()!=null){
			BillItem targetItem = getCardPanel().getHeadItem(itemKey);
			if(targetItem == null){
				return null;
			}
			if(targetItem.getValueObject()!=null){
				return targetItem.getValueObject();
			}
			
			if(getCardVO() != null
					&& getCardVO().getParentVO()!=null
					&& getCardVO().getParentVO().getAttributeValue(itemKey)!=null){
				return getCardVO().getParentVO().getAttributeValue(itemKey);
			}
			
			ReverseRefTakenVO reverseRefTakenVO = getReverseRefTakenVO(itemKey);
			if (reverseRefTakenVO != null) {
				//需要进行携带
				BillItem sourceItem = getCardPanel().getHeadItem(
						reverseRefTakenVO.getSource());
				
				if(sourceItem!=null){
					UIRefPane pane = (UIRefPane)sourceItem.getComponent();
					if(!pane.isAutoCheck() 
							&& sourceItem.getValueObject() == null 
							&& !CommonUtil.isNull(pane.getText())){
						return null;
					}
					if(sourceItem.getValueObject() == null){
						Object sourcevalue = getValueByTakenInCard(sourceItem.getKey());
						((UIRefPane)sourceItem.getComponent()).setPK(sourcevalue);
					}
					
					Object ret = ((UIRefPane)sourceItem.getComponent()).getRefValue(reverseRefTakenVO.getRefFeild());
					if(ret != null && ret instanceof RefValueVO){
						return ((RefValueVO)ret).getOriginValue();
					}
				}
			}
		}
		return null;
	}
	
	public Object getValueByTakenInVO(String tabCode, CircularlyAccessibleValueObject sourcevo, String attname){
		BillModel bm = getBillModel(tabCode);
		Object targetValue = null;
		if(bm!=null&&sourcevo!=null&&attname!=null){
			targetValue = sourcevo.getAttributeValue(attname);
			if(targetValue == null){
				ReverseRefTakenVO vo = getReverseRefTakenVO(attname);
				if(vo!=null){
					Object sourceValue = sourcevo.getAttributeValue(vo.getSource());
					if(sourceValue!=null){
						BillItem sourceItem = bm.getItemByKey(vo.getSource());
						String tempkey = buildKey(sourceValue, attname);
						
						if (getCacheMap().containsKey(tempkey)) {
							targetValue = getCacheMap().get(tempkey);
						}else{
							try{
								if(sourceItem.getDataType() == IBillItem.UFREF
										|| sourceItem.getDataType() == IBillItem.USERDEF){
									UIRefPane refpane = new UIRefPane();
									AbstractRefModel refmodel = (AbstractRefModel)((UIRefPane)sourceItem.getComponent()).getRefModel().getClass().newInstance();
									refpane.setRefModel(refmodel);
									refpane.setPK(sourceValue);
									putInCacheMap(tempkey, refpane.getRefValue(vo.getRefFeild()));
									targetValue = getCacheMap().get(tempkey);
								}
							}catch(Exception e){
							}
						}
					}
				}
			}
		}
		
		return targetValue;
	}
	
	public Object getValueByTakenInVector(String tabCode, Vector sourceVector, String attname){
		BillModel bm = getBillModel(tabCode);
		Object targetValue = null;
		if(bm!=null&&sourceVector!=null&&attname!=null){
			int targetIndex = bm.getBodyColByKey(attname);
			if(targetIndex!=-1){
				targetValue = sourceVector.get(targetIndex);
				if(targetValue == null){
					ReverseRefTakenVO vo = getReverseRefTakenVO(attname);
					if(vo!=null){
						int sourceIndex = bm.getBodyColByKey(vo.getSource());
						if(sourceIndex!=-1){
							Object sourceValue = sourceVector.get(sourceIndex);
							if(sourceValue!=null){
								BillItem sourceItem = bm.getItemByKey(vo.getSource());
								String tempkey = buildKey(sourceValue, attname);
								
								if (getCacheMap().containsKey(tempkey)) {
									targetValue = getCacheMap().get(tempkey);
								}else{
									try{
										if(sourceItem.getDataType() == IBillItem.UFREF
												|| sourceItem.getDataType() == IBillItem.USERDEF){
											UIRefPane refpane = new UIRefPane();
											AbstractRefModel refmodel = (AbstractRefModel)((UIRefPane)sourceItem.getComponent()).getRefModel().getClass().newInstance();
											refpane.setRefModel(refmodel);
											refpane.setPK(sourceValue);
											putInCacheMap(tempkey, refpane.getRefValue(vo.getRefFeild()));
											targetValue = getCacheMap().get(tempkey);
										}
									}catch(Exception e){
									}
								}
							}
						}
					}
				}
			}
		}
		
		return targetValue;
	}

	public Object getValueByTakenInList(String tabCode, String itemKey, int rowno) {
//		if(getListPanel()!=null){
			BillModel bm = getBillModel(tabCode);
			
			if(bm != null){
				Object targetValue = getOrgVauleFromBillModel(bm, rowno, itemKey);
				//xwq 2009.5.12 票据表体删行後增行将原来的数据携带出来了，原因是由于render时调用了此方法，如果当前被渲染列是被携带字段，为空时取buffer数据，这样是不对的
				//正确的做法是：无论何种情况下均从billmodel取值。此处修改可能引起其他模块问题，需要仔细测试
//				if(targetValue == null ){
//					targetValue = getValueInBuffer(tabCode, itemKey, rowno);
//				}
				if(targetValue != null && targetValue.toString().trim().length()!=0){
					BillItem item = bm.getItemByKey(itemKey);
					if(item.getDataType() == IBillItem.COMBO){
						targetValue = ((UIComboBox)item.getComponent()).getItemIndexByObject(targetValue);
						if(!item.isWithIndex()){
							targetValue = ((UIComboBox)item.getComponent()).getItemValueAt((Integer)targetValue);
						}
					}
					return targetValue;
				}
				
				ReverseRefTakenVO vo = getReverseRefTakenVO(itemKey);
				if (vo != null) {
	
					BillItem sourceItem = bm.getItemByKey(vo.getSource());
						
					if(sourceItem != null){
						Object sourceValue = getOrgVauleFromBillModel(bm, rowno, sourceItem.getKey());
						
						if(sourceValue == null){
							sourceValue = getValueInBuffer(tabCode, sourceItem.getKey(), rowno);
						}
						
						if(sourceValue == null){
							sourceValue = getValueByTakenInList(tabCode, sourceItem.getKey(), rowno);
						}
						
						String tempkey = buildKey(sourceValue, itemKey);
						
						if (getCacheMap().containsKey(tempkey)) {
							return getCacheMap().get(tempkey);
						}
						try{
							if(sourceItem.getDataType() == IBillItem.UFREF
									|| sourceItem.getDataType() == IBillItem.USERDEF){
								UIRefPane refpane = (UIRefPane)sourceItem.getComponent();
//								AbstractRefModel refmodel = (AbstractRefModel)((UIRefPane)sourceItem.getComponent()).getRefModel().getClass().newInstance();
//								refpane.setRefModel(refmodel);
								refpane.setPK(sourceValue);
								Object targetvalue = refpane.getRefValue(vo.getRefFeild());
								
								//此处格式化精度
								BillItem currItem = bm.getItemByKey(itemKey);
//								currItem.setValue(targetvalue);
								if(currItem.getDataType() == BillItem.DECIMAL){
									targetvalue = currItem.converType(targetvalue);
								}
//								targetvalue = currItem.converType(currItem.getValueObject());
								
								putInCacheMap(tempkey, targetvalue);
								return getCacheMap().get(tempkey);
							}
						}catch(Exception e){
							return null;
						}
					}
				}
			}
//		}

		return null;
	}
	
	private Object getValueInBuffer(String tabCode, String itemKey, int rowno){
		Object ret = null;
		try{
			BillModel bm = getBillModel(tabCode);
			BillItem bi = bm.getItemByKey(itemKey);
			if(bi!=null){
				String detail = getMap().get(tabCode+"_"+getPosition());
				if(HEAD.equals(detail)){
					AggregatedValueObject vo = getAbstractUI().getBufferData().getVOByRowNo(rowno);
					ret = getValueByTakenInVO(tabCode, vo.getParentVO(), itemKey);
				}else{
					AggregatedValueObject vo = getAbstractUI().getBufferData().getCurrentVO();
					if (IBillOperate.OP_ADD == getAbstractUI().getBillOperate()){
						return ret;
					}
					if(vo instanceof IExAggVO){
						ret = ((IExAggVO)vo).getTableVO(tabCode)[rowno].getAttributeValue(itemKey);
					}else{
						ret = getValueByTakenInVO(tabCode,vo.getChildrenVO()[rowno],itemKey);
					}
				}
			}
		}catch(Exception e){
		}
		
		return ret;
	}

	
	private String[] praseTabCode(String tabcode){
		String[] ret = new String[2];
		
		ret[0] = tabcode.substring(0, tabcode.lastIndexOf('_'));
		ret[1] = tabcode.substring(tabcode.lastIndexOf('_')+1);
		
		return ret;
		
	}

	private CurrencyClientUtil getCurrencyClientUtil() {
		return currencyClientUtil;
	}
	
	public void cleanCache(){
		getCacheMap().clear();
	}

	public void setCacheType(int cacheType) {
		this.cacheType = cacheType;
	}
	
	private int getCacheType(){
		return this.cacheType;
	}
	
	private void putInCacheMap(String key, final Object value){
		Object newvalue = null;
		if(value!=null && value instanceof RefValueVO){
			newvalue = ((RefValueVO)value).getOriginValue();
		}else{
			newvalue = value;
		}
		getCacheMap().put(key, newvalue);
	}
	
	private Object getOrgVauleFromBillModel(BillModel bm, int rowIndex, String strKey){
		Object ret = null;
		if(bm!=null){
			ret = bm.getValueAt(rowIndex, strKey);
		}
		
		while(ret!=null && ret instanceof RefValueVO){
			ret = ((RefValueVO)ret).getOriginValue();
		}
		
		return ret;
	}
	
	public AggregatedValueObject getCardVO(){
		return cardVo;
	}
	
	public void setCardVO(AggregatedValueObject cardvo){
		cardVo = cardvo;
	}

}
