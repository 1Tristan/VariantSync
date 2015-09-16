package de.ovgu.variantsync.presentationlayer.view.codemapping;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

public class MarkerJob extends Job {

	private int start;
	private int end;
	private String feature;
	private IResource res;
	private IMarker marker;
	private String markerId;

	public MarkerJob(String name) {
		super(name);
	}

	public MarkerJob(String name, IResource res, int start, int end,
			String feature, String markerId) {
		super(name);
		this.res = res;
		this.start = start;
		this.end = end;
		this.feature = feature;
		this.markerId = markerId;
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		try {
			marker = null;
			// note: you use the id that is defined in your plugin.xml
			marker = res.createMarker(markerId);
			marker.setAttribute(IMarker.MESSAGE, "Feature: " + feature);
			// compute and set char start and char end
			marker.setAttribute(IMarker.CHAR_START, start);
			marker.setAttribute(IMarker.CHAR_END, end);
			System.out.println("\n!!! " + start + ", " + end);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}