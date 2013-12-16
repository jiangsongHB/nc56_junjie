package nc.ui.reportquery.component.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.event.TableModelEvent;

import nc.bs.logging.Logger;
import nc.ui.pub.formulaparse.FormulaParseRunInServer;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.qecommon.QEBigDecimal;
import nc.vo.pub.querymodel.DatasetUtil;

import com.borland.dx.dataset.AccessEvent;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.DataSetView;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;

/**
 * С�ƺϼƱ�ģ��
 * 
 * @author jl created on 2005-12-26 <strong>����޸���: jl<strong> <strong>����޸�����:
 *         2005-12-26<stong>
 */
public class AggregateTableModel extends RptTableModel {
	/**
	 * �洢���ݼ��е���������--���ݼ��е�һ�ж�Ӧ��������е�һ��Ԫ�� ׷�ӵ�С�ƺϼ��б��������
	 */
	protected Vector datas;

	/**
	 * С�ƺϼ�������
	 */
	protected AggregateDescriptor aggrDesc;

	/**
	 * �洢�еļ������ڷּ���ʾ��
	 */
	protected Hashtable rowLevels = new Hashtable();

	/**
	 * ���ܷ���
	 */
	protected boolean isSumUp = false;

	/**
	 * ��֤��־
	 */
	protected boolean verified = false;

	/**
	 * ������ͼ
	 */
	protected DataSetView dataSetView;
	/**
	 * Ĭ�Ϲ��캯�� created on 2005-12-27
	 */
	public AggregateTableModel() {
		super();
	}

	/**
	 * ��DataSet����������ʼ�� created on 2005-12-27
	 * 
	 * @param dataSet
	 * @param aggrDesc
	 */
	public AggregateTableModel(DataSet dataSet, AggregateDescriptor aggrDesc) {
		setDataSet(dataSet);
		this.aggrDesc = aggrDesc;
		isSumUp = aggrDesc.isSumUP();
		aggregateData();
	}

