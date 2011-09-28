package nc.ui.cmp.pub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.logging.Logger;
import nc.cmp.utils.CheckException;
import nc.cmp.utils.CmpUtils;
import nc.cmp.utils.Lists;
import nc.ui.cmp.netpayment.PaymentProc;
import nc.ui.cmp.settlement.QueryAggVOHandler;
import nc.ui.cmp.settlement.UIRuntimeVO;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.cmp.settlement.DataSourceVO;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.pub.BusinessException;

public class DefaultListEventHandler {

	private QueryAggVOHandler query;

	private UIRuntimeVO runtime;
	
	private PaymentProc netPayProc;
	
	private int pageSize;
	
//	private Map<Integer,List<SettlementHeadVO>> map;
	
//	private Map<Integer,List<SettlementHeadVO>> selectedMap;
	
//	private Map<String,SettlementHeadVO> selectedMap;
	
	private Map<Integer,List<SettlementHeadVO>> tempMap;
	
	
	public void onBoNext() {
		try {
			dealSelectList(getRuntime().getTransfer().getPageNo());
//			dealSelectList(getRuntime().getTransfer().getPageNo());
			DataSourceVO<SettlementHeadVO> source = new DataSourceVO<SettlementHeadVO>();
			int temp = getRuntime().getTransfer().getPageNo();
//			getRuntime().getTransfer().setTempMap(getTempMap());
			source.setGoToPage(++temp);
			source.setPageSize(getRuntime().getTransfer().getPageSize());
			source.setCondition(getRuntime().getTransfer().getCondition());
			source.setTotalPage(getRuntime().getTransfer().getTotalPage());
			source.setVoucher(getRuntime().getTransfer().getVourch());
			if(getRuntime().getTransfer().getMap().get(source.getGoToPage()) != null) {
				getQuery().bufferAggVO(getRuntime().getListUI(), getRuntime().getTransfer().getMap().get(source.getGoToPage()), source);
			}else {
				getQuery().queryAggVO(getRuntime().getListUI(), source);
			}
			getRuntime().getTransfer().setPageNo(temp);
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getRuntime().getListUI().showErrorMessage(e.getMessage());
		}
	}

	private void dealSelectList(int pageNo) throws BusinessException {
		if(!CheckException.checkContionsIsNull(getSelecteds())) {
			getRuntime().getTransfer().getTempMap().remove(pageNo);
//			getRuntime().getTransfer().getTempMap().put(pageNo, getSelecteds());
			getRuntime().getTransfer().getTempMap().put(pageNo, getSelecteds());
		}else {
//			getRuntime().getTransfer().getTempMap().remove(pageNo);
			if(getRuntime().getTransfer().getTempMap().containsKey(pageNo)) {
				getRuntime().getTransfer().getTempMap().remove(pageNo);
			}
		}
	}

	public void onBoPrev() {
		try {
//			dealSelectList();
			dealSelectList(getRuntime().getTransfer().getPageNo());
			DataSourceVO<SettlementHeadVO> source = new DataSourceVO<SettlementHeadVO>();
//			getRuntime().getTransfer().setTempMap(getTempMap());
			int temp = getRuntime().getTransfer().getPageNo();
			source.setGoToPage(--temp);
			source.setPageSize(getRuntime().getTransfer().getPageSize());
			source.setCondition(getRuntime().getTransfer().getCondition());
			source.setTotalPage(getRuntime().getTransfer().getTotalPage());
			source.setVoucher(getRuntime().getTransfer().getVourch());
			if(getRuntime().getTransfer().getMap().get(source.getGoToPage()) != null) {
				getQuery().bufferAggVO(getRuntime().getListUI(), getRuntime().getTransfer().getMap().get(source.getGoToPage()), source);
			}else {
				getQuery().queryAggVO(getRuntime().getListUI(), source);
			}
			getRuntime().getTransfer().setPageNo(temp);
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getRuntime().getListUI().showErrorMessage(e.getMessage());
		}
	}

