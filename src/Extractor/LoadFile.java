package Extractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.conqat.lib.commons.logging.SimpleLogger;
import org.conqat.lib.simulink.builder.SimulinkModelBuilder;
import org.conqat.lib.simulink.builder.SimulinkModelBuildingException;
import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.conqat.lib.simulink.util.SimulinkBlockRenderer;

public class LoadFile {
    public static Set<String> load(String[] args)
            throws SimulinkModelBuildingException, IOException {
        File file = new File(args[0]);
        Set<String> inPorts = new HashSet<>();
        //ArrayList<String> inPorts = new ArrayList<String>();
        ArrayList<String> outPorts = new ArrayList<String>();
        try (SimulinkModelBuilder builder = new SimulinkModelBuilder(file,
                new SimpleLogger())) {
            SimulinkModel model = builder.buildModel();

            // list all blocks in the model
            for (SimulinkBlock block : model.getSubBlocks()) {
                if (block.getType().equals("Inport")) {
                    inPorts.add(block.getId());
                } else if (block.getType().equals("Outport")) {
                    outPorts.add(block.getId());
                }
                //System.out.println("Block: " + block.getName());
            }
            System.out.println("InPorts:" + inPorts);
            //System.out.println("OutPorts:" + outPorts);
            // render a block or model as PNG image
            SimulinkBlockRenderer simulinkBlockRenderer = new SimulinkBlockRenderer();
            BufferedImage image = simulinkBlockRenderer.renderBlock(model);
            ImageIO.write(image, "PNG", new File(file.getPath() + ".png"));
        }
        return inPorts;
    }
}
