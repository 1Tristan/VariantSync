package de.ovgu.variantsync.presentationlayer.view.codemapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeHighlighting;

public class CodeMarkerFactory {

	// Marker ID
	private static final String MARKER_GREEN = "de.ovgu.pfofe.variantsync.codehighlightmarkerGreen";
	private static final String MARKER_BLUE = "de.ovgu.pfofe.variantsync.codehighlightmarkerBlue";
	private static final String MARKER_YELLOW = "de.ovgu.pfofe.variantsync.codehighlightmarkerYellow";

	// Annotation ID
	private static final String ANNOTATION_COLOR1 = "de.ovgu.pfofe.variantsync.codehighlightannotationColor1";
	public static final String ANNOTATION_COLOR2 = "de.ovgu.pfofe.variantsync.codehighlightannotationColor2";
	public static final String ANNOTATION_COLOR3 = "de.ovgu.pfofe.variantsync.codehighlightannotationColor3";

	private static Map<String, String> featureColorMap = new HashMap<String, String>();
	private static List<String> markers = new ArrayList<String>();

	public static void setFeatureColor(String feature, String color) {
		featureColorMap.put(feature, color);
	}

	public static String getFeatureColor(String feature) {
		String color = featureColorMap.get(feature);
		if (color == null) {
			color = "1";
		}
		return color;
	}

	/*
	 * Creates a Marker
	 */
	public static void createMarker(String id, IResource res, int start,
			int end, String feature, CodeHighlighting color)
			throws CoreException {
		try {
			Job job = new CreateMarkerJob(feature + "§" + id, res, start, end,
					feature, getMarker(color));
			job.setPriority(Job.SHORT);
			job.schedule();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * returns a list of a resources markers
	 */
	public static List<IMarker> findMarkers(IResource resource) {
		if (markers.isEmpty()) {
			markers.add(MARKER_YELLOW);
			markers.add(MARKER_GREEN);
			markers.add(MARKER_BLUE);
		}
		List<IMarker> returnList = new ArrayList<IMarker>();
		for (String marker : markers) {
			try {
				returnList.addAll(Arrays.asList(resource.findMarkers(
						marker, true, IResource.DEPTH_INFINITE)));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return returnList;
	}

	private static String getMarker(CodeHighlighting color) {
		if (color == null) {
			color = CodeHighlighting.YELLOW;
		}
		String marker = "";
		if (color.equals(CodeHighlighting.YELLOW)) {
			marker = MARKER_YELLOW;
		} else if (color.equals(CodeHighlighting.BLUE)) {
			marker = MARKER_BLUE;
		} else if (color.equals(CodeHighlighting.GREEN)) {
			marker = MARKER_GREEN;
		}
		return marker;
	}

	/*
	 * Returns a list of markers that are linked to the resource or any sub
	 * resource of the resource
	 */
	public static List<IMarker> findAllMarkers(IResource resource) {
		if (markers.isEmpty()) {
			markers.add(MARKER_YELLOW);
			markers.add(MARKER_GREEN);
			markers.add(MARKER_BLUE);
		}
		List<IMarker> returnList = new ArrayList<IMarker>();
		for (String marker : markers) {
			try {
				returnList.addAll(Arrays.asList(resource.findMarkers(marker,
						true, IResource.DEPTH_INFINITE)));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return returnList;
	}

	/*
	 * Returns the selection of the package explorer
	 */
	public static TreeSelection getTreeSelection() {

		ISelection selection = VariantSyncPlugin.getActiveWorkbenchWindow()
				.getSelectionService().getSelection();
		if (selection instanceof TreeSelection) {
			return (TreeSelection) selection;
		}
		return null;
	}

	/*
	 * Returns the selection of the active editor
	 */
	public static TextSelection getTextSelection() {

		ISelection selection = VariantSyncPlugin.getActiveWorkbenchWindow()
				.getSelectionService().getSelection();
		if (selection instanceof TextSelection) {
			return (TextSelection) selection;
		}
		return null;
	}

	public static void addAnnotation(IMarker marker, ITextEditor editor,
			String annotation, int offset, int length) {
		// The DocumentProvider enables to get the document currently loaded in
		// the editor
		IDocumentProvider idp = editor.getDocumentProvider();

		// This is the document we want to connect to. This is taken from the
		// current editor input.
		IDocument document = idp.getDocument(editor.getEditorInput());

		// The IannotationModel enables to add/remove/change annotation to a
		// Document loaded in an Editor
		IAnnotationModel iamf = idp.getAnnotationModel(editor.getEditorInput());

		// Note: The annotation type id specify that you want to create one of
		// your annotations
		String an = "";
		if (annotation.equals("1")) {
			an = ANNOTATION_COLOR1;
		} else if (annotation.equals("2")) {
			an = ANNOTATION_COLOR2;
		} else if (annotation.equals("3")) {
			an = ANNOTATION_COLOR3;
		}
		SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation(an, marker);

		// Finally add the new annotation to the model
		iamf.connect(document);
		iamf.addAnnotation(ma, new Position(offset, length));
		iamf.disconnect(document);
	}

}
