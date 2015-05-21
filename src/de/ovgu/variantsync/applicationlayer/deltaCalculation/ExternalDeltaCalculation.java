package de.ovgu.variantsync.applicationlayer.deltacalculation;

import java.util.List;

import de.ovgu.variantsync.applicationlayer.datamodel.exception.PatchException;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;

/**
 * Provides functions to compute deltas (differences) between original and
 * revised files. Encapsulates functions of external difflib.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 15.05.2015
 */
class ExternalDeltaCalculation {

	public String getUnifiedDiff(List<String> content) {
		StringBuilder uniDiff = new StringBuilder();
		for (String s : content) {
			uniDiff.append(s);
			uniDiff.append("\n");
		}
		return uniDiff.toString();
	}

	public Patch createUnifiedDifference(List<String> content) {
		return DiffUtils.parseUnifiedDiff(content);
	}

	public List<String> createUnifiedDifference(String originalFile,
			String revisedFile, List<String> linesOriginalFile, Patch patch,
			int lineNumbers) {
		return DiffUtils.generateUnifiedDiff(originalFile, revisedFile,
				linesOriginalFile, patch, lineNumbers);
	}

	@SuppressWarnings("unchecked")
	public List<String> unpatchText(List<String> content, Patch patch) {
		return (List<String>) DiffUtils.unpatch(content, patch);
	}

	public Patch computeDifference(List<String> content1, List<String> content2) {
		return DiffUtils.diff(content1, content2);
	}

	@SuppressWarnings("unchecked")
	public List<String> computePatch(List<String> content, Patch patch)
			throws PatchException {
		try {
			return (List<String>) DiffUtils.patch(content, patch);
		} catch (PatchFailedException e) {
			throw new PatchException("Patch could not be computed in diffLib.",
					e);
		}
	}

}