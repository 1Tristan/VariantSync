package de.ovgu.variantsync.applicationlayer.deltaCalculation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.ovgu.variantsync.VariantSyncConstants;
import de.ovgu.variantsync.applicationlayer.datamodel.monitoring.MonitorSet;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ChangeTypes;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ResourceChangesFilePatch;
import de.ovgu.variantsync.persistancelayer.PersistanceOperationProvider;
import difflib.Patch;

/**
 * Computes difference (delta) of a resource. If resource is changed, changes
 * will be computed. The following synonyms are used to rephrase differences
 * between two file versions are:
 * <ul>
 * <li>delta</li>
 * <li>patch</li>
 * </ul>
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 15.05.2015
 */
class DeltaCalculation {

	private String unifiedDiff;
	private ExternalDeltaCalculation externalDeltaOperations;

	public DeltaCalculation() {
		externalDeltaOperations = new ExternalDeltaCalculation();
	}

	/**
	 * Computes string describing changes between two files or one file in
	 * different versions.
	 * 
	 * @param filePatch
	 *            patch of two files or one file in different versions
	 * @return changes of two files described in filePatch object
	 */
	public String getChanges(ResourceChangesFilePatch filePatch) {
		String changes = "";
		changes = changes + "Project:  " + filePatch.getProject().getName()
				+ "\n";
		changes = changes + "Path:  " + filePatch.getPath() + "\n";
		String status = filePatch.getStatus();
		changes = changes + "Operation:  " + status + "\n";
		if (status.equals(ChangeTypes.CHANGE)) {
			changes = changes
					+ "=====================Unified Diff====================="
					+ "\n";
			String diff = filePatch.getUnidiff();
			if (diff == null || diff.isEmpty()) {
				diff = getUnifieddiff(filePatch);
			}
			changes = changes + diff;
		}
		return changes;
	}

	/**
	 * Computes difference (delta) between actual and ancient version of changed
	 * file and creates new file with change information in admin folder.
	 * 
	 * @param res
	 *            changed resource
	 */
	public void createPatch(IResource res) {
		IFile currentFile = (IFile) res;
		IFileState[] states = null;
		try {
			states = currentFile.getHistory(null);
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
		List<String> historyFilelines = null;
		List<String> currentFilelines = null;
		try {
			historyFilelines = getFileContent(new BufferedReader(
					new InputStreamReader(states[0].getContents(),
							states[0].getCharset())));
			currentFilelines = getFileContent(new BufferedReader(
					new InputStreamReader(currentFile.getContents(),
							currentFile.getCharset())));
		} catch (UnsupportedEncodingException | CoreException e) {
			e.printStackTrace();
		}

		Patch patch = externalDeltaOperations.computeDifference(
				historyFilelines, currentFilelines);
		if (patch.getDeltas().size() == 0) {
			return;
		}
		String filename = currentFile.getName();

		List<String> unifiedDiff = externalDeltaOperations
				.createUnifiedDifference(filename, filename, historyFilelines,
						patch, 0);
		int pointer = 0;
		if (MonitorSet.getInstance().removeSynchroItem(res)) {
			pointer = 1;
		}
		String diffFileName = res.getName() + "_" + ChangeTypes.CHANGE + "_"
				+ pointer + "_" + System.currentTimeMillis();

		File parentFile = res.getProjectRelativePath().toFile().getParentFile();
		String subPath = null;
		if (parentFile == null) {
			subPath = "";
		} else {
			subPath = parentFile.getPath();
		}

		IPath diffFilePath = res.getProject().getLocation()
				.append(VariantSyncConstants.ADMIN_FOLDER).append(subPath);
		File diffFile = new File(diffFilePath.append(diffFileName).toOSString());

		try {
			PersistanceOperationProvider.getInstance().addLinesToFile(
					unifiedDiff, diffFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates patch object from changed file.
	 * 
	 * @param changedFile
	 *            changed file
	 * @return patch object
	 */
	public synchronized Patch getPatch(
			ResourceChangesFilePatch changedFile) {
		Patch patch = null;
		String parentFolder = changedFile.getFile().getParentFile().getPath();
		IPath base = new Path(changedFile.getProject().getLocation()
				.toOSString());
		IPath temp = new Path(parentFolder).append(changedFile
				.getPatchFileName());
		IPath relativePath = temp.makeRelativeTo(base);
		if (changedFile.getStatus().equals(ChangeTypes.CHANGE)) {
			if (!changedFile.getProject().getFile(relativePath).exists()) {
				try {
					changedFile.getProject().getFile(relativePath)
							.refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			if (changedFile.getProject().getFile(relativePath).exists()) {
				try {
					List<String> lines = PersistanceOperationProvider
							.getInstance().readFile(
									changedFile.getProject()
											.getFile(relativePath)
											.getContents());
					unifiedDiff = externalDeltaOperations.getUnifiedDiff(lines);
					patch = externalDeltaOperations
							.createUnifiedDifference(lines);
				} catch (Exception e) {
					e.printStackTrace();
					unifiedDiff = "";
				}
			} else {
				try {
					changedFile.getProject().getFile(relativePath)
							.refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return patch;
	}

	/**
	 * Computes unified difference of a changed file.
	 * 
	 * @param changedFile
	 *            changed file
	 * @return changes as string
	 */
	public String getUnifieddiff(ResourceChangesFilePatch changedFile) {
		if (unifiedDiff == null) {
			getPatch(changedFile);
		}
		return unifiedDiff;
	}

	/**
	 * Reads content from file using buffered reader. Adds each line in file to
	 * List<String>.
	 * 
	 * @param reader
	 *            buffered Reader for file
	 * @return list with file content
	 */
	private List<String> getFileContent(BufferedReader reader) {
		List<String> fileContent = new LinkedList<String>();
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileContent;
	}
}
