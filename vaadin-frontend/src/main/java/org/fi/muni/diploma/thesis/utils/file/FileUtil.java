package org.fi.muni.diploma.thesis.utils.file;

import java.io.InputStream;

public class FileUtil {

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
}