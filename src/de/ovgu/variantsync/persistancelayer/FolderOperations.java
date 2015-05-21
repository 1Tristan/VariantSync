package de.ovgu.variantsync.persistancelayer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.variantsync.applicationlayer.datamodel.monitoring.MonitorSet;

/**
 * Handle file and directory operations on file system including changes
 * monitoring.<br>
 * Supported operations:<br>
 * <ul>
 * <li>create directory</li>
 * <li>delete directory</li>
 * </ul>
 * Files and folders will be monitored.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 14.05.2015
 */
class FolderOperations {

	/**
	 * Creates specified folder.
	 * 
	 * @param folder
	 *            IFolder object
	 * @throws CoreException
	 *             folder could not be created
	 */
	public void mkdirs(IFolder folder) throws CoreException {
		IContainer container = folder.getParent();
		if (!container.exists()) {
			mkdirs((IFolder) container);
		}
		MonitorSet.getInstance().addSynchroItem(folder);
		folder.create(true, true, null);
	}

	/**
	 * Deletes specified folder.
	 * 
	 * @param folder
	 *            IFolder object
	 * @throws CoreException
	 *             folder could not be deleted
	 */
	public void deldirs(IFolder folder) throws CoreException {
		recordDelItem(folder);
		folder.delete(true, null);
	}

	/**
	 * Adds monitoring to folder and its containing sub-folders and files.
	 * 
	 * @param folder
	 *            IFolder object
	 * @throws CoreException
	 */
	private void recordDelItem(IFolder folder) throws CoreException {
		MonitorSet.getInstance().addSynchroItem(folder);
		IResource[] members = folder.members();
		for (IResource res : members) {
			if (res instanceof IFolder) {
				recordDelItem((IFolder) res);
			} else {
				MonitorSet.getInstance().addSynchroItem(res);
			}
		}
	}
}
