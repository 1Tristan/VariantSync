package de.ovgu.variantsync.applicationlayer.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.variantsync.applicationlayer.AbstractModel;
import de.ovgu.variantsync.applicationlayer.datamodel.exception.FeatureException;
import de.ovgu.variantsync.presentationlayer.controller.ControllerProperties;

/**
 * Receives controller invocation as part of MVC implementation and encapsulates
 * functionality of its package.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 18.05.2015
 */
public class FeatureProvider extends AbstractModel implements
		IFeatureOperations {

	private FeatureHandler featureHandler;

	public FeatureProvider() {
		this.featureHandler = new FeatureHandler();
	}

	@Override
	public Map<IProject, Boolean> checkFeatureSupport(List<IProject> projects,
			Object[] selectedFeatures) {
		Map<IProject, Boolean> resultMap = null;
		try {
			resultMap = featureHandler.checkFeatureSupport(projects,
					selectedFeatures);
		} catch (FeatureException e) {
			propertyChangeSupport.firePropertyChange(
					ControllerProperties.EXCEPTION_PROPERTY.getProperty(),
					"Feature support could not be checked.", e);
		}
		propertyChangeSupport.firePropertyChange(
				ControllerProperties.FEATURECHECK.getProperty(), null,
				resultMap);
		return resultMap;
	}

	@Override
	public Map<IProject, Set<Feature>> getFeatures(List<IProject> projects) {
		Map<IProject, Set<Feature>> featureMap = new HashMap<IProject, Set<Feature>>();
		for (IProject project : projects) {
			Set<Feature> features = null;
			try {
				features = featureHandler.getFeatures(project);
				featureMap.put(project, features);
			} catch (FeatureException e) {
				propertyChangeSupport.firePropertyChange(
						ControllerProperties.EXCEPTION_PROPERTY.getProperty(),
						"Supported features for project " + project.getName()
								+ " could not be computed.", e);
			}
		}
		propertyChangeSupport.firePropertyChange(
				ControllerProperties.FEATUREEXTRACTION.getProperty(), null,
				featureMap);
		return featureMap;
	}
}