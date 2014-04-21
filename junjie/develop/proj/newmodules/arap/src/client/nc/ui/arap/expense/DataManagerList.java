package nc.ui.arap.expense;

import java.util.Vector;

import nc.bd.accperiod.AccountCalendar;
import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.logging.Log;
import nc.ui.arap.pub.ConditionVO;
import nc.ui.arap.pub.IuiConstData;
import nc.ui.arap.pub.PubMethodUI;
import nc.ui.arap.pub.ReportTools;
import nc.vo.arap.func.ProxyReporter;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.pub.ClientVO;
import nc.vo.arap.pub.EfficientPubMethod_NEW;
import nc.vo.arap.pub.IEfficientPubMethod;
import nc.vo.arap.pub.NoDataException;
import nc.vo.arap.pub.PubConstData;
import nc.vo.arap.pub.PubMethodVO;
import nc.vo.arap.pub.QryObjVO;
import nc.vo.arap.pub.QueryStructVO;
import nc.vo.arap.pub.RSTransferVO;
import nc.vo.arap.pub.StatValueObject;
import nc.vo.arap.util.StringUtils;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.rs.IResultSetConst;
import nc.vo.pub.rs.LineMode;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.ResultSetCalute;

public class DataManagerList implements PubConstData, IResultSetConst {
	/** ��ѯ���vo���鼯�� */
	private ClientVO[] m_voResults = null;

	/** �Ƿ����ø��ұ�ʶ */
	private boolean m_bFbFlag = false;

	/** �����ڼ䷽ʽ false�����ڼ䷽ʽ */
	private boolean m_bDate = true;

	/** ���ڴ�ӡ�Ĳ�ѯ���� */
	private Vector m_objForPrint = null;

	protected IuiConstData constdata = new IuiConstData();

	/**
	 * @return ���� m_objForPrint��
	 */
	public Vector getObjForPrint() {
		return m_objForPrint;
	}

	/**
	 * @param forPrint
	 *            Ҫ���õ� m_objForPrint��
	 */
	public void setObjForPrint(Vector forPrint) {
		m_objForPrint = forPrint;
	}

	/**
	 * DataManagerZZ ������ע�⡣
	 */
	public DataManagerList() {
		super();
	}

	/**
	 * ���ܣ�������ҳ��ʽ�Ͳ�ѯ���������µĲ�ѯ��������ѯ����ͷ��������� ���ߣ����� ����ʱ�䣺(2001-8-6 13:52:19) ������<|>
	 * ����ֵ�� �㷨�� �쳣������
	 * 
	 * @return nc.vo.arap.pub.ConditionVO
	 * @param voSource
	 *            nc.vo.arap.pub.ConditionVO
	 * @param strBillType
	 *            java.lang.String
	 */
	private ConditionVO getConditions(ConditionVO voSource,
			QueryStructVO voStructs) {
		ConditionVO newVo = (ConditionVO) voSource.clone();
		if (newVo.getVetGroupByFields() == null) {
			newVo.setVetGroupByFields(new Vector<QryObjVO>());
		}

		if (voStructs.getCorp() != null && voStructs.getCorp().length > 1) {
			Vector<QryObjVO> vetObjs = (Vector<QryObjVO>) newVo.getVQryObj()
					.clone();
			vetObjs.insertElementAt(PubMethodUI.getCorpVO(), 0);
			newVo.setVQryObj(vetObjs);
		}
		// for(int i=newVo.getVQryObj().size()-1;i>=0;i--){
		// newVo.getVetGroupByFields().insertElementAt(newVo.getVQryObj().elementAt(i),0);
		// }
		return newVo;
	}

	private boolean depdetailSel = true;

	public boolean isDepdetailSel() {
		return depdetailSel;
	}

	public void setDepdetailSel(boolean depdetailSel) {
		this.depdetailSel = depdetailSel;
	}

	/**
	 * ���ܣ��õ���ѯ�����ֶ� ���ߣ����� ����ʱ�䣺(2001-10-24 11:27:26) ������QueryStructVO voQryStruct
	 * ����ֵ�� �㷨��
	 * 
	 * @return java.lang.String[]
	 */
	String[] getGroupByFlds(String[] sGrpby, boolean bSum)
			throws java.sql.SQLException {
		String sGroupBy = EfficientPubMethod_NEW.array2string(sGrpby,
				IEfficientPubMethod.COMMA);
		if (sGroupBy != null && sGroupBy.length() > 0) {
			sGroupBy += ",";
		} else {
			sGroupBy = "";
		}
		/** �·ݷ�ʽ */
		if (bSum && isDepdetailSel()) {
			sGroupBy += "NDQJ,";
		}
		if (isDepdetailSel())
			sGroupBy += "RQ";
		sGroupBy = sGroupBy.toUpperCase();
		String[] arryGy = PubMethodVO.getToken(sGroupBy,
				IEfficientPubMethod.COMMA);
		return arryGy;
	}


