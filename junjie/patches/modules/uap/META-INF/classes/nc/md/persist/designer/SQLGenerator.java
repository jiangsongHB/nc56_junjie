package nc.md.persist.designer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nc.md.innerservice.MDQueryService;
import nc.md.model.AssociationKind;
import nc.md.model.IAssociation;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.ICardinality;
import nc.md.model.IColumn;
import nc.md.model.IComponent;
import nc.md.model.IForeignKey;
import nc.md.model.ITable;
import nc.md.model.MetaDataException;

public class SQLGenerator {

	public static String[] getDropTableScript(String  componentID) throws MetaDataException
	{
		IComponent comp = MDQueryService.lookupMDInnerQueryService().getComponentByID(componentID);
		if(comp == null)
			return null;
		List<IBean> beanList = new ArrayList<IBean>();
		IBusinessEntity primaryEntity = comp.getPrimaryBusinessEntity();
		getDropTableScript(primaryEntity, null, beanList);
		List<IBusinessEntity> bEntities = comp.getBusinessEntities();
		for (IBusinessEntity businessEntity : bEntities)
		{
			if(!beanList.contains(businessEntity)
					)
			{
				beanList.add(businessEntity);
			}
		}
		String[] rets = new String[beanList.size()];
		for (int i =0;i<beanList.size();i++)
		{
			rets[i] ="drop table " + beanList.get(i).getTable().getName();
		}
		return rets;
	}

	private static void getDropTableScript(IBean bean, IBean parent, List<IBean> beanList)
	{
		if (parent == null && !beanList.contains(bean))
		{
			beanList.add(bean);
		}
		if (parent != null && beanList.contains(parent))
		{
			if(beanList.contains(bean))//
				return;
			beanList.add(beanList.indexOf(parent), bean);
		}
		List<IAssociation> associationList = bean.getAssociationsByKind(AssociationKind.Composite, ICardinality.ASS_ALL);
		if (associationList != null)
		{
			for (IAssociation association : associationList)
			{
				IBean endBean = association.getEndBean();
				if (endBean.equals(bean))//自关联？
				{
				}
				else
				{
					getDropTableScript(endBean, bean, beanList);
				}
			}
		}
	}

	public static String getForeignKeyScript(IBean bean) {
		StringBuffer foreignKeySQL = new StringBuffer();
		
		String endTableName = bean.getTable().getName();

		//对于自包含实体，如果存在多个1-n关系，那么表与扩展表之间就会存在多个外键，暂时没有处理
		//和此实体关联的其他实体，聚合关系均产生外键,关联关系暂时NC不需要--CCH
		List relatedBeanList = bean.getRelatedEntities(AssociationKind.Composite, ICardinality.ASS_ALL);
		for (Iterator iterator = relatedBeanList.iterator(); iterator.hasNext();) {
			IBean targetBean = (IBean) iterator.next();
			String startTableName = targetBean.getTable().getName();
			if(targetBean.equals(bean)) //自包含
			{
				continue;
			}
			List<IForeignKey> foreignKeies = null;
			startTableName = targetBean.getTable().getName();
			foreignKeies = targetBean.getTable().getForeignKeiesWithEndTable(bean.getTable());
			genForeignKeySQLImp(foreignKeySQL, endTableName, startTableName, foreignKeies);
		}
		return foreignKeySQL.toString();
	}

	
	private static void genForeignKeySQLImp(StringBuffer foreignKeySQL, String sourceTableName, String targetTableName, List<IForeignKey> foreignKeies) {
		if(foreignKeies==null) return;
		for (IForeignKey fk : foreignKeies) {
			if(fk.getAssType()!=AssociationKind.Composite) continue;
			IColumn startCol = fk.getStartColumn();
			IColumn endCol = fk.getEndColumn();

			foreignKeySQL.append("alter table ").append(targetTableName).append("\n");
			foreignKeySQL.append(" add constraint ").append(generatorFKName(targetTableName,startCol.getName()));
			foreignKeySQL.append(" foreign key ").append("( ").append(startCol.getName()).append(")\n");
			foreignKeySQL.append("references ").append(sourceTableName).append(" (").append(endCol.getName()).append(
					")\n");
			foreignKeySQL.append("go\n");
		}
	}

