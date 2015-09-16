package de.ovgu.variantsync.applicationlayer.features;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.featureide.fm.core.Constraint;
import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.ConfigurationReader;
import de.ovgu.featureide.fm.core.io.FeatureModelReaderIFileWrapper;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelReader;
import de.ovgu.variantsync.applicationlayer.datamodel.exception.FeatureException;

/**
 * Provides functions to read and check features and feature configurations of
 * projects.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 20.05.2015
 */
class FeatureHandler {

	private static FeatureHandler instance;
	private Set<Constraint> constraints;

	private FeatureHandler() {
		constraints = new LinkedHashSet<Constraint>();
	}

	public static FeatureHandler getInstance() {
		if (instance == null) {
			instance = new FeatureHandler();
		}
		return instance;
	}

	/**
	 * Get specified features of a project.
	 * 
	 * @param project
	 *            the project to check
	 * @return set of features
	 * @throws FeatureException
	 *             configuration object could not be created and features could
	 *             not be read
	 */
	public Set<Feature> getConfiguredFeaturesOfProject(IProject project)
			throws FeatureException {
		IProject featureInfoProject = getFeatureInfoProject();
		IFile modelFile = null;
		IFile configFile = null;
		if (featureInfoProject != null) {
			modelFile = featureInfoProject.getFile(FeatureConstants.MODEL_FILE);
			configFile = featureInfoProject.getFolder(
					FeatureConstants.CONFIGS_PATH).getFile(
					project.getName() + "."
							+ FeatureConstants.CONFIG_FILE_EXTENSION);
		}
		if (featureInfoProject != null && modelFile.exists()
				&& configFile.exists()) {
			Configuration config = null;
			config = readConfig(configFile, modelFile);
			return new HashSet<Feature>(config.getSelectedFeatures());
		} else {
			return new HashSet<Feature>();
		}
	}

	/**
	 * Get all features of feature model as set.
	 * 
	 * @return set of features
	 * @throws FeatureException
	 *             configuration object could not be created and features could
	 *             not be read
	 */
	public Set<String> getFeatures() throws FeatureException {
		IProject featureInfoProject = getFeatureInfoProject();
		if (featureInfoProject != null) {
			return getFeatureModel().getFeatureNames();
		}
		return null;
	}

	/**
	 * Get all features of feature model as set.
	 * 
	 * @return set of features
	 * @throws FeatureException
	 *             configuration object could not be created and features could
	 *             not be read
	 */
	public Set<String> getFeatureExpressions() throws FeatureException {
		IProject featureInfoProject = getFeatureInfoProject();
		if (featureInfoProject != null) {
			Set<String> expressions = new LinkedHashSet<String>(
					getFeatureModel().getFeatureNames());
			Iterator<Constraint> itConstraints = constraints.iterator();
			while (itConstraints.hasNext()) {
				expressions.add(itConstraints.next().toString());
			}
			return expressions;
		}
		return null;
	}

	/**
	 * Checks if projects contain all given features.
	 * 
	 * @param projects
	 *            the projects to check
	 * @param selectedFeatures
	 *            the features which all projects should contain
	 * @return mapping with specification which projects support all features
	 *         and which projects do not support all features
	 * @throws FeatureException
	 *             configuration object could not be created and features could
	 *             not be read
	 */
	public Map<IProject, Boolean> checkFeatureSupport(List<IProject> projects,
			Object[] selectedFeatures) throws FeatureException {
		Map<IProject, Boolean> checkedProjects = new HashMap<IProject, Boolean>();
		for (IProject project : projects) {
			Boolean value = checkFeatureSupport(project, selectedFeatures);
			checkedProjects.put(project, value);
		}
		return checkedProjects;
	}

