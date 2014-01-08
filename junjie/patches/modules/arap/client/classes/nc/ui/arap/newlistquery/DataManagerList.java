package nc.ui.arap.newlistquery;

import java.util.Iterator;
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
import nc.vo.pub.rs.ComTool;
import nc.vo.pub.rs.IResultSetConst;
import nc.vo.pub.rs.InitLineMode;
import nc.vo.pub.rs.LineMode;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.ResultJoinTool;
import nc.vo.pub.rs.ResultSetCalute;
import nc.vo.pub.rs.ResultSumTool;

import org.apache.commons.lang.ArrayUtils;
public class DataManagerList implements PubConstData, IResultSetConst{
	/**查询结果vo数组集合*/
	private ClientVO[] m_voResults = null;
	/**是否启用辅币标识*/
	private boolean m_bFbFlag = false;
	/**日期期间方式 false——期间方式*/
	private boolean m_bDate = true;
	/**用于打印的查询对象*/
	private Vector m_objForPrint = null;
	
	private boolean isShowDaily=true;
	protected IuiConstData constdata = new IuiConstData();
	 RSTransferVO voRs = null;
	 
	public RSTransferVO getRSTransferVO() {
		 return voRs;
	 }
	 /**
     * @return 返回 m_objForPrint。
     */
    public Vector getObjForPrint() {
        return m_objForPrint;
    }
    /**
     * @param forPrint 要设置的 m_objForPrint。
     */
    public void setObjForPrint(Vector forPrint) {
        m_objForPrint = forPrint;
    }
/**
 * DataManagerZZ 构造子注解。
 */
public DataManagerList() {
	super();
}
/**
 *  功能：根据帐页格式和查询条件构造新的查询条件（查询对象和分组条件）
 *  作者：宋涛
 *  创建时间：(2001-8-6 13:52:19)
 *  参数：<|>
 *  返回值：
 *  算法：
 *  异常描述：
 * @return nc.vo.arap.pub.ConditionVO
 * @param voSource nc.vo.arap.pub.ConditionVO
 * @param strBillType java.lang.String
 */
private ConditionVO getConditions(
	ConditionVO voSource,
	QueryStructVO voStructs) {
	ConditionVO newVo = (ConditionVO) voSource.clone();
	if (voSource.getBillType().equals(constdata.ZYGS_WBJE) || voSource.getBillType().equals(constdata.ZYGS_SHLWB)) {
        newVo.getVQryObj().addElement(PubMethodUI.getBzVO());
    }
	if (newVo.getVetGroupByFields() == null) {
		newVo.setVetGroupByFields(new Vector<QryObjVO>());
	}

	if (voStructs.getCorp()!=null&&voStructs.getCorp().length > 1) {
		Vector<QryObjVO> vetObjs =  (Vector<QryObjVO>)newVo.getVQryObj().clone();
		vetObjs.insertElementAt(PubMethodUI.getCorpVO(),0);
		newVo.setVQryObj(vetObjs);
	}
	//for(int i=newVo.getVQryObj().size()-1;i>=0;i--){
	//newVo.getVetGroupByFields().insertElementAt(newVo.getVQryObj().elementAt(i),0);
	//}
	return newVo;
}
/**
 * 功能：得到查询分组字段
 * 作者：宋涛
 * 创建时间：(2001-10-24 11:27:26)
 * 参数：QueryStructVO voQryStruct
 * 返回值：
 * 算法：
 *
 * @return java.lang.String[]
 */
String[] getGroupByFlds(String[] sGrpby,boolean bSum) throws java.sql.SQLException {
	String sGroupBy = EfficientPubMethod_NEW.array2string(sGrpby,IEfficientPubMethod.COMMA);
	if (sGroupBy != null && sGroupBy.length() > 0) {
		sGroupBy += ",";
	} else {
		sGroupBy = "";
	}
	/**月份方式*/
	if (bSum) {
		sGroupBy += "NDQJ,";
	}
	sGroupBy += "RQ";
	sGroupBy = sGroupBy.toUpperCase();
	String[] arryGy = PubMethodVO.getToken(sGroupBy, IEfficientPubMethod.COMMA);
	return arryGy;
}
/**
 * 功能：得到计算余额列小计合计时在初始行中添加的表达式
 * 作者：宋涛
 * 创建时间：(2001-11-15 19:44:39)
 * 参数：<|>
 * 返回值：
 * 算法：
 * 异常描述：
 *
 * @return java.lang.String
 * @param bFxFlag boolean
 * @param arryGroupBy java.lang.String[]
 */
private String getInitExp(boolean bFxFlag, String[] arryGroupBy,String[] arryKeys) {
	String[] sJeName = new String[] { "SHL", "YB", "FB", "BB" }; //,"WD"  QMWDJE wanglei 2013-01-07
	StringBuffer sExp = new StringBuffer();
	for (int i = arryGroupBy.length - 1; i >= 0; i--) {
		for (int k = 0; k < sJeName.length; k++) {
			sExp.append("QM" + sJeName[k] + "YE" + (i + 1) + "->IIF(");
			for (int j = 0; j <= i; j++) {
				if (j != 0) {
					sExp.append(" || ");
				}
				sExp.append("((UPLINEFORCE_");
				sExp.append(arryGroupBy[j]);
				sExp.append("<>\"0\") && (UPLINEFORCE_");
				sExp.append(arryGroupBy[j]);
				sExp.append("<>");
				sExp.append(arryGroupBy[j]);
				sExp.append("))");
			}
			sExp.append(",QM" + sJeName[k] + "YE,");
			sExp.append("UPLINEFORCE_QM" + sJeName[k] + "YE" + (i + 1) + "+");
			sExp.append("QM" + sJeName[k] + "YE");
			sExp.append(");");
		}
	}
	for (int k = 0; k < sJeName.length; k++) {
		sExp.append("QM" + sJeName[k] + "YE0->");
		sExp.append("UPLINEFORCE_QM" + sJeName[k] + "YE0+");
		sExp.append("QM" + sJeName[k] + "YE");
		sExp.append(";");
	}
	//for (int i = 0; i <= arryGroupBy.length; i++) {
	//if (i < arryGroupBy.length - 1) {
	//for (int k = 0; k < sJeName.length; k++) {
	//sExp.append("QM" + sJeName[k] + "YE" + i + "->");
	//sExp.append("UPLINE_QM" + sJeName[k] + "YE" + i + "+");
	//sExp.append("QM" + sJeName[k] + "YE");
	//sExp.append(";");
	//}
	//} else {
	//for (int k = 0; k < sJeName.length; k++) {
	//sExp.append("QM" + sJeName[k] + "YE" + i + "->");
	//sExp.append("QM" + sJeName[k] + "YE");
	//sExp.append(";");
	//}
	//}
	//}
	//Logger.debug("InitExp:" + sExp);
	return sExp.toString();
}
/**
 * 功能：得到查询结果集中的金额字段
 * 作者：宋涛
 * 创建时间：(2001-10-25 18:54:44)
 * 参数：<|>
 * 返回值：
 * 算法：
 *
 * @return java.lang.String[]
 */
private String[] getJeFlds(int ifx) {
	switch(ifx){
		case 3:	
		case 5:
			    return new String[] {
			        "JFSHLJE",
			        "JFYBJE",
			        "JFFBJE",
			        "JFBBJE",
			        "DFSHLJE",
			        "DFYBJE",
			        "DFFBJE",
			        "DFBBJE",
			        "JFYBSJ",
			        "JFFBSJ",
			        "JFBBSJ",
			        "DFYBSJ",
			        "DFFBSJ",
			        "DFBBSJ",
			        "JFYBWSJE",
			        "JFFBWSJE",
			        "JFBBWSJE",
			        "DFYBWSJE",
			        "DFFBWSJE",
			        "DFBBWSJE",
					"QMSHLYE",
			        "QMYBYE",
			        "QMFBYE",
			        "QMBBYE",
			        "WDJE", //wanglei 2014-01-07,
			        //"QMWDYE"
		        };
		case 4:
			return new String[] {
					"JFSHLJE",
					"JFYBJE",
					"JFFBJE",
					"JFBBJE",
					"DFSHLJE",
					"DFYBJE",
					"DFFBJE",
					"DFBBJE",
					"QMSHLYE",
					"QMYBYE",
					"QMFBYE",
					"QMBBYE" ,
					"WDJE" , //wanglei 2014-01-07
					//"QMWDYE"
					};
		default: return null; 
	}
}
/**
 * 功能：得到需要的LineMode
 * 作者：宋涛
 * 创建时间：(2001-10-25 18:42:53)
 * 参数：<|>
 * 返回值：
 * 算法：
 *
 * @return LineMode[]
 */
private LineMode[] getLineModes(
    QueryStructVO voQryStruct,
    boolean bFxFlag,
    boolean bHasSum,
    String[] arryGroupBys,
    String[][] strObj,
    String[][] strDisplay)
    throws java.sql.SQLException {
    /**查询对象字段*/
    String sObj_Name =
    	EfficientPubMethod_NEW.array2string(strObj[5], IEfficientPubMethod.COMMA);
    String sObj_Key =
    	EfficientPubMethod_NEW.array2string(strObj[1], IEfficientPubMethod.COMMA);
    String sObjFormal = EfficientPubMethod_NEW.array2string(strObj[4], ";");
    String sDisplayFormal = EfficientPubMethod_NEW.array2string(strDisplay[4], ";");
    if (sDisplayFormal != null && sObjFormal != null) {
        sDisplayFormal = sObjFormal + ";" + sDisplayFormal;
    }

    /**用为查询对象的查询对象Key*/
    String[] arryKeys = getGroupByFlds(strObj[5], bHasSum);
    /**是否包含余额列*/
    boolean[] bHasYe = new boolean[] { false };

    /**汇总字段数组*/
    String[] arrySumFlds = getSumFld(voQryStruct, bHasYe);
    /**本年累计中的金额字段*/
    String[] arryLjFlds = getLjValueFlds();
    /**期初以及本期中的金额字段*/
    String[] arryJeFlds = getJeFlds(voQryStruct.getIfx());
    /**期末余额中的金额字段*/
    String[] sQMValueFlds = new String[] { "QMSHLYE", "QMYBYE", "QMFBYE", "QMBBYE" };  //,"QMWDYE" wanglei 2013-01-07 骏杰金属增加期末未达金额
    Vector<LineMode> vetLm = new Vector<LineMode>();
    /**初始行的公式*/
    String sInitFormula = "ZY->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000057")/*@res "期初"*/ + "\""; //sObjFormal +
    String sLoopFormula =
        //sDisplayFormal+
    "QMSHLYE->UPLINE_QMSHLYE+"
        + (bFxFlag ? "JFSHLJE-DFSHLJE;" : "DFSHLJE-JFSHLJE;")
        + "QMYBYE->UPLINE_QMYBYE+"
        + (bFxFlag ? "JFYBJE-DFYBJE;" : "DFYBJE-JFYBJE;")
        + "QMFBYE->UPLINE_QMFBYE+"
        + (bFxFlag ? "JFFBJE-DFFBJE;" : "DFFBJE-JFFBJE;")
        + "QMBBYE->UPLINE_QMBBYE+"
        + (bFxFlag ? "JFBBJE-DFBBJE" : "DFBBJE-JFBBJE");
       // + "QMWDYE->UPLINE_QMWDYE+WDJE;"; //wanglei 2013-01-07 骏杰金属增加期末未达金额
    if (bHasSum) {
        for (int i = 0; i < arryLjFlds.length; i++) {
            //sInitFormula += ";" + arryLjFlds[i] + "->CUR_" + arryLjFlds[i];
            sLoopFormula += ";"
                + arryLjFlds[i]
                + "->IIF(( (UPLINE_ND<>\"0\") && (UPLINE_ND<>ND)),CUR_"
                + arryJeFlds[i]
                + ",UPLINE_"
                + arryLjFlds[i]
                + "+"
                + arryJeFlds[i]
                + ")";
        }
    }
    String sInitFla = ""; //sObjFormal;
    if (bHasYe[0]) {
        //sInitFormula +=";"+getAppendInitExp(arryGroupBys);
        //sInitFla += ";" ;
        sInitFla += getInitExp(bFxFlag, arryGroupBys, arryKeys);
        sLoopFormula += ";" + getLoopExp(bFxFlag, arryGroupBys);
    }
    LineMode lmTemp =
        new InitLineMode(
            INIT_LINE,
            "FLAG=0",
            sObj_Key + (sObj_Name.length() > 0 ? ("," + sObj_Name) : ""),
            sInitFla,
            sInitFormula);
    vetLm.addElement(lmTemp);
    lmTemp = new LineMode(LOOP_LINE, "FLAG=1", "", sLoopFormula);
    vetLm.addElement(lmTemp);

