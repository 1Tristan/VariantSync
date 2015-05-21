package de.ovgu.variantsync.applicationlayer.deltacalculation;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.ovgu.variantsync.VariantSyncConstants;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.datamodel.exception.FileOperationException;
import de.ovgu.variantsync.applicationlayer.datamodel.monitoring.MonitorSet;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ChangeTypes;
import de.ovgu.variantsync.applicationlayer.datamodel.resources.ResourceChangesFilePatch;
import de.ovgu.variantsync.persistancelayer.IPersistanceOperations;
import de.ovgu.variantsync.utilitylayer.log.LogOperations;
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
	private IPersistanceOperations persistanceOperations = ModuleFactory
			.getPersistanceOperations();

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
			LogOperations.logError("File states could not be retrieved.", e);
			return;
		}
		List<String> historyFilelines = null;
		List<String> currentFilelines = null;
		try {
			historyFilelines = persistanceOperations.readFile(
					states[0].getContents(), states[0].getCharset());
			currentFilelines = persistanceOperations.readFile(
					currentFile.getContents(), currentFile.getCharset());
		} catch (CoreException | FileOperationException e) {
			LogOperations.logError("File states could not be read.", e);
		}

		Patch patch = externalDeltaOperations.computeDifference(
				historyFilelines, currentFilelines);
		if (patch.getDeltas().isEmpty()) {
			return;
		}
		String filename = currentFile.getName();

		List<String> tmpUnifiedDiff = externalDeltaOperations
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
			persistanceOperations.addLinesToFile(tmpUnifiedDiff, diffFile);
		} catch (FileOperationException e) {
			LogOperations
					.logError(
							"Lines could not be added to diff file in admin folder.",
							e);
		}
	}

	/**
	 * Creates patch object from changed file.
	 * 
	 * @param changedFile
	 *            changed file
	 * @return patch object
	 */
	public synchronized Patch getPatch(ResourceChangesFilePatch changedFile) {
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
					LogOperations.logError(
							"File could not be refreshed in workspace.", e);
				}
			}
			if (changedFile.getProject().getFile(relativePath).exists()) {
				try {
					List<String> lines = persistanceOperations
							.readFile(changedFile.getProject()
									.getFile(relativePath).getContents());
					unifiedDiff = externalDeltaOperations.getUnifiedDiff(lines);
					patch = externalDeltaOperations
							.createUnifiedDifference(lines);
				} catch (CoreException | FileOperationException e) {
					LogOperations.logError(
							"Diff file in admin folder could not be read.", e);
					unifiedDiff = "";
				}
			} else {
				try {
					changedFile.getProject().getFile(relativePath)
							.refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException e) {
					LogOperations.logError(
							"File could not be refreshed in workspace.", e);
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
}
