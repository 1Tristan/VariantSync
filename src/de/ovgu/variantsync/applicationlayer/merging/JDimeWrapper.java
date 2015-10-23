package de.ovgu.variantsync.applicationlayer.merging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.fosd.jdime.Main;
import de.ovgu.variantsync.VariantSyncConstants;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.datamodel.exception.FileOperationException;
import de.ovgu.variantsync.persistencelayer.IPersistanceOperations;

class JDimeWrapper {

	private static IPersistanceOperations persistanceOperations = ModuleFactory
			.getPersistanceOperations();

	public static void main(String[] args) {
		merge(new File(""), null, null);
	}

	public static List<String> merge(List<String> left, List<String> base,
			List<String> right) {
		File leftVersion = writeTmpFile(left, "Left");
		File baseVersion = writeTmpFile(base, "Base");
		File rightVersion = writeTmpFile(right, "Right");
		merge(leftVersion, baseVersion, rightVersion);
		List<String> lines = null;
		try {
			lines = persistanceOperations.readFile(new FileInputStream(
					new File(VariantSyncConstants.MERGE_OUTPUT)));
		} catch (FileNotFoundException | FileOperationException e) {
			e.printStackTrace();
		}
		return lines;
	}

	private static File writeTmpFile(List<String> code, String filename) {
		File f = new File("C:\\Users\\pfofe\\Desktop\\" + filename + ".java");
		try {
			persistanceOperations.addLinesToFile(code, f);
		} catch (FileOperationException e) {
			e.printStackTrace();
		}
		return f;
	}

	private static String merge(File left, File base, File right) {
		List<String> command = new ArrayList<>();
		command.add(left.getAbsolutePath());
		command.add(base.getAbsolutePath());
		command.add(right.getAbsolutePath());

		File f = new File(VariantSyncConstants.MERGE_OUTPUT);
		if(f.exists())
			f.delete();
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Main(command.toArray(new String[] {}),
				VariantSyncConstants.MERGE_OUTPUT);
		return "";
	}
}
