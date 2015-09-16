package de.ovgu.variantsync.presentationlayer.view.codemapping;

import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

public class EditorListener implements ISelectionListener, IFileBufferListener,
		IPartListener2 {

	public void partOpened(IWorkbenchPartReference partRef) {
		System.out.println(partRef.getTitle());
	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		System.out.println(arg0.getTitle());
	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		System.out.println(arg0.getTitle());
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference arg0) {
	}

	@Override
	public void partClosed(IWorkbenchPartReference arg0) {
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {
	}

	@Override
	public void partHidden(IWorkbenchPartReference arg0) {
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bufferContentAboutToBeReplaced(IFileBuffer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bufferContentReplaced(IFileBuffer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bufferCreated(IFileBuffer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bufferDisposed(IFileBuffer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dirtyStateChanged(IFileBuffer arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChangeFailed(IFileBuffer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanging(IFileBuffer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateValidationChanged(IFileBuffer arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void underlyingFileDeleted(IFileBuffer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void underlyingFileMoved(IFileBuffer arg0, IPath arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}
}