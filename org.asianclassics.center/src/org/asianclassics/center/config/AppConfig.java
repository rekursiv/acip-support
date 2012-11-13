package org.asianclassics.center.config;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class AppConfig {

	private final static String defaultFileName = "acipcenter-config.js";
	private CenterAppConfigModel config;
	private ObjectMapper mapper;
	
	@Inject
	public AppConfig() {
		mapper = new ObjectMapper();
		config = new CenterAppConfigModel();
		
		if (!config.useDefaults && Files.exists(buildPath(defaultFileName))) {
			try {
				config = loadFromFile(defaultFileName, CenterAppConfigModel.class);
			} catch (Exception e) {
				stashError(e);
			}
			if (config==null || config.useDefaults) {
				config = new CenterAppConfigModel();
			}
		}
		
		if (config.loadFromFile!=null) {
			try {
				config = loadFromFile(config.loadFromFile, CenterAppConfigModel.class);
			} catch (Exception e) {
				stashError(e);
			}
		} else if (config.saveToFile!=null) {
			String fileName = config.saveToFile;
			if (fileName.isEmpty()) {
				fileName = defaultFileName;
				config.saveToFile = null;
			}
			try {
				saveToFile(fileName, config);
			} catch (Exception e) {
				stashError(e);
			}
		}
		
		
		config.setDebugConfig();    ////////  TEMP
		debug(config);
	}

	
	
	public CenterAppConfigModel get() {
		return config;
	}
	
	
	private void stashError(Exception e) {
		// TODO:  save error for later logging
		e.printStackTrace();
	}
	
	private Path buildPath(String fileName) {
		String pathStr = System.getProperty("user.dir")+"/"+fileName;
		System.out.println(pathStr);
		Path path = Paths.get(pathStr);
		return path;
	}
	
	private void saveToFile(String fileName, Object model) throws Exception {
		System.out.println("save: "+fileName);
		String txt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
		List<String> lines = Arrays.asList(txt.replace("\r", "").split("\\n"));
		Files.write(buildPath(fileName), lines, StandardCharsets.UTF_8);
	}

	private <T> T loadFromFile(String fileName, Class<T> modelType) throws Exception {
		System.out.println("load: "+fileName);
		StringBuilder sb = new StringBuilder();
		for (String line : Files.readAllLines(buildPath(fileName), StandardCharsets.UTF_8)) {
			if (sb.length()>0) sb.append("\n");
			sb.append(line);
		}
		return mapper.readValue(sb.toString(), modelType);
	}

	private void debug(Object obj) {
		if (obj==null) {
			System.out.println("!!!   NULL");
			return;
		}
		System.out.println("--------------");
		try {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("^^^^^^^^^^^^^^");
	}
	
}
