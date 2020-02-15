package cn.tsinghua.simulink.test;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import cn.tsinghua.simulink.extractor.ExternalInput;
import cn.tsinghua.simulink.extractor.LoadFile;

public class CGFuzzRoot {
	
	public static void main(String[] args) {
		int max_times = 100;
		int times = 0;
		CGEngine cge = new CGEngine();
		File file = new File(args[0]);
		String model_name = args[0].substring(0, args[0].length() - 4);
		try {
			ArrayList<ExternalInput> inputs = LoadFile.loadExternalInput(file);
			while (times < max_times) {
				ArrayList<String> values = cge.InitializeExternalInputsInRandom(inputs);
				SeedKind sk = RunSimulinkWithCoverage.CoverageRun(model_name, values);
				if (sk == SeedKind.Interesting) {
					System.out.println(StringUtils.join(values.iterator(), "#"));
				}
				times++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
