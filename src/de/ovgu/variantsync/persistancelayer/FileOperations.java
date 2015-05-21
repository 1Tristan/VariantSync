package de.ovgu.variantsync.persistancelayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Provides operations to handle files on file system.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 15.05.2015
 */
class FileOperations {

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
	public void addLinesToFile(List<String> lines, File file)
			throws IOException {
		if (file.exists()) {
			file.delete();
		}
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		PrintWriter out = null;
		try {
			file.createNewFile();
			out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			for (int i = 0; i < lines.size(); i++) {
				out.println(lines.get(i));
			}
		} catch (IOException e) {
			throw new IOException("File could not be created.");
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * Reads TXT-file and adds each line to list of string elements.
	 * 
	 * @param inputStream
	 *            input file
	 * @return list of file content
	 * @throws IOException
	 *             file could not be read
	 */
	public List<String> readFile(InputStream inputStream) throws IOException {
		List<String> fileLines = new LinkedList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String line = "";
		while ((line = reader.readLine()) != null) {
			fileLines.add(line);
		}
		return fileLines;
	}

	/**
	 * Creates an IFile-object.
	 * 
	 * @param file
	 *            file to create
	 * @return created IFile
	 * @throws CoreException
	 *             file could not be created
	 */
	public IFile createIFile(IFile file) throws CoreException {
		String content = "";
		InputStream source = new ByteArrayInputStream(content.getBytes());
		file.create(source, IResource.FORCE, null);
		return file;
	}
}
