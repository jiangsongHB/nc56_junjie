package nc.itf.uap.pub.jj;

import java.util.List;
import nc.vo.ic.md.MdcrkVO;

/**
 * @function 骏杰二次开发公共接口
 * 
 * @author QUSIDA
 * 
 * @date 2010-11-12 上午11:43:12
 *
 */
public interface IJJUAPService {
	
	/**
	 * @function 传入接口的参数修改为一个List<MdcrkVO>对象,
	 * MdcrkVO的第10个自定义字段在传入时将被注入一个存货基本
	 * 档案PK,接口的作用是根据这个基本档案PK查询出分类,再根据
	 * 宽度,查询出对应的附加值.存放于自定义字段11,如果当前查询
	 * 出错,则将对应自定义字段11置空,并将错误信息插入自定义字段12. 最后再将整个List<MdcrkVO>返回. 
	 *
	 * @author MeiChao
	 *
	 * @param List<MdcrkVO>
	 * 
	 * @throws Exception 
	 *
	 * @return List<MdcrkVO>
	 *
	 * @date 2010-11-05 上午9:58:57
	 */
	public abstract List<MdcrkVO> queryAdditionalvalue(List<MdcrkVO> mdvo) throws Exception;
	
	/**
	 * @function 根据存货基本档案ID查出理算系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 上午11:44:10
	 */
	public abstract Object  queryAdjustmentcoefficient(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function 根据存货基本档案ID查出毛边系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 上午11:44:13
	 */
	public abstract Object  queryBurrcoefficient(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function 公共查询方法，传入sql查出你想要的
	 *
	 * @author QuSida
	 *
	 * @param sql
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 上午11:44:16
	 */
	public abstract Object queryService(String sql) throws Exception;

}
