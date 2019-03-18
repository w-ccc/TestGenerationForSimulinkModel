package cn.tsinghua.simulink.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Extractor.ParameterType;

/**
 * Created by wendy on 2019/1/8.
 */
public class CTM {
	
	private Map<String, Set<Double>> classificationTree = new HashMap<>();
	
	// for inner testing.
//	private Random rand = new Random(10);

	public CTM(Map<String, Set<Double>> classificationTree) {
		this.classificationTree.putAll(classificationTree);
	}

	public CTM() {
	}

	public void initCT(Map<String, ParameterType> inPorts) {
		for (Map.Entry<String, ParameterType> inport : inPorts.entrySet()) {
			System.out.println("inport.getKey():" + inport.getKey());
			classificationTree.put(inport.getKey(), getCandidate(inport.getValue()));
		}
	}

	private Set<Double> getCandidate(ParameterType parameterType) {
		Set<Double> candidate = new HashSet<>();
//		int num = (int) (rand.nextInt(10));
		int num = 4;
		ParameterType.TypeID typeID = parameterType.getTypeID();
		double min = parameterType.getMin();
		double max = parameterType.getMax();
		if (typeID == ParameterType.TypeID.Boolean) {
			candidate.add(0.0);
			candidate.add(1.0);
		} else {
			if (min == Double.MIN_VALUE || max == Double.MAX_VALUE) {
				for (int i = 0; i < num; i++) {
					candidate.add(100.0 + i * 30);
				}
			} else {
				double step = (max - min) / num;
				if (!(typeID == ParameterType.TypeID.Double || typeID == ParameterType.TypeID.Single || typeID == ParameterType.TypeID.NoType)) {
					step = (int)step;
					min = (int)min;
					if (step == 0)
						step = 1;
				}
				for (int i = 0; i < num; i++) {
					if (min + i * step > max) {
						continue;
					}
					candidate.add(min + i * step);
				}
			}
		}
		System.out.println(candidate.size());
		return candidate;
	}

//    public void pairWise() {
//        List<String> portName = new ArrayList<>();
//        PairwiseTestingToolkit toolkit = new PairwiseTestingToolkit();
//        for (Map.Entry<String, Set<Double>> entry: classificationTree.entrySet()) {
//            portName.add(entry.getKey());
//        }
//        toolkit.setMetaParameterProvider(new IMetaParameterProvider() {
//            public MetaParameter get() {
//                MetaParameter mp = new MetaParameter(2);
//                for (Map.Entry<String, Set<Double>> entry: classificationTree.entrySet()) {
//                    Factor factor = new Factor(entry.getKey());
//                    for (Double candidate : entry.getValue()) {
//                        factor.addLevel(candidate.toString());
//                    }
//                    mp.addFactor(factor);
//                }
//                return mp;
//            }
//        });
//        toolkit.setEngine(new AMEngine());
//        toolkit.setTestCasesGenerator(new TXTTestCasesGenerator());
//        try {
//            String[][] result = toolkit.generateTestData();
//            for (int i = 0; i < result.length; i++) {
//                Map<String, Double> testCase = new HashMap<>();
//                for (int j = 0; j <result[i].length; j++) {
//                    testCase.put(portName.get(j), Double.valueOf(result[i][j]));
//                }
//                testCases.add(testCase);
//            }
//            //System.out.println(toolkit.generateTestCases());
//        } catch (MetaParameterException e) {
//            e.printStackTrace();
//        } catch (EngineException e) {
//            e.printStackTrace();
//        }
//    }

