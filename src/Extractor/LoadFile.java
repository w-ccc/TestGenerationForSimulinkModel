package Extractor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.conqat.lib.commons.logging.SimpleLogger;
import org.conqat.lib.simulink.builder.SimulinkModelBuilder;
import org.conqat.lib.simulink.builder.SimulinkModelBuildingException;
import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;

public class LoadFile {

	public static Map<String, ParameterType> load(String[] args) throws SimulinkModelBuildingException, IOException {
		File file = new File(args[0]);
		return LoadFromFile(file);
	}

	public static Map<String, ParameterType> LoadFromFile(File file) throws SimulinkModelBuildingException, IOException {
		Map<String, ParameterType> inPorts = new HashMap<>();
		// ArrayList<String> inPorts = new ArrayList<String>();
//		ArrayList<String> outPorts = new ArrayList<String>();
		try (SimulinkModelBuilder builder = new SimulinkModelBuilder(file, new SimpleLogger())) {
			SimulinkModel model = builder.buildModel();

			// list all blocks in the model
			for (SimulinkBlock block : model.getSubBlocks()) {
				if (block.getType().equals("Inport")) {
					ParameterType parameterType = new ParameterType(ParameterType.toTypeID(block.getParameter("OutDataTypeStr")));
					if (!block.getParameter("OutMin").equals("[]")) {
						parameterType.setMin(Double.parseDouble(block.getParameter("OutMin")));
					}
					if (!block.getParameter("OutMax").equals("[]")) {
						parameterType.setMax(Double.parseDouble(block.getParameter("OutMax")));
					}
					inPorts.put(block.getId(), parameterType);
				}
			}
			System.out.println("InPorts:" + inPorts.keySet());
			// System.out.println("OutPorts:" + outPorts);
			// render a block or model as PNG image
//            SimulinkBlockRenderer simulinkBlockRenderer = new SimulinkBlockRenderer();
			//BufferedImage image = SimulinkBlockRenderer.renderBlock(model);
			//ImageIO.write(image, "PNG", new File(file.getPath() + ".png"));
		}
		return inPorts;
	}

}
