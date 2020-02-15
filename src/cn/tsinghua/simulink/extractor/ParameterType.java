package cn.tsinghua.simulink.extractor;

;

/**
 * Created by wendy on 2019/3/4.
 */
public class ParameterType {
	
	private final TypeID typeID;
	private double min;
	private double max;

	public ParameterType(TypeID pTypeID) {
		typeID = pTypeID;
		min = Double.MIN_VALUE;
		max = Double.MAX_VALUE;
	}

	public void setMin(double pMin) {
		min = pMin;
	}

	public void setMax(double pMax) {
		max = pMax;
	}

	public static TypeID toTypeID(String type) {
		switch (type) {
		case "boolean":
			return TypeID.Boolean;
		case "double":
			return TypeID.Double;
		case "single":
			return TypeID.Single;
		case "int8":
			return TypeID.Int8;
		case "int16":
			return TypeID.Int16;
		case "int32":
			return TypeID.Int32;
		case "uint8":
			return TypeID.UInt8;
		case "uint16":
			return TypeID.UInt16;
		case "uint32":
			return TypeID.UInt32;
		default:
			return TypeID.NoType;
		}
	}

	public TypeID getTypeID() {
		return typeID;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}
}
