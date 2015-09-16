package de.ovgu.variantsync.applicationlayer.datamodel.features;

import java.util.ArrayList;
import java.util.List;

public class Feature {

	private String name;
	private List<JavaElement> elements;

	public Feature(String name, List<JavaElement> elements) {
		this.name = name;
		this.elements = elements;
	}

	public Feature(String name, JavaElement project) {
		this.name = name;
		addProject(project);
	}

	public void addProject(JavaElement project) {
		if (elements == null) {
			elements = new ArrayList<JavaElement>();
		}
		elements.add(project);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the projects
	 */
	public List<JavaElement> getElements() {
		return elements;
	}

	/**
	 * @param elements
	 *            the projects to set
	 */
	public void setElements(List<JavaElement> elements) {
		this.elements = elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Feature [name=" + name + ", \n\telements=" + elements + "]";
	}

}
