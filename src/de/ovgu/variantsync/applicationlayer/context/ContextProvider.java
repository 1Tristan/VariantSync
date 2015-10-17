package de.ovgu.variantsync.applicationlayer.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ovgu.variantsync.applicationlayer.AbstractModel;
import de.ovgu.variantsync.applicationlayer.Util;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeHighlighting;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaProject;
import de.ovgu.variantsync.presentationlayer.view.context.FeatureContextSelection;

/**
 * Receives controller invocation as part of MVC implementation and encapsulates
 * functionality of its package.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
public class ContextProvider extends AbstractModel implements
		IContextOperations {

	private ContextHandler contextHandler;

	public ContextProvider() {
		contextHandler = ContextHandler.getInstance();
	}

	@Override
	public void activateContext(String featureExpression) {
		contextHandler.activateContext(featureExpression);
	}

	@Override
	public String getActiveFeatureContext() {
		return FeatureContextSelection.activeContext;
	}

	@Override
	public String getActiveProject() {
		return FeatureContextSelection.activeProject;
	}

	@Override
	public void recordCodeChange(List<String> changedCode, String projectName,
			String pathToProject, String packageName, String className,
			List<String> wholeClass) {
		System.out.println("\n=== Changed Code ===");
		System.out.println(changedCode.toString());
		contextHandler.recordCodeChange(projectName, pathToProject,
				changedCode, className, packageName, wholeClass);
	}

	@Override
	public void setContextColor(String featureExpression, CodeHighlighting color) {
		contextHandler.setContextColor(featureExpression, color);
	}

	@Override
	public void stopRecording() {
		contextHandler.stopRecording();
	}

	@Override
	public void addContext(Context c) {
		ContextHandler.getInstance().addContext(c);
	}

	@Override
	public void addCode(String projectName, String packageName,
			String className, List<String> code, List<String> wholeClass) {
		ContextAlgorithm ca = new ContextAlgorithm(ContextHandler.getInstance()
				.getActiveContext());
		ca.addCode(projectName, packageName, className, code, wholeClass);
	}

	@Override
	public void addCode(String projectName, String packageName,
			String className, List<String> code, Context c,
			List<String> wholeClass) {
		ContextAlgorithm ca = new ContextAlgorithm(c);
		ca.addCode(projectName, packageName, className, code, wholeClass);
	}

	@Override
	public Map<String, List<JavaClass>> findJavaClass(String projectName,
			String className) {
		Map<String, List<JavaClass>> result = new HashMap<String, List<JavaClass>>();
		Collection<Context> contexts = ContextHandler.getInstance()
				.getAllContexts();
		for (Context c : contexts) {
			List<JavaClass> classes = new ArrayList<JavaClass>();
			JavaProject jp = c.getJavaProject(projectName);
			if (jp != null && jp.getName().equals(projectName)) {
				Util.getClassesByClassName(jp.getChildren(), classes, className);
			}
			result.put(c.getFeatureExpression(), classes);
		}
		return result;
	}

	@Override
	public CodeHighlighting findColor(String featureExpression) {
		Collection<Context> contexts = ContextHandler.getInstance()
				.getAllContexts();
		for (Context c : contexts) {
			if (c.getFeatureExpression().equals(featureExpression)) {
				return c.getColor();
			}
		}
		return null;
	}

	@Override
	public Context getContext(String featureExpression) {
		return ContextHandler.getInstance().getContext(featureExpression);
	}

	@Override
	public void deleteAllContexts() {
		contextHandler.clean();
	}
}