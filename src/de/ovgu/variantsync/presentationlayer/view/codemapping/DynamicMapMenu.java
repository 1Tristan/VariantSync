package de.ovgu.variantsync.presentationlayer.view.codemapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.presentationlayer.controller.ControllerHandler;
import de.ovgu.variantsync.presentationlayer.controller.FeatureController;

/**
 * Creates menu items for popup menus which appear after a right-click.
 * Identifies the project which belongs to the clicked element. Gets features
 * belonging to specified project.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 04.06.2015
 */
public abstract class DynamicMapMenu extends ContributionItem {

	private Map<IProject, Set<Feature>> features;
	protected FeatureController controller = ControllerHandler.getInstance()
			.getFeatureController();

	protected static final String NO_FEATURE_DEFINED = "any feature for this project definied";

	/**
	 * Identifies the project which belongs to the clicked element.
	 * 
	 * @return the IProject reference
	 */
	protected abstract IProject getProject();

	/**
	 * Executes an action after a feature was selected in code-feature-mapping
	 * menu.
	 * 
	 * @param feature
	 *            the feature that is chosen
	 */
	protected abstract void handleSelection(String feature);

	@Override
	public void fill(final Menu menu, int index) {
		List<String> features = getFeatureList(getProject());
		for (final String feature : features) {
			MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
			menuItem.setText(feature);
			if (!feature.contains(NO_FEATURE_DEFINED)) {
				menuItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						handleSelection(feature);
					}
				});
			} else {
				menuItem.setEnabled(false);
			}
		}
	}

	/**
	 * Gets features belonging to specified project.
	 * 
	 * @param project
	 *            the project to fetch features
	 * @return list with name´s of features
	 */
	protected List<String> getFeatureList(IProject project) {
		features = controller.getFeaturesDirectly(VariantSyncPlugin
				.getDefault().getSupportProjectList());
		Set<Feature> featuresOfSelectedProject = features.get(project);
		List<String> features = new ArrayList<String>();
		if (featuresOfSelectedProject != null
				&& !featuresOfSelectedProject.isEmpty()) {
			Iterator<Feature> itFeature = featuresOfSelectedProject.iterator();
			while (itFeature.hasNext()) {
				features.add(itFeature.next().getName());
			}
		} else {
			features.add(NO_FEATURE_DEFINED);
		}
		return features;
	}

}