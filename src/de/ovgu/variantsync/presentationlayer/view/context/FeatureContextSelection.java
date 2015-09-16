package de.ovgu.variantsync.presentationlayer.view.context;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.presentationlayer.controller.ControllerHandler;
import de.ovgu.variantsync.presentationlayer.controller.ControllerProperties;
import de.ovgu.variantsync.presentationlayer.controller.ControllerTypes;
import de.ovgu.variantsync.presentationlayer.controller.FeatureController;
import de.ovgu.variantsync.presentationlayer.view.AbstractView;

public class FeatureContextSelection extends ContributionItem implements
		AbstractView, IHandler {

	private static FeatureController featureController = ControllerHandler
			.getInstance().getFeatureController();
	private static final String NO_FEATURE_DEFINED = "any features definied";
	public static String activeContext = "no feature context active";
	public static String activeProject = "no project active";
	public static long time;
	private static Menu staticMenu;
	private static MenuItem staticMenuItem;
	private static int staticIndex;

	@Override
	public void fill(final Menu menu, int index) {
		VariantSyncPlugin.getDefault().registerView(this,
				ControllerTypes.CONTEXT);
		staticMenu = menu;
		staticIndex = index;
		buildPulldownMenu(staticMenu, staticIndex);
	}

	public static void refreshMenuItems() {
		if (staticMenu != null) {
			MenuItem[] items = staticMenu.getItems();
			for (MenuItem item : items) {
				item.dispose();
			}
			buildPulldownMenu(staticMenu, staticIndex);
		}
	}

	private static void buildPulldownMenu(final Menu menu, int index) {
		Set<String> features = featureController.getFeatureExpressions();
		for (final String feature : features) {
			staticMenuItem = new MenuItem(menu, SWT.BALLOON, index);
			staticMenuItem.setText(feature);
			if (!feature.contains(NO_FEATURE_DEFINED)) {
				staticMenuItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						handleSelection(feature);
					}
				});
			} else {
				staticMenuItem.setEnabled(false);
			}
		}
	}

	private static void handleSelection(String feature) {
		System.out.println("Feature-Context: " + feature + " is chosen.");
		activeContext = feature;
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL
						| SWT.SHEET), new LabelProvider());
		List<IProject> projectList = VariantSyncPlugin.getDefault()
				.getSupportProjectList();
		String[] projectNames = new String[projectList.size()];
		int i = 0;
		for (IProject project : projectList) {
			projectNames[i] = project.getName();
			i++;
		}
		dialog.setElements(projectNames);
		dialog.setTitle("Choose the project you are working on.");
		if (dialog.open() != Window.OK) {
			return;
		}
		Object[] result = dialog.getResult();
		String chosenProject = (String) result[0];
		activeProject = chosenProject;
		System.out.println("Project: " + chosenProject + " is chosen");
		ControllerHandler.getInstance().getContextController()
				.activateContext(feature, chosenProject);
		FeatureContextActivity.refreshMenuItem();
		time = System.nanoTime();
	}

	@Override
	public void addHandlerListener(IHandlerListener arg0) {
		// not required
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// not required
		return arg0;
	}

	@Override
	public boolean isHandled() {
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener arg0) {
		// not required
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(
				ControllerProperties.CONSTRAINT_PROPERTY.getProperty())) {
			FeatureContextSelection.refreshMenuItems();
		}
	}
}