package nc.ui.arap.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.ui.fi_print.entry.page.PagePrintInfo;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.vo.arap.func.ProxyReporter;
import nc.vo.arap.pub.ClientVO;
import nc.vo.arap.pub.HeadVO;
import nc.vo.arap.pub.PubConstData;
import nc.vo.arap.pub.QryObjVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.report.QryOrderVO;
public class ReportTools implements PubConstData {
    /** ����ģ�� */
    private ReportBaseClass m_ReportTemplet = null;

    /** ����ģ�������õĳ�ʼ���� */
    private ConditionVO m_initCondVo = null;

    /** ���ڴ��͵Ĳ�ѯ����vo */
    private ConditionVO m_voConds = null;

    /** ÿ�����ֶθ��� */
    private int m_iJeFldNum = 1;

    /** ���ͷ��Ϣ */
    //private Vector m_vFldGroup = null;
    /** �������ֶ�����ʾ�ֶ��е�λ�� */
    private int m_iQcPos = -1;

    private int m_iJfPos = -1;

    private int m_iDfPos = -1;

    private int m_iQmPos = -1;

    private int m_iJePos = -1;

    private int m_iYePos = -1;

    private int m_iJfPos_sj = -1;

    private int m_iDfPos_sj = -1;

    private int m_iJfPos_wsje = -1;

    private int m_iDfPos_wsje = -1;

    /** �ۼƽ���ֶ�����ʾ�ֶ��е�λ�� */
    private int m_iJfljPos = -1;

    private int m_iDfljPos = -1;

    /** ģ�������õĸ�����ֶ���ʾ���� */
    private String m_strQcName = null;

    private String m_strJfName = null;

    private String m_strDfName = null;

    private String m_strJfName_sj = null;

    private String m_strDfName_sj = null;

    private String m_strJfName_wsje = null;

    private String m_strDfName_wsje = null;

    private String m_strQmName = null;

    private String m_strJeName = null;

    private String m_strYeName = null;

    /** �ۼƽ���ֶ���ʾ���� */
    private String m_strJfljName = null;

    private String m_strDfljName = null;

    /** �Ƿ����ø��ұ�ʶ */
    private boolean m_bFbFlag = false;

    /** �Ƿ���ʾ�����ۼƱ�ʶ */
    private boolean m_bSumFlag = false;

    /** ����ֶα��� */
    private String[] m_strJeFld = null;

    /** ����ֶ����� */
    private String[] m_strJeNames = null;

    /** ����ֶε�С��λ�� */
    private int m_iMaxJeDec = 2;

    /** ���С��λ������ */
    private static Hashtable<String,Integer> m_hashJeDec = new Hashtable<String, Integer>();

    /** ��ͷitem��ʾ˳��ȽϹ��� */
    private sun.misc.Compare m_comparer;

    protected IuiConstData constdata = new IuiConstData();

    /**
     * a���ܣ� ����:���� ����ʱ��:(2002-7-15 13:37:02) ����: <|>����ֵ: �㷨:
     */
    public ReportTools() {
        initialize();
    }

