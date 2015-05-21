package de.ovgu.variantsync.applicationlayer.datamodel.resources;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Represents a folder which can be synchronized.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 15.05.2015
 */
public class ResourceChangesFolder implements IChangedFile {

	private String name;
	private ArrayList<IChangedFile> children;
	private String status;

	public ResourceChangesFolder(String name, IProject project) {
		this.name = name;
	}

	@Override
	public ArrayList<IChangedFile> getChildren() {
		return this.children;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public IChangedFile getParent() {
		return null;
	}

	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_FOLDER);
	}

	@Override
	public boolean hasChildren() {
		if (children != null) {
			return this.children.size() > 0;
		} else {
			return false;
		}
	}

	@Override
	public void addChildren(IChangedFile child) {
		if (children == null) {
			children = new ArrayList<IChangedFile>();
		}
		child.setParent(this);
		children.add(child);
	}

	@Override
	public void setParent(IChangedFile parent) {
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResourceChangesFolder) {
			return this.name.equals(((ResourceChangesFolder) obj).name);
		}
		return false;
	}

	@Override
	public int hashCode() {

		return this.name.hashCode();
	}

	@Override
	public String getTime() {
		return null;
	}

	@Override
	public void linkFile(File file) {
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

}