    /**小计linemode*/
    for (int i = arryKeys.length - 1; i >= 0; i--) {
    	if(!this.isShowDaily()&&"RQ".equalsIgnoreCase(arryKeys[i]))
    		continue;
        if (arryKeys[i].toLowerCase().indexOf("code") != -1)
            continue;
        String sKeys = "";
        //以名称分组但是需要加上编码区别同名称情况
        int a = i;
        //if ((arryKeys[i].toLowerCase().indexOf("name") != -1)
        //&&(arryKeys[i].toLowerCase().indexOf("corp") == -1))
        //a = i + 1;
        for (int j = 0; j <= a; j++) {
            if (j != 0) { /**不是第一行*/
                sKeys += ",";
            }
            sKeys += arryKeys[j];
            //sKeys +=",";
            //sKeys += arryGroupBys[j];
        }
        String sFormula = "";
        String sTempMonth = "";
        if (arryKeys[i].equalsIgnoreCase("NDQJ")) {
            String sMonthFla = "";
            for (int j = 0; j < arrySumFlds.length; j++) {
                arrySumFlds[j] = arrySumFlds[j].toUpperCase();
                if (j != 0) {
                    sFormula += ";";
                    sMonthFla += ",";
                    sTempMonth += ",";
                }
                //String sPreFix = arrySumFlds[j].substring(0, arrySumFlds[j].length() - 2);
                sFormula += arrySumFlds[j] + "->UPLINE_" + arrySumFlds[j] + "LJ";
                sMonthFla += arrySumFlds[j];
                sTempMonth += arrySumFlds[j];
            }
            if (sTempMonth.length() > 0) {
                sMonthFla = sTempMonth + "->" + "SUMLINE_" + sMonthFla;
            }
            sFormula += ";ZY->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000214")/*@res "本年累计"*/ + "\"";
            sMonthFla += ";ZY->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000215")/*@res "本月合计"*/+ "\"";
            /**插入本月合计*/
            for (int j = 0; j < sQMValueFlds.length; j++) {
                sMonthFla += ";" + sQMValueFlds[j] + "->UPLINE_" + sQMValueFlds[j];
                // + (i + 1);
                sFormula += ";" + sQMValueFlds[j] + "->UPLINE_" + sQMValueFlds[j];
            }
            lmTemp = new LineMode(APPEND_LINE, "", sKeys, sMonthFla, false);
            vetLm.addElement(lmTemp);
            /**本月合计完成*/
        } else {
            for (int j = 0; j < arrySumFlds.length; j++) {
                arrySumFlds[j] = arrySumFlds[j].toUpperCase();
                if (j != 0) {
                    sFormula += ",";
                    sTempMonth += ",";
                }
                sFormula += arrySumFlds[j];
                sTempMonth += arrySumFlds[j];
            }
            if (sTempMonth.length() > 0) {
                sFormula = sTempMonth + "->" + "SUMLINE_" + sFormula;
            }
            if (arryKeys[i].equalsIgnoreCase("RQ")) {
                sFormula += ";" + arryKeys[i] + "->\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000216")/*@res "本日合计"*/ +"\"";
                for (int j = 0; j < sQMValueFlds.length; j++) {
                    sFormula += ";" + sQMValueFlds[j] + "->UPLINE_" + sQMValueFlds[j];
                    // + (i + 1);
                }
            } else { /**需要仔细看看*/
//                if (false) {//arryKeys[i].toLowerCase().indexOf("corp") != -1||
////            arryKeys[i]
////                    .toLowerCase()
////                    .indexOf("ddh")
////                    != -1)
//                    sFormula += ";"
//                        + arryKeys[i]
//                        + "->IIF(UPLINE_"
//                        + arryKeys[i]
//                        + "=\"0\",\"小计\",UPLINE_"
//                        + arryKeys[i]
//                        + "+\"小计\")";
//                else {
            	String xj =nc.ui.ml.NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000000")/*@res "小计"*/;
                    sFormula += ";"
                        + arryKeys[i]
                        + "->IIF(UPLINE_"
                        + arryKeys[i]
                        + "=\"0\",\""+ xj + "\",UPLINE_"
                        + arryKeys[i]
                        + "+\"" + xj +"\")";
                    if (i > 0 && arryKeys[i - 1].toLowerCase().indexOf("code") != -1) {
                        sFormula += ";"
                            + arryKeys[i
                            - 1]
                            + "->IIF(UPLINE_"
                            + arryKeys[i
                            - 1]
                            + "=\"0\",\"" + xj +"\",UPLINE_"
                            + arryKeys[i
                            - 1]
                            + "+\"" + xj + "\")";
                    }
             //   }
                sFormula += ";" + "QMSHLYE,QMYBYE,QMFBYE,QMBBYE->YESUM_QMSHLYE,QMYBYE,QMFBYE,QMBBYE";//QMWDYE wanglei 2013-01-07 骏杰金属增加期末未达金额
            }
        }
        lmTemp = new LineMode(APPEND_LINE, "", sKeys, sFormula, false);
        vetLm.addElement(lmTemp);
    }
    if (voQryStruct.getPaginationConfig() == null) {
    	/**合计linemode*/
        String sFla = "";
        String tempFla = "";
        for (int j = 0; j < arrySumFlds.length; j++) {
            if (j != 0) {
                sFla += ",";
                tempFla += ",";
            }
            sFla += arrySumFlds[j];
            tempFla += arrySumFlds[j];
        }
        if (tempFla.length() > 0) {
            sFla = tempFla + "->SUMLINE_" + sFla;
        }
        sFla += ";" + "QMSHLYE,QMYBYE,QMFBYE,QMBBYE->YESUM_QMSHLYE,QMYBYE,QMFBYE,QMBBYE"; //wanglei 2013-01-07 骏杰金属增加期末未达金额
        lmTemp = new LineMode(APPEND_LINE, "ZY=" + nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001146")/*@res "合计"*/, "", sFla);
        vetLm.addElement(lmTemp);
    }
    
    LineMode[] lmA = new LineMode[vetLm.size()];
    vetLm.copyInto(lmA);

    return lmA;
}
/**
 * 功能：得到累计中的字段名称
 * 作者：宋涛
 * 创建时间：(2001-10-25 18:52:31)
 * 参数：<|>
 * 返回值：
 * 算法：
 *
 * @return java.lang.String[]
 */