	public List<Map<String, Double>> nWise(int n) {
		/*
		 * Map<String, Set<Double>> orederedCT = new HashMap<>(); List<String> portName
		 * = new ArrayList<>(); List<Map.Entry<String,Set<Double>>> list = new
		 * ArrayList<>(classificationTree.entrySet()); Collections.sort(list,new
		 * Comparator<Map.Entry<String,Set<Double>>>() { public int
		 * compare(Map.Entry<String, Set<Double>> o1, Map.Entry<String, Set<Double>> o2)
		 * { return o2.getValue().size() - o1.getValue().size(); } }); for
		 * (Map.Entry<String, Set<Double>> entry : list) {
		 * orederedCT.put(entry.getKey(), entry.getValue());
		 * portName.add(entry.getKey()); }
		 */
		List<Map<String, Double>> testCases = new ArrayList<>();
		int i, j, k, m;
		List<String> keySet = new ArrayList<>();
		List<Set<Double>> valueSet = new ArrayList<>();
		for (Map.Entry<String, Set<Double>> entry : classificationTree.entrySet()) {
			keySet.add(entry.getKey());
			valueSet.add(entry.getValue());
		}
		m = keySet.size();
		Double[][] allPair = getFullCombination(valueSet);
		System.out.println(allPair.length);
		int[][] nPairPattern = getMPairPattern(n, m);
		// n-wise
		List<List<Double>> resultValues = new ArrayList<>();
		for (i = 0; i < allPair.length; i++) {
			List<Double> temp = new ArrayList<>();
			for (j = 0; j < m; j++) {
				temp.add(allPair[i][j]);
			}
			resultValues.add(temp);
		}
		for (i = 0; i < allPair.length; i++) {
			int sameNum = 0;
			for (j = 0; j < nPairPattern.length; j++) {
				for (List<Double> otherValue : resultValues) {
					if (equal(allPair[i], otherValue))
						continue;
					for (k = 0; k < n; k++) {
						if (allPair[i][nPairPattern[j][k]] != otherValue.get(nPairPattern[j][k])) {
							break;
						}
					}
					if (k == n) {
						sameNum++;
						break;
					}
				}
				if (sameNum < j) {
					break;
				}
			}
			if (sameNum == nPairPattern.length) {
				remove(allPair[i], resultValues);
			}
		}
		for (List<Double> value : resultValues) {
			Map<String, Double> temp = new HashMap<>();
			for (i = 0; i < m; i++) {
				temp.put(keySet.get(i), value.get(i));
			}
			testCases.add(temp);
		}
		return testCases;
	}

	private boolean equal(Double[] a, List<Double> b) {
		int n = a.length;
		int m = b.size();
		int i;
		if (n != m) {
			return false;
		}
		for (i = 0; i < n; i++) {
			if (a[i] != b.get(i)) {
				return false;
			}
		}
		return true;
	}

	private void remove(Double[] a, List<List<Double>> b) {
		for (List<Double> temp : b) {
			if (equal(a, temp)) {
				b.remove(temp);
				return;
			}
		}
	}

	private int[][] getMPairPattern(int n, int m) {
		int N = 1, i, j = 1, k = 1;
		if (m > n) {// Cnm
			for (i = 0; i < n; i++) {
				k = k * (n - i);
				j = j * (m - i);
			}
			N = j / k;
		}
		int[][] result = new int[N][n];
		int index = 0;
		List<List<Integer>> candidateArray = new ArrayList<>();
		for (i = 0; i < m; i++) {
			int tempN = candidateArray.size();
			for (k = 0; k < tempN; k++) {
				List<Integer> list = candidateArray.get(k);
				List<Integer> temp = new ArrayList<Integer>(list);
				temp.add(i);
				if (temp.size() == n) {
					for (j = 0; j < n; j++) {
						result[index][j] = temp.get(j);
					}
					index++;
				} else {
					candidateArray.add(temp);
				}
			}
			List<Integer> temp = new ArrayList<>();
			temp.add(i);
			candidateArray.add(temp);
		}
		return result;
	}

	private Double[][] getFullCombination(List<Set<Double>> values) {
		Double[][] result = null;
		Double[][] temp;
		int i = 1, j = 0, k, n = 1;
		for (Set<Double> value : values) {
			temp = result;
			result = new Double[n * value.size()][i];
			if (i == 1) {
				temp = new Double[n * value.size()][i];
				for (Double candidate : value) {
					result[j][0] = temp[j][0] = candidate;
					j++;
				}
			} else {
				for (j = 0; j < n; j++) {
					k = 0;
					for (Double candidate : value) {
						for (int m = 0; m < i - 1; m++) {
							result[j * value.size() + k][m] = temp[j][m];
						}
						result[j * value.size() + k][i - 1] = candidate;
						k++;
					}
				}
			}
			n = n * value.size();
			i++;
		}
		return result;
	}

