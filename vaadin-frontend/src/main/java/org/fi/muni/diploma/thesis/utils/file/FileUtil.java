package org.fi.muni.diploma.thesis.utils.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class FileUtil {
private static final Logger logger = Logger.getLogger(FileUtil.class.getName());
	public InputStream getFileFromClasspath(String name) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = null;

		if (name.toLowerCase().contains("existing")) {

			resourceStream = loader.getResourceAsStream("img/existingservice.svg");

		} else if (name.toLowerCase().contains("new")) {

			resourceStream = loader.getResourceAsStream("img/newservice.svg");

		}

		return resourceStream;
	}

	public InputStream processSvg(InputStream is, String processName, String processState, Boolean servicesQueried, Boolean passedTests, Boolean serviceSelected) {

		String input = this.getStringFromInputStream(is);
		String output = "";
		Boolean processSvg = true;
		
		
	//logger.info("process name svg:"+processName);
	//logger.info("process state svg:"+processState);
	
	//first task
	if (processState == null && processName.toLowerCase().contains("exist")) {
		
		output = input.replace("<rect id=\"_E35C0A48-354D-43D1-A277-9AC67A3AE5B2bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#_E35C0A48-354D-43D1-A277-9AC67A3AE5B2background);stroke:#000000;stroke-width:1\" />",
				"<rect id=\"_E35C0A48-354D-43D1-A277-9AC67A3AE5B2bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#_E35C0A48-354D-43D1-A277-9AC67A3AE5B2background);stroke:#00FF00;stroke-width:3\" />");
		
		processSvg = false;
	}
	
	if (!serviceSelected && processName.toLowerCase().contains("exist") && processState != null) {
		
		//we selecting service
		
		output = input.replace("<rect id=\"_34BA16F3-5FD2-4D3C-9B3A-B5483FF6605Bbg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#_34BA16F3-5FD2-4D3C-9B3A-B5483FF6605Bbackground);stroke:#000000;stroke-width:1\" />",
				"<rect id=\"_34BA16F3-5FD2-4D3C-9B3A-B5483FF6605Bbg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#_34BA16F3-5FD2-4D3C-9B3A-B5483FF6605Bbackground);stroke:#00FF00;stroke-width:3\" />");
		processSvg = false;
	}
	
	
if (processState!=null && processSvg) {
		// both processes have different process definition
		switch (processState) {

		case "New": {
			
		//	logger.info("svg processing new stated");
			//
			if (processName.toLowerCase().contains("new")) {
				
				//logger.info("svg processing new process");
				
//logger.info("Did we find the shit???:"+	input.indexOf("<rect id=\"_75122166-EEDD-4B00-A8B5-93DA91720E76bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" stroke=\"#000000\" stroke-width=\"1\""));
				
				output = input.replace(
						"<rect id=\"_75122166-EEDD-4B00-A8B5-93DA91720E76bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#radialGradient5251);stroke:#000000;stroke-width:1\" />",
						"<rect id=\"_75122166-EEDD-4B00-A8B5-93DA91720E76bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#radialGradient5251);stroke:#00FF00;stroke-width:3\" />");
		//logger.info("svg output:"+output);
			}
			
			break;
		}
		
		case "Initial": {
			
			if (processName.toLowerCase().contains("new")) {
				
				output = input.replace
						("rect id=\"_6E3BA68D-561A-4EE5-9E9D-C2B9437E1CC0frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />",
					     "rect id=\"_6E3BA68D-561A-4EE5-9E9D-C2B9437E1CC0frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");
				
			}
			
			
			
			break;
		}
		
		case "registered": {
			if (processName.toLowerCase().contains("new")) {
				if (!servicesQueried) {
				output = input.replace("<rect id=\"_7A06414E-66A9-4AA3-90DF-7580708F55B1frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />"
						,"<rect id=\"_7A06414E-66A9-4AA3-90DF-7580708F55B1frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");
				} else {
					
					output = input.replace("<rect id=\"_5AF26B3E-895F-4B56-B07B-E095C7FB2C7Ebg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#radialGradient5257);stroke:#000000;stroke-width:1\" />",
							"<rect id=\"_5AF26B3E-895F-4B56-B07B-E095C7FB2C7Ebg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#radialGradient5257);stroke:#00FF00;stroke-width:3\" />");
				}
			}
			
			break;
			
		}
		
		case "InTest": {
			
			//still in test
			if (processName.toLowerCase().contains("new") && passedTests) {
			
				output = input.replace("<rect id=\"_EE6CA2B1-51CF-460B-9069-3147DBA9AA19frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />",
						"<rect id=\"_EE6CA2B1-51CF-460B-9069-3147DBA9AA19frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");
			}
			//evaluate tests result
			else if (processName.toLowerCase().contains("new") && !passedTests) {
				
				output = input.replace("<rect id=\"_4245D8F1-E391-4D62-8DA1-2A888705FD9Cbg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#radialGradient5311);stroke:#000000;stroke-width:1\" />",
						"<rect id=\"_4245D8F1-E391-4D62-8DA1-2A888705FD9Cbg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#radialGradient5311);stroke:#00FF00;stroke-width:3\" />");
				
			}
			
			else if (processName.toLowerCase().contains("exist") && passedTests) {
				
				output = input.replace("<rect id=\"_551CFE4C-F00F-42C9-A252-F702C1A339B5frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />",
						"<rect id=\"_551CFE4C-F00F-42C9-A252-F702C1A339B5frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");
			}
			
			else if (processName.toLowerCase().contains("exist") && !passedTests) {
				
				output = input.replace("<rect id=\"_DC411D5C-CD72-4F6B-BB58-610ED5DC33D1bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#_DC411D5C-CD72-4F6B-BB58-610ED5DC33D1background);stroke:#000000;stroke-width:1\" />",
						"<rect id=\"_DC411D5C-CD72-4F6B-BB58-610ED5DC33D1bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:url(#_DC411D5C-CD72-4F6B-BB58-610ED5DC33D1background);stroke:#00FF00;stroke-width:3\" />");
				
			}
			
			
			break;
		}
		
		case "Available": {
			
			if (processName.toLowerCase().contains("new")) {
				
				output = input.replace("<rect id=\"_04D14669-B0C7-4052-A073-5EA3008B8417frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />",
						"<rect id=\"_04D14669-B0C7-4052-A073-5EA3008B8417frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");
				
			}
			else if (processName.toLowerCase().contains("exist")) {
				
				output = input.replace("<rect id=\"_EA37E573-396E-40AB-977C-50BB516005E5frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />",
						"<rect id=\"_EA37E573-396E-40AB-977C-50BB516005E5frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");
				
			}
			
			break;
		}
		
		case "Deprecated": {
			if (processName.toLowerCase().contains("new")) {
				
				output = input.replace("<rect id=\"_50E71468-5EE8-4172-BFB5-7E80F484E994frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />",
						"<rect id=\"_50E71468-5EE8-4172-BFB5-7E80F484E994frame\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");
			}
			else if (processName.toLowerCase().contains("exist")) {
				
				output = input.replace("<rect id=\"_49407B96-9644-4699-9E13-E3B7C6EA42FCframe\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#000000;stroke-width:4\" />",
						"<rect id=\"_49407B96-9644-4699-9E13-E3B7C6EA42FCframe\" oryx:resize=\"vertical horizontal\" oryx:anchors=\"bottom top right left\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"fill:none;stroke:#00FF00;stroke-width:4\" />");	
			}
			break;
		}
		default: {

			break;
		}
		}
}

		// convert String into InputStream
		 InputStream outputStream = new ByteArrayInputStream(output.getBytes());
		return outputStream;
	}

	/**
	 * internal method for converting inputstream to string
	 * 
	 * @param is
	 * @return
	 */
	private String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}