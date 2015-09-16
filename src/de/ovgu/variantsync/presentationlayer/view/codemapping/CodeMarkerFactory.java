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

public class CodeMarkerFactory {

	// Marker ID
	public static final String MARKER = "de.ovgu.pfofe.variantsync.codehighlightmarker";

	// Annotation ID
	public static final String ANNOTATION_COLOR1 = "de.ovgu.pfofe.variantsync.codehighlightannotationColor1";
	public static final String ANNOTATION_COLOR2 = "de.ovgu.pfofe.variantsync.codehighlightannotationColor2";
	public static final String ANNOTATION_COLOR3 = "de.ovgu.pfofe.variantsync.codehighlightannotationColor3";

	private static Map<String, String> featureColorMap = new HashMap<String, String>();

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
			int end, String feature) throws CoreException {
		try {
			Job job = new CreateMarkerJob(feature + "§" + id, res, start, end,
					feature, MARKER);
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
		try {
			return Arrays.asList(resource.findMarkers(MARKER, true,
					IResource.DEPTH_ZERO));
		} catch (CoreException e) {
			return new ArrayList<IMarker>();
		}
	}

	/*
	 * Returns a list of markers that are linked to the resource or any sub
	 * resource of the resource
	 */
	public static List<IMarker> findAllMarkers(IResource resource) {
		try {
			return Arrays.asList(resource.findMarkers(MARKER, true,
					IResource.DEPTH_INFINITE));
		} catch (CoreException e) {
			return new ArrayList<IMarker>();
		}
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
