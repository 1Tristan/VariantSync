package de.ovgu.variantsync.applicationlayer.deltaCalculation;

import java.util.List;

class DiffUtilities {

	public static String getUnifiedDiff(List<String> content) {
		StringBuilder uniDiff = new StringBuilder();
		for (String s : content) {
			uniDiff.append(s);
			uniDiff.append("\n");
		}
		return uniDiff.toString();
	}
}
