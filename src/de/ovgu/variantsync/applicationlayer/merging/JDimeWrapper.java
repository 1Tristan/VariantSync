package de.ovgu.variantsync.applicationlayer.merging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.fosd.jdime.Main;
import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.datamodel.exception.FileOperationException;
import de.ovgu.variantsync.persistencelayer.IPersistanceOperations;

class JDimeWrapper {
	private static IMergeOperations mergeOperations = ModuleFactory
			.getMergeOperations();
	private static IPersistanceOperations persistanceOperations = ModuleFactory
			.getPersistanceOperations();
	private static final String MODE = "-mode structured";

	/*-mode autotuning -output C:\Users\pfofe\Desktop\TestJDime Z:\Studium\Master\Semester\SS_15\Masterarbeit\variantsyncWorkspace\JDime\testfiles\left\SimpleTests\Bag\Bag.java Z:\Studium\Master\Semester\SS_15\Masterarbeit\variantsyncWorkspace\JDime\testfiles\base\SimpleTests\Bag\Bag.java Z:\Studium\Master\Semester\SS_15\Masterarbeit\variantsyncWorkspace\JDime\testfiles\right\SimpleTests\Bag\Bag.java*/
	/*-Djava.library.path=C:\Users\pfofe\Desktop\winglpk-4.55\glpk-4.55\w64*/

	public static void main(String[] args) {
		merge(new File(""), null, null);
	}

	public static List<String> merge(List<String> left, List<String> base,
			List<String> right) {
		File leftVersion = writeTmpFile(left, "Left");
		File baseVersion = writeTmpFile(base, "Base");
		File rightVersion = writeTmpFile(right, "Right");
		merge(leftVersion, baseVersion, rightVersion);
		// return mergeOperations.performThreeWayMerge(base, left, right);
		return new ArrayList<String>() {
		};
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

	public static String merge(File left, File base, File right) {
		ProcessBuilder builder = new ProcessBuilder(
		// "java",
		// "-jar",
		// "jdime.jar",
		// "-mode autotuning",
		// "-output C:\\Users\\pfofe\\Desktop\\TestJDime",
		// "Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\left\\SimpleTests\\Bag\\Bag.java",
		// "Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\base\\SimpleTests\\Bag\\Bag.java",
		// "Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\right\\SimpleTests\\Bag\\Bag.java"
		);
		List<String> command = new ArrayList<>();
		command.add(left.getAbsolutePath());
		command.add(base.getAbsolutePath());
		command.add(right.getAbsolutePath());

		// command.add("java");command.add("-jar Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\VariantSync\\libraries\\JDime.jar");
		// command.add(MODE);
		// command.add("-output C:\\Users\\pfofe\\Desktop\\TestJDime");

		// command.add("Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\left\\SimpleTests\\Bag\\Bag.java");
		// command.add("Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\base\\SimpleTests\\Bag\\Bag.java");
		// command.add("Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\right\\SimpleTests\\Bag\\Bag.java");

		Main m = new Main(command.toArray(new String[] {}));
		System.out.println(m.toString());

		// builder.command(command);
		// builder.redirectInput();
		// Process process = null;
		// try {
		// process = builder.start();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// StringBuilder text = new StringBuilder();
		//
		// Charset cs = StandardCharsets.UTF_8;
		// BufferedReader r = new BufferedReader(new InputStreamReader(
		// process.getInputStream(), cs));
		// String line;
		// try {
		// while ((line = r.readLine()) != null) {
		// text.append(line).append(System.lineSeparator());
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// try {
		// process.waitFor();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		
		// builder.command(command);
		// Process process = null;
		// try {
		// process = builder.start();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// InputStream is = process.getInputStream();
		// InputStreamReader isr = new InputStreamReader(is);
		// BufferedReader br = new BufferedReader(isr);
		// String line;
		// try {
		// while ((line = br.readLine()) != null) {
		// System.out.println(line);
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		System.out.println("Program terminated!");
		return "";
	}
}
