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
 * Defines functions to handle folders and different kinds of files on file
 * system.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 17.05.2015
 */
public interface IPersistanceOperations {

	/**
	 * Creates specified folder.
	 * 
	 * @param folder
	 *            IFolder object
	 * @throws CoreException
	 *             folder could not be created
	 */
	void mkdirs(IFolder folder) throws CoreException;

	/**
	 * Deletes specified folder.
	 * 
	 * @param folder
	 *            IFolder object
	 * @throws CoreException
	 *             folder could not be deleted
	 */
	void deldirs(IFolder folder) throws CoreException;

	/**
	 * Creates file or folder in admin folder ".variantsync" which maps original
	 * project structure. Each file contains informations about changes.
	 * 
	 * @param res
	 *            resource to add
	 */
	void addAdminResource(IResource res);

	/**
	 * Removes file or folder from admin folder ".variantsync".
	 * 
	 * @param res
	 *            resource to remove
	 */
	void removeAdminFile(IResource res);

	/**
	 * Adds lines to specified file.
	 * 
	 * @param lines
	 *            lines to add
	 * @param file
	 *            target file
	 * @throws IOException
	 *             file could not be created
	 */
	void addLinesToFile(List<String> lines, File file) throws IOException;

	/**
	 * Reads TXT-file and adds each line to list of string elements.
	 * 
	 * @param inputStream
	 *            input file
	 * @return list of file content
	 * @throws IOException
	 *             file could not be read
	 */
	List<String> readFile(InputStream inputStream) throws IOException;

	/**
	 * Creates an IFile-object.
	 * 
	 * @param file
	 *            file to create
	 * @return created IFile
	 * @throws CoreException
	 *             file could not be created
	 */
	IFile createIFile(IFile file) throws CoreException;

	/**
	 * Reads a xml file and creates a SynchroInfo object.
	 * 
	 * @param inputStream
	 * @return SynchroInfo object
	 */
	MonitorItemStorage readSynchroXMLFile(InputStream inputStream);

	/**
	 * Writes SynchroInfo object in xml file.
	 * 
	 * @param file
	 *            target file
	 * @param info
	 *            SynchroInfo object
	 * @throws FileNotFoundException
	 *             target does not exists
	 */
	void writeXMLFile(File file, MonitorItemStorage info)
			throws FileNotFoundException;
}