	/**
	 * ���ܣ��õ���ѯ������еĽ���ֶ� ���ߣ����� ����ʱ�䣺(2001-10-25 18:54:44) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return java.lang.String[]
	 */
//	private String[] getJeFlds() {
//		return new String[] { "JFSHLJE", "JFYBJE", "JFFBJE", "JFBBJE"};
//	}

	/**
	 * ���ܣ��õ���Ҫ��LineMode ���ߣ����� ����ʱ�䣺(2001-10-25 18:42:53) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return LineMode[]
	 */
	private LineMode[] getLineModes(QueryStructVO voQryStruct, boolean bFxFlag,
			boolean bHasSum, String[] arryGroupBys, String[][] strObj,
			String[][] strDisplay) throws java.sql.SQLException {
		/** ��ѯ�����ֶ� */

		String sObjFormal = EfficientPubMethod_NEW.array2string(strObj[4], ";");
		String sDisplayFormal = EfficientPubMethod_NEW.array2string(
				strDisplay[4], ";");
		if (sDisplayFormal != null && sObjFormal != null) {
			sDisplayFormal = sObjFormal + ";" + sDisplayFormal;
		}

		/** ��Ϊ��ѯ����Ĳ�ѯ����Key */
		String[] arryKeys = getGroupByFlds(strObj[5], bHasSum);
		/** �Ƿ��������� */
//		boolean[] bHasYe = new boolean[] { false };

		/** �����ֶ����� */
		String[] arrySumFlds = getSumFld(voQryStruct);
		/** �����ۼ��еĽ���ֶ� */
//		String[] arryLjFlds = getLjValueFlds();
		/** �ڳ��Լ������еĽ���ֶ� */

		Vector<LineMode> vetLm = new Vector<LineMode>();

		LineMode lmTemp =null;
		String sumString = "";//sum��ʽ
		for (int j = 0; j < arrySumFlds.length; j++) {
			arrySumFlds[j] = arrySumFlds[j].toUpperCase();
			if (j != 0) {
				sumString += ",";

			}
			sumString += arrySumFlds[j];
		}
		if (sumString.length() > 0) {
			sumString = sumString + "->" + "SUMLINE_" + sumString;
		}
		
		/////end
		vetLm.addElement(new LineMode(LOOP_LINE, "FLAG=1", "", "JFYBJE->JFYBJE"));
//		 
//			vetLm.addElement(lmTemp);
		/** С��linemode */
		for (int i = arryKeys.length - 1; i >= 0; i--) {
			if (arryKeys[i].toLowerCase().indexOf("code") != -1)
				continue;
			String sKeys = "";
			// �����Ʒ��鵫����Ҫ���ϱ�������ͬ�������
			int a = i;
	
			
			
			for (int j = 0; j <= a; j++) {
				if (j != 0) {
					/** ���ǵ�һ�� */
					sKeys += ",";
				}
				sKeys += arryKeys[j];
			
			}
			String sFormula = sumString;
//			String sTempMonth = "";
			if (arryKeys[i].equalsIgnoreCase("NDQJ")) {

				 
				sFormula += ";ZY->\""
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"20060504", "UPP20060504-000215")/*
																	 * @res
																	 * "���ºϼ�"
																	 */
						+ "\"";
			 

				/** ���ºϼ���� */
			} else {

				if (arryKeys[i].equalsIgnoreCase("RQ")) {
					sFormula += ";"
							+ arryKeys[i]
							+ "->\""
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"20060504", "UPP20060504-000216")/*
																		 * @res
																		 * "���պϼ�"
																		 */
							+ "\"";

				} else {
					/** ��Ҫ��ϸ���� */
				
					String xj = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"20060504", "UPP20060504-000000")/* @res "С��" */;
					sFormula += ";" + arryKeys[i] + "->IIF(UPLINE_"
							+ arryKeys[i] + "=\"0\",\"" + xj + "\",UPLINE_"
							+ arryKeys[i] + "+\"" + xj + "\")";
					if (i > 0
							&& arryKeys[i - 1].toLowerCase().indexOf("code") != -1) {
						sFormula += ";" + arryKeys[i - 1] + "->IIF(UPLINE_"
								+ arryKeys[i - 1] + "=\"0\",\"" + xj
								+ "\",UPLINE_" + arryKeys[i - 1] + "+\"" + xj
								+ "\")";
					}
					// }
					sFormula += ";";
				}
			}
			lmTemp = new LineMode(APPEND_LINE, "", sKeys, sFormula, true);
			vetLm.addElement(lmTemp);
		}
		/** �ϼ�linemode */

		
