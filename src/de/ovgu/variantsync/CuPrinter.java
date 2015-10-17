package de.ovgu.variantsync;

import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class CuPrinter {

	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		FileInputStream in = new FileInputStream(
				"Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\runtime-EclipseApplication\\Test1\\src\\mainPackage\\Coloring.java");

		CompilationUnit cu;
		try {
			// parse the file
			cu = JavaParser.parse(in);
		} finally {
			in.close();
		}

		// prints the resulting compilation unit to default system output
		System.out.println(cu.getChildrenNodes());
		System.out.println();
		System.out.println(cu.toString());
	}
}