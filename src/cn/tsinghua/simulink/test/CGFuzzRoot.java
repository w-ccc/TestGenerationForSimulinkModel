package cn.tsinghua.simulink.test;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import cn.tsinghua.simulink.extractor.ExternalInput;
import cn.tsinghua.simulink.extractor.LoadFile;

public class CGFuzzRoot {
	
	public static void main(String[] args) {
//		int max_times = 100;
//		int times = 0;
		double running_time = Double.parseDouble(args[1]);
		System.out.println("Allowed running time: " + running_time + " miniutes BTW " + (running_time * 60) + " seconds");
		double a_time = running_time * 60 * 1000;
		CGEngine cge = new CGEngine();
		File file = new File(args[0]);
		String model_name = args[0].substring(0, args[0].length() - 4);
		long start_time = System.currentTimeMillis();
		try {
			RunSimulinkWithCoverage runner = new RunSimulinkWithCoverage(model_name);
			runner.Initialize();
			ArrayList<ExternalInput> inputs = LoadFile.loadExternalInput(file);
			while ((System.currentTimeMillis() - start_time) < a_time) {
				ArrayList<String> values = cge.InitializeExternalInputsInRandom(inputs);
				RunInfo info = runner.CoverageRun(values);
				if (info.seed_kind == SeedKind.Interesting) {
					System.out.println("Achieved coverage:" + info.coverage + " ==== " + StringUtils.join(values.iterator(), "#"));
				}
			}
			runner.Close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
