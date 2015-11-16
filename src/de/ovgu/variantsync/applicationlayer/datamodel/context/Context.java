package de.ovgu.variantsync.applicationlayer.datamodel.context;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.ovgu.variantsync.VariantSyncConstants;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
@XmlRootElement(name = "Context")
public class Context {

	private String featureExpression;
	private Map<String, JavaProject> javaProjects;
	private CodeHighlighting color;
	private ChangeLogMap changeLog;

	public Context() {
		this.javaProjects = new HashMap<String, JavaProject>();
		this.color = CodeHighlighting.YELLOW;
		this.changeLog = new ChangeLogMap();
	}

	public Context(String featureExpression) {
		this.featureExpression = featureExpression;
		this.javaProjects = new HashMap<String, JavaProject>();
		if (featureExpression.equals(VariantSyncConstants.DEFAULT_CONTEXT)) {
			this.color = CodeHighlighting.DEFAULTCONTEXT;
		} else {
			this.color = CodeHighlighting.YELLOW;
		}
		this.changeLog = new ChangeLogMap();
	}

	public void initProject(String projectName, String pathToProject) {
		javaProjects.put(projectName, new JavaProject(projectName,
				pathToProject));
	}

	public boolean containsProject(String projectName) {
		return javaProjects.containsKey(projectName);
	}

	/**
	 * @return the featureExpression
	 */
	public String getFeatureExpression() {
		return featureExpression;
	}

	/**
	 * @param featureExpression
	 *            the featureExpression to set
	 */
	public void setFeatureExpression(String featureExpression) {
		this.featureExpression = featureExpression;
	}

	/**
	 * @return the javaProject
	 */
	public JavaProject getJavaProject(String projectName) {
		return javaProjects.get(projectName);
	}

	/**
	 * @param javaProject
	 *            the javaProject to set
	 */
	public void setJavaProject(JavaProject javaProject) {
		this.javaProjects.put(javaProject.getName(), javaProject);
	}

	public String getPathToProject(String projectName) {
		if (javaProjects == null || javaProjects.get(projectName) == null)
			return null;
		return javaProjects.get(projectName).getPath();
	}

	public void setPathToProject(String projectName, String path) {
		if (javaProjects.get(projectName) == null) {
			javaProjects.put(projectName, new JavaProject(projectName, path));
			return;
		}
		javaProjects.get(projectName).setPath(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Context [featureExpression=" + featureExpression
				+ ", javaProject=" + javaProjects + ", color=" + color + "]";
	}

	/**
	 * @return the color
	 */
	public CodeHighlighting getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(CodeHighlighting color) {
		this.color = color;
	}

	/**
	 * @return the javaProjects
	 */
	@XmlElement(name = "color")
	public Map<String, JavaProject> getJavaProjects() {
		return javaProjects;
	}

	/**
	 * @param javaProjects
	 *            the javaProjects to set
	 */
	public void setJavaProjects(Map<String, JavaProject> javaProjects) {
		this.javaProjects = javaProjects;
	}

	public boolean isSynchronized(long key, String target) {
		return changeLog.contains(key, target);
	}

	public void addSynchronizedChange(long key, String target) {
		changeLog.put(key, target);
	}

	public void removeSynchronizedChange(long key) {
		changeLog.removeChange(key);
	}

	public ChangeLogMap getChangeLog() {
		return changeLog;
	}

	/**
	 * @param changeLog
	 *            the changeLog to set
	 */
	public void setChangeLog(ChangeLogMap changeLog) {
		this.changeLog = changeLog;
	}
}