private String[] getLjValueFlds() {
	return new String[] {
		"JFSHLJELJ",
		"JFYBJELJ",
		"JFFBJELJ",
		"JFBBJELJ",
		"DFSHLJELJ",
		"DFYBJELJ",
		"DFFBJELJ",
		"DFBBJELJ",
//		"JFYBSJLJ",
//		"JFFBSJLJ",
//		"JFBBSJLJ",
//		"DFYBSJLJ",
//		"DFFBSJLJ",
//		"DFBBSJLJ",
//		"JFYBWSJELJ",
//		"JFFBWSJELJ",
//		"JFBBWSJELJ",
//		"DFYBWSJELJ",
//		"DFFBWSJELJ",
//		"DFBBWSJELJ",
		};
}
/**
 * 功能：得到余额累计在循环行中的表达式
 * 作者：宋涛
 * 创建时间：(2001-11-15 19:10:23)
 * 参数：<|>
 * 返回值：
 * 算法：
 * 异常描述：
 *
 * @return java.lang.String
 * @param bFxFlag boolean
 * @param arryGroupBy java.lang.String[]
 */
private String getLoopExp(boolean bFxFlag, String[] arryGroupBy) {
	return "";
	//String[] sJeName = new String[] { "SHL", "YB", "FB", "BB" };
	//StringBuffer sExp = new StringBuffer();

	//for (int i = arryGroupBy.length - 1; i >= 0; i--) {
		//for (int k = 0; k < sJeName.length; k++) {
			//sExp.append("QM" + sJeName[k] + "YE" + (i + 1) + "->IIF(");
			//for (int j = 0; j <= i; j++) {
				//if (i != arryGroupBy.length - 1 || i != j) {
					//if (j != 0) {
						//sExp.append(" || ");
					//}
					//sExp.append("((UPLINEFORCE_");
					//sExp.append(arryGroupBy[j]);
					//sExp.append("<>\"0\") && (UPLINEFORCE_");
					//sExp.append(arryGroupBy[j]);
					//sExp.append("<>");
					//sExp.append(arryGroupBy[j]);
					//sExp.append("))");
				//}
			//}
			//String sAddExp =
				//(bFxFlag
					//? "JF" + sJeName[k] + "JE-DF" + sJeName[k] + "JE"
					//: "DF" + sJeName[k] + "JE-JF" + sJeName[k] + "JE");
			//sExp.append(",");
			//sExp.append(sAddExp);
			//sExp.append(",UPLINEFORCE_QM" + sJeName[k] + "YE"+(i+1)+"+");
			//sExp.append(sAddExp);
			//sExp.append(");");
		//}
	//}
	//for (int k = 0; k < sJeName.length; k++) {
		//sExp.append("QM" + sJeName[k] + "YE0->");
		//sExp.append("UPLINEFORCE_QM" + sJeName[k] + "YE0+");
		//String sAddExp =
			//(bFxFlag
				//? "JF" + sJeName[k] + "JE-DF" + sJeName[k] + "JE"
				//: "DF" + sJeName[k] + "JE-JF" + sJeName[k] + "JE");
		//sExp.append(sAddExp);
		//sExp.append(";");
	//}
	//Logger.debug("LoopExp:" + sExp);
	//return sExp.toString();
}
/**
 *  功能：得到查询结果
 *  作者：宋涛
 *  创建时间：(2001-7-8 13:36:31)
 *  参数：int iBillType 单据状态,
 		int iPoint 功能节点号,
 		int iWldx 往来对象,
 		Vector vQryCond 查询对象以及查询条件,
 		Vector vOrderby 排序条件,
 *  返回值：Object[] 分别为结果vo 列名、结果vo数组和显示列名
 *  算法：
 *  异常描述：
 * @return java.lang.Object[]
 */
