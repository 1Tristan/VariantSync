package de.ovgu.variantsync.presentationlayer.view.resourcechanges;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ResourceChangesFilePatch;
import de.ovgu.variantsync.presentationlayer.controller.Controller;
import de.ovgu.variantsync.presentationlayer.controller.ControllerProperties;
import de.ovgu.variantsync.presentationlayer.view.AbstractView;

/**
 * Provides content for cell header in table of resource changes view.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 18.05.2015
 */
public class ResourceChangesColumnLabelProvider extends CellLabelProvider
		implements AbstractView {

	private int headID = 0;
	private ViewerCell cell;
	private Controller controller;
	private ResourceChangesFilePatch patch;

	/**
	 * Registers this instance as view which can receive events from controller
	 * as part of MCV-Implementation.
	 * 
	 * @param i
	 * @param controller
	 */
	public ResourceChangesColumnLabelProvider(int i, final Controller controller) {
		this.headID = i;
		this.controller = controller;
		VariantSyncPlugin.getDefault().registerView(this);
	}

	@Override
	public void update(ViewerCell cell) {
		this.cell = cell;
		Object o = cell.getElement();
		if (o instanceof ResourceChangesFilePatch
				&& ((ResourceChangesFilePatch) o).isSynchronized()) {
			cell.setBackground(Display.getDefault().getSystemColor(
					SWT.COLOR_GRAY));
		} else {
			cell.setBackground(Display.getDefault().getSystemColor(
					SWT.COLOR_WHITE));
		}
		if (headID == 0) {
			cell.setImage(new ResourceChangesLabelProvider().getImage(o));
			if (o instanceof ResourceChangesFilePatch) {
				cell.setText(((ResourceChangesFilePatch) o).getStatus()
						+ "     " + ((ResourceChangesFilePatch) o).getTime());
			} else {
				cell.setText(new ResourceChangesLabelProvider().getText(o));
			}
		}
		if (headID == 1 && o instanceof ResourceChangesFilePatch) {
			IProject project = ((ResourceChangesFilePatch) o).getProject();
			if (project != null) {
				cell.setText(project.getName());
			}
		}
		if (headID == 2 && o instanceof ResourceChangesFilePatch) {
			patch = (ResourceChangesFilePatch) o;
			controller.getProjectNames(patch);
		}
		if (headID == 3 && o instanceof ResourceChangesFilePatch) {
			patch = (ResourceChangesFilePatch) o;
			controller.getSynchronizedProjects(patch);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		if (cell != null) {
			if (evt.getPropertyName().equals(
					ControllerProperties.PROJECTNAMES_PROPERTY.getProperty())) {
				if (headID == 2) {
					String projects = "";
					List<String> projectNames = (List<String>) evt
							.getNewValue();
					for (String name : projectNames) {
						projects = projects + name + " ";
					}
					cell.setText(projects);
				}
			} else if (evt.getPropertyName().equals(
					ControllerProperties.SYNCHRONIZEDPROJECTS_PROPERTY
							.getProperty())) {
				List<String> projectList = (List<String>) evt.getNewValue();
				controller.setSynchronizedProjectsCount(patch,
						projectList.size());
				if (headID == 3) {
					String projects = "";
					if (projectList != null) {
						for (String project : projectList) {
							projects = projects + project + " ";
						}
						cell.setText(projects);
					}
				}
			}
		}
	}
}
