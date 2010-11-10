/**
   脚本说明：更新码单历史数据，如果没有码历史数据，或都不要对历史数据进行操作，可以不执行些脚本
   作者：何意求
   日期：2010-11-10
   版本：v1.0
**/


update nc_mdxcl_b set def4='N',def7=md_length,def8=md_width,def9=md_meter;

update nc_mdcrk set def4='N',def7=md_length,def8=md_width,def9=md_meter;

update nc_mdsd set def4='N'