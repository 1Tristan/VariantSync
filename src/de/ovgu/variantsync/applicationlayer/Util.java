package de.ovgu.variantsync.applicationlayer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;
import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaElement;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaProject;
import de.ovgu.variantsync.presentationlayer.view.codemapping.MarkerHandler;
import de.ovgu.variantsync.presentationlayer.view.codemapping.MarkerInformation;

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

	public static void getClassesWithClassName(List<JavaElement> elements,
			List<JavaClass> classes, String className) {
		if (elements != null && !elements.isEmpty()) {
			for (JavaElement je : elements) {
				if (je instanceof JavaClass && je.getName().equals(className)) {
					classes.add((JavaClass) je);
				} else {
					getClassesWithClassName(je.getChildren(), classes,
							className);
				}
			}
		}
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
