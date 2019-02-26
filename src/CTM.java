import pairwisetesting.PairwiseTestingToolkit;
import pairwisetesting.coredomain.*;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.testcasesgenerator.TXTTestCasesGenerator;

import java.util.*;

/**
 * Created by wendy on 2019/1/8.
 */
public class CTM {
    private Map<String, Set<Double>> classificationTree = new HashMap<>();
    private List<Map<String, Double>> testCases = new ArrayList<>();

    public CTM(Set<String> inPorts) {
        for (String portName : inPorts) {
            classificationTree.put(portName, getCandidate());
        }
    }

    private Set<Double> getCandidate() {
        Set<Double> candidate = new HashSet<>();
        int num =(int)(Math.random() * 19);
        candidate.add(0.0);
        candidate.add(1.0);
        //candidate.add(100.0);
        for (int i = 0; i < num; i++) {
            candidate.add(100.0 + i * 30);
        }
        return candidate;
    }

    public  List<Map<String, Double>> getTestCases() {
        return testCases;
    }

    public void pairWise() {
        List<String> portName = new ArrayList<>();
        PairwiseTestingToolkit toolkit = new PairwiseTestingToolkit();
        for (Map.Entry<String, Set<Double>> entry: classificationTree.entrySet()) {
            portName.add(entry.getKey());
        }
        toolkit.setMetaParameterProvider(new IMetaParameterProvider() {
            public MetaParameter get() {
                MetaParameter mp = new MetaParameter(2);
                for (Map.Entry<String, Set<Double>> entry: classificationTree.entrySet()) {
                    Factor factor = new Factor(entry.getKey());
                    for (Double candidate : entry.getValue()) {
                        factor.addLevel(candidate.toString());
                    }
                    mp.addFactor(factor);
                }
                return mp;
            }
        });
        toolkit.setEngine(new AMEngine());
        toolkit.setTestCasesGenerator(new TXTTestCasesGenerator());
        try {
            String[][] result = toolkit.generateTestData();
            for (int i = 0; i < result.length; i++) {
                Map<String, Double> testCase = new HashMap<>();
                for (int j = 0; j <result[i].length; j++) {
                    testCase.put(portName.get(j), Double.valueOf(result[i][j]));
                }
                testCases.add(testCase);
            }
            //System.out.println(toolkit.generateTestCases());
        } catch (MetaParameterException e) {
            e.printStackTrace();
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }

    public void nWise(int n) {
        /*Map<String, Set<Double>> orederedCT = new HashMap<>();
        List<String> portName = new ArrayList<>();
        List<Map.Entry<String,Set<Double>>> list = new ArrayList<>(classificationTree.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Set<Double>>>() {
            public int compare(Map.Entry<String, Set<Double>> o1,
                               Map.Entry<String, Set<Double>> o2) {
                return o2.getValue().size() - o1.getValue().size();
            }
        });
        for (Map.Entry<String, Set<Double>> entry : list) {
            orederedCT.put(entry.getKey(), entry.getValue());
            portName.add(entry.getKey());
        }*/
        int i, j, k, m;
        //全排列
        List<String> keySet = new ArrayList<>();
        List<Set<Double>> valueSet = new ArrayList<>();
        for (Map.Entry<String, Set<Double>> entry : classificationTree.entrySet()) {
            keySet.add(entry.getKey());
            valueSet.add(entry.getValue());
        }
        m = keySet.size();
        Double[][] allPair = getFullCombination(valueSet);
        System.out.println(allPair.length);
        //n元素结对
        int[][] nPairPattern = getMPairPattern(n, m);
        //n-wise
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
                        if(allPair[i][nPairPattern[j][k]] != otherValue.get(nPairPattern[j][k])) {
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
            if(sameNum == nPairPattern.length){
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
    }

    private boolean equal(Double [] a, List<Double> b){
        int n = a.length;
        int m = b.size();
        int i;
        if (n != m){
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
        if (m > n) {//Cnm
            for (i = 0; i < n; i++) {
                k = k * (n - i);
                j = j * (m - i);
            }
            N = j / k;
        }
        int[][] result = new int[N][n];
        int index = 0;
        List<List<Integer>> candidateArray = new ArrayList<>();
        for(i = 0; i < m; i++) {
            int tempN = candidateArray.size();
            for(k = 0; k < tempN; k++) {
                List<Integer> list = candidateArray.get(k);
                List<Integer> temp = new ArrayList(list);
                temp.add(i);
                if(temp.size() == n) {
                    for(j = 0; j < n; j++) {
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
}
