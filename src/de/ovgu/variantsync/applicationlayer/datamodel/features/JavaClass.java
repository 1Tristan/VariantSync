package de.ovgu.variantsync.applicationlayer.datamodel.features;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import de.ovgu.variantsync.applicationlayer.features.mapping.UtilOperations;
import de.ovgu.variantsync.presentationlayer.controller.data.JavaElements;

public class JavaClass extends JavaElement {

	public JavaClass() {
		super();
		codeLines = new ArrayList<CodeLine>();
	}

	private List<CodeLine> codeLines;

	public JavaClass(String name, String path, JavaElement member) {
		super(name, path, JavaElements.CLASS);
		addChild(member);
	}

	public JavaClass(String name, String path, List<JavaElement> members) {
		super(name, path, JavaElements.CLASS);
		setChildren(members);
	}

	public JavaClass(String name, String path, List<String> code,
			int numberOfCodeLines) {
		super(name, path, JavaElements.CLASS);
		addCode(new CodeFragment(code, 0, numberOfCodeLines, 0));
	}

	public JavaClass(String name, String path, CodeFragment code) {
		super(name, path, JavaElements.CLASS);
		addCode(code);
	}

	public JavaClass(String name, String path) {
		super(name, path, JavaElements.CLASS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("JavaClass [name=" + getName() + ", codeLines=");
		List<CodeLine> listCl = codeLines;
		for (CodeLine cl : listCl) {
			sb.append("\n\t");
			sb.append(cl.toString());
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	protected JavaElement getConcreteType(String name, String path) {
		return new JavaClass(name, path);
	}

	@XmlElementWrapper(name = "code")
	@XmlElement(name = "codeline")
	public List<CodeLine> getCodeLines() {
		return codeLines;
	}

	public List<CodeLine> getClonedCodeLines() {
		List<CodeLine> tmpList = new ArrayList<CodeLine>();
		for (CodeLine cl : codeLines) {
			try {
				tmpList.add(cl.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return tmpList;
	}

	public void addCode(CodeFragment code) {
		codeLines = UtilOperations.getInstance().addCode(code, codeLines);
	}

	public void addCode(List<CodeLine> code) {
		codeLines.addAll(code);
	}

	public void removeCode(int start, int end) {
		codeLines = UtilOperations.getInstance().removeCode(start, end,
				codeLines);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public JavaElement clone() throws CloneNotSupportedException {
		List<JavaElement> children = getChildren();
		List<JavaElement> clonedChildren = new ArrayList<JavaElement>();
		if (children != null && !children.isEmpty() && children.get(0) != null) {
			for (JavaElement element : children) {
				clonedChildren.add(element.clone());
			}
		}
		JavaElement copy = getConcreteType(getName(), getPath());
		if (clonedChildren != null) {
			copy.setChildren(clonedChildren);
		}
		List<CodeLine> clonedCodeFragments = new CopyOnWriteArrayList<CodeLine>();
		List<CodeLine> code = getClonedCodeLines();
		if (code != null) {
			for (CodeLine fragment : code) {
				clonedCodeFragments.add(fragment.clone());
			}
			((JavaClass) copy).setCodeLines(clonedCodeFragments);
		}
		return copy;
	}

	/**
	 * @param codeLines
	 *            the codeFragments to set
	 */
	public boolean setCodeLines(List<CodeLine> codeLines) {
		this.codeLines.clear();
		for (CodeLine cl : codeLines) {
			try {
				this.codeLines.add(cl.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
