package edu.usu.cs.mas.managedisaster.service.util;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class ConfigImpl implements Config {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigImpl.class);
	
	private static final String VALUE = "value";
	private static final String TAG_NAME = "parameter";
	private static final String NAME = "name";
	
	private SAXBuilder builder;
	private File xmlFile;
	private Document document;
	
	@Override
	public String getString(String name) {
		Element parameter = getParameter(name);
		Attribute valueAttr = parameter.getAttribute(VALUE);
		String strValue = valueAttr.getValue();
		return strValue;
	}
	
	@Override
	public boolean getBoolean(String name) {
		Element parameter = getParameter(name);
		Attribute valueAttr = parameter.getAttribute(VALUE);
		String strValue = valueAttr.getValue();
		return Boolean.parseBoolean(strValue);
	}
	
	private Element getParameter(String name) {
		if(StringUtils.isBlank(name)) {
			return null;
		}
		if(document == null) {
			initializeDocument();
		}
		Element config = document.getRootElement();
		List<Element> parameters = config.getChildren(TAG_NAME);
		if(CollectionUtils.isEmpty(parameters)) {
			return null;
		}
		for(int i=0; i< parameters.size(); i++) {
			Element parameter = parameters.get(i);
			Attribute attributeName = parameter.getAttribute(NAME);
			if(attributeName == null) {
				continue;
			}
			String attributeNameValue = attributeName.getValue();
			if(attributeNameValue.equalsIgnoreCase(name)) {
				return parameter;
			}
		}
		return null;
	}

	private void initializeDocument() {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String fileName = classLoader.getResource("mas-config.xml").getFile();
			xmlFile = new File(fileName);
			builder = new SAXBuilder();
			document = (Document) builder.build(xmlFile);
		}
		catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Problem of initiating the config xml",e);
		}
	}
	

}
