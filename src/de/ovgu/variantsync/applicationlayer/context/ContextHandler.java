package de.ovgu.variantsync.applicationlayer.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.ovgu.variantsync.VariantSyncConstants;
import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.Util;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeHighlighting;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaElement;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaProject;
import de.ovgu.variantsync.persistencelayer.IPersistanceOperations;
import de.ovgu.variantsync.presentationlayer.view.codemapping.MarkerInformation;
import de.ovgu.variantsync.presentationlayer.view.context.MarkerHandler;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
class ContextHandler {

	private Map<String, Context> contextMap;
	private static ContextHandler instance;
	private IPersistanceOperations persistenceOp = ModuleFactory
			.getPersistanceOperations();
	private Context activeContext;

	private ContextHandler() {
		contextMap = new HashMap<String, Context>();
	}

	public static ContextHandler getInstance() {
		if (instance == null) {
			instance = new ContextHandler();
		}
		return instance;
	}

	public void activateContext(String featureExpression) {
		if (existsContext(featureExpression)) {
			continueRecording(featureExpression);
		} else {
			startContext(featureExpression);
		}
	}

	private boolean existsContext(String featureExpression) {
		return getContext(featureExpression) != null;
	}

	public void continueRecording(String featureExpression) {
		activeContext = getContext(featureExpression);
	}

	public void stopRecording() {
		if (activeContext != null) {
			contextMap.put(activeContext.getFeatureExpression(), activeContext);
			persistenceOp.saveContext(activeContext,
					Util.parseStorageLocation(activeContext));
		}
		activateContext(VariantSyncConstants.DEFAULT_CONTEXT);
	}

	private void startContext(String featureExpression) {
		if (activeContext != null) {
			contextMap.put(activeContext.getFeatureExpression(), activeContext);
			persistenceOp.saveContext(activeContext,
					Util.parseStorageLocation(activeContext));
		}
		if (contextMap.containsKey(featureExpression)) {
			activeContext = getContext(featureExpression);
		} else {
			activeContext = new Context(featureExpression);
		}
	}

	public void recordCodeChange(String projectName, String pathToProject,
			List<String> changedCode, String className, String packageName) {
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
	}

	private List<MarkerInformation> initMarker(Context context,
			String projectName, String className) {
		Set<MarkerInformation> markers = new HashSet<MarkerInformation>();
		Map<String, List<JavaClass>> classes = ModuleFactory
				.getContextOperations().findJavaClass(projectName, className);
		Set<Entry<String, List<JavaClass>>> set = classes.entrySet();
		Iterator<Entry<String, List<JavaClass>>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, List<JavaClass>> entry = it.next();
			List<JavaClass> listClasses = entry.getValue();
			for (JavaClass c : listClasses) {
				List<CodeLine> cls = c.getCodeLines();
				int i = 0;
				List<CodeLine> tmp = new ArrayList<CodeLine>();
				for (CodeLine cl : cls) {
					tmp.add(cl);
					if (cls.size() > i + 1
							&& cls.get(i + 1).getLine() == cl.getLine() + 1) {
						tmp.add(cls.get(i + 1));
					} else {
						MarkerInformation mi = new MarkerInformation(0, tmp
								.get(0).getLine(), tmp.get(tmp.size() - 1)
								.getLine(), 0, 0);
						mi.setFeature(entry.getKey());
						mi.setColor(ModuleFactory.getContextOperations()
								.findColor(entry.getKey()));
						markers.add(mi);
						tmp.clear();
					}
					i++;
				}
			}
		}
		JavaProject jp = context.getJavaProject(projectName);
		List<JavaElement> elements = jp.getChildren();
		List<JavaClass> cc = new ArrayList<JavaClass>();
		Util.getClassesByClassName(elements, cc, className);
		for (JavaClass c : cc) {
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
					mi.setColor(context.getColor());
					markers.add(mi);
					tmp.clear();
				}
				i++;
			}
		}
		return new ArrayList<MarkerInformation>(markers);
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

	public void setContextColor(String featureExpression, CodeHighlighting color) {
		Context c = contextMap.get(featureExpression);
		if (c != null) {
			c.setColor(color);
			persistenceOp.saveContext(c, Util.parseStorageLocation(c));
		}
	}

	/**
	 * @return the activeContext
	 */
	public Context getActiveContext() {
		return activeContext;
	}

}