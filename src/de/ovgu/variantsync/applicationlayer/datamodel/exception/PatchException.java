package de.ovgu.variantsync.applicationlayer.datamodel.exception;

/**
 * Exception which can be thrown in applicationlayer using patch functions, e.g.
 * from difflib.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 21.05.2015
 */
@SuppressWarnings("serial")
public class PatchException extends Exception {

	/**
	 * Creates exception with individual error message and throwable object
	 * containing stacktrace.
	 * 
	 * @param arg0
	 *            individual error message
	 * @param arg1
	 *            throwable object containing stacktrace
	 */
	public PatchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
