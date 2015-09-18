package de.ovgu.variantsync.applicationlayer.datamodel.context;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.swt.graphics.RGB;

import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaProject;

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
	private RGB color;

	public Context() {
		this.javaProjects = new HashMap<String, JavaProject>();
	}

	public Context(String featureExpression) {
		this.featureExpression = featureExpression;
		this.javaProjects = new HashMap<String, JavaProject>();
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
		return javaProjects.get(projectName).getPath();
	}

	public void setPathToProject(String projectName, String path) {
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
	public RGB getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(RGB color) {
		this.color = color;
	}

	/**
	 * @return the javaProjects
	 */
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

}