	public void onBoLast() {
		try {
//			dealSelectList();
			dealSelectList(getQuery().getTotalPage());
//			getRuntime().getTransfer().setTempMap(getTempMap());
			DataSourceVO<SettlementHeadVO> source = new DataSourceVO<SettlementHeadVO>();
			source.setGoToPage(getQuery().getTotalPage());
			source.setPageSize(getRuntime().getTransfer().getPageSize());
			source.setCondition(getRuntime().getTransfer().getCondition());
			source.setTotalPage(getRuntime().getTransfer().getTotalPage());
			source.setVoucher(getRuntime().getTransfer().getVourch());
			if(getRuntime().getTransfer().getMap().get(source.getGoToPage()) != null) {
				getQuery().bufferAggVO(getRuntime().getListUI(), getRuntime().getTransfer().getMap().get(source.getGoToPage()), source);
			}else {
				getQuery().queryAggVO(getRuntime().getListUI(), source);
			}
			getRuntime().getTransfer().setPageNo(getQuery().getTotalPage());
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getRuntime().getListUI().showErrorMessage(e.getMessage());
		}
	}

	public void onBoFirst() {
		try {
			dealSelectList(1);
//			getRuntime().getTransfer().setTempMap(getTempMap());
			DataSourceVO<SettlementHeadVO> source = new DataSourceVO<SettlementHeadVO>();
			source.setGoToPage(1);
			source.setPageSize(getRuntime().getTransfer().getPageSize());
			source.setCondition(getRuntime().getTransfer().getCondition());
			source.setTotalPage(getRuntime().getTransfer().getTotalPage());
			source.setVoucher(getRuntime().getTransfer().getVourch());
			if(getRuntime().getTransfer().getMap().get(source.getGoToPage()) != null) {
				getQuery().bufferAggVO(getRuntime().getListUI(), getRuntime().getTransfer().getMap().get(source.getGoToPage()), source);
			}else {
				getQuery().queryAggVO(getRuntime().getListUI(), source);
			}
			getRuntime().getTransfer().setPageNo(1);
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getRuntime().getListUI().showErrorMessage(e.getMessage());
		}
	}


	public void onBoSelNone() {
		for (int row = 0; row < getRuntime().getListUI().getBillListPanel().getHeadBillModel().getRowCount(); row++) {
			getRuntime().getListUI().getBillListPanel().getHeadBillModel().getRowAttribute(row).setRowState(BillModel.NORMAL);
		}
		getRuntime().getListUI().getBillListPanel().repaint();
	}

	public void onBoSelAll() {
		for (int row = 0; row < getRuntime().getListUI().getBillListPanel().getHeadBillModel().getRowCount(); row++) {
			getRuntime().getListUI().getBillListPanel().getHeadBillModel().getRowAttribute(row).setRowState(BillModel.SELECTED);
		}
		getRuntime().getListUI().getBillListPanel().repaint();
	}

	public void execLoadFormula() {
		getRuntime().getListUI().getBillListPanel().getHeadBillModel().execLoadFormula();
	}