	private static String generatorFKName(String targetTableName, String columnName) {
		String strHash = String.valueOf(Math.abs((targetTableName + columnName).hashCode())%1000000);
		String tableName = targetTableName;
		String colName = columnName;
		int index = targetTableName.indexOf("_");
		if(index > -1)
			tableName = targetTableName.substring(index + 1);
		index = columnName.indexOf("_");
		if(index > -1)
			colName = columnName.substring(index + 1);
		
		String dbType = System.getProperty("dbType");
		if(dbType!=null && dbType.equalsIgnoreCase("db2")) {
			//对于db2数据库，约束名最大长度是18，很容易超过长度限制
			if(tableName.length() > 3) {
				tableName = tableName.substring(tableName.length() - 4);
			}
			if(colName.length() > 3) {
				colName = colName.substring(colName.length() - 4);
			}
		}
		else {
			//非db2数据库，长度控制在30位以内
			if(tableName.length() > 9) {
				tableName = tableName.substring(tableName.length() - 10);
			}
			if(colName.length() > 9) {
				colName = colName.substring(colName.length() - 10);
			}
		}
		StringBuffer fkName = new StringBuffer("fk_");
		fkName.append(tableName).append("_").append(colName).append(strHash);
		
		return fkName.toString();
	}

	public static String getCreateTableScript(IBean bean,Set<String> midTableSet) {
		ITable table = bean.getTable();
		StringBuffer refBuf = generateCreateSQLByTable(table);
		return refBuf.toString();
	}

	private static StringBuffer generateCreateSQLByTable(ITable table) {
		if (table == null)
			return null;
		List columns = table.getColumns();
		// 建表SQL语句
		StringBuffer createTableSQL = new StringBuffer();
		createTableSQL.append("/* tablename: ").append(table.getDisplayName()).append(" */\n\n"); //wanglei 2014-02-24 为方便数据字典导入调整输出格式
		createTableSQL.append("create table ").append(table.getName()).append(" (\n");
		// 逐渐列表
		ArrayList<IColumn> pkeyList = new ArrayList<IColumn>();
		for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
			IColumn column = (IColumn) iterator.next();
//			createTableSQL.append("/*").append(column.getDisplayName()).append("*/  ");  //wanglei 2014-02-24 为方便数据字典导入调整输出格式
			createTableSQL.append(column.getName()).append(" ");
			createTableSQL.append(column.getSQLDataType());
			Integer length = column.getLength();
			if (length != null && length > 0) {
				createTableSQL.append("(").append(length);
				if (needsetPrecise(column.getSQLDataType())) {
					Integer precise = column.getPrecise();
					if (precise != null) {
						createTableSQL.append(",").append(precise);
					}
					createTableSQL.append(")");
				} else {
					createTableSQL.append(")");
				}

			}
			createTableSQL.append(" ");
			if (column.isIndentitied()) {
				createTableSQL.append("IDENTITY (");
				Integer seed = column.getIncrementSeed();
				Integer step = column.getIncrementStep();
				if (seed != null)
					createTableSQL.append(seed + ",");
				createTableSQL.append(step == null ? 1 : step);
				createTableSQL.append(") ");
			}
			if (column.isNullable()) {
				createTableSQL.append("NULL");
			} else {
				createTableSQL.append("NOT NULL");
			}
			createTableSQL.append(" \n /*").append(column.getDisplayName()).append("*/  "); //wanglei 2014-02-24 为方便数据字典导入调整输出格式
			createTableSQL.append(",\n");
			// 是主键
			if (column.isPkey()) {
				pkeyList.add(column);
			}
		}
		if (pkeyList.size() > 0) {
			createTableSQL.append("CONSTRAINT ").append("PK_" + table.getName().toUpperCase()).append(" PRIMARY KEY (");
			for (Iterator iterator = pkeyList.iterator(); iterator.hasNext();) {
				IColumn pkcol = (IColumn) iterator.next();
				createTableSQL.append(pkcol.getName()).append(",");
			}
			createTableSQL.deleteCharAt(createTableSQL.length() - 1);
			createTableSQL.append("),\n");
		}
		createTableSQL
				.append("ts char(19) null default convert(char(19),getdate(),20),\n dr smallint null default 0,\n");
		createTableSQL.append(")\ngo \n\n");
		return /* getDropTableScript(table) + "\n"+ */createTableSQL;
	}

	/*
	 * private static String getDropTableScript(ITable table) { StringBuffer
	 * script = new StringBuffer(); script.append("if exists (select 1 from
	 * sysobjects where id = object_id('");
	 * script.append(table.getName()).append("') and type = 'U') drop table
	 * ").append(table.getName()).append(";"); return script.toString(); }
	 */
	// 是否需要设置精度
	private static boolean needsetPrecise(String type) {
		String[] needPrecise = { "decimal", "double", "float", "numeric" };
		for (int i = 0; i < needPrecise.length; i++) {
			if (needPrecise[i].equalsIgnoreCase(type))
				return true;
		}
		return false;
	}
}
