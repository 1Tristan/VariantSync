package de.ovgu.variantsync.applicationlayer.datamodel.context;

import java.util.List;

public class CodeFragment {

	private List<String> code;
	private int startLine;
	private int endLine;
	private int offset;
	private String className;
	private String packageName;

	public CodeFragment(List<String> code, int startLine, int endLine, int offset) {
		this.code = code;
		this.startLine = startLine;
		this.endLine = endLine;
		this.offset = offset;
	}

	public CodeFragment(List<String> code, String className, String packageName) {
		this.code = code;
		this.className = className;
		this.packageName = packageName;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return code.size();
	}

	/**
	 * @return the code
	 */
	public List<String> getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(List<String> code) {
		this.code = code;
	}

	/**
	 * @return the startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * @param startLine
	 *            the startLine to set
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CodeSelection [code=\n" + code + ", \nstartLine=" + startLine
				+ ", endLine=" + endLine + ", offset=" + offset + "]\n";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected CodeFragment clone() throws CloneNotSupportedException {
		return new CodeFragment(code, startLine, endLine, offset);
	}

	/**
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
