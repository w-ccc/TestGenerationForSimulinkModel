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
		case "int64":
			return TypeID.Int64;
		case "uint8":
			return TypeID.UInt8;
		case "uint16":
			return TypeID.UInt16;
		case "uint32":
			return TypeID.UInt32;
		case "Inherit: auto":
			return TypeID.Int8;
		default:
			System.err.println("Serious Error! Uncognized Type:" + type);
			System.exit(1);
			return TypeID.NoType;
		}
	}
	
	public String toTypeString() {
		switch (typeID) {
		case Boolean:
			return "boolean";
		case Double:
			return "double";
		case Single:
			return "single";
		case Int8:
			return "int8";
		case Int16:
			return "int16";
		case Int32:
			return "int32";
		case Int64:
			return "int64";
		case UInt8:
			return "uint8";
		case UInt16:
			return "uint16";
		case UInt32:
			return "uint32";
		case UInt64:
			return "uint64";
		default:
			return "Serious Error! Uncognized type!";
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