public Object[] getResults(ConditionVO voCond, QueryStructVO voStructs)
    throws Exception, NoDataException {
    ConditionVO voConds = new ConditionVO();
    QueryStructVO voStruct = (QueryStructVO) voStructs.clone();
    /**clone条件vo*/
    voConds = getConditions(voCond, voStructs);
    voConds.setFbFlag(m_bFbFlag);
    if (m_voResults == null) {
        setQryType(voStruct);
        //Vector vCustConds = voConds.getVetCustCond();
        Vector vSumFlds = voConds.getVetSumFields();
        if (vSumFlds != null && vSumFlds.size() > 0) {
            String[] sSumFlds = new String[vSumFlds.size()];
            vSumFlds.copyInto(sSumFlds);
            voStruct.setSumFlds(sSumFlds);
        }
        /**本年累计查询日期（期间）*/
        if (StringUtils.isEmpty(voStruct.getDate()[0])) voStruct.getDate()[0] = "1900-01-01";
		if (StringUtils.isEmpty(voStruct.getDate()[1])) voStruct.getDate()[1] = "3000-12-31";
		String[] strYearSumDate = getYearSumDate(voStruct.getDate());
        voStruct.setSumDate(strYearSumDate);
        voStruct.setVetDisplay(PubMethodUI.getVetDisplayFld(voConds));
        voStruct.setVetGroupBy(voConds.getVetGroupByFields());
        voStruct.setVetQryObj(voConds.getVQryObj());
        voStruct.setVetSort(voConds.getVetSortCond());
        long begin = System.currentTimeMillis();
//        RSTransferVO voRs = NewListQueryBO_Client.getQueryResult(voStruct);
        if(voStruct.getIfx()==PubConstData.ALL) {
        	voStruct.setWanglaiDetail(true);
        	voStruct.setIncludeyffk(true);
        } else voStruct.setWanglaiDetail(false);
        voStruct.setStrBillType(voCond.getBillType());
        if(voStruct.getIfx()==PubConstData.YS)
        	voRs=ProxyReporter.getIArapListQueryPrivate().getQueryResultMxz(voStruct);
        else{
        	voRs = ProxyReporter.getIArapListQueryPrivate().getQueryResultMxz(voStruct);
        }
        if (!voStruct.isRecheck() && !(voCond.getBillType().equals(new IuiConstData().ZYGS_WBJE) || voCond.getBillType().equals(new IuiConstData().ZYGS_SHLWB))) {
        	 Iterator<QryObjVO> iterator = voStruct.getVetQryObj().iterator();
       	    while (iterator.hasNext()) {
       		  QryObjVO qryObjVO = iterator.next();
       		  if ("bzbm".equals(qryObjVO.getQryfld())) {
       			 iterator.remove();
       		  }
       	   }
        }
        long end = System.currentTimeMillis();
        Log.getInstance(this.getClass()).debug("钟悦打印调用后台共使用时间："+(end-begin)+"毫秒");
        m_voResults = null;
        StatValueObject[] vo =
            manageData(voRs.getResultset(), voStruct, voRs.getObjs(), voRs.getDisplays());
        m_voResults = PubMethodUI.convert(vo);
        Log.getInstance(this.getClass()).debug("钟悦打印前台计算共使用时间："+(System.currentTimeMillis()-end)+"毫秒");
    }
    return settleData(voConds);
}
//private HashMap m_hashBddataVO = new HashMap();//key-value:pk-BddataVO
//public void setHashBddataVO(HashMap hash) {
////    m_hashBddataVO = hash;
//	if(m_hashBddataVO==null){
//m_hashBddataVO=new HashMap();
//	}
//	if(hash==null||hash.isEmpty()){
//		return;
//	}
//	m_hashBddataVO.putAll(hash);
//	
//}
//private HashMap getHashBddataVO() {
//    return m_hashBddataVO;
//}
/**
 * 功能：得到查询对象中不包含余额的汇总字段
 * 作者：宋涛
 * 创建时间：(2001-11-8 12:04:24)
 * 参数：<|>
 * 返回值：
 * 算法：
 * 异常描述：
 *
 * @return java.lang.String[]
 * @param voStruct nc.vo.arap.pub.QueryStructVO
 */
