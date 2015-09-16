package de.ovgu.variantsync.applicationlayer.context;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.diff.Diff;
import de.ovgu.variantsync.applicationlayer.datamodel.diff.DiffIndices;
import de.ovgu.variantsync.applicationlayer.datamodel.diff.DiffStep;
import de.ovgu.variantsync.applicationlayer.features.IFeatureOperations;
import de.ovgu.variantsync.applicationlayer.features.mapping.UtilOperations;
import de.ovgu.variantsync.presentationlayer.controller.data.JavaElements;
import de.ovgu.variantsync.presentationlayer.controller.data.MappingElement;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 14.09.2015
 */
class ContextAlgorithm {

	private IFeatureOperations featureOperations = ModuleFactory
			.getFeatureOperations();
	private Context context;

	public ContextAlgorithm(Context context) {
		this.context = context;
	}

	public void addCode(String packageName, String className, List<String> code) {

		// Mapping auf FeatureExpressions umstellen/ von FeatureExpressions
		// extrahieren und immer das Project zurückgeben mit angepasstem Mapping
		// => Blackbox mit Input (MappingElement, Project) und Output (Project)
		// Zuordnung Project zu FeatureExpression und Datenhaltung erfolgt im
		// Context
		System.out.println(code.toString());

		List<Diff> diffs = ContextUtils.analyzeDiff(code);
		refreshCodeBase(diffs, packageName, className);

		// String file = "/src/" + packageName + "/" + className;
		// refreshMarker(diffs, file, context.getPathOfJavaProject());

		UtilOperations.getInstance().printProject(context.getJavaProject());
	}

	private void refreshCodeBase(List<Diff> diffs, String packageName,
			String className) {
		int i = 0;
		for (Diff diff : diffs) {
			DiffIndices di = diff.getDiffIndices();
			List<DiffStep> diffSteps = diff.getDiffSteps();
			int startNew = di.getStartIndixNewCode();
			int startOld = di.getStartIndixOldCode();
			int removeCounter = 0;
			int addCounter = 0;
			for (DiffStep ds : diffSteps) {
				List<String> list = new LinkedList<String>();
				if (ds == null) {
					continue;
				}
				list.add(ds.getCode());
				if (ds.isAddFlag()) {
					addCode(packageName, className, startNew, startNew, list);
					if (!UtilOperations.getInstance().ignoreAddCounter()) {
						addCounter++;
					}
					startNew++;
				} else {
					removeCode(packageName, className, startOld, startOld, list);
					removeCounter++;
					if (startNew > startOld) {
						startNew--;
					}
				}
			}
			if (removeCounter > 0) {
				ContextUtils.decreaseCodeLines(
						diffs.subList(i + 1, diffs.size()), removeCounter);
			}
			if (addCounter > 0) {
				ContextUtils.increaseCodeLines(
						diffs.subList(i + 1, diffs.size()), addCounter);
			}
			i++;
		}
	}

	private void addCode(String packageName, String className, int start,
			int end, List<String> extractedCode) {
		setUpProject();
		MappingElement mapping = new MappingElement(
				context.getFeatureExpression(), className,
				JavaElements.CODE_FRAGMENT, context.getPathToProject()
						+ "/src/" + packageName.replace(".", "/") + "/"
						+ className, extractedCode, start, end, end - start);
		mapping.setPathToProject(context.getPathToProject());
		featureOperations.addCodeFragment(mapping, context.getJavaProject());
	}

	private void removeCode(String packageName, String className, int start,
			int end, List<String> extractedCode) {
		setUpProject();
		MappingElement mapping = new MappingElement(
				context.getFeatureExpression(), className,
				JavaElements.CODE_FRAGMENT, context.getPathToProject()
						+ "/src/" + packageName.replace(".", "/") + "/"
						+ className, extractedCode, start, end, end - start);
		mapping.setPathToProject(context.getPathToProject());
		featureOperations.removeMapping(mapping, context.getJavaProject());
	}

	private void setUpProject() {
		if (context.getPathToProject() == null) {
			List<IProject> projects = VariantSyncPlugin.getDefault()
					.getSupportProjectList();
			for (IProject p : projects) {
				if (p.getName().equals(context.getNameOfJavaProject())) {
					context.setPathToProject(p.getFullPath().toString());
				}
			}
		}
	}

	private void refreshMarker(List<Diff> diffs, String file, String projectName) {
		System.out.println(file);
		IPath path = new Path(file);
		List<IProject> projects = VariantSyncPlugin.getDefault()
				.getSupportProjectList();
		IProject iProject = null;
		projectName = projectName.substring(1);
		for (IProject p : projects) {
			if (p.getName().equals(projectName)) {
				iProject = p;
				break;
			}
		}
		IFile iFile = iProject.getFile(path);
		System.out.println(iFile.toString());

		// TODO Marker-Update

		// IMarker marker = null;
		// try {
		// marker = CodeMarkerFactory.createMarker(iFile, new Position(offset,
		// length), featureExpression);
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }
		// MarkerInformation mi = new MarkerInformation(marker.getId(), start,
		// end, offset, length);
		// MarkerHandler.getInstance().addMarker(mi, marker, iFile,
		// featureExpression);
	}
}
