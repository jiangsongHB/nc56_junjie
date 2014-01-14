package nc.bs.arap.cscreater.projection;

import nc.bs.arap.pub.criterion.IProjection;
import nc.bs.arap.pub.criterion.ProjectionList;
import nc.bs.arap.pub.criterion.Projections;
import nc.vo.arap.query.IQueryMetadata;

public class ZeroEndMoneyProjectionCreater implements ProjectionCreater {

	public IProjection create(IQueryMetadata metadata) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.sqlProjection("0.0").alias("qmshlye"))
				.add(Projections.sqlProjection("0.0").alias("qmybye"))
				.add(Projections.sqlProjection("0.0").alias("qmfbye"))
				.add(Projections.sqlProjection("0.0").alias("qmbbye"))
				.add(Projections.sum(" case when (zb.zzzt='0 ' and zb.djdl='sk') then fb.dfybje when (zb.zzzt='0 ' and zb.djdl='fk') then fb.jfybje else 0 end ").alias("wdje"));  //wanglei 2014-01-07 
				//.add(Projections.sqlProjection("0.0 ").alias("qmwdye"));
		return projectionList;
	}

}
