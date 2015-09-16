package de.ovgu.variantsync.applicationlayer.datamodel.context;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.swt.graphics.RGB;

import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeLine;
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
	private JavaProject javaProject;
	private String id;
	private String pathToProject;
	private RGB color;

	public Context() {
	}

	public Context(String projectName, String pathToProject,
			String featureExpression) {
		this.featureExpression = featureExpression;
		this.javaProject = new JavaProject(projectName, pathToProject);
		this.id = featureExpression + "$" + projectName;
		this.pathToProject = pathToProject;
	}

	public List<CodeLine> getCodeLines() {
		return javaProject.getClonedCodeLines();
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
	public JavaProject getJavaProject() {
		return javaProject;
	}

	/**
	 * @param javaProject
	 *            the javaProject to set
	 */
	public void setJavaProject(JavaProject javaProject) {
		this.javaProject = javaProject;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Context [featureExpression=" + featureExpression
				+ ", javaProject=" + javaProject + ", id=" + id + "]";
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
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the pathToProject
	 */
	public String getPathToProject() {
		return pathToProject;
	}

	/**
	 * @param pathToProject
	 *            the pathToProject to set
	 */
	public void setPathToProject(String pathToProject) {
		this.pathToProject = pathToProject;
	}

	/**
	 * 
	 * @return the pathOfJavaProject
	 */
	@XmlTransient
	public String getPathOfJavaProject() {
		return javaProject.getPath();
	}

	/**
	 * 
	 * @return the nameOfJavaProject
	 */
	@XmlTransient
	public String getNameOfJavaProject() {
		return javaProject.getName();
	}
}