private String[] getSumFld(QueryStructVO voStruct,boolean[] bHasYe) {
	String[] sFlds = voStruct.getSumFlds();
	Vector<String> vetFlds = new Vector<String>();
	if (sFlds == null || sFlds.length == 0) {
		return new String[0];
	}
	for (int i = 0; i < sFlds.length; i++) {
		if (sFlds[i].endsWith("ye")) {
			bHasYe[0] = true;
			continue;
		}
		vetFlds.addElement(sFlds[i]);
	}
	
	vetFlds.addElement("wdje"); //wanglei 2014-01-07
	if (vetFlds.size() > 0) {
		String[] sResuls = new String[vetFlds.size()];
		vetFlds.copyInto(sResuls);
		return sResuls;
	}
	return new String[0];
}
/**
 * 功能：得到查询到的结果vo
 * 作者：宋涛
 * 创建时间：(2001-11-14 11:13:59)
 * 参数：<|>
 * 返回值：
 * 算法：
 * 异常描述：
 *
 * @return nc.vo.arap.pub.ClientVO[]
 */
public ClientVO[] getVos() {
	return m_voResults;
}
/**
 *  功能：得到本年累计日期条件。
 *  作者：宋涛
 *  创建时间：(2001-7-16 11:02:19)
 *  参数：<|>
 *  返回值：
 *  算法：
 *  异常描述：
 * @return java.lang.String[]
 * @param vetDateCond java.util.Vector
 */
