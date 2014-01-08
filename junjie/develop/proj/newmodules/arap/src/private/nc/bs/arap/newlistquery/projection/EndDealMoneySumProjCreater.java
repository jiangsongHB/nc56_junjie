package nc.bs.arap.newlistquery.projection;

import nc.bs.arap.cscreater.projection.ProjectionCreater;
import nc.bs.arap.pub.criterion.IProjection;
import nc.bs.arap.pub.criterion.ProjectionList;
import nc.bs.arap.pub.criterion.Projections;
import nc.vo.arap.query.IQueryMetadata;
import nc.vo.arap.query.config.SysEnum;

public class EndDealMoneySumProjCreater implements ProjectionCreater {

	public IProjection create(IQueryMetadata metadata) {
		ProjectionList projectionList = Projections.projectionList();
		if (metadata.getConfig().getSystem() != SysEnum.YS) {
			projectionList.add(Projections.sum("clb.dfclshl - clb.jfclshl").alias("qmshlye"))
					.add(Projections.sum("clb.dfclybje - clb.jfclybje").alias("qmybye"))
					.add(Projections.sqlProjection("0.0").alias("qmfbye"))
					.add(Projections.sum("clb.dfclbbje - clb.jfclbbje").alias("qmbbye"))
					.add(Projections.sqlProjection("0.0").alias("wdje"));   //wanglei 2014-01-07 骏杰增加未达金额字段
					//.add(Projections.sqlProjection("0.0 ").alias("qmwdye"));
		} else {
			projectionList.add(Projections.sum("clb.jfclshl - clb.dfclshl").alias("qmshlye"))
					.add(Projections.sum("clb.jfclybje - clb.dfclybje").alias("qmybye"))
					.add(Projections.sqlProjection("0.0").alias("qmfbye"))
					.add(Projections.sum("clb.jfclbbje - clb.dfclbbje").alias("qmbbye"))
					.add(Projections.sqlProjection("0.0").alias("wdje"));  //wanglei 2014-01-07 骏杰增加未达金额字段
					//.add(Projections.sqlProjection("0.0 ").alias("qmwdye"));
		}
		
		return projectionList;
	}

}
