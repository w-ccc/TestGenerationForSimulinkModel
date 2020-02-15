package cn.tsinghua.simulink.test;

import java.util.ArrayList;

import cn.tsinghua.simulink.extractor.ExternalInput;
import cn.tsinghua.simulink.util.RandomGenerator;

public class CGEngine {
	
	RandomGenerator rg = new RandomGenerator();
	
	public ArrayList<String> InitializeExternalInputsInRandom(ArrayList<ExternalInput> inputs) {
		ArrayList<String> result = new ArrayList<String>();
		for (ExternalInput ei : inputs) {
			String val = null;
			switch (ei.GetParameterType().getTypeID()) {
			case Boolean:
				val = rg.GenerateRandomBoolean()+"";
				break;
			case Int8:
				val = rg.GenerateRandomInteger(-128, 127)+"";
				break;
			case Int16:
				val = rg.GenerateRandomInteger(-32768, 32767)+"";
				break;
			case Int32:
				val = rg.GenerateRandomInteger(-2147483648, 2147483647)+"";
				break;
			case Int64:
				val = rg.GenerateRandomLong()+"";
				break;
			case UInt8:
				val = rg.GenerateRandomInteger(0, 255)+"";
				break;
			case UInt16:
				val = rg.GenerateRandomInteger(0, 65535)+"";
				break;
			case UInt32:
				val = (rg.GenerateRandomInteger(-2147483648, 2147483647)+2147483648L)+"";
				break;
			case UInt64:
				val = rg.GenerateRandomLong()+"+9223372036854775808";
				break;
			case Single:
				val = rg.GenerateRandomFloat()+"";
				break;
			case Double:
				val = rg.GenerateRandomDouble()+"";
				break;
			default:
				break;
			}
			result.add("[-1, " + val + "]");
		}
		return result;
	}
	
	public ArrayList<String> MutateExternalInputsInRandom(ArrayList<String> input_port_values) {
		return null;
	}
	
}
