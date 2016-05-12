package com.ilimi.taxonomy.content.common;

public class ContentErrorMessageConstants {
	
	public static final String XML_PARSE_CONFIG_ERROR = "Parse configuration error while parsing Content XML file.";
	
	public static final String XML_NOT_WELL_FORMED_ERROR = "Content XML is not well formed.";
	
	public static final String XML_IO_ERROR = "Input/Output Error while reading XML file.";

	public static final String XML_OBJECT_CONVERSION_CASTING_ERROR = "Something went wrong while converting Content XML Objects.";
	
	public static final String CONTROLLER_ASSESSMENT_ITEM_JSON_OBJECT_CONVERSION_CASTING_ERROR = "Invalid JSON !!! Something went wrong while converting Assessment Item JSON Objects.";
	
	private ContentErrorMessageConstants(){
	  throw new AssertionError();
	}

}
