package de.ovgu.variantsync.applicationlayer.context;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.graphics.RGB;

import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaClass;

/**
 * Defines functions to manage contexts.
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
public interface IContextOperations {

	void activateContext(String featureExpression, String projectName);

	String getActiveFeatureContext();

	String getActiveProject();

	void recordCodeChange(List<String> changedCode, IFile res);

	void setContextColor(String featureExpression, RGB color);

	void stopRecording();

	void addContext(Context c);

	void addCode(String packageName, String className, List<String> code);

	void addCode(String packageName, String className, List<String> code,
			Context c);

	List<JavaClass> findJavaClass(String projectName, String className);
}