//		if (voQryStruct.getFunnodeFlag() == 20100513) {
		lmTemp = new LineMode(APPEND_LINE, "ZY="
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0001146")/* @res "�ϼ�" */, "", sumString,true);
//		}else{
//		
//		if (strObj[5][0].toLowerCase().indexOf("_name") != -1) {
//            lmTemp = new LineMode(APPEND_LINE, strObj[5][0] + "=" 
//    				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
//					"UC000-0001146")/* @res "�ϼ�" */, "", sumString,true);
//        } else if (strObj[5][0].toLowerCase().indexOf("_code") != -1) {
//	        lmTemp = new LineMode(APPEND_LINE, strObj[5][0] + "=" +  nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
//			"UC000-0001146")/* @res "�ϼ�" */, "", sumString+";"+strObj[5][1] + "->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
//			"UC000-0001146")/* @res "�ϼ�" */ + "\"");
//        }
//		}
//		
		
		vetLm.addElement(lmTemp);
		LineMode[] lmA = new LineMode[vetLm.size()];
		vetLm.copyInto(lmA);

		return lmA;
	}
	
//	private LineMode[] getLineModes(
//		    QueryStructVO voQryStruct,
//		    boolean bFxFlag,
//		    boolean bHasSum,
//		    String[] arryGroupBys,
//		    String[][] strObj,
//		    String[][] strDisplay)
//		    throws java.sql.SQLException {
//		    /**��ѯ�����ֶ�*/
//		    String sObj_Name =
//		    	EfficientPubMethod_NEW.array2string(strObj[5], IEfficientPubMethod.COMMA);
//		    String sObj_Key =
//		    	EfficientPubMethod_NEW.array2string(strObj[1], IEfficientPubMethod.COMMA);
//		    String sObjFormal = EfficientPubMethod_NEW.array2string(strObj[4], ";");
//		    String sDisplayFormal = EfficientPubMethod_NEW.array2string(strDisplay[4], ";");
//		    if (sDisplayFormal != null && sObjFormal != null) {
//		        sDisplayFormal = sObjFormal + ";" + sDisplayFormal;
//		    }
//
//		    /**��Ϊ��ѯ����Ĳ�ѯ����Key*/
//		    String[] arryKeys = getGroupByFlds(strObj[5], bHasSum);
//		    /**�Ƿ���������*/
//		    boolean[] bHasYe = new boolean[] { false };
//
//		    /**�����ֶ�����*/
//		   // String[] arrySumFlds = getSumFld(voQryStruct, bHasYe);
//		    String[] arrySumFlds = new String[]{  "jfybje", "jffbje", "jfbbje"};
//		    /**�����ۼ��еĽ���ֶ�*/
//		    String[] arryLjFlds = getLjValueFlds();
//		    /**�ڳ��Լ������еĽ���ֶ�*/
//		    String[] arryJeFlds = getJeFlds();
//		   
//		    Vector<LineMode> vetLm = new Vector<LineMode>();
//		    /**��ʼ�еĹ�ʽ*/
//		    String sInitFormula = "ZY->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000057")/*@res "�ڳ�"*/ + "\""; //sObjFormal +
//		    String sLoopFormula =
//		        //sDisplayFormal+
//		    "QMSHLYE->UPLINE_QMSHLYE+"
//		        + (bFxFlag ? "JFSHLJE-DFSHLJE;" : "DFSHLJE-JFSHLJE;")
//		        + "QMYBYE->UPLINE_QMYBYE+"
//		        + (bFxFlag ? "JFYBJE-DFYBJE;" : "DFYBJE-JFYBJE;")
//		        + "QMFBYE->UPLINE_QMFBYE+"
//		        + (bFxFlag ? "JFFBJE-DFFBJE;" : "DFFBJE-JFFBJE;")
//		        + "QMBBYE->UPLINE_QMBBYE+"
//		        + (bFxFlag ? "JFBBJE-DFBBJE" : "DFBBJE-JFBBJE");
//		    if (bHasSum) {
//		        for (int i = 0; i < arryLjFlds.length; i++) {
//		            sLoopFormula += ";"
//		                + arryLjFlds[i]
//		                + "->IIF(( (UPLINE_ND<>\"0\") && (UPLINE_ND<>ND)),CUR_"
//		                + arryJeFlds[i]
//		                + ",UPLINE_"
//		                + arryLjFlds[i]
//		                + "+"
//		                + arryJeFlds[i]
//		                + ")";
//		        }
//		    }
//		    String sInitFla = ""; //sObjFormal;
//		    if (bHasYe[0]) {
//		        sInitFla += getInitExp(bFxFlag, arryGroupBys, arryKeys);
//		    }
//		    LineMode lmTemp =
//		        new InitLineMode(
//		            INIT_LINE,
//		            "FLAG=0",
//		            sObj_Key + (sObj_Name.length() > 0 ? ("," + sObj_Name) : ""),
//		            sInitFla,
//		            sInitFormula);
//		    vetLm.addElement(lmTemp);
//		    lmTemp = new LineMode(LOOP_LINE, "FLAG=1", "", sLoopFormula);
//		    vetLm.addElement(lmTemp);
//
//		    /**С��linemode*/
//		    for (int i = arryKeys.length - 1; i >= 0; i--) {
//		        if (arryKeys[i].toLowerCase().indexOf("code") != -1)
//		            continue;
//		        String sKeys = "";
//		        //�����Ʒ��鵫����Ҫ���ϱ�������ͬ�������
//		        int a = i;
//		        for (int j = 0; j <= a; j++) {
//		            if (j != 0) { /**���ǵ�һ��*/
//		                sKeys += ",";
//		            }
//		            sKeys += arryKeys[j];
//		        }
//		        String sFormula = "";
//		        String sTempMonth = "";
//		        if (arryKeys[i].equalsIgnoreCase("NDQJ")) {
//		            String sMonthFla = "";
//		            for (int j = 0; j < arrySumFlds.length; j++) {
//		                arrySumFlds[j] = arrySumFlds[j].toUpperCase();
//		                if (j != 0) {
//		                    sFormula += ";";
//		                    sMonthFla += ",";
//		                    sTempMonth += ",";
//		                }
//		                sFormula += arrySumFlds[j] + "->UPLINE_" + arrySumFlds[j] + "LJ";
//		                sMonthFla += arrySumFlds[j];
//		                sTempMonth += arrySumFlds[j];
//		            }
//		            if (sTempMonth.length() > 0) {
//		                sMonthFla = sTempMonth + "->" + "SUMLINE_" + sMonthFla;
//		            }
//		            sFormula += ";ZY->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000214")/*@res "�����ۼ�"*/ + "\"";
//		            sMonthFla += ";ZY->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000215")/*@res "���ºϼ�"*/+ "\"";
//		            /**���뱾�ºϼ�*/
//		     
//		            lmTemp = new LineMode(APPEND_LINE, "", sKeys, sMonthFla, false);
//		            vetLm.addElement(lmTemp);
//		            /**���ºϼ����*/
//		        } else {
//		            for (int j = 0; j < arrySumFlds.length; j++) {
//		                arrySumFlds[j] = arrySumFlds[j].toUpperCase();
//		                if (j != 0) {
//		                    sFormula += ",";
//		                    sTempMonth += ",";
//		                }
//		                sFormula += arrySumFlds[j];
//		                sTempMonth += arrySumFlds[j];
//		            }
//		            if (sTempMonth.length() > 0) {
//		                sFormula = sTempMonth + "->" + "SUMLINE_" + sFormula;
//		            }
//		            if (arryKeys[i].equalsIgnoreCase("RQ")) {
//		                sFormula += ";" + arryKeys[i] + "->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000216")/*@res "���պϼ�"*/ +"\"";
//		            
//		            } else { /**��Ҫ��ϸ����*/
//		            	String xj =nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000000")/*@res "С��"*/;
//		                    sFormula += ";"
//		                        + arryKeys[i]
//		                        + "->IIF(UPLINE_"
//		                        + arryKeys[i]
//		                        + "=\"0\",\""+ xj + "\",UPLINE_"
//		                        + arryKeys[i]
//		                        + "+\"" + xj +"\")";
//		                    if (i > 0 && arryKeys[i - 1].toLowerCase().indexOf("code") != -1) {
//		                        sFormula += ";"
//		                            + arryKeys[i
//		                            - 1]
//		                            + "->IIF(UPLINE_"
//		                            + arryKeys[i
//		                            - 1]
//		                            + "=\"0\",\"" + xj +"\",UPLINE_"
//		                            + arryKeys[i
//		                            - 1]
//		                            + "+\"" + xj + "\")";
//		                    }
//		             //   }
//		                sFormula += ";" + "QMSHLYE,QMYBYE,QMFBYE,QMBBYE->YESUM_QMSHLYE,QMYBYE,QMFBYE,QMBBYE";
//		            }
//		        }
//		        lmTemp = new LineMode(APPEND_LINE, "", sKeys, sFormula, false);
//		        vetLm.addElement(lmTemp);
//		    }
//		    /**�ϼ�linemode*/
//		    String sFla = "";
//		    String tempFla = "";
//		    for (int j = 0; j < arrySumFlds.length; j++) {
//		        if (j != 0) {
//		            sFla += ",";
//		            tempFla += ",";
//		        }
//		        sFla += arrySumFlds[j];
//		        tempFla += arrySumFlds[j];
//		    }
//		    if (tempFla.length() > 0) {
//		        sFla = tempFla + "->SUMLINE_" + sFla;
//		    }
//		    sFla += ";"  ;
//		    lmTemp = new LineMode(APPEND_LINE, "ZY=" + nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001146")/*@res "�ϼ�"*/, "", sFla);
//		    vetLm.addElement(lmTemp);
//		    LineMode[] lmA = new LineMode[vetLm.size()];
//		    vetLm.copyInto(lmA);
//
//		    return lmA;
//		}

	/**
	 * ���ܣ��õ��ۼ��е��ֶ����� ���ߣ����� ����ʱ�䣺(2001-10-25 18:52:31) ������<|> ����ֵ�� �㷨��
	 * 
	 * @return java.lang.String[]
	 */