    /**
     * ���ܣ��޸Ľ���ֶε���ʾ���ƣ����Ҽ�¼�½���ֶ����ڵ���ʾλ��, �Լ�����ֶ���ģ������ֵ����ʾ���ơ� ���ߣ����� ����ʱ�䣺(2001-8-7
     * 11:17:09) ������voConds nc.vo.arap.pub.ConditionVO ��ѯ����vo ����ֵ�� �㷨�� �쳣������
     */
    private void changeItemName(ConditionVO voConds) {
        for (int i = 0; i < voConds.getVetDisplayFlds().size(); i++) {
            ReportItem item = (ReportItem) voConds.getVetDisplayFlds()
                    .elementAt(i);
            if (item == null || item.getKey() == null) {
                continue;
            }
            if (item != null && item.getKey().equals("qcbbye")) {
                m_iQcPos = item.getShowOrder();
                m_strQcName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("jfbbje")) {
                m_iJfPos = item.getShowOrder();
                m_strJfName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("jfbbsj")) {
                m_iJfPos_sj = item.getShowOrder();
                m_strJfName_sj = item.getName();
                if (m_iJeFldNum != 1
                        || (m_iJeFldNum == 2 && !getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_SHLJE))
                        ) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("jfbbwsje")) {
                m_iJfPos_wsje = item.getShowOrder();
                m_strJfName_wsje = item.getName();
                if (m_iJeFldNum != 1
                        || (m_iJeFldNum == 2 && !getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_SHLJE))
                        ) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("dfbbje")) {
                m_iDfPos = item.getShowOrder();
                m_strDfName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null
                    && item.getKey().equals("dfbbsj")
                    
                    ) {
                m_iDfPos_sj = item.getShowOrder();
                m_strDfName_sj = item.getName();
                if (m_iJeFldNum != 1
                        || (m_iJeFldNum == 2 && !getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_SHLJE))
                        ) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null
                    && item.getKey().equals("dfbbwsje")                    
                            ) {
                m_iDfPos_wsje = item.getShowOrder();
                m_strDfName_wsje = item.getName();
                if (m_iJeFldNum != 1
                        || (m_iJeFldNum == 2 && !getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_SHLJE))
                        ) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("qmbbye")) {
                m_iQmPos = item.getShowOrder();
                m_strQmName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("jfbbjelj")) {
                m_iJfljPos = item.getShowOrder();
                m_strJfljName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("dfbbjelj")) {
                m_iDfljPos = item.getShowOrder();
                m_strDfljName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("bbje")) {
                m_iJePos = item.getShowOrder();
                m_strJeName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            } else if (item != null && item.getKey().equals("bbye")) {
                m_iYePos = item.getShowOrder();
                m_strYeName = item.getName();
                if (m_iJeFldNum != 1) {
                    item.setName(constdata.JE_BB);
                }
            }
        }
    }

    /**
     * ���ܣ��޸Ľ���ֶ�����ʾ�ֶ��е�λ��ֵ�� ���ߣ����� ����ʱ�䣺(2001-8-10 9:59:17) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param iSource
     *            int
     */
    private void changeJePos(int iSource) {
        if (m_iJfPos > iSource) {
            m_iJfPos += m_iJeFldNum - 1;
        }
        if (m_iJfPos_sj > iSource) {
            m_iJfPos_sj += m_iJeFldNum - 1;
        }
        if (m_iJfPos_wsje > iSource) {
            m_iJfPos_wsje += m_iJeFldNum - 1;
        }
        if (m_iDfPos > iSource) {
            m_iDfPos += m_iJeFldNum - 1;
        }
        if (m_iDfPos_sj > iSource) {
            m_iDfPos_sj += m_iJeFldNum - 1;
        }
        if (m_iDfPos_wsje > iSource) {
            m_iDfPos_wsje += m_iJeFldNum - 1;
        }
        if (m_iQmPos > iSource) {
            m_iQmPos += m_iJeFldNum - 1;
        }
        if (m_iQcPos > iSource) {
            m_iQcPos += m_iJeFldNum - 1;
        }
        if (m_iJfljPos > iSource) {
            m_iJfljPos += m_iJeFldNum - 1;
        }
        if (m_iDfljPos > iSource) {
            m_iDfljPos += m_iJeFldNum - 1;
        }
        if (m_iJePos > iSource) {
            m_iJePos += m_iJeFldNum - 1;
        }
        if (m_iYePos > iSource) {
            m_iYePos += m_iJeFldNum - 1;
        }
    }

    /**
     * ���ܣ��޸Ľ���ֶ�����ʾ�ֶ��е�λ��ֵ�� ���ߣ����� ����ʱ�䣺(2001-8-20 9:59:17) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param iSource
     *            int
     */
    private void changeJePos2(int iSource) {
        if (getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_WBJE)
                || getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_JE)) {
            changeJePos(iSource);
        } else {
            if (m_iJfPos > iSource) {
                m_iJfPos += m_iJeFldNum - 2;
            }
            if (m_iJfPos_sj > iSource) {
                m_iJfPos_sj += m_iJeFldNum - 2;
            }
            if (m_iJfPos_wsje > iSource) {
                m_iJfPos_wsje += m_iJeFldNum - 2;
            }
            if (m_iDfPos > iSource) {
                m_iDfPos += m_iJeFldNum - 2;
            }
            if (m_iDfPos_sj > iSource) {
                m_iDfPos_sj += m_iJeFldNum - 2;
            }
            if (m_iDfPos_wsje > iSource) {
                m_iDfPos_wsje += m_iJeFldNum - 2;
            }
            if (m_iQmPos > iSource) {
                m_iQmPos += m_iJeFldNum - 2;
            }
            if (m_iQcPos > iSource) {
                m_iQcPos += m_iJeFldNum - 2;
            }
            if (m_iJfljPos > iSource) {
                m_iJfljPos += m_iJeFldNum - 2;
            }
            if (m_iDfljPos > iSource) {
                m_iDfljPos += m_iJeFldNum - 2;
            }
            if (m_iJePos > iSource) {
                m_iJePos += m_iJeFldNum - 2;
            }
            if (m_iYePos > iSource) {
                m_iYePos += m_iJeFldNum - 2;
            }
        }

    }

    /**
     * a���ܣ� ����:���� ����ʱ��:(2002-7-15 14:17:42) ����: <|>����ֵ: �㷨:
     *
     * @return sun.misc.Compare
     */
    private  sun.misc.Compare getComparer() {
        if (m_comparer == null) {
            m_comparer = new Comparer();
        }
        return m_comparer;
    }

    /**
     * ���ܣ��õ�vo ���ߣ����� ����ʱ�䣺(2001-9-28 8:35:28) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param newConds
     *            nc.vo.arap.pub.ConditionVO
     */
    private ConditionVO getConditions() {
        return m_voConds;
    }

    /**
     * ���ܣ��õ���������vo����õ�����ʾ��Ϣ�� ��������items,�����з�����Ϣ������ͷ��Ϣ�� �ͱ�ͷitems��Ϣ ���ߣ�����
     * ����ʱ�䣺(2001-9-28 8:42:22) ������ ����ֵ����ʾ��Ϣ����[0]����items[1]�������ͷ��Ϣ[2]��ͷitems��Ϣ
     * �㷨�� �쳣������
     *
     * @return java.lang.Object[]
     */
    public Object[] getDisplayInfos() {
    	try{
        setFbFlag(getConditions().getFbFlag());
        /** ���ý���ֶ� */
        setJeField(getConditions().getBillType());
        settleData(getConditions());
        Object[] oResults = new Object[3];
        Vector vetFlds = getConditions().getVetDisplayFlds();
        ReportItem[] items = new ReportItem[vetFlds.size()];
        vetFlds.copyInto(items);
        oResults[0] = items;
        Vector vetFldGroups = getConditions().getVetFldGroupbyCond();
        FldgroupVO[] Flds = new FldgroupVO[vetFldGroups.size()];
        vetFldGroups.copyInto(Flds);
        oResults[1] = Flds;

        //Vector vetFlds = voConds.getVetDisplayFlds();
        //ReportItem[] items = new ReportItem[vetFlds.size()];
        //vetFlds.copyInto(items);
        //m_Results[1] = items;
        //FldgroupVO[] Flds = new FldgroupVO[m_vFldGroup.size()];
        //m_vFldGroup.copyInto(Flds);
        //m_Results[2] = Flds;

        return oResults;
    	}catch(Exception e){
    		Log.getInstance(this.getClass()).error(e.getMessage(),e);
    	}
    	return null;
    }

    /**
     * ���ܣ��õ�ģ���е����õ�����vo ���ߣ����� ����ʱ�䣺(2001-9-28 10:18:54) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @return nc.vo.arap.pub.ConditionVO
     */
    private ConditionVO getInitCondVo() {
        if (m_initCondVo == null) {
            m_initCondVo = new ConditionVO();
            m_initCondVo.setBillType(constdata.ZYGS_JE);
        }
        return m_initCondVo;
    }

    /**
     * ���ܣ��õ����ֵ�С��λ�� ���ߣ����� ����ʱ�䣺(2002-1-21 9:09:07) ������ <|>����ֵ�� �㷨��
     *
     * @return int
     */
    private int getMaxCurrDec() {
        return m_iMaxJeDec;
    }

    /**
     * ���ܣ����ݴ���ı���pk�õ�������Ӧ���ֵ����С��λ�� ����������Ϊ�գ��������б�����С��λ������һ�� ���ߣ�����
     * ����ʱ�䣺(2002-1-21 9:05:16) ������ <|>����ֵ�� �㷨��
     *
     * @param CurrType
     *            java.lang.String[]
     */
    public static int getMaxJeDec(String[] sCurrType) {
        int iMaxJeDec = 0;
        try {
            String sKey = "";
            if (sCurrType == null || sCurrType.length == 0) {
                sKey = "--All--";
            } else {
                for (int i = 0; i < sCurrType.length; i++) {
                    sKey += "," + sCurrType[i];
                }
            }
            Object oValue = m_hashJeDec.get(sKey);
            if (oValue != null) {
                iMaxJeDec = ((Integer) oValue).intValue();
            } else {
//                iMaxJeDec = PubBO_Client.getMaxCurrLength(sCurrType);
            	iMaxJeDec = ProxyReporter.getIArapQueryDocInfoPublic().getMaxCurrLength(sCurrType);
                m_hashJeDec.put(sKey, new Integer(iMaxJeDec));
            }
        } catch (Exception e) {
        	Logger.error(e.getMessage(),e);
        }
        return iMaxJeDec;
    }

    /**
     * ���ܣ����ݴ���ı���pk�õ�������Ӧ���ֵ����С��λ�� ����������Ϊ�գ��������б�����С��λ������һ�� ���ߣ�����
     * ����ʱ�䣺(2002-1-21 9:05:16) ������ <|>����ֵ�� �㷨��
     *
     * @param CurrType
     *            java.lang.String[]
     */
    public static int getMaxMnyDec(String sCurrType) {
        if (sCurrType != null && sCurrType.length() > 0) {
            return getMaxJeDec(new String[] { sCurrType });
        } else {
            return getMaxJeDec(null);
        }
    }

    /**
     * ���� ReportBaseClass ����ֵ��
     *
     * @return nc.ui.pub.report.ReportBaseClass
     */
    /* ���棺�˷������������ɡ� */
    private ReportBaseClass getReportBase() {
        return m_ReportTemplet;
    }

    /**
     * ���ܣ��ӱ���ģ����ȡ����ʾ�ֶ���Ϣ ���ߣ����� ����ʱ�䣺(2001-8-3 10:07:11) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    private void getTempletBodyFlds() {
        ReportItem[] voFlds = getReportBase().getBody_Items();
        voFlds = sortItems(voFlds);
        Vector<ReportItem> vetBodyDisplayFld = new Vector<ReportItem>();
        if (voFlds != null && voFlds.length > 0) {
            int j = 1;
            for (int i = 0; i < voFlds.length; i++) {
                voFlds[i] = setItemDecDigits(voFlds[i], getMaxCurrDec());
                if (voFlds[i].getShowOrder() != -1) {
                    voFlds[i].setShowOrder(j++);
                }
                vetBodyDisplayFld.addElement(voFlds[i]);
            }
        }
        getInitCondVo().setVetDisplayFlds(vetBodyDisplayFld);
    }

    /**
     * ���ܣ��õ���ѯģ�������õ���Ϣ ���ߣ����� ����ʱ�䣺(2001-9-14 13:23:08) ������
     * <|>����ֵ�ֱ�Ϊ��[�����ֶΡ����ͷ�ֶΡ������ֶΡ���ͷ�ֶΡ������ֶ�] ����ֵ�в����������ֶΣ���Ϊ����ģ���л����ֶβ�����ͬ
     *
     * ����ֵ�� �㷨�� �쳣������
     *
     * @return Vector
     */
    public ConditionVO getTempletData() {
        //Vector vResult = new Vector();
        //vResult.addElement(getInitCondVo().getVetDisplayFlds());
        //vResult.addElement(getInitCondVo().getVetFldGroupbyCond());
        //vResult.addElement(getInitCondVo().getVetGroupByFields());
        //vResult.addElement(getInitCondVo().getVetHeadFlds());
        //vResult.addElement(getInitCondVo().getVetSortCond());
        //return vResult;
        return getInitCondVo();
    }

    /**
     * ���ܣ��ӱ���ģ����ȡ�ö��ͷ��Ϣ ���ߣ����� ����ʱ�䣺(2001-8-6 9:01:39) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    private void getTempletFldGroup() {
        FldgroupVO[] voFldGroups = getReportBase().getFieldGroup();
        Vector<FldgroupVO> vetFldGroup = new Vector<FldgroupVO>();
        if (voFldGroups != null && voFldGroups.length > 0) {
            for (int i = 0; i < voFldGroups.length; i++) {
                vetFldGroup.addElement(voFldGroups[i]);
            }
        }
        getInitCondVo().setVetFldGroupybyCond(vetFldGroup);
    }

    /**
     * ���ܣ��ӱ���ģ���еõ�С������Ϣ ���ߣ����� ����ʱ�䣺(2001-8-3 10:11:25) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    private void getTempletGroupby() {
        QryOrderVO[] voGroupby = getReportBase().getGroupVOs();
        Vector<QryObjVO> vetGroupby = new Vector<QryObjVO>();
        if (voGroupby != null && voGroupby.length > 0) {
            for (int i = 0; i < voGroupby.length; i++) {
                QryObjVO voTmp = new QryObjVO();
                voTmp.setFldorigin(voGroupby[i].getFldorigin());
                voTmp.setQryfld(voGroupby[i].getQryfld());
                vetGroupby.addElement(voTmp);
            }
        }
        getInitCondVo().setVetGroupByFields(vetGroupby);
    }

    /**
     * ���ܣ�ȡ�ñ���ģ�������õı�ͷ��Ϣ ���ߣ����� ����ʱ�䣺(2001-8-10 15:14:11) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    private void getTempletHeadFlds() {
        ReportItem[] voFlds = getReportBase().getHead_Items();
        Vector<ReportItem> vetHeadFlds = new Vector<ReportItem>();
        if (voFlds != null && voFlds.length > 0) {
            for (int i = 0; i < voFlds.length; i++) {
                if (voFlds[i].getKey().indexOf("qryobj") != -1) {
                    voFlds[i].setShow(false);
                }
                vetHeadFlds.addElement(voFlds[i]);
            }
        }
        getReportBase().setHead_Items(voFlds);
        getInitCondVo().setVetHeadFlds(vetHeadFlds);
    }

    /**
     * ���ܣ��ӱ���ģ���еõ�����������Ϣ ���ߣ����� ����ʱ�䣺(2001-8-3 10:57:33) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    private void getTempletOrderby() {
        QryOrderVO[] voOrderby = getReportBase().getOrderVOs();
        Vector<QryOrderVO> vetOrderby = new Vector<QryOrderVO>();
        if (voOrderby != null && voOrderby.length > 0) {
            for (int i = 0; i < voOrderby.length; i++) {
                vetOrderby.addElement(voOrderby[i]);
            }
        }
        getInitCondVo().setVetSortCond(vetOrderby);
    }

    /**
     * ���ܣ���ʼ����������Ϣ ���ߣ����� ����ʱ�䣺(2001-6-25 10:56:05) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    private void getTempletSumFields() {
        String[] strFieldNames = getReportBase().getSums();
        if (strFieldNames == null || strFieldNames.length == 0) {
            getInitCondVo().setVetSumFields(new Vector());
            return;
        }
        Vector<String> vetSumFields = new Vector<String>();
        for (int i = 0; i < strFieldNames.length; i++) {
            String[] strFields = new String[] { strFieldNames[i] };
            if (strFieldNames[i].equalsIgnoreCase("qcbbye")) {
                strFields = new String[] { "qcshlye", "qcybye", "qcfbye",
                        "qcbbye" };
            } else if (strFieldNames[i].equalsIgnoreCase("jfbbje")) {
                strFields = new String[] { "jfshlje", "jfybje", "jffbje",
                        "jfbbje" };
            } else if (strFieldNames[i].equalsIgnoreCase("dfbbje")) {
                strFields = new String[] { "dfshlje", "dfybje", "dffbje",
                        "dfbbje" };
            } else if (strFieldNames[i].equalsIgnoreCase("qmbbye")) {
                strFields = new String[] { "qmshlye", "qmybye", "qmfbye",
                        "qmbbye" };
            } else if (strFieldNames[i].equalsIgnoreCase("bbye")) {
                strFields = new String[] { "shlye", "ybye", "fbye", "bbye" };
            } else if (strFieldNames[i].equalsIgnoreCase("bbje")) {
                strFields = new String[] { "shlje", "ybje", "fbje", "bbje" };
            } else if (strFieldNames[i].equalsIgnoreCase("jfbbsj")) {
                strFields = new String[] { "jfybsj", "jffbsj", "jfbbsj" };
            } else if (strFieldNames[i].equalsIgnoreCase("dfbbsj")) {
                strFields = new String[] { "dfybsj", "dffbsj", "dfbbsj" };
            } else if (strFieldNames[i].equalsIgnoreCase("jfbbwsje")) {
                strFields = new String[] { "jfybwsje", "jffbwsje", "jfbbwsje" };
            } else if (strFieldNames[i].equalsIgnoreCase("dfbbwsje")) {
                strFields = new String[] { "dfybwsje", "dffbwsje", "dfbbwsje" };
            }
            if (strFields == null || strFields.length == 0) {
                continue;
            }
            for (int j = 0; j < strFields.length; j++) {
                vetSumFields.addElement(strFields[j]);
            }
        }
        getInitCondVo().setVetSumFields(vetSumFields);
        //return vetSumFields;
    }

    /**
     * ���ܣ����ñ�ͷ��Ŀ����󳤶� ����:���� ����ʱ��:(2002-6-28 13:20:56) ����: <|>����ֵ: �㷨:
     */
    private void initHeadItem(Vector vObj) {
        ReportItem[] items = m_ReportTemplet.getHead_Items();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                m_ReportTemplet.setMaxLenOfHeadItem(items[i].getKey(), 50);
                if (items[i].getKey().indexOf("qryobj") != -1 && vObj != null) {
                    boolean bFind = false;
                    for (int j = 0; j < vObj.size(); j++) {
                        if (items[i].getKey().equalsIgnoreCase("qryobj" + j)) {
                            items[i].setName(((QryObjVO) vObj.elementAt(j))
                                    .getM_strDisplayName());
                            //items[i].setShowOrder(Math.abs(items[i].getShowOrder()));
                            items[i].setShow(true);
                            bFind = true;
                            break;
                        }
                    }
                    if (!bFind) {
                        //items[i].setShowOrder(Math.abs(items[i].getShowOrder())
                        // * -1);
                        items[i].setShow(false);
                    }

                }
            }
        }
        m_ReportTemplet.setHead_Items(items);
    }

    /**
     * ��ʼ���ࡣ
     */
    /* ���棺�˷������������ɡ� */
    private void initialize() {
        try {
            m_voConds = new ConditionVO();

        } catch (java.lang.Throwable e) {
        	Log.getInstance(this.getClass()).error(e.getMessage(),e);
        }
        // user code begin {2}
        // user code end
    }

    /**
     * ��ʼ��ģ�� �������ڣ�(01-7-7 14:34:21)
     */
    public void initTemplet() {
        getTempletBodyFlds();
        getTempletFldGroup();
        getTempletGroupby();
        getTempletHeadFlds();
        getTempletOrderby();
        getTempletSumFields();
    }

    /**
     * ���ܣ�����ʾ���еĽ���зֽ��Ϊ��Ӧ��ҳ��ʽ����Ҫ���ֶΣ����������ͷ ���ߣ����� ����ʱ�䣺(2001-8-7 14:16:36) ������
     * <|>����ֵ�� �㷨�� �쳣������
     *
     * @param sFldGroupName
     *            java.lang.String
     */
    private void insertCols(String sFldGroupName, ConditionVO voConds) {
    	Vector<ReportItem> vetItems = voConds.getVetDisplayFlds();
        int ibbPos = 0;
        String strPrefix = null;
        String strSuffix = null;
        String[] strJeFld = null;
        String[] strJeNames = null;
        strJeFld = m_strJeFld;
        strJeNames = m_strJeNames;

        if (sFldGroupName.equals(constdata.ZD_QCYE)) {
            ibbPos = m_iQcPos;
            strPrefix = "qc";
            strSuffix = "ye";
            sFldGroupName = m_strQcName;
        } else if (sFldGroupName.equals(constdata.ZD_JFJE)) {
            ibbPos = m_iJfPos;
            strPrefix = "jf";
            strSuffix = "je";
            sFldGroupName = m_strJfName;
        } else if (sFldGroupName.equals(constdata.ZD_DFJE)) {
            ibbPos = m_iDfPos;
            strPrefix = "df";
            strSuffix = "je";
            sFldGroupName = m_strDfName;
        } else if (sFldGroupName.equals(constdata.ZD_QMYE)) {
            strPrefix = "qm";
            strSuffix = "ye";
            ibbPos = m_iQmPos;
            sFldGroupName = m_strQmName;
        } else if (sFldGroupName.equals(constdata.ZD_JFLJ)) {
            strPrefix = "jf";
            strSuffix = "jelj";
            ibbPos = m_iJfljPos;
            sFldGroupName = m_strJfljName;
        } else if (sFldGroupName.equals(constdata.ZD_DFLJ)) {
            strPrefix = "df";
            strSuffix = "jelj";
            ibbPos = m_iDfljPos;
            sFldGroupName = m_strDfljName;
        } else if (sFldGroupName.equals(constdata.ZD_JE)) {
            strPrefix = "";
            strSuffix = "je";
            ibbPos = m_iJePos;
            sFldGroupName = m_strJeName;
        } else if (sFldGroupName.equals(constdata.ZD_YE)) {
            strPrefix = "";
            strSuffix = "ye";
            ibbPos = m_iYePos;
            sFldGroupName = m_strYeName;
        } else if (sFldGroupName.equals(constdata.ZD_JFSJ)) {
            ibbPos = m_iJfPos_sj;
            strPrefix = "jf";
            strSuffix = "sj";
            sFldGroupName = m_strJfName_sj;
            String[][] str = removeShuLiao(strJeNames, strJeFld);
            strJeNames = str[1];
            strJeFld = str[0];
        } else if (sFldGroupName.equals(constdata.ZD_JFWSJE)) {
            ibbPos = m_iJfPos_wsje;
            strPrefix = "jf";
            strSuffix = "wsje";
            sFldGroupName = m_strJfName_wsje;
            String[][] str = removeShuLiao(strJeNames, strJeFld);
            strJeNames = str[1];
            strJeFld = str[0];
        } else if (sFldGroupName.equals(constdata.ZD_DFSJ)) {
            ibbPos = m_iDfPos_sj;
            strPrefix = "df";
            strSuffix = "sj";
            sFldGroupName = m_strDfName_sj;
            String[][] str = removeShuLiao(strJeNames, strJeFld);
            strJeNames = str[1];
            strJeFld = str[0];
        } else if (sFldGroupName.equals(constdata.ZD_DFWSJE)) {
            ibbPos = m_iDfPos_wsje;
            strPrefix = "df";
            strSuffix = "wsje";
            sFldGroupName = m_strDfName_wsje;
            String[][] str = removeShuLiao(strJeNames, strJeFld);
            strJeNames = str[1];
            strJeFld = str[0];
        }
        String strItem1 = null;
        String strItem2 = null;
        Vector<FldgroupVO> vFldGroup = voConds.getVetFldGroupbyCond();
        for (int i = 0; i < vetItems.size(); i++) {
            ReportItem item = (ReportItem) vetItems.elementAt(i);
            if (item.getShowOrder() == ibbPos) {
                //if(m_isShl && sFldGroupName.equals(ZD_JFSJ)){
                //String [] temp = new String[m_strJeFld.length-1];
                //for(int k=0;k<temp.length;k++){
                //temp[k] = m_strJeFld[k+1];
                //}
                //m_strJeNames
                //m_strJeFld = temp;
                //}
                PubMethodUI.changeFldGroupPos(vFldGroup, ibbPos,
                        strJeFld.length - 1);
                for (int j = 0; j < strJeFld.length - 1; j++) {
                    //if(j==0 && m_isShl && sFldGroupName.equals(ZD_JFSJ)){
                    //continue;
                    //}
                    strItem2 = String.valueOf(ibbPos);
                    ReportItem newItem = ConditionVO.cloneReportItem(item);
                    newItem.setShowOrder(ibbPos);
                    newItem.setKey(strPrefix + strJeFld[j] + strSuffix);
                    newItem.setName(strJeNames[j]);
                    vetItems = PubMethodUI.insertCol(vetItems, ibbPos, newItem);
                    ibbPos++;
                    if (strItem1 != null && strItem2 != null) {
                        //������ͷ��Ϣ
                        vFldGroup = PubMethodUI.insertFldGroup(vFldGroup,
                                sFldGroupName, strItem1, strItem2, false);
                        strItem2 = sFldGroupName;
                    }
                    strItem1 = strItem2;
                    strItem2 = null;
                }
                if (strJeFld.length > 1) {
                    strItem2 = String.valueOf(ibbPos);
                    vFldGroup = PubMethodUI.insertFldGroup(vFldGroup,
                            sFldGroupName, strItem1, strItem2, true);
                }
                break;
            }
        }
        voConds.setVetFldGroupybyCond(vFldGroup);
        voConds.setVetDisplayFlds(vetItems);

    }

    /**
     * ���ܣ�����ʾ����Ϣ����������ۼƽ��(����)�ֶ���Ϣ, ͬʱ����������صĶ��ͷ��Ϣ�� ���ߣ����� ����ʱ�䣺(2001-8-10
     * 9:14:34) ��������ѯ����vo ����ֵ�� �㷨�� �쳣������
     *
     * @param voConds
     *            nc.vo.arap.pub.ConditionVO
     */
    private void insertLjItem(ConditionVO voConds) {
    	Vector<ReportItem> vItems = voConds.getVetDisplayFlds();
        int iSysCode = voConds.getISysCode();
        int iWldx = voConds.getIWldx();
        Vector<FldgroupVO> vFldGroup = voConds.getVetFldGroupbyCond();
        for (int i = 0; i < vItems.size(); i++) {
            ReportItem item = (ReportItem) vItems.elementAt(i);
            if (item.getKey().equals("jfbbje")) {
                ReportItem newItem = new ReportItem();
                newItem = ConditionVO.cloneReportItem(item);
                newItem.setKey("jfbbjelj");
                if (iSysCode == 3 && iWldx < 2) {
                    newItem.setName(constdata.ZD_LJYS);
                } else if (iSysCode == 4 && iWldx < 2) {
                    newItem.setName(constdata.ZD_LJFK);
                } else {
                    newItem.setName(constdata.ZD_JFLJ);
                }
                vItems = PubMethodUI.insertCol(vItems, item.getShowOrder() + 1,
                        newItem);
                vFldGroup = PubMethodUI.changeFldGroupPos(vFldGroup, i, 1);
            }
            if (item.getKey().equals("dfbbje")) {
                ReportItem newItem = new ReportItem();
                newItem = ConditionVO.cloneReportItem(item);
                newItem.setKey("dfbbjelj");
                if (iSysCode == 3 && iWldx < 2) {
                    newItem.setName(constdata.ZD_LJSK);
                } else if (iSysCode == 4 && iWldx < 2) {
                    newItem.setName(constdata.ZD_LJYF);
                } else {
                    newItem.setName(constdata.ZD_DFLJ);
                }
                vItems = PubMethodUI.insertCol(vItems, item.getShowOrder() + 1,
                        newItem);
                vFldGroup = PubMethodUI.changeFldGroupPos(vFldGroup, i, 1);
            }
        }
        voConds.setVetFldGroupybyCond(vFldGroup);
        voConds.setVetDisplayFlds(vItems);
    }
 

    /**
     * ���ܣ��������ѯ�����ʾ�ڽ����� ���ߣ����� ����ʱ�䣺(2001-7-8 12:47:54) ������Object[] oHeads
     * ��ͷ��Ϣ,Object[] oBodys ������Ϣ �����ݽṹ��ͬ �ֱ��Ų�ѯ�����vo��ʾ����Ϣ�Ͷ��ͷ��Ϣ ����ֵ�� �㷨�� �쳣������
     *
     * @param oResults
     *            java.lang.Object[]
     */
    public void onDisplayResult(Object oHeads, Object[] oBodys) {
        if (oHeads != null) { //��ʾ��ͷ��Ϣ
            onShowHead(oHeads);
        }

        Map<String,String> keys=new HashMap<String, String>();
        if (oHeads instanceof HeadVO) {
            HeadVO vo = null;
            vo = (HeadVO) oHeads;
            Object obj = vo.getData().getAttributeValue(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060504","UPP20060504-000218")/*@res "��ʾƾ֤��"*/);
            Vector<ReportItem> vect = new Vector<ReportItem>();
            if (obj != null && obj.toString().equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060504","UPP20060504-000189")/*@res "��"*/)) {
                ReportItem[] items = (ReportItem[]) oBodys[1];
                for (int i = 0; i < items.length; i++) {
                    if (items[i]!=null&&items[i].getKey()!=null&&!items[i].getKey().equalsIgnoreCase("vouchid")) {
                    	if((items[i].getKey().equals("fb.bzbm") || items[i].getKey().equals("bzmc")) && keys.containsKey("fb_bzbm_name")){
                    		continue;
                    	}
                    	if(!keys.containsKey(items[i].getKey()) ){
	                        vect.add(items[i]);
	                        keys.put(items[i].getKey(),items[i].getKey());
                    	}
                    } else {
                        if (oBodys[2] != null) {
                            FldgroupVO[] fldvos = (FldgroupVO[]) oBodys[2];
                            Vector<FldgroupVO> vectFldvo = new Vector<FldgroupVO>();
                            for (int j = 0; j < fldvos.length; j++) {
                                vectFldvo.add(fldvos[j]);
                            }
                            nc.ui.arap.pub.PubMethodUI.changeFldGroupPos(
                                    vectFldvo, items[i].getPos(), -1);
                        }

                    }
                }
                ReportItem[] newItems = new ReportItem[vect.size()];
                vect.copyInto(newItems);
                oBodys[1] = newItems;
            }

        }

        if (oBodys != null) { //��ʾ������Ϣ
            onShowBody(oBodys);
        }
    }

    /**
     * ���ܣ�����������ݣ�׼����һ�β�ѯ�� ���ߣ����� ����ʱ�䣺(2001-8-6 12:36:36) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    public void onRemoveData() {
        m_iJeFldNum = 1;
        //m_vFldGroup = null;
        m_iQcPos = -1;
        m_iJfPos = -1;
        m_iDfPos = -1;
        m_iQmPos = -1;
        m_iJePos = -1;
        m_iYePos = -1;
        m_iJfPos_sj = -1;
        m_iDfPos_sj = -1;
        m_iJfPos_wsje = -1;
        m_iDfPos_wsje = -1;
    }

    /**
     * ���ܣ���ʾ��ͷ��Ϣ ���ߣ����� ����ʱ�䣺(2001-9-21 13:35:09) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param oResults
     *            java.lang.Object[]
     */
    protected void onShowBody(Object[] oResults) {
        getReportBase().setFieldGroup((FldgroupVO[]) oResults[2]);
        ReportItem[] items = (ReportItem[]) oResults[1];
        //items = PubMethodUI.sortItems(items);
        items = sortItems(items);
        items = preDealItems(items);
        getReportBase().setBody_Items(items);
        getReportBase().getBillTable().removeSortListener();
        
        getReportBase().setShowNO(true);  //wanglei 2014-04-17 ��ʾ�кţ������޸ĵĻ�������ģ��ı���Ͷ���ʾ�ˣ���֪������ʲô���⡣
        
        setBodyData((ClientVO[]) oResults[0]);
    }

    /**
     * ���ܣ���ʾ��ͷ��Ϣ ���ߣ����� ����ʱ�䣺(2001-9-21 13:35:09) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param oResults
     *            java.lang.Object[]
     */
    private void onShowHead(Object oResults) {
        HeadVO vo = (HeadVO) oResults;
        ClientVO voData = vo.getData();
        Vector vObj = vo.getObject();
        initHeadItem(vObj);
        getReportBase().setHeadDataVO(voData);
    }

    /**
     * ���ܣ�����ʾ��Ϣ��Ԥ�������Ժ��������������� ���ߣ����� ����ʱ�䣺(2001-12-25 8:58:45) ������ <|>����ֵ�� �㷨��
     *
     * @return nc.ui.pub.report.ReportItem[]
     * @param items
     *            nc.ui.pub.report.ReportItem[]
     */
    private ReportItem[] preDealItems(ReportItem[] items) {
        //����ע����Ϊ���������С��λ��
        for (int i = 0; i < items.length; i++) {
            if (items[i].getDataType() == 2) {
                if (items[i].getKey().indexOf("shl") != -1) {
                    items[i].setDecimalDigits(MyClientEnvironment.getShlDec());
                    items[i].getNumberFormat().setShowThMark(true);
                    items[i].getNumberFormat().setShowZeroLikeNull(true);
                } else if (items[i].getKey().indexOf("je") != -1
                        || items[i].getKey().indexOf("ye") != -1
                        || items[i].getKey().indexOf("sj") != -1
                        || items[i].getKey().indexOf("ze")!=-1) {
                    //items[i].setDecimalDigits(getMaxCurrDec());
                    setItemDecDigits(items[i], getMaxCurrDec());
                }
            }
            if (items[i].getDataType() == 1) {
                items[i].setDecimalDigits(0);
            }
        }
        return items;
    }

    /**
     * ���ܣ�ȥ�������еĵ�һ��Ԫ�� ��������Ϊ˰�����˰���������
     *
     * shl yb bb
     *
     *
     * ���� ԭ�� ����
     *
     * �㷨�� �쳣������
     *
     * @return nc.vo.arap.pub.ConditionVO
     */
    private String[][] removeShuLiao(String[] strJeNames, String[] strJeFld) {
        if (m_strJeFld == null || m_strJeFld.length == 0
                || m_strJeNames == null || m_strJeNames.length == 0) {
            return null;
        }
        strJeFld = new String[m_strJeFld.length - 1];
        strJeNames = new String[strJeFld.length];
        if (m_strJeFld[0].trim().equalsIgnoreCase("shl")) {
            for (int i = 1; i < m_strJeFld.length; i++) {
                strJeFld[i - 1] = m_strJeFld[i];
                strJeNames[i - 1] = m_strJeNames[i];
            }
            //m_strJeFld = strJeFld;
            //m_strJeNames = strJeNames;
        } else {
            return new String[][] { m_strJeFld, m_strJeNames };
        }
        return new String[][] { strJeFld, strJeNames };
    }

    /**
     * ���ܣ����ñ����������� ���ߣ����� ����ʱ�䣺(2001-10-31 14:05:26) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param newData
     *            nc.vo.arap.pub.ClientVO[]
     */
    public void setBodyData(ClientVO[] newData) {
    	translateResID(newData);
        getReportBase().setBodyDataVO(newData);
    }
//��vo�е���Դid��Ϊ��ʵ������
//	UPP20060504-000236=����ֺ���
//	UPP20060504-000237=ͬ���ֺ���
//	UPP20060504-000238=�����Գ�
//	UPP20060504-000239=���ʷ���
//	UPP20060504-000240=�����ջ�
//	
//	UPP20060504-000058=����ת��
//	UPP20060504-000059=����ת��
//	UPP20060504-000060=�������
    private void translateResID(ClientVO[] newData){
    	if(newData==null){
    		return;
    	}
    	for(int i=0; i<newData.length;i++){
    		Object obj = newData[i].getAttributeValue("zy");
    		if(obj!=null&&obj.toString().indexOf("UPP20060504")!=-1){
    			String str=NCLangRes.getInstance().getStrByID("20060504", obj.toString().trim());
    			newData[i].setAttributeValue("zy",str);
    			Log.getInstance(this.getClass()).debug("��������ժҪID��"+obj+"��ʵժҪ��"+str);
    		}
    	}
    }
    /**
     * ���ܣ����ò�ѯ����������������ʾ��Ϣ ���ߣ����� ����ʱ�䣺(2001-9-28 8:35:28) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param newConds
     *            nc.vo.arap.pub.ConditionVO
     */
    public void setConditions(ConditionVO newConds) {
        m_voConds = newConds;
    }

    /**
     * ���ܣ������Ƿ����ø��ұ�ʶ ���ߣ����� ����ʱ�䣺(2001-8-7 11:21:47) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param bFlag
     *            boolean
     */
    private void setFbFlag(boolean bFlag) {
        m_bFbFlag = bFlag;
    }

    /**
     * ���ܣ����ñ�ͷ���ʹ��״̬ ���ߣ����� ����ʱ�䣺(2001-9-25 13:41:22) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param bFlag
     *            java.lang.Boolean
     */
    public void setHeadItemsEnable(boolean bFlag) {
        ReportItem[] items = getReportBase().getHead_Items();
        if (items == null || items.length == 0) {
            return;
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i].getDataType() == 6) {
                items[i].getComponent().setEnabled(bFlag);
            }
        }
    }

    /**
     * ���ܣ����ý����С��λ�� ���ߣ����� ����ʱ�䣺(2001-12-4 14:01:23) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param iLen
     *            int
     */
    private ReportItem setItemDecDigits(ReportItem item, int iLen) {
        //Vector vetDisplay = getInitCondVo().getVetDisplayFlds();
        if (item.getDataType() == 2) {
            String sKey = item.getKey();
            if (sKey.indexOf("ye") != -1 || sKey.indexOf("je") != -1
                    || sKey.indexOf("BALANCE") != -1
                    || sKey.indexOf("jelj") != -1
                    || sKey.indexOf("sj")!=-1 || sKey.indexOf("ze")!=-1) {
                /** ����ֶ� */
                if (sKey.indexOf("bb") != -1) {
                    item.setDecimalDigits(MyClientEnvironment.getLocalPoint());
                } else if (sKey.indexOf("fb") != -1
                        && MyClientEnvironment.isFracUsed()) {
                    item.setDecimalDigits(MyClientEnvironment.getFracPoint());
                } else {
                    item.setDecimalDigits(iLen);
                }
                item.getNumberFormat().setShowThMark(true);
                item.getNumberFormat().setShowZeroLikeNull(true);
            }
        }
        return item;
    }

    /**
     * ���ܣ����ݲ�ͬ��ҳ��ʽ������ÿ�����ֶ�Ӧ�������ֶ���Ϣ ���ߣ����� ����ʱ�䣺(2001-8-7 11:40:27) ������ <|>����ֵ��
     * �㷨�� �쳣������
     *
     * @param strBillType
     *            java.lang.String
     */
    private void setJeField(String strBillType) {
    	if(strBillType==null){
    		strBillType=constdata.ZYGS_JE;
    	}
        int iFbExist = 0;
        if (m_bFbFlag) {
            iFbExist = 1;
        }
        if (strBillType.equals(constdata.ZYGS_JE)) {
            m_iJeFldNum = 1 + iFbExist;
            m_strJeFld = new String[m_iJeFldNum];
            m_strJeNames = new String[m_iJeFldNum];
        } else if (strBillType.equals(constdata.ZYGS_WBJE)) {
            m_iJeFldNum = 2 + iFbExist;
            m_strJeFld = new String[m_iJeFldNum];
            m_strJeNames = new String[m_iJeFldNum];
            m_strJeFld[0] = "yb";
            m_strJeNames[0] = constdata.JE_YB;
        } else if (strBillType.equals(constdata.ZYGS_SHLJE)) {
            m_iJeFldNum = 2 + iFbExist;
            m_strJeFld = new String[m_iJeFldNum];
            m_strJeNames = new String[m_iJeFldNum];
            m_strJeFld[0] = "shl";
            m_strJeNames[0] = constdata.JE_SHL;
        } else if (strBillType.equals(constdata.ZYGS_SHLWB)) {
            m_iJeFldNum = 3 + iFbExist;
            m_strJeFld = new String[m_iJeFldNum];
            m_strJeNames = new String[m_iJeFldNum];
            m_strJeFld[0] = "shl";
            m_strJeFld[1] = "yb";
            m_strJeNames[0] = constdata.JE_SHL;
            m_strJeNames[1] = constdata.JE_YB;
        }
        m_strJeFld[m_iJeFldNum - 1] = "bb";
        m_strJeNames[m_iJeFldNum - 1] = constdata.JE_BB;
        if (m_bFbFlag) {
            m_strJeFld[m_iJeFldNum - 2] = "fb";
            m_strJeNames[m_iJeFldNum - 2] = constdata.JE_FB;
        }
    }

    /**
     * ���ܣ����ݴ���ı���pk�õ�������Ӧ���ֵ����С��λ�� ����������Ϊ�գ��������б�����С��λ������һ�� ���ߣ�����
     * ����ʱ�䣺(2002-1-21 9:05:16) ������ <|>����ֵ�� �㷨��
     *
     * @param CurrType
     *            java.lang.String[]
     */
    public void setMaxJeDec(String[] sCurrType) {
        try {
            //if (sCurrType != null) {
            //Vector vType = new Vector();
            //for (int i = 0; i < sCurrType.length; i++) {
            //vType.addElement(sCurrType[i]);
            //}
            //if (MyClientEnvironment.getFracCurrPK()!=null &&
            // MyClientEnvironment.getFracCurrPK().length()>0) {
            //vType.addElement(MyClientEnvironment.getFracCurrPK());
            //}
            //vType.addElement(MyClientEnvironment.getLocalCurrPK());
            //try {
            //sCurrType = new String[vType.size()];
            //vType.copyInto(sCurrType);
            //} catch (Exception ex) {
            //}
            //}

            String sKey = "";
            if (sCurrType == null || sCurrType.length == 0) {
                sKey = "--All--";
            } else {
                for (int i = 0; i < sCurrType.length; i++) {
                    sKey += "," + sCurrType[i];
                }
            }
            Object oValue = m_hashJeDec.get(sKey);
            if (oValue != null) {
                m_iMaxJeDec = ((Integer) oValue).intValue();
            } else {
//                m_iMaxJeDec = PubBO_Client.getMaxCurrLength(sCurrType);
            	 m_iMaxJeDec =   ProxyReporter.getIArapQueryDocInfoPublic().getMaxCurrLength(sCurrType);
                m_hashJeDec.put(sKey, new Integer(m_iMaxJeDec));
            }
        } catch (Exception e) {
        	Log.getInstance(this.getClass()).error(e.getMessage(),e);
        }
    }

    /**
     * ���ܣ����ñ���ģ�� ���ߣ����� ����ʱ�䣺(2001-9-14 11:40:10) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param newReportTemplet
     *            nc.ui.pub.report.ReportBaseClass
     */
    public void setReportTemplet(ReportBaseClass newReportTemplet) {
        m_ReportTemplet = newReportTemplet;
        initTemplet();
    }

    /**
     * ���ܣ������Ƿ���ʾ�����ۼ���Ϣ ���ߣ����� ����ʱ�䣺(2001-8-9 13:35:49) ������ <|>����ֵ�� �㷨�� �쳣������
     */
    public void setSumFlag(boolean newFlag) {
        m_bSumFlag = newFlag;
    }

    /**
     * ���ܣ��������� ���ߣ����� ����ʱ�䣺(2001-8-6 13:44:50) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param voResults
     *            nc.vo.arap.pub.StatValueObject[]
     * @param voConds
     *            nc.vo.arap.pub.ConditionVO
     */
    private void settleData(ConditionVO voConds) {
        /** ����ʾ����������Ӳ�ѯ���� */
    	Vector<ReportItem> vetDisplayFlds = voConds.getVetDisplayFlds();
        int iDyCol = 0;
//        String codeformula = "";
//        String nameformula = "";
        if (voConds.getVQryObj() != null) {
            for (int i = 0; i < voConds.getVQryObj().size(); i++) {
                QryObjVO voObj = (QryObjVO) voConds.getVQryObj().elementAt(i);

                ReportItem item = null;
                String key = null;
                //                if (voObj.getFldCode() == null) {
                //                    item = PubMethodUI.getItem(voObj, i);
                //                    iDyCol++;
                //                    vetDisplayFlds = PubMethodUI.insertCol(vetDisplayFlds,
                //                            iDyCol, item);
                //                }

                if (voConds.getShowType().equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0001155")/*@res "����"*/)) {
                    item = PubMethodUI.getItem(voObj, i);
                    key = item.getKey();
                    boolean contained = false;
                    for (ReportItem reportItem : vetDisplayFlds) {
                    	if ((key + "_name").equals(reportItem.getKey())) {
                    		contained = true;
                    		break;
                    	}
                    }
                    if (!contained) {
                    	iDyCol++;
                        vetDisplayFlds = PubMethodUI.insertCol(vetDisplayFlds,
                                iDyCol, item);

                       
                        
                        if (key != null && !key.equalsIgnoreCase("RQ")
                                && !key.equalsIgnoreCase("ND")
                                && !key.equalsIgnoreCase("ZY")
    							&& !key.equals("djlxda_djlxjc")
    							&& !key.equals("zb_djbh")) {                    	
                            key += "_name";
                            item.setKey(key);
                        }
                    }
                    

                } else if (voConds.getShowType().equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0003279")/*@res "����"*/)) {
                    QryObjVO voCode = (QryObjVO) voObj.clone();
                    voCode.setQryfld(voObj.getQryfld());
                    key=voObj.getQryfld();
                    if (key != null && !key.equalsIgnoreCase("RQ")
                            && !key.equalsIgnoreCase("ND")
                            && !key.equalsIgnoreCase("ZY")
							&& !key.equals("djlxda_djlxjc")
							&& !key.equals("zb_djbh")) {
                    voCode.setM_strDisplayName(voObj.getM_strDisplayName()
                            + NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0003279")/*@res "����"*/);
                    }
                    item = PubMethodUI.getItem(voCode, i);
                    key = item.getKey();
                    boolean contained = false;
                    for (ReportItem reportItem : vetDisplayFlds) {
                    	if ((key + "_code").equals(reportItem.getKey())) {
                    		contained = true;
                    		break;
                    	}
                    }
                    if (!contained) {
                    	iDyCol++;
                        vetDisplayFlds = PubMethodUI.insertCol(vetDisplayFlds,
                                iDyCol, item);

                        key = item.getKey();
                        if (key != null && !key.equalsIgnoreCase("RQ")
                                && !key.equalsIgnoreCase("ND")
                                && !key.equalsIgnoreCase("ZY")
    							&& !key.equals("djlxda_djlxjc")
    							&& !key.equals("zb_djbh")) {
                            key += "_code";
                            item.setKey(key);
                        }
                    }
                    

                } else if (voConds.getShowType().equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060504","UPP20060504-000104")/*@res "����+����"*/)) {
                    QryObjVO voCode = (QryObjVO) voObj.clone();
                    voCode.setQryfld(voObj.getQryfld());
                    voCode.setM_strDisplayName(voCode.getM_strDisplayName()+ NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/);
                    item = PubMethodUI.getItem(voCode, i);
                    key = item.getKey();
                    boolean contained = false;
                    for (ReportItem reportItem : vetDisplayFlds) {
                    	if ((key + "_code").equals(reportItem.getKey())) {
                    		contained = true;
                    		break;
                    	}
                    }
                    if (!contained) {
                    	iDyCol++;
                        if(item.getName()!=null && item.getName().equalsIgnoreCase(NCLangRes.getInstance().getStrByID("20060504","UC000-0002313")/*@res "����"*/+NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/)
                        	||	item.getName()!=null && item.getName().equalsIgnoreCase(NCLangRes.getInstance().getStrByID("20060504","UC000-0000807")/*@res "��������"*/+NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/)
    						||	item.getName()!=null && item.getName().equalsIgnoreCase(NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000270")/*@res "���ݱ��"*/+NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/)){
                        	item.setShow(false);
                        }
                        vetDisplayFlds = PubMethodUI.insertCol(vetDisplayFlds,
                                iDyCol, item);
                        key = item.getKey();

//                        if (key != null && !key.equalsIgnoreCase("RQ")
//                                && !key.equalsIgnoreCase("ND")
//                                && !key.equalsIgnoreCase("ZY")
//    							&& !key.equals("djlxda_djlxjc")
//    							&& !key.equals("zb_djbh")) {
                            key += "_code";
                            item.setKey(key);
                    }
                    
                   // }
                    item = PubMethodUI.getItem(voObj, i+1 );
                    key = item.getKey();
                    contained = false;
                    for (ReportItem reportItem : vetDisplayFlds) {
                    	if ((key + "_name").equals(reportItem.getKey())) {
                    		contained = true;
                    		break;
                    	}
                    }
                    if (!contained) {
                    	iDyCol++;
                        vetDisplayFlds = PubMethodUI.insertCol(vetDisplayFlds,
                                iDyCol, item);
                        key = item.getKey();
                        if (key != null && !key.equalsIgnoreCase("RQ")
                                && !key.equalsIgnoreCase("ND")
                                && !key.equalsIgnoreCase("ZY")
    							&& !key.equals("djlxda_djlxjc")
    							&& !key.equals("zb_djbh")) {
                            key += "_name";
                            item.setKey(key);
                        }
                    }
                }
            }
            //��pk��-rocking 080523 
            if( voConds.getQryObjPk()!=null){
            	 for (int i = 0; i < voConds.getVQryObj().size(); i++) {
                     QryObjVO voObj = (QryObjVO) voConds.getVQryObj().elementAt(i);
                     ReportItem item = null;
                        item = PubMethodUI.getItem(voObj, voConds.getVetDisplayFlds().size());
                        if(!containitem(voConds.getVetDisplayFlds(),item)){
    	                     vetDisplayFlds = PubMethodUI.insertCol(vetDisplayFlds,
    	                       voConds.getVetDisplayFlds().size()+100, item);
    	                     item.setShow(false);
                        }
                }

            }
            voConds.setVetDisplayFlds(vetDisplayFlds);
            /** �������ͷ������ԭʼ�е�λ�� */
            Vector<FldgroupVO> vFldGroup = voConds.getVetFldGroupbyCond();
            vFldGroup = PubMethodUI.changeFldGroupPos(vFldGroup, 0, iDyCol);
            voConds.setVetFldGroupybyCond(vFldGroup);
        }
        /** ��������ۼ����ݣ�����ۼ��ֶ���Ϣ */
        if (m_bSumFlag) {
            insertLjItem(voConds);
        }
        /** �޸Ķ��ͷ����ʾ���� */
        changeItemName(voConds);
        /** ������ͷ��Ϣ��������ֶβ�ֳ���Ӧ��ҳ��ʽ�Ķ������ֶ� */
        settleFldGroup(voConds);
    }
    private boolean containitem(Vector<ReportItem> vetDisplayFlds, ReportItem item) {
		// TODO Auto-generated method stub
    	if(vetDisplayFlds==null || vetDisplayFlds.size()==0|| item==null){
    		return false;
    	}
    	for(ReportItem tempitem:vetDisplayFlds){
    		if(tempitem==null){
    			continue;
    		}
    		if(tempitem.getKey().equalsIgnoreCase(item.getKey())){
    			return true;
    		}
    	}
		return false;
	}

    /**
     * ���ܣ�������ֶδ����Ϊ���ͷ�� ���ߣ����� ����ʱ�䣺(2001-8-7 13:48:28) ������ <|>����ֵ�� �㷨�� �쳣������
     *
     * @param voConds
     *            nc.vo.arap.pub.ConditionVO
     */
    private void settleFldGroup(ConditionVO voConds) {
        if (m_iJeFldNum == 1) {
            return;
        }
        if (m_iQcPos != -1) {
            insertCols(constdata.ZD_QCYE, voConds);
            changeJePos(m_iQcPos);
            m_iQcPos += m_iJeFldNum - 1;
        }
        if (m_iJfPos != -1) {
            insertCols(constdata.ZD_JFJE, voConds);
            changeJePos(m_iJfPos);
            m_iJfPos += m_iJeFldNum - 1;
        }
        if (m_iJfPos_sj != -1) {
            insertCols(constdata.ZD_JFSJ, voConds);
            changeJePos2(m_iJfPos_sj);
            if (getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_WBJE)
                    || getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_JE)) {
                m_iJfPos_sj += m_iJeFldNum - 1;
            } else {
                m_iJfPos_sj += m_iJeFldNum - 2;
            }
        }
        if (m_iJfPos_wsje != -1) {
            insertCols(constdata.ZD_JFWSJE, voConds);
            changeJePos2(m_iJfPos_wsje);
            if (getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_WBJE)
                    || getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_JE)) {
                m_iJfPos_wsje += m_iJeFldNum - 1;
            } else {
                m_iJfPos_wsje += m_iJeFldNum - 2;
            }
        }
        if (m_iDfPos != -1) {
            insertCols(constdata.ZD_DFJE, voConds);
            changeJePos(m_iDfPos);
            m_iDfPos += m_iJeFldNum - 1;
        }
        if (m_iDfPos_sj != -1) {
            insertCols(constdata.ZD_DFSJ, voConds);
            changeJePos2(m_iDfPos_sj);
            if (getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_WBJE)
                    || getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_JE)) {
                m_iDfPos_sj += m_iJeFldNum - 1;
            } else {
                m_iDfPos_sj += m_iJeFldNum - 2;
            }
        }
        if (m_iDfPos_wsje != -1) {
            insertCols(constdata.ZD_DFWSJE, voConds);
            changeJePos2(m_iDfPos_wsje);
            if (getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_WBJE)
                    || getConditions().getBillType().equalsIgnoreCase(constdata.ZYGS_JE)) {
                m_iDfPos_wsje += m_iJeFldNum - 1;
            } else {
                m_iDfPos_wsje += m_iJeFldNum - 2;
            }
        }
        if (m_iQmPos != -1) {
            insertCols(constdata.ZD_QMYE, voConds);
            changeJePos(m_iQmPos);
            m_iQmPos += m_iJeFldNum - 1;
        }
        if (m_iJePos != -1) {
            insertCols(constdata.ZD_JE, voConds);
            changeJePos(m_iJePos);
            m_iJePos += m_iJeFldNum - 1;
        }
        if (m_iYePos != -1) {
            insertCols(constdata.ZD_YE, voConds);
            changeJePos(m_iYePos);
            m_iYePos += m_iJeFldNum - 1;
        }
        if (m_bSumFlag) {
            if (m_iJfljPos != -1) {
                insertCols(constdata.ZD_JFLJ, voConds);
                changeJePos(m_iJfljPos);
                m_iJfljPos += m_iJeFldNum - 1;
            }
            if (m_iDfljPos != -1) {
                insertCols(constdata.ZD_DFLJ, voConds);
                changeJePos(m_iDfljPos);
                m_iDfljPos += m_iJeFldNum - 1;
            }
        }
    }

    /**
     * ���ܣ����ݱ���ģ�嵥Ԫ�е���ʾ˳������items ���ߣ����� ����ʱ�䣺(2001-8-17 15:51:49) ����������ģ�嵥Ԫ ����ֵ��
     * �����ı���ģ�嵥Ԫ �㷨�� �쳣������
     *
     * @return nc.ui.pub.report.ReportItem[]
     * @param items
     *            nc.ui.pub.report.ReportItem[]
     */
    public  ReportItem[] sortItems(ReportItem[] items) {
        sun.misc.Sort.quicksort(items, getComparer());
        return items;
        //if(items == null || items.length==0){
        //return null;
        //}
        //ReportItem[] newItems = new ReportItem[items.length];
        //for(int i=0;i<items.length;i++){
        //newItems[items[i].getShowOrder()-1] = items[i];
        //}
        //return newItems;
    }
    
    @SuppressWarnings("restriction")
	public void templetPrint(Vector vQryobj,String nodeCode){
         nc.ui.fi_print.entry.FiPrintEntry printEntry = createPrintEntry(vQryobj);
  		 printEntry.printView( nodeCode);
    	
    }
    
    @SuppressWarnings("restriction")
	public void templetPagePrint(Vector vQryobj,String nodeCode, PagePrintInfo info){
         nc.ui.fi_print.entry.FiPrintEntry printEntry = createPagePrintEntry(vQryobj, info);
  		 printEntry.printView( nodeCode);
    	
    }
    
    @SuppressWarnings("restriction")
	public void templetPrintNoView(Vector vQryobj,String nodeCode){
  		  nc.ui.fi_print.entry.FiPrintEntry printEntry = createPrintEntry(vQryobj);
  		  printEntry.print( nodeCode);
    	
    }

	private nc.ui.fi_print.entry.FiPrintEntry createPrintEntry(Vector vQryobj) {
		QryObjVO[] qryobjVOs = null;
  		  if(vQryobj!=null){
  			  qryobjVOs = new QryObjVO[vQryobj.size()];
  			vQryobj.copyInto(qryobjVOs);
  		  }
  		
//  		QryobjectCode,QryobjectName
//  		QryvalueCode,QryvalueName
          Vector<String> vDisplayName = new Vector<String>();
          Vector<String> vQryFld = new Vector<String>();
          Map<String, String[]> mapvar = new HashMap<String, String[]>();
          Map<String, String[]> mapvalue = new HashMap<String, String[]>();
          if(qryobjVOs!=null){
          mapvar.put("QryvalueCode", new String[qryobjVOs.length]);
          mapvar.put("QryvalueName", new String[qryobjVOs.length]);          
          mapvalue.put("QryobjectCode", new String[qryobjVOs.length]);
          mapvalue.put("QryobjectName", new String[qryobjVOs.length]);
          
          for(int i = 0; i <qryobjVOs.length;i++){
        	  	String displayName = qryobjVOs[i].getM_strDisplayName();
        	  	vDisplayName.add(displayName);
        	  	String qryfld = qryobjVOs[i].getQryfld();
        	  	vQryFld.add(qryfld);
        	  	mapvar.get("QryvalueCode")[i]=qryobjVOs[i].getFldCode();
        	  	mapvar.get("QryvalueName")[i]=qryobjVOs[i].getQryfld();
        	  	mapvalue.get("QryobjectCode")[i]=qryobjVOs[i].getM_strDisplayName()+NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/;
        	  	mapvalue.get("QryobjectName")[i]=qryobjVOs[i].getM_strDisplayName();
        	  	
          }
          }
            
          nc.ui.fi_print.entry.FiPrintEntry printEntry = nc.ui.fi_print.entry.FiPrintEntryFactory//.createDynamicColumnReportFiPrintEntry(
  		  .createReportPrintEntry(
  				  this.getReportBase(),  				 
  				  mapvalue,
  				 mapvar
//  				  "QryobjectName",
//  				  "QryvalueName",
//  				  vDisplayName,
//  				  vQryFld
  				  );
		return printEntry;
	}
	
	private nc.ui.fi_print.entry.FiPrintEntry createPagePrintEntry(Vector vQryobj, PagePrintInfo info) {
		QryObjVO[] qryobjVOs = null;
  		  if(vQryobj!=null){
  			  qryobjVOs = new QryObjVO[vQryobj.size()];
  			vQryobj.copyInto(qryobjVOs);
  		  }
  		
//  		QryobjectCode,QryobjectName
//  		QryvalueCode,QryvalueName
          Vector<String> vDisplayName = new Vector<String>();
          Vector<String> vQryFld = new Vector<String>();
          Map<String, String[]> mapvar = new HashMap<String, String[]>();
          Map<String, String[]> mapvalue = new HashMap<String, String[]>();
          if(qryobjVOs!=null){
          mapvar.put("QryvalueCode", new String[qryobjVOs.length]);
          mapvar.put("QryvalueName", new String[qryobjVOs.length]);          
          mapvalue.put("QryobjectCode", new String[qryobjVOs.length]);
          mapvalue.put("QryobjectName", new String[qryobjVOs.length]);
          
          for(int i = 0; i <qryobjVOs.length;i++){
        	  	String displayName = qryobjVOs[i].getM_strDisplayName();
        	  	vDisplayName.add(displayName);
        	  	String qryfld = qryobjVOs[i].getQryfld();
        	  	vQryFld.add(qryfld);
        	  	mapvar.get("QryvalueCode")[i]=qryobjVOs[i].getFldCode();
        	  	mapvar.get("QryvalueName")[i]=qryobjVOs[i].getQryfld();
        	  	mapvalue.get("QryobjectCode")[i]=qryobjVOs[i].getM_strDisplayName()+NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/;
        	  	mapvalue.get("QryobjectName")[i]=qryobjVOs[i].getM_strDisplayName();
        	  	
          }
          }
          
          nc.ui.fi_print.entry.FiPrintEntry printEntry = nc.ui.fi_print.entry.FiPrintEntryFactory//.createDynamicColumnReportFiPrintEntry(
  		  .createReportPagePrintEntry(
  				  this.getReportBase(),  				 
  				  mapvalue,
  				 mapvar, info
  				  );
		return printEntry;
	}
	
    /**
     * ģ��ѡ����<��ѯ����:<ѡ��ֵ>>
     * @return
     */
    
	public   Map<String,List<String>> getSelectedQryObjs(ReportBaseClass rbc,Vector<QryObjVO> qryobj){
		Map<String,List<String>> ret =new HashMap<String,List<String>>();
		int count=rbc.getBillTable().getSelectedRowCount();
		if(count<1)return null;
		Object curObj=null;
		String fldpk=null;
		for(int i=0;i<count;i++){
			for(QryObjVO obj:qryobj){
				fldpk=obj.getFldorigin()+ "_"+obj.getQryfld();
				curObj=rbc.getBodyValueAt(rbc.getBillTable().getSelectedRows()[i],fldpk);
				if(null!=curObj&&curObj.toString().trim().length()>0){
					if(ret.get( fldpk)==null)
						ret.put( fldpk, new ArrayList<String>());
					ret.get( fldpk).add(curObj.toString().trim());
				}
			}
		}
		return ret==null||ret.size()==0?null:ret;
	}
}