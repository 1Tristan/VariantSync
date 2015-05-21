package de.ovgu.variantsync.persistancelayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.variantsync.applicationlayer.datamodel.monitoring.MonitorItemStorage;

/**
 * Provides functions to handle folders and different kinds of files on file
 * system.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 17.05.2015
 */
public class PersistanceOperationProvider implements IPersistanceOperations {

	private FileOperations fileOperations;
	private FolderOperations folderOperations;
	private XMLOperations xmlOperations;
	private AdminFolderManager adminFolderManager;

	private static PersistanceOperationProvider instance;

	private PersistanceOperationProvider() {
		fileOperations = new FileOperations();
		folderOperations = new FolderOperations();
		xmlOperations = new XMLOperations();
		adminFolderManager = new AdminFolderManager();
	}

	public static PersistanceOperationProvider getInstance() {
		if (instance == null) {
			instance = new PersistanceOperationProvider();
		}
		return instance;
	}

	@Override
	public void mkdirs(IFolder folder) throws CoreException {
		folderOperations.mkdirs(folder);
	}

	@Override
	public void deldirs(IFolder folder) throws CoreException {
		folderOperations.deldirs(folder);
	}

	@Override
	public void addLinesToFile(List<String> lines, File file)
			throws IOException {
		fileOperations.addLinesToFile(lines, file);
	}

	@Override
	public List<String> readFile(InputStream inputStream) throws IOException {
		return fileOperations.readFile(inputStream);
	}

	@Override
	public IFile createIFile(IFile file) throws CoreException {
		return fileOperations.createIFile(file);
	}

	@Override
	public MonitorItemStorage readSynchroXMLFile(InputStream inputStream) {
		return xmlOperations.readSynchroXMLFile(inputStream);
	}

	@Override
	public void writeXMLFile(File file, MonitorItemStorage info)
			throws FileNotFoundException {
		xmlOperations.writeXMLFile(file, info);
	}

	@Override
	public void addAdminResource(IResource res) {
		adminFolderManager.add(res);
	}

	@Override
	public void removeAdminFile(IResource res) {
		adminFolderManager.remove(res);
	}

}