	public SettlementAggVO[] getStateSelectedVOs() throws BusinessException {
		List<SettlementAggVO> aggVOList = new ArrayList<SettlementAggVO>();
		int count = getRuntime().getListUI().getBillListPanel().getHeadBillModel().getDataVector().size();
		Set<Integer> selectedRowls = new HashSet<Integer>();
		for (int i = 0; i < count; i++) {
			if (getRuntime().getListUI().getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				selectedRowls.add(i);
			}
		}
		if(selectedRowls.size() == 0) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000025")/*@res "请选择需要一张的数据"*/);
		}
		for (Iterator iter = selectedRowls.iterator(); iter.hasNext();) {
			Integer row = (Integer) iter.next();
			SettlementAggVO aggVO = (SettlementAggVO)getRuntime().getListUI().getBillListPanel().getBillListData().getBillValueVO(row, SettlementAggVO.class.getName(), SettlementHeadVO.class.getName(), SettlementBodyVO.class.getName());
			SettlementHeadVO head = (SettlementHeadVO)aggVO.getParentVO();
			head = getRuntime().getBufferMap().get(head.getPrimaryKey());
			SettlementBodyVO[] bodys = CmpUtils.covertListToArrays(head.getBodys(), SettlementBodyVO.class);
			aggVO = new SettlementAggVO();
			aggVO.setParentVO(head);
			aggVO.setChildrenVO(bodys);
			aggVOList.add(aggVO);
		}
		return CmpUtils.covertListToArrays(aggVOList, SettlementAggVO.class);
	}
	
	/**
	 * 记录每一页选择的
	 * @return
	 * @throws BusinessException
	 */
	public List<SettlementHeadVO> getSelecteds() throws BusinessException {
		int count = getRuntime().getListUI().getBillListPanel().getHeadBillModel().getDataVector().size();
		Set<Integer> selectedRowls = new HashSet<Integer>();
		for (int i = 0; i < count; i++) {
			if (getRuntime().getListUI().getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				selectedRowls.add(i);
			}
		}
		if(selectedRowls.size() == 0) {
			return null;
		}
		List<SettlementHeadVO> headList = Lists.newArrayList();
		for(Integer row : selectedRowls) {
			SettlementAggVO aggVO = (SettlementAggVO)getRuntime().getListUI().getBillListPanel().getBillListData().getBillValueVO(row, SettlementAggVO.class.getName(), SettlementHeadVO.class.getName(), SettlementBodyVO.class.getName());
			SettlementHeadVO head = (SettlementHeadVO)aggVO.getParentVO();
			head = getRuntime().getBufferMap().get(head.getPrimaryKey());
			headList.add(head);
		}
		return headList;
	}


	public SettlementAggVO[] getSelectedAggVO() throws BusinessException {
		if(CheckException.checkMapIsNull(getRuntime().getTransfer().getTempMap())) {
			List<SettlementAggVO> aggVOList = new ArrayList<SettlementAggVO>();
			int[] rows = getSelectedRows();
			if(rows.length == 0 || rows == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000025")/*@res "请选择要处理的数据"*/);
			}
			for(int i = 0; i<rows.length; i++) {
				int row = rows[i];
				SettlementAggVO aggVO = (SettlementAggVO)getRuntime().getListUI().getBillListPanel().getBillListData().getBillValueVO(row, SettlementAggVO.class.getName(), SettlementHeadVO.class.getName(), SettlementBodyVO.class.getName());
				SettlementHeadVO head = (SettlementHeadVO)aggVO.getParentVO();

				head = getRuntime().getBufferMap().get(head.getPrimaryKey());
				SettlementBodyVO[] bodys = CmpUtils.covertListToArrays(head.getBodys(), SettlementBodyVO.class);
				aggVO = new SettlementAggVO();
				aggVO.setParentVO(head);
				aggVO.setChildrenVO(bodys);
				aggVOList.add(aggVO);
			}
			return CmpUtils.covertListToArrays(aggVOList, SettlementAggVO.class);
		}else {
//			List<SettlementHeadVO> list = Lists.newArrayList();
			dealSelectList(getRuntime().getTransfer().getPageNo());
			List<SettlementAggVO> aggList = Lists.newArrayList();
			for(Integer i : getRuntime().getTransfer().getTempMap().keySet()) {
//				list.addAll(getTempMap().get(i));
				for(SettlementHeadVO head : getRuntime().getTransfer().getTempMap().get(i)) {
					SettlementAggVO agg = new SettlementAggVO();
					agg.setParentVO(head);
					agg.setChildrenVO(CmpUtils.covertListToArrays(head.getBodys(), SettlementBodyVO.class));
					aggList.add(agg);
				}
			}
			return CmpUtils.covertListToArrays(aggList, SettlementAggVO.class);
		}

	}

	public int[] getSelectedRows() {
		int count = getRuntime().getListUI().getBillListPanel().getHeadBillModel()
				.getDataVector().size();

		Set<Integer> selectedRowls = new HashSet<Integer>();
		for (int i = 0; i < count; i++) {
			if (getRuntime().getListUI().getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				selectedRowls.add(i);
			}
		}

		//ncm heyl  高亮显示的行作为选择对象，只选择打上勾的。
//		if (getRuntime().getListUI().getBillListPanel().getHeadTable().getSelectedRow() >= 0) {
//			selectedRowls.add(getRuntime().getListUI().getBillListPanel().getHeadTable()
//					.getSelectedRow());
//		}

		int[] selectedRows = new int[selectedRowls.size()];
		int i = 0;
		for (Integer row : selectedRowls) {
			selectedRows[i++] = row;
		}
		return selectedRows;
	}
	/**
	 *
	 * @param table
	 * @param column
	 * @param pk
	 * @param id
	 * @return
	 */
	public String getColValue(String table, String column, String pk, String id) {
		FormulaParse parser = new FormulaParse();
		parser.setExpress("aim->getColValue(" + table + "," + column + "," + pk + ",id);");
		parser.addVariable("id", id);
		String aim = parser.getValue();
		return aim;
	}

	public void sortHeads(List<SettlementHeadVO> headList) {
		Collections.sort(headList, new Comparator<SettlementHeadVO>() {

			public int compare(SettlementHeadVO o1, SettlementHeadVO o2) {
				if (o1.getPk_tradetype().equals(o2.getPk_tradetype())) {
					if (o1.getBusi_billdate().toString().compareTo(o1.getBusi_billdate().toString()) == 0) {
						return o1.getBillcode().compareTo(o2.getBillcode());
					}
					return o1.getBusi_billdate().toString().compareTo(o1.getBusi_billdate().toString());
				}
				return o1.getPk_tradetype().compareTo(o2.getPk_tradetype());
			}
		});
	}


	public QueryAggVOHandler getQuery() {
		if(query == null) {
			query = new QueryAggVOHandler();
		}
		return query;
	}

	public void setQuery(QueryAggVOHandler query) {
		this.query = query;
	}

	public UIRuntimeVO getRuntime() {
		return runtime;
	}

	public void setRuntime(UIRuntimeVO runtime) {
		this.runtime = runtime;
	}

	public PaymentProc getNetPayProc() {
		if (netPayProc == null) {
			netPayProc = new PaymentProc();
		}
		return netPayProc;
	}

	public void setNetPayProc(PaymentProc netPayProc) {
		this.netPayProc = netPayProc;
	}

