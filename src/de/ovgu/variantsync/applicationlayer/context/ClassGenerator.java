package de.ovgu.variantsync.applicationlayer.context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;

public class ClassGenerator {

	public ClassGenerator(String filepath, List<CodeLine> code) {
		FileInputStream in = null;
		try {
			in = new FileInputStream("Z:\\Studium\\Master\\Semester\\SS_15\\Masterarbeit\\runtime-EclipseApplication\\"+filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		CompilationUnit cu = null;
		try {
			// parse the file
			cu = JavaParser.parse(in);
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<Node> nodes = cu.getChildrenNodes();
		iterate(nodes);
		System.out.println();
	}
	
	private void iterate(List<Node> nodes) {
		for(Node n : nodes){
			System.out.println(n.toString());
			if(n.getChildrenNodes() != null){
				 iterate(n.getChildrenNodes());
			}
		}
	}

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
