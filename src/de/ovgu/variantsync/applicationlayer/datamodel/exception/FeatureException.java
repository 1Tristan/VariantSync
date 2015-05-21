package de.ovgu.variantsync.applicationlayer.datamodel.exception;

/**
 * Exception which can be thrown in applicationlayer handling features.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 20.05.2015
 */
@SuppressWarnings("serial")
public class FeatureException extends Exception {

	public FeatureException() {
	}

	public FeatureException(String arg0) {
		super(arg0);
	}

	public FeatureException(Throwable arg0) {
		super(arg0);
	}

	public FeatureException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FeatureException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
