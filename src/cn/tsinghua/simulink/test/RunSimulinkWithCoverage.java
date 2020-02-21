package cn.tsinghua.simulink.test;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;

import com.mathworks.engine.MatlabEngine;

public class RunSimulinkWithCoverage {

	public static SeedKind CoverageRun(String model_name, List<String> input_port_values) throws Exception {
		MatlabEngine eng = MatlabEngine.startMatlab();
		Future<Void> fLoad = eng.evalAsync("load_system('" + model_name + "')");
		System.out.print("Loading Simulink Model...");
		while (!fLoad.isDone()) {
			System.out.print(".");
			Thread.sleep(5000);
		}
		System.out.println();
		System.out.println("Simulink Model Loaded");
		File all_cv_file = new File(model_name + "_cv.cvt");
		if (all_cv_file.exists()) {
			eng.eval("[a_tests, a_cvs] = cvload('" + model_name + "_cv');");
			eng.eval("all_cv = a_cvs{1};");
			eng.eval("all_cov = decisioninfo(all_cv, '" + model_name + "');");
			eng.eval("all_cov_value = all_cov(1) / all_cov(2);");
		}
		System.out.println("Running Simulation...");
		String vals = StringUtils.join(input_port_values.iterator(), ",");
//		System.out.println("vals:" + vals);
		eng.eval("cvdata = cvsim('test', 'LoadExternalInput', 'on', 'ExternalInput', '" + vals + "');");
		eng.eval("after_all_cv = cvdata;");
		if (all_cv_file.exists()) {
			eng.eval("after_all_cv = all_cv + after_all_cv;");
		}
		eng.eval("after_all_cov = decisioninfo(after_all_cv, '" + model_name + "');");
		eng.eval("after_all_cov_value = after_all_cov(1) / after_all_cov(2);");
		SeedKind result = SeedKind.NotInterested;
		if (!all_cv_file.exists() || (all_cv_file.exists() && (double)eng.getVariable("all_cov_value") < (double)eng.getVariable("after_all_cov_value"))) {
			// seed is interesting
			result = SeedKind.Interesting;
		}
		eng.eval("cvsave('" + model_name + "_cv', after_all_cv);");
		eng.close();
		System.out.println("Simulation Over");
		return result;
	}
	
}
