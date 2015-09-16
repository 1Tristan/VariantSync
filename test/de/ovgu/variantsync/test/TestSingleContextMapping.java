package de.ovgu.variantsync.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.context.IContextOperations;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaProject;

/**
 * Basis for all tests (except AddCodeToEmptyContext-Test) is the adding of the
 * following code:
 * 
 * 5 private int a; 7 public Main(int g) { 8 a = g; 9 } 10 11 public int getA()
 * { 12 return a; 13 } 14 15 public void setA(int a) { 16 this.a = a; 17 }
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 06.09.2015
 */
public class TestSingleContextMapping {

	private final String FEATURE_EXPRESSION = "Test";
	private Context context;
	private IContextOperations co = ModuleFactory.getContextOperations();

	@Before
	public void before() {
		context = new Context("TestProject", "arbitraryPathToProject",
				FEATURE_EXPRESSION);
	}

	@Test
	public void testAddCodeToEmptyContext() {
		List<String> diff = new ArrayList<String>();

		// add code to empty context
		String[] diffArray = "--- Main.java, +++ Main.java, @@ -5,0 +5,1 @@, +	private int a;, @@ -6,0 +7,11 @@, +	public Main(int g) {, +		a = g;, +	}, +, +	public int getA() {, +		return a;, +	}, +, +	public void setA(int a) {, +		this.a = a;, +	}"
				.split(", ");
		for (String s : diffArray) {
			diff.add(s);
		}
		System.out.println("\n" + diff.toString());
		co.addCode("mainpackage", "Main.java", diff, context);

		JavaProject jp = context.getJavaProject();
		List<CodeLine> codeOfClass = jp.getChildren().get(0).getChildren()
				.get(0).getClonedCodeLines();

		CodeLine cl = codeOfClass.get(0);
		assertEquals(cl.getCode(), "private int a;");
		assertEquals(cl.getLine(), 5);
		cl = codeOfClass.get(1);
		assertEquals(cl.getCode(), "public Main(int g) {");
		assertEquals(cl.getLine(), 7);
		cl = codeOfClass.get(2);
		assertEquals(cl.getCode(), "a = g;");
		assertEquals(cl.getLine(), 8);
		cl = codeOfClass.get(3);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 9);
		cl = codeOfClass.get(4);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 10);
		cl = codeOfClass.get(5);
		assertEquals(cl.getCode(), "public int getA() {");
		assertEquals(cl.getLine(), 11);
		cl = codeOfClass.get(6);
		assertEquals(cl.getCode(), "return a;");
		assertEquals(cl.getLine(), 12);
		cl = codeOfClass.get(7);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 13);
		cl = codeOfClass.get(8);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 14);
		cl = codeOfClass.get(9);
		assertEquals(cl.getCode(), "public void setA(int a) {");
		assertEquals(cl.getLine(), 15);
		cl = codeOfClass.get(10);
		assertEquals(cl.getCode(), "this.a = a;");
		assertEquals(cl.getLine(), 16);
		cl = codeOfClass.get(11);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 17);

		System.out.println("\n" + jp.toString());
	}

	@Test
	public void testAddCodeInsideExistingCode() {
		List<String> diff = new ArrayList<String>();

		// add code to empty context
		String[] diffArray = "--- Main.java, +++ Main.java, @@ -5,0 +5,1 @@, +	private int a;, @@ -6,0 +7,11 @@, +	public Main(int g) {, +		a = g;, +	}, +, +	public int getA() {, +		return a;, +	}, +, +	public void setA(int a) {, +		this.a = a;, +	}"
				.split(", ");
		for (String s : diffArray) {
			diff.add(s);
		}
		System.out.println("\n" + diff.toString());
		co.addCode("mainpackage", "Main.java", diff, context);

		JavaProject jp = context.getJavaProject();
		System.out.println("\n" + jp.toString() + "\n");

		// add code line inside existing code
		diffArray = "--- Main.java, +++ Main.java, @@ -6,0 +6,1 @@, +	private int b;, @@ -7,1 +8,1 @@, -	public Main(int g) {, +	public Main(int g, int h) {, @@ -9,0 +10,1 @@, +		b = h;"
				.split(", ");
		diff.clear();
		for (String s : diffArray) {
			diff.add(s);
		}
		diff.set(6, "+	public Main(int g, int h) {");
		diff.remove(7);
		co.addCode("mainpackage", "Main.java", diff, context);

		List<CodeLine> codeOfClass = jp.getChildren().get(0).getChildren()
				.get(0).getClonedCodeLines();

		CodeLine cl = codeOfClass.get(0);
		assertEquals(cl.getCode(), "private int a;");
		assertEquals(cl.getLine(), 5);
		cl = codeOfClass.get(1);
		assertEquals(cl.getCode(), "private int b;");
		assertEquals(cl.getLine(), 6);
		cl = codeOfClass.get(2);
		assertEquals(cl.getCode(), "public Main(int g, int h) {");
		assertEquals(cl.getLine(), 8);
		cl = codeOfClass.get(3);
		assertEquals(cl.getCode(), "a = g;");
		assertEquals(cl.getLine(), 9);
		cl = codeOfClass.get(4);
		assertEquals(cl.getCode(), "b = h;");
		assertEquals(cl.getLine(), 10);
		cl = codeOfClass.get(5);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 11);
		cl = codeOfClass.get(6);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 12);
		cl = codeOfClass.get(7);
		assertEquals(cl.getCode(), "public int getA() {");
		assertEquals(cl.getLine(), 13);
		cl = codeOfClass.get(8);
		assertEquals(cl.getCode(), "return a;");
		assertEquals(cl.getLine(), 14);
		cl = codeOfClass.get(9);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 15);
		cl = codeOfClass.get(10);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 16);
		cl = codeOfClass.get(11);
		assertEquals(cl.getCode(), "public void setA(int a) {");
		assertEquals(cl.getLine(), 17);
		cl = codeOfClass.get(12);
		assertEquals(cl.getCode(), "this.a = a;");
		assertEquals(cl.getLine(), 18);
		cl = codeOfClass.get(13);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 19);

		System.out.println("\n" + jp.toString());
	}

	@Test
	public void testAddCodeInsideExistingCode_Simple() {
		List<String> diff = new ArrayList<String>();

		// add code to empty context
		String[] diffArray = "--- Main.java, +++ Main.java, @@ -5,0 +5,1 @@, +	private int a;, @@ -6,0 +7,11 @@, +	public Main(int g) {, +		a = g;, +	}, +, +	public int getA() {, +		return a;, +	}, +, +	public void setA(int a) {, +		this.a = a;, +	}"
				.split(", ");
		for (String s : diffArray) {
			diff.add(s);
		}
		System.out.println("\n" + diff.toString());
		co.addCode("mainpackage", "Main.java", diff, context);

		JavaProject jp = context.getJavaProject();
		System.out.println("\n" + jp.toString() + "\n");

		// add code line inside existing code
		diffArray = "--- Main.java, +++ Main.java, @@ -6,0 +6,1 @@, +	private int b;, @@ -7,1 +8,1 @@, -	public Main(int g) {, +	public Main(int g, int h) {, @@ -9,0 +10,1 @@, +		b = h;"
				.split(", ");
		diff.clear();
		for (String s : diffArray) {
			diff.add(s);
		}
		diff.set(6, "+	public Main(int g, int h) {");
		diff.remove(7);
		co.addCode("mainpackage", "Main.java", diff, context);

		List<CodeLine> codeOfClass = jp.getChildren().get(0).getChildren()
				.get(0).getClonedCodeLines();

		CodeLine cl = codeOfClass.get(0);
		assertEquals(cl.getCode(), "private int a;");
		assertEquals(cl.getLine(), 5);
		cl = codeOfClass.get(1);
		assertEquals(cl.getCode(), "private int b;");
		assertEquals(cl.getLine(), 6);
		cl = codeOfClass.get(2);
		assertEquals(cl.getCode(), "public Main(int g, int h) {");
		assertEquals(cl.getLine(), 8);
		cl = codeOfClass.get(3);
		assertEquals(cl.getCode(), "a = g;");
		assertEquals(cl.getLine(), 9);
		cl = codeOfClass.get(4);
		assertEquals(cl.getCode(), "b = h;");
		assertEquals(cl.getLine(), 10);
		cl = codeOfClass.get(5);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 11);
		cl = codeOfClass.get(6);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 12);
		cl = codeOfClass.get(7);
		assertEquals(cl.getCode(), "public int getA() {");
		assertEquals(cl.getLine(), 13);
		cl = codeOfClass.get(8);
		assertEquals(cl.getCode(), "return a;");
		assertEquals(cl.getLine(), 14);
		cl = codeOfClass.get(9);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 15);
		cl = codeOfClass.get(10);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 16);
		cl = codeOfClass.get(11);
		assertEquals(cl.getCode(), "public void setA(int a) {");
		assertEquals(cl.getLine(), 17);
		cl = codeOfClass.get(12);
		assertEquals(cl.getCode(), "this.a = a;");
		assertEquals(cl.getLine(), 18);
		cl = codeOfClass.get(13);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 19);

		System.out.println("\n" + jp.toString());
	}

	@Test
	public void testAddCodeInsideExistingCode_Comprehensive1() {
		List<String> diff = new ArrayList<String>();

		// add code to empty context
		String[] diffArray = "--- Main.java, +++ Main.java, @@ -4,2 +4,5 @@, -	, -	, +, +	public Main(int g) {, +		System.out.println(\"This is a test.\");, +	}, +"
				.split(", ");
		for (String s : diffArray) {
			diff.add(s);
		}
		System.out.println("\n" + diff.toString());
		co.addCode("mainpackage", "Main.java", diff, context);

		JavaProject jp = context.getJavaProject();
		System.out.println("\n" + jp.toString() + "\n");

		// add code line inside existing code
		diffArray = "--- Main.java, +++ Main.java, @@ -5,1 +5,5 @@, -	public Main(int g) {, +	private boolean isFalse;, +	private int i = 0;, +, +	public Main(int g, boolean isfalse) {, +		isFalse = true;, @@ -7,0 +11,1 @@, +		this.isFalse = isfalse;, @@ -8,0 +13,5 @@, +	, +	private int helpMe(int i) {, +		i = i * 10;, +		return -1;, +	}, @@ -9,0 +19,9 @@, +	public void setA(int a) {, +		helpMe(4);, +	}, +, +	public void test() {, +		System.out.println(\"This is not a test!\");, +		System.out.println(\"This is not a test, too!\");, +	}, +"
				.split(", ");
		diff.clear();
		for (String s : diffArray) {
			diff.add(s);
		}
		diff.set(7, "+	public Main(int g, boolean isfalse) {");
		diff.remove(8);
		diff.set(24, "+		System.out.println(\"This is not a test, too!\");");
		diff.remove(25);
		co.addCode("mainpackage", "Main.java", diff, context);

		List<CodeLine> codeOfClass = jp.getChildren().get(0).getChildren()
				.get(0).getClonedCodeLines();

		System.out.println("\n" + jp.toString());

		CodeLine cl = codeOfClass.get(0);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 4);
		cl = codeOfClass.get(1);
		assertEquals(cl.getCode(), "private boolean isFalse;");
		assertEquals(cl.getLine(), 5);
		cl = codeOfClass.get(2);
		assertEquals(cl.getCode(), "private int i = 0;");
		assertEquals(cl.getLine(), 6);
		cl = codeOfClass.get(3);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 7);
		cl = codeOfClass.get(4);
		assertEquals(cl.getCode(), "public Main(int g, boolean isfalse) {");
		assertEquals(cl.getLine(), 8);
		cl = codeOfClass.get(5);
		assertEquals(cl.getCode(), "isFalse = true;");
		assertEquals(cl.getLine(), 9);
		cl = codeOfClass.get(6);
		assertEquals(cl.getCode(), "System.out.println(\"This is a test.\");");
		assertEquals(cl.getLine(), 10);
		cl = codeOfClass.get(7);
		assertEquals(cl.getCode(), "this.isFalse = isfalse;");
		assertEquals(cl.getLine(), 11);
		cl = codeOfClass.get(8);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 12);
		cl = codeOfClass.get(9);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 13);
		cl = codeOfClass.get(10);
		assertEquals(cl.getCode(), "private int helpMe(int i) {");
		assertEquals(cl.getLine(), 14);
		cl = codeOfClass.get(11);
		assertEquals(cl.getCode(), "i = i * 10;");
		assertEquals(cl.getLine(), 15);
		cl = codeOfClass.get(12);
		assertEquals(cl.getCode(), "return -1;");
		assertEquals(cl.getLine(), 16);
		cl = codeOfClass.get(13);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 17);
		cl = codeOfClass.get(14);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 18);
		cl = codeOfClass.get(15);
		assertEquals(cl.getCode(), "public void setA(int a) {");
		assertEquals(cl.getLine(), 19);
		cl = codeOfClass.get(16);
		assertEquals(cl.getCode(), "helpMe(4);");
		assertEquals(cl.getLine(), 20);
		cl = codeOfClass.get(17);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 21);
		cl = codeOfClass.get(18);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 22);
		cl = codeOfClass.get(19);
		assertEquals(cl.getCode(), "public void test() {");
		assertEquals(cl.getLine(), 23);
		cl = codeOfClass.get(20);
		assertEquals(cl.getCode(),
				"System.out.println(\"This is not a test!\");");
		assertEquals(cl.getLine(), 24);
		cl = codeOfClass.get(21);
		assertEquals(cl.getCode(),
				"System.out.println(\"This is not a test, too!\");");
		assertEquals(cl.getLine(), 25);
		cl = codeOfClass.get(22);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 26);
		cl = codeOfClass.get(23);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 27);
	}

	@Test
	public void testAddCodeInsideExistingCode_Comprehensive2() {
		List<String> diff = new ArrayList<String>();

		// add code to empty context
		String[] diffArray = "--- Main.java, +++ Main.java, @@ -5,1 +5,3 @@, -, +	public Main(int g) {, +		System.out.println(\"This is a test.\");, +	}"
				.split(", ");
		for (String s : diffArray) {
			diff.add(s);
		}
		System.out.println("\n" + diff.toString());
		co.addCode("mainpackage", "Main.java", diff, context);

		JavaProject jp = context.getJavaProject();
		System.out.println("\n" + jp.toString() + "\n");

		// add code line inside existing code
		diffArray = "--- Main.java, +++ Main.java, @@ -5,1 +5,5 @@, -	public Main(int g) {, +	private boolean isFalse;, +	private int i = 0;, +	, +	public Main(int g, boolean isFalse) {, +		isFalse = true;, @@ -7,0 +11,1 @@, +		this.isFalse = isFalse;, @@ -8,0 +13,14 @@, +	, +	private int helpMe(int i) {, +		i = i * 10;, +		return -1;, +	}, +	, +	public void setA(int a) {, +		helpMe(4);, +	}, +	, +	public void test() {, +		System.out.println(\"This is not a test!\");, +		System.out.println(\"This is not a test, too!\");, +	}"
				.split(", ");

		diff.clear();
		for (String s : diffArray) {
			diff.add(s);
		}
		diff.set(7, "+	public Main(int g, boolean isFalse) {");
		diff.remove(8);
		diff.set(24, "+		System.out.println(\"This is not a test, too!\");");
		diff.remove(25);
		co.addCode("mainpackage", "Main.java", diff, context);

		List<CodeLine> codeOfClass = jp.getChildren().get(0).getChildren()
				.get(0).getClonedCodeLines();

		System.out.println("\n" + jp.toString());

		// TODO create new Test -> Überblick über Änderungen verloren!

		int i = 0;
		int line = 5;
		CodeLine cl = codeOfClass.get(i);
		assertEquals(cl.getCode(), "private boolean isFalse;");
		assertEquals(cl.getLine(), line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "private int i = 0;");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "public Main(int g, boolean isFalse) {");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "isFalse = true;");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "System.out.println(\"This is a test.\");");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "this.isFalse = isFalse;");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "private int helpMe(int i) {");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "i = i * 10;");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "return -1;");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "public void setA(int a) {");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "helpMe(4);");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "public void test() {");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(),
				"System.out.println(\"This is not a test!\");");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(),
				"System.out.println(\"This is not a test, too!\");");
		assertEquals(cl.getLine(), ++line);
		cl = codeOfClass.get(++i);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), ++line);
	}

	@Test
	public void testRemoveCode() {
		List<String> diff = new ArrayList<String>();
		String[] diffArray = "--- Main.java, +++ Main.java, @@ -5,0 +5,1 @@, +	private int a;, @@ -6,0 +7,11 @@, +	public Main(int g) {, +		a = g;, +	}, +, +	public int getA() {, +		return a;, +	}, +, +	public void setA(int a) {, +		this.a = a;, +	}"
				.split(", ");
		for (String s : diffArray) {
			diff.add(s);
		}
		System.out.println("\n" + diff.toString());
		co.addCode("mainpackage", "Main.java", diff, context);

		JavaProject jp = context.getJavaProject();
		System.out.println("\n" + jp.toString() + "\n");
		List<CodeLine> codeOfClass = jp.getChildren().get(0).getChildren()
				.get(0).getClonedCodeLines();

		// remove code from the inside of existing code
		diffArray = "--- Main.java, +++ Main.java, @@ -11,4 +11,0 @@, -	public int getA() {, -		return a;, -	}, -"
				.split(", ");
		diff.clear();
		for (String s : diffArray) {
			diff.add(s);
		}
		System.out.println("\n" + diff.toString());
		co.addCode("mainpackage", "Main.java", diff, context);

		codeOfClass = jp.getChildren().get(0).getChildren().get(0)
				.getClonedCodeLines();

		CodeLine cl = codeOfClass.get(0);
		assertEquals(cl.getCode(), "private int a;");
		assertEquals(cl.getLine(), 5);
		cl = codeOfClass.get(1);
		assertEquals(cl.getCode(), "public Main(int g) {");
		assertEquals(cl.getLine(), 7);
		cl = codeOfClass.get(2);
		assertEquals(cl.getCode(), "a = g;");
		assertEquals(cl.getLine(), 8);
		cl = codeOfClass.get(3);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 9);
		cl = codeOfClass.get(4);
		assertEquals(cl.getCode(), "");
		assertEquals(cl.getLine(), 10);
		cl = codeOfClass.get(5);
		assertEquals(cl.getCode(), "public void setA(int a) {");
		assertEquals(cl.getLine(), 11);
		cl = codeOfClass.get(6);
		assertEquals(cl.getCode(), "this.a = a;");
		assertEquals(cl.getLine(), 12);
		cl = codeOfClass.get(7);
		assertEquals(cl.getCode(), "}");
		assertEquals(cl.getLine(), 13);

		System.out.println("\n" + jp.toString());
	}
}
