package de.ovgu.variantsync.presentationlayer.controller;

import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.context.IContextOperations;

/**
 * Manages context operations and data exchanges between view and model.
 * Transforms user interactions in gui elements in model compatible actions.
 * Implements required methods to communication with view and model.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 02.09.2015
 */
public class ContextController extends AbstractController {

	private IContextOperations contextOperations = ModuleFactory
			.getContextOperations();

	public String getActiveFeatureContext() {
		return contextOperations.getActiveFeatureContext();
	}

	public String getActiveProject() {
		return contextOperations.getActiveProject();
	}

	public void activateContext(String featureExpression) {
		contextOperations.activateContext(featureExpression);
	}

	public void stopContextRecording() {
		contextOperations.stopRecording();
	}

}