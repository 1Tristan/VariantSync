package de.ovgu.variantsync.applicationlayer.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.RGB;

import de.ovgu.variantsync.VariantSyncConstants;
import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.Util;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaElement;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaProject;
import de.ovgu.variantsync.persistencelayer.IPersistanceOperations;
import de.ovgu.variantsync.presentationlayer.view.codemapping.MarkerHandler;
import de.ovgu.variantsync.presentationlayer.view.codemapping.MarkerInformation;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
class ContextHandler {

	private Map<String, RGB> colorMap;
	private Map<String, Context> contextMap;
	private static ContextHandler instance;
	private IPersistanceOperations persistenceOp = ModuleFactory
			.getPersistanceOperations();
	private Context activeContext;
	private static final String DEFAULT_CONTEXT = "Default";

	private ContextHandler() {
		contextMap = new HashMap<String, Context>();
		colorMap = new HashMap<String, RGB>();
	}

	public static ContextHandler getInstance() {
		if (instance == null) {
			instance = new ContextHandler();
		}
		return instance;
	}

	public boolean existsContext(String featureExpression) {
		return getContext(featureExpression) != null;
	}

	public void continueRecording(String featureExpression) {
		activeContext = getContext(featureExpression);
	}

	public void stopRecording() {
		if (activeContext != null) {
			contextMap.put(activeContext.getFeatureExpression(), activeContext);
		}
		String storageLocation = VariantSyncPlugin.getDefault()
				.getWorkspaceLocation() + VariantSyncConstants.CONTEXT_PATH;
		String filename = "/" + activeContext.getFeatureExpression() + ".xml";

		// creates target folder if it does not already exist
		File folder = new File(storageLocation);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		storageLocation += filename;
		persistenceOp.saveContext(activeContext, storageLocation);
		// activeContext = contextMap.get(DEFAULT_CONTEXT);
	}

	public void startNewContext(String featureExpression) {
		if (activeContext != null) {
			contextMap.put(activeContext.getFeatureExpression(), activeContext);
		}
		if (contextMap.containsKey(featureExpression)) {
			activeContext = getContext(featureExpression);
		} else {
			activeContext = new Context(featureExpression);
		}
	}

	public void recordCodeChange(String projectName, String pathToProject,
			List<String> changedCode, String className, String packageName) {
		if (activeContext != null) {
			if (!activeContext.containsProject(projectName)) {
				activeContext.initProject(projectName, pathToProject);
			}
			ContextAlgorithm ca = new ContextAlgorithm(activeContext);
			ca.addCode(projectName, packageName, className, changedCode);
			UpdateAlgorithm ua = new UpdateAlgorithm();
			ua.updateCode(projectName, packageName, className, changedCode,
					activeContext.getFeatureExpression());

			String file = "/src/" + packageName + "/" + className;
			file = file.replace(".", "/");
			file = file.replace("/java", ".java");
			IPath path = new Path(file);
			List<IProject> projects = VariantSyncPlugin.getDefault()
					.getSupportProjectList();
			IProject iProject = null;
			for (IProject p : projects) {
				if (p.getName().equals(projectName)) {
					iProject = p;
					break;
				}
			}
			IFile iFile = iProject.getFile(path);
			MarkerHandler.getInstance().clearAllMarker(iFile);
			List<MarkerInformation> markers = initMarker(
					activeContext,
					projectName,
					iFile.toString().substring(
							iFile.toString().lastIndexOf("/") + 1));

			MarkerHandler.getInstance().setMarker(iFile, markers);
		} else {

			// TODO: default context immer dann aktivieren, wenn kein anderer
			// Context aktiv ist + beim Start von Eclipse aktivieren!

			// activeContext = contextMap.get(DEFAULT_CONTEXT);
		}
	}

	private List<MarkerInformation> initMarker(Context context,
			String projectName, String className) {
		List<MarkerInformation> markers = new ArrayList<MarkerInformation>();

		JavaProject jp = context.getJavaProject(projectName);
		List<JavaElement> elements = jp.getChildren();
		List<JavaClass> classes = new ArrayList<JavaClass>();
		Util.getClassesByClassName(elements, classes, className);
		for (JavaClass c : classes) {
			List<CodeLine> cls = c.getCodeLines();
			int i = 0;
			List<CodeLine> tmp = new ArrayList<CodeLine>();
			for (CodeLine cl : cls) {
				tmp.add(cl);
				if (cls.size() > i + 1
						&& cls.get(i + 1).getLine() == cl.getLine() + 1) {
					tmp.add(cls.get(i + 1));
				} else {
					MarkerInformation mi = new MarkerInformation(0, tmp.get(0)
							.getLine(), tmp.get(tmp.size() - 1).getLine(), 0, 0);
					mi.setFeature(context.getFeatureExpression());
					markers.add(mi);
					tmp.clear();
				}
				i++;
			}

		}
		return markers;
	}

	public void addContext(Context c) {
		contextMap.put(c.getFeatureExpression(), c);
	}

	public Context getContext(String featureExpression) {
		return contextMap.get(featureExpression);
	}

	public Collection<Context> getAllContexts() {
		return contextMap.values();
	}

	public void setContextColor(String featureExpression, RGB color) {
		colorMap.put(featureExpression, color);
	}

	public RGB getColor(String featureExpression) {
		return colorMap.get(featureExpression);
	}

	/**
	 * @return the activeContext
	 */
	public Context getActiveContext() {
		return activeContext;
	}

}