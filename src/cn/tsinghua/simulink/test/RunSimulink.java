package cn.tsinghua.simulink.test;

import com.mathworks.engine.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.Arrays;

/**
 * Created by wendy on 2019/2/26.
 */
public class RunSimulink {
    public static void main(String[] args, List<Map<String, Double>> testCases) throws Exception {
        String modelName = args[0].substring(0, args[0].length() - 4);
        MatlabEngine eng = MatlabEngine.startMatlab();
        Future<Void>  fLoad = eng.evalAsync("load_system('" + args[0] + "')");
        while (!fLoad.isDone()){
            System.out.println("Loading Simulink model...");
            Thread.sleep(10000);
        }
        System.out.println("Running Simulation...");
        for (Map<String, Double> testCase : testCases) {
            eng.eval("simIn = Simulink.SimulationInput('" + modelName + "');");
            for (Map.Entry<String, Double> input : testCase.entrySet()) {
                //System.out.println("simIn = simIn.setVariable('" + input.getKey().split("/")[1] + "', " + input.getValue() + ");");
                eng.eval("simIn = simIn.setVariable('" + input.getKey().split("/")[1] + "', " + input.getValue() + ");");
            }
            Future<Void> fSim = eng.evalAsync("simOut = sim(simIn);");
            while (!fSim.isDone()) {
                Thread.sleep(10000);
            }
            // Get simulation data
            eng.eval("y = simOut.get('yout');");
            eng.eval("t = simOut.get('tout');");
            eng.eval("n = y.numElements;");
            // Graph results and create image file
            //eng.eval("plot(t,y)");
            //eng.eval("print('vdpPlot','-djpeg')");
            // Return results to Java
            double n = eng.getVariable("n");
            double[] y = new double[(int)n];
            for (int i = 1; i <= n; i++) {
                eng.eval("yy = y{" + i + "}.Values.Data(1);");
                y[i-1] = eng.getVariable("yy");
            }
            //double[][] y = eng.getVariable("y");
            //double[] t = eng.getVariable("t");
            // Display results
            //System.out.println("Simulation result " + Arrays.deepToString(y));
            System.out.println("Simulation input " + testCase);
            System.out.println("Simulation result " + Arrays.toString(y));
            //System.out.println("Time vector " + Arrays.toString(t));
        }
        eng.close();
    }
}