	/**
	 * С�ƺϼ��㷨 created on 2005-12-26 <strong>����޸���: jl<strong> <strong>����޸�����:
	 * @i18n upp08110600262=aggregateData������ʱ
	 */
	protected void aggregateData() {
		// ��ʼʱ��--��¼��
		long start = System.currentTimeMillis();

		ignoreAccess = true;
		// ��֤������
		verifyDesp();
		// �Ŵ�洢����������
		datas = new Vector(dataSet.getRowCount() * 2);
		// ���������ͼ
		dataSetView = getDataSetView();
		// ������
		Variant[] keyValues = new Variant[aggrDesc.getGroupColumns().length];
		for (int i = 0; i < keyValues.length; i++) {
			keyValues[i] = new Variant();
		}
		// С�ƺϼ���
		Variant[][] aggrRow = new Variant[keyValues.length][];

		int[] counts = new int[keyValues.length];
		// �ܼ���
		Variant[] totalRow = createVariantArray(dataSetView.getColumnCount());
		// �õ������б��
		int[] keyOrdinal = getkeyOrdinal();
		// С�ƺϼ��б��
		int[] aggrOrdinal = getaggrOrdinal();
		// ������������¼Ϊ�յ���
		int[][] suppleCounts = getSuppleCount();

		rowLevels.clear();
		// ����׼���������

		// ����������ǰ���ȼӺϼ�С����
		if (isSumUp) {
			if (aggrDesc.isTotalAggr()) {
				// �ϼ��е��м���
				int totallevel = aggrDesc.getGroupColumns().length + 1;
				// ����ϼ���
				addTotalRow(totalRow, totallevel);
			}
			// ���С����
			if (aggrDesc.isSubtotal() && dataSetView.getRowCount() > 0) {
				for (int j = 0; j < keyValues.length; j++) {
					// ���·���ؼ�ֵ
					keyValues[j] = updateKeyValue(keyOrdinal[j], 0);
					// ���С����
					aggrRow[j] = createVariantArray(dataSetView.getColumnCount());
					// ����С����
					addSubTotalRow(aggrRow[j], keyOrdinal[j], keyValues[j], j);
				}
			}
		}

		// //zjb+
		// if (!dataSetView.isOpen()) {
		// dataSetView.open();
		// }
		int iRowCount = dataSetView.getRowCount();

		for (int row = 0; row < iRowCount; row++) {
			// ����key���ж��Ƿ�����С����
			if (aggrDesc.isSubtotal())
				for (int i = 0; i < keyValues.length; i++) {
					Variant newValues = new Variant();
					dataSetView.getVariant(keyOrdinal[i], row, newValues);
					if (keyValues[i] == null || !newValues.equals(keyValues[i])) {
						if (isSumUp) {
							for (int j = i; j < keyValues.length; j++) {
								aggrRow[j] = createVariantArray(dataSetView.getColumnCount());

								keyValues[j] = updateKeyValue(keyOrdinal[j], row);

								addSubTotalRow(aggrRow[j], keyOrdinal[j], keyValues[j], j);

								counts[j] = 0;// ��������

								Arrays.fill(suppleCounts[j], 0);// ������������
							}
						} else {
							for (int j = keyValues.length - 1; j >= i; j--) {
								if (aggrRow[j] != null) {
									addSubTotalRow(aggrRow[j], keyOrdinal[j], keyValues[j], j);
								}
								keyValues[j] = updateKeyValue(keyOrdinal[j], row);

								aggrRow[j] = createVariantArray(dataSetView.getColumnCount());

								counts[j] = 0;// ��������
								Arrays.fill(suppleCounts[j], 0);// ������������
							}
						}
						break;
					}
				}

			Variant[] dataRow = createVariantArray(dataSetView.getColumnCount());
			// �õ�ԭʼ����
			for (int i = 0; i < dataRow.length; i++) {
				dataSetView.getVariant(i, row, dataRow[i]);
			}

			// ����С����
			if (aggrDesc.isSubtotal())
				computeSubTotalRow(aggrRow, aggrOrdinal, dataRow, counts, suppleCounts);

			// �����ܼ���
			if (aggrDesc.isTotalAggr())
				computeTotalRow(aggrRow, totalRow, aggrOrdinal, dataRow, row, suppleCounts);
			// ��ӵ�ǰ��
			//jl+ since v5.02��ʾ/����ԭʼ�п���
			if(aggrDesc.isShowOriginRow()){
				datas.add(dataRow);	
			}
		}

		// ���������ں�����
		if (!isSumUp) {
			// ���С����
			if (aggrDesc.isSubtotal())
				for (int i = keyValues.length - 1; i >= 0; i--) {
					if (aggrRow[i] == null) {
						continue;
					}
					addSubTotalRow(aggrRow[i], keyOrdinal[i], keyValues[i], i);
				}

			// ����ܼ���
			if (aggrDesc.isTotalAggr()) {
				addTotalRow(totalRow, keyValues.length + 1);
			}
		}

		// ���㹫ʽ
		calcFormula(aggrOrdinal);

		fireTableDataChanged();

		ignoreAccess = false;

		long time = System.currentTimeMillis() - start;
		// ��ӡ��С�ƺϼƺ�ʱ--����֮��
		Logger.debug("aggregateData������ʱ" + time + "ms!@" + hashCode());
	}

