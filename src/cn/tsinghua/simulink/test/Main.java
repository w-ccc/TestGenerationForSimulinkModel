package cn.tsinghua.simulink.test;
import Extractor.LoadFile;
import Extractor.ParameterType;
import org.conqat.lib.simulink.builder.SimulinkModelBuildingException;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Map<String, ParameterType> inPorts = new HashMap<>();
        try {
            inPorts = LoadFile.load(args);
        } catch (SimulinkModelBuildingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CTM ctm = new CTM();
        ctm.initCT(inPorts);
        //ctm.pairWise();
        if (inPorts.isEmpty())
            return;
        //List<Map<String, Double>> testCases = ctm.nWise(2);//TODO: store in excel
        List<Map<String, Double>> testCases = ctm.newNWise(2);
        System.out.println("testCases.size():" + testCases.size());
        /*Matlab rs = null;
        try {
            rs = new Matlab();
            for (Map<String, Double> testCase : testCases) {
                MWNumericArray input = new MWNumericArray(testCase.values().toArray(), MWClassID.DOUBLE);
                Object[] result = rs.run_simulink(1, args[0],input);
                //rs.main(args);
                break;
            }
        } catch (MWException e) {
            e.printStackTrace();
        }*/
        try {
//            RunSimulink.main(args, testCases);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