private String[] getYearSumDate(String[] sDates) {
	if(sDates==null){
		return null;
	}
	String strDate = sDates[0];
	String strBeginDate = null;
	String strEndDate = null;
	UFDate ufdBeginDate = new UFDate(strDate);
	strEndDate = ufdBeginDate.getDateBefore(1).toString();
	if (m_bDate){		
		strBeginDate = ufdBeginDate.getYear()+"-01-01";		
	}else{
		AccountCalendar calendar = AccountCalendar.getInstance();    
	    try {
			calendar.setDate(ufdBeginDate);
			 strBeginDate=calendar.getYearVO().getBegindate().toString();
		} catch (InvalidAccperiodExcetion e) {
			Log.getInstance(PubMethodUI.class).error(e.getMessage(),e);
		}
//		strBeginDate = strDate.substring(0,strDate.indexOf("-"));
//		strBeginDate += "-01";
//		int iyear = Integer.valueOf(strDate.substring(0,strDate.indexOf("-"))).intValue();
//		int iMonth = Integer.valueOf(strDate.substring(strDate.indexOf("-")+1)).intValue();
//		iMonth--;
//		if (iMonth==0){
//			iyear--;
//			iMonth = 12;
//		}
//		strEndDate = iyear + (iMonth>9? ("-"):("-0"))+iMonth;
	}
	return new String[]{strBeginDate,strEndDate};
}
/**
 * 功能：向查询对象中添加日期
 * 作者：宋涛
 * 创建时间：(2001-8-31 9:40:07)
 * 参数：vSource 查询对象集合
 * 返回值：
 * 算法：
 * 异常描述：
 * @return java.util.Vector
 * @param vSource java.util.Vector
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

private boolean isLastPageIndex(QueryStructVO voQryStruct) {
	return voQryStruct.getPaginationConfig() != null && voQryStruct.getPaginationConfig().getPageIndex() == getRSTransferVO().getPageCount();
}

/**
 * 功能：利用记录集工具整理数据得到vo数组
 * 作者：宋涛
 * 创建时间：(2001-10-23 17:38:33)
 * 参数： mrs nc.vo.pub.rs.MemoryResultSet[]结果集合
 * 返回值：
 * 算法：
 *
 * @return nc.vo.bd.manage.StateValueObject[]
 * @param
 */
private StatValueObject[] manageData(
	MemoryResultSet[] mrs,
	QueryStructVO voQryStruct,
	String[][] strObjs,
	String[][] strDisplay)
	throws Exception {
	try {
		/**是否有本年累计*/
		boolean bHasSum = voQryStruct.isQuerybyPeriod();
//		if(voQryStruct.getDate()!=null){
//			bHasSum=voQryStruct.getDate()[0].length() == 7 ? true : false;
//		}
		/**方向标志*/
		boolean bFxFlag = voQryStruct.getSysFlag()[0] == iApFlag ? false : true;
		/**期初和本期记录集*/
		long yuchuli = System.currentTimeMillis();
		MemoryResultSet sumRs = null;
		MemoryResultSet[] mrsSource = mrs;
		if (isLastPageIndex(voQryStruct)) {
			sumRs = mrs[mrs.length - 1];
			mrsSource = (MemoryResultSet[]) ArrayUtils.subarray(mrs, 0, mrs.length - 1);
		}

//		MemoryResultSet[] mrsSource =
//			prepareData(mrs, voQryStruct, bHasSum, strObjs, strDisplay);
		Log.getInstance(this.getClass()).debug("####预处理prepareData时间共:"+(System.currentTimeMillis()-yuchuli));
		
		long createCodeAndName= System.currentTimeMillis();

//		PubMethodVO.createCodeAndName(strObjs);
	    /*为打印查询对象更新查询对象 update 2005-01-04*/
	    setObjForPrint(PubMethodUI.updateQryObjForPrint(voQryStruct.getVetQryObj(),strObjs[5]));
	    Log.getInstance(this.getClass()).debug("####createCodeAndName时间共:"+(System.currentTimeMillis()-createCodeAndName));
//		HashMap hashBddataVO = this.getHashBddataVO();
	 
		/**小计字段*/
		String[] arryGroupBys = getGroupByFlds(strObjs[1], bHasSum);
	
		
		ResultSetCalute rsc = new ResultSetCalute();
		String sOrderBy =
			EfficientPubMethod_NEW.array2string(
				strObjs[5],
				IEfficientPubMethod.COMMA);
		sOrderBy += ",FLAG,RQ";
		if(voQryStruct.getIfx()==PubConstData.YS)//应收款
			sOrderBy += ",clbh,zy";
		sOrderBy = sOrderBy.toUpperCase();
		rsc.setSortOrder(sOrderBy);
		rsc.setAppendInitValue(true);
		prepareDisplay(strDisplay,  mrsSource, strObjs, arryGroupBys);
		mrsSource[0].appendClumnByDefaultValue("FLAG",java.sql.Types.INTEGER, "0");
		mrsSource[1].appendClumnByDefaultValue("FLAG",java.sql.Types.INTEGER, "1");
		LineMode[] lmA =
			getLineModes(voQryStruct, bFxFlag, bHasSum, arryGroupBys, strObjs, strDisplay);
		rsc.setLineMode(lmA);
		rsc.addResultSet(mrsSource[0]);
		rsc.addResultSet(mrsSource[1]);
		MemoryResultSet mrsResults = rsc.execute();
		mrsResults = preparePageSum(sumRs, mrsResults, voQryStruct, strDisplay, strObjs, arryGroupBys);
		PubMethodVO.createCodeAndName(strObjs);
		return PubMethodVO.getVOArrayByRS(mrsResults);
	} catch (Exception e) {
		Log.getInstance(this.getClass()).error(e.getMessage(),e);
		throw new Exception(
			"ListQueryBO::manageData(	MemoryResultSet[] mrs,QueryStructVO voQryStruct,String strObjs) Exception!"
				+ e.getMessage());
	}
}

