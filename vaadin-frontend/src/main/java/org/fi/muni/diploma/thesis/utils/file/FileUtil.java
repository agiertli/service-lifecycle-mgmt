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

	public InputStream processSvg(InputStream is, String processName, String processState) {

		String input = this.getStringFromInputStream(is);
		String output = "";
		
		
//	logger.info("process name svg:"+processName);
//	logger.info("process state svg:"+processState);

		// both processes have different process definition
		switch (processState) {

		case "New": {
			
			logger.info("svg processing new stated");
			//
			if (processName.toLowerCase().contains("new")) {
				
				logger.info("svg processing new process");
				
//logger.info("Did we find the shit???:"+	input.indexOf("<rect id=\"_75122166-EEDD-4B00-A8B5-93DA91720E76bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" stroke=\"#000000\" stroke-width=\"1\""));
				
				output = input.replace("<rect id=\"_75122166-EEDD-4B00-A8B5-93DA91720E76bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" stroke=\"#000000\" stroke-width=\"1\"",
						"<rect id=\"_75122166-EEDD-4B00-A8B5-93DA91720E76bg_frame\" oryx:resize=\"vertical horizontal\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" stroke=\"#66FF33\" stroke-width=\"3\" fill=\"url(#_75122166-EEDD-4B00-A8B5-93DA91720E76background) #ffffcc\" />");
	//	logger.info("svg output:"+output);
			}

			break;
		}
		default: {

			break;
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