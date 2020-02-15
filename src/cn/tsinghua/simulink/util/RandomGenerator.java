package cn.tsinghua.simulink.util;

import java.util.Random;

public class RandomGenerator {
	
	Random r = new Random(1000);
	
	public boolean GenerateRandomBoolean() {
		return r.nextBoolean();
	}
	
	public int GenerateRandomInteger(int lower, int upper) {
		return r.nextInt(upper-lower+1) + lower;
	}
	
	public long GenerateRandomLong() {
		return r.nextLong();
	}
	
	public double GenerateRandomDouble() {
		return r.nextDouble();
	}
	
	public double GenerateRandomFloat() {
		return r.nextFloat();
	}
	
}
