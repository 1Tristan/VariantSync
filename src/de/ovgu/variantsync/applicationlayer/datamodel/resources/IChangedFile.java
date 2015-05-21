package de.ovgu.variantsync.applicationlayer.datamodel.resources;

import java.io.File;
import java.util.List;

import org.eclipse.swt.graphics.Image;

/**
 * Describes resources which can be synchronized. These resources are files or
 * folders. They can be deleted, added or - in case of files - changed.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 15.05.2015
 */
public interface IChangedFile {

	/**
	 * Returns children of file.
	 * 
	 * @return children
	 */
	public List<IChangedFile> getChildren();

	/**
	 * Returns name of file.
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Returns parent of file.
	 * 
	 * @return parent
	 */
	public IChangedFile getParent();

	public Image getImage();

	public String getTime();

	public boolean hasChildren();

	public String getPath();

	public void addChildren(IChangedFile child);

	public void setParent(IChangedFile parent);

	public void linkFile(File file);

	public void setStatus(String status);

	public String getStatus();

}