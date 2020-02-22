package cn.tsinghua.simulink.test;

public class RunInfo {
	
	SeedKind seed_kind = null;
	double coverage = 0;
	
	public RunInfo(SeedKind seed_kind, double coverage) {
		this.seed_kind = seed_kind;
		this.coverage = coverage;
	}
	
}
