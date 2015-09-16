package de.ovgu.variantsync.applicationlayer.datamodel.context;

import java.util.LinkedList;
import java.util.List;

import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeFragment;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
public class Code {

	private List<CodeFragment> codeFragments;

	public Code() {
		codeFragments = new LinkedList<CodeFragment>();
	}

	/**
	 * @return the codeFragments
	 */
	public List<CodeFragment> getCodeFragments() {
		return codeFragments;
	}

	/**
	 * @param codeFragments
	 *            the codeFragments to set
	 */
	public void setCodeFragments(List<CodeFragment> codeFragments) {
		this.codeFragments = codeFragments;
	}

	public void addCodeFragment(CodeFragment fragment) {
		codeFragments.add(fragment);
	}
}
