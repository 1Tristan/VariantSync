package de.ovgu.variantsync.applicationlayer.merging;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.fosd.jdime.Main;

class JDimeWrapper {

	private static final String MODE = "-mode structured";

	/*-mode autotuning -output C:\Users\pfofe\Desktop\TestJDime Z:\Studium\Master\Semester\SS_15\Masterarbeit\variantsyncWorkspace\JDime\testfiles\left\SimpleTests\Bag\Bag.java Z:\Studium\Master\Semester\SS_15\Masterarbeit\variantsyncWorkspace\JDime\testfiles\base\SimpleTests\Bag\Bag.java Z:\Studium\Master\Semester\SS_15\Masterarbeit\variantsyncWorkspace\JDime\testfiles\right\SimpleTests\Bag\Bag.java*/
	/*-Djava.library.path=C:\Users\pfofe\Desktop\winglpk-4.55\glpk-4.55\w64*/

	public static void main(String[] args) {
		merge(null, null, null);
	}

	public static void merge(List<String> quellCode, List<String> targetCode) {
		merge(null, null, null);
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

		// command.add("java");command.add("-jar Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\VariantSync\\libraries\\JDime.jar");
//		command.add(MODE);
//		command.add("-output C:\\Users\\pfofe\\Desktop\\TestJDime");
		command.add("Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\left\\SimpleTests\\Bag\\Bag.java");
		command.add("Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\base\\SimpleTests\\Bag\\Bag.java");
		command.add("Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\variantsyncWorkspace\\JDime\\testfiles\\right\\SimpleTests\\Bag\\Bag.java");

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
		builder.command(command);
		Process process = null;
		try {
			process = builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		try {
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Program terminated!");
		return "";
	}
}
