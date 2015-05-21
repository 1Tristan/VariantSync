package de.ovgu.variantsync.applicationlayer.datamodel.resources;

/**
 * Describe types for resource changes. E.g. a resource (like a java file or a
 * folder) can be changed, removed oder added.
 * 
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 15.05.2015
 */
public final class ChangeTypes {

	public final static String CHANGE = "CHANGE";
	public final static String REMOVEFOLDER = "REMOVEFOLDER";
	public final static String REMOVEFILE = "REMOVEFILE";
	public final static String ADDFOLDER = "ADDFOLDER";
	public final static String ADDFILE = "ADDFILE";
}
