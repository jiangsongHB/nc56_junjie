package nc.bs.arap.newlistquery.projection;

import nc.bs.arap.cscreater.projection.ProjectionCreater;
import nc.bs.arap.pub.criterion.IProjection;
import nc.bs.arap.pub.criterion.ProjectionList;
import nc.bs.arap.pub.criterion.Projections;
import nc.vo.arap.query.IQueryMetadata;
import nc.vo.arap.query.config.SysEnum;

public class EndBillBalMoneySumProjCreater implements ProjectionCreater {

	public IProjection create(IQueryMetadata metadata) {
		ProjectionList projectionList = Projections.projectionList();
		if (metadata.getConfig().getSystem() == SysEnum.YS) {
			projectionList.add(Projections.sum("fb.fx * fb.shlye").alias("qmshlye"))
			.add(Projections.sum("fb.fx * fb.ybye").alias("qmybye"))
			.add(Projections.sqlProjection("0.0").alias("qmfbye"))
			.add(Projections.sum("fb.fx * fb.bbye").alias("qmbbye"))
			.add(Projections.sum("case when (zb.zzzt='0 ' and zb.djdl='sk') then fb.bbye  else 0 end ").alias("wdje")); //wanglei 2014-01-07 
			//.add(Projections.sqlProjection("0.0").alias("qmwdye"));
		} else {
			projectionList.add(Projections.sum("-1 * fb.fx * fb.shlye").alias("qmshlye"))
			.add(Projections.sum("-1 * fb.fx * fb.ybye").alias("qmybye"))
			.add(Projections.sqlProjection("0.0").alias("qmfbye"))
			.add(Projections.sum("-1 * fb.fx * fb.bbye").alias("qmbbye"))
			.add(Projections.sum("case when (zb.zzzt='0 ' and zb.djdl='sk') then fb.bbye  else 0 end ").alias("wdje")); //wanglei 2014-01-07 
			//.add(Projections.sqlProjection("0.0").alias("qmwdye"));
		}
		
		return projectionList;
	}

}
