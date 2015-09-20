package de.ovgu.variantsync.applicationlayer.context;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeHighlighting;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaClass;

/**
 * Defines functions to manage contexts.
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
public interface IContextOperations {

	void activateContext(String featureExpression);

	String getActiveFeatureContext();

	String getActiveProject();

	void recordCodeChange(List<String> changedCode, String projectName,
			String pathToProject, IFile res);

	void setContextColor(String featureExpression, CodeHighlighting color);

	void stopRecording();

	void addContext(Context c);

	void addCode(String projectName, String packageName, String className,
			List<String> code);

	void addCode(String projectName, String packageName, String className,
			List<String> code, Context c);

	Map<String, List<JavaClass>> findJavaClass(String projectName,
			String className);

	CodeHighlighting findColor(String featureExpression);

	Context getContext(String featureExpression);

}