//	public Map<String, SettlementHeadVO> getSelectedMap() {
//		return selectedMap;
//	}
//
//	public void setSelectedMap(Map<String, SettlementHeadVO> selectedMap) {
//		this.selectedMap = selectedMap;
//	}

	public Map<Integer, List<SettlementHeadVO>> getTempMap() {
		if(tempMap == null) {
			tempMap = new HashMap<Integer, List<SettlementHeadVO>>();
		}
		return tempMap;
	}

	public void setTempMap(Map<Integer, List<SettlementHeadVO>> tempMap) {
		this.tempMap = tempMap;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

//	public Map<Integer, List<SettlementHeadVO>> getMap() {
//		if(map == null) {
//			map = new HashMap<Integer, List<SettlementHeadVO>>();
//		}
//		return map;
//	}
//
//	public void setMap(Map<Integer, List<SettlementHeadVO>> map) {
//		this.map = map;
//	}

//	public Map<Integer,List<SettlementHeadVO>> getSelectedMap() {
//		if(selectedMap == null) {
//			selectedMap = new HashMap<Integer,List<SettlementHeadVO>>();
//		}
//		return selectedMap;
//	}
//
//	public void setSelectedMap(Map<Integer,List<SettlementHeadVO>> selectedMap) {
//		this.selectedMap = selectedMap;
//	}

}