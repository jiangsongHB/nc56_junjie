package nc.vo.ic.ic261;

/**
 * �����ߣ�����
 * �������ڣ�(2001-5-22 11:35:51)
 * ���ܣ�
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public class CheckMode {
	final static public int WholeWh= 0; //�����̵�
	final static public int Space= 1; //��λ�̵�
	final static public int Goods= 2; //����̵�
	final static public int Circle= 3; //�����̵�
	final static public int Keeper= 4; //����Ա�̵�
	final static public int Minus= 5; //������̵�
	final static public int NActive= 6; //�޶�̬�̵�
	final static public int Term= 7; //�������̵�
	final static public int Dynamic = 8;//��̬�̵�

	final static public int Check= 9; //ѡ��
	final static public int UnCheck= 10; //δѡ��
	final static public int md= 11; //�뵥�̵�


	public static String[] getCheckMode(){
		return new String[]{
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000167")/*@res "�����̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000164")/*@res "��λ�̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000162")/*@res "����̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000160")/*@res "�����̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000166")/*@res "����Ա�̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000163")/*@res "������̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000165")/*@res "�޶�̬�̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000161")/*@res "�������̵�"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000547")/*@res "��̬�̵�"*/,
				"",
				"",
				"�뵥�̵�"//add by ouyangzhb 2012-04-17 �����뵥��ť
				};
	}
/**
 * CheckMode ������ע�⡣
 */
public CheckMode() {
	super();

}
}