private MemoryResultSet preparePageSum(MemoryResultSet sumRs, MemoryResultSet pageRs, QueryStructVO voQryStruct, String[][] strDisplay, String[][] strObjs, String[] arryGroupBys) throws Exception {
	if (sumRs != null) {
		prepareDisplay(strDisplay, new MemoryResultSet[] {sumRs}, strObjs, arryGroupBys);
		boolean bFxFlag = voQryStruct.getSysFlag()[0] == iApFlag ? false : true;
		String[] sFlds = new String[] {"SHL", "YB", "FB", "BB"}; //  ,"WD" QMEDYE wanglei 2014-01-07
		for (int i = 0; i < sFlds.length; i++) {
			sumRs.setClumnByFormulate(
				"QM" + sFlds[i] + "YE", java.sql.Types.DECIMAL,
				"QM"
					+ sFlds[i]
					+ "YE+"
					+ (bFxFlag
						? "JF" + sFlds[i] + "JE-DF" + sFlds[i] + "JE"
						: "DF" + sFlds[i] + "JE-JF" + sFlds[i] + "JE"));
		}
		sumRs.appendClumnByDefaultValue("FLAG",java.sql.Types.INTEGER, "0");
		int i = !voQryStruct.isQuerybyPeriod() ? 1 : 3;
		sumRs.setClumnByFormulate(sumRs.getMetaData().getColumnName(i) ,  "\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001146")/*@res "合计"*/ + "\"");
		sumRs.setClumnByFormulate(sumRs.getMetaData().getColumnName(i), "\"" + nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001146")/*@res "合计"*/ + "\"");
		try {
			sumRs.setClumnByFormulate("fb_ddh_code" ,  "\"\"");
			sumRs.setClumnByFormulate("fb_ddh_name", "\"\"");
		} catch(Exception e) {
			
		}
		
		return nc.vo.arap.pub.mrstools.MrsTools.unionAll(pageRs, sumRs);
	}
	return pageRs;
}

private void prepareDisplay(String[][] strDisplay, MemoryResultSet[] mrsSource, String[][] strObjs, String[] arryGroupBys) throws Exception {
//	PubMethodVO.insertCodeAndNameColumns(mrsSource,strObjs);
	/**插入计算余额列小计时保存中间计算结果的列*/
	String[] sValues = new String[] { "0", "0", "0", "0" }; //,"0" QMWDYE wanglei 2014-01-07
	for (int i = arryGroupBys.length; i >= 0; i--) {
		String[] sYeFlds =
			new String[] { "QMSHLYE" + i, "QMYBYE" + i, "QMFBYE" + i, "QMBBYE" + i }; //,"QMWDYE"+i wanglei 2013-01-07 骏杰金属增加期末未达金额
		int[] types = new int[]{java.sql.Types.DECIMAL,java.sql.Types.DECIMAL,java.sql.Types.DECIMAL,java.sql.Types.DECIMAL};
		for (int j = 0; j < mrsSource.length; j++) {
			mrsSource[j].appendClumnByDefaultValue(sYeFlds,types, sValues);
		}
	}
}
/**
 *  功能：清除数据管理类中的已有数据
 *  作者：宋涛
 *  创建时间：(2001-7-8 14:31:52)
 *  参数：
 *  返回值：
 *  算法：
 *  异常描述：
 */
public void onRemoveAll() {
	m_voResults = null;
	m_bFbFlag = false;
	m_bDate = true;
}
/**
 * 功能：预处理数据
 * 作者：宋涛
 * 创建时间：(2001-10-25 16:00:59)
 * 参数：<|>
 * 返回值：
 * 算法：
 *
 * @return nc.vo.pub.rs.MemoryResultSet[]
 */
