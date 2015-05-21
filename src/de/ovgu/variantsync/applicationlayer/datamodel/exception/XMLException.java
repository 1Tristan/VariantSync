package de.ovgu.variantsync.applicationlayer.datamodel.exception;

/**
 * Exception which can be thrown in persistancelayer reading or writing xml
 * files.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 19.05.2015
 */
@SuppressWarnings("serial")
public class XMLException extends Exception {

	public XMLException() {
	}

	public XMLException(String arg0) {
		super(arg0);
	}

	public XMLException(Throwable arg0) {
		super(arg0);
	}

	public XMLException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public XMLException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