	/**
	 * ��÷����б�� created on 2005-12-26
	 * 
	 * @param dataSetView
	 * @param aggrDescl
	 * @return <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	private int[] getkeyOrdinal() {
		int[] keyOrdinal = new int[aggrDesc.getGroupColumns().length];
		for (int i = 0; i < keyOrdinal.length; i++) {
			keyOrdinal[i] = dataSetView.findOrdinal(aggrDesc.getGroupColumns()[i]);
		}
		return keyOrdinal;
	}

	/**
	 * ��ȡС�ƺϼ������ created on 2005-12-26
	 * 
	 * @param dataSetView
	 * @return <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	private int[] getaggrOrdinal() {
		int[] aggrOrdinal = new int[aggrDesc.getAggrColumns().length];
		for (int i = 0; i < aggrOrdinal.length; i++) {
			aggrOrdinal[i] = dataSetView.findOrdinal(aggrDesc.getAggrColumns()[i]);
		}
		return aggrOrdinal;
	}

	/**
	 * ��ø��������� created on 2005-12-26
	 * 
	 * @return <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	private int[][] getSuppleCount() {
		int[][] suppleCounts = new int[aggrDesc.getGroupColumns().length + 1][aggrDesc.getAggrColumns().length];// ������������¼Ϊ��ֵ����
		for (int i = 0; i < suppleCounts.length; i++) {
			suppleCounts[i] = new int[aggrDesc.getAggrColumns().length];
			Arrays.fill(suppleCounts[i], 0);
		}
		return suppleCounts;
	}

	/**
	 * ����С���� created on 2005-12-26 <strong>����޸���: jl<strong> <strong>����޸�����:
	 * 2005-12-26<stong>
	 */
	private void computeSubTotalRow(Variant[][] aggrRow, int[] aggrOrdinal, Variant[] dataRow, int[] counts,
			int[][] suppleCounts) {
		for (int i = 0; i < aggrRow.length; i++) {
			counts[i]++;
			for (int j = 0; j < aggrOrdinal.length; j++) {
				int order2 = aggrOrdinal[j];
				if (dataRow[order2].isNull()) {
					suppleCounts[i][j]++;
				}
				aggrProcess(aggrRow[i][order2], dataRow[order2], aggrDesc.getAggrOperators()[j], counts[i],
						suppleCounts[i][j]);
			}
		}
	}

