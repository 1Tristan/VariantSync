package de.ovgu.variantsync.presentationlayer.view.resourceChanges;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.ovgu.variantsync.VariantSyncConstants;
import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ChangeTypes;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.IChangedFile;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ResourceChangesFile;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ResourceChangesFilePatch;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ResourceChangesFolder;
import de.ovgu.variantsync.presentationlayer.view.eclipseadjustment.VSyncSupportProjectNature;

/**
 * Provides content for resource changes view. Extracts informations about
 * changed/added/removed resources from admin folder and admin file. Uses a
 * whitelist to decide which resources were already been synchronized and which
 * resources are still out of synchronization.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 20.05.2015
 */
public class ResourceChangesContentProvider implements ITreeContentProvider {

	private IChangedFile invisibleRoot;
	private List<IProject> projectList = new ArrayList<IProject>();
	private List<String> whitelist;

	/**
	 * Retrieves all projects from workspace and checks if they support project
	 * nature to decide if a project is under synchronization.
	 */
	private void initalize() {
		projectList.clear();
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot()
				.getProjects()) {
			try {
				if (project.isOpen()
						&& project
								.hasNature(VSyncSupportProjectNature.NATURE_ID)) {
					this.projectList.add(project);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		IChangedFile root;
		invisibleRoot = new ResourceChangesFile("");
		if (projectList.size() > 0) {
			root = new ResourceChangesFolder("Project root", null);
			invisibleRoot.addChildren(root);
			for (IProject project : projectList) {
				IPath adminPath = project.getLocation().append(
						VariantSyncConstants.ADMIN_FOLDER);
				File admin = new File(adminPath.toOSString());
				if (admin.exists()) {
					whitelist = VariantSyncPlugin.getDefault()
							.getChangeEntries();
					scanAdminFiles(admin, root, project);
				}
			}
		} else {
			root = new ResourceChangesFolder("no changes", null);
			invisibleRoot.addChildren(root);
		}
	}

	/**
	 * Scans all files and sub folders of admin folder.
	 * 
	 * @param file
	 * @param root
	 * @param project
	 *            scanned project
	 */
	private void scanAdminFiles(File file, IChangedFile root, IProject project) {
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				IChangedFile subdir = new ResourceChangesFolder(f.getName(),
						project);
				if (root.hasChildren() && root.getChildren().contains(subdir)) {
					IChangedFile tempDir = root.getChildren().get(
							root.getChildren().indexOf(subdir));
					scanAdminFiles(f, tempDir, project);
				} else if (!root.hasChildren()
						|| !root.getChildren().contains(subdir)) {
					subdir.linkFile(f);
					root.addChildren(subdir);
					scanAdminFiles(f, subdir, project);
				}
			}
			if (f.isFile()) {
				String adminFileInfo[] = f.getName().split("_");
				if (adminFileInfo.length >= 4) {
					String event = adminFileInfo[adminFileInfo.length - 3];
					if (event.equals(ChangeTypes.ADDFOLDER)
							|| event.equals(ChangeTypes.REMOVEFOLDER)) {
						IChangedFile subfile = new ResourceChangesFilePatch(
								f.getName(), project);
						subfile.setStatus(event);
						subfile.linkFile(f);
						root.addChildren(subfile);
					} else {
						String adminInfoTeil = "_"
								+ adminFileInfo[adminFileInfo.length - 3] + "_"
								+ adminFileInfo[adminFileInfo.length - 2] + "_"
								+ adminFileInfo[adminFileInfo.length - 1];
						String originName = f.getName();
						if (whitelist.contains(originName)) {
							continue;
						}
						String name = originName.replaceAll(adminInfoTeil, "");
						ResourceChangesFile temp = new ResourceChangesFile(name);
						ResourceChangesFilePatch patch = new ResourceChangesFilePatch(
								f.getName(), project);
						patch.setStatus(adminFileInfo[adminFileInfo.length - 3]);
						patch.linkFile(f);
						if (root.hasChildren()
								&& root.getChildren().contains(temp)) {
							int index = root.getChildren().indexOf(temp);
							IChangedFile existFileKnoten = root.getChildren()
									.get(index);
							((ResourceChangesFile) existFileKnoten)
									.addProject(project);
							existFileKnoten.addChildren(patch);
						} else {
							root.addChildren(temp);
							temp.addProject(project);
							temp.addChildren(patch);
						}
					}
				}
			}
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ResourceChangesView) {
			initalize();
			if (invisibleRoot.hasChildren()) {
				return invisibleRoot.getChildren().toArray();
			}
		}
		if (parentElement instanceof IChangedFile) {
			List<IChangedFile> elements = ((IChangedFile) parentElement)
					.getChildren();
			if (elements != null) {
				Collections.sort(elements,
						ResourceChangesFilePatch.timeComparator);
				return elements.toArray();
			}
		}
		return new Object[0];
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IChangedFile) {
			return ((IChangedFile) element).hasChildren();
		}
		return false;
	}

	@Override
	public Object getParent(Object element) {
		// not required
		return null;
	}

	@Override
	public void dispose() {
		// not required
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// not required
	}
}
