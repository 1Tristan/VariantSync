package de.ovgu.variantsync.applicationlayer.features.mapping;

import java.util.List;

import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeFragment;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaElement;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaProject;
import de.ovgu.variantsync.presentationlayer.controller.data.MappingElement;

public interface IMappingOperations {

	JavaElement getElement(List<JavaElement> javaPackages, String className,
			String classPath);

	JavaProject addMapping(JavaProject project, MappingElement mapping);

	JavaElement createElement(String pathToProject, String elementName,
			String pathToElement);

	boolean containsElement(List<JavaElement> elements, String elementName,
			String pathToElement, String contentOfElement);

	void removeMapping(String elementName, String pathToElement,
			CodeFragment code, JavaElement project);
}