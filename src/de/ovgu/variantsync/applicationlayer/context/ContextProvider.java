package de.ovgu.variantsync.applicationlayer.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.graphics.RGB;

import de.ovgu.variantsync.applicationlayer.AbstractModel;
import de.ovgu.variantsync.applicationlayer.Util;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaProject;
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
	public void activateContext(String featureExpression, String projectName) {
		if (contextHandler.existsContext(featureExpression, projectName)) {
			contextHandler.continueRecording(featureExpression, projectName);
		} else {
			contextHandler.startNewContext(featureExpression, projectName);
		}
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
	public void recordCodeChange(List<String> changedCode, IFile res) {
		String packageName = res.getLocation().toString();
		packageName = packageName.substring(packageName.indexOf("src") + 4,
				packageName.lastIndexOf("/"));
		packageName = packageName.replace("/", ".");
		contextHandler
				.recordCodeChange(changedCode, res.getName(), packageName);
	}

	@Override
	public void setContextColor(String featureExpression, RGB color) {
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
	public void addCode(String packageName, String className, List<String> code) {
		ContextAlgorithm ca = new ContextAlgorithm(ContextHandler.getInstance()
				.getActiveContext());
		ca.addCode(packageName, className, code);
	}

	@Override
	public void addCode(String packageName, String className,
			List<String> code, Context c) {
		ContextAlgorithm ca = new ContextAlgorithm(c);
		ca.addCode(packageName, className, code);
	}

	@Override
	public List<JavaClass> findJavaClass(String projectName, String className) {
		List<JavaClass> classes = new ArrayList<JavaClass>();
		Collection<Context> contexts = ContextHandler.getInstance()
				.getAllContexts();
		for (Context c : contexts) {
			JavaProject jp = c.getJavaProject();
			if (jp.getName().equals(projectName)) {
				Util.getClassesWithClassName(jp.getChildren(), classes,
						className);
			}
		}
		return classes;
	}
}