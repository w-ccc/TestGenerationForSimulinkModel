import Extractor.LoadFile;
import com.mathworks.toolbox.javabuilder.*;
import org.conqat.lib.simulink.builder.SimulinkModelBuildingException;
import run_simulink.Matlab;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Set<String> inPorts = new HashSet<>();
        try {
            inPorts = LoadFile.load(args);
        } catch (SimulinkModelBuildingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CTM ctm = new CTM(inPorts);
        ctm.nWise(2);
        //ctm.pairWise();
        List<Map<String, Double>> testCases = ctm.getTestCases();//TODO: store in excel
        System.out.println(testCases.size() + " " + testCases);
        Matlab rs = null;
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
        }
    }
}
