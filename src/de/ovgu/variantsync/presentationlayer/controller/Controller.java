package de.ovgu.variantsync.presentationlayer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import de.ovgu.variantsync.applicationlayer.datamodel.resources.ResourceChangesFilePatch;

/**
 * Connections between view and model. Transforms user interactions in gui
 * elements in model compatible actions. Implements required methods to
 * communication with view and model.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 18.05.2015
 */
public class Controller extends AbstractController {

	private static Controller instance;
	private Map<ResourceChangesFilePatch, Integer> synchronizedProjectsCount = new HashMap<ResourceChangesFilePatch, Integer>();

	private Controller() {
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public void synchronize(Object[] result, ResourceChangesFilePatch patch) {
		setModelProperty(
				ControllerProperties.SYNCHRONIZATION_PROPERTY.getProperty(),
				result, patch);
	}

	public void getProjectList(ResourceChangesFilePatch patch) {
		setModelProperty(
				ControllerProperties.PROJECTLIST_PROPERTY.getProperty(), patch);
	}

	public void getSynchronizedProjects(ResourceChangesFilePatch patch) {
		setModelProperty(
				ControllerProperties.SYNCHRONIZEDPROJECTS_PROPERTY
						.getProperty(),
				patch);
	}

	public void getChanges(ResourceChangesFilePatch patch) {
		setModelProperty(
				ControllerProperties.UNIFIEDDIFF_PROPERTY.getProperty(), patch);
	}

	public void getProjectNames(ResourceChangesFilePatch patch) {
		setModelProperty(
				ControllerProperties.PROJECTNAMES_PROPERTY.getProperty(), patch);
	}

	public void checkFeatureSupport(Object[] elements, Object[] selectedFeatures) {
		List<IProject> projects = new ArrayList<IProject>();
		for (Object o : selectedFeatures) {
			projects.add((IProject) o);
		}
		setModelProperty(ControllerProperties.FEATURECHECK.getProperty(),
				projects, selectedFeatures);
	}

	public void getFeatures(List<IProject> projects) {
		setModelProperty(ControllerProperties.FEATUREEXTRACTION.getProperty(),
				projects);
	}

	/**
	 * @return the synchronizedProjectsCount
	 */
	public int getSynchronizedProjectsCount(ResourceChangesFilePatch patch) {
		return synchronizedProjectsCount.get(patch);
	}

	/**
	 * @param synchronizedProjectsCount
	 *            the synchronizedProjectsCount to set
	 */
	public void setSynchronizedProjectsCount(ResourceChangesFilePatch patch,
			int synchronizedProjectsCount) {
		this.synchronizedProjectsCount.put(patch, synchronizedProjectsCount);
	}
}