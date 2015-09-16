package de.ovgu.variantsync.presentationlayer.view.codemapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import de.ovgu.variantsync.VariantSyncPlugin;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 14.09.2015
 */
public class MarkerHandler {

	private Map<Long, MarkerInformation> markerMap;

	private static MarkerHandler instance;

	private MarkerHandler() {
		markerMap = new HashMap<Long, MarkerInformation>();
	}

	public static MarkerHandler getInstance() {
		if (instance == null) {
			instance = new MarkerHandler();
		}
		return instance;
	}

	public void addMarker(MarkerInformation mi, IMarker marker, IResource file,
			String feature) {
		CodeMarkerFactory.addAnnotation(marker, VariantSyncPlugin.getEditor(),
				CodeMarkerFactory.getFeatureColor(feature), mi.getOffset(),
				mi.getLength());
		mi.setMarkerId(marker.getId());
		markerMap.put(mi.getMarkerId(), mi);
	}

	public void removeMarker(IFile file, int start, int end) {
		List<IMarker> markers = CodeMarkerFactory.findMarkers(file);
		for (IMarker marker : markers) {
			MarkerInformation mi = markerMap.get(marker.getId());
			if (mi != null && start == mi.getStart() && end == mi.getEnd()) {
				try {
					markerMap.remove(marker.getId());
					marker.delete();
					break;
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void clearAllMarker(IFile file) {
		Job job = new RemoveMarkerJob(file);
		job.setPriority(Job.SHORT);
		job.schedule();
		// TODO clear Marker for the file that will be presented. Then, use
		// IDocument to get offset by line number for new marker
		// List<IMarker> markers = CodeMarkerFactory.findMarkers(file);
		// for (IMarker marker : markers) {
		// try {
		// markerMap.remove(marker.getId());
		// marker.delete();
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }
		// }
	}

	public void setMarker(IFile file, List<MarkerInformation> markers) {
		IDocument document = (IDocument) VariantSyncPlugin.getEditor()
				.getDocumentProvider()
				.getDocument(VariantSyncPlugin.getEditor().getEditorInput());
		int i = 0;
		for (MarkerInformation mi : markers) {
			try {
				IRegion regionStart = document
						.getLineInformation(mi.getStart() - 1);
				IRegion regionEnd = document
						.getLineInformation(mi.getEnd() - 1);
				try {
					int start = regionStart.getOffset();
					int end = regionEnd.getOffset() + 2;
					if (regionStart.getLength() == regionEnd.getLength()
							&& regionStart.getOffset() == regionEnd.getOffset()) {
						end = regionStart.getOffset() + regionEnd.getLength();
					}
					CodeMarkerFactory.createMarker(String.valueOf(i), file,
							start, end, mi.getFeature());
					// addMarker(mi, marker, file, mi.getFeature());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			i++;
		}
	}
}
