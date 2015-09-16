package de.ovgu.variantsync.presentationlayer.view.codemapping;

import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.ovgu.variantsync.VariantSyncPlugin;

public class CreateMarkerJob extends Job {

	private int start;
	private int end;
	private String feature;
	private IResource res;
	private IMarker marker;
	private String markerId;

	public CreateMarkerJob(String name) {
		super(name);
	}

	public CreateMarkerJob(String name, IResource res, int start, int end,
			String feature, String markerId) {
		super(name);
		this.res = res;
		this.start = start;
		this.end = end;
		this.feature = feature;
		// String tmp = res.getLocationURI().toString();
		// tmp = tmp.replace(".", "/");
		// tmp = tmp.replace("/java", ".java");
		// IPath path = new Path(tmp);
		// IResource r = new Resource(path,ResourcesPlugin.getWorkspace();
		this.markerId = markerId;
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		try {
			marker = null;
			marker = res.createMarker(markerId);
			marker.setAttribute(IMarker.MESSAGE, "Feature: " + feature);
			marker.setAttribute(IMarker.CHAR_START, start);
			marker.setAttribute(IMarker.CHAR_END, end);
			System.out.println("\n!!! " + start + ", " + end);
		} catch (CoreException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}

}