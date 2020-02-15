package cn.tsinghua.simulink.test;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;

import com.mathworks.engine.MatlabEngine;

public class RunSimulinkWithCoverage {

	public static SeedKind CoverageRun(String model_name, List<String> input_port_values) throws Exception {
		SeedKind result = SeedKind.NotInterested;
		MatlabEngine eng = MatlabEngine.startMatlab();
		Future<Void> fLoad = eng.evalAsync("load_system('" + model_name + "')");
		while (!fLoad.isDone()) {
			System.out.println("Loading Simulink Model...");
			Thread.sleep(10000);
		}
		eng.eval("all_cv = cvload('" + model_name + "_cv');");
		eng.eval("all_cov = decisioninfo(all_cv, '" + model_name + "');");
		eng.eval("all_cov_value = all_cov(1) / all_cov(2);");
		System.out.println("Running Simulation...");
		String vals = StringUtils.join(input_port_values.iterator(), ",");
		eng.eval("cvdata = cvsim('test', 'LoadExternalInput', 'on', 'ExternalInput', '" + vals + "');");
		eng.eval("after_all_cv = all_cv + cvdata;");
		eng.eval("after_all_cov = decisioninfo(after_all_cv, '" + model_name + "');");
		eng.eval("after_all_cov_value = after_all_cov(1) / after_all_cov(2);");
		if ((double)eng.getVariable("all_cov_value") < (double)eng.getVariable("after_all_cov_value")) {
			// seed is interesting
			result = SeedKind.Interesting;
		}
		eng.eval("cvsave('" + model_name + "_cv', after_all_cv);");
		eng.close();
		return result;
	}
	
}
