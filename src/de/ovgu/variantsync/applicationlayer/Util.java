package de.ovgu.variantsync.applicationlayer;

import java.util.List;

import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaElement;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 16.09.2015
 */
public class Util {

	public Util() {
		// TODO Auto-generated constructor stub
	}

	public static void getClassesByClassName(List<JavaElement> elements,
			List<JavaClass> classes, String className) {
		if (elements != null && !elements.isEmpty()) {
			for (JavaElement je : elements) {
				if (je instanceof JavaClass && je.getName().equals(className)) {
					classes.add((JavaClass) je);
				} else {
					getClassesByClassName(je.getChildren(), classes, className);
				}
			}
		}
	}

	public static boolean containsClass(List<JavaElement> elements,
			String className) {
		if (elements != null && !elements.isEmpty()) {
			for (JavaElement je : elements) {
				if (je instanceof JavaClass && je.getName().equals(className)) {
					return true;
				} else {
					return containsClass(je.getChildren(), className);
				}
			}
		}
		return false;
	}

	// TODO nur hier einmal zentral auslagern
	// public static void refreshMarker(IFile iFile) {
	// MarkerHandler.getInstance().clearAllMarker(iFile);
	// List<MarkerInformation> markers = initMarker(activeContext, iFile
	// .toString().substring(iFile.toString().lastIndexOf("/") + 1));
	//
	// MarkerHandler.getInstance().setMarker(iFile, markers);
	// }
	//
	// private static List<MarkerInformation> initMarker(Context context, String
	// className) {
	// List<MarkerInformation> markers = new ArrayList<MarkerInformation>();
	//
	// JavaProject jp = context.getJavaProject();
	// List<JavaElement> elements = jp.getChildren();
	// List<JavaClass> classes = new ArrayList<JavaClass>();
	// Util.getClassesWithClassName(elements, classes, className);
	// for (JavaClass c : classes) {
	// List<CodeLine> cls = c.getCodeLines();
	// int i = 0;
	// List<CodeLine> tmp = new ArrayList<CodeLine>();
	// for (CodeLine cl : cls) {
	// tmp.add(cl);
	// if (cls.size() > i + 1
	// && cls.get(i + 1).getLine() == cl.getLine() + 1) {
	// tmp.add(cls.get(i + 1));
	// } else {
	// MarkerInformation mi = new MarkerInformation(0, tmp.get(0)
	// .getLine(), tmp.get(tmp.size() - 1).getLine(), 0, 0);
	// mi.setFeature(context.getFeatureExpression());
	// markers.add(mi);
	// tmp.clear();
	// }
	// i++;
	// }
	//
	// }
	// return markers;
	// }

}
