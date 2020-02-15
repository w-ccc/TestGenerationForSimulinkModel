package cn.tsinghua.simulink.extractor;

public class ExternalInput {
	
	String name = null;
	int[] dimension = null;
	ParameterType param_type = null;
	
	public ExternalInput(String name, int[] dimension, ParameterType param_type) {
		this.name = name;
		this.dimension = dimension;
		this.param_type = param_type;
	}
	
	public String GetName() {
		return name;
	}
	
	public int[] GetDimension() {
		return dimension;
	}
	
	public ParameterType GetParameterType() {
		return param_type;
	}
	
}
