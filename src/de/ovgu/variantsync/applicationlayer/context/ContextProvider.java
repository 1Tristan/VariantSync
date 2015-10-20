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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.AbstractModel;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.Util;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeChange;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeHighlighting;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaElement;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaProject;
import de.ovgu.variantsync.applicationlayer.datamodel.exception.FileOperationException;
import de.ovgu.variantsync.persistencelayer.IPersistanceOperations;
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
	private IPersistanceOperations persistanceOperations = ModuleFactory
			.getPersistanceOperations();

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

	@Override
	public Collection<String> getProjects(String fe) {
		Context c = ContextHandler.getInstance().getContext(fe);
		if (c != null && c.getJavaProjects() != null)
			return c.getJavaProjects().keySet();
		else
			return new ArrayList<String>() {
			};
	}

	@Override
	public Collection<String> getClasses(String fe, String projectName) {
		Context c = ContextHandler.getInstance().getContext(fe);
		JavaProject jp = c.getJavaProjects().get(projectName);
		List<JavaClass> classes = ContextUtils.getClasses(jp);
		List<String> classNames = new ArrayList<String>();
		for (JavaElement e : classes) {
			classNames.add(e.getName());
		}
		return classNames;
	}

	@Override
	public Collection<CodeChange> getChanges(String fe, String projectName,
			String className) {
		Context c = ContextHandler.getInstance().getContext(fe);
		JavaProject jp = c.getJavaProjects().get(projectName);
		List<JavaElement> classes = new ArrayList<JavaElement>();
		ContextUtils.iterateElements(jp.getChildren(), classes);
		for (JavaElement e : classes) {
			if (e.getName().equals(className)) {
				return ((JavaClass) e).getChanges();
			}
		}
		return null;
	}

	@Override
	public List<String> getSyncTargets(String fe, String projectName,
			String className) {
		List<String> syncTargets = new ArrayList<String>();
		Context c = ContextHandler.getInstance().getContext(fe);
		Map<String, JavaProject> mapJp = c.getJavaProjects();
		Set<Entry<String, JavaProject>> entries = mapJp.entrySet();
		Iterator<Entry<String, JavaProject>> it = entries.iterator();
		Set<String> usedProjects = new HashSet<String>();
		while (it.hasNext()) {
			Entry<String, JavaProject> e = it.next();
			if (!e.getKey().equals(projectName)) {
				List<JavaElement> classes = new ArrayList<JavaElement>();
				JavaProject jp = e.getValue();
				ContextUtils.iterateElements(jp.getChildren(), classes);
				for (JavaElement element : classes) {
					if (element.getName().equals(className)) {
						syncTargets
								.add(jp.getName() + ": " + element.getName());
						usedProjects.add(element.getName());
					}
				}
			}
		}
		List<IProject> supportedProjects = VariantSyncPlugin.getDefault()
				.getSupportProjectList();
		for (IProject p : supportedProjects) {
			String name = p.getName();
			if (!usedProjects.contains(name) && !name.equals(projectName)) {
				IResource javaClass = null;
				try {
					javaClass = findFileRecursively(p, className);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				if (javaClass != null) {
					syncTargets.add(name + ": " + javaClass.getName());
					usedProjects.add(name);
				}
			}
		}
		return syncTargets;
	}

	private IFile findFileRecursively(IContainer container, String name)
			throws CoreException {
		for (IResource r : container.members()) {
			if (r instanceof IContainer) {
				IFile file = findFileRecursively((IContainer) r, name);
				if (file != null) {
					return file;
				}
			} else if (r instanceof IFile && r.getName().equals(name)) {
				return (IFile) r;
			}
		}

		return null;
	}

	// TODO: use package-name to get full-qualified path to class
	@Override
	public List<CodeLine> getTargetCode(String fe, String projectName,
			String className) {
		List<CodeLine> targetCode = new ArrayList<CodeLine>();
		Context c = ContextHandler.getInstance().getContext(fe);
		Map<String, JavaProject> mapJp = c.getJavaProjects();
		Set<Entry<String, JavaProject>> entries = mapJp.entrySet();
		Iterator<Entry<String, JavaProject>> it = entries.iterator();
		while (it.hasNext()) {
			Entry<String, JavaProject> e = it.next();
			if (e.getKey().equals(projectName)) {
				List<JavaElement> classes = new ArrayList<JavaElement>();
				JavaProject jp = e.getValue();
				ContextUtils.iterateElements(jp.getChildren(), classes);
				for (JavaElement element : classes) {
					if (element.getName().equals(className)) {
						targetCode.addAll(element.getClonedCodeLines());
					}
				}
			}
		}
		if (targetCode.isEmpty()) {
			List<IProject> supportedProjects = VariantSyncPlugin.getDefault()
					.getSupportProjectList();
			for (IProject p : supportedProjects) {
				String name = p.getName();
				if (name.equals(projectName)) {
					IResource javaClass = null;
					try {
						javaClass = findFileRecursively(p, className);
					} catch (CoreException e) {
						e.printStackTrace();
					}
					if (javaClass != null) {
						IFile file = (IFile) javaClass;
						List<String> linesOfFile = null;
						try {
							linesOfFile = persistanceOperations.readFile(
									file.getContents(), file.getCharset());
						} catch (FileOperationException | CoreException e) {
							e.printStackTrace();
						}
						int i = 0;
						for (String line : linesOfFile) {
							targetCode.add(new CodeLine(line, i, false, false));
							i++;
						}
					}
				}
			}
		}
		return targetCode;
	}

	@Override
	public CodeHighlighting getContextColor(String featureExpression) {
		return contextHandler.getContext(featureExpression).getColor();
	}

}