//	private String[] getLjValueFlds() {
//		return new String[] {  
// 			"JFSHLJE", "JFYBJE", "JFFBJE", "JFBBJE"
// 
// 
//		};
//	}


	/**
	 * ���ܣ��õ���ѯ��� ���ߣ����� ����ʱ�䣺(2001-7-8 13:36:31) ������int iBillType ����״̬, int
	 * iPoint ���ܽڵ��, int iWldx ��������, Vector vQryCond ��ѯ�����Լ���ѯ����, Vector vOrderby
	 * ��������, ����ֵ��Object[] �ֱ�Ϊ���vo ���������vo�������ʾ���� �㷨�� �쳣������
	 * 
	 * @return java.lang.Object[]
	 */
	public Object[] getResults(ConditionVO voCond, QueryStructVO voStructs)
			throws Exception, NoDataException {
		ConditionVO voConds = new ConditionVO();
		QueryStructVO voStruct = (QueryStructVO) voStructs.clone();
		/** clone����vo */
		voConds = getConditions(voCond, voStructs);
		voConds.setFbFlag(m_bFbFlag);
		if (m_voResults == null) {
			setQryType(voStruct);
			// Vector vCustConds = voConds.getVetCustCond();
			Vector vSumFlds = voConds.getVetSumFields();
			if (vSumFlds != null && vSumFlds.size() > 0) {
				String[] sSumFlds = new String[vSumFlds.size()];
				vSumFlds.copyInto(sSumFlds);
				voStruct.setSumFlds(sSumFlds);
			}
			/** �����ۼƲ�ѯ���ڣ��ڼ䣩 */
			if (StringUtils.isEmpty(voStruct.getDate()[0])) voStruct.getDate()[0] = "1900-01-01";
			if (StringUtils.isEmpty(voStruct.getDate()[1])) voStruct.getDate()[1] = "3000-12-31";
			String[] strYearSumDate = getYearSumDate(voStruct.getDate());
			voStruct.setSumDate(strYearSumDate);
			voStruct.setVetDisplay(PubMethodUI.getVetDisplayFld(voConds));
			voStruct.setVetGroupBy(voConds.getVetGroupByFields());
			voStruct.setVetQryObj(voConds.getVQryObj());
			voStruct.setVetSort(voConds.getVetSortCond());
			long begin = System.currentTimeMillis();
			// RSTransferVO voRs =
			// NewListQueryBO_Client.getQueryResult(voStruct);
			RSTransferVO voRs = ProxyReporter.getIArapListQueryPrivate()
					.getQueryResultDetail(voStruct);
			long end = System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug(
					"���ô�ӡ���ú�̨��ʹ��ʱ�䣺" + (end - begin) + "����");
			m_voResults = null;
			StatValueObject[] vo = manageData(voRs.getResultset(), voStruct,
					voRs.getObjs(), voRs.getDisplays());
			m_voResults = PubMethodUI.convert(vo);
			Log.getInstance(this.getClass()).debug(
					"���ô�ӡǰ̨���㹲ʹ��ʱ�䣺" + (System.currentTimeMillis() - end)
							+ "����");
		}
		return settleData(voConds);
	}

	// private HashMap m_hashBddataVO = new HashMap();//key-value:pk-BddataVO
	// public void setHashBddataVO(HashMap hash) {
	// // m_hashBddataVO = hash;
	// if(m_hashBddataVO==null){
	// m_hashBddataVO=new HashMap();
	// }
	// if(hash==null||hash.isEmpty()){
	// return;
	// }
	// m_hashBddataVO.putAll(hash);
	//	
	// }
	// private HashMap getHashBddataVO() {
	// return m_hashBddataVO;
	// }
	/**
	 * ���ܣ��õ���ѯ�����в��������Ļ����ֶ� ���ߣ����� ����ʱ�䣺(2001-11-8 12:04:24) ������<|> ����ֵ�� �㷨��
	 * �쳣������
	 * 
	 * @return java.lang.String[]
	 * @param voStruct
	 *            nc.vo.arap.pub.QueryStructVO
	 */
	private String[] getSumFld(QueryStructVO voStruct) {
		String[] sFlds = voStruct.getSumFlds();
		Vector<String> vetFlds = new Vector<String>();
		if (sFlds == null || sFlds.length == 0) {
			return new String[0];
		}
		for (int i = 0; i < sFlds.length; i++) {

			vetFlds.addElement(sFlds[i]);
		}
		if (vetFlds.size() > 0) {
			String[] sResuls = new String[vetFlds.size()];
			vetFlds.copyInto(sResuls);
			return sResuls;
		}
		return new String[0];
	}

	/**
	 * ���ܣ��õ���ѯ���Ľ��vo ���ߣ����� ����ʱ�䣺(2001-11-14 11:13:59) ������<|> ����ֵ�� �㷨�� �쳣������
	 * 
	 * @return nc.vo.arap.pub.ClientVO[]
	 */
	public ClientVO[] getVos() {
		return m_voResults;
	}

	/**
	 * ���ܣ��õ������ۼ����������� ���ߣ����� ����ʱ�䣺(2001-7-16 11:02:19) ������<|> ����ֵ�� �㷨�� �쳣������
	 * 
	 * @return java.lang.String[]
	 * @param vetDateCond
	 *            java.util.Vector
	 */
	private String[] getYearSumDate(String[] sDates) {
		if (sDates == null) {
			return null;
		}
		String strDate = sDates[0];
		String strBeginDate = null;
		String strEndDate = null;
		UFDate ufdBeginDate = new UFDate(strDate);
		strEndDate = ufdBeginDate.getDateBefore(1).toString();
		if (m_bDate) {
			strBeginDate = ufdBeginDate.getYear() + "-01-01";
		} else {
			AccountCalendar calendar = AccountCalendar.getInstance();
			try {
				calendar.setDate(ufdBeginDate);
				strBeginDate = calendar.getYearVO().getBegindate().toString();
			} catch (InvalidAccperiodExcetion e) {
				Log.getInstance(PubMethodUI.class).error(e.getMessage(), e);
			}
			// strBeginDate = strDate.substring(0,strDate.indexOf("-"));
			// strBeginDate += "-01";
			// int iyear =
			// Integer.valueOf(strDate.substring(0,strDate.indexOf("-"))).intValue();
			// int iMonth =
			// Integer.valueOf(strDate.substring(strDate.indexOf("-")+1)).intValue();
			// iMonth--;
			// if (iMonth==0){
			// iyear--;
			// iMonth = 12;
			// }
			// strEndDate = iyear + (iMonth>9? ("-"):("-0"))+iMonth;
		}
		return new String[] { strBeginDate, strEndDate };
	}

	/**
	 * ���ܣ����ѯ������������� ���ߣ����� ����ʱ�䣺(2001-8-31 9:40:07) ������vSource ��ѯ���󼯺� ����ֵ�� �㷨��
	 * �쳣������
	 * 
	 * @return java.util.Vector
	 * @param vSource
	 *            java.util.Vector
	 */
	private Vector<QryObjVO> insertRq(Vector<QryObjVO> vSource) {
		if (vSource == null) {
			vSource = new Vector<QryObjVO>();
		}
		QryObjVO newVo = new QryObjVO();
		newVo.setFldorigin(null);
		newVo.setFldtype(new Integer(STRING));
		newVo.setIsSum(ArapConstant.UFBOOLEAN_TRUE);
		newVo.setQryfld("rq");
		newVo.setM_strDisplayName(constdata.ZD_RQ);
		vSource.insertElementAt(newVo, 0);
		return vSource;
	}

	/**
	 * ���ܣ����ü�¼�������������ݵõ�vo���� ���ߣ����� ����ʱ�䣺(2001-10-23 17:38:33) ������ mrs
	 * nc.vo.pub.rs.MemoryResultSet[]������� ����ֵ�� �㷨��
	 * 
	 * @return nc.vo.bd.manage.StateValueObject[]
	 * @param
	 */
	private StatValueObject[] manageData(MemoryResultSet[] mrs,
			QueryStructVO voQryStruct, String[][] strObjs, String[][] strDisplay)
			throws Exception {
		try {
			if (mrs == null) return null;
			long begin = System.currentTimeMillis();
			/** �Ƿ��б����ۼ� */
			boolean bHasSum = voQryStruct.isQuerybyPeriod();
			// if(voQryStruct.getDate()!=null){
			// bHasSum=voQryStruct.getDate()[0].length() == 7 ? true : false;
			// }
			/** �����־ */
			boolean bFxFlag = voQryStruct.getSysFlag()[0] == iApFlag ? false
					: true;
			/** �ڳ��ͱ��ڼ�¼�� */
			long yuchuli = System.currentTimeMillis();
			MemoryResultSet[] mrsSource = null;
		 
 				mrsSource = new MemoryResultSet[] { mrs[0] };
 		 

			Log.getInstance(this.getClass()).debug(
					"####Ԥ����prepareDataʱ�乲:"
							+ (System.currentTimeMillis() - yuchuli));

			long createCodeAndName = System.currentTimeMillis();
			// HashMap hashBddataVO = this.getHashBddataVO();
			PubMethodVO.createCodeAndName(strObjs);
			/* Ϊ��ӡ��ѯ������²�ѯ���� update 2005-01-04 */
			setObjForPrint(PubMethodUI.updateQryObjForPrint(voQryStruct
					.getVetQryObj(), strObjs[5]));
			Log.getInstance(this.getClass()).debug(
					"####createCodeAndNameʱ�乲:"
							+ (System.currentTimeMillis() - createCodeAndName));

			PubMethodVO.insertCodeAndNameColumns(mrsSource, strObjs);
			/** С���ֶ� */
			String[] arryGroupBys = getGroupByFlds(strObjs[1], bHasSum);
			/** �ϲ�ʹ�õ�LineMode */
			long linemode = System.currentTimeMillis();
			LineMode[] lmA = getLineModes(voQryStruct, bFxFlag, bHasSum,
					arryGroupBys, strObjs, strDisplay);
			long linemodeend = System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug(
					"####getLineModesʱ�乲:" + (linemodeend - linemode));

			Log.getInstance(this.getClass()).debug(
					"####linemodeend-beginʱ�乲:" + (linemodeend - begin));

			ResultSetCalute rsc = new ResultSetCalute();
			String sOrderBy = EfficientPubMethod_NEW.array2string(strObjs[5],
					IEfficientPubMethod.COMMA);
			
			boolean bDateFlag = !voQryStruct.isQuerybyPeriod();
			if (bDateFlag) {
				sOrderBy += ",FLAG,RQ";
 			} 
//				else
//				sOrderBy += ",FLAG";
			sOrderBy = sOrderBy.toUpperCase();
			rsc.setSortOrder(sOrderBy);
			rsc.setAppendInitValue(false);

			// mrsSource[0].appendClumnByDefaultValue("FLAG",java.sql.Types.INTEGER,
			// "0");
			mrsSource[0].appendClumnByDefaultValue("FLAG",
					java.sql.Types.INTEGER, "1");

			long tempinsertcolbegin = System.currentTimeMillis();
			;
			Log.getInstance(this.getClass()).debug(
					"####tempinsertcolbegin-linemodeendʱ�乲:"
							+ (tempinsertcolbegin - linemodeend));

			// for (int i = 0; i < strObjs[4].length; i++) {
			// if (strObjs[4][i] != null && strObjs[4][i].length() > 0) {
			// mrsSource[0].setClumnByFormulate(strObjs[5][i],java.sql.Types.VARCHAR,
			// strObjs[4][i]);
			// mrsSource[1].setClumnByFormulate(strObjs[5][i],java.sql.Types.VARCHAR,
			// strObjs[4][i]);
			// }
			// }
			if (strDisplay[4] != null) {
				long t1 = 0, t2 = 0;
				for (int i = 0; i < strDisplay[4].length; i++) {
					if (strDisplay[4][i] != null
							&& strDisplay[4][i].length() > 0) {
						long appendClumnByDefaultValue = System
								.currentTimeMillis();
						mrsSource[0].appendClumnByDefaultValue(
								strDisplay[5][i], java.sql.Types.VARCHAR, "");
						long appendClumnByDefaultValue2 = System
								.currentTimeMillis();
						t1 += appendClumnByDefaultValue2
								- appendClumnByDefaultValue;
						// strDisplay[5]:[djlxda_djlxjc, zb_djbh,
						// bzda_currtypename, fb_fph, zb_vouchid]
						// strDisplay[4][djlxda_djlxjc->getColValue(arap_djlx,djlxjc,djlxoid,djlxda_djlxjcTop),
						// ,
						// bzda_currtypename->getColValue(bd_currtype,currtypename,pk_currtype,bzda_currtypenameTop),
						// , ]
						mrsSource[1].setClumnByFormulate(strDisplay[5][i],
								java.sql.Types.VARCHAR, strDisplay[4][i]);
						long setClumnByFormulate = System.currentTimeMillis();
						Log
								.getInstance(this.getClass())
								.debug(
										"####setClumnByFormulate_"
												+ i
												+ "_"
												+ strDisplay[5][i]
												+ ":"
												+ (setClumnByFormulate - appendClumnByDefaultValue2));
						t2 += setClumnByFormulate - appendClumnByDefaultValue2;
					}
				}
				Log.getInstance(this.getClass()).debug(
						"####appendClumnByDefaultValueʱ�乲:" + (t1));
				Log.getInstance(this.getClass()).debug(
						"####setClumnByFormulate:" + (t2));

			}
			long tempinsertcolend = System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug(
					"####tempinsertcolend-tempinsertcolbegin:"
							+ (tempinsertcolend - tempinsertcolbegin));

			/** ������������С��ʱ�����м���������� */
//			String[] sValues = new String[] { "0", "0", "0", "0" };
//			for (int i = arryGroupBys.length; i >= 0; i--) {
//				String[] sYeFlds = new String[] { "QMSHLYE" + i, "QMYBYE" + i,
//						"QMFBYE" + i, "QMBBYE" + i };
//				int[] types = new int[] { java.sql.Types.DECIMAL,
//						java.sql.Types.DECIMAL, java.sql.Types.DECIMAL,
//						java.sql.Types.DECIMAL };
//				for (int j = 0; j < 1; j++) {
//					mrsSource[j].appendClumnByDefaultValue(sYeFlds, types,
//							sValues);
//				}
//			}
			long end = System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug(
					"####end-tempinsertcolendʱ�乲:" + (end - tempinsertcolend));

			Log.getInstance(this.getClass()).debug("����rs֮ǰ:" + (end - begin));
			rsc.setLineMode(lmA);
			rsc.addResultSet(mrsSource[0]);
			// rsc.addResultSet(mrsSource[1]);
			MemoryResultSet mrsResults = rsc.execute();
			long end1 = System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("����rs��ʱ:" + (end1 - end));
			// ComTool.printResultSet(mrsResults);

			return PubMethodVO.getVOArrayByRS(mrsResults);
		} catch (Exception e) {
			Log.getInstance(this.getClass()).error(e.getMessage(), e);
			throw new Exception(
					"ListQueryBO::manageData(	MemoryResultSet[] mrs,QueryStructVO voQryStruct,String strObjs) Exception!"
							+ e.getMessage());
		}
	}

	/**
	 * ���ܣ�������ݹ������е��������� ���ߣ����� ����ʱ�䣺(2001-7-8 14:31:52) ������ ����ֵ�� �㷨�� �쳣������
	 */
	public void onRemoveAll() {
		m_voResults = null;
		m_bFbFlag = false;
		m_bDate = true;
	}


	/**
	 * ���ܣ������Ƿ����ø��ұ�ʶ ���ߣ����� ����ʱ�䣺(2001-8-7 11:21:47) ������<|> ����ֵ�� �㷨�� �쳣������
	 * 
	 * @param bFlag
	 *            boolean
	 */
	public void setFbFlag(boolean bFlag) {
		m_bFbFlag = bFlag;
	}

	/**
	 * ���ܣ����ò�ѯ��ʽ�����ڻ��ڼ䣩 ���ߣ����� ����ʱ�䣺(2001-7-5 11:36:26) ������<|> ����ֵ�� �㷨�� �쳣������
	 * 
	 * @return java.lang.String[]
	 * @param vetDateCond
	 *            java.util.Vector
	 */
	private void setQryType(QueryStructVO voStruct) {
		m_bDate = !voStruct.isQuerybyPeriod();

	}

	/**
	 * ���ܣ��������ݱ�����ʾ����Ϣ������Ӧ�Ķ��ͷ��Ϣ ���ߣ����� ����ʱ�䣺(2001-7-8 14:42:59) ������vetQryObjs
	 * ��ѯ����voResults ��ѯ������� ����ֵ�� �ֱ𷵻�vo��������ѯ������ݺ���ʾ�� �㷨�� �쳣������
	 * 
	 */
	private Object[] settleData(ConditionVO voConds) {

		Vector<QryObjVO> vObjs = (Vector<QryObjVO>) voConds.getVQryObj()
				.clone();
		if(isDepdetailSel())
		vObjs = insertRq(vObjs);
		
		voConds.setVQryObj(vObjs);
		ReportTools tools = new ReportTools();
		tools.setConditions(voConds);
		tools.setSumFlag(false);
		Object[] oTmp = tools.getDisplayInfos();
		Object[] oResults = new Object[3];
		oResults[0] = m_voResults;
		oResults[1] = oTmp[0];
		oResults[2] = oTmp[1];
		return oResults;
	}
}