private MemoryResultSet[] prepareData(
	MemoryResultSet[] mrs,
	QueryStructVO voQryStruct,
	boolean bHasSum,
	String[][] strObj,
	String[][] strDisplay)
	throws java.sql.SQLException {

	
	MemoryResultSet[] mrsResults = new MemoryResultSet[2];

	/**需要计算的数字字段*/
	String[] sValueFlds = getJeFlds(voQryStruct.getIfx());
	
//	String[] sObjFlds = strObj[1];
//	if (!ArrayUtils.isEmpty(strObj[5])) {
//		for (String column : strObj[5]) {
//			sObjFlds = (String[]) ArrayUtils.add(sObjFlds, column);
//		}
//	}

	/**查询对象字段*/
	String sObjKey = EfficientPubMethod_NEW.array2string(strObj[1], IEfficientPubMethod.COMMA);
	String sDisplay =
		EfficientPubMethod_NEW.array2string(strDisplay[1], IEfficientPubMethod.COMMA);
	/**期初*/
	long qichu = System.currentTimeMillis();
	mrsResults[0] = ComTool.sumResultSet(mrs[0], mrs[1], strObj[1], sValueFlds, JOIN_MODE);
	mrs[0]=null;
	mrs[1]=null;
	Log.getInstance(this.getClass()).debug("####预处理期初处理时间:"+(System.currentTimeMillis()-qichu)); 
	
	long benqi = System.currentTimeMillis();
	ResultSumTool sumTool = new ResultSumTool();
	sumTool.setSourceResultSet(mrs[2]);
	mrs[2]=null;
	sumTool.setSumKey(PubMethodVO.getToken(sObjKey + ",zy,clbh," + sDisplay, ","));
	sumTool.setSumValueKey(sValueFlds);
	sumTool.setJoinMode(JOIN_MODE);
	int LJ =10;
	if(voQryStruct.isNew()){
		LJ=3;
	}
	for (int i = 3; i <= LJ; i++) {
		sumTool.addSumResultSet(mrs[i]);
		mrs[i]=null;
	}
	/**本期*/
	mrsResults[1] = sumTool.execute();
	sumTool=null;
	Log.getInstance(this.getClass()).debug("####预处理本期处理时间:"+(System.currentTimeMillis()-benqi)); 
	
	//ComTool.printResultSet(mrsResults[1]);
	long bennianleiji = System.currentTimeMillis();
	if (bHasSum) {
		String[] arryLjValueFlds = getLjValueFlds();
		sumTool = new ResultSumTool();
		sumTool.setSourceResultSet(mrs[mrs.length - 1]);
		mrs[mrs.length - 1]=null;
		sumTool.setSumKey(strObj[1]);
		sumTool.setSumValueKey(arryLjValueFlds);
		sumTool.setJoinMode(JOIN_MODE);
		for (int i = LJ+1; i < mrs.length - 1; i++) {
			sumTool.addSumResultSet(mrs[i]);
			mrs[i]=null;
		}
		MemoryResultSet mrsLj = sumTool.execute();
		//ComTool.printResultSet(mrsLj);
		/**连接本年累计和期初*/
		ResultJoinTool rjt = new ResultJoinTool();
		rjt.setSourceResultSet(mrsResults[0]);
		rjt.addJoinResultSet(mrsLj, strObj[1], arryLjValueFlds);
		mrsResults[0] = rjt.execute();
		//ComTool.printResultSet(mrsResults[0]);
		/**连接本年累计和本期*/
		rjt = new ResultJoinTool();
		rjt.setSourceResultSet(mrsResults[1]);
		rjt.addJoinResultSet(mrsLj, strObj[1], arryLjValueFlds);
		mrsResults[1] = rjt.execute();
		//ComTool.printResultSet(mrsResults[1]);
	}
	sumTool=null;
	Log.getInstance(this.getClass()).debug("####预处理本年累计处理时间:"+(System.currentTimeMillis()-bennianleiji)); 
	return mrsResults;
}
/**
 *  功能：设置是否启用辅币标识
 *  作者：宋涛
 *  创建时间：(2001-8-7 11:21:47)
 *  参数：<|>
 *  返回值：
 *  算法：
 *  异常描述：
 * @param bFlag boolean
 */
public void setFbFlag(boolean bFlag) {
	m_bFbFlag = bFlag;
}
/**
 *  功能：设置查询方式（日期或期间）
 *  作者：宋涛
 *  创建时间：(2001-7-5 11:36:26)
 *  参数：<|>
 *  返回值：
 *  算法：
 *  异常描述：
 * @return java.lang.String[]
 * @param vetDateCond java.util.Vector
 */
private void setQryType(QueryStructVO voStruct) {
	m_bDate = !voStruct.isQuerybyPeriod();

}
/**
 *  功能：整理数据表体显示列信息，和相应的多表头信息
 *  作者：宋涛
 *  创建时间：(2001-7-8 14:42:59)
 *  参数：vetQryObjs 查询对象，voResults 查询结果数据
 *  返回值： 分别返回vo列名、查询结果数据和显示列
 *  算法：
 *  异常描述：

 */
private Object[] settleData(ConditionVO voConds) {
	
	Vector<QryObjVO> vObjs =(Vector<QryObjVO>)voConds.getVQryObj().clone();
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
public boolean isShowDaily() {
	return isShowDaily;
}
public void setShowDaily(boolean isShowDaily) {
	this.isShowDaily = isShowDaily;
}
public void setM_voResults(ClientVO[] results) {
	m_voResults = results;
}
}