	// TODO: Synchronize feature model in file (.variantsyncfeatureinfo)
	// TODO: synchronize constraints in feature model in file => Layer to
	// decouple feature expression and feature model with features and
	// constraints?
	// TODO: stop context
	// TODO: create data structure to manage context recordings (see code
	// mapping and old variant sync tool)
	// TODO: create persistence function for data model
	// TODO: retrieve changes after stop recording (abbild des projektes beim
	// start des contextes nehmen und nach Beendigung -> Diff?)
	// TODO: fill data structure and save changes

	// TODO: develop algorithm

	public FeatureModel getFeatureModel() throws FeatureException {
		FeatureModel fm = new FeatureModel();
		fm.setRoot(new Feature(fm));
		try {
			new FeatureModelReaderIFileWrapper(new XmlFeatureModelReader(fm))
					.readFromFile(getFeatureInfoProject().getFile(
							FeatureConstants.MODEL_FILE));
		} catch (FileNotFoundException | UnsupportedModelException e) {
			throw new FeatureException(
					"Features from feature model could not be read.", e);
		}
		return fm;
	}

	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
	}

	public void deleteFeatureExpression(String expr) {
		boolean isRemoved = false;
		Iterator<Constraint> itConstraint = constraints.iterator();
		while (itConstraint.hasNext()) {
			Constraint c = itConstraint.next();
			String name = c.toString();
			if (name.equals(expr)) {
				constraints.remove(c);
				isRemoved = true;
				break;
			}
		}
		if (!isRemoved) {
			FeatureModel fm = null;
			try {
				fm = getFeatureModel();
			} catch (FeatureException e) {
				// TODO handle exception
				e.printStackTrace();
			}
			Feature f = fm.getFeature(expr);
			if (f != null) {
				fm.deleteFeature(f);
			}
		}
	}

	/**
	 * Reads feature configuration file and creates configuration object.
	 * 
	 * @param configFile
	 *            the file to read
	 * @param modelFile
	 *            the feature model file
	 * @return configuration
	 * @throws FeatureException
	 *             feature model or configuration file could not be read
	 */
	private Configuration readConfig(IFile configFile, IFile modelFile)
			throws FeatureException {
		FeatureModel fm = new FeatureModel();
		fm.setRoot(new Feature(fm));
		Configuration config = null;
		try {
			new FeatureModelReaderIFileWrapper(new XmlFeatureModelReader(fm))
					.readFromFile(modelFile);
			config = new Configuration(fm);
			ConfigurationReader reader = new ConfigurationReader(config);
			reader.readFromFile(configFile);
		} catch (CoreException | IOException | UnsupportedModelException e) {
			throw new FeatureException(
					"Feature model or configuration file could not be read. See stacktace for more precisely informations.",
					e);
		}
		return config;
	}

	/**
	 * Checks if a project contains all specified features.
	 * 
	 * @param project
	 *            the project to check
	 * @param features
	 *            features which the project should contain
	 * @return true if project contains all features; otherwise false
	 * @throws FeatureException
	 *             features could not be read
	 */
	private boolean checkFeatureSupport(IProject project, Object[] features)
			throws FeatureException {
		Set<Feature> projectFeatures = getConfiguredFeaturesOfProject(project);
		int flag = 0;
		for (Object feature : features) {
			for (Feature f : projectFeatures) {
				if (f.getName().equals(feature)) {
					flag++;
					break;
				}
			}
		}
		return flag == features.length;
	}

	private IProject getFeatureInfoProject() throws FeatureException {
		IProject featureInfoProject = null;
		for (IProject p : ResourcesPlugin.getWorkspace().getRoot()
				.getProjects()) {
			try {
				if (p.isOpen()
						&& p.getName().equals(
								FeatureConstants.FEATUREINFO_PROJECT_NAME)
						&& p.hasNature(FeatureConstants.FEATURE_PROJECT_NATURE)) {
					featureInfoProject = p;
					break;
				}
			} catch (CoreException e) {
				throw new FeatureException("Nature support of project "
						+ p.getName() + " could not be checked.", e);
			}
		}
		return featureInfoProject;
	}

}
