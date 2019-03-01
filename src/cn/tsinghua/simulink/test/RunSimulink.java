package cn.tsinghua.simulink.test;

import com.mathworks.engine.*;
import java.util.concurrent.Future;
import java.util.Arrays;

/**
 * Created by wendy on 2019/2/26.
 */
public class RunSimulink {
    public static void main(String[] args) throws Exception {
        MatlabEngine eng = MatlabEngine.startMatlab();
        Future<Void>  fLoad = eng.evalAsync("load_system('" + args[0] + "')");
        while (!fLoad.isDone()){
            System.out.println("Loading Simulink model...");
            Thread.sleep(10000);
        }
        Future<Void> fSim = eng.evalAsync("simOut = sim('" + args[0] +"');");
        while (!fSim.isDone()) {
            System.out.println("Running Simulation...");
            Thread.sleep(10000);
        }
        // Get simulation data
        eng.eval("y = simOut.get('yOut');");
        eng.eval("t = simOut.get('tOut');");
        // Graph results and create image file
        eng.eval("plot(t,y)");
        eng.eval("print('vdpPlot','-djpeg')");
        // Return results to Java
        double[][] y = eng.getVariable("y");
        double[] t = eng.getVariable("t");
        // Display results
        System.out.println("Simulation result " + Arrays.deepToString(y));
        System.out.println("Time vector " + Arrays.toString(t));
        eng.close();
    }
}