	public List<Map<String, Double>> newNWise(int n) {
		//List<Map<String, Double>> result = new ArrayList<>();//record n-wise result
		List<List<Map<String, Double>>> tempResult = new ArrayList<>();//record 1 to n wise
		List<Map<String, Double>> lastN1 = new ArrayList<>();
		List<Map<String, Double>> lastN = new ArrayList<>();
		int m = classificationTree.size();
		int i = 0;
		if (m < n)
			n = m;
		//to cal n-wise in m elements, we should cal n-wise in m-1 elements and (n-1)-wise in m-1 elements first
		for (Map.Entry<String, Set<Double>> entry : classificationTree.entrySet()) {
			lastN.clear();
			lastN1.clear();
			if (i == 0) {
				List<Map<String, Double>> testcases = new ArrayList<>();
				for (Double candidate : entry.getValue()) {
					Map<String, Double> testcase = new HashMap<>();
					testcase.put(entry.getKey(), candidate);
					testcases.add(testcase);
				}
				tempResult.add(testcases);
			} else {
				for (int j = 0; /*j < m - i && */j < n && j <= i; j++) {
					lastN1 = lastN;
					//for (Map<String, Double> testcase : tempResult.get(j)) {
					//	lastN.add(new HashMap<>(testcase));
					//}
					if (j < i)
						lastN = new ArrayList<>(tempResult.get(j));
					if (j == 0 && i <= m - n) {
						//cal 1-wise
						int k = 0;
						int testcaseN = tempResult.get(j).size();
						Double lastCandidate = 0.0;
						for (Double candidate : entry.getValue()) {
							if (k < testcaseN) {
								Map<String, Double> testcase = new HashMap<>(tempResult.get(j).get(k));
								testcase.put(entry.getKey(), candidate);
								tempResult.get(j).remove(k);
								tempResult.get(j).add(k, testcase);
							} else {
								Map<String, Double> testcase = new HashMap<>(tempResult.get(j).get(testcaseN - 1));
								testcase.put(entry.getKey(), candidate);
								tempResult.get(j).add(k, testcase);
							}
							k++;
							lastCandidate = candidate;
						}
						while (k < testcaseN) {
							Map<String, Double> testcase = new HashMap<>(tempResult.get(j).get(k));
							testcase.put(entry.getKey(),lastCandidate);
							tempResult.get(j).remove(k);
							tempResult.get(j).add(k, testcase);
							k++;
						}
					} else if (j == i) {
						//cal n-wise in n elements from (n-1)-wise in n-1 elements
						List<Map<String, Double>> cartProduct = new ArrayList<>();
						for (Double candidate : entry.getValue()) {
							for (Map<String, Double> testcase : lastN1) {
								Map<String, Double> newTestcase = new HashMap<>(testcase);
								newTestcase.put(entry.getKey(), candidate);
								cartProduct.add(newTestcase);
							}
						}
						tempResult.add(j, cartProduct);
					} else if (j >= i - m + n) {
						//cal n-wise in m elements from n-wise in m-1 elements and (n-1)-wise in m-1 elements
						List<Map<String, Double>> DValue = new ArrayList<>();
						for (Map<String, Double> testcase : lastN) {
							if (!lastN1.contains(testcase)) {
								DValue.add(testcase);
							}
						}
						if (DValue.isEmpty()) {
							DValue = lastN;
						}
						List<Map<String, Double>> cartProduct = new ArrayList<>();
						for (Double candidate : entry.getValue()) {
							//DValue X candidate
							for (Map<String, Double> testcase : DValue) {
								Map<String, Double> newTestcase = new HashMap<>(testcase);
								newTestcase.put(entry.getKey(), candidate);
								cartProduct.add(newTestcase);
							}
							DValue = lastN1;
						}
						tempResult.remove(j);
						tempResult.add(j,cartProduct);
					}
				}
			}
			i++;
		}
		return tempResult.get(n-1);
	}

}