	/**
	 * �����ܼ��� created on 2005-12-26
	 * 
	 * @param aggrRow
	 * @param totalRow
	 * @param aggrOrdinal
	 * @param dataRow
	 * @param row
	 * @param suppleCounts
	 *            <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	private void computeTotalRow(Variant[][] aggrRow, Variant[] totalRow, int[] aggrOrdinal, Variant[] dataRow,
			int row, int[][] suppleCounts) {
		for (int j = 0; j < aggrOrdinal.length; j++) {
			int order2 = aggrOrdinal[j];
			if (dataRow[order2].isNull()) {
				suppleCounts[aggrRow.length][j]++;
			}
			aggrProcess(totalRow[order2], dataRow[order2], aggrDesc.getAggrOperators()[j], row + 1,
					suppleCounts[aggrRow.length][j]);
		}
	}

	// TableModel interface implementation

	/**
	 * <p>
	 * Returns the number of rows in the model, which is equal to the number of
	 * rows in the <code>DataSet</code>.
	 * </p>
	 * 
	 * @return The number of rows in the model (<code>DataSet</code>).
	 */
	public int getRowCount() {
		if (isValidDataSetState()) {
			try {
				return datas.size();
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		// there was a problem getting the rowCount, return 0;
		return 0;
	}

	/**
	 * <p>
	 * Returns the value at the row and column. Does not cause the current
	 * <code>DataSet</code> row to change.
	 * </p>
	 * 
	 * <p>
	 * If the <code>Column</code> has a <code>pickList</code> property
	 * indicating to lookup the value in a different table, the lookup value
	 * will be returned instead.
	 * </p>
	 * 
	 * @param row
	 *            The row of the specified cell.
	 * @param columnIndex
	 *            The column of the specified cell.
	 * @return The value at the row and column.
	 * @see #setValueAt
	 */
	public Object getValueAt(int row, int columnIndex) {
		if (isValidDataSetState()) {
			try {
				Variant[] rowData = (Variant[]) datas.get(row);
				Variant value = rowData[columnMap[columnIndex]];
				if (value.getType() == Variant.BIGDECIMAL) {
					QEBigDecimal bigDecimal = new QEBigDecimal(value.getAsBigDecimal());
					return bigDecimal;
				} else if (value.getType() == Variant.DOUBLE) {
					double d = value.getAsDouble();
					QEBigDecimal bigdecimal = new QEBigDecimal(new BigDecimal(String.valueOf(d)));
					return bigdecimal;
				}
				return value.getAsObject();
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return null;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-27 16:30:21)
	 * 
	 * @return int
	 * @param row
	 *            int
	 */
	public int getRowLevel(int row) {
		Integer level = (Integer) rowLevels.get(new Integer(row));
		return level == null ? 0 : level.intValue();
	}

	/**
	 * ����ϼ��� created on 2005-12-26
	 * 
	 * @param totalRow
	 * @param level
	 *            <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	private void addTotalRow(Variant[] totalRow, int level) {
		int colCount = dataSet.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			if (dataSet.getColumn(i).getDataType() == Variant.STRING) {
				totalRow[i].setString(aggrDesc.getSumFlag());
				break;
			}
		}
		datas.add(totalRow);
		rowLevels.put(new Integer(datas.size() - 1), new Integer(level));
	}

	/**
	 * ���¹ؼ���ֵ created on 2005-12-26
	 * 
	 * @param keyOrdinal
	 * @param row
	 * @return <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	private Variant updateKeyValue(int keyOrdinal, int row) {
		Variant key = new Variant();
        
        // V55+ �޸��ظ����õ��µ�Ч������
        // getDataSetView().getVariant(keyOrdinal, row, key);
        dataSetView.getVariant(keyOrdinal, row, key);
        
		return key;
	}

	/**
	 * ��С���� created on 2005-12-26
	 * 
	 * @param aggrRow
	 * @param keyOrdinal
	 * @param keyValue
	 * @param level
	 *            <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	private void addSubTotalRow(Variant[] aggrRow, int keyOrdinal, Variant keyValue, int level) {
		setKeyValue2TotalRow(aggrRow, keyOrdinal, keyValue, level + 1);
		datas.add(aggrRow);
		rowLevels.put(new Integer(datas.size() - 1), new Integer(aggrDesc.getGroupColumns().length - level));
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-28 10:45:31)
	 * 
	 * @return com.borland.dx.dataset.Variant[]
	 * @param length
	 *            int
	 */
	public Variant[] createVariantArray(int length) {
		Variant[] result = new Variant[length];
		for (int i = 0; i < length; i++) {
			result[i] = new Variant();
		}
		return result;
	}

	/**
	 * �滻level��valueΪ��Ӧ��ֵ created on 2005-12-27
	 * 
	 * @param sumRow
	 * @param ordinal
	 * @param keyValue
	 * @param level
	 *            <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-27<stong>
	 */
	private void setKeyValue2TotalRow(Variant[] sumRow, int ordinal, Variant keyValue, int level) {
		String value = keyValue.toString();
		String strlevel = "" + level;
		String flag = StringUtil.replaceAllString(aggrDesc.getSubtotalFlag(), "%value%", value);
		flag = StringUtil.replaceAllString(flag, "%level%", strlevel);

		sumRow[ordinal].setString(flag);
	}

	/**
	 * ���������� created on 2005-12-27
	 * 
	 * @param colName
	 *            ����
	 * @param aggrOper
	 *            �����
	 * @param add
	 *            ����/ɾ���б�־ <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-27<stong>
	 */
	protected void updateAggrDsp(String colName, String aggrOper, boolean add) {
		String[] aggrCols = aggrDesc.getAggrColumns();
		String[] aggrOpers = aggrDesc.getAggrOperators();
		if (add) {
			int length = aggrCols == null ? 0 : aggrCols.length;
			String[] newAggrCols = new String[length + 1];
			String[] newAggrOpers = new String[length + 1];
			if (length > 0) {
				System.arraycopy(aggrCols, 0, newAggrCols, 0, length);
				System.arraycopy(aggrOpers, 0, newAggrOpers, 0, length);
			}
			newAggrCols[length] = colName;
			newAggrOpers[length] = aggrOper;
			aggrDesc.setAggrColumns(newAggrCols);
			aggrDesc.setAggrOperators(newAggrOpers);
		} else {
			aggrDesc.delCol(colName);
		}
	}

	/**
	 * ��֤������ created on 2005-12-27 <strong>����޸���: jl<strong> <strong>����޸�����:
	 * 2005-12-27<stong>
	 */
	protected void verifyDesp() {
		if (!verified) {
			int groupCount = (aggrDesc.getGroupColumns() == null) ? 0 : aggrDesc.getGroupColumns().length;
			for (int i = groupCount - 1; i >= 0; i--) {
				if (dataSet.findOrdinal(aggrDesc.getGroupColumns()[i]) < 0) {
					aggrDesc.delCol(aggrDesc.getGroupColumns()[i]);
				}
			}
			int aggrCount = (aggrDesc.getAggrColumns() == null) ? 0 : aggrDesc.getAggrColumns().length;
			for (int i = aggrCount - 1; i >= 0; i--) {
				if (dataSet.findOrdinal(aggrDesc.getAggrColumns()[i]) < 0) {
					aggrDesc.delCol(aggrDesc.getAggrColumns()[i]);
				}
			}
			verified = true;
		}
	}

	/**
	 * �������� created on 2005-12-26
	 * 
	 * @param aggrValue
	 * @param rowValue
	 * @param type
	 * @param rownum
	 * @param suppleCount
	 *            <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 */
	public void aggrProcess(Variant aggrValue, Variant rowValue, String type, int rownum, int suppleCount) {
		if (type == null || type.equals(AggregateDescriptor.SUM)) {
			//@edit by yza at 2009-2-11 �۸�һ�������ٸĻ�ȥ,��ȻVariant��Double������ӻ���С��λ����������
			VariantWrapper wrapper = new VariantWrapper(aggrValue);
			BigDecimal value = wrapper.add(rowValue);
			if(value == null){
				aggrValue.add(rowValue, aggrValue);
			}else{
				aggrValue.setDouble(value.doubleValue());
			}
		} else if (type.startsWith(AggregateDescriptor.AVG)) {
			if (aggrValue.isNull() && rowValue.isNull()) {
				aggrValue.setAsVariant(rowValue);
			} else {
				double oldvalue = aggrValue.isNull() ? 0.0 : Double.parseDouble(aggrValue.toString());
				double rowvalue = rowValue.isNull() ? 0.0 : Double.parseDouble(rowValue.toString());
				if (type.indexOf(AggregateDescriptor.IGNORENULL) >= 0) {
					if (rowValue.isNull()) {
						return;
					}
					rownum = rownum - suppleCount;
				}
				if (rownum == 0) {
					aggrValue.setDouble(0.0);
					return;
				}
				double newvalue = oldvalue + (rowvalue - oldvalue) / rownum;
				aggrValue.setDouble(newvalue);
			}
		} else if (type.equals(AggregateDescriptor.MAX)) {
			if (aggrValue.isNull() || aggrValue.compareTo(rowValue) < 0) {
				aggrValue.setAsVariant(rowValue);
			}
		} else if (type.equals(AggregateDescriptor.MIN)) {
			if(rownum==1)//csli+:ȷ����һ�θ�ֵ
				aggrValue.setAsVariant(rowValue);
			else if ( aggrValue.compareTo(rowValue) > 0) {
				aggrValue.setAsVariant(rowValue);
			}
		} else if (type.equals(AggregateDescriptor.PRE)) {
			aggrValue.setAsVariant(rowValue);
		}
	}

	/**
	 * ���㹫ʽ created on 2005-12-27
	 * 
	 * @param aggrOrdinal
	 *            <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-27<stong>
	 */
	protected void calcFormula(int[] aggrOrdinal) {
		Enumeration keys = rowLevels.keys();

		// �õ���Ҫ���㹫ʽ��ͳ����
		ArrayList calcRows = new ArrayList();
		Integer Ilevel0 = new Integer(0);
		while (keys.hasMoreElements()) {
			Integer IRow = (Integer) keys.nextElement();
			Integer Ilevel = (Integer) rowLevels.get(IRow);
			if (Ilevel.compareTo(Ilevel0) > 0) {
				calcRows.add(IRow);
			}
		}
		int statisticalRowCount = calcRows.size();
		if (statisticalRowCount == 0)
			return;

		// ���㹫ʽ
		for (int j = 0; j < aggrOrdinal.length; j++) {
			String oper = aggrDesc.getAggrOperators()[j];
			if (oper == null || !oper.startsWith(AggregateDescriptor.FOR)) {
				continue;
			}
			String formulaExp = oper.substring(oper.indexOf(":") + 1);
			FormulaParseFather parser = getFormulaParser(aggrDesc.getAggrColumns()[j], formulaExp);
			VarryVO varry = parser.getVarry();
			String[] params = varry.getVarry();
			int order2 = aggrOrdinal[j];
			Object[] results = null;
			if (params != null && params.length > 0) {
				// ȡ���ʽ��Ĳ���ֵ
				// Hashtable htParamV = new Hashtable();
				for (int i = 0; i < params.length; i++) {
					ArrayList paramArray = new ArrayList(statisticalRowCount);
					int ordinal = getColumnOrdinal(params[i]);
					for (int l = 0; l < statisticalRowCount; l++) {
						int row = ((Integer) calcRows.get(l)).intValue();
						Object objValue = getValueAt(row, ordinal);
						// String strValue = (objValue==
						// null)?"":objValue.toString();
						// if ((value.getType() == Variant.STRING) && (colType
						// == Variant.STRING))
						// strValue = parser.getStarts()
						// +strValue+parser.getEnds();
						paramArray.add(objValue);
					}
					parser.addVariable(params[i], paramArray);
				}
				// parser.setDataS(htParamV);
				// д�빫ʽ�е�ֵ
				results = parser.getValueO();
			} else {// ��ʽ��û�в������������Ȼ�����Բ��󣬵����ų��������ı�̬
				Object result = parser.getValueAsObject();
				results = new Object[statisticalRowCount];
				Arrays.fill(results, result);
			}
			// ����ֵ
			for (int i = 0; i < statisticalRowCount; i++) {
				int row = ((Integer) calcRows.get(i)).intValue();
				int colType = getColumn(order2).getDataType();
				Variant val = new Variant();
				if (results[i] != null) {
					val.setAsObject(DatasetUtil.changeObject2CorrectType(results[i], colType), colType);
				} else {
					val.setNull(Variant.UNASSIGNED_NULL);
				}
				// convertFormulaResult(results[i], colType);
				((Variant[]) datas.get(row))[order2] = val;
			}
		}
	}

	/**
	 * �õ���ʽ���� �������ڣ�(2004-7-7 14:18:57)
	 * 
	 * @return nc.ui.pub.formulaparse.FormulaParse
	 * @param formulaName
	 *            ��ʽ�� java.lang.String
	 * @param formulaExp
	 *            ��ʽ���ʽ java.lang.String
	 */
	public FormulaParseFather getFormulaParser(String formulaName, String formulaExp) {
		if (formulaName == null || formulaExp == null) {
			return null;
		}
		FormulaParseRunInServer parser = new FormulaParseRunInServer();
		if(formulaExp.indexOf("->")<0)
			formulaExp=formulaName+"->"+formulaExp;
		parser.setExpress(formulaExp);
		return parser;
	}

	/**
	 * ���ݵ�ǰ��ģ�����ݼ���ȡ������ͼ created on 2005-12-26
	 * 
	 * @return <strong>����޸���: jl<strong> <strong>����޸�����: 2005-12-26<stong>
	 * @see DataSetView
	 */
	private DataSetView getDataSetView() {
		// if(dataSetView==null){ //zjbע��
		dataSetView = new DataSetView();
		dataSetView.setStorageDataSet((StorageDataSet) dataSet);

		//֧�ֺϲ������� by csli
		boolean bGroup=aggrDesc.getGroupColumns() != null&&aggrDesc.getGroupColumns().length >0;
		if ((!bGroup)&&dataSet.getSort()==null) {
			aggrDesc.setGroupColumns(new String[0]);
		}else if(dataSet.getSort()==null){
			dataSetView.setSort(new SortDescriptor("AggrSort" + System.currentTimeMillis(), aggrDesc.getGroupColumns(),
			aggrDesc.getOrder(), false, false, java.util.Locale.getDefault().toString()));
		}else if(!bGroup){
			dataSetView.setSort(dataSet.getSort());
		}else{
			dataSetView.setSort(getSortDescriptor());
		}
		
		// ������������
		dataSetView.setMasterLink(dataSet.getMasterLink());
		dataSetView.open();
		// ���й���������
		if (dataSet.getRowFilterListener() != null) {
			try {
				dataSetView.addRowFilterListener(dataSet.getRowFilterListener());
			} catch (Exception ex) {
				Logger.error(ex);
			}
		}
		dataSetView.refilter();
		// } //zjbע��
		return dataSetView;
	}
	
	private SortDescriptor getSortDescriptor(){
		SortDescriptor desc=dataSet.getSort();
		if (desc != null) {
			int length = aggrDesc.getOrder().length + desc.getDescending().length;
			String[] keys = new String[length];
			boolean[] orders = new boolean[length];
			for (int i = 0; i < length; i++) {
				keys[i] = i < aggrDesc.getOrder().length ? aggrDesc.getGroupColumns()[i] : desc.getKeys()[i
						- aggrDesc.getOrder().length];
				orders[i] = i < aggrDesc.getOrder().length ? aggrDesc.getOrder()[i] : desc.getDescending()[i
						- aggrDesc.getOrder().length];
			}
			return new SortDescriptor("AggrSort" + System.currentTimeMillis(), keys, orders, false, false,
					java.util.Locale.getDefault().toString());
		}
		return desc;
	}



	public void Data_Change() {
		aggregateData();
	}

	public void Row_Change_Posted() {
		aggregateData();
		// ���������Ϊ��֪ͨ������и�ʽ
		fireTableChanged(new TableModelEvent(this, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, TableModelEvent.UPDATE));

	}

	public void Column_Add() {
		updateModel();
		// arg1.getNewOrdinal()ȡ����
		int column = getColumnCount() - 1;
		fireTableChanged(new TableModelEvent(this, 0, 0, column, TableModelEvent.INSERT));
		// aggregateData();
	}

	public void Column_Drop(AccessEvent accEvent) {
		int ordinal = accEvent.getDropColumn().getOrdinal();
		int column = convertDSordinal2ModelIndex(ordinal);
		String colName = accEvent.getDropColumn().getColumnName();
		updateAggrDsp(colName, "", false);
		updateModel();
		fireTableChanged(new TableModelEvent(this, 0, 0, column, TableModelEvent.DELETE));
		// aggregateData();
	}
	
	/*
	 * Brload��Variant�ڲ�double��������С��λ��������,
	 * ����ͨ��clone����װVariant�����ͺ������������������
	 * ���ı䱾�����������
	 * yza
	 */
	class VariantWrapper{
		private Variant target = null;
		public VariantWrapper(Variant target){
			this.target = target;
		}
		
		public BigDecimal add(Variant part){
			if (target.getType() == Variant.DOUBLE
					&& part.getType() == Variant.DOUBLE) {
				//clone target
				Variant targetNew = new Variant(Variant.BIGDECIMAL);
				targetNew.setVariant(targetNew);
				targetNew.setBigDecimal(new BigDecimal(String.valueOf(target.getDouble())));
				//clone part
				Variant partNew = new Variant(Variant.BIGDECIMAL);
				partNew.setVariant(partNew);
				partNew.setBigDecimal(new BigDecimal(String.valueOf(part.getDouble())));
				
				targetNew.add(partNew, targetNew);
				return targetNew.getBigDecimal();
			}
			return null;
		}
	}
} 