package cn.tsinghua.simulink.extractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

import javax.imageio.ImageIO;

import org.conqat.lib.commons.logging.SimpleLogger;
import org.conqat.lib.simulink.builder.SimulinkModelBuilder;
import org.conqat.lib.simulink.builder.SimulinkModelBuildingException;
import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.conqat.lib.simulink.util.SimulinkBlockRenderer;

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
			BufferedImage image = SimulinkBlockRenderer.renderBlock(model);
			ImageIO.write(image, "PNG", new File(file.getPath() + ".png"));
		}
		return inPorts;
	}
	
	public static void main(String[] args) {
		try {
			LoadFile.LoadFromFile(new File("test_model.mdl"));
		} catch (SimulinkModelBuildingException | IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<ExternalInput> loadExternalInput(File file) throws ZipException, IOException, SimulinkModelBuildingException {
		ArrayList<ExternalInput> external_inputs = new ArrayList<ExternalInput>();
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
					external_inputs.add(new ExternalInput(block.getId(), null, parameterType));
				}
			}
//			System.out.println("InPorts:" + in_ports.keySet());
			// System.out.println("OutPorts:" + outPorts);
			// render a block or model as PNG image
//            SimulinkBlockRenderer simulinkBlockRenderer = new SimulinkBlockRenderer();
//			BufferedImage image = SimulinkBlockRenderer.renderBlock(model);
//			ImageIO.write(image, "PNG", new File(file.getPath() + ".png"));
		}
//		return in_ports;
		return external_inputs;
	}

}
