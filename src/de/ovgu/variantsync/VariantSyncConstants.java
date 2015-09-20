package de.ovgu.variantsync;

/**
 * Describe file names and paths for feature handling. E.g. path to config
 * files, name of feature model file, ...
 * 
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 20.05.2015
 */
public final class VariantSyncConstants {

	public static final String PLUGIN_ID = "de.ovgu.variantsync";
	public static final String ADMIN_FOLDER = ".variantsync";
	public static final String ADMIN_FILE = ".variantsyncInfo";
	public static final String CONTEXT_PATH = "/variantsyncFeatureInfo/context";
	public static final String FEATURE_EXPRESSION_PATH = "/variantsyncFeatureInfo/featureExpression/FeatureExpressions.xml";
	public static final String DEFAULT_CONTEXT = "Default_Context";

	private VariantSyncConstants() {
	}
}
