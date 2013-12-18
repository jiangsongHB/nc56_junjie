package nc.ui.sourceref.arap;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.pf.AbstractReferQueryUI;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.querytemplate.IBillReferQuery;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 应付单的参照显示UI
 * 
 * @author 付世超 2010-10-28
 * 
 */
public class SourceBillDlgF1To25 extends BillSourceDLG implements ActionListener,
BillEditListener, BillTableMouseListener, ListSelectionListener{

	protected BillListPanel ivjbillListPanel = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjbtnCancel = null;

	private UIButton ivjbtnOk = null;

	private UIButton ivjbtnQuery = null;

	private UIPanel ivjPanlCmd = null;

	//单据vo,主表vo,子表vo
	protected String m_billVo = null;

	protected String m_billHeadVo = null;

	protected String m_billBodyVo = null;

	//查询条件语句
	protected String m_whereStr = null;

	//返回的集合Vo
	protected AggregatedValueObject retBillVo = null;

	//返回集合VO数组
	protected AggregatedValueObject[] retBillVos = null;

	protected boolean isRelationCorp = true;

	//主计量数量小数位数
	protected Integer m_BD501 = null;

	//辅计量数量小数位数
	protected Integer m_BD502 = null;

	//单价小数位数
	protected Integer m_BD505 = null;

	//换算率小数位数
	protected Integer m_BD503 = null;

	private class HeadRowStateListener implements IBillModelRowStateChangeEventListener {

		public void valueChanged(RowStateChangeEvent e) {
			if (e.getRow() != getbillListPanel().getHeadTable().getSelectedRow()) {
				headRowChange(e.getRow());
			}

			BillModel model = getbillListPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();

			if (e.isSelectState()) {
				getbillListPanel().getChildListPanel().selectAllTableRow();
			} else {
				getbillListPanel().getChildListPanel().cancelSelectAllTableRow();
			}
			model.addRowStateChangeEventListener(l);

			getbillListPanel().updateUI();
		}

	}

	/**
	 * 根据类名称，where语句构造参照界面
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param parent
	 */
	public SourceBillDlgF1To25(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, parent);
		m_whereStr = getQueryWhere();
		initialize();
	}

	/**
	 * 根据类名称，where语句构造参照界面
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 */
	public SourceBillDlgF1To25(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, nodeKey, userObj, parent);
		m_whereStr = getQueryWhere();
		
		initialize();
	}

	/* (non-Javadoc)
	 * @see event.ActionListener#actionPerformed(event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getbtnOk()) {
			onOk();
		} else if (e.getSource() == getbtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getbtnQuery()) {
			onQuery();
		}
	}

	/**
	 * 增加单据模版
	 * <li>该方法被PfUtilClient.childButtonClicked()调用
	 */
	public void addBillUI() {
		//增加模版调用
		getUIDialogContentPane().add(getbillListPanel(), BorderLayout.CENTER);
		//增加对控件监听
		addListenerEvent();
	}

	/**
	 * 事件侦听
	 */
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getbtnQuery().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);
		getbillListPanel().getHeadBillModel()
				.addRowStateChangeEventListener(new HeadRowStateListener());

		//表头列表 行切换事件处理器
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	/* (non-Javadoc)
	 * @see BillEditListener#afterEdit(BillEditEvent)
	 */
	public void afterEdit(BillEditEvent e) {
	}

	/**
	 * 只对表头进行处理
	 * <li>行切换 事件
	 * <li>双击 事件
	 * <li>WARN::行切换事件发生在双击事件之前
	 * @param iNewRow
	 */
	private synchronized void headRowChange(int iNewRow) {
		if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow, getpkField()) != null) {
			if (!getbillListPanel().setBodyModelData(iNewRow)) {
				//1.初次载入表体数据
				loadBodyData(iNewRow);
				//2.备份到模型中
				getbillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getbillListPanel().repaint();
	}

	/* (non-Javadoc)
	 * @see BillEditListener#bodyRowChange(BillEditEvent)
	 */
	public void bodyRowChange(BillEditEvent e) {
	}

	/**
	 * @return
	 */
	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				//获的显示位数值
				//装载模板
				nc.vo.pub.bill.BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(getBillType(), null,
				/*getBusinessType(),*/getOperator(), getPkCorp(), getNodeKey());

				BillListData billDataVo = new BillListData(vo);

				//更改主表的显示位数
				String[][] tmpAry = getHeadShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				//更改子表的显示位数
				tmpAry = getBodyShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				ivjbillListPanel.setListData(billDataVo);

				//进行主子隐藏列的判断
				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}

				ivjbillListPanel.setMultiSelect(true);
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	/**
	 * 获得单据类型VO类信息
	 *
	 * <li>数组[0]=单据聚合Vo;数组[1]=单据主表Vo;数组[2]=单据子表Vo;
	 */
	public void getBillVO() {
		try {
			//0--单据vo;1-主表Vo;2-子表Vo;
			m_billVo = "nc.vo.ep.dj.DJZBVO";
			m_billHeadVo = "nc.vo.ep.dj.DJZBHeaderVO";
			m_billBodyVo = "nc.vo.ep.dj.DJZBItemVO";
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 子表条件语句
	 * @return
	 */
	public String getBodyCondition() {
		
		String strBodyConditon = "  and abs(nvl(arap_djfb.ntotalinvoicenumber,0))< abs(nvl(arap_djfb.dfshl,0)) and arap_djfb.isverifyfinished = 'N' "; // + 
				//"  and exists (select 1 from bd_invbasdoc where pk_invbasdoc = arap_djfb.cinventoryid  and laborflag = 'Y' )" ;
		IBillReferQuery queryCondition = getQueyDlg();
		String qryWhere = queryCondition.getWhereSQL();
		//String tmpWhere = null;
		if (qryWhere.contains("fb.hbbm")) {
			String tmpWhere = qryWhere.replaceAll("fb.", "arap_djfb.");
			tmpWhere=tmpWhere.substring(tmpWhere.indexOf("arap_djfb.hbbm"));
			if (tmpWhere.lastIndexOf(")") == tmpWhere.length()-1 )
				tmpWhere = tmpWhere.substring(0, tmpWhere.length()-1);
			String strSplit[] = tmpWhere.split(" ");
			tmpWhere = " and ( ";
			for (int i = 0 ; i< 3; i++){
				tmpWhere = tmpWhere.concat(strSplit[i]);
			}
			tmpWhere = tmpWhere.concat(" ) ");
			strBodyConditon = strBodyConditon.concat(tmpWhere);
			//strBodyConditon.concat(tmpWhere.replaceAll("fb.", "arap_djfb."));
		}
		
		return strBodyConditon ;
		

		//return null;
	}

	/**
	 * 子表隐藏字段
	 * @return
	 */
	public String[] getBodyHideCol() {
		return null;
	}

	/**
	 * 返回子表二维数组，第一维为必须为2。第二维不限，
	 * 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}}
	 * 注意:必须保证二行的长度相等。否则系统按默认值2取。
	 * 创建日期：(2001-12-25 10:19:03)
	 * @return java.lang.String[][]
	 */
	public String[][] getBodyShowNum() {
		return null;
	}

	/**
	 * 取消
	 * @return
	 */
	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);
		}
		return ivjbtnCancel;
	}

	/**
	 * 确定
	 * @return
	 */
	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "确定"*/);
		}
		return ivjbtnOk;
	}

	/**
	 * 查询
	 * @return
	 */
	private UIButton getbtnQuery() {
		if (ivjbtnQuery == null) {

			ivjbtnQuery = new UIButton();
			ivjbtnQuery.setName("btnQuery");
			ivjbtnQuery
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*@res "查询"*/);
		}
		return ivjbtnQuery;
	}

	/**
	 * 主表查询条件
	 * @return
	 */
	public String getHeadCondition() {
		return "  (zb.zyx19 is null or zb.zyx19<>'Y')  and zb.zgyf = '1' and zb.sxbz = 10 and zb.dr=0 ";
	}

	/**
	 * 主表隐藏字段
	 * @return
	 */
	public String[] getHeadHideCol() {
		return null;
	}

	/**
	 * 返回主表二维数组，第一维为必须为2。第二维不限，
	 * 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}}
	 * 注意:必须保证二行的长度相等。否则系统按默认值2取。
	 * 创建日期：(2001-12-25 10:19:03)
	 * @return java.lang.String[][]
	 */
	public String[][] getHeadShowNum() {
		return null;
	}

	/**
	 * 返回是否与业务类型有关，
	 * 如果与业务类型无关，则系统不进行业务类型条件的拼接
	 * 注意:必须明确保证是否与业务类型的关系
	 * @return
	 */
	public boolean getIsBusinessType() {
		return true;
	}

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
			ivjPanlCmd.add(getbtnQuery(), getbtnQuery().getName());
		}
		return ivjPanlCmd;
	}

	public AggregatedValueObject getRetVo() {
		return retBillVo;
	}

	public AggregatedValueObject[] getRetVos() {
		return retBillVos;
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			//2003-05-12平台进行显示调用
			//getUIDialogContentPane().add(getbillListPanel(), "Center");
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	private void initialize() {

		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000114")/*@res "单据的参照界面"*/);
		setContentPane(getUIDialogContentPane());
		setTitle(getTitle());
		//获取单据对应的单据vo名称
		getBillVO();
	}

	 public String getTitle() {
	        String sret = null;
	        if(getCurrentBillType()!=null && getBillType()!=null){
	          sret = NCLangRes.getInstance().getStrByID("billtype", "D" + getBillType().trim());
	          sret += " " + NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000442") + " "/* @res "生成"*/;
	          sret += nc.ui.ml.NCLangRes.getInstance().getStrByID("billtype", "D" + getCurrentBillType().trim());
	        }else{
	          sret = NCLangRes.getInstance().getStrByID("102220", "UPP102220-000114");/* @res "单据的参照界面" */
	        }      
	        return sret;
	      }
	/**
	 * 根据主表获取子表数据
	 * @param row 选中的表头行
	 */
	public void loadBodyData(int row) {
		try {
			//获得主表ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()).toString();
			//查询子表VO数组
			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData(getBillType(),
					id, getBodyCondition());
			
			//2010-11-22 MeiChao begin 对应付单表体行费用信息的过滤改造
			//防止对其他业务流程产生影响,因此也在此过滤处加一个判断
			DJZBItemVO[] newtmpBodyVo = null;
			if(this.getCurrentBillType()!=null&&this.getCurrentBillType().equals("25")){
			//add by 付世超  2010-10-27 加入对非费用暂估的过滤 begin
		    ArrayList<DJZBItemVO> tempExpenseVoList = new ArrayList<DJZBItemVO>();
		    for (int j = 0; j < tmpBodyVo.length; j++) {
		    	DJZBItemVO tmpBVo = (DJZBItemVO) tmpBodyVo[j];
//		    	if(tmpBVo.getZyx18()!=null&&tmpBVo.getZyx18().equals("tureFree")){
//		    		tempFreeVoList.add(tmpBVo);
//		    	}
		    	String invbasid=tmpBVo.getCinventoryid();
		    	Object o=this.execQuery(invbasid);
		    	if(o!=null&&(((String)o).trim().toUpperCase()).equals("Y")){//判断存货的应税劳务标志是否为Y
		    			tempExpenseVoList.add(tmpBVo);//如果是费用类存货,那么将此行加入费用VO数组
		    	}
			}
		    
		    if(tempExpenseVoList!=null&&tempExpenseVoList.size()!=0){
		    	newtmpBodyVo = new DJZBItemVO[tempExpenseVoList.size()];
		    	newtmpBodyVo = tempExpenseVoList.toArray(newtmpBodyVo);
		    }
		  //add by 付世超  2010-10-27 加入对非费用暂估的过滤 end
			}
		  //2010-11-22 MeiChao end 对应付单表体行费用信息的过滤改造
			
			if(getbillListPanel().getBillListData().isMeataDataTemplate())
				getbillListPanel().getBillListData().setBodyValueObjectByMetaData(newtmpBodyVo);
			else
				getbillListPanel().setBodyValueVO(newtmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

	}


	public void loadHeadData() {
		try {
			//利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			//wanglei 2013-12-13 获得查询条件
			IBillReferQuery queryCondition = getQueyDlg();
			tmpWhere = queryCondition.getWhereSQL();
			
			if (getHeadCondition() != null) {  //2013-12-13 下面的方法都没写,这里还重置，晕！
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			// 采购发票参照采购应付单的过滤条件(是否红冲：否    是否暂估应付：是)
//				whereString = whereString+" and zb.isreded = 'N' and zb.zgyf = 1 and zb.dr = 0)";
				//因为生成的暂估应付单业务流程改为 arap中的 应付通用流程 所以将更改后的流程id加入查询条件 
					//另加入对暂估应付的过滤  1 标识暂估应付  0 标识 非暂估应付 by 付世超 2010-10-23
			//2010-11-22 将下面语句中的 or zb.xslxbm = '00011110000000002RGT' 去掉,
			//并添加一个对当前单据类型的判断,防止其他地方使用到采购应付单拉式时此条件对其他业务产生影响
//			if(this.getCurrentBillType()!=null&&this.getCurrentBillType().equals("25")){
//				
//			tmpWhere = tmpWhere + " and (zb.zyx19 is null or zb.zyx19<>'Y')  and zb.zgyf = '1' and zb.sxbz = 10 and zb.dr=0  "; //wanglei 2013-12-13 这种低级错误也有/
//			
//			}
			
			String businessType = null;
			if (getIsBusinessType()) {
				businessType = getBusinessType();
			}
			CircularlyAccessibleValueObject[] tmpHeadVo = PfUtilBO_Client.queryHeadAllData(getBillType(),
					businessType, tmpWhere);

			if(getbillListPanel().getBillListData().isMeataDataTemplate())
				getbillListPanel().getBillListData().setHeaderValueObjectByMetaData(tmpHeadVo);
			else
				getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

			//lj+ 2005-4-5
			//selectFirstHeadRow();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"pfworkflow", "UPPpfworkflow-000490")/*@res "数据加载失败！"*/);
		}
	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.bill.BillTableMouseListener#mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent)
	 */
	public void mouse_doubleclick(BillMouseEnent e) {

	}

	/**
	 * "确定"按钮的响应，从界面获取被选单据VO
	 */
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(m_billVo,
					m_billHeadVo, m_billBodyVo);
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
		}
		this.closeOK();
	}

	/**
	 * 在该界面上进行再次查询
	 */
	public void onQuery() {
		IBillReferQuery queryCondition = getQueyDlg();
		if (queryCondition.showModal() == UIDialog.ID_OK) {
			//返回查询条件
			m_whereStr = queryCondition.getWhereSQL();
			loadHeadData();
			//fgj@ 2001-11-06 修改置空表体数据
			getbillListPanel().setBodyValueVO(null);
			//hxr@ 2005-3-31 初始选中一行
			JTable table = getbillListPanel().getParentListPanel().getTable();
			int iRowCount = table.getRowCount();
			if (iRowCount > 0 && table.getSelectedRow() < 0) {
				table.changeSelection(0, 0, false, false);
			}
		}
	}

	/**
	 * 更改主表的显示位数（根据产品的返回）
	 * @param billDataVo
	 * @param strShow
	 * @throws Exception
	 */
	public void setVoDecimalDigitsBody(BillListData billDataVo, String[][] strShow) throws Exception {
		if (strShow.length < 2)
			return;

		if (strShow[0].length != strShow[1].length)
			throw new Exception(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000115")/*@res "显示位数组第一、二行不匹配"*/);

		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getBodyItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	/**
	 * 更改主表的显示位数（根据产品的返回）
	 * @param billDataVo
	 * @param strShow
	 * @throws Exception
	 */
	public void setVoDecimalDigitsHead(BillListData billDataVo, String[][] strShow) throws Exception {
		if (strShow.length < 2) { return; }
		if (strShow[0].length != strShow[1].length)
			throw new Exception(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000115")/*@res "显示位数组第一、二行不匹配"*/);

		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getHeadItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	/* (non-Javadoc)
	 * @see event.ListSelectionListener#valueChanged(event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int headRow = ((ListSelectionModel) e.getSource()).getAnchorSelectionIndex();
			if (headRow >= 0) {
				headRowChange(headRow);
			}
		}
	}
	/**
	 * @function 根据存货基本档案查询查询存货的应税劳务标示
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc 存货基本档案ID
	 *
	 * @return Object
	 *
	 * @date 2010-8-7 上午10:31:53
	 */
	private Object execQuery(String pk_invbasdoc) {  
		IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Object o = null;
		String sql = "select laborflag from bd_invbasdoc where pk_invbasdoc = '"+pk_invbasdoc+"'";
		try {
			o = qryBS.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return o;
	}

	
	
}