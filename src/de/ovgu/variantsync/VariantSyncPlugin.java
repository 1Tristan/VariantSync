package de.ovgu.variantsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.datamodel.monitoring.MonitorItemStorage;
import de.ovgu.variantsync.applicationlayer.deltacalculation.DeltaOperationProvider;
import de.ovgu.variantsync.applicationlayer.monitoring.ChangeListener;
import de.ovgu.variantsync.applicationlayer.synchronization.SynchronizationProvider;
import de.ovgu.variantsync.persistancelayer.IPersistanceOperations;
import de.ovgu.variantsync.presentationlayer.controller.Controller;
import de.ovgu.variantsync.presentationlayer.view.AbstractView;
import de.ovgu.variantsync.presentationlayer.view.console.ChangeOutPutConsole;
import de.ovgu.variantsync.presentationlayer.view.eclipseadjustment.VSyncSupportProjectNature;
import de.ovgu.variantsync.utilitylayer.UtilityModel;
import de.ovgu.variantsync.utilitylayer.log.LogOperations;

/**
 * Entry point to start plugin variantsync. Variantsync supports developers to
 * synchronize similar software projects which are developed in different
 * variants. This version is based on first version of variantsync which was
 * developed by Lei Luo in 2012.
 * 
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 2.0
 * @since 14.05.2015
 */
public class VariantSyncPlugin extends AbstractUIPlugin {

	// shared instance
	private static VariantSyncPlugin plugin;
	private ChangeOutPutConsole console;
	private List<IProject> projectList = new ArrayList<IProject>(0);
	private Map<IProject, MonitorItemStorage> synchroInfoMap = new HashMap<IProject, MonitorItemStorage>();
	private ChangeListener resourceModificationListener;
	private IPersistanceOperations persistanceOperations = ModuleFactory
			.getPersistanceOperations();
	private Controller controller = Controller.getInstance();

	@Override
	public void start(BundleContext context) {
		try {
			super.start(context);
		} catch (Exception e) {
			LogOperations.logError("Plugin could not be started.", e);
		}
		plugin = this;
		console = new ChangeOutPutConsole();
		initMVC();
		try {
			initResourceMonitoring();
		} catch (CoreException e) {
			LogOperations.logError(
					"Resouce monitoring could not be initialized.", e);
		}
	}

	@Override
	public void stop(BundleContext context) {
		plugin = null;
		try {
			super.stop(context);
		} catch (Exception e) {
			LogOperations.logError("Plugin could not be started.", e);
		}
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		ws.removeResourceChangeListener(resourceModificationListener);
	}

	/**
	 * Returns list of projects which has eclipse nature support and variantsync
	 * nature id.
	 * 
	 * @return list of projects
	 */
	public List<IProject> getSupportProjectList() {
		this.projectList.clear();
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot()
				.getProjects()) {
			try {
				if (project.isOpen()
						&& project
								.hasNature(VSyncSupportProjectNature.NATURE_ID)) {
					this.projectList.add(project);
				}
			} catch (CoreException e) {
				UtilityModel.getInstance().handleError(e,
						"nature support could not be checked");
			}
		}
		return new ArrayList<IProject>(projectList);
	}

	/**
	 * Updates synchroInfoMap. This map contains IProject-objects mapped to
	 * SynchroInfo objects.
	 */
	public void updateSynchroInfo() {
		this.synchroInfoMap.clear();
		List<IProject> projects = this.getSupportProjectList();
		for (IProject project : projects) {
			IFile infoFile = project.getFolder(
					VariantSyncConstants.ADMIN_FOLDER).getFile(
					VariantSyncConstants.ADMIN_FILE);
			MonitorItemStorage info = new MonitorItemStorage();
			if (infoFile.exists()) {
				try {
					info = persistanceOperations.readSynchroXMLFile(infoFile
							.getContents());
				} catch (CoreException e) {
					UtilityModel.getInstance().handleError(e,
							"info file could not be read");
				}
			}
			this.synchroInfoMap.put(project, info);
		}
	}

	/**
	 * Initializes model view controller implementation to encapsulate gui
	 * elements from business logic.
	 */
	private void initMVC() {

		// register models as request handler for controller
		controller.addModel(new SynchronizationProvider());
		controller.addModel(new DeltaOperationProvider());
	}

	/**
	 * Prepares resource monitoring in eclipse workspace. Resource monitoring
	 * means that projects will be watches. If a projects´s resource changes and
	 * change was saved, monitoring will notice changed resource (POST_CHANGE).
	 * A resource is a file or folder.
	 * 
	 * @throws CoreException
	 *             resources could not be monitored
	 */
	private void initResourceMonitoring() throws CoreException {
		resourceModificationListener = new ChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				resourceModificationListener, IResourceChangeEvent.POST_CHANGE);
		resourceModificationListener.registerSaveParticipant();
	}

	/**
	 * Returns monitored items of specific project.
	 * 
	 * @param project
	 *            the project to get monitored items from
	 * @return MonitorItemStorage
	 */
	public MonitorItemStorage getSynchroInfoFrom(IProject project) {
		if (this.synchroInfoMap.get(project) == null) {
			this.updateSynchroInfo();
		}
		return this.synchroInfoMap.get(project);
	}

	/**
	 * Registers a view. If a model fires an event, all registered views receive
	 * this element.
	 * 
	 * @param view
	 *            view to register
	 */
	public void registerView(AbstractView view) {
		controller.addView(view);
	}

	/**
	 * Removes a view from controller. If a model fires an event, this view will
	 * no longer receive this event.
	 * 
	 * @param view
	 *            view to remove
	 */
	public void removeView(AbstractView view) {
		controller.removeView(view);
	}

	/**
	 * @return the display
	 */
	public static Display getStandardDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	/**
	 * Logs a message on eclipse console.
	 * 
	 * @param msg
	 *            the message to log
	 */
	public void logMessage(String msg) {
		console.logMessage(msg);
	}

	/**
	 * @return the console
	 */
	public ChangeOutPutConsole getConsole() {
		return console;
	}

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static VariantSyncPlugin getDefault() {
		return plugin;
	}

}
