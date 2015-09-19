package de.ovgu.variantsync.applicationlayer.datamodel.context;

import javax.xml.bind.annotation.XmlAttribute;

public class CodeLine {

	private String code;
	private int line;

	public CodeLine() {
	}

	public CodeLine(String code, int line) {
		this.code = code;
		this.line = line;
	}

	/**
	 * @return the code
	 */

	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	@XmlAttribute
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the line
	 */

	public int getLine() {
		return line;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	@XmlAttribute
	public void setLine(int line) {
		this.line = line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CodeLine [line=" + line + ", code=" + code + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public CodeLine clone() throws CloneNotSupportedException {
		return new CodeLine(code, line);